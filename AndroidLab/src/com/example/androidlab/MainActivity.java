package com.example.androidlab;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends EhBaseActiity {

	private Button launch_first_btn;
	private TextView monitor_body_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void initView() {
		launch_first_btn = (Button) this.findViewById(R.id.launch_first_btn);
		monitor_body_tv = (TextView) this.findViewById(R.id.monitor_body_tv);
	}

	@Override
	public void setListener() {
		this.launch_first_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, TaskMonitorActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}
		});

	}

}
