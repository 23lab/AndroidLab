package com.example.activitykillnoresult;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.androidlab.EhBaseActiity;
import com.example.androidlab.R;
import com.example.androidlab.tools.L;

public class NoResultSenderActivity extends EhBaseActiity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_result_sender);
		L.d(NoResultRecieveActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_result_sender, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)  {
		Intent intent = new Intent(this, NoResultRecieveActivity.class);
		this.startActivityForResult(intent, 9527);
//		this.finish();
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
