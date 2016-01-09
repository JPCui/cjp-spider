package cn.cjp.spider.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 站点
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name="sp_site")
public class SiteDomain extends BaseDomain {

	/**
	 * 站点URL
	 */
	String siteUrl;
	/**
	 * 站点名称
	 */
	String siteName;
	/**
	 * domain of the site
	 */
	String siteDomain;

	/**
	 * 备注
	 */
	String remarks;

	/**
	 * 获取站点URL
	 * 
	 * @return
	 */
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * 设置站点URL
	 * 
	 * @param siteUrl
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	/**
	 * 获取站点名称
	 * 
	 * @return
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * 设置站点名称
	 * 
	 * @param siteName
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(String siteDomain) {
		this.siteDomain = siteDomain;
	}
	/**
	 * 获取备注
	 * 
	 * @return
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置备注
	 * 
	 * @param remarks
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
