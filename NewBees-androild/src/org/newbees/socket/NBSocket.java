package org.newbees.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NBSocket extends Socket {
	public NBSocket(RspListener rspListener) throws UnknownHostException,
			IOException {
		super();
		this.rspListener = rspListener;
	}
	
	public void connect(String host, int port) throws IOException {
		super.connect(new InetSocketAddress(host, port));
		startListenRsp();
	}

	public void send(String msg) {
		synchronized (this) {
			if (this.isConnected()) {
				try {
					JSONObject json = new JSONObject();
					// Send this msg to whom
					json.put("target", "");
					// Command
					json.put("cmd", "");

					Log.d("NewBees", this.getLocalAddress().getHostAddress());
					Log.d("NewBees", "" + this.getLocalPort());
					Log.d("NewBees", this.getRemoteSocketAddress().toString());
					Log.d("NewBees", this.getLocalSocketAddress().toString());

					JSONObject params = new JSONObject();
					params.put("p1", "value1");
					params.put("p2", "value2");
					json.put("params", params);

					Log.d("NewBees", json.toString());

					OutputStream socketOutputStream = this.getOutputStream();
					socketOutputStream.write(org.newbees.socket.Protocol
							.encode(json.toString()));
					socketOutputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private RspListener rspListener;
	private String myName = "";

	public void startListenRsp() {
		// 开始polling 数据
		new Thread() {
			StringBuilder buffer = new StringBuilder();
			// 0, 初始状态
			// 1, 已经找到C_A
			// 2, 已经找到C_D
			final int STATE_WAITING_FOR_START = 0;
			final int STATE_WAITING_FOR_END = 1;
			int state = STATE_WAITING_FOR_START;

			@Override
			public void run() {
				InputStream ips;
				try {
					ips = NBSocket.this.getInputStream();
					while (true) {
						int chr = -1;
						if ((chr = ips.read()) != -1) {
							Log.d("NewBees", "" + (char) chr);
							process(chr);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void process(int chr) {
				if (chr == Protocol.C_A) {
					state = STATE_WAITING_FOR_END;
					// 遇到C_A则开始准备等待C_D到来, 清空之前的数据
					buffer.delete(0, buffer.length());
				} else if (chr == Protocol.C_D) {
					// 如果已经接收过A, 则说明这是合法结束符
					if (state == STATE_WAITING_FOR_END) {
						String msg = buffer.toString();

						// check if it is the first response after connection, if it is,
						// update NBSocket.myName, then give a callback at the same time
						try {
							JSONObject jsonRsp = new JSONObject(Protocol.decode(msg));
							if ("conn_rsp".equals(jsonRsp.getString("cmd"))) {
								NBSocket.this.myName = jsonRsp.getString("clientId");
								rspListener.onConnection(NBSocket.this.myName);
								return;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						rspListener.onData(Protocol.decode(msg));
					}

					// 恢复状态
					state = STATE_WAITING_FOR_START;
				} else {
					buffer.append((char) chr);
				}

				if (buffer.length() > 4096) {
					// 每条消息不能超过4096, 超过则丢弃
					buffer.delete(0, buffer.length());
				}
			}

		}.start();
	}

	public interface RspListener {
		public void onConnection(String socketName);
		public void onData(String data);
	}
}
