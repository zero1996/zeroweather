package com.zeroweather.app.application;

import com.baidu.apistore.sdk.ApiStoreSDK;

import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		ApiStoreSDK.init(this, "957304c382e4d8445023deaf0122f9dc");
		super.onCreate();
	}

}
