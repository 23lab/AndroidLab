package us.stupidx.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.widget.Toast;

public class Tools {
    /**
     * 微信的包名
     * **/
    public static String WXPAKAGENAME = "com.tencent.mm";

    /**
     * 得到调用者的R里的资源
     * **/
    public static int reflectResouce(String RclassName, String para, String filed) {
        if (RclassName == null || para == null || filed == null)
            return 0;
        Object oj = null;
        try {
            Class<?> c = Class.forName(RclassName + "$" + para);
            Field f = c.getField(filed);
            oj = f.get(c.newInstance());
            return Integer.parseInt(oj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    /**
     * 判断当前硬件是否安装某个软件
     * 
     * @param para 包名
     * @return 0 没有安装 1安装
     * **/
    public static boolean isInstalledApp(Context context, String para) {
        if (para == null) {
            return false;
        }
        ArrayList<String> userApparrayList = null;
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (userApparrayList == null) {
            userApparrayList = new ArrayList<String>();
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    userApparrayList.add(packageInfo.applicationInfo.packageName);
                }
            }
        }
        if (userApparrayList.contains(para)) {
            return true;
        }
        return false;
    }

    public static void toast(Activity act, String txt) {
        Toast.makeText(act, txt, Toast.LENGTH_SHORT).show();
    }
}
