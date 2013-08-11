package com.example.androidlab;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TestActivityResultActivity extends EhBaseActiity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_activity_result);
		this.initView();
		this.setListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_activity_result, menu);
		return true;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

}
