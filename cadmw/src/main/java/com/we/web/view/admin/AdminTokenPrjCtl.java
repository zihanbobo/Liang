package com.we.web.view.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.we.service.userTask.TokenPrjService;

@Controller
@RequestMapping("/admin")
public class AdminTokenPrjCtl extends AdminBaseCtl {

	static final Logger log = LoggerFactory.getLogger(AdminTokenPrjCtl.class);

	@Autowired
	TokenPrjService tokenPrjService;

	/** 所有的货币列表 */
	@RequestMapping("/coinTypes")
	public ModelAndView coinTypes(String _id, Integer status, Integer page, Integer size, HttpServletRequest request) {
		Page<DBObject> curPage = tokenPrjService.query(status, _id, page, size);
		curPage.setUrl(request.getRequestURI());
		Map<String, Object> model = super.getImgMap();
		model.put("page", curPage);
		model.put("_id", _id);
		model.put("status", status);
		return new ModelAndView("admin/coinType/coinTypes", model);
	}

	/** 进入货币信息修改界面 */
	@RequestMapping("/coinType")
	public ModelAndView coinType(String id) {
		Map<String, Object> model = super.getImgMap();
		if (StringUtils.isNotBlank(id)) {
			DBObject dbo = tokenPrjService.findById(id);
			model.put("obj", dbo);
		}
		return new ModelAndView("admin/coinType/coinType", model);
	}

	/** 修改货币信息 */
	@ResponseBody
	@RequestMapping(value = "/coinType", method = RequestMethod.POST)
	public ReMsg coinType(String _id, String tokenName, String pic, int status, int sort, String summary, String url,
			Double price, String stage, String platform, String country, Boolean candy, String coinUnit,
			Long initialAmount) {
		long adminId = tokenPrjService.getUid();
		if (adminId < 1) {
			return new ReMsg(ReCode.PARAMETER_ERR);
		}
		return new ReMsg(tokenPrjService.upset(_id, tokenName, pic, status, sort, summary, url, price, stage, platform,
				country, candy, coinUnit, initialAmount));
	}

	/** 更改货币状态 */
	@ResponseBody
	@RequestMapping("/coinType/status")
	public ReMsg status(String _id, int status) {
		long adminId = tokenPrjService.getUid();
		if (adminId < 1) {
			return new ReMsg(ReCode.PARAMETER_ERR);
		}
		return tokenPrjService.status(_id, status, adminId);
	}

	/** 获取所有的上线货币 */
	@ResponseBody
	@RequestMapping("/coinType/findCoin")
	public ReMsg findAllCoinType(Integer status) {
		return new ReMsg(ReCode.OK, tokenPrjService.findAllCoinType(status));
	}

}
