﻿package com.we.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.mongo.DboUtil;
import com.we.common.utils.DateUtil;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.division.DivisionTask.DivisionTaskType;
import com.we.models.finance.CoinLog;
import com.we.models.sign.Sign;

@Service
public class SignService extends BaseService {

	static final Logger log = LoggerFactory.getLogger(SignService.class);

	@Autowired
	UserService userService;

	@Autowired
	UserWalletService userWalletService;
	
	@Autowired
	UserDivisionService userDivisionService;

	public List<DBObject> findLogByUid(Long userId, int page, int size) {
		return getC(DocName.SIGN_LOG).find(new BasicDBObject("userId", userId)).sort(new BasicDBObject("day", -1))
				.skip(getStart(page, size)).limit(getSize(size)).toArray();
	}

	public DBObject findLogByUid(Long userId) {
		String id = DateUtil.curDay() + "_" + userId;
		return getC(DocName.SIGN_LOG).findOne(new BasicDBObject("_id", id));
	}

	public DBObject findById(Long userId) {
		return getC(DocName.SIGN).findOne(new BasicDBObject("_id", userId));
	}

	public Sign getSign(Long userId) {
		return DboUtil.toBean(findById(userId), Sign.class);
	}

	public static final int[] reward = new int[] { 50, 55, 60, 65, 70, 77, 777 };

	// 签到奖励
	public ReCode sendReward(long uid, int day) {
		return userWalletService.addCoin(uid, CoinLog.IN_SIGN, day, CoinLog.DEF_COIN_TYPE, reward[day - 1], 0, "출석 체크");//签到
	}

	// 签到
	public ReMsg sign(long uid) {
		if (super.lock("sign:" + uid, 2)) {
			try {
				Sign sign = getSign(uid);
				if (sign == null) {
					sign = new Sign(uid);
				} else {
					long lastSignTime = sign.getUpdateTime();
					long time = DateUtil.getTodayZeroTime();
					if (time - lastSignTime > 0) {// 证明今天还没有签到
						if (time - lastSignTime < Const.DAY) {
							// 证明昨天也签到了
							sign = sign.continued();
						} else {
							sign = sign.discontinuous();
						}
					} else {
						// 今天已经签到了
						return new ReMsg(ReCode.HAVE_BEEN_SIGNED);
					}
				}
				super.getMongoTemplate().save(sign);
				
				return new ReMsg(sendReward(uid, sign.getCycle()));
			} finally {
				super.unlock("sign:" + uid);
			}
		}
		return new ReMsg(ReCode.FAIL);
	}

	// 查询签到情况
	public DBObject isSign(long uid) {
		DBObject dbo = findById(uid);
		if (dbo != null) {// 签到记录不为空 且连续签到记录不为0
			long lastSignTime = DboUtil.getLong(dbo, "updateTime");// 最后一次签到时间
			long time = DateUtil.getTodayZeroTime();
			if (time < lastSignTime && lastSignTime - time < Const.DAY) {// 今天已经签到
				dbo.put("sign", true);
			} else {
				dbo.put("sign", false);
			}
		}
		return dbo;
	}

}
