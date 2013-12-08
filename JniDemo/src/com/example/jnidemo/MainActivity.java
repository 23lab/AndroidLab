package com.example.jnidemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.localRef500).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						localRef500();
					}
				});

		Button localRef512 = ((Button)findViewById(R.id.localRef512));
		localRef512.setTextColor(Color.RED);
		localRef512.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						localRef512();
					}
				});
		

		findViewById(R.id.localRef256).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						localRef256();
					}
				});
	}

	public native static void localRef500();

	public native static void localRef512();

	public native static void localRef256();
	
	static {
		System.loadLibrary("JniDemo");
	}
}
