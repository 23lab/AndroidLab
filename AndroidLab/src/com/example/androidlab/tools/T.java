package com.example.androidlab.tools;

import android.content.Context;
import android.widget.Toast;

public class T {
	public static void show(Context ctx, String msg){
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}
}
