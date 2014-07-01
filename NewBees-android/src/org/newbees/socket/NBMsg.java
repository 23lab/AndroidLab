package org.newbees.socket;

import org.json.JSONException;
import org.json.JSONObject;

public class NBMsg {
	private String mCmd = "";
	private String mTarget = "";
	private String mSource = "";
	private JSONObject mParams = null;

	// protocol keys
	private final static String CMD_KEY = "cmd";
	private final static String TARGET_KEY = "target";
	private final static String SOURCE_KEY = "source";
	private final static String SOURCE_PARAMS = "params";
	
	// const value of some special situation
	public final static String MSG_CMD_CONN = "connection";

	public NBMsg(String jsonText) {
		try {
			JSONObject json = new JSONObject(jsonText);

			// keys here must match that used at server
			if (json.has(CMD_KEY)) {
				this.mCmd = json.getString(CMD_KEY);
			}
			
			if (json.has(TARGET_KEY)) {
				this.mTarget = json.getString(TARGET_KEY);
			}
			
			if (json.has(SOURCE_KEY)) {
				this.mSource = json.getString(SOURCE_KEY);
			}
			
			if (json.has(SOURCE_PARAMS)) {
				this.mParams = json.getJSONObject(SOURCE_KEY);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public NBMsg(String mCmd, String mTarget, String mSource, JSONObject mParams) {
		super();
		this.mCmd = mCmd;
		this.mTarget = mTarget;
		this.mSource = mSource;
		this.mParams = mParams;
	}
	
	public boolean isConnectionMsg() {
		return NBMsg.MSG_CMD_CONN.equals(this.getCmd());
	}

	public String getCmd() {
		return mCmd;
	}

	public String getTarget() {
		return mTarget;
	}

	public void setTarget(String mTarget) {
		this.mTarget = mTarget;
	}

	public String getSource() {
		return mSource;
	}

	public void setSource(String mSource) {
		this.mSource = mSource;
	}

	public JSONObject getParams() {
		return mParams;
	}

	public void setParams(JSONObject mParams) {
		this.mParams = mParams;
	}
	public String getParam(String key) {
		if (this.mParams != null && this.mParams.has(key)) {
			String param;
			try {
				param = this.mParams.getString(key);
				return param;
			} catch (JSONException e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}
}
