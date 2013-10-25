package us.stupidx.tools;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
/**
 * APN工具�?
 * <p>
 * 
 * </p>
 */
public class APNUtil {
	/**
	 * cmwap
	 */
	public static final int MPROXYTYPE_CMWAP = 1;
	/**
	 * wifi
	 */
	public static final int MPROXYTYPE_WIFI = 2;
	/**
	 * cmnet
	 */
	public static final int MPROXYTYPE_CMNET = 4;
	/**
	 * uninet服务器列�?
	 */
	public static final int MPROXYTYPE_UNINET = 8;
	/**
	 * uniwap服务器列�?
	 */
	public static final int MPROXYTYPE_UNIWAP = 16;
	/**
	 * net类服务器列表
	 */
	public static final int MPROXYTYPE_NET = 32;
	/**
	 * wap类服务器列表
	 */
	public static final int MPROXYTYPE_WAP = 64;
	/**
	 * 默认服务器列�?
	 */
	public static final int MPROXYTYPE_DEFAULT = 128;
	/**
	 * cmda net
	 */
	public static final int MPROXYTYPE_CTNET = 256;
	/**
	 * cmda wap
	 */
	public static final int MPROXYTYPE_CTWAP = 512;
	/**
	 * 联�? 3gwap
	 */
	public static final int MPROXYTYPE_3GWAP = 1024;
	/**
	 * 联�? 3gnet
	 */
	public static final int MPROXYTYPE_3GNET = 2048;

	public static final String ANP_NAME_WIFI = "wifi"; // 中国移动wap APN名称
	public static final String ANP_NAME_CMWAP = "cmwap"; // 中国移动wap APN名称
	public static final String ANP_NAME_CMNET = "cmnet"; // 中国移动net APN名称
	public static final String ANP_NAME_UNIWAP = "uniwap"; // 中国联�?wap APN名称
	public static final String ANP_NAME_UNINET = "uninet"; // 中国联�?net APN名称
	public static final String ANP_NAME_WAP = "wap"; // 中国电信wap APN名称
	public static final String ANP_NAME_NET = "net"; // 中国电信net APN名称
	public static final String ANP_NAME_CTWAP = "ctwap"; // wap APN名称
	public static final String ANP_NAME_CTNET = "ctnet"; // net APN名称
	public static final String ANP_NAME_NONE = "none"; // net APN名称

	// apn地址
	private static Uri PREFERRED_APN_URI = Uri
	.parse("content://telephony/carriers/preferapn");

	// apn属�?类型
	public static final String APN_PROP_APN = "apn";
	// apn属�?代理
	public static final String APN_PROP_PROXY = "proxy";
	// apn属�?端口
	public static final String APN_PROP_PORT = "port";

	public static final byte APNTYPE_NONE   = 0 ;//未知类型 
	public static final byte APNTYPE_CMNET  = 1 ;//cmnet  
	public static final byte APNTYPE_CMWAP  = 2 ;//cmwap  
	public static final byte APNTYPE_WIFI   = 3 ;//WiFi  
	public static final byte APNTYPE_UNINET = 4 ;//uninet 
	public static final byte APNTYPE_UNIWAP = 5 ;//uniwap 
	public static final byte APNTYPE_NET    = 6 ;//net类接入点 
	public static final byte APNTYPE_WAP    = 7 ;//wap类接入点 
	public static final byte APNTYPE_CTNET = 8; //ctnet
	public static final byte APNTYPE_CTWAP = 9; //ctwap
	public static final byte APNTYPE_3GWAP = 10; //3gwap
	public static final byte APNTYPE_3GNET = 11; //3gnet
	
	//jce接入点类�?
	public static final int JCE_APNTYPE_UNKNOWN = 0;
	public static final int JCE_APNTYPE_DEFAULT = 1;
	public static final int JCE_APNTYPE_CMNET = 2;
	public static final int JCE_APNTYPE_CMWAP = 4;
	public static final int JCE_APNTYPE_WIFI = 8;
	public static final int JCE_APNTYPE_UNINET = 16;
	public static final int JCE_APNTYPE_UNIWAP = 32;
	public static final int JCE_APNTYPE_NET = 64;
	public static final int JCE_APNTYPE_WAP = 128;
	public static final int JCE_APNTYPE_CTNET = 256;
	public static final int JCE_APNTYPE_CTWAP = 512;
	
	
	
	/**
	 * 获取自定义apn类型
	 * @param context
	 * @return
	 */
	public static byte getApnType(Context context){
		int netType = getMProxyType(context);

		if(netType == MPROXYTYPE_WIFI){
			return APNTYPE_WIFI;
		}else if (netType == MPROXYTYPE_CMWAP) {
			return APNTYPE_CMWAP;
		} else if (netType == MPROXYTYPE_CMNET) {
			return APNTYPE_CMNET;
		} else if (netType == MPROXYTYPE_UNIWAP) {
			return APNTYPE_UNIWAP;
		} else if (netType == MPROXYTYPE_UNINET) {
			return APNTYPE_UNINET;
		}else if (netType == MPROXYTYPE_WAP) {
			return APNTYPE_WAP;
		} else if (netType == MPROXYTYPE_NET) {
			return APNTYPE_NET;
		} else if (netType == MPROXYTYPE_CTWAP) {
			return APNTYPE_CTWAP;
		} else if (netType == MPROXYTYPE_CTNET) {
			return APNTYPE_CTNET;
		} else if (netType == MPROXYTYPE_3GWAP) {
			return APNTYPE_3GWAP;
		} else if (netType == MPROXYTYPE_3GNET) {
			return APNTYPE_3GNET;
		}
		return APNTYPE_NONE;
	}

	/**
	 * 获取系统APN代理IP
	 * 
	 * @param context
	 * @return
	 */
	public static String getApnProxyIp(Context context) {
		byte apnType = APNUtil.getApnType(context);
		if (apnType==APNUtil.APNTYPE_CMWAP||apnType==APNUtil.APNTYPE_UNIWAP||apnType==APNUtil.APNTYPE_3GWAP)
			return "10.0.0.172";
		
		if (apnType==APNUtil.APNTYPE_CTWAP) 
			return "10.0.0.200";
		
		return getApnProxy(context);
	}

	/**
	 * 获取系统APN代理IP
	 * @param context
	 * @return
	 */
	public static String getApnProxy(Context context) {
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null,
				null, null, null);
		c.moveToFirst();
		if (c.isAfterLast()) {
			c.close();
			return null;
		}
		String strResult = c.getString(c.getColumnIndex(APN_PROP_PROXY));
		c.close();
		return strResult;
	}
	
	/**
	 * 获取系统APN代理端口
	 * 
	 * @param context
	 * @return
	 */
	public static String getApnPort(Context context) {
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null,
				null, null, null);
		c.moveToFirst();
		if (c.isAfterLast()) {
			c.close();
			return "80";
		}
		
		String port = null;
		port = c.getString(c.getColumnIndex(APN_PROP_PORT));
		if(port == null){
			c.close();
			port = "80";
		}
		c.close();
		return port;
	}

	/**
	 * 获取系统APN代理端口
	 * 
	 * @param context
	 * @return
	 */
	public static int getApnPortInt(Context context) {
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null,
				null, null, null);
		c.moveToFirst();
		if (c.isAfterLast()) {
			c.close();
			return -1;
		}
		int result = c.getInt(c.getColumnIndex(APN_PROP_PORT));
		return result;
	}
	
	/**
	 * 是否有网关代�?
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasProxy(Context context) {
		int netType = getMProxyType(context);
		//#if ${polish.debug}
		Logger.d("netType:" + netType);
		//#endif
		if (netType == MPROXYTYPE_CMWAP || netType == MPROXYTYPE_UNIWAP
				|| netType == MPROXYTYPE_WAP || netType == MPROXYTYPE_CTWAP || netType == MPROXYTYPE_3GWAP) {
			return true;
		}
		return false;
	}

	/**
	 * 获取自定义当前联网类�?
	 * 
	 * @param act
	 *            当前活动Activity
	 * @return 联网类型 -1表示未知的联网类�? 正确类型�?MPROXYTYPE_WIFI | MPROXYTYPE_CMWAP |
	 *         MPROXYTYPE_CMNET
	 */
	public static int getMProxyType(Context act) {
		try {
			ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(cm == null)
				return MPROXYTYPE_DEFAULT;
			
			NetworkInfo info = cm.getActiveNetworkInfo();
			if(info == null)
				return MPROXYTYPE_DEFAULT;
			String typeName = info.getTypeName();
			//#if ${polish.debug}
			Logger.d("typeName:" + typeName);
			//#endif
			if (typeName.toUpperCase().equals("WIFI")) { // wifi网络
				return MPROXYTYPE_WIFI;
			} else {
				String extraInfo = info.getExtraInfo().toLowerCase();
				//#if ${polish.debug}
				Logger.d("extraInfo:" + extraInfo);
				//#endif
				if (extraInfo.startsWith("cmwap")) { // cmwap
					return MPROXYTYPE_CMWAP;
				} else if (extraInfo.startsWith("cmnet")
						|| extraInfo.startsWith("epc.tmobile.com")) { // cmnet
					return MPROXYTYPE_CMNET;
				} else if (extraInfo.startsWith("uniwap")) {
					return MPROXYTYPE_UNIWAP;
				} else if (extraInfo.startsWith("uninet")) {
					return MPROXYTYPE_UNINET;
				} else if (extraInfo.startsWith("wap")) {
					return MPROXYTYPE_WAP;
				} else if (extraInfo.startsWith("net")) {
					return MPROXYTYPE_NET;
				} else if(extraInfo.startsWith("ctwap")){
					return MPROXYTYPE_CTWAP;
				}else if(extraInfo.startsWith("ctnet")){
					return MPROXYTYPE_CTNET;
				} else if(extraInfo.startsWith("3gwap")) {
					return MPROXYTYPE_3GWAP;
				} else if(extraInfo.startsWith("3gnet")) {
					return MPROXYTYPE_3GNET;
				}
				else if (extraInfo.startsWith("#777")) { // cdma
					String proxy = getApnProxy(act);
					if (proxy != null && proxy.length() > 0) {
						return MPROXYTYPE_CTWAP;
					} else {
						return MPROXYTYPE_CTNET;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MPROXYTYPE_DEFAULT;
	}

	/**
	 * �?��是否有网�?
	 * @param c
	 * @return
	 */
	public static boolean isNetworkAvailable(Context act) {
		ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm == null)
			return false;
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null && info.isAvailable())
			return true;
		return false;
	}
	
	/**
	 * 活动网络是否有效
	 * @param ctx
	 * @return
	 */
	public static boolean isActiveNetworkAvailable(Context ctx){
		ConnectivityManager cm = (ConnectivityManager) ctx
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null)
			return info.isAvailable();
		return false;
	}
	
}
