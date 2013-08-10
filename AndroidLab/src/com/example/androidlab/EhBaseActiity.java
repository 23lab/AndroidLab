package com.example.androidlab;

import android.app.Activity;
import android.os.Bundle;

public abstract class EhBaseActiity extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initView();
		this.setListener();
	}
	
	public abstract void initView();
	public abstract void setListener();
}
