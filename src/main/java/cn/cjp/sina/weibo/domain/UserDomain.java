package cn.cjp.sina.weibo.domain;

import cn.cjp.base.utils.JacksonUtil;

public class UserDomain {
	private Long id;
	
	private String screenName;
	
	private String profileImageUrl;
	
	private String profileUrl;
	
	private int statuses_count;
	
	private boolean verified;
	
	private String  verifiedReason;
	
	private String description;
	
	private String remark;
	
	private int verifiedType;
	
	/**
	 * 最新的一条微博
	 */
	private String text;
	
	private String createdAt;
	
	private String gender ;
	
	private String mbtype;
	
	private int ismember;
	
	private String valid;
	
	private String fansNum;
	
	private String desc1;
	
	private String desc2;
	
	public String toString(){
		return JacksonUtil.toJson(this);
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMbtype() {
		return mbtype;
	}

	public void setMbtype(String mbtype) {
		this.mbtype = mbtype;
	}

	public int getIsmember() {
		return ismember;
	}

	public void setIsmember(int ismember) {
		this.ismember = ismember;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getFansNum() {
		return fansNum;
	}

	public void setFansNum(String fansNum) {
		this.fansNum = fansNum;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	public int getVerifiedType() {
		return verifiedType;
	}

	public void setVerifiedType(int verifiedType) {
		this.verifiedType = verifiedType;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
