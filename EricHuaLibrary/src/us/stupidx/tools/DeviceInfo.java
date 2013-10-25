package us.stupidx.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public final class DeviceInfo {
    public static String getPlatform() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDeviceName());
        sb.append(";Android ");
        sb.append(getVersion());
        sb.append(",level ");
        sb.append(getApiLevel());
        return sb.toString();
    }

    public static String getDeviceName() {
        try {
            return Build.MODEL;
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return "null";
    }

    public static String getVersion() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return "null";
    }

    public static String getApiLevel() {
        try {
            return Build.VERSION.SDK;
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return "null";
    }

    public static int getApiLevelInt() {
        return Integer.parseInt(getApiLevel());
    }

    public static String getImei(Context context) {
        if (context == null) {
            return null;
        }
        String imei = null;
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = manager.getDeviceId();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return imei;
    }

    // 获取IMSI--R
    public static String getImsi(Context context) {
        if (context == null) {
            return null;
        }
        String imsi = null;
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = manager.getSubscriberId();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return imsi;
    }

    // 获取android ID
    public static String getAndroidId(Context context) {
        if (context == null) {
            return null;
        }
        String tmpAndroidId = null;
        try {
            tmpAndroidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return tmpAndroidId;
    }

    // 获取电话号码 ---r
    public static String getMobileNo(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmpMobileNo = null;
        try {

            tmpMobileNo = manager.getLine1Number();

        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return tmpMobileNo;
    }

    public static String getMacAddress(Context context) {
        if (context == null) {
            return null;
        }

        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return null;

    }

    public static String getWifiMacAddress(Context context) {
        if (context == null) {
            return null;
        }

        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getBSSID();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return null;

    }

    public static boolean checkIsHaveCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        if (context == null) {
            return null;
        }

        try {
            DisplayMetrics metric = new DisplayMetrics();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(metric);
            return metric;
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return null;

    }

    // 获取CPU名字
    public static String getCpuProductorName() {
        if (Integer.parseInt(Build.VERSION.SDK) < 8) {
            return null;
        }

        try {
            return Build.HARDWARE;
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return null;
    }

    public static String getRomSize() {
        String tmpRomSize = null;
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            tmpRomSize = totalBlocks * blockSize / 1024 / 1024 + "";
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return tmpRomSize;
    }

    public static long getFreeMem(Context context) {
        if (context == null) {
            return -1;
        }
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memInfo);
            return (memInfo.availMem); // bytes
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return -1;
    }

    public static String getRamSize() {
        String str1 = "/proc/meminfo";
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            fr = new FileReader(str1);
            localBufferedReader = new BufferedReader(fr, 8192);
            String text = localBufferedReader.readLine();
            String[] array = text.split(":\\s+", 2);
            String tempRamSize = array[1].toLowerCase();
            String newRamSize = tempRamSize.replace("kb", "").trim();
            long i = Long.parseLong(newRamSize) / 1024;
            return i + "";
        } catch (Throwable thr) {
            thr.printStackTrace();
        } finally {

            try {
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }
        return null;
    }

    public static long getFreeStorage() {
        try {
            StatFs fs = new StatFs("/data");
            int blockSize = fs.getBlockSize();
            long availableBlocks = fs.getAvailableBlocks();
            return availableBlocks * blockSize; // bytes
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return -1;
    }

    public static long getFreeCardSpace() {
        try {
            StatFs fs = new StatFs("/sdcard");
            int blockSize = fs.getBlockSize();
            long availableBlocks = fs.getAvailableBlocks();
            return availableBlocks * blockSize; // bytes
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据应用包名，获取指定应用占用的CPU和内存信息
     * 
     * @param pkgName
     * @return 12%|45460K
     */
    public static String getAppCpuMemInfo(String pkgName) {
        final String[] top = { "/system/bin/top", "-n", "1" };
        String result = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process pro = rt.exec(top);
            is = pro.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String tmp;
            while (null != (tmp = br.readLine())) {
                if (tmp.endsWith(pkgName)) {
                    String[] tmpArr = tmp.split(" ");
                    result = tmpArr[7] + "|" + tmpArr[15];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (is != null)
                    is.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String getNetworkName(Context context) {
        // 此处绝对不能返回null
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info == null)
            return "";
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "wifi";
        return "" + info.getExtraInfo();
    }

    public static String getNetWorkType(Context context) {
        String tmpNetworkType = "unknown";
        try {
            ConnectivityManager connectionManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo == null)
                return tmpNetworkType;
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                tmpNetworkType = "wifi";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null) {
                    switch (tm.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        tmpNetworkType = "GPRS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        tmpNetworkType = "EDGE";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        tmpNetworkType = "UMTS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        tmpNetworkType = "HSDPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        tmpNetworkType = "HSUPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        tmpNetworkType = "HSPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        tmpNetworkType = "CDMA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        tmpNetworkType = "CDMA - EvDo rev. 0";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        tmpNetworkType = "CDMA - EvDo rev. A";
                        break;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        tmpNetworkType = "CDMA - 1xRTT";
                        break;
                    default:
                        tmpNetworkType = "unknown";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpNetworkType;
    }

    public static String getCountry() {
        String tmpContry = null;
        try {
            tmpContry = Locale.getDefault().getCountry();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return tmpContry;
    }

    public static String getLanguage() {
        String tmpLanguage = null;
        try {
            tmpLanguage = Locale.getDefault().getLanguage();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return tmpLanguage;
    }

    public static String getSensor(Context context) {
        if (context == null) {
            return null;
        }

        String backCamera = "X";
        String frontCamera = "X";
        String gravitySensor = "X";
        String gyroscopeSensor = "X";

        StringBuffer sensorName = new StringBuffer();

        // 获取摄像头信息
        if (Integer.parseInt(Build.VERSION.SDK) < 10) {// 如果2.3版本以下
                                                       // 待处理
        } else {
            try {
                Class<?> classType = Class.forName("android.hardware.Camera");
                Method getCameras = classType.getMethod("getNumberOfCameras");
                int camersNumber = (Integer) getCameras.invoke(classType);
                if (camersNumber == 0) {
                    backCamera = "N";
                    frontCamera = "N";
                } else {
                    Class<?> classTypeInfo = Class.forName("android.hardware.Camera$CameraInfo");
                    Object cameraInfo = classTypeInfo.newInstance();
                    Method[] allMethods = classType.getMethods();
                    Method getCameraInfo = null;
                    for (Method temp : allMethods) {
                        if (temp.getName().equals("getCameraInfo")) {
                            getCameraInfo = temp;
                            break;
                        }
                    }
                    java.lang.reflect.Field facing = classTypeInfo.getField("facing");
                    java.lang.reflect.Field back = classTypeInfo.getField("CAMERA_FACING_BACK");
                    java.lang.reflect.Field front = classTypeInfo.getField("CAMERA_FACING_FRONT");

                    if (getCameraInfo != null) {
                        for (int i = 0; i < camersNumber; i++) {
                            getCameraInfo.invoke(classType, i, cameraInfo);
                            int fa = facing.getInt(cameraInfo);
                            int ba = back.getInt(cameraInfo);
                            int fr = front.getInt(cameraInfo);
                            if (fa == ba) {// back
                                backCamera = "Y";
                                if (camersNumber == 1)
                                    frontCamera = "N";
                            } else if (fa == fr) {// front
                                frontCamera = "Y";
                                if (camersNumber == 1)
                                    backCamera = "N";
                            }
                        }
                    }

                }

                SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                if (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {// 重力
                    gravitySensor = "Y";
                } else {
                    gravitySensor = "N";
                }
                if (sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {// 陀螺仪
                    gyroscopeSensor = "Y";
                } else {
                    gyroscopeSensor = "N";
                }

                sensorName.append(backCamera).append(frontCamera).append(gravitySensor).append(gyroscopeSensor);
                return sensorName.toString();
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }

        return null;
    }

    // 获取品牌
    public static String getBrand() {
        String tmpBrand = null;
        try {
            tmpBrand = Build.BRAND;
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return tmpBrand;
    }
}
