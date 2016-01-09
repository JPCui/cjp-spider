package cn.cjp.spider.domain;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * base of domain
 * @author Administrator
 *
 */
@MappedSuperclass
public class BaseDomain {
	
	@Id
	private String id;
	
	private Date updateDate;
	
	private Date addDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the addDate
	 */
	public Date getAddDate() {
		return addDate;
	}

	/**
	 * @param addDate the addDate to set
	 */
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	
}
