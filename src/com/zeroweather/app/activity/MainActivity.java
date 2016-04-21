package com.zeroweather.app.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

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
import com.zeroweather.app.model.Weather;
import com.zeroweather.app.util.NetUtil;
import com.zeroweather.app.util.NetUtil.HttpCallbackListener;
import com.zeroweather.app.util.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AMapLocationListener {
	private ZeroWeatherDB zeroWeatherDB;
	private App mApp;
	private TextView locatiionCityTV;// 显示城市
	private TextView dateTV;// 显示日期
	private TextView nowTmpTV;// 显示当前温度
	private TextView nowCondTxtTV;// 显示当前天气状况描述
	private TextView todayTmpScope;// 显示今天温度范围
	private LinearLayout nowWeatherLayout;
	private LinearLayout topWeatherLayout;
	private LinearLayout weatherLayout;
	private ImageView locationIV;
	private ImageView moreIV;

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	public static final String GET_ALL_CHINA_CITY = "https://api.heweather.com/x3/citylist?search=allchina&key=096a3e8e36984b81a10fb81c3819ccae";// 获取可获得天气中国城市的接口

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		
		Message msg = new Message();
		msg.what = Utils.MSG_LOAD_CITY_DETAIL;
		mHandler.sendMessage(msg);

	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		mApp = (App) getApplicationContext();
		locatiionCityTV = (TextView) findViewById(R.id.location_city);
		dateTV = (TextView) findViewById(R.id.date);
		nowTmpTV = (TextView) findViewById(R.id.now_tmp);
		nowCondTxtTV = (TextView) findViewById(R.id.now_cond_txt);
		todayTmpScope = (TextView) findViewById(R.id.today_tmp_scope);
		nowWeatherLayout = (LinearLayout) findViewById(R.id.now_weather);
		topWeatherLayout = (LinearLayout) findViewById(R.id.top_weather);
		weatherLayout = (LinearLayout) findViewById(R.id.weather);
		locationIV = (ImageView) findViewById(R.id.location);
		moreIV = (ImageView) findViewById(R.id.more);

		// 获取控件高度
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		nowWeatherLayout.measure(w, h);
		topWeatherLayout.measure(w, h);
		weatherLayout.measure(w, h);
		int nowWeatherHeight = nowWeatherLayout.getMeasuredHeight();
		int topWeatherHeight = topWeatherLayout.getMeasuredHeight();
		int padding = weatherLayout.getPaddingTop();
		// 设置margin
		LinearLayout.LayoutParams params = (LayoutParams) nowWeatherLayout
				.getLayoutParams();
		params.topMargin = getScreenHeight() - nowWeatherHeight
				- topWeatherHeight - padding * 4;

		zeroWeatherDB = ZeroWeatherDB.getInstance(this);
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return 屏幕高度
	 */
	private int getScreenHeight() {
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获取可获得天气的城市列表
	 */
	private void getCityList() {
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
		} else {
			Message msg = new Message();
			msg.what = Utils.MSG_LOAD_CITY_DETAIL;
			mHandler.sendMessage(msg);

		}
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
				// locatiionCityTV.setText("正在定位...");
				break;
				// 定位完成
			case Utils.MSG_LOCATION_FINISH:
				AMapLocation loc = (AMapLocation) msg.obj;
				Map<String, String> result = Utils.getLocationStr(loc);
				if (!result.toString().equals("{}")) {
					String district = Utils.parseDistrictName(result
							.get("district"));
					String city = Utils.parseCityName(result.get("city"));
					String province = Utils.parseProvinceName(result
							.get("province"));
					// String code = zeroWeatherDB.queryCityCode(district,
					// province);
					// if ("".equals(code)) {
					// code = zeroWeatherDB.queryCityCode(city, province);
					// }
					// 先通过区域名查询城市代码，查询不到则通过城市名查询。
					CityDetail cityDetail = null;
					InputStream is;
					try {
						is = MainActivity.this.getAssets().open("citylist.xml");
						cityDetail = parse(is, district, province);
						if (cityDetail == null) {
							is = MainActivity.this.getAssets().open("citylist.xml");
							cityDetail = parse(is, city, province);
						}
						// 获取天气
						if (cityDetail != null) {
							getWeather(cityDetail.getId());
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					locationIV.setImageResource(R.drawable.location_on);
				} else {
					locationIV.setImageResource(R.drawable.location_off);
					Toast.makeText(MainActivity.this, "定位失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Utils.MSG_LOCATION_STOP:
				// tv.setText("定位停止");
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
	 * 根据城市代码获取天气
	 * 
	 * @param cityCode
	 *            城市代码
	 */
	private void getWeather(String cityCode) {
		Parameters para = new Parameters();
		para.put("cityid", cityCode);
		ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
				ApiStoreSDK.GET, para, new ApiCallBack() {
			@Override
			public void onSuccess(int status, String responseString) {
				Log.i("sdkdemo", "onSuccess");
				Weather weather = parseWeatherJson(responseString);
				if (weather != null) {
					locatiionCityTV.setText(weather.getCity());
					dateTV.setText(weather.getDate());
					nowTmpTV.setText(weather.getNowTemp() + "°");
					nowCondTxtTV.setText(weather.getNowCondTxt());
					todayTmpScope.setText(weather.getTodayTempMax()
							+ "°/" + weather.getTodayTempMin() + "℃");

				}
			}

			@Override
			public void onComplete() {
				Log.i("sdkdemo", "onComplete");
			}

			@Override
			public void onError(int status, String responseString,
					Exception e) {
				Log.i("sdkdemo", "onError, status: " + status);
				Log.i("sdkdemo",
						"errMsg: " + (e == null ? "" : e.getMessage()));
			}

		});

	}

	/**
	 * 解析城市列表json,并保存至数据库
	 */
	public void parseCitylistJson(String response) {
		List<CityDetail> list = new ArrayList<CityDetail>();
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
					list.add(cityDetail);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 保存至xml
		writeToXml(WriteToString(list));
	}

	/**
	 * 解析天氣Json，保存具体信息到Weather实体类并返回
	 * 
	 * @param weatherJson
	 * @return
	 */
	public Weather parseWeatherJson(String weatherJson) {
		Weather weather = new Weather();
		try {
			JSONObject jsonObj = new JSONObject(weatherJson);
			if (jsonObj.has("HeWeather data service 3.0")) {
				JSONArray heWeather = jsonObj
						.getJSONArray("HeWeather data service 3.0");
				JSONObject info = heWeather.getJSONObject(0);
				if (info.has("status")) {
					if (info.getString("status").equals("ok")) {
						JSONObject basic = info.getJSONObject("basic");
						// 城市
						weather.setCity(basic.getString("city"));
						JSONArray daily = info.getJSONArray("daily_forecast");
						JSONObject today = daily.getJSONObject(0);
						JSONObject todayTmp = today.getJSONObject("tmp");
						// 日期
						weather.setDate(today.getString("date"));
						// 今日最高温
						weather.setTodayTempMax(todayTmp.getString("max"));
						// 今日最低温
						weather.setTodayTempMin(todayTmp.getString("min"));
						JSONObject now = info.getJSONObject("now");
						JSONObject nowCond = now.getJSONObject("cond");
						// 当前温度
						weather.setNowTemp(now.getString("tmp"));
						// 当前天气描述
						weather.setNowCondTxt(nowCond.getString("txt"));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weather;
	}

	public String WriteToString(List<CityDetail> cityDetails) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);

			for (int i = 0; i < cityDetails.size(); i++) {
				CityDetail cityDetail = cityDetails.get(i);

				serializer.startTag("", "cityDetail");

				serializer.startTag("", "country");
				serializer.text(cityDetail.getCountry());
				serializer.endTag("", "country");

				serializer.startTag("", "province");
				serializer.text(cityDetail.getProvince());
				serializer.endTag("", "province");

				serializer.startTag("", "city");
				serializer.text(cityDetail.getCity());
				serializer.endTag("", "city");

				serializer.startTag("", "id");
				serializer.text(cityDetail.getId());
				serializer.endTag("", "id");

				serializer.endTag("", "cityDetail");
			}
			serializer.endDocument();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	private boolean writeToXml(String str) {
		Log.i("---------------", str);
		try {
			OutputStream out = openFileOutput("citys.xml", MODE_PRIVATE);
			OutputStreamWriter outWriter = new OutputStreamWriter(out);
			outWriter.write(str);
			outWriter.close();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public CityDetail parse(InputStream is, String cityName, String provinceName)
			throws Exception {
		CityDetail cityDetail = null;
		XmlPullParser xpp = Xml.newPullParser();
		// 设置输入流 并指明编码方式
		xpp.setInput(is, "UTF-8");
		// 产生第一个事件
		int eventType = xpp.getEventType();

		boolean flag = false;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			// 判断当前事件是否为文档开始事件
			case XmlPullParser.START_DOCUMENT:

				break;
				// 判断当前事件是否为标签元素开始事件
			case XmlPullParser.START_TAG:
				if (xpp.getName().equals("cityDetail")) {

				} else if (xpp.getName().equals("city")) {
					eventType = xpp.next();// 让解析器指向city属性的值
					if (xpp.getText().trim().equals(cityName)) {
						cityDetail = new CityDetail();
						flag = true;
						cityDetail.setCity(xpp.getText());
					}
				} else if (xpp.getName().equals("country")) {
					eventType = xpp.next();// 让解析器指向country属性的值
					if (flag) {
						cityDetail.setCountry(xpp.getText().trim());
					}
				} else if (xpp.getName().equals("province")) {
					eventType = xpp.next();// 让解析器指向province属性的值
					if (xpp.getText().trim().equals(provinceName)) {
						if (flag) {
							cityDetail.setProvince(xpp.getText().trim());
						}
					}
				} else if (xpp.getName().equals("id")) {
					eventType = xpp.next();// 让解析器指向id属性的值
					if (flag) {
						cityDetail.setId(xpp.getText().trim());
						return cityDetail;
					}
				}
				break;

				// 判断当前事件是否为标签结束事件
			case XmlPullParser.END_TAG:
				if (xpp.getName().equals("cityDetail")) {
					cityDetail = null;
				}
				break;
			}
			// 进入下一个元素并触发相应事件
			eventType = xpp.next();
		}
		return cityDetail;
	}

}
