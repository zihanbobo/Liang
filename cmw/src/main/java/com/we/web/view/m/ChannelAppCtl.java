package com.we.web.view.m;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DBObject;
import com.we.common.Constant.Const;
import com.we.common.Constant.ReCode;
import com.we.common.utils.JSONUtil;
import com.we.core.conf.Config;
import com.we.core.web.ReMsg;
import com.we.models.userTask.Task;
import com.we.service.BannerService;
import com.we.service.NoticeService;
import com.we.service.UserWalletService;
import com.we.service.article.ArticleService;
import com.we.service.userTask.UserTaskService;

@Controller
@RequestMapping(value = "/app")
public class ChannelAppCtl extends BaseCtl {

	@Autowired
	UserTaskService userTaskService;
	
	@Autowired
	UserWalletService userWalletService;

	@Autowired
	ArticleService articleService;
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	BannerService bannerService;
	
	@Autowired
	MyAppCtl myAppCtl;

	
	/** 任务页面 */
	@RequestMapping(value = "/task",method=RequestMethod.GET)
	@ResponseBody
	public ReMsg task(Integer type,Long taskId,HttpServletRequest req) {
		Map<String, Object> res = super.getCommonMap();
		res.put("types", Task.TastType.values());
		res.put("tasks", userTaskService.getUserTaskList(type));
		res.put("coinPics", userWalletService.queryCoinPics());
		if(taskId==null) {
			taskId = 0L;
		}
		return new ReMsg(ReCode.OK, res);
	}
	
	/** 新闻页面 */
	@RequestMapping(value = "/news",method=RequestMethod.GET)
	@ResponseBody
	public ReMsg news(Integer page, Integer size, HttpServletRequest req) {
		if (size == null || size == 0) {
			size = 10;
		}
		Map<String, Object> res = super.getCommonMap();
		// 新闻
		res.put("news", articleService.queryArticles(null, page, size, null, null,1));
		
		return new ReMsg(ReCode.OK, JSONUtil.beanToMap(res));
	}
	
	/** 获取一条新闻的详细信息 */
	@RequestMapping(value="/article",method=RequestMethod.GET)
	@ResponseBody
	public ReMsg news(Long id) {
		Map<String, Object> res = super.getCommonMap();
		res.put("new", articleService.readArticle(id));
		return new ReMsg(ReCode.OK,res);
	}
	
	/** 获取公告 */
	@ResponseBody
	@RequestMapping(value="/notices",method=RequestMethod.GET)
	public ReMsg notices(Integer page, Integer size) { 
		return new ReMsg(ReCode.OK, noticeService.find(Const.STATUS_OK, page, size));
	}
	
	/** 获取一条公告的详细信息 */
	@RequestMapping(value="/notice",method=RequestMethod.GET)
	@ResponseBody
	public ReMsg notice(Long id) {
		Map<String, Object> res = super.getCommonMap();
		res.put("notice", noticeService.findById(id));
		return new ReMsg(ReCode.OK,res);
	}
	
	@RequestMapping(value="/queryNoticeActivitys",method=RequestMethod.GET)
	@ResponseBody
	public ReMsg queryNoticeActivitysList() {
		Map<String, Object> res = super.getCommonMap();
		res.put("activitys", noticeService.queryNoticeActivitysList(1,20));
		return new ReMsg(ReCode.OK,res);
	}
	
	//首页banner 
	@ResponseBody
	@RequestMapping(value="/banners",method=RequestMethod.GET)
	public ReMsg banner(Integer type) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("imgDomain", Config.cur().getImgDomain());
		List<DBObject> p =new ArrayList<DBObject>();
		if(null!=type&&type<BannerService.TYPES.length) {
			p = bannerService.findBanner(BannerService.TYPES[type], Const.STATUS_OK, 0, 10);
		}else {
			p = bannerService.findBanner(BannerService.TYPES[4], Const.STATUS_OK, 0, 10);
		}
		res.put("banners", p);
		return new ReMsg(ReCode.OK, res);
	}
	
	
	@RequestMapping(value="/shareText",method=RequestMethod.GET)
	public ModelAndView shareText(String code,String poster1,String poster2,Integer conNum) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("code", code);
		res.put("poster1", Config.cur().getImgDomain()+poster1);
		res.put("poster2", Config.cur().getImgDomain()+poster2);
		res.put("conNum", conNum);
		return new ModelAndView("htmlApp/share",res);
	}
	@RequestMapping(value="/sharePic",method=RequestMethod.GET)
	public ModelAndView sharePic(String picUrl) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("picUrl", picUrl);
		return new ModelAndView("htmlApp/sharePic",res); 
	}
	@RequestMapping(value="/shareNews",method=RequestMethod.GET)
	public ModelAndView shareNews(Integer i,Long gid) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("imgDomain", Config.cur().getImgDomain());
		if(i!=null&&1==i) {
			res.put("new", articleService.readArticle(gid));
		}else {
			res.put("new",noticeService.findById(gid));
		}
		res.put("i", i);
		return new ModelAndView("htmlApp/newsDetail",res); 
	}

}
