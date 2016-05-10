package com.zeroweather.app.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.amap.api.location.AMapLocation;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.zeroweather.app.R;
import com.zeroweather.app.activity.MainActivity;
import com.zeroweather.app.model.CityDetail;
import com.zeroweather.app.util.Utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class App extends Application {
	private SharedPreferences mPref;
	private boolean isFirstEnter;//是否第一次进入程序
	
	@Override
	public void onCreate() {
		ApiStoreSDK.init(this, "957304c382e4d8445023deaf0122f9dc");
		super.onCreate();
		mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		isFirstEnter = true;
	}

	public boolean isFirstEnter() {
		isFirstEnter = mPref.getBoolean("isFirstEnter", true);
		return isFirstEnter;
	}
	public void setFirstEnter(boolean isFirstEnter) {
		this.isFirstEnter = isFirstEnter;
		Editor editor = mPref.edit();
		editor.putBoolean("isFirstEnter", false);
		editor.commit();
	}

}
