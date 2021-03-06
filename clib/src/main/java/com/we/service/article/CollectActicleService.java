package com.we.service.article;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.redis.RK;
import com.we.common.utils.DateUtil;
import com.we.common.utils.T2TUtil;
import com.we.core.Page;
import com.we.core.web.ReMsg;
import com.we.models.DocName;
import com.we.models.article.Article;
import com.we.models.article.CollectArticle;
import com.we.models.article.Group;
import com.we.service.BaseService;

@Service
public class CollectActicleService extends BaseService{
	static final Logger log=LoggerFactory.getLogger(CollectActicleService.class);
	
	private final static String sign ="@";
	
	public DBObject findById(Long id){
		if(id==null||id<=0) {
			return null;
		}                                                                                                                                                                                                         
		
		return getC(DocName.COLLECT_ARTICLE).findOne(new BasicDBObject("_id",id));
	}
	
	public ReMsg updateActicleById(Long id,String content,String title) {
		if(id==null||id<=0) {
			return new ReMsg(ReCode.FAIL);
		}
		DBObject dbObj = new BasicDBObject();
		if(StringUtils.isNotEmpty(content)) {
			dbObj.put("content", content);
		}
		if(StringUtils.isNotEmpty(title)){
			dbObj.put("title", title);
		}
		
		 Object obj =getC(DocName.COLLECT_ARTICLE).findAndModify(new BasicDBObject("_id", id), new BasicDBObject("_id",Integer.valueOf(1)), null, false,
				 new BasicDBObject("$set", dbObj), false,false, WriteConcern.ACKNOWLEDGED).get("_id");
		 if(obj!=null&&id.toString().equals(obj.toString())) {
			 return new ReMsg(ReCode.OK);
		 }
		
		return new ReMsg(ReCode.FAIL);
	}
	
	public ReMsg deleteActicleById(Long id) {
		if(id==null||id<=0) {
			return new ReMsg(ReCode.FAIL);
		}
		
		getC(DocName.COLLECT_ARTICLE).update(new BasicDBObject("_id", id),new BasicDBObject("$set", new BasicDBObject("status",Const.STATUS_FAILED)));
		
		return new ReMsg(ReCode.OK);
	}
	
	public Page<DBObject> findCollectActiclePage(Integer page,Integer size,String title,String content,Boolean mark,Integer status,String beginDate,String endDate){
		DBObject q =getParamDBObj(title,content,mark,beginDate,endDate,status);
		page = getPage(page);
		size = getSize(size);
		
		List<DBObject> list=getC(DocName.COLLECT_ARTICLE).find(q).skip(super.getStart(page,size)).limit(size)
		.sort(new BasicDBObject("updateTime", -1)).toArray();
		
		int count =getC(DocName.COLLECT_ARTICLE).find(q).count();
		
		return new Page<DBObject>(count,size,page,list);
	}
	
	public DBObject getParamDBObj(String title,String content,Boolean mark,String beginDate,String endDate,Integer status){
		DBObject q = initDate(beginDate,endDate,"updateTime");
		if(StringUtils.isNotBlank(title)) {
			q.put("title", new BasicDBObject("$regex", title));
		}
		
		if(mark!=null) {
			q.put("mark", mark);
		}
		
		if(status!=null&&(3==status||-1==status)) {
			q.put("status",status);
		}
		if(status!=null&&1==status) {
			q.put("status",0);
		}
		q.put("status", new BasicDBObject("$ne",Const.STATUS_FAILED));
		return q;
	}
	
	public void recordHadCollect(String conDateStr,String conTime,String content) {
		if(content!=null&&content.length()>50) {
			content=content.substring(0, 50);
		}
		 getRedisTemplate().opsForHash().put(RK.collectActicleMark(),conDateStr+sign+conTime,content);
	}
	
	public Boolean isContainContent(String conDateStr,String conTime,String content) {
		Boolean boo=getRedisTemplate().opsForHash().hasKey(RK.collectActicleMark(), conDateStr+sign+conTime);
		if(boo!=null&&boo&&content!=null) {
			String str =(String)getRedisTemplate().opsForHash().get(RK.collectActicleMark(), conDateStr+sign+conTime);
			if(StringUtils.isNotEmpty(str))return content.contains(str);
		}
		return false;
	}
	
	public void clearRecordData() {
		getRedisTemplate().opsForHash().putIfAbsent(RK.collectActicleMark(),"clear"+sign+sign,String.valueOf(DateUtil.getTodayZeroTime()));
		Object obj =getRedisTemplate().opsForHash().get(RK.collectActicleMark(),"clear"+sign+sign);
		if(obj!=null) {
			long l=T2TUtil.obj2Long(obj);
			if(System.currentTimeMillis()-l>Const.DAY) {
				getRedisTemplate().delete(RK.collectActicleMark());
			}
		}
	}
	
	
	public List<DBObject> findPublicDataByParam(Integer page,Integer size){
		DBObject q = new BasicDBObject("status",new BasicDBObject("$ne",Const.STATUS_FAILED));
		page = getPage(page);
		size = getSize(size);
		
		List<DBObject> list=getC(DocName.COLLECT_ARTICLE).find(q).skip(super.getStart(page,size)
				).limit(size).sort(new BasicDBObject("conDate", -1)).sort(new BasicDBObject("conTime", -1)).toArray();
		return list;
	}
	
	public void saveAcrticleList(List<CollectArticle> list){
		getMongoTemplate().insert(list,CollectArticle.class);
	}
	
	public DBObject findByContent(String title,String content) {
		if(content==null) {
			content="";
		}
		return getC(DocName.COLLECT_ARTICLE).findOne(new BasicDBObject("groupId",Group.SYSTEM_GROUP).append("title", title).append("content",content));
	}
	
	public DBObject findAcrticleByOpt(String title,String content) {
		if(content==null) {
			content="";
		}
		return getC(DocName.ARTICLE).findOne(new BasicDBObject("title", title).append("content",content));
	}
	
	public void saveAcrticleData(List<Article> list){
		getMongoTemplate().insert(list,Article.class);
	}
	
	private Article getArtcle(String title, String content,Long conDate,String conTime,Boolean mark) {
		Article article =new Article();
		article.set_id(super.getNextId(DocName.ARTICLE));
		article.setTitle(title);
		article.setContent(content);
		article.setPublishTime(getHmTime(conDate,conTime));
		article.setMark(mark);
		article.setCreateTime(System.currentTimeMillis());
		article.setUpdateTime(article.getCreateTime());
		article.setAudited(Article.AUDITED_Y);
		article.setGroupId(Group.SYSTEM_GROUP);
		article.setPri(Article.PRI_ALL);
		article.setStatus(Article.PERSONAL);
		return article;
	}
	
	private final String pageUrl ="https://kr.coinness.com/";
	private final String firstNode = ".news-list .home-container .day";
	private final String secondeNode =".content-wrap ul.newscontainer>li";
	
	public void getRequireWebHtmlData() {
		clearRecordData();
		 Document document;
		try {
			final String[] dislodges = {"코인니스"};
			int from =1;//https://kr.coinness.com/
			List<Article> list =new ArrayList<Article>();
			document = Jsoup.connect(pageUrl).cookie("tz", "%2B08%3A00").timeout(60 * 1000).get();
			 Elements els1 = document.body().select(firstNode);
			 for(Element e:els1) {
				 if(StringUtils.isNotEmpty(secondeNode)) {
					 Elements els2 = e.select(secondeNode);
					 String conDateStr = e.select(".fixtoptime span").text().trim();
					 long conDate=DateUtil.getTodayZeroTime();
					 String conDateStr2 =validKo(conDateStr);
					 try {
						 if(!"".equals(conDateStr2)) {
							 conDate =DateUtil.convertDate(conDateStr2, "yyyy"+koYear+"MM"+koMon+"dd"+koDate).getTime();
						 }
					} catch (ParseException pe) {
						conDate=DateUtil.getTodayZeroTime();
					}
					 for(Element e2:els2) {
						 String markObj = e2.select(".content h3").attr("style");
						 Boolean colorBack = e2.select(".content h3 .newstime").hasClass("newstimeLink");
						 Elements titleNode=e2.select(".content h3").eq(0);
						 String conTime=titleNode.select(".newstime").remove().text();
						 String title=titleNode.text().trim();
						 boolean mark=false;
						 if(StringUtils.isNotEmpty(markObj)||colorBack) {
							 mark=true;
						 }
						 String content = e2.select(".news-content>:first-child").text().trim();
						 if(content.indexOf(dislodges[0])>0) {
							 continue;
						 }
						 Boolean bo=isContainContent(conDateStr2,conTime,content);
						 DBObject dbo=null;
						 if(bo!=null&&!bo) {
							 dbo = findAcrticleByOpt(title,content);
						 }
						 if(dbo==null&&!bo) {
							 Article article=getArtcle(title,content,conDate,conTime,mark);
							 article.setSource(from);
							 article.setUserId(11l);
							 list.add(article);
							 recordHadCollect(conDateStr2,conTime,content);
						 }
						 
					 }
					 
				 }
				 
			 }
			 
			 if(list!=null&&list.size()>0) {
				 saveAcrticleData(list);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			log.error("------请求抓取内容出错："+e.getMessage());
		}
	}
	
	
	private final static String koYear="년";
	private final static String koMon ="월";
	private final static String koDate="일";
	public static String validKo(String con) {
		String[] arr = con.split("，");
		if(arr.length>0) {
			for(String str:arr) {
				if(str.contains(koYear)&&str.contains(koMon)&&str.contains(koDate)) {
					return str.substring(0, str.indexOf(koDate)+1);
				}
			}
		}
		return "";
	}
	
	public static Long getHmTime(Long ori,String hm) {
		if(ori!=null&&ori!=0&&hm.indexOf(":")>0) {
			String[] splhm=hm.split(":");
			if(null==T2TUtil.str2Int(splhm[0])||null==T2TUtil.str2Int(splhm[1])) {
				return ori;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(ori));
			calendar.set(Calendar.HOUR_OF_DAY, T2TUtil.str2Int(splhm[0]));
			calendar.set(Calendar.MINUTE,T2TUtil.str2Int(splhm[1]));
			return calendar.getTimeInMillis();
		}
		return ori;
	}
	
	DBObject initDate(String beginDate,String endDate) {
		return initDate(beginDate, endDate, null);
	}
	
	DBObject initDate(String beginDate,String endDate,String sortDate) {
		DBObject q = new BasicDBObject();
		DBObject dateDbo =new BasicDBObject();
		try {
			if(StringUtils.isNotBlank(beginDate)) {
				Date begin = DateUtil.convertDate(beginDate, "yyyy-MM-dd");
				long bn = DateUtil.getZeroTime(begin);
				dateDbo.put("$gte",bn);
			}
		} catch (ParseException e1) {
			log.error("date err:" + beginDate);
			return q;
		}
		if (StringUtils.isNotBlank(endDate)) {
			try {
				Date end = DateUtil.convertDate(endDate, "yyyy-MM-dd");
				long ed = DateUtil.getZeroTime(end);
				dateDbo.put("$lte", ed);
			} catch (ParseException e1) {
				log.error("date err:" + endDate);
				return q;
			}
		}
		if(dateDbo!=null&&(dateDbo.containsField("$gte")||dateDbo.containsField("$lt"))) {
			if(sortDate!=null) {
				q.put(sortDate,dateDbo);
			}else {
				q.put("conDate",dateDbo);
			}
		}
		return q;
	}
	
}
