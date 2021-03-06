package com.we.web.view.admin;

import java.text.ParseException;
import java.util.HashMap;
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
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.utils.DateUtil;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.service.UserWalletService;
import com.we.service.userTask.TokenPrjService;

@Controller
@RequestMapping("/admin")
public class AdminUserWalletCtl extends AdminBaseCtl {

	static final Logger log = LoggerFactory.getLogger(AdminUserWalletCtl.class);

	@Autowired
	UserWalletService userWalletService;

	@Autowired
	TokenPrjService tokenPrjService;

	/** 用户钱包列表 */
	@RequestMapping("/userWallets")
	public ModelAndView userWallets(Long _id, Integer page, Integer size, HttpServletRequest request)
			throws ParseException {
		Page<DBObject> curPage = userWalletService.queryUserWallet(_id, page, size);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", curPage);
		model.put("_id", _id);
		return new ModelAndView("admin/users/userWallets", model);
	}

	/** coinLog 相关 */

	/** coinLogs列表 */
	@RequestMapping("/coinlogs")
	public ModelAndView coinlogs(String st, String et, Integer io, Integer type, Long uid, String coinType,
			Integer page, Integer size, HttpServletRequest request) throws ParseException {
		long startDate = 0;
		long endDate = 0;
		if (StringUtils.isNotBlank(st)) {
			startDate = DateUtil.getZeroTime(DateUtil.convertDate(st, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotBlank(et)) {
			endDate = DateUtil.getZeroTime(DateUtil.convertDate(et, "yyyy-MM-dd")) + DateUtil.DAY;
		}
		Page<DBObject> curPage = userWalletService.queryCoinLog(startDate, endDate, io, type, uid, coinType, page,
				size);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("page", curPage);
		model.put("io", io);
		model.put("uid", uid);
		model.put("type", type);
		model.put("st", st);
		model.put("et", et);
		model.put("coinType", coinType);
		model.put("coinTypes", tokenPrjService.findAllCoinType(Const.STATUS_OK));
		return new ModelAndView("admin/finance/coinLogs", model);
	}

	/** 进入更改用户钱包货币信息界面 */
	@RequestMapping("/changeCoin")
	public ModelAndView changeCoin() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("coinTypes", tokenPrjService.findAllCoinType(Const.STATUS_OK));
		return new ModelAndView("admin/finance/changeCoin", model);
	}

	/** 修改用户钱包里的货币 */
	@ResponseBody
	@RequestMapping(value = "/changeCoin", method = RequestMethod.POST)
	public ReMsg coinlogs(int io, long uid, String coinType, long coin, String detail, HttpServletRequest request) {
		long adminId = userWalletService.getUid();
		if (adminId < 1) {
			return new ReMsg(ReCode.PARAMETER_ERR);
		}
		return userWalletService.adminChangeCoin(adminId, io, uid, coinType, coin, detail);
	}

}
