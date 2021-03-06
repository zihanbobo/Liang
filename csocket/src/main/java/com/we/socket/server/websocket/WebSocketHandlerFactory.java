package com.we.socket.server.websocket;

import io.netty.channel.ChannelHandlerContext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.we.socket.model.JsonMsg;


public class WebSocketHandlerFactory {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandlerFactory.class);

	private WebSocketHandlerFactory() {

	}

//	private static Map<Integer, WebSocketCommandHandler> handlers = new ConcurrentHashMap<Integer, WebSocketCommandHandler>();
//	private static Map<String, Class<? extends WebSocketCommandHandler>> mp = new HashMap<String, Class<? extends WebSocketCommandHandler>>();
	static WebSocketDefHandler defHandler = new WebSocketDefHandler();
//	static {
//		mp.put(MsgType.DEFAULT.getT(), WebSocketDefHandler.class);
//	}

//	public static WebSocketCommandHandler getDefHandler() {
//		return getHandler(MsgType.DEFAULT.getT());
//	}

//	public static WebSocketCommandHandler getHandler(final String msgType) {
//		WebSocketCommandHandler h = handlers.get(msgType);
//		if (h == null) {
//			Class<? extends WebSocketCommandHandler> c = mp.get(msgType);
//			if (c == null) {
//				logger.error("command Error,no msg");
//			}
//			try {
//				h = c.newInstance();
//				handlers.put(msgType, h);
//			} catch (InstantiationException | IllegalAccessException e) {
//				logger.error("", e);
//			}
//		}
//		return h;
//	}

	public static void handler(final ChannelHandlerContext ctx,final JsonMsg msg) {
		defHandler.handler(ctx, msg);
	}

}
