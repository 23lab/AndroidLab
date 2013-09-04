package us.stupidx.androidpush;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.androidlab.EhBaseActiity;
import com.example.androidlab.R;

public class SocketClientActivity extends EhBaseActiity {

	private Socket socket = null;
	SocketClient main;
	protected String TAG = "SocketClientActivity";
	String SEND_MSG = "SEND_MSG";

	class Task extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... args) {
			if (args.length == 0) {
				main = new SocketClient();
				socket = main.getSocket();
				try {
					socket.setKeepAlive(true);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				setSocket(main.getSocket());

			} else if (SEND_MSG.equals(args[0])) {
				main.sendMessage("xxxx");
				return "";
			}
			return "";
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socket_client);
		new Task().execute();
		this.findViewById(R.id.send_socket_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Log.d(TAG, "sendMessage(xxxx)");
						new Task().execute(SEND_MSG);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.socket_client, menu);
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

	public Socket getSocket() {
		return socket;
	}

	public Socket setSocket(Socket socket) {
		this.socket = socket;
		return socket;
	}

}
