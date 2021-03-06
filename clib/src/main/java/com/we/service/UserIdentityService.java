package com.we.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.UserIdentity;

/** 用户的身份认证 */
@Service
public class UserIdentityService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(UserIdentityService.class);

	/** 后台审核 改状态 */
	public ReMsg validUseridentity(final Long _id, final Integer status, long adminId) {
		super.getC(DocName.USER_IDENTITY).update(new BasicDBObject("_id", _id),
				new BasicDBObject("$set", new BasicDBObject("status", status).append("adminId", adminId)
						.append("updateTime", System.currentTimeMillis())));
		return new ReMsg(ReCode.OK);
	}

	public Page<DBObject> query(Long _id, Integer status, Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		List<DBObject> dbos = find(_id, status, page, size);
		int count = count(_id, status);
		return new Page<DBObject>(count, size, page, dbos);
	}

	public int count(Long _id, Integer status) {
		DBObject q = new BasicDBObject();
		if (_id != null && _id > 0) {
			q.put("_id", _id);
		}
		if (status != null && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.USER_IDENTITY).find(q).count();
	}

	public List<DBObject> find(Long _id, Integer status, Integer page, Integer size) {
		DBObject q = new BasicDBObject();
		if (_id != null && _id > 0) {
			q.put("_id", _id);
		}
		if (status != null && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.USER_IDENTITY).find(q).skip(super.getStart(page, size)).limit(getSize(size))
				.sort(new BasicDBObject("updateTime", -1)).toArray();
	}

	public DBObject findById(long _id) {
		return getC(DocName.USER_IDENTITY).findOne(new BasicDBObject("_id", _id));
	}

	public ReMsg saveUserIdentity(long _id, String frontPic, String backPic, String inHandPic, String realname,
			String number) {
		UserIdentity userIdentity = new UserIdentity(_id, realname, number, frontPic, backPic, inHandPic,
				Const.STATUS_DEF);
		userIdentity.setUpdateTime(System.currentTimeMillis());
		super.getMongoTemplate().save(userIdentity);
		return new ReMsg(ReCode.OK);
	}
	
	public ReMsg updateKYC(long _id, String frontPic, String backPic, String inHandPic,String realname) {
		if(StringUtils.isBlank(frontPic)&&StringUtils.isBlank(backPic)&&StringUtils.isBlank(inHandPic)) {
			return new ReMsg(ReCode.KYC_UPLOAD_ERR);
		}
		
			DBObject u = new BasicDBObject("updateTime", System.currentTimeMillis());
			if(StringUtils.isNotBlank(frontPic)) {
				u.put("frontPic", frontPic);
			}
			if(StringUtils.isNotBlank(backPic)) {
				u.put("backPic", backPic);
			}
			if(StringUtils.isNotBlank(inHandPic)) {
				u.put("inHandPic", inHandPic);
			}
			if(StringUtils.isNotBlank(realname)) {
				u.put("realname", realname);
			}
			u.put("status", Const.STATUS_PROCESSED);
			getC(DocName.USER_IDENTITY).update(new BasicDBObject("_id", _id), new BasicDBObject("$set", u), true, true);
		return new ReMsg(ReCode.OK);
	}
	
}
