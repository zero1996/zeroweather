package com.zeroweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class NetUtil {

	public interface HttpCallbackListener {
		void onFinish(String response);

		void onError(Exception e);
	}

	public static void sendRequest(final String url,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			public void run() {
				InputStream in = null;
				BufferedReader reader = null;
				HttpURLConnection connection = null;
				try {
					URL mUrl = new URL(url);
					connection = (HttpURLConnection) mUrl.openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestMethod("GET");
					connection.setReadTimeout(20 * 1000);
					connection.setConnectTimeout(20 * 1000);
					in = connection.getInputStream();
					reader = new BufferedReader(new InputStreamReader(in));
					String line = "";
					StringBuilder response = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

}
