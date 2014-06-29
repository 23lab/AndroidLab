package org.newbees.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;

import android.util.Log;

public class NBSocket extends Socket {

	public NBSocket() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NBSocket(InetAddress dstAddress, int dstPort,
			InetAddress localAddress, int localPort) throws IOException {
		super(dstAddress, dstPort, localAddress, localPort);
		// TODO Auto-generated constructor stub
	}

	public NBSocket(InetAddress dstAddress, int dstPort) throws IOException {
		super(dstAddress, dstPort);
		// TODO Auto-generated constructor stub
	}

	public NBSocket(Proxy proxy) {
		super(proxy);
		// TODO Auto-generated constructor stub
	}

	public NBSocket(SocketImpl impl) throws SocketException {
		super(impl);
		// TODO Auto-generated constructor stub
	}

	public NBSocket(String dstName, int dstPort, InetAddress localAddress,
			int localPort) throws IOException {
		super(dstName, dstPort, localAddress, localPort);
		// TODO Auto-generated constructor stub
	}

	public NBSocket(String dstName, int dstPort) throws UnknownHostException,
			IOException {
		super(dstName, dstPort);
		// TODO Auto-generated constructor stub
	}
	
	public void send(String msg) {
		synchronized (this) {
			if (this.isConnected()) {
				try {
					OutputStream socketOutputStream = this.getOutputStream();
					socketOutputStream.write(org.newbees.socket.Protocol.encode(msg));
					socketOutputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private RspListener rspListener;
	public void startListenRsp(final RspListener rspListener) {
		// 设置回调对象
		this.rspListener = rspListener;
		
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
						rspListener.onData(Protocol.decode(msg));
					}

					// 恢复状态
					state = STATE_WAITING_FOR_START;
				} else {
					buffer.append((char)chr);
				}

				if (buffer.length() > 4096) {
					// 每条消息不能超过4096, 超过则丢弃
					buffer.delete(0, buffer.length());
				}
			}
			
		}.start();
	}

	public interface RspListener{
		public void onData(String data);
	}
}
