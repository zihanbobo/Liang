package com.we.web.view.m;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DBObject;
import com.we.common.Constant.ReCode;
import com.we.common.mongo.DboUtil;
import com.we.common.utils.JSONUtil;
import com.we.common.utils.MapUtil;
import com.we.common.utils.RegexUtil;
import com.we.common.utils.T2TUtil;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.division.Division;
import com.we.service.AuthService;
import com.we.service.BusinessCooperationService;
import com.we.service.LoginLogService;
import com.we.service.MailLogService;
import com.we.service.SignService;
import com.we.service.UserDivisionService;
import com.we.service.UserIdentityService;
import com.we.service.UserService;
import com.we.service.UserShareService;
import com.we.service.UserWalletService;
import com.we.service.cloud.StorageService;
import com.we.service.image.PosterService;
import com.we.service.userTask.UserTaskService;

@Controller
@RequestMapping("/app/my")
public class MyAppCtl extends BaseCtl {

	@Autowired
	AuthService authService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	UserTaskService userTaskService;
	
	@Autowired
	UserDivisionService userDivisionService;

	@Autowired
	UserWalletService userWalletService;
	
	@Autowired
	UserShareService userShareService;
	
	@Autowired
	SignService signService;
	
	@Autowired
	PosterService posterService;
	
	@Autowired
	UserIdentityService userIdentityService;
	
	@Autowired
	MailLogService mailLogService;
	
	@Autowired
	LoginLogService loginLogService;
	
	@Autowired
	BusinessCooperationService businessCooperationService;
	
	/** 个人设置界面 */
	@ResponseBody
	@RequestMapping(value = "/settingPersonal", method = RequestMethod.GET)
	public ReMsg settingPersonal(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		// 用户信息
		DBObject dbo =userService.findByIdSafe(uid);
		if(null==dbo) {
			return new ReMsg(ReCode.USER_NOT_EXISTS);
		}
		return new ReMsg(ReCode.OK,dbo);
	}
	
	/** 上传个人头像 */
	@ResponseBody
	@RequestMapping(value = "/updatePersonalImg", method = RequestMethod.POST)
	public ReMsg updatePersonalImg(@RequestParam MultipartFile file, HttpServletRequest req) {
		return userService.updatePersonPhoto(file);
	}
	
	/** 上传 */
	@ResponseBody
	@RequestMapping(value = "/uploadImg")
	public ReMsg uploadImg(@RequestParam MultipartFile file, HttpServletRequest req) {
		return storageService.uploadMyImg(file, null, null);
	}
	

	/** 任务查看上传图片 */
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg uploadView(Long id, HttpServletRequest req) {
		DBObject dbo = userTaskService.findById(id);
		Map<String, Object> res = super.getCommonMap();
		res.put("obj", dbo);
		return new ReMsg(ReCode.OK,res);
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateSettingPersonal", method = RequestMethod.POST)
	public ReMsg updateSettingPersonal(HttpServletRequest req, String username, Integer sex, String photo) {
		if (StringUtils.isEmpty(username)) {
			return new ReMsg(ReCode.NICKNAME_EMP_ERR);
		}
		if (StringUtils.isNotEmpty(username) && username.length() > 20) {
			return new ReMsg(ReCode.NICKNAME_PATTAN_ERR);
		}
		return userService.upsetPersonInfo(username, sex, photo);

	}
	
	/** 任务上传图片 */
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ReMsg upload(String photos, Long id) {
		String[] pics = null;
		if (StringUtils.isNotBlank(photos)) {
			pics = photos.split(",");
		} else {
			// 没有图片
			return new ReMsg(ReCode.FAIL);
		}
		return userTaskService.upload(id, pics);
	}
	
	/** 用户点击做任务 决定要下一步往哪儿走 */
	@ResponseBody
	@RequestMapping(value = "/doTask", method = RequestMethod.POST)
	public ReMsg doTask(long taskId) {
		return userTaskService.doTask(taskId);
	}
	
	/** 用户领取奖励 */
	@ResponseBody
	@RequestMapping(value = "/taskRewards", method = RequestMethod.POST)
	public ReMsg taskRewards(Long utid) {
		long uid = super.getUid();
		return userTaskService.recvReward(uid, utid);
	}
	
	/** 个人中心主界面 */
	@RequestMapping(value = "/personalMine", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("deprecation")
	public ReMsg personalMine(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		Map<String, Object> res = super.getCommonMap();
		// 用户信息
		DBObject dbo = userService.findById(uid);
		Division division =Division.of(DboUtil.getInt(dbo, "divisionId")!=null?DboUtil.getInt(dbo, "divisionId"):0);
		res.put("divisionName", division.getDes());
		res.put("divisionId",division.getCode());
		res.put("user", dbo);

		res.put("walletsAmount", userWalletService.getMyUserCoinsAmount(uid));
		
		res.put("todayAmount", userWalletService.getTodayCoinIn(uid));
		DBObject userShare = userShareService.findById(uid);
		int downDount=0;   
		if(userShare!=null) {
			downDount=T2TUtil.str2Int(DboUtil.getString(userShare, "m1"), 0)+
			T2TUtil.str2Int(DboUtil.getString(userShare, "m2"), 0)+
			T2TUtil.str2Int(DboUtil.getString(userShare, "m3"), 0);
		}
		res.put("downCount",downDount);
		return new ReMsg(ReCode.OK,res);
	}
	
	/** 我的个人任务 */
	@RequestMapping(value = "/personalTask", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg personalTask(HttpServletRequest req) {
		long uid = super.getUid();
		if(uid<0)return new ReMsg(ReCode.NOT_AUTHORIZED);
		Map<String, Object> res =new HashMap<String, Object>();
		// 签到情况
		res.put("sign", signService.isSign(uid));
		res.put("signReward", SignService.reward);
		res.put("myDivisonTask", userDivisionService.getUserDivisionList(uid, 3,2));
		return new ReMsg(ReCode.OK,res);
	}
	
	//签到
	@ResponseBody
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public ReMsg sign(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.AUTHCODE_ERR);
		}
		ReMsg reMsg = signService.sign(uid);
		if (0 == reMsg.getCode()) {
			DBObject dbo=signService.isSign(uid);
			dbo.put("signReward",SignService.reward);
			reMsg.setData(dbo);
		}
		return reMsg;
	}
	
	//领奖励
	@ResponseBody
	@RequestMapping(value = "/takeReward", method = RequestMethod.POST)
	public ReMsg updateTakeReward(@RequestParam(value = "taskCode", required = true) Integer taskCode) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.FAIL);
		}
		ReMsg reMsg= userDivisionService.recvReword(uid, taskCode);
		@SuppressWarnings("unchecked")
		Map<String, Object> result =(Map<String, Object>)reMsg.getData();
		if(result.containsKey("newDivId")&&result.containsKey("befDivId")) {
			Integer divId =(Integer)result.get("newDivId");
			result.put("befRate", Division.of((Integer)(result.get("befDivId"))).getRate()*100);
			result.put("curRate", Division.of(divId).getRate()*100);
			result.put("amount", Division.of(divId).getAmount());
			result.put("name", Division.of(divId).getDes());
		}
		return new ReMsg(ReCode.OK, result);
	}
	
	
	/** 我的等级 界面 */
	@RequestMapping(value = "/invitingLevel", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg invitingLevel(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		DBObject dbo = userService.findById(uid);
		int divisionId =DboUtil.getInt(dbo, "divisionId");
		Map<String, Object> res = super.getCommonMap();

		res.put("divisonInfos", userDivisionService.getAppDivisionValues());
		
		// 用户段位信息
		res.put("userDivisionId", divisionId);

		return new ReMsg(ReCode.OK,res);
	}
	
	/** 我的钱包界面 */
	@RequestMapping(value = "/wallet", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg wallet(HttpServletRequest req) {
		long uid = super.getUid();
		Map<String, Object> res = super.getCommonMap();

		res.put("objs", userWalletService.queryUserAllCoins(uid));

		return new ReMsg(ReCode.OK,res);
	}
	/** 账户详细界面 */
	@RequestMapping(value = "/accountDetails", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg accountDetails(HttpServletRequest req, Integer page,Integer size, String coinType) {
		long uid = super.getUid();
		// 用户信息
		Page<DBObject> pageObj = userWalletService.queryCoinLogByUid(uid, page, coinType, size);
		return new ReMsg(ReCode.OK, JSONUtil.beanToMap(pageObj));
	}
	
	/** 我的下线 界面 */
	@RequestMapping(value = "/invitingFriendDownline", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg invitingFriendDownline(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		Map<String, Object> res = super.getCommonMap();
		//分享下级信息
		res.putAll(userShareService.findShareIncludeUser(uid)); 
		res.put("divisonInfos", userDivisionService.getAppDivisionValues());
		return new ReMsg(ReCode.OK,res);
	}
	
	/** 获取分享图片 */
	@ResponseBody
	@RequestMapping(value = "/sharePic", method = RequestMethod.GET)
	public ReMsg myShare(HttpServletRequest req) {
		Map<String, Object> user = super.getTokenUser();
		String code = MapUtil.getStr(user, "shareCode");
		Map<String, Object> res = super.getCommonMap();
		res.put("shareCode", code);
		try {
			List<String> pics = posterService.createPoster(code);
			for (int i = 0; i < pics.size(); i++) {
				res.put("poster" + i, pics.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ReMsg(ReCode.OK, res);
	}
	
	/** 海报. */
	@RequestMapping(value = "/downloadPoster")
	public ReMsg downloadPoster(HttpServletRequest req, HttpServletResponse res, String picAddr) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.FAIL);
		}
		String fileName = "sharePoster.jpg";
		storageService.downloadPoster(fileName,picAddr,req, res);
		return null;
	}
	
	/**
	 * 更改个人昵称
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePersonUsername", method = RequestMethod.POST)
	public ReMsg updatePersonUsername(HttpServletRequest req, String username) {
		return userService.updatePersonUsername(username);
	}
	/**
	 * 更改个人性别
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePersonSex", method = RequestMethod.POST)
	public ReMsg updatePersonSex(HttpServletRequest req, Integer sex) {
		if (sex!=null&&(sex==1||sex==2)) {
			return userService.updatePersonSex(sex);
		}
		
		return new ReMsg(ReCode.FAIL);

	}
	
	// 更改密码
	@ResponseBody
	@RequestMapping(value = "/updateOriginalPwd", method = RequestMethod.POST)
	public ReMsg updateOriginalPwd(HttpServletRequest req, HttpServletResponse resq,
			@RequestParam(value = "oldPwd", required = true) String oldPwd,
			@RequestParam(value = "newPwd", required = true) String newPwd) {
		if (newPwd != null && (newPwd.length() < 6)) {
			return new ReMsg(ReCode.PASSWORD_LENGTH_ERR);
		}
		if (RegexUtil.isNumber(newPwd)) {
			return new ReMsg(ReCode.PASSWORD_LOWERCASE_ERR);
		}
		ReMsg reMsg = userService.upsetPwd(oldPwd, newPwd, req);
		if (0 == reMsg.getCode()) {
			authService.logout(req, resq);
		}
		return reMsg;
	}
	
	// 异步检验原密码
	@ResponseBody
	@RequestMapping(value = "/getOriginalPwd", method = RequestMethod.GET)
	public ReMsg getOriginalPwd(HttpServletRequest req, HttpServletResponse resq,
			@RequestParam(value = "oldPwd", required = true) String oldPwd) {
		if (oldPwd != null && oldPwd.length() < 6) {
			return new ReMsg(ReCode.OLDPWD_WRITE_ERR);
		}
		return userService.getOriginalPwd(oldPwd);
	}
	
	/**账号设置界面 */
	@RequestMapping(value = "/settingAccount",method =RequestMethod.GET)
	@ResponseBody
	public ReMsg settingAccount(HttpServletRequest req) {
		long uid = super.getUid();
		Map<String, Object> res = super.getCommonMap();
		// 用户信息
		DBObject userDbo = userService.findById(uid);
		res.put("user", userDbo);

		res.put("userIdentity", userIdentityService.findById(uid));

		res.putAll(mailLogService.queryUserMailLogStatus(uid, userDbo));

		return new ReMsg(ReCode.OK,res);
	}
	
	/** 账号登陆记录 */
	@RequestMapping(value = "/getAccountLoginRecord",method =RequestMethod.GET)
	@ResponseBody
	public ReMsg getAccountLoginRecord(HttpServletRequest req) {
		long uid = super.getUid();
		Map<String, Object> res = super.getCommonMap();

		res.put("hisrecords", loginLogService.queryLastLogByUser(uid, 10));

		return new ReMsg(ReCode.OK,res);
	}
	
	/** 绑定邮箱界面 */
	@RequestMapping(value = "/settingEmail", method = RequestMethod.GET)
	@ResponseBody
	public ReMsg settingEmail(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		Map<String, Object> res = super.getCommonMap();
		// 用户信息
		DBObject userDbo = userService.findById(uid);
		res.put("user", userDbo);
		res.putAll(mailLogService.queryUserMailLogStatus(uid, userDbo));
		return new ReMsg(ReCode.OK,res);
	}
	
	// 发送邮箱
	@ResponseBody
	@RequestMapping(value = "/updateOriginalEmail", method = RequestMethod.POST)	
	public ReMsg updateOriginalEmail(HttpServletRequest req,
			@RequestParam(value = "email", required = true) String email) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}

		return mailLogService.sendMailToUser(req, email, uid);
	}
	
	/** KYC认证界面 */
	@ResponseBody
	@RequestMapping(value = "/settingkyc",method =RequestMethod.GET)
	public ReMsg settingKYC(HttpServletRequest req) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		Map<String, Object> res = super.getCommonMap();
		// 用户信息
		res.put("userIdentity", userIdentityService.findById(uid));

		return new ReMsg(ReCode.OK,res);
	}
	
	/** 更改KYC认证 */
	@ResponseBody
	@RequestMapping(value = "/updateSettingkyc",method=RequestMethod.POST)
	public ReMsg updateSettingkyc(HttpServletRequest req, @RequestParam(value = "frontPic") String frontPic,
			@RequestParam(value = "backPic", required = false) String backPic,
			@RequestParam(value = "inHandPic") String inHandPic) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		Map<String, Object> res = super.getCommonMap();

		return userIdentityService.updateKYC(uid, frontPic, backPic, inHandPic, String.valueOf(res.get("username")));
	}
	
	//商务合作页
	@ResponseBody
	@RequestMapping(value = "/applyForBusiness")
	public ReMsg applyForBusiness(String name, String introduction, String officialAddr, String contact, String phone,
			String contactInfo, String email, String cooperation) {
		long uid = super.getUid();
		return businessCooperationService.saveBusinessCooperation(uid, name, introduction, officialAddr, contact, phone,
				contactInfo, email, cooperation);
	}
	
}
