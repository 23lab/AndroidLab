package com.example.androidlab;

import java.util.List;

import com.example.androidlab.tools.CT;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Instrumentation.ActivityMonitor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskMonitorActivity extends EhBaseActiity {

	private Button refresh_tasks_btn;
	private TextView all_tasks_info_tv;
	private Button refresh_app_procs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_task_monitor);
		this.initView();
		this.setListener();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_monitor, menu);
		return true;
	}

	@Override
	public void initView() {
		refresh_tasks_btn = (Button)this.findViewById(R.id.refresh_tasks_btn);
		all_tasks_info_tv = (TextView)this.findViewById(R.id.all_tasks_info_tv);
		refresh_app_procs = (Button) this.findViewById(R.id.refresh_app_procs);
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
					info += "    " + ti.topActivity.toString() + "; \n";
				}
				all_tasks_info_tv.setText(info);
				
			}
			
		});
		
		refresh_app_procs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager am = (ActivityManager) TaskMonitorActivity.this.getSystemService(ACTIVITY_SERVICE);
				List<RunningAppProcessInfo> procs = am.getRunningAppProcesses();
				String txt = "";
				for (RunningAppProcessInfo info: procs) {
					txt += CT.array2Str(info.pkgList)  + "; \n";
				}
				
				all_tasks_info_tv.setText(txt);
			}
		});
	}

}
