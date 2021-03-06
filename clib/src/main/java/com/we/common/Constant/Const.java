package com.we.common.Constant;

public class Const {

	public static final int LOGIN_ERR_MAX = 10; // 最多登录次数，超过需要等待n分钟
	public static final int LOGIN_ERR_AUTHCODE = 3;// 多次登录超过n次，出验证码
	public static final int REG_MAX_AUTHCODE = 3;// 多次注册出验证码

	/** status */
	public static final int STATUS_OK = 3;
	public static final int STATUS_PROCESSED = 2;
	public static final int STATUS_DEF = 1;
	public static final int STATUS_FAILED = -1;

	/** time */
	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;
	public static final long MONTH = 30 * DAY;
	public static final long YEAR = 365 * DAY;


	/** shareCode */
	public static final String SHARE_CODE = "shareCode";
	public static final String SHARE_CODE_SENDING = "shareCodeSending";
	
	/** defalult_link*/
	public static final String EMPTYADDR = "#";

}
