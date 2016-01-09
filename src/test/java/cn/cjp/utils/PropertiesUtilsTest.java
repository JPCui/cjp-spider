package cn.cjp.utils;

import org.junit.Assert;
import org.junit.Test;

import cn.cjp.utils.PropertiesUtils;

public class PropertiesUtilsTest {
	
	@Test
	public void test(){
		PropertiesUtils propsUtils = new PropertiesUtils("http-proxy.properties");
		int proxyNum = Integer.parseInt(propsUtils.getPropValue("proxy.num"));
		
		Assert.assertNotNull(proxyNum);
	}

}
