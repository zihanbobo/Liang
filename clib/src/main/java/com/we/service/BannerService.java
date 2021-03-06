package com.we.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.ReCode;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.Banner;
import com.we.models.DocName;
import com.we.service.cloud.StorageService;

@Service
public class BannerService extends BaseService {

	public static final String[] TYPES = { "home", "task", "news", "business" ,"appMain"};

	// public static final String[] OPS = { "url", "tool", "eif", "game",
	// "toolTag" };
	@Autowired
	StorageService storageService;

	static final Logger log = LoggerFactory.getLogger(BannerService.class);

	public DBObject findBannerById(Long id) {
		return getC(DocName.BANNER).findOne(new BasicDBObject("_id", id));
	}

	public List<DBObject> findBanner(String type, Integer status, Integer page, Integer size) {
		DBObject q = new BasicDBObject();
		if (null != status && status != 0) {
			q.put("status", status);
		}
		if (StringUtils.isNotBlank(type)) {
			q.put("type", type);
		}
		long time = System.currentTimeMillis(); // 1500
		q.put("startTime", new BasicDBObject("$lte", time));// 开始时间小于当前时间 // 1000
		q.put("endTime", new BasicDBObject("$gte", time));// 结束时间大于当前时间 // 2000

		List<DBObject> dbos = getC(DocName.BANNER).find(q).skip(super.getStart(page, size)).limit(getSize(size))
				.sort(new BasicDBObject("sort", -1).append("updateTime", -1)).toArray();
		return dbos;
	}

	public List<DBObject> findBannerAdmin(String type, Integer status, Integer page, Integer size) {
		DBObject q = new BasicDBObject();
		if (null != status && status != 0) {
			q.put("status", status);
		}
		if (StringUtils.isNotBlank(type)) {
			q.put("type", type);
		}
		return getC(DocName.BANNER).find(q).skip(super.getStart(page, size)).limit(getSize(size))
				.sort(new BasicDBObject("sort", -1).append("updateTime", -1)).toArray();

	}

	public int countBanner(String type, Integer status) {
		DBObject q = new BasicDBObject();
		if (null != status && status != 0) {
			q.put("status", status);
		}
		if (StringUtils.isNotBlank(type)) {
			q.put("type", type);
		}
		return getC(DocName.BANNER).find(q).count();
	}

	public Page<DBObject> queryBanner(Integer status, String type, Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		List<DBObject> dbos = this.findBannerAdmin(type, status, page, size);
		int count = countBanner(type, status);
		return new Page<DBObject>(count, size, page, dbos);
	}

	public ReMsg upsert(Long id, String type, String title, String pic, String op, String opId, Integer status,
			Integer sort, Long startTime, Long endTime, String prePic) {
		if (StringUtils.isBlank(title)) {
			return new ReMsg(ReCode.EMPTY_TITLE);
		}
		Long adminId = super.getUid();// 获取操作用户 权限验证未做 TODO
		if (id == null || id < 1L) {
			id = super.getNextId(DocName.BANNER);
			Banner banner = new Banner(id, type, title, pic, op, opId, status, adminId, sort, startTime, endTime,
					prePic);
			getMongoTemplate().save(banner);
			return new ReMsg(ReCode.OK);
		} else {
			DBObject dbo = new BasicDBObject("adminId", adminId).append("updateTime", System.currentTimeMillis());
			if (StringUtils.isNotBlank(type)) {
				dbo.put("type", type);
			}
			if (StringUtils.isNotBlank(title)) {
				dbo.put("title", title);
			}
			if (StringUtils.isNotBlank(pic)) {
				dbo.put("pic", pic);
			}
			if (StringUtils.isNotBlank(prePic)) {
				dbo.put("prePic", prePic);
			}
			if (StringUtils.isNotBlank(op)) {
				dbo.put("op", op);
			}
			if (StringUtils.isNotBlank(opId)) {
				dbo.put("opId", opId);
			}
			if (status != null && status != 0) {
				dbo.put("status", status);
			}
			if (sort != null && sort != 0) {
				dbo.put("sort", sort);
			}
			if (startTime != null && startTime != 0) {
				dbo.put("startTime", startTime);
			}
			if (endTime != null && endTime != 0) {
				dbo.put("endTime", endTime);
			}
			super.getC(DocName.BANNER).update(new BasicDBObject("_id", id), new BasicDBObject("$set", dbo));
			return new ReMsg(ReCode.OK);
		}
		// return new ReMsg(ReCode.FAIL);
	}
	
	public ReMsg uploadPic(MultipartFile file, String ptoken, String timestamp) {
		return storageService.uploadPic(file, ptoken, timestamp);
	}

}
