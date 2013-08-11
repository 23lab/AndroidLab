package com.example.androidlab;

import com.example.androidlab.tools.L;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends EhBaseActiity {

	private ListView demos_lv;
	private String[] demos = { // Activity带包名的全名
			"com.example.androidlab.ErrorActivity",
			"com.example.androidlab.FirstActivity",		
			"com.example.androidlab.TaskMonitorActivity",		
			"com.example.androidlab.TestActivityResultActivity",		
	};
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.initView();
		this.setListener();
		this.setLogLifeCycle(false);
		this.context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void initView() {
		demos_lv = (ListView) findViewById(R.id.demos_lv);
		ArrayAdapter<String> sa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demos);
		demos_lv.setAdapter(sa);
	}

	@Override
	public void setListener() {
		demos_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				String selectedDemo = demos[pos];
				try {
					Intent intent = new Intent(MainActivity.this, Class.forName(selectedDemo));
					startActivity(intent);
				} catch (ClassNotFoundException e){
					L.d("Activity Name Error");
					Toast.makeText(context, "Activity Name Error: \n" + selectedDemo, Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});

	}

}
