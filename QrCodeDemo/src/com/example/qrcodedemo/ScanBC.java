package com.example.qrcodedemo;

import java.io.IOException;
import java.util.Vector;

import oicq.wlogin_sdk.tools.util;

import org.json.JSONException;
import org.json.JSONObject;

import us.stupidx.tools.Logger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;

public class ScanBC extends Activity implements SurfaceHolder.Callback {

	private static final float BEEP_VOLUME = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

	private CaptureActivityHandler handler;

	private enum Source {
		NATIVE_APP_INTENT, PRODUCT_SEARCH_LINK, ZXING_LINK, NONE
	}

	private Intent mIntent;
	private Bundle mBundle;

	private ViewfinderView viewfinderView;
	private MediaPlayer mediaPlayer;
	private Result lastResult;
	private boolean hasSurface;
	private boolean playBeep = true;
	private Source source;
	private String returnUrlTemplate;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;

	private long mAppid = 715017003;
	private long mRole = 114;
	private int version = 0x1;

	public String retContent;

	public TextView loginResult;
	public TextView prompt;
	public LinearLayout showCard;
	PopupWindow popupWindow;

	/*
	 * private final OnCompletionListener beepListener = new
	 * OnCompletionListener() { public void onCompletion(MediaPlayer
	 * mediaPlayer) { mediaPlayer.seekTo(0); } };
	 */
	public Handler getHandler() {
		return handler;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

			setContentView(R.layout.scanbc);

			mIntent = this.getIntent();
			mBundle = mIntent.getExtras();

			CameraManager.init(getApplication());
			viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);

			// showCard = (LinearLayout)findViewById(R.id.showCard);
			// showCard.setVisibility(View.INVISIBLE);
			prompt = (TextView) findViewById(R.id.prompt);
			handler = null;
			lastResult = null;
			hasSurface = false;
			inactivityTimer = new InactivityTimer(this);
		} catch (Exception e) {
			Logger.d(e.toString());
		}
	}// end of onCreate

	@Override
	protected void onResume() {
		// 初始化扫描界面
		super.onResume();
		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}

		source = Source.NONE;
		decodeFormats = null;
		characterSet = null;
		// initBeepSound();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

		} catch (IOException io) {
			showErrMsg("cameraBug");
		} catch (RuntimeException e) {
			showErrMsg("cameraBug");
		}

		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	private void showErrMsg(String strMsg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(strMsg);
		builder.setPositiveButton("buttonOK", new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 成功获取二维码，在handler中调用这个方法，回传bitmap
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		if (barcode == null) {
			handleDecodeInternally(rawResult, null);
		} else {
			playBeepSoundAndVibrate();// / 播放声音和振动代表成功获取二维码
			drawResultPoints(barcode, rawResult);

			switch (source) {
			case NATIVE_APP_INTENT:
			case PRODUCT_SEARCH_LINK:
				handleDecodeExternally(rawResult, barcode);
				break;
			case ZXING_LINK:
				if (returnUrlTemplate == null) {
					handleDecodeInternally(rawResult, barcode);
				} else {
					handleDecodeExternally(rawResult, barcode);
				}
				break;
			case NONE: {
				handleDecodeInternally(rawResult, barcode);
			}
				break;
			}
		}
	}

	/**
	 * 把图片截图下来之后,标记二维码所在的点 Superimpose a line for 1D or dots
	 * for 2D to highlight the key features of the barcode.
	 * 
	 * @param barcode
	 *            A bitmap of the captured image.
	 * @param rawResult
	 *            The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.imageBorder));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);
			paint.setColor(getResources().getColor(R.color.points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.UPC_A))
					|| (rawResult.getBarcodeFormat()
							.equals(BarcodeFormat.EAN_13))) {
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	/**
	 * 二维码处理成功
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
		viewfinderView.setVisibility(View.GONE);
		String content = rawResult.getText();
		Log.d("==============", content);
		int index = content.indexOf("?k=") + 3;
		content = content.substring(content.indexOf("?k=") + 3, index + 32);// 二维码的长度是32，控制长度
		// content = tools.getStrFromJson( content, "k");
		// View r = findViewById(R.id.relativeLayoutResult);
		// r.setVisibility(View.VISIBLE);
		Log.d("==============", "the bar code:" + content);
		Intent intent = new Intent();
		byte[] temp = util.base64_decode_url(content.getBytes(),
				content.length());
		intent.putExtra("CODE2D", temp);
		ScanBC.this.setResult(1, intent);
		ScanBC.this.finish();
		// int ret = sendToServer(temp);
	}

	/*
	 * private int sendToServer(byte[] code) { int ret = 0; byte[] st = null;
	 * byte[] st_key = null; int pos = 0; int clientIP = 0; int clientPort = 0;
	 * int pad = 0; long uin = Long.parseLong(strUin);
	 * 
	 * SharedPreferences settings = getSharedPreferences("CONFIG_FILE", 0);
	 * if(settings.getBoolean("LoginAnima", true)) pad = 0;//1：没有动画
	 * 0：有动画要靠一靠 else pad = 1;//跳过动画步骤 Log.d("==============",
	 * "nedd anima???:"+pad); WloginSigInfo info; info =
	 * UserInfo.mLoginHelper.mG.get_siginfo(uin, mAppid); if(info == null){ ret
	 * = util.E_NO_KEY; tools.showDialog(ScanBC.this, "找不到这个帐号的票据");
	 * return -1; } //找到了票据 UserInfo.info.get_clone(info, new byte[0]); st
	 * = UserInfo.info._userStSig; st_key = UserInfo.info._userSt_Key; int
	 * bodyLength = 1 + 28 + 14 + 2 + 1 + 8 + 2 + st.length + 2 + code.length +
	 * 16 + 1 + 2 + 1; byte[] tmpBody = new byte[bodyLength];
	 * 
	 * pos = PkgTools.fillBegin(tmpBody, pos); pos =
	 * PkgTools.fillDBPkgHead(tmpBody, pos, bodyLength, 0x3, clientIP,
	 * clientPort, 21, pad); pos = PkgTools.fillCldPkgHead(tmpBody, pos, uin,
	 * version); pos = PkgTools.fill0x3Body(tmpBody, pos, uin, code, st,
	 * version, pad); pos = PkgTools.fillEnd(tmpBody, pos); mReqContext = new
	 * TransReqContext();
	 * 
	 * UserInfo.g_context = ScanBC.this; try{ mReqContext.set_body(tmpBody); ret
	 * = UserInfo.mLoginHelper.RequestTransport(0, st, st_key, strUin, mAppid,
	 * mRole, mReqContext); } catch(Exception e) { {
	 * 
	 * StringWriter sw = new StringWriter(); PrintWriter pw = new
	 * PrintWriter(sw, true); e.printStackTrace(pw); pw.flush(); sw.flush();
	 * String s = sw.toString(); util.LOGW("exception", s); } }
	 * 
	 * return ret; }
	 */

	private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
		viewfinderView.drawResultBitmap(barcode);
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(VIBRATE_DURATION);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();

			/*
			 * ByteArrayOutputStream outByteStream = new
			 * ByteArrayOutputStream(); ObjectOutputStream outStream = new
			 * ObjectOutputStream(outByteStream); outStream.writeObject(hander);
			 * bundle.putByteArray("handle", outByteStream.toByteArray());
			 */
			ScanBC.this.setResult(RESULT_OK, intent);
			ScanBC.this.finish();
			/*
			 * if (source == Source.NATIVE_APP_INTENT) {
			 * setResult(RESULT_CANCELED); finish(); return true; } else if
			 * ((source == Source.NONE || source == Source.ZXING_LINK) &&
			 * lastResult != null) { resetStatusView(); if (handler != null) {
			 * handler.sendEmptyMessage(R.id.restart_preview); } return true; }
			 */
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * private void initBeepSound() { if (playBeep && mediaPlayer == null) {
	 * setVolumeControlStream(AudioManager.STREAM_MUSIC); mediaPlayer = new
	 * MediaPlayer(); mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	 * mediaPlayer.setOnCompletionListener(beepListener); AssetFileDescriptor
	 * file = getResources().openRawResourceFd( R.raw.beep); try {
	 * mediaPlayer.setDataSource(file.getFileDescriptor(),
	 * file.getStartOffset(), file.getLength()); file.close();
	 * mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME); mediaPlayer.prepare(); }
	 * catch (IOException e) { mediaPlayer = null; } } }
	 */

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public String getStrFromJson(String str, String key) {
		String temp;
		try {
			JSONObject obj = new JSONObject(str);
			temp = obj.get(key).toString();
		} catch (JSONException je) {
			temp = "";
		}
		return temp;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}
