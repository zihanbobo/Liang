package com.we.models;

/** 活动通知 */
public class NoticeActivity extends AbstractDocument {

	/** 
	 * 
	 */
	private static final long serialVersionUID = -6260890925457156479L;

	private String title;
	private String content;
	private Long adminId;
	private int status;
	private long createTime;
	private String actLink;
	private Long actEndTime =0l;
	private Integer lkFlag ;
	private String pic;
	private double sort;
	
	public NoticeActivity(String title, String content, int status, Long adminId, String actLink,
			Long actEndTime,Integer lkFlag,String pic) {
		super();
		this.title = title;
		this.content = content;
		this.adminId = adminId;
		this.status = status;
		this.actLink = actLink;
		if(actEndTime!=null) {
			this.actEndTime =actEndTime;
		}
		this.lkFlag = lkFlag;
		this.pic = pic;
		this.createTime = System.currentTimeMillis();
		this.updateTime = System.currentTimeMillis();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getActLink() {
		return actLink;
	}
	public void setActLink(String actLink) {
		this.actLink = actLink;
	}
	public Long getActEndTime() {
		return actEndTime;
	}
	public void setActEndTime(Long actEndTime) {
		this.actEndTime = actEndTime;
	}
	public Integer getLkFlag() {
		return lkFlag;
	}
	public void setLkFlag(Integer lkFlag) {
		this.lkFlag = lkFlag;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public double getSort() {
		return sort;
	}
	public void setSort(double sort) {
		this.sort = sort;
	}

}
