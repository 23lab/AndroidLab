package us.stupidx.filelab;

import java.io.File;
import java.io.FilenameFilter;

import com.example.androidlab.EhBaseActiity;
import com.example.androidlab.R;
import com.example.androidlab.R.layout;
import com.example.androidlab.R.menu;
import com.example.androidlab.tools.L;
import com.example.androidlab.tools.T;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class ReadDataAppActivity extends EhBaseActiity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_data_app);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_data_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		L.d("onOptionsItemSelected");
		String packageCodePath = this.getPackageCodePath();
		T.show(this, packageCodePath);
		
		T.show(this, this.getPackageResourcePath());
		String fileName = "";
		File file = new File("/data/app/" + this.getPackageName() + "-2.apk");
		T.show(this, "exists: " + file.exists());
		T.show(this, "exists: " + file.exists());
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
