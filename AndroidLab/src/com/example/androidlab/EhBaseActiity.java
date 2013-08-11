package com.example.androidlab;

import com.example.androidlab.tools.L;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class EhBaseActiity extends Activity{
	protected boolean logLifeCycle = true;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public abstract void initView();
	public abstract void setListener();

	public boolean isLogLifeCycle() {
		return logLifeCycle;
	}

	public void setLogLifeCycle(boolean logLifeCycle) {
		this.logLifeCycle = logLifeCycle;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.logLifeCycle("onNewIntent");
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.logLifeCycle("onResume");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		this.logLifeCycle("onPostCreate");
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		this.logLifeCycle("onPostResume");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		this.logLifeCycle("onRestart");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		this.logLifeCycle("onRestoreInstanceState");
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.logLifeCycle("onResume");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		this.logLifeCycle("onSaveInstanceState");
	}

	@Override
	protected void onStart() {
		super.onStart();
			this.logLifeCycle("onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.logLifeCycle("onStop");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.logLifeCycle("onActivityResult");
	}

	private void logLifeCycle(String lc){
		if (this.logLifeCycle) {
			L.d(this.toString() + ": " + lc);
		}
		
	}
	
	
}
