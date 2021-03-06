package com.we.service.userTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.mongo.DboUtil;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.division.Division;
import com.we.models.division.DivisionTask.DivisionTaskType;
import com.we.models.finance.CoinLog;
import com.we.models.userTask.Task;
import com.we.models.userTask.UserTask;
import com.we.service.BaseService;
import com.we.service.UserDivisionService;
import com.we.service.UserService;
import com.we.service.UserWalletService;
import com.we.view.TaskVo;

@Service
public class UserTaskService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(UserTaskService.class);

	@Autowired
	TaskService taskService;
	
	@Autowired
	UserService userService;

	@Autowired
	UserWalletService userWalletService;

	@Autowired
	TelegramService telegramService;
	
	@Autowired
	UserDivisionService userDivisionService;

	public DBObject findById(Long id) {
		return super.findById(DocName.USER_TASK, id);
	}

	public DBObject findByUidAndtaskId(Long uid, Long taskId) {
		return getC(DocName.USER_TASK).findOne(new BasicDBObject("uid", uid).append("taskId", taskId));
	}

	public Page<DBObject> query(Long uid, Long taskId, Integer status, Integer page, Integer size) {
		size = getSize(size);
		page = getPage(page);
		List<DBObject> dbos = find(uid, taskId, status, page, size);
		Integer count = count(uid, taskId, status);
		return new Page<DBObject>(count, size, page, dbos);
	}

	public List<DBObject> find(Long uid, Long taskId, Integer status, Integer page, Integer size) {
		DBObject q = new BasicDBObject();
		if (null != uid && uid != 0) {
			q.put("uid", uid);
		}
		if (null != taskId && taskId != 0) {
			q.put("taskId", taskId);
		}
		if (null != status && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.USER_TASK).find(q).sort(new BasicDBObject("updateTime", -1)).skip(getStart(page, size))
				.limit(getSize(size)).toArray();
	}

	/** 查询用户所有的任务记录 */
	public List<DBObject> find(Long uid, Long taskId) {
		DBObject q = new BasicDBObject();
		if (null != uid && uid != 0) {
			q.put("uid", uid);
		}
		if (null != taskId && taskId != 0) {
			q.put("taskId", taskId);
		}
		return getC(DocName.USER_TASK).find(q).sort(new BasicDBObject("sort", -1)).toArray();
	}

	public Integer count(Long uid, Long taskId, Integer status) {
		DBObject q = new BasicDBObject();
		if (null != uid && uid != 0) {
			q.put("uid", uid);
		}
		if (null != taskId && taskId != 0) {
			q.put("taskId", taskId);
		}
		if (null != status && status != 0) {
			q.put("status", status);
		}
		return getC(DocName.USER_TASK).find(q).count();
	}

	public List<TaskVo> getUserTaskList( Integer type) {
		long uid = super.getUid();
		List<TaskVo> ts = new ArrayList<TaskVo>();
		List<DBObject> dbos = taskService.findAllOnlineTasks(type);
		if (uid > 0) {
			List<TaskVo> tse = new ArrayList<TaskVo>();	
			for (DBObject dbo : dbos) {
				Task t = DboUtil.toBean(dbo, Task.class);
				//System.out.println(uid+" tid:"+ t.get_id());
				DBObject ut = this.findByUidAndtaskId(uid, t.get_id());
				if (ut != null) {
					Integer status = DboUtil.getInt(ut, "status");
					//System.out.println("status:"+ status);
					if (status != UserTask.SUCCESS) {
						ts.add(new TaskVo(t, status, DboUtil.getLong(ut, "_id")));
					}else {
						tse.add(new TaskVo(t, status, DboUtil.getLong(ut, "_id")));
					}
				} else {
					ts.add(new TaskVo(t));
				}
			}
			ts.addAll(tse);
		} else {
			for (DBObject dbo : dbos) {
				Task t = DboUtil.toBean(dbo, Task.class);
				ts.add(new TaskVo(t));
			}

		}
		return ts;
	}

	/** 校验任务是否过期 */
	public boolean taskIsExpired(Long startTime, Long endTime, long nowTime) {
		if (startTime != null && endTime != null) {
			if (startTime < nowTime && nowTime < endTime) {
				return false;
			}
		}
		return true;
	}

	/** 点击做任务 */
	public ReMsg doTask(Long taskId) {
		Long uid = super.getUid();
		DBObject task = taskService.findById(taskId);
		Integer template = DboUtil.getInt(task, "template");
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("type", template);
		if (template == Task.TastTemplate.JOIN_TELEGRAM.getCode()) {
			// 加入telegram群组
			return telegramService.doTelegramTask(taskId, uid);
		} else if (template == Task.TastTemplate.FOllOW_TWITTER.getCode()) {
			// 推特账号 拼接对应的地址 或是数据库存的完整地址
			res.put("url", "https://twitter.com/" + DboUtil.getString(task, "data"));
			res.put("urlType", "_blank");
			return new ReMsg(ReCode.OK, res);
		} else if (template == Task.TastTemplate.SHARE_UPLOAD.getCode()) {
			DBObject utb = this.findByUidAndtaskId(uid, taskId);
			if (utb == null) {
				res.put("obj", this.save(taskId, uid, UserTask.DOING, 0, 1));
			} else {
				if (DboUtil.getInt(utb, "status") > UserTask.DOING) {
					return new ReMsg(ReCode.FAIL);
				} else if (DboUtil.getInt(utb, "status") < UserTask.DOING) {
					super.getC(DocName.USER_TASK).update(new BasicDBObject("_id", DboUtil.getLong(utb, "_id")),
							new BasicDBObject("$set", new BasicDBObject("status", UserTask.DOING)));
				}
				res.put("obj", utb);
			}
			return new ReMsg(ReCode.OK, res);
		} else if (template == Task.TastTemplate.JOIN_KAKAO.getCode()) {
			// TODO
			return new ReMsg(ReCode.OK, res);
		} else if(template == Task.TastTemplate.JOIN_LINK.getCode()) {
			res.put("url",DboUtil.getString(task, "data"));
			return new ReMsg(ReCode.OK, res);
		}
		return new ReMsg(ReCode.FAIL);
	}

	/** 用户进行任务 返回值 该任务的最新状态 */
	public ReMsg upload(Long id, String[] pics) {
		long uid = super.getUid();
		if (uid < 1) {
			return new ReMsg(ReCode.NOT_AUTHORIZED);
		}
		DBObject dbo = this.findById(id);
		if (dbo != null) {
			UserTask ut = DboUtil.toBean(dbo, UserTask.class);
			if (ut.getStatus() == UserTask.DOING && ut.getUid() == uid) {
				ut.setPics(pics);
				ut.setStatus(UserTask.AUDIT);
				ut.setUpdateTime(System.currentTimeMillis());
				super.getMongoTemplate().save(ut);
				return new ReMsg(ReCode.OK, ut);
			}
			
		}
		return new ReMsg(ReCode.FAIL);
	}

	public ReCode doJoinTelegarm(Task task, long uid) {
		Integer status = Const.STATUS_DEF;
		DBObject dbo = findByUidAndtaskId(uid, task.get_id());
		if (dbo == null) {// 没做过这个任务
			if (task.getTemplate() == Task.TastTemplate.JOIN_TELEGRAM.getCode()) {
				status = UserTask.FINISH;
			}
			save(task.get_id(), uid, status, 1, 1);
			super.getC(DocName.TASK).update(new BasicDBObject("_id", task.get_id()),
					new BasicDBObject("$inc", new BasicDBObject("finish", 1)));
			return ReCode.OK;
		}
		return ReCode.FAIL;
	}

	private UserTask save(long taskId, long uid, Integer status, int plan, int total) {
		long id = super.getNextId(DocName.USER_TASK);
		UserTask userTask = new UserTask(id, uid, taskId, plan, total, status);
		userTask.setUpdateTime(System.currentTimeMillis());
		super.getMongoTemplate().save(userTask);
		return userTask;
	}

	/** 用户领取任务奖励 */
	public ReMsg recvReward(long uid, long id) {
		if (super.lock("recvReward:" + uid + "-" + id, 2)) {
			try {
				DBObject dbo = this.findById(id);
				if (DboUtil.getInt(dbo, "status") == UserTask.FINISH) {
					long taskId = DboUtil.getLong(dbo, "taskId");
					DBObject taskDbo = taskService.findById(taskId);

					Task t = DboUtil.toBean(taskDbo, Task.class);
					super.getC(DocName.USER_TASK).update(new BasicDBObject("_id", id),
							new BasicDBObject("$set", new BasicDBObject("status", UserTask.SUCCESS).append("updateTime",
									System.currentTimeMillis())));
					
					DBObject u1 = userService.findById(uid);
					Long m1  = null;
					Long m2 = null;
					Long m3 = null;
					if(u1!=null) {
						m1 = DboUtil.getLong(u1, "shareUid");
						int divId = DboUtil.getInt(u1, "divisionId");
						Division d = Division.of(divId);
						if(m1!=null) {
							DBObject u2 = userService.findById(m1);
							if(u2!=null) {
								m2 =  DboUtil.getLong(u2, "shareUid");
								if(m2!=null) {
									DBObject u3 = userService.findById(m2);
									m3 = DboUtil.getLong(u3, "shareUid");
								}
							}
						}
						// 奖励是待领取
						HashMap<String, Double> rewards = t.getRewards();
						// 发送任务奖励
						//TODO 发放奖励没有做折扣，也没有上级提成
						//领取任务奖励UserTaskId|
						for (String key : rewards.keySet()) {
							Double amount = rewards.get(key)*d.getRate();
							userWalletService.addCoin(uid, CoinLog.IN_TASK, taskId, key, amount, 0,
									"미션리워드 수령 사용자 ID:" + id);//领取任务奖励完成，用户任务ID为123
							if(m1!=null) {
								userWalletService.addCoin(m1, CoinLog.IN_TASK, taskId, key, amount*0.05, 0,
										"미션리워드 수령 사용자 ID:" + id);
							}
							if(m2!=null) {
								userWalletService.addCoin(m2, CoinLog.IN_TASK, taskId, key, amount*0.03, 0,
										"미션리워드 수령 사용자 ID:" + id);
							}
							if(m3!=null) {
								userWalletService.addCoin(m3, CoinLog.IN_TASK, taskId, key, amount*0.02, 0,
										"미션리워드 수령 사용자 ID:" + id);
							}
						}
						
						userDivisionService.doDivisionTask(uid, DivisionTaskType.FINISH_USER_TASK_5, 1);
						userDivisionService.doDivisionTask(uid, DivisionTaskType.FINISH_USER_TASK_10, 1);
						
						Map<String, Object> ret = new HashMap<String, Object>();
						ret.put("rewards", rewards);
						ret.put("taskId", taskId);
						return new ReMsg(ReCode.OK, ret);
					}
				}
			} catch (Exception e) {
				log.error("Reward err:", e);
			} finally {
				super.unlock("recvReward:" + uid + "-" + id);
			}
		}
		return new ReMsg(ReCode.FAIL);
	}

	/** 审核 */
	public ReMsg audit(long id, Integer status) {
		Long adminId = super.getUid();
		DBObject dbo = this.findById(id);
		if (DboUtil.getInt(dbo, "status") == UserTask.AUDIT) {
			super.getC(DocName.USER_TASK).update(new BasicDBObject("_id", id),
					new BasicDBObject("$set", new BasicDBObject("status", status)
							.append("updateTime", System.currentTimeMillis()).append("adminId", adminId)));
			if (status == UserTask.FINISH) {
				super.getC(DocName.TASK).update(new BasicDBObject("_id", DboUtil.getLong(dbo, "taskId")),
						new BasicDBObject("$inc", new BasicDBObject("finish", 1)));
			}
		}
		return new ReMsg(ReCode.OK);
	}
}