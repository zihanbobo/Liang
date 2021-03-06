package com.we.socket.server.websocket;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.we.common.Constant.ReCode;
import com.we.common.redis.RK;
import com.we.common.utils.JSONUtil;
import com.we.core.spring.SpringContextHolder;
import com.we.core.web.ReMsg;
import com.we.dubbo.RpcChatBoxService;
import com.we.socket.model.JsonMsg;
import com.we.socket.store.DBInit;
import com.we.socket.store.RedisInit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/**
 * 说明：处理器
 *
 * @author
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

	private WebSocketServerHandshaker handshaker;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long incrId(String key) {
		return (long) getChatRedis().execute(new RedisCallback() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.incr(key.getBytes());
			}
		});
	}

	public long incrMsgId() {
		return incrId(RK.imMsgId());
	}
	
	public MongoTemplate getChatDb() {
		return DBInit.getMainDb();
	}

	public StringRedisTemplate getMainRedis() {
		return RedisInit.getMainRedis();
	}

	public StringRedisTemplate getChatRedis() {
		return RedisInit.getMainRedis();
	}

	public Long getUid(String token) {
		Long uid = 0L;
		if (token != null) {
			String strUid = getMainRedis().opsForValue().get(
					RK.accessToken(token));
			if (strUid != null) {
				try {
					uid = Long.parseLong(strUid);
				} catch (Exception e) {
				}
			}
		}
		return uid;
	}
	
	public void regUser(final Channel c,final Long uid) {
		if (uid!=null && uid > 0) {
			WebSocketChannelMap.addUserInfo(uid, c);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加
		// Global.group.add(ctx.channel());
		System.out.println("客户端与服务端连接开启");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除
		WebSocketChannelMap.clearUserInfo(ctx.channel());
		System.out.println("客户端与服务端连接关闭");

	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// logger.error("::::::" + msg.toString());
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, ((FullHttpRequest) msg));
		} else if (msg instanceof WebSocketFrame) {
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame)
			throws UnsupportedEncodingException {
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			System.out.println("Close");
			WebSocketChannelMap.clearUserInfo(ctx.channel());
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			System.out.println("Ping");
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}

		ByteBuf buf = frame.content();
		byte[] b = new byte[buf.readableBytes()];
		buf.getBytes(0, b);
		String req = new String(b, "utf-8");
		
		System.out.println("服务端收到：" + req);
		JsonMsg jm = JSONUtil.jsonToBean(req, JsonMsg.class);
		WebSocketHandlerFactory.handler(ctx, jm);
		System.out.println("调用好了");
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:7397/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private static boolean isKeepAlive(FullHttpRequest req) {
		return false;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		WebSocketChannelMap.clearUserInfo(ctx.channel());
		cause.printStackTrace();
		ctx.close();
	}

}
