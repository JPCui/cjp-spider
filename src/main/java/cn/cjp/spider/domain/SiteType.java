package cn.cjp.spider.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sp_site_type")
public class SiteType extends BaseDomain {

	/**
	 * type for site
	 */
	private String name;

	/**
	 * get name of site type
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name of site type
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
