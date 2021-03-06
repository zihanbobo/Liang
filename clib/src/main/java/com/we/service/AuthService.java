package com.we.service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.LoginType;
import com.we.common.Constant.ReCode;
import com.we.common.crypto.MDUtil;
import com.we.common.http.CookieUtil;
import com.we.common.http.UserAgent;
import com.we.common.http.UserAgentUtil;
import com.we.common.mongo.DboUtil;
import com.we.common.redis.RK;
import com.we.common.utils.AddressComponent;
import com.we.common.utils.DateUtil;
import com.we.common.utils.LbsUtil;
import com.we.common.utils.RegexUtil;
import com.we.common.utils.T2TUtil;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.LoginLog;
import com.we.models.User;

@Service
public class AuthService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	UserService userService;

	@Autowired
	SmsService smsService;

	Random r = new Random();

	public ReMsg isLogin(String token, String lbs, HttpServletRequest req) {
		if (isBanned(req)) {// 判断有效设备
			return new ReMsg(ReCode.BANNED);
		}
		ValueOperations<String, String> opsv = getRedisTemplate().opsForValue();
		String s = opsv.get(RK.accessToken(token));
		if (StringUtils.isNotBlank(s)) {
			Long uid = T2TUtil.str2Long(s);
			if (uid > 0) {
				opsv.set(RK.accessToken(token), s, 10, TimeUnit.DAYS);
				loginLog(uid, true, req, true, 0, lbs);
				BasicDBObject u = new BasicDBObject();
				try {
					AddressComponent ac = null;
					if (super.getUser("province") == null) {
						if (StringUtils.isNotBlank(lbs)) {
							u.put("lbs", lbs);
							ac = LbsUtil.getLocationLbs(lbs);
						} else {
							ac = LbsUtil.getIpLbs(req);
						}
						if (ac != null) {
							u.append("province", LbsUtil.getFmtProvince(ac)).append("city", LbsUtil.getFmtCity(ac));
							userService.update(uid, u);
						}
					}
					if (null != ac) {
						final AddressComponent nac = ac;
						CompletableFuture.runAsync(() -> {
							LbsUtil.updateLocation(nac.getX(), nac.getY(), uid, System.currentTimeMillis(),
									(long) super.getUser("sex"));
						});
					}
				} catch (Exception e) {
					log.error("更新lbs失败");
				}
				 return new ReMsg(ReCode.OK);
			}
		}
		return new ReMsg(ReCode.ACCESS_TOKEN_ERR);
	}

	public ReMsg login(String username, String pwd, HttpServletRequest req) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(pwd)) {
			return new ReMsg(ReCode.PARAMETER_ERR);
		}
//		if (isBanned(req)) {// 判断有效设备
//			return new ReMsg(ReCode.BANNED);
//		}
		ValueOperations<String, String> opsv = getRedisTemplate().opsForValue();
		String sc = opsv.get(RK.loginFailedCount(username));
		if (StringUtils.isNotBlank(sc)) {
			int c = Integer.valueOf(sc);
			if (c > Const.LOGIN_ERR_MAX) {// 大于最大登录次数
				return new ReMsg(ReCode.LOGIN_FAILED_ERR_TOO_OFTEN);
			} 
//			else if (c > Const.LOGIN_ERR_AUTHCODE) {// 验证码验证
//				if (this.codeVeri(req)) {
//					return new ReMsg(ReCode.AUTHCODE_ERR, super.authcodeImage(req));
//				}
//			}
		}
		DBObject dbo = null;
		if (RegexUtil.isPhoneChina(username)||RegexUtil.isPhoneKorea(username)) {
			dbo = userService.findByPhone(username);
		} else {
			dbo = userService.findByUsername(username);
		}
		if (dbo == null) {
			return new ReMsg(ReCode.LOGIN_FAILED_ERR_USERNAME);
		} else {
			if (MDUtil.SHA.digest2HEX(pwd).equals(DboUtil.getString(dbo, "pwd"))) {
				int loginType = LoginType.DEF.getLoginType();
				if(UserAgentUtil.watchIsMobile(req))loginType=LoginType.APP.getLoginType();
				return loginOk(loginType, req, dbo);
			} else {
				Long c = opsv.increment(RK.loginFailedCount(username), 1);
//				if (c > Const.LOGIN_ERR_AUTHCODE) {// 登录3次失败，添加验证码逻辑
//					return new ReMsg(ReCode.LOGIN_FAILED_ERR_PASSWORD, super.authcodeImage(req));
//				}
				getRedisTemplate().expire(RK.loginFailedCount(username), 5, TimeUnit.MINUTES);

				loginLog(Long.parseLong(dbo.get("_id").toString()), false, req, false, LoginType.DEF.getLoginType(),
						null);
				return new ReMsg(ReCode.LOGIN_FAILED_ERR_PASSWORD);
			}
		}
	}

	private ReMsg loginOk(int loginType, HttpServletRequest req, DBObject dbo) {
		if (!validUser(dbo)) {// 判断账户是否被禁用
			return new ReMsg(ReCode.LOGIN_NOT_ALLOWED);
		}
		String acccode = MDUtil.SHA.digest2HEX((new ObjectId()).toString() + "BibiZdbNb");
		ValueOperations<String, String> opsv = getRedisTemplate().opsForValue();
		opsv.set(RK.accessToken(acccode), dbo.get("_id").toString(), 10, TimeUnit.DAYS);
		loginLog(DboUtil.getLong(dbo, "_id"), true, req, false, loginType, null);
		return new ReMsg(ReCode.OK, acccode);
	}

	public ReMsg checkUsername(String username, HttpServletRequest req) {
		String clientId = super.getClientId(req);
		ValueOperations<String, String> opsv = getRedisTemplate().opsForValue();
		String ccnt = opsv.get(RK.regClientCount(clientId));
		int cnt = 0;
		if (StringUtils.isNotBlank(ccnt)) {
			cnt = Integer.parseInt(ccnt);
		}
		DBObject dbo = userService.findByUsername(username);
		if (cnt > Const.REG_MAX_AUTHCODE) {
			if (null != dbo) {
				return new ReMsg(ReCode.USERNAME_EXISTS, super.authcodeImage(req));
			}
			return new ReMsg(ReCode.OK, super.authcodeImage(req));
		} else {
			if (null != dbo) {
				return new ReMsg(ReCode.USERNAME_EXISTS);
			}
			return new ReMsg(ReCode.OK);
		}
	}


	public ReMsg forgot(String phone, String pwd, String code, HttpServletRequest req)
			throws JsonParseException, JsonMappingException, NumberFormatException, IOException {
		if (!smsService.validCode(phone, code)) {
			return new ReMsg(ReCode.AUTHCODE_ERR);
		}
		DBObject dbo = userService.findByPhone(phone);
		if (null == dbo) {
			return new ReMsg(ReCode.USER_NOT_EXISTS);
		}
		User user = DboUtil.toBean(dbo, User.class);
		ReMsg rc = userService.updatePwd(user.get_id(), pwd);
		if (ReCode.OK.reCode() == rc.getCode()) {
			return loginOk(LoginType.DEF.getLoginType(), req, DboUtil.beanToDBO(user));
		}
		return rc;
	}

	public boolean isBanned(HttpServletRequest req) {
		return isBanned(getDevId(req));
	}

	public boolean isBanned(String devId) {
		return getRedisTemplate().hasKey(RK.blackDev(devId));
	}

	public void removeToken(String acctoken) {
		getRedisTemplate().delete(RK.accessToken(acctoken));
	}
	
	public void logout(HttpServletRequest req,  HttpServletResponse resq) {
		String token = super.getToken(req);
		if(token==null) {
			token = CookieUtil.getCookieValue("accessToken", req);
			CookieUtil.removeCookie("accessToken", req, resq);
			if(token==null) {
				token= req.getHeader("accessToken");
			}
		}
		
		if(token!=null) {
			this.removeToken(token);// 删除用户key
		}
		
	}

	public ReMsg getAuthUser(HttpServletRequest request, String token) {
		String devId = this.getDevId(request);
		if (this.isBanned(devId)) { // 封掉客户端 uid
			this.removeToken(token);// 删除用户key
			return new ReMsg(ReCode.BANNED);
		}
		Long uid = getUid(token);
		if (uid == 0) {// 用户登录失效
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		} else {
			DBObject user = userService.findById(uid);
			if (!validUser(user)) {
				this.removeToken(token);// 删除用户key
				return new ReMsg(ReCode.LOGIN_NOT_ALLOWED);
			}
			return new ReMsg(ReCode.OK, user.toMap());
		}
	}

	// 记录登录日志
	public boolean loginLog(long userId, boolean status, HttpServletRequest req, boolean isAuto, int loginType,
			String lbs) {
		int day = DateUtil.curDay();
		if (status && isAuto) {
			long count = super.getC(DocName.LOGIN_LOG)
					.count((new BasicDBObject("userId", userId)).append("status", status).append("day", day));
			if (count > 0) {
				return true;
			}
		}
		String ip = getIp(req);
		UserAgent ua = UserAgentUtil.getUserAgent(req);
		String devId = this.getDevId(req);
		Double version = this.getVer(req);
		int via = getVia(req);
		long now = System.currentTimeMillis();
		LoginLog ll = new LoginLog(super.getNextId(DocName.LOGIN_LOG), userId, day, ip, now, status, ua, devId, version,
				via, loginType, lbs);
		super.getMongoTemplate().save(ll);
		return true;
	}

	private boolean validUser(DBObject user) {
		if (null == user) {
			return false;
		}
		if (null != user.get("status") && "1".equals(user.get("status").toString())) {
			return true;
		}
		return false;
	}

}
