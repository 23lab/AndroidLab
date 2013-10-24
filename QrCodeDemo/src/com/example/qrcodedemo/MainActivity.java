package com.example.qrcodedemo;

import org.apache.http.conn.ManagedClientConnection;

import us.stupidx.tools.Logger;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zijunlin.Zxing.Demo.CaptureActivity;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends Activity {

	private Button give_login_btn;
	private Button recieve_login_btn;
	private ImageView qr_code_iv;
	private EditText openid_et;
	private EditText acctoken_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		give_login_btn = (Button) findViewById(R.id.give_login_btn);
		recieve_login_btn = (Button) findViewById(R.id.recieve_login_btn);
		qr_code_iv = (ImageView) findViewById(R.id.qr_code_iv);

		openid_et = (EditText) findViewById(R.id.openid_et);
		acctoken_et = (EditText) findViewById(R.id.acctoken_et);

		give_login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 100);
			}
		});

		recieve_login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				start();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && data.getExtras() != null) {
			startGiveLogin(data.getExtras().getString("id"));
			Toast.makeText(this, data.getExtras().getString("id"),
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private final WebSocketConnection mConnectionRecieve = new WebSocketConnection();
	private final WebSocketConnection mConnectionGive = new WebSocketConnection();

	private void startGiveLogin(final String id) {
		final String wsuri = "ws://tealab-43734.apne1.actionbox.io:4000/";

		if (!mConnectionGive.isConnected()) {

			try {
				mConnectionGive.connect(wsuri, new WebSocketHandler() {

					@Override
					public void onOpen() {
						Logger.d("Status: Connected to " + wsuri);
						mConnectionGive.sendTextMessage("{\"id\": " + id
								+ ", \"body\": {\"openid\": \""
								+ openid_et.getText().toString()
								+ "\", \"accessToken\": \""
								+ acctoken_et.getText().toString() + "\"}}");

					}

					@Override
					public void onTextMessage(String payload) {
						Toast.makeText(MainActivity.this, payload,
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onClose(int code, String reason) {
						Logger.d("Connection lost.");
					}
				});
			} catch (WebSocketException e) {

				Logger.d(e.toString());
			}
		} else {
			Logger.d("Status: Connected to " + wsuri);
			mConnectionGive.sendTextMessage("{\"id\": " + id
					+ ", \"body\": {\"openid\": \""
					+ openid_et.getText().toString()
					+ "\", \"accessToken\": \""
					+ acctoken_et.getText().toString() + "\"}}");

		}
	}

	private void start() {
		final String wsuri = "ws://tealab-43734.apne1.actionbox.io:4000/";

		try {
			mConnectionRecieve.connect(wsuri, new WebSocketHandler() {

				@Override
				public void onOpen() {
					Logger.d("Status: Connected to " + wsuri);
					mConnectionRecieve.sendTextMessage("reg");
				}

				@Override
				public void onTextMessage(String payload) {
					try { // data back
						Long.parseLong(payload);
						try {
							final Bitmap bmp = create2DCode(payload);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									((ImageView) findViewById(R.id.qr_code_iv))
											.setImageBitmap(bmp);
								}
							});
						} catch (WriterException e) {
							e.printStackTrace();
						}
						Logger.d("Got echo: " + payload);
						Toast.makeText(MainActivity.this, payload,
								Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// other, login info in demo
						Toast.makeText(MainActivity.this, payload,
								Toast.LENGTH_LONG).show();
					}

				}

				@Override
				public void onClose(int code, String reason) {
					Logger.d("Connection lost.");
				}
			});
		} catch (WebSocketException e) {

			Logger.d(e.toString());
		}
	}

	public Bitmap create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

}
