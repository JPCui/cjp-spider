package cn.cjp.spider.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * attributes in a type of site
 * @author Administrator
 *
 */
@Entity
@Table(name="sp_attribute")
public class AttributeDomain extends BaseDomain{
	
	private String name;
	
	private String value;
	
	@OneToMany(targetEntity=SiteType.class)
	private List<SiteType> siteTypes;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the siteTypes
	 */
	public List<SiteType> getSiteTypes() {
		return siteTypes;
	}

	/**
	 * @param siteTypes the siteTypes to set
	 */
	public void setSiteTypes(List<SiteType> siteTypes) {
		this.siteTypes = siteTypes;
	}
	
	
}
