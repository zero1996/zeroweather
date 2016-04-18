package com.zeroweather.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.zeroweather.app.R;
import com.zeroweather.app.application.App;
import com.zeroweather.app.db.ZeroWeatherDB;
import com.zeroweather.app.model.CityDetail;
import com.zeroweather.app.util.NetUtil;
import com.zeroweather.app.util.NetUtil.HttpCallbackListener;
import com.zeroweather.app.util.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements AMapLocationListener {
	private ZeroWeatherDB zeroWeatherDB;
	private App mApp;
	private TextView tv;

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	public static final String GET_ALL_CHINA_CITY = "https://api.heweather.com/x3/citylist?search=allchina&key=096a3e8e36984b81a10fb81c3819ccae";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mApp = (App) getApplicationContext();
		tv = (TextView) findViewById(R.id.text_view);
		zeroWeatherDB = ZeroWeatherDB.getInstance(this);

		if (mApp.isFirstEnter()) {
			NetUtil.sendRequest(GET_ALL_CHINA_CITY, new HttpCallbackListener() {
				@Override
				public void onFinish(String response) {
					parseCitylistJson(response);
					Message msg = new Message();
					msg.what = Utils.MSG_LOAD_CITY_DETAIL;
					mHandler.sendMessage(msg);
					mApp.setFirstEnter(false);
				}

				@Override
				public void onError(Exception e) {
				}
			});
		}else{
			Message msg = new Message();
			msg.what = Utils.MSG_LOAD_CITY_DETAIL;
			mHandler.sendMessage(msg);
			
		}
		
		
		

		// Parameters para = new Parameters();
		// para.put("city", "beijing");
		// ApiStoreSDK.execute("https://api.heweather.com/x3/citylist?search=allchina",
		// ApiStoreSDK.GET,
		// null,`
		// new ApiCallBack() {
		// @Override
		// public void onSuccess(int status, String responseString) {
		// Log.i("sdkdemo", "onSuccess");
		// tv.setText(responseString);
		// }
		//
		// @Override
		// public void onComplete() {
		// Log.i("sdkdemo", "onComplete");
		// }
		//
		// @Override
		// public void onError(int status, String responseString, Exception e) {
		// Log.i("sdkdemo", "onError, status: " + status);
		// Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
		// }
		//
		// });

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

	Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case Utils.MSG_LOCATION_START:
				tv.setText("正在定位...");
				break;
			// 定位完成
			case Utils.MSG_LOCATION_FINISH:
				AMapLocation loc = (AMapLocation) msg.obj;
				Map<String, String> result = Utils.getLocationStr(loc);
				String city = result.get("city");
				String district = result.get("district");
				Log.i("city", city);
				Log.i("district", district);
				tv.setText(city);
				break;
			case Utils.MSG_LOCATION_STOP:
				tv.setText("定位停止");
				break;
			case Utils.MSG_LOAD_CITY_DETAIL:
				startLocation();
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

	/**
	 * 解析城市列表json,并保存至数据库
	 */
	public void parseCitylistJson(String response) {
		try {
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj.has("city_info")) {
				JSONArray jsonAray = jsonObj.getJSONArray("city_info");
				for (int i = 0; i < jsonAray.length(); i++) {
					JSONObject detail = jsonAray.getJSONObject(i);
					CityDetail cityDetail = new CityDetail();
					cityDetail.setCountry(detail.getString("cnty"));
					cityDetail.setProvince(detail.getString("prov"));
					cityDetail.setCity(detail.getString("city"));
					cityDetail.setId(detail.getString("id"));
					zeroWeatherDB.saveCityDetail(cityDetail);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
