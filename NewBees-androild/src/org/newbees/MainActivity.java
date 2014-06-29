package org.newbees;

import java.io.IOException;
import java.net.UnknownHostException;

import org.newbees.socket.NBSocket;
import org.newbees.socket.NBSocket.RspListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.kevinsawicki.wishlist.Toaster;

public class MainActivity extends Activity {

	private static final int PORT = 8111;
	private static final String DOMAIN = "198.52.100.190";

	private NBSocket skt;

	public void onClicked(View v) {
		Thread child = new Thread() {
			@Override
			public void run() {
				try {
					skt = new NBSocket(DOMAIN, PORT);
					skt.startListenRsp(new RspListener() {
						@Override
						public void onData(final String data) {
							Log.d("NewBees", "Server Rsp: " + data);
							MainActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toaster.showShort(MainActivity.this, "Server Rsp: " + data);
								}
							});
						}
					});
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		child.start();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onSendMessageClicked(View v) {
		if (skt != null && skt.isConnected()) {
			skt.send("msg use NBSocket send");
		} else {
			Log.d("NewBees", "socket not ready");
		}
	}
}
