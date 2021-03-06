package com.we.socket.server.websocket;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 *   
 */
public class WebSocketChannelMap {

	static final Logger logger = LoggerFactory.getLogger(WebSocketChannelMap.class);
	//private final static Lock lock = new ReentrantLock();

	private static Map<Long, HashSet<Channel>> ucmap = new ConcurrentHashMap<Long, HashSet<Channel>>();// 用户SessionId，channel对应
	private static Map<Channel, Long> sessionUserMap = new ConcurrentHashMap<Channel, Long>();// sessionId,用户Id对应


	// 连接数
	public static int getChannelCnt() {
		return sessionUserMap.size();
	}

	public static Collection<Channel> getAllChannel() {
		return sessionUserMap.keySet();
	}

	public static Collection<Long> getAllUsers() {
		return sessionUserMap.values();
	}

	public static void clearUserInfo(Channel c) {
		Long uid = sessionUserMap.get(c);
		if (uid != null && uid > 0) {
			sessionUserMap.remove(c);
			HashSet<Channel> cs = ucmap.get(uid);
			if(cs!=null) {
				if(cs.size() == 1) {
					ucmap.remove(uid);
				}else {
					cs.remove(c);
					ucmap.put(uid, cs);
				}
			}
		}
	}



	public static void addUserInfo(final long uid, final Channel c) {
		HashSet<Channel> cs = getChannel(uid);
		if(cs==null){
			cs = new HashSet<Channel>();
		}
		cs.add(c);
		sessionUserMap.put(c, uid);
		ucmap.put(uid, cs);
	}




	public static HashSet<Channel> getChannel(final long uid) {
		if (uid > 0) {
			return ucmap.get(uid);
		}
		return new HashSet<Channel>();
	}


	public static long getUid(final Channel c) {
		Long uid = sessionUserMap.get(c);
		if (uid == null) {
			uid = 0L;
		}
		return uid;
	}


}
