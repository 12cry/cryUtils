package com.cry.cryUtils.common;

public class CommonUtils {
	public static String upperFirst(String str) {
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}
	public static String lowerFirst(String str) {
		return str.substring(0,1).toLowerCase()+str.substring(1);
	}
//	public static replaceSalsh(String str) {
//		str.replace("/", ".");
//	}
}
