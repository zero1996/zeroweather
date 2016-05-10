package com.zeroweather.app.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.zeroweather.app.util.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;

public abstract class BaseActivity extends Activity implements AMapLocationListener {
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	private Handler mHandler;
	private boolean isFirstEnter = true;//首次打开程序

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleMessage();
		if(isFirstEnter){
			startLocation();//开始定位
			isFirstEnter = false;

		}
	}
	/**
	 * 处理消息
	 */
	private void handleMessage() {
		mHandler = new Handler() {
			public void dispatchMessage(android.os.Message msg) {
				switch (msg.what) {
				case Utils.MSG_LOCATION_START:
					// 正在定位...
					updateViewOnLocationStart();
					break;
					// 定位完成
				case Utils.MSG_LOCATION_FINISH:
					updateViewOnLocationFinish(msg);
					break;
				case Utils.MSG_LOCATION_STOP:
					//定位停止
					break;
				default:
					break;
				}
			};
		};

	}

	/**
	 * 低功耗定位模式
	 */
	private void startLocation() {
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		// 设置定位监听
		locationClient.setLocationListener(this);
		// 设置为不是单次定位
		locationOption.setOnceLocation(true);
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
		mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
	}

	/**
	 * 定位监听
	 */
	@Override
	public void onLocationChanged(AMapLocation loc) {
		if (null != loc) {
			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = Utils.MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 正在定位时更新UI，子类实现
	 */
	public abstract void updateViewOnLocationStart();
	/**
	 * 定位完成时更新UI，子类实现
	 * @param msg 
	 */
	public abstract void updateViewOnLocationFinish(Message msg);
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
