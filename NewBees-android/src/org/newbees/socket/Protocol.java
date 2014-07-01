package org.newbees.socket;

import java.util.Locale;

public class Protocol {
	public static final char C_A = 0x0001; // start of a msg
	public static final char C_B = 0x0002;
	public static final char C_C = 0x0003;
	public static final char C_D = 0x0004; // start of a msg
	public static final int MAX_MSG_LENGTH = 4096;

	// 协议中有6位来表示长度
	public static final int LENGTH_SPACE = 6;

	public static byte[] encode(String msg) {
		// use the first 2 byte as length
		int length = LENGTH_SPACE + msg.length();
		String lengtthInProtocol = String.format("%0" + LENGTH_SPACE + "d", length, Locale.CHINA);
		
		// 协议规则: C_A+6位length+body+C_D
		String pkgedMsg = C_A + lengtthInProtocol + msg + C_D;
		System.out.println(pkgedMsg);
		System.out.println("pkgedMsg length: " + pkgedMsg.length());
		return pkgedMsg.getBytes();
	}
	
	public static String decode(String rawMsg) {
		if (rawMsg != null && rawMsg.length() != 0) {
			return rawMsg.substring(6);
		} else {
			return "";
		}
	}
}
