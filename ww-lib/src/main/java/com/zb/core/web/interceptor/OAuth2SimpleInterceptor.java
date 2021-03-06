package com.zb.core.web.interceptor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zb.common.ParamKey;
import com.zb.common.Constant.ReCode;
import com.zb.common.http.CookieUtil;
import com.zb.common.utils.JSONUtil;
import com.zb.core.web.ReMsg;
import com.zb.core.web.SimpleJsonView;
import com.zb.service.AuthService;

public class OAuth2SimpleInterceptor extends HandlerInterceptorAdapter {

	static final Logger log = LoggerFactory
			.getLogger(OAuth2SimpleInterceptor.class);

	@Autowired
	private AuthService authService;

	private static final ThreadLocal<Map<String, Object>> sessionHolder = new ThreadLocal<Map<String, Object>>();

	static final Integer ROBOT_MAX = 1023956;

	static String TOKEN = "token";

	static final String BEARER_TYPE = "bearer";

	protected AuthService getAuthService() {
		return authService;
	}

	protected static ThreadLocal<Map<String, Object>> getSessionholder() {
		return sessionHolder;
	}

	public static void setSession(Map<String, String> session) {
		sessionHolder.set((Map) session);
	}

	public static Map<String, Object> getSession() {
		Map<String, Object> map = sessionHolder.get();

		return map;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// System.out.println("TOKEN postHandle");
		sessionHolder.remove();
	}

	String[] uris = { "/api/register", "/api/registerPhone",
			"/api/checkUsername", "/api/guest", "/api/login", "/api/authcode",
			"/api/dynamicKey", "/api/sendSms" };

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException, IOException {
		String cururi = request.getRequestURI();
		for (String uri : uris) {
			if (uri.equals(cururi)) {
				return true;
			}
		}
		String token = parseToken(request); // 通过url获取地址中token信息
		if (token != null) {
			ReMsg rm = authService.getAuthUser(request, token);
			if (ReCode.OK.reCode() == rm.getCode()) {
				sessionHolder.set((Map) rm.getData());
				return true;
			} else {
				handleNotAuthorized(request, response, rm);
				return false;
			}
		}
		return true;
	}

	/**
	 * 封装状态信息，返回数据
	 * 
	 * @param request
	 * @param response
	 * @param json
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleNotAuthorized(HttpServletRequest request,
			HttpServletResponse response, ReMsg rm) throws ServletException,
			IOException {
		String json = JSONUtil.beanToJson(rm);
		String callback = request.getParameter(ParamKey.In.callback);
		if (StringUtils.isNotBlank(callback)) {
			json = callback + '(' + json + ')';
		}
		SimpleJsonView.rennderJson(json, response);
	}

	/**
	 * 解析token，通过url地址获取token
	 * 
	 * @param request
	 * @return
	 */
	public static String parseToken(HttpServletRequest request) {
		String token = request.getParameter(TOKEN);// 获取token信息
		if (token == null) {
			token = parseHeaderToken(request);// 解析头信息
		}
		return token;
	}

	/**
	 * Parse the OAuth header parameters. The parameters will be oauth-decoded.
	 *
	 * @param request
	 *            The request.
	 * @return The parsed parameters, or null if no OAuth authorization header
	 *         was supplied.
	 */
	static String parseHeaderToken(HttpServletRequest request) {
		return CookieUtil.getCookieValue("accessToken", request);
	}

}