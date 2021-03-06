package com.zb.models.article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zb.models.AbstractDocument;

public class Article extends AbstractDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6016361050004072890L;

	public static final int DEL = -1;// 删除
	public static final int DOWN = 1;// 下线
	public static final int PERSONAL = 2;// 个人页
	public static final int OK = 3;// 上首页

	public static final int TYPE_MULT = 1;
	public static final int TYPE_TXT = 2;
	public static final int TYPE_IMG = 3;
	public static final int TYPE_VIDEO = 4;
	public static final int TYPE_DRAW = 5;
	public static final int TYPE_VOICE = 6;

	public static final int ESSENCE_N = 1;
	public static final int ESSENCE_Y = 2;

	public static final int AUDITED_N = 1;
	public static final int AUDITED_Y = 2;

	public static final int ANONYMOUS_N = 1;
	public static final int ANONYMOUS_Y = 2;

	public static final int TYPE_PRAISE = 1;
	public static final int TYPE_HIT = 2;

	public static final int GOD_COMMENT_NO = 1;
	public static final int GOD_COMMENT_YES = 2;

	
	public static final int PRI_ALL = 1;
	public static final int PRI_FRIEND = 2;
	
	
	private Long groupId;
	private int type; // 文章类型 1.富媒体，2 文本 3.图片 4 视频 5 画板 6 语音
	private String title;// 标题
	// private String summary;// 再要
	private String content;// 内容
	private String cover;// 封面
	private String[] pics; // 图片集
	private String video;// 视频地址
	private Long userId;
	private String[] tags;
	private int status; // -1 删除 1 下线 2发到个人中心 3 上圈子首页
	private int essence;// 加精 1 普通 2 加精
	private int audited;// 是否审核 1未审核 2审核通过
	private long adminId;// 最后操作管理员
	private double sort;
	private int readCnt;// 读数
	private int praiseCnt; // 赞数
	private int hitCnt; // 踩数量
	private int commentCnt; // 评论数
	private int shareCnt; // 分享数
	private long createTime;
	private long publishTime;
	private int anonymous;// 是否匿名
	private Long topicId;// 话题ID
	private String topic;// 话题
	private Long hotTime; // 最热时间
	private int hotLevel; // 最热等级
	private Integer pri;//私密状态，1,广场可见，2朋友圈可见

	private long flower = 0; // 收花数量

	private int godComment = GOD_COMMENT_NO;// 1 代表没有 2 代表有
	private Long godUid;
	private Long godCid;
	private String godContent;
	private Integer godCount;
	private String[] godPics; // 神评论图片集

	private String[] atUsers; // @的人
	private List<Map> draws = null; // 画板集
	private String voiceUrl; // 语音

	public Article(Long id, int type, String title, String content, String cover, long userId, String[] tags,
			int status, int essence, int audited, double sort, long adminId, Long groupId, String[] pics, String video,
			int anonymous, Long topicId, String topic, Long publishTime, String[] atUsers, List<Map> draws,
			String voiceUrl,Integer pri) {
		this.adminId = adminId;
		this.audited = audited;
		this.content = content;
		this.cover = cover;
		this.createTime = System.currentTimeMillis();
		this.essence = essence;
		super._id = id;
		this.status = status;
		if (publishTime != null && publishTime > 0) {
			this.publishTime = publishTime;
		} else {
			this.publishTime = this.createTime;
		}
		this.sort = sort;
		this.userId = userId;
		this.tags = tags;
		this.title = title;
		this.type = type;
		this.updateTime = this.createTime;
		this.groupId = groupId;
		this.pics = pics;
		this.video = video;
		this.anonymous = anonymous;
		this.topicId = topicId;
		this.topic = topic;
		this.atUsers = atUsers;
		this.draws = draws;
		this.voiceUrl = voiceUrl;
		this.pri = pri;
	}

	public Article() {

	}

	public List<Map> getDraws() {
		return draws;
	}

	public void setDraws(List<Map> draws) {
		this.draws = draws;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public String[] getAtUsers() {
		return atUsers;
	}

	public void setAtUsers(String[] atUsers) {
		this.atUsers = atUsers;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	// public String getSummary() {
	// return summary;
	// }
	//
	// public void setSummary(String summary) {
	// this.summary = summary;
	// }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getEssence() {
		return essence;
	}

	public void setEssence(int essence) {
		this.essence = essence;
	}

	public int getAudited() {
		return audited;
	}

	public void setAudited(int audited) {
		this.audited = audited;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public int getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(int readCnt) {
		this.readCnt = readCnt;
	}

	public int getPraiseCnt() {
		return praiseCnt;
	}

	public void setPraiseCnt(int praiseCnt) {
		this.praiseCnt = praiseCnt;
	}

	public int getCommentCnt() {
		return commentCnt;
	}

	public void setCommentCnt(int commentCnt) {
		this.commentCnt = commentCnt;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String[] getPics() {
		return pics;
	}

	public void setPics(String[] pics) {
		this.pics = pics;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getShareCnt() {
		return shareCnt;
	}

	public void setShareCnt(int shareCnt) {
		this.shareCnt = shareCnt;
	}

	public double getSort() {
		return sort;
	}

	public void setSort(double sort) {
		this.sort = sort;
	}

	public int getHitCnt() {
		return hitCnt;
	}

	public void setHitCnt(int hitCnt) {
		this.hitCnt = hitCnt;
	}

	public int getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(int anonymous) {
		this.anonymous = anonymous;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getGodUid() {
		return godUid;
	}

	public void setGodUid(Long godUid) {
		this.godUid = godUid;
	}

	public String getGodContent() {
		return godContent;
	}

	public void setGodContent(String godContent) {
		this.godContent = godContent;
	}

	public Integer getGodCount() {
		return godCount;
	}

	public void setGodCount(Integer godCount) {
		this.godCount = godCount;
	}

	public Long getGodCid() {
		return godCid;
	}

	public void setGodCid(Long godCid) {
		this.godCid = godCid;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getGodComment() {
		return godComment;
	}

	public void setGodComment(int godComment) {
		this.godComment = godComment;
	}

	public Long getHotTime() {
		return hotTime;
	}

	public void setHotTime(Long hotTime) {
		this.hotTime = hotTime;
	}

	public int getHotLevel() {
		return hotLevel;
	}

	public void setHotLevel(int hotLevel) {
		this.hotLevel = hotLevel;
	}

	public long getFlower() {
		return flower;
	}

	public void setFlower(long flower) {
		this.flower = flower;
	}

	public String[] getGodPics() {
		return godPics;
	}

	public void setGodPics(String[] godPics) {
		this.godPics = godPics;
	}

	public int getPri() {
		return pri;
	}

	public void setPri(int pri) {
		this.pri = pri;
	}
	
}
