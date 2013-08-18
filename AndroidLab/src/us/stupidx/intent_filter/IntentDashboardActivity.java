package us.stupidx.intent_filter;

import com.example.androidlab.EhBaseActiity;
import com.example.androidlab.R;
import com.example.androidlab.R.layout;
import com.example.androidlab.R.menu;
import com.example.androidlab.tools.L;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

public class IntentDashboardActivity extends EhBaseActiity {

	private final static String TAG = IntentDashboardActivity.class.getCanonicalName();
	private ListView intent_flag_lv;
	private String[] intentFlags = {
			"Intent.FLAG_ACTIVITY_NEW_TASK_" + Intent.FLAG_ACTIVITY_NEW_TASK,
			"Intent.FLAG_ACTIVITY_SINGLE_TOP" + Intent.FLAG_ACTIVITY_SINGLE_TOP,
			"Intent.FLAG_ACTIVITY_MULTIPLE_TASK" + Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
	};
	private Button launch_activity_by_flags_btn;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_dashboard);
		intent_flag_lv = (ListView) findViewById(R.id.intent_flag_lv);
		ArrayAdapter<String> adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, intentFlags );
		intent_flag_lv.setAdapter(adpt);
		intent_flag_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		launch_activity_by_flags_btn = (Button) findViewById(R.id.launch_activity_by_flags_btn);
		launch_activity_by_flags_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SparseBooleanArray checkedList = intent_flag_lv.getCheckedItemPositions();
				for (int i = 0; i < intentFlags.length; i ++) {
					if (checkedList.get(i)) {
						L.d(intentFlags[i]);
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intent_dashboard, menu);
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
