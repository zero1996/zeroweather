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
	 * �����ߵ����綨λ
	 */
	private void startLocation() {
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// ���ö�λģʽΪ�͹���ģʽ
		locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		// ���ö�λ����
		locationClient.setLocationListener(this);
				
		//����Ϊ���ζ�λ
		locationOption.setOnceLocation(true);
		
		// �����Ƿ���Ҫ��ʾ��ַ��Ϣ
		locationOption.setNeedAddress(false);
		
		// ���ö�λ����
		locationClient.setLocationOption(locationOption);
		// ������λ
		locationClient.startLocation();
		mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != locationClient) {
			/**
			 * ���AMapLocationClient���ڵ�ǰActivityʵ�����ģ�
			 * ��Activity��onDestroy��һ��Ҫִ��AMapLocationClient��onDestroy
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
				tv.setText("���ڶ�λ...");
				break;
			//��λ���
			case Utils.MSG_LOCATION_FINISH:
				AMapLocation loc = (AMapLocation)msg.obj;
				String result = Utils.getLocationStr(loc);
				tv.setText(result);
				break;
			case Utils.MSG_LOCATION_STOP:
				tv.setText("��λֹͣ");
				break;
			default:
				break;
			}
		};
	};

	// ��λ����
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
