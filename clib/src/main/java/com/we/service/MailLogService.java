package com.we.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.crypto.MDUtil;
import com.we.common.mongo.DboUtil;
import com.we.common.utils.MailUtil;
import com.we.common.utils.MailUtil.Type;
import com.we.common.utils.RandomUtil;
import com.we.common.utils.RegexUtil;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.MailLog;
import com.we.models.division.DivisionTask.DivisionTaskType;

@Service
public class MailLogService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(MailLogService.class);

	@Autowired
	UserService userService;
	
	@Autowired
	UserDivisionService userDivisionService;

	public ReMsg saveMailLog(String mailNo, String code, Long uid, String ip) {
		long id = super.getNextId(DocName.MAIL_LOG);
		MailLog mailLog = new MailLog(id, mailNo, code, uid, ip);
		super.getMongoTemplate().save(mailLog);
		return new ReMsg(ReCode.OK);
	}

	public DBObject findByCode(String code) {
		return getC(DocName.MAIL_LOG).findOne(new BasicDBObject("code", code));
	}

	public DBObject findByUidTime(Long uid) {
		List<DBObject> list = getC(DocName.MAIL_LOG).find(new BasicDBObject("uid", uid))
				.sort(new BasicDBObject("updateTime", -1)).toArray();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public ReMsg validEmail(String code) {
		DBObject mldbo = this.findByCode(code);
		if (mldbo != null) {
			MailLog ml = DboUtil.toBean(mldbo, MailLog.class);
			if (ml.getStatus() == Const.STATUS_DEF && System.currentTimeMillis() - ml.getUpdateTime() < Const.DAY) {
				userService.updateMailPass(ml.getUid(), ml.getMailNo());
				super.getC(DocName.MAIL_LOG).update(new BasicDBObject("_id", ml.get_id()),
						new BasicDBObject("$set", new BasicDBObject("status", Const.STATUS_OK)));
				userDivisionService.doDivisionTask(ml.getUid(), DivisionTaskType.EMAIL, 1);
				return new ReMsg(ReCode.OK);
			}
		}
		return new ReMsg(ReCode.FAIL);
	}

	public ReMsg updateMailStatus(String code, Integer status, Integer sendStatus) {
		DBObject u = new BasicDBObject("updateTime", System.currentTimeMillis());
		if (status != null) {
			u.put("status", status);
		}
		if (sendStatus != null) {
			u.put("sendStatus", sendStatus);
		}
		if (getC(DocName.MAIL_LOG).update(new BasicDBObject("code", code), new BasicDBObject("$set", u), false, false,
				WriteConcern.ACKNOWLEDGED).getN() > 0) {
			return new ReMsg(ReCode.OK);
		}
		return new ReMsg(ReCode.FAIL);
	}

	public ReMsg sendMailToUser(HttpServletRequest req, String mailNo, Long uid) {
		if (!RegexUtil.isEmail(mailNo)) {
			return new ReMsg(ReCode.EMAIL_PATTERN_ERR);
		}
		if (userService.findByEmail(mailNo) != null) {
			return new ReMsg(ReCode.Mail_REGED_ERR);
		}
		if(validMailIfSending(req, String.valueOf(uid)+mailNo)) {
			return new ReMsg(ReCode.SHARE_CODE_SENDING);
		}
		;
		// 发送
		String codeMd5 = MDUtil.getMD5(mailNo + uid + RandomUtil.random(6));
		String urlPath = "https://candy.club/validEmail/" + codeMd5;
		String domain ="https://candy.club";
		String subject = "이메일 인증";
		
		StringBuilder body = new StringBuilder("<div style=\"width:540px; margin:0 auto; color:#666\">");
		body.append("<div style=\"border-bottom:1px solid #ddd; padding-bottom:15px;margin-bottom:30px; margin-top:100px\"><img src=\""+domain+"/active/assets/image/logo.png\" width=\"240\"/></div>");
		body.append("<h1 style=\"font-size:26px\">Candy club이메일 인증</h1>");
		body.append("<span>CandyClub 사용을 환영합니다. 아래 링크를 클릭하여 이메일 인증을 완료해주세요.</span><br/>");
		body.append("<a href=\""+urlPath+"\" style=\"background-color:#FF4984;color:#fff; padding:5px 30px; display:inline-block; margin:15px 0; margin-bottom:30px; border-radius:4px\">인증하기</a><br>");
		body.append("<span>화면 전환이 안된다면 링크를 복사 후 브라우저에서 실행해주세요</span><br>");
		body.append("<a href=\""+urlPath+"\" style=\"text-decoration:none; color:#666; margin-top:10px;display:inline-block;\">"+urlPath+"</span>");
		body.append("<div style=\"margin-top:50px;border-top:1px solid #ddd;padding-top:20px; text-align:center;font-size:12px;color:#999\" >Copyright © 2018 CANDY.CLUB. All Rights Reserved</div>");
		body.append("</div>");
		
		try {
			boolean b = MailUtil.sendMailByJava(mailNo, subject, body.toString(),Type.HTML);
			log.info("---发送本人要认证邮箱：---" + body);
			saveMailLog(mailNo, codeMd5, uid, super.getIp(req));
			if (b)
				return new ReMsg(ReCode.OK);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			req.getSession().removeAttribute(Const.SHARE_CODE_SENDING);
		}
		return new ReMsg(ReCode.SHARE_CODE_FAIL);
	}

	public static String getUrlPath(HttpServletRequest req) {
		String urlPath = "";
		int port = req.getServerPort();
		if (port > 0)
			urlPath = ":" + port;
		if (StringUtils.isNotEmpty(req.getContextPath()))
			urlPath += "/" + req.getContextPath();
		return "https://" + req.getServerName() + urlPath;
	}
	
	public Map<String,Object> queryUserMailLogStatus(Long uid,DBObject userDbo) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(DboUtil.getBool(userDbo, "emailVeri")!=null &&DboUtil.getBool(userDbo, "emailVeri") && StringUtils.isNotEmpty(DboUtil.getString(userDbo, "email"))) {
			map.put("mailStatus", Const.STATUS_OK);
			return map;
		}
		DBObject dbo =this.findByUidTime(uid);
		MailLog ml = DboUtil.toBean(dbo, MailLog.class);
		if (ml!=null&&ml.getStatus() == Const.STATUS_DEF && (System.currentTimeMillis() - ml.getUpdateTime()) < 5*Const.MINUTE) {
			map.put("mailStatus", Const.STATUS_PROCESSED);
			map.put("mailNo", ml.getMailNo());
			return map;
		}
		map.put("mailStatus", Const.STATUS_FAILED);
		return map;
	}
	
	public Boolean validMailIfSending(HttpServletRequest req,Object rnd) {
		if(req.getSession(false)!=null&&rnd.equals(req.getSession().getAttribute(Const.SHARE_CODE_SENDING))) {
			return true;
		}
		req.getSession().setAttribute(Const.SHARE_CODE_SENDING, rnd);
		return false;
	}

}
