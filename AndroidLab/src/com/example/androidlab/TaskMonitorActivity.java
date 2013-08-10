package com.example.androidlab;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskMonitorActivity extends EhBaseActiity {

	private Button refresh_tasks_btn;
	private TextView all_tasks_info_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_monitor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.task_monitor, menu);
		return true;
	}

	@Override
	public void initView() {
		refresh_tasks_btn = (Button)this.findViewById(R.id.refresh_tasks_btn);
		all_tasks_info_tv = (TextView)this.findViewById(R.id.all_tasks_info_tv);
	}

	@Override
	public void setListener() {
		refresh_tasks_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActivityManager am = (ActivityManager) TaskMonitorActivity.this.getSystemService(ACTIVITY_SERVICE);
				List<RunningTaskInfo> runningTasks = am.getRunningTasks(100);
				String info = "";
				for (RunningTaskInfo ti: runningTasks) {
					info += ti.baseActivity.getClassName() + ":" + ti.numActivities + "; \n";
				}
				all_tasks_info_tv.setText(info);
			}
			
		});
	}

}
