package com.zb.common.Constant;


public enum LoginType  {
	DEF(1),
	QQ(2),
	WeiXin(3),
	Weibo(4),
	GUEST(5);
    private int loginType = 0;
   
    private LoginType(int thirdLoginType){
		this.loginType = thirdLoginType;
	}

	public int getLoginType() {
		return loginType;
	}
    
    
}
