package org.newbees.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NBSocket extends Socket {
	private static final String NEW_BEES = "NewBees";

	public NBSocket(RspListener rspListener) throws UnknownHostException,
			IOException {
		super();
		this.rspListener = rspListener;
	}

	public void connect(final String host, final int port) throws IOException {
		// connection thread
		SocketThread nbSocketThread = new SocketThread(host, port);
		nbSocketThread.start();
		try {
			// use this to make a call in the SocketThread
			nbSocketThread.queue.put("");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) {
		synchronized (this) {
			if (!this.isConnected()) {
				Log.w(NEW_BEES, "socket not connected!");
				return;
			}
			try {
				JSONObject json = new JSONObject();
				// Send this msg to whom
				json.put("target", "");
				// Command
				json.put("cmd", "");

				Log.d(NEW_BEES, this.getLocalAddress().getHostAddress());
				Log.d(NEW_BEES, "" + this.getLocalPort());
				Log.d(NEW_BEES, this.getRemoteSocketAddress().toString());
				Log.d(NEW_BEES, this.getLocalSocketAddress().toString());

				JSONObject params = new JSONObject();
				params.put("p1", "value1");
				params.put("p2", "value2");
				json.put("params", params);

				Log.d(NEW_BEES, json.toString());

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

	private RspListener rspListener;
	private String myName = "";

	public interface RspListener {
		public void onConnection(NBMsg msg);

		public void onData(NBMsg msg);
	}

	private class SocketThread extends Thread {
		StringBuilder buffer = new StringBuilder();
		// 0, 初始状态
		// 1, 已经找到C_A
		// 2, 已经找到C_D
		final int STATE_WAITING_FOR_START = 0;
		final int STATE_WAITING_FOR_END = 1;
		int state = STATE_WAITING_FOR_START;
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

		private String host = "";
		private int port = 0;
		public SocketThread(String host, int port) {
			this.host = host;
			this.port = port;
		}
		@Override
		public void run() {
			try {
				NBSocket.super.connect(new InetSocketAddress(this.host, this.port));
				InputStream ips = NBSocket.this.getInputStream();
				while (true) {
					String msg;
					while ((msg = queue.poll()) != null) {
						// implement outer call in this thread
					}
					int chr = -1;
					if ((chr = ips.read()) != -1) {
						Log.d(NEW_BEES, "" + (char) chr);
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
					NBMsg nBmsg = new NBMsg(Protocol.decode(msg));

					// check if it is the first connection msg
					if (nBmsg.isConnectionMsg()) {
						NBSocket.this.myName = nBmsg.getParam("clienId");
						rspListener.onConnection(nBmsg);
						return;
					}
					rspListener.onData(new NBMsg(Protocol.decode(msg)));
				}

				// 恢复状态
				state = STATE_WAITING_FOR_START;
			} else {
				buffer.append((char) chr);
			}

			if (buffer.length() > Protocol.MAX_MSG_LENGTH) {
				// 每条消息不能超过4096, 超过则丢弃
				buffer.delete(0, buffer.length());
			}
		}
	}
}
