package us.stupidx.androidpush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class SocketClient {
	private static final String TAG = "AndroidLab";
	private String hostName;
	private int portNum;
	private int delaySecond; // 发文接收返回报文延时
	private Socket socket = null;

	public SocketClient() {
		this.hostName = "192.168.1.106";
		this.portNum = 4000;
		this.delaySecond = 50000;
	}

	Socket getSocket() {
		try {
			if (socket == null || socket.isClosed()) {
				socket = new Socket(hostName, portNum);
				socket.setKeepAlive(true);
			}
		} catch (UnknownHostException e) {
			System.out.println("-->未知的主机名:" + hostName + " 异常");
		} catch (IOException e) {
			System.out.println("-hostName=" + hostName + " portNum=" + portNum
					+ "---->IO异常错误" + e.getMessage());
		}
		return socket;
	}

	public String sendMessage(String strMessage) {
		String str = "";
		String serverString = "";
		Socket socket;
		try {
			socket = getSocket();

			PrintWriter out = new PrintWriter(socket.getOutputStream());
			System.out.println("---->发送报文=" + strMessage);
			out.println(strMessage);
			out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			long sendTime = System.currentTimeMillis();
			long receiveTime = System.currentTimeMillis();
			boolean received = false; // 成功接收报文
			boolean delayTooLong = false;
			serverString = null;
			while (!received && !delayTooLong) {
				if (socket.getInputStream().available() > 0) {
					// serverString = in.readLine();
					char tagChar[];
					tagChar = new char[1024];
					int len;
					String temp;
					String rev = "";
					if ((len = in.read(tagChar)) != -1) {
						temp = new String(tagChar, 0, len);
						rev += temp;
						temp = null;
					}
					serverString = rev;
				}
				receiveTime = System.currentTimeMillis();
				if (serverString != null)
					received = true; // 字符串不为空，接收成功
				if ((receiveTime - sendTime) > delaySecond)
					delayTooLong = true; // 接收等待时间过长，超时
			}
			Log.d(TAG, "" + serverString);

			// in.close();
			// out.close();
			str = serverString;
			if (delayTooLong)
				str = "2190"; // 超时标志为真，返回超时码
			if (!received)
				str = "2190";
			// socket.close();
		} catch (UnknownHostException e) {
			System.out.println("---->出现未知主机错误! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			str = "2191";
			// System.exit(1);
		} catch (IOException e) {
			System.out.println("---->出现IO异常! 主机信息=" + this.hostName + " 端口号="
					+ this.portNum + " 出错信息=" + e.getMessage());
			e.printStackTrace();
			str = "2191";
		} catch (Exception e) {
			e.printStackTrace();
			str = "2177";
			System.out.println("---->出现未知异常" + e.getMessage());
		} finally {
			// socket = null;
			str.trim();
			// log.info("--->返回的socket通讯字符串="+str);
		}
		return str;
	}
}