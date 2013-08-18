package com.example.androidlab.tools;

import android.util.Log;

public class L {
	private final static String DEFAULT_TAG = "AndroidLab";
	public static void d(String msg) {
		Log.d(DEFAULT_TAG, msg);
	}
	
	public static void d(Object msg) {
		d("" + msg);
	}
	public static void d(String tag, String msg){
		Log.d(tag, msg);
	}
	
	public static void d(Class<? extends Object> cls) {
		Log.d(DEFAULT_TAG, cls.getCanonicalName());
	}

}
