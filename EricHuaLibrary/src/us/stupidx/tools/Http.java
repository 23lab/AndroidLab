package us.stupidx.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

public class Http {

    public static int get(String url, HashMap<String, String> params) {
        Logger.d("Http get");
        List<BasicNameValuePair> kvs = new LinkedList<BasicNameValuePair>();
        for (HashMap.Entry<String, String> e : params.entrySet()) {
            kvs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
        }

        try {
            String param = URLEncodedUtils.format(kvs, "UTF-8");
            HttpGet method = new HttpGet(url + "?" + param);
            method.setHeader("Connection", "close");
            method.setHeader("Cache-Control", "no-cache");
            // 去掉Expect: 100-Continue特性
            method.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(method);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                Logger.d("WHTTP Get: " + line);
            }
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int post(String url, HashMap<String, String> params) {
        Logger.d("Http post");

        List<BasicNameValuePair> kvs = new LinkedList<BasicNameValuePair>();
        for (HashMap.Entry<String, String> e : params.entrySet()) {
            kvs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
        }

        try {
            HttpPost method = new HttpPost(url);
            method.setEntity(new UrlEncodedFormEntity(kvs, "utf-8"));
            method.setHeader("Connection", "close");
            method.setHeader("Cache-Control", "no-cache");
            // 去掉Expect: 100-Continue特性
            method.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(method);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                Logger.d("HTTP Post: " + line);
            }
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
