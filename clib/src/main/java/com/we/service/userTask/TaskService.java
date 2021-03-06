package com.we.service.userTask;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.mongo.DboUtil;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.userTask.Task;
import com.we.models.userTask.TokenPrj;
import com.we.service.BaseService;

@Service
public class TaskService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	TokenPrjService tokenPrjService;

	public DBObject findById(Long id) {
		return super.findById(DocName.TASK, id);
	}

	public Task getTaskByUniqueId(Integer template, String uniqueId) {
		return DboUtil.toBean(getC(DocName.TASK).findOne(
				new BasicDBObject("uniqueId", uniqueId).append("template", template).append("status", Const.STATUS_OK)),
				Task.class);
	}

	public Page<DBObject> query(Long _id, Integer type, Integer status, Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		List<DBObject> dbos = find(_id, type, status, page, size);
		Integer count = count(_id, type, status);
		return new Page<DBObject>(count, size, page, dbos);
	}

	public List<DBObject> find(Long _id, Integer type, Integer status, Integer page, Integer size) {
		DBObject q = new BasicDBObject();
		if (null != status && status != 0) {
			q.put("status", status);
		}
		if (null != type && type != 0) {
			q.put("type", type);
		}
		if (null != _id && _id != 0) {
			q.put("_id", _id);
		}
		return getC(DocName.TASK).find(q).sort(new BasicDBObject("sort", -1).append("updateTime", -1)).skip(getStart(page, size))
				.limit(getSize(size)).toArray();
	}

	public Integer count(Long _id, Integer type, Integer status) {
		DBObject q = new BasicDBObject();
		if (null != status && status != 0) {
			q.put("status", status);
		}
		if (null != type && type != 0) {
			q.put("type", type);
		}
		if (null != _id && _id != 0) {
			q.put("_id", _id);
		}
		return getC(DocName.TASK).find(q).count();
	}

	/** 获取所有的上线任务 */
	// TODO 要分为获取平台任务还是项目方任务
	public List<DBObject> findAllOnlineTasks(Integer type) {
		DBObject q = new BasicDBObject("status", Const.STATUS_OK);
		if (null != type && type != 0) {
			q.put("type", type);
		}
		long time = System.currentTimeMillis();
		q.put("startTime", new BasicDBObject("$lt", time));
		q.put("endTime", new BasicDBObject("$gt", time));
		return getC(DocName.TASK).find(q).sort(new BasicDBObject("sort", -1).append("updateTime", -1)).limit(1000).toArray();
	}

	/** 更新任务信息 */
	public ReMsg upSet(Long id, String title, String detail, Integer type, Integer template, String data, String qrCode,
			Long total, Integer count, int sort, Long startTime, Long endTime, String symbol, String[] coinType,
			Double coinAmount[], Integer status, String uniqueId,String pic) {
		Long adminId = super.getUid();
		if (adminId < 1) {
			return new ReMsg(ReCode.FAIL);
		}
		if (null == id || id < 1) {
			id = super.getNextId(DocName.TASK);
		}
		HashMap<String, Double> rewards = new HashMap<String, Double>();
		if (coinType != null && coinType.length > 0) {
			for (int i = 0; i < coinType.length; i++) {
				if (StringUtils.isNotBlank(coinType[i]) && coinAmount[i] != null && coinAmount[i] > 0) {
					rewards.put(coinType[i], coinAmount[i]);
				}
			}
		}
		TokenPrj tp = tokenPrjService.getTokenPrj(symbol); 
		
		Task task = new Task(id, title, detail, type, template, data, qrCode, total, count, sort, startTime, endTime,
				adminId, tp, rewards, status, uniqueId);
		if(StringUtils.isNotBlank(pic)) {
			task.setPic(pic);
		}
		task.setUpdateTime(System.currentTimeMillis());
		Long finish=DboUtil.getLong(findById(id), "finish");
		task.setFinish(finish==null?0:finish);
		getMongoTemplate().save(task);
		// 删除缓存
		return new ReMsg(ReCode.OK, task);
	}

	public ReCode downTask(Long id) {
		getC(DocName.TASK).update(new BasicDBObject("_id", id),
				new BasicDBObject("$set", new BasicDBObject("status", -1)));
		return ReCode.OK;
	}

}
