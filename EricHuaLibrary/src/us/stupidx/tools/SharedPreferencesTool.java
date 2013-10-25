package us.stupidx.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesTool {
    private static String msdkShareKey = "msdk";
    public static String getString(Context ctx, String key, String deftValue) {
        SharedPreferences myPrefs = ctx.getSharedPreferences(msdkShareKey, Context.MODE_PRIVATE);
        return myPrefs.getString(key, deftValue);
    }
    
    public static void putString(Context ctx, String key, String value) {
        SharedPreferences myPrefs = ctx.getSharedPreferences(msdkShareKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();  
        prefsEditor.putString(key, value);  
        prefsEditor.commit();
    }
    
    public static boolean getBoolean(Context ctx, String key, boolean deftValue) {
        SharedPreferences myPrefs = ctx.getSharedPreferences(msdkShareKey, Context.MODE_PRIVATE);
        return myPrefs.getBoolean(key, deftValue);
    }
    
    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences myPrefs = ctx.getSharedPreferences(msdkShareKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();  
        prefsEditor.putBoolean(key, value);  
        prefsEditor.commit();
    }
    public static int getInt(Context ctx, String key, int deftValue) {
        SharedPreferences myPrefs = ctx.getSharedPreferences(msdkShareKey, Context.MODE_PRIVATE);
        return myPrefs.getInt(key, deftValue);
    }
    
    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences myPrefs = ctx.getSharedPreferences(msdkShareKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();  
        prefsEditor.putInt(key, value);  
        prefsEditor.commit();
    }
}
