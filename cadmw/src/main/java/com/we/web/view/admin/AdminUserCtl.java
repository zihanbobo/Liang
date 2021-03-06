package com.we.web.view.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DBObject;
import com.we.common.Constant.ReCode;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminUserCtl extends AdminBaseCtl {

	static final Logger log = LoggerFactory.getLogger(AdminUserCtl.class);

	@Autowired
	UserService userService;

	@RequestMapping("/users")
	public ModelAndView resPacks(Long userId, Integer role, Integer status, Integer page, Integer size, String phone,
			String nickname, HttpServletRequest request) {
		Page<DBObject> curPage = userService.queryUser(userId, status, role, phone, nickname, page, size);
		curPage.setUrl(request.getRequestURI());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", curPage);
		model.put("status", status);
		model.put("role", role);
		model.put("userId", userId);
		model.put("phone", phone);
		model.put("nickname", nickname);
		return new ModelAndView("admin/users/users", model);
	}

	@RequestMapping("/user")
	public ModelAndView pack(Long id) {
		DBObject dbo = userService.findById(id);
		return new ModelAndView("admin/users/user", "obj", dbo);
	}

	@ResponseBody
	@RequestMapping(value = "/user/info", method = RequestMethod.POST)
	public ReMsg setInfo(Long uid, String firstname, String lastname, String phone, Integer status, Integer sex,
			Integer role, String ethAddr, String email) {
		return userService.admUpsetUserInfo(uid, firstname, lastname, phone, status, sex, role, ethAddr, email);
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/info")
	public ReMsg getUser(Long uid) {
		return new ReMsg(ReCode.OK, userService.findByIdSafe(uid));
	}

	@ResponseBody
	@RequestMapping(value = "/user/setRole", method = RequestMethod.POST)
	public ReMsg setRole(Long uid, Integer role) {
		return userService.setRole(uid, role);
	}

	@ResponseBody
	@RequestMapping(value = "/user/setStatus", method = RequestMethod.POST)
	public ReMsg setStatus(Long uid, Integer status) {
		return userService.setStatus(uid, status);
	}

	@ResponseBody
	@RequestMapping(value = "/user/pwd", method = RequestMethod.POST)
	public ReMsg setPwd(Long uid, String newPwd) {
		return userService.updatePwd(uid, newPwd);
	}

	@ResponseBody
	@RequestMapping(value = "/user/setEthAddr", method = RequestMethod.POST)
	public ReMsg setEthAddr(Long uid, String ethAddr) {
		return userService.setEthAddr(uid, ethAddr);
	}

	@ResponseBody
	@RequestMapping(value = "/user/setFirstlastname", method = RequestMethod.POST)
	public ReMsg setFirstlastname(Long uid, String firstname, String lastname) {
		return userService.setFirstlastname(uid, firstname, lastname);
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/addDefineUser", method = RequestMethod.POST)
	public ReMsg addDefineUser(String encode,String phone,String password,String shareCode,HttpServletRequest req) {
		if(getUid()>0) {
			return userService.reg(encode,phone, password, shareCode, req);
		}
		return new ReMsg(ReCode.NOT_AUTHORIZED);
	}
	
	@RequestMapping("/user/add")
	public ModelAndView userAdd() {
		return new ModelAndView("admin/users/userAdd");
	}

}
