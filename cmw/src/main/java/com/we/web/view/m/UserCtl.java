package com.we.web.view.m;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.we.common.Constant.Const;
import com.we.common.Constant.EncodeCountry;
import com.we.common.Constant.Param;
import com.we.common.Constant.ReCode;
import com.we.common.http.CookieUtil;
import com.we.common.http.UserAgentUtil;
import com.we.common.utils.DateUtil;
import com.we.common.utils.RandomUtil;
import com.we.common.utils.RegexUtil;
import com.we.core.conf.Config;
import com.we.core.web.ReMsg;
import com.we.service.AuthService;
import com.we.service.MailLogService;
import com.we.service.SmsService;
import com.we.service.UserIdentityService;
import com.we.service.UserService;
import com.we.service.UserShareService;
import com.we.service.UserWalletService;
import com.we.service.WithdrawLogService;

@Controller
public class UserCtl extends BaseCtl {

	@Autowired
	AuthService authService;

	@Autowired
	UserService userService;

	@Autowired
	SmsService smsService;

	@Autowired
	UserWalletService userWalletService;

	@Autowired
	WithdrawLogService withdrawLogService;

	@Autowired
	UserIdentityService userIdentityService;

	@Autowired
	MailLogService mailLogService;
	
	@Autowired
	UserShareService userShareService;

	/** 获取安全码 */
	@ResponseBody
	@RequestMapping(value = "/dynamicKey", method = RequestMethod.POST)
	public ReMsg getDynamicKey(String reqKey,Integer flag) {
		if(Param.REGISTER_FLAG!=flag&&Param.FORGET_FLAG!=flag) {
			return new ReMsg(ReCode.PARAMETER_ERR);
		}
		ReMsg reMsg = userService.queryUserByPhone(reqKey);
		if(Param.REGISTER_FLAG==flag && ReCode.EMPTY_PHONE_ERR.reCode()!=reMsg.getCode()) {
			return reMsg;
		}
		if(Param.FORGET_FLAG==flag && ReCode.REGISTED_PHONE_ERR.reCode()!=reMsg.getCode()) {
			return reMsg;
		}
		return authService.getKey(reqKey);
	}

	/** 请求验证码接口 */
	@ResponseBody
	@RequestMapping(value = "/sendSms", method = RequestMethod.POST)
	public ReMsg sendSmsAuthCode(String encode, String phone, long timestamp, String data, HttpServletRequest req)
			throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isBlank(encode)) {
			encode = EncodeCountry.SOUTH_KOREA.getEncode();
		}
		return smsService.sendAuthCode(req, encode, phone, timestamp, data);
	}

	/** 应该有一个跳转主页的地址 带邀请码的 邀请码存入cookie 然后注册时提取这个邀请码 */
	@RequestMapping("/i/{shareCode}")
	public ModelAndView shareCodeMainView(HttpServletRequest req, HttpServletResponse resq,
			@PathVariable String shareCode) {
		
		if (StringUtils.isNotBlank(shareCode)) {
			CookieUtil.addCookie(Const.SHARE_CODE, shareCode, 0, "/", Config.cur().get("domain"), resq);
		}
		if(UserAgentUtil.watchIsMobile(req)) {
			return new ModelAndView("guideMobile");
		}
		return new ModelAndView("guide");
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserNum", method = RequestMethod.GET)
	public String getUserNum(Long initNum){
		return userService.getUserNum(initNum);
	}

	/** 注册界面 */
	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public ModelAndView regView(String url, HttpServletRequest req) {
		Map<String, String> res = new HashMap<String, String>();
		res.put("url", url);
		return new ModelAndView("register", res);
	}

	/** 登录界面 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginView(String url, String phone, HttpServletRequest req) {
		Map<String, String> res = new HashMap<String, String>();
		res.put("url", url);
		res.put("phone", phone);
		return new ModelAndView("login", res);
	}

	/** 忘记密码界面 */
	@RequestMapping(value = "/forget")
	public ModelAndView forgetPwd(HttpServletRequest req, HttpServletResponse res) {
		return new ModelAndView("forget");
	}

	/** 退出 */
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resq) {
		authService.logout(req, resq);
		return new ModelAndView("redirect:/");
	}

	/** 登陆 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String username, String password, String url, HttpServletRequest req,
			HttpServletResponse resq) {
		ReMsg rm = authService.login(username, password, req);
		if (rm.getCode() == ReCode.OK.reCode()) {
			// cookie有效期一个月
			if (rm.getData() instanceof String) {
				authService.logout(req, resq);
				CookieUtil.addCookie("accessToken", (String) rm.getData(), (int) (Const.MONTH / 1000), "/",
						Config.cur().get("domain"), resq);
			} else {
				Map<String, String> res = (Map<String, String>) rm.getData();
				authService.logout(req, resq);
				CookieUtil.addCookie("accessToken", res.get("token"), (int) (Const.MONTH / 1000), "/",
						Config.cur().get("domain"), resq);
			}
			if (StringUtils.isNotBlank(url) && !url.startsWith("/login")) {
				return new ModelAndView("redirect:" + url);
			} else {
				return new ModelAndView("redirect:/my/personalMine");
			}  
		}
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("username", username);
		ret.put("password", password);
		ret.put("url", url);
		ret.put("msg", rm.getMsg());
		return new ModelAndView("login", ret);
	}

	/** 验证码重设密码接口 */
	@RequestMapping(value = "/forget", method = RequestMethod.POST)
	public ModelAndView upSetpwd(String encode, String url, String phone, String code, String password,
			HttpServletRequest req,HttpServletResponse resq) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isBlank(encode)) {
			encode = EncodeCountry.SOUTH_KOREA.getEncode();
		}
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("code", code);
		ret.put("phone", phone);
		ret.put("url", url);
		if (!RegexUtil.isPhoneChina(phone) && !RegexUtil.isPhoneKorea(phone)) {
			ret.put("msg", ReCode.PHONE_NUMBER_FORMAT_ERROR.getMsg());
			return new ModelAndView("forget", ret);
		}
		if (!RegexUtil.isPassword(password)) {
			ret.put("msg", ReCode.PASSWORD_ERR.getMsg());
			return new ModelAndView("forget", ret);
		}
		if (StringUtils.isBlank(code)) {
			ret.put("msg", ReCode.AUTHCODE_ERR.getMsg());
			return new ModelAndView("forget", ret);
		}
		// 更改密码
		ReMsg rm = userService.upsetPwd(phone, code, password, req);
		if (rm.getCode() == ReCode.OK.reCode()) {
			// 进入登陆界面
			authService.logout(req, resq);
			return this.loginView(url, phone, req);
		}
		// 重设密码时出现问题 回到忘记密码界面
		ret.put("msg", rm.getMsg());
		return new ModelAndView("forget", ret);
	}

	/** 注册用户 国家编码 手机号 密码 验证码 邀请码 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerPhone(String url, String encode, String phone, String password, String code,
			boolean checkbox,Integer flag,HttpServletRequest req, HttpServletResponse resq) {
		String redictStr = "register";
		if(Param.SHARE_PAGE_FLAG_WEB==flag) {
			redictStr="guide";
		}
		if(Param.SHARE_PAGE_FLAG_MOBILE==flag) {
			redictStr="guideMobile";
		}
		Map<String, String> ret = new HashMap<String, String>();
		if(!checkbox) {
			ret.put("msg", ReCode.NOT_SELECTION_AGREEMENT.getMsg());
			return new ModelAndView(redictStr, ret);
		}
		if (StringUtils.isBlank(encode)) {
			encode = EncodeCountry.SOUTH_KOREA.getEncode();
		}
		ret.put("encode", encode);
		ret.put("phone", phone);
		ret.put("code", code);
		ret.put("url", url);
		if (!RegexUtil.isPhoneChina(phone) && !RegexUtil.isPhoneKorea(phone)) {
			ret.put("msg", ReCode.PHONE_NUMBER_FORMAT_ERROR.getMsg());
			return new ModelAndView(redictStr, ret);
		}
		if (!RegexUtil.isPassword(password)) {
			ret.put("msg", ReCode.PASSWORD_ERR.getMsg());
			return new ModelAndView(redictStr, ret);	
		}
		// 从cookie中取到邀请码
		String shareCode = CookieUtil.getCookieValue(Const.SHARE_CODE, req);
		// 注册
		ReMsg rm = userService.reg(encode, phone, password, code, shareCode, req);
		// 注册成功 执行登陆
		if (rm.getCode() == ReCode.OK.reCode()) {
			return this.login(phone, password, url, req, resq);
		}
		// 注册时出现问题 返回msg信息
		ret.put("msg", rm.getMsg());
		return new ModelAndView(redictStr, ret);
	}

	// 更改绑定邮箱
	@RequestMapping(value = "/validEmail/{md5code}", method = RequestMethod.GET)
	public ModelAndView updatePassedEmail(HttpServletRequest req, @PathVariable(value = "md5code") String md5code) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("msg", mailLogService.validEmail(md5code));
		return new ModelAndView("personal/setting-email-callback", res);
	}
	//协议页面
	@RequestMapping(value = "agreement", method = RequestMethod.GET)
	public ModelAndView aboutaGreement(HttpServletRequest req) {
		return new ModelAndView("agreement");
	}
	//404
	@RequestMapping(value = "404", method = RequestMethod.GET)
	public ModelAndView error404(HttpServletRequest req) {
		return new ModelAndView("404");
	}
	
	/** 邀请好友界面 分享文字*/
	@RequestMapping(value = "/share/invitingFriends")
	public ModelAndView InvitingFriends(HttpServletRequest req,Integer conNum,String conCode) {
		long uid = super.getUid();
		if (uid < 1) {
			if(conNum!=null&&conNum>0) {
				Map<String, Object> res = new HashMap<String, Object>();
				res.put("conNum", conNum);
				res.put("conCode", conCode);
				return new ModelAndView("personal/inviting-friends",res);
			}
			return new ModelAndView("redirect:/login");
		}
		Map<String, Object> res = super.getTokenUser();
		String shareCode = "";
		if (res.containsKey("shareCode")) {
			shareCode = String.valueOf(res.get("shareCode"));
		}
		// 用户邀请网络
		res.put("userShare", userShareService.findById(uid));
		String contextPath = req.getContextPath();
		if (StringUtils.isNotEmpty(contextPath))
			contextPath += "/";
		res.put("shareCode", shareCode != null ? shareCode : "");
		res.put("shareUrl", "https://www.candy.club/i/" + shareCode);
		return new ModelAndView("personal/inviting-friends", res);
	}
	
}
