package com.xz.blog.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {
	
	public static boolean isEmail(String string) {
		if (string == null)
			return false;
		String regEx1 = "^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(string);
		if (m.matches())
			return true;
		else
			return false;
	}
	
	/**
	 * 不能包含特殊字符
	 */
	public static boolean isUserName(String s) {
		if(s==null) return false;
		String regx	= "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";
		
		Pattern p;
		Matcher m;
		p = Pattern.compile(regx);
		m = p.matcher(s);
		if (m.matches())
			return true;
		else
			return false;
	}

}
