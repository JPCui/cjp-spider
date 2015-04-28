package cn.cjp.demo;

import org.apache.commons.lang.math.RandomUtils;

public class Test{

	public static void main(String[] args) {
		
		for(int i=0; i<100; i++){
			System.out.println(RandomUtils.nextInt(10));
		}
		
	}
	
}