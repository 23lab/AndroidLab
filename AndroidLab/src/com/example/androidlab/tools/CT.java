package com.example.androidlab.tools;

public class CT {
	public static String array2Str(String[] strArr){
		if (strArr == null || strArr.length == 0) {
			return "";
		}
		
		String str = "";
		for (int i = 0; i < strArr.length; i++) {
			str += "" + strArr[i];
		}
		return str;
	}
}
