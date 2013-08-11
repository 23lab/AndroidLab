package com.example.androidlab.tools;

import android.util.Log;

public class L {
	private final static String DEFAULT_TAG = "";
	public static void d(String msg) {
		Log.d(DEFAULT_TAG, msg);
	}
	
	public static void d(Object msg) {
		d("" + msg);
	}
	public static void d(String tag, String msg){
		Log.d(tag, msg);
	}

}
