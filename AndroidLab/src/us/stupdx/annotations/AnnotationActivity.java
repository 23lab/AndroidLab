package us.stupdx.annotations;

import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidlab.R;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_annotation)
public class AnnotationActivity extends Activity {
	@ViewById(R.id.test_annotation_btn)
	Button testBtn;
	
	@Click
	void test_annotation_btn(){
		Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.annotation, menu);
		return true;
	}

}
