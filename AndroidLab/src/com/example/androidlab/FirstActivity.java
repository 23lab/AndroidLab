package com.example.androidlab;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class FirstActivity extends EhBaseActiity {

	private View check_running_tasks_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		this.initView();
		this.setListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first, menu);
		return true;
	}

	@Override
	public void initView() {
		check_running_tasks_btn = this.findViewById(R.id.check_running_tasks_btn);
	}

	@Override
	public void setListener() {
		check_running_tasks_btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				ActivityManager am = (ActivityManager) FirstActivity.this.getSystemService(ACTIVITY_SERVICE);
				List<RunningTaskInfo> runningTasks = am.getRunningTasks(100);
				String info = "";
				for (RunningTaskInfo ti: runningTasks) {
					info += ti.baseActivity.getClassName() + ":" + ti.numActivities + "; \n";
				}
				Toast.makeText(FirstActivity.this, info, Toast.LENGTH_LONG).show();
			}});
	}

}
