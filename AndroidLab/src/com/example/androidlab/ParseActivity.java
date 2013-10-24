package com.example.androidlab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.androidlab.tools.L;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ParseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parse);
	    Parse.initialize(this, "qAV0lYjFp20ZbiS0KqRZgmcrqDL7jQrZRLzI0Bx9", "jZuNQh0d0I0o7tln8DcCiAJapjPIRSd81L3eY2Nd"); 
	    ParseUser.enableAutomaticUser();
	    ParseUser newUser = new ParseUser();
	    newUser.setUsername("erichua23");
	    newUser.setPassword("huating");
	    newUser.setEmail("huatingzl@gmail.com");
	    ParseACL acl = new ParseACL();
	    acl.setPublicReadAccess(true);
	    newUser.setACL(acl);
	    
	    newUser.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException arg0) {
				L.d("newUser.signUpInBackground");
			}
		});
	    ParseACL defaultACL = new ParseACL();
	    // Optionally enable public read access while disabling public write access.
	    // defaultACL.setPublicReadAccess(true);
	    ParseACL.setDefaultACL(defaultACL, true);
	    
	    ParseObject testObject = new ParseObject("TestObject");
	    testObject.put("foo", "android");

	    testObject.saveInBackground();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parse, menu);
		return true;
	}

}
