package com.we.service;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.utils.DateUtil;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.Notice;
import com.we.models.NoticeActivity;

/** 公告 */
@Service
public class NoticeService extends BaseService {
	static final Logger log = LoggerFactory.getLogger(NoticeService.class);

	public ReMsg upsetNotice(Long _id, String title, String content, Integer status, Long adminId) {
		if (_id == null || _id == 0) {
			_id = super.getNextId(DocName.NOTICE);
			long time = System.currentTimeMillis();
			Notice notice = new Notice(time, title, content, adminId, status);
			notice.setUpdateTime(time);
			notice.set_id(_id);
			super.getMongoTemplate().save(notice);
		} else {
			DBObject u = new BasicDBObject("updateTime", System.currentTimeMillis());
			if (status != null && status != 0) {
				u.put("status", status);
			}
			if (adminId != null && adminId != 0) {
				u.put("adminId", adminId);
			}
			if (StringUtils.isNotBlank(content)) {
				u.put("content", content);
			}
			if (StringUtils.isNotBlank(title)) {
				u.put("title", title);
			}
			getC(DocName.NOTICE).update(new BasicDBObject("_id", _id), new BasicDBObject("$set", u));
		}
		return new ReMsg(ReCode.OK);
	}

	public DBObject findById(Long _id) {
		return getC(DocName.NOTICE).findOne(new BasicDBObject("_id", _id));
	}

	public Page<DBObject> query(Integer status, Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		List<DBObject> dbos = find(status, page, size);
		int count = count(status);
		return new Page<DBObject>(count, size, page, dbos);
	}

	public int count(Integer status) {
		DBObject q = new BasicDBObject();
		if (status != null && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.NOTICE).find(q).count();
	}

	public List<DBObject> find(Integer status, Integer page, Integer size) {
		DBObject q = new BasicDBObject();
		if (status != null && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.NOTICE).find(q).skip(super.getStart(page, size)).limit(getSize(size))
				.sort(new BasicDBObject("_id", -1)).toArray();
	}
	
	public ReMsg updateNoticeActivity(Long _id, String title, String content,Integer status, Long adminId,String actLink,String endTime,
			Integer lkFlag,String pic,Double sort) {
		Long actEndTime =null;
		if(StringUtils.isNotBlank(endTime)) {
			try {
				actEndTime = DateUtil.convertDate(endTime, "yyyy-MM-dd").getTime();
			} catch (ParseException e) {
				return new ReMsg(ReCode.FAIL, "活动结束时间有误！");
			}
		}
		if (_id == null || _id == 0) {
			_id = super.getNextId(DocName.NOTICE_ACTIVITY);
			NoticeActivity notice = new NoticeActivity(title, content, status, adminId,actLink,actEndTime,lkFlag,pic);
			notice.set_id(_id);
			if(null!=sort)notice.setSort(sort);
			super.getMongoTemplate().save(notice);
		} else {
			DBObject u = new BasicDBObject("updateTime", System.currentTimeMillis());
			if (status != null && status != 0) {
				u.put("status", status);
			}
			if (adminId != null && adminId != 0) {
				u.put("adminId", adminId);
			}
			if (StringUtils.isNotBlank(content)) {
				u.put("content", content);
			}
			if (StringUtils.isNotBlank(title)) {
				u.put("title", title);
			}
			if(StringUtils.isNotBlank(actLink)) {
				u.put("actLink", actLink);
			}
			if(null!=actEndTime) {
				u.put("actEndTime", actEndTime);
			}
			if(null!=lkFlag&&lkFlag!=2) {
				lkFlag=1;
			}
			u.put("lkFlag", lkFlag);
			if(StringUtils.isNotBlank(pic)) {
				u.put("pic", pic);
			}
			if(null!=sort) {
				u.put("sort", sort);
			}
			getC(DocName.NOTICE_ACTIVITY).update(new BasicDBObject("_id", _id), new BasicDBObject("$set", u));
		}
		return new ReMsg(ReCode.OK);
	}
	
	public Page<DBObject> queryNoticeActivitys(Integer status, Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		List<DBObject> dbos = findNoticeActivitys(status, page, size,false);
		int count = countNoticeActivitys(status);
		return new Page<DBObject>(count, size, page, dbos);
	}

	public int countNoticeActivitys(Integer status) {
		DBObject q = new BasicDBObject();
		if (status != null && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.NOTICE_ACTIVITY).find(q).count();
	}

	public List<DBObject> findNoticeActivitys(Integer status, Integer page, Integer size,boolean expire) {
		DBObject q = new BasicDBObject();
		if (status != null && status != 0) {
			q.put("status", status);
		}
		if(expire) {
			q.put("actEndTime",new BasicDBObject("$gt",System.currentTimeMillis()));
		}
		return getC(DocName.NOTICE_ACTIVITY).find(q).skip(super.getStart(page, size)).limit(getSize(size))
				.sort(new BasicDBObject("sort", -1).append("updateTime", -1)).toArray();
	}
	
	public DBObject findNoticeActivityById(Long _id) {
		DBObject dbo=new BasicDBObject();
		if(null!=_id&&_id>0) {
			dbo.put("_id", _id);
			return getC(DocName.NOTICE_ACTIVITY).findOne(dbo);
		}
		return dbo;
	}
	
	public List<DBObject> queryNoticeActivitysList(Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		return findNoticeActivitys(Const.STATUS_OK, page, size,true);
	}

}
