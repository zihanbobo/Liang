package com.we.dubbo;

import com.we.core.web.ReMsg;
import com.we.socket.model.JsonMsg;

public interface RpcChatBoxService {

	public ReMsg chat(JsonMsg msg);
	
	public ReMsg Login(String token) ;
}

