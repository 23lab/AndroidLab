package us.stupidx.tools;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class JsonHelper {
	public static Bundle json2bundle(JSONObject json) throws JSONException {
		Iterator<String> keys = json.keys();
		Bundle b = new Bundle();
		String key = "";
		while (keys.hasNext()) {
			key = keys.next();
			b.putString(key, json.getString(key));
		}
		return b;
	}
}
