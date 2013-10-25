package us.stupidx.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
 * FileName：Logger.java
 * Description：logger wrapper
 * History：
 * 1.0 samuelmo Apr 3, 2010 Create
 */

public class Logger {
	private static final byte LOG_NULL = 0; // null device

	// log
	public static final byte LOG_CONSOLE = 1; // console log
	public static final byte LOG_FILE = 2; // file log
	public static final byte LOG_BOTH = 3; // both

	private static String DEFAULT_TAG = "DaiGoal";
	private static byte logDevice = LOG_CONSOLE;

	private static FileLogHandler fileLog;

	private static boolean enableCustomProxy = false;

	public static boolean isEnableCustomProxy() {
		return enableCustomProxy;
	}

	static {
		if (logDevice > LOG_CONSOLE) {
			fileLog = new FileLogHandler();
		}
	}

	public static void e(String tag, Throwable throwable) {
		if (throwable == null)
			return;

		StackTraceElement[] stacks = new Throwable().getStackTrace();
		if (stacks.length > 1) {
			StringBuilder sb = new StringBuilder();
			sb.append("class : ").append(stacks[1].getClassName())
					.append("; line : ").append(stacks[1].getLineNumber());
			Log.d(tag, sb.toString());
		}

		throwable.printStackTrace();
	}

	public static void d(String tag, String msg, int device) {
		if (msg == null)
			msg = "NULL MSG";

		switch (device) {
		case LOG_CONSOLE:
			Log.d(tag, msg);
			break;
		case LOG_FILE:
			writeToLog(tag + "\t" + msg);
			break;
		case LOG_BOTH:
			Log.d(tag, msg);
			writeToLog(tag + "\t" + msg);
			break;
		case LOG_NULL:
		default:
			break;
		}
	}

	public static void timeStamp(Exception exception, String step) {
		StackTraceElement stackTraceElement = exception.getStackTrace()[0];
		String className = stackTraceElement.getClassName();
		String methodName = stackTraceElement.getMethodName();
		int lineNum = stackTraceElement.getLineNumber();
		if (step == null)
			step = "";
		else
			step += "-";
		Log.d("TimeStamp", step + className + "." + methodName + "():"
				+ lineNum);
	}

	private static void writeToLog(String log) {
		Message msg = fileLog.obtainMessage();
		msg.obj = log;
		fileLog.sendMessage(msg);
	}

	/**
	 * Get whether log switch is on.
	 */
	public static boolean getIsLogged() {
		return (logDevice != LOG_NULL);
	}

	/**
	 * Set whether log switch is on.
	 */
	public static void setIsLogged(boolean isLogged) {
		if (isLogged) {
			logDevice = LOG_CONSOLE;
		} else {
			logDevice = LOG_NULL;
		}
	}

	public static void init() {
		if (logDevice > LOG_CONSOLE) {
			fileLog = new FileLogHandler();
		}
	}

	private static class FileLogHandler extends Handler {
		private boolean hasSDCard = true;
		private FileOutputStream logOutput;
		private File logFile;

		FileLogHandler() {
			hasSDCard = FileUtils.hasExternalStorage();
			if (hasSDCard) {
				try {
					logFile = FileUtils.getLogFile();
					if (!logFile.exists()) {
						logFile.createNewFile();
					}
				} catch (IOException e) {
				}
			}
		}

		public void handleMessage(Message msg) {
			if (!hasSDCard) {
				return;
			}

			try {
				String log = (String) msg.obj + "\n";
				if (log != null) {
					byte[] logData = log.getBytes();
					getLogOutput().write(logData, 0, logData.length);
				}
			} catch (Exception e) {
			}
		}

		FileOutputStream getLogOutput() throws Exception {
			if (logOutput == null) {
				logOutput = new FileOutputStream(logFile, true);
			}
			return logOutput;
		}
	}

	// 获取当前线程的函数调用栈从顶向下指定层的类名
	public static String getTag(String subTag, int index) {
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		if (index < 0 || index >= traces.length) {
			return "";
		}
		String clsName = traces[index].getClassName();
		String methodName = traces[index].getMethodName();
		String shortClsName = "";
		int dot = clsName.lastIndexOf('.');
		if (dot != -1) {
			shortClsName = clsName.substring(dot + 1);
		}

		if (CommonUtil.ckIsEmpty(subTag)) {
			return DEFAULT_TAG + " " + shortClsName + "." + methodName;
		} else {
			return DEFAULT_TAG + ">" + subTag + " " + shortClsName + "."
					+ methodName;
		}
	}

	// 打log的函数（调用这个函数的函数），是在函数栈的第5层
	public static int e(String msg) {
		return Log.e(getTag(null, STACK_TRACE_DEEP), msg);
	}

	// 打log的函数（调用这个函数的函数），是在函数栈的第5层
	public static int e(String subTag, String msg) {
		return Log.e(getTag(subTag, STACK_TRACE_DEEP), msg);
	}

	// 打log的函数（调用这个函数的函数），是在函数栈的第5层
	public static void d(String msg) {
		Log.d(getTag(null, STACK_TRACE_DEEP), msg);
	}

	public static void d(Bundle b) {
		String tag = getTag(null, STACK_TRACE_DEEP);
		if (b == null) {
			Log.d(tag, "empty bundle");
			return;
		}

		Set<String> keys = b.keySet();// 手Q通过这种方式传 platformId
		for (String key : keys) {
			if (b.get(key) instanceof byte[]) {
				Log.d(tag,
						key + ":" + HexUtil.bytes2HexStr(b.getByteArray(key)));
			} else if (b.get(key) instanceof String) {
				Log.d(tag, key + ":" + b.getString(key));
			} else if (b.get(key) instanceof Long) {
				Log.d(tag, key + ":" + b.getLong(key));
			} else if (b.get(key) instanceof Integer) {
				Log.d(tag, key + ":" + b.getInt(key));
			} else {
				Log.d(tag, key);
			}
		}
	}

	// 打log的函数（调用这个函数的函数），是在函数栈的第5层
	public static int d(String subTag, String msg) {
		return Log.d(getTag(subTag, STACK_TRACE_DEEP), msg);
	}

	private static final int STACK_TRACE_DEEP = 4;
}
