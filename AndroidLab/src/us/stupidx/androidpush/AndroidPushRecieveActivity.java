package us.stupidx.androidpush;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.androidlab.EhBaseActiity;
import com.example.androidlab.R;

public class AndroidPushRecieveActivity extends EhBaseActiity{

	protected static final String TAG = "AndroidPush";
	SocketIO socket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_push_recieve);

		try {
			socket = new SocketIO("http://androidpush-25631.apne1.actionbox.io:3000/");
			socket.connect(new IOCallback() {
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					try {
						System.out.println("Server said:" + json.toString(2));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onMessage(String data, IOAcknowledge ack) {
					System.out.println("Server said: " + data);
				}

				@Override
				public void onError(SocketIOException socketIOException) {
					System.out.println("an Error occured");
					socketIOException.printStackTrace();
				}

				@Override
				public void onDisconnect() {
					System.out.println("Connection terminated.");
				}

				@Override
				public void onConnect() {
					Log.d("AndroidPush", "Connection established");
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					System.out
							.println("Server triggered event '" + event + "'");
				}
			});

			// This line is cached until the connection is establisched.
			socket.send("Hello Server!");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android_push_recieve, menu);
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
