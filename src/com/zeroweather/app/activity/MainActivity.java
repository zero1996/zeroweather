package com.zeroweather.app.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.zeroweather.app.R;
import com.zeroweather.app.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AMapLocationListener{
	private TextView tv;
	private Button btLocation;

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.text);
		
		startLocation();
		
		
	}
	
	/**
	 * 启动高德网络定位
	 */
	private void startLocation() {
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		// 设置定位监听
		locationClient.setLocationListener(this);
				
		//设置为单次定位
		locationOption.setOnceLocation(true);
		
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(false);
		
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
		mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
	}

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

	Handler mHandler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case Utils.MSG_LOCATION_START:
				tv.setText("正在定位...");
				break;
			//定位完成
			case Utils.MSG_LOCATION_FINISH:
				AMapLocation loc = (AMapLocation)msg.obj;
				String result = Utils.getLocationStr(loc);
				tv.setText(result);
				break;
			case Utils.MSG_LOCATION_STOP:
				tv.setText("定位停止");
				break;
			default:
				break;
			}
		};
	};

	// 定位监听
	@Override
	public void onLocationChanged(AMapLocation loc) {
		if (null != loc) {
			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = Utils.MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		}
	}

}
