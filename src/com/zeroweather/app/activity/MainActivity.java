package com.zeroweather.app.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import com.zeroweather.app.apt.DailyForecastAdapter;
import com.zeroweather.app.apt.GridAdapter;
import com.zeroweather.app.db.ZeroWeatherDB;
import com.zeroweather.app.model.CityDetail;
import com.zeroweather.app.model.DailyForecast;
import com.zeroweather.app.model.Grid;
import com.zeroweather.app.model.Weather;
import com.zeroweather.app.util.NetUtil;
import com.zeroweather.app.util.NetUtil.HttpCallbackListener;
import com.zeroweather.app.util.Utils;
import com.zeroweather.app.view.WeatherCircleView;
import com.zeroweather.app.view.DailyForecastView;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements AMapLocationListener {
	private ZeroWeatherDB zeroWeatherDB;
	private App mApp;
	private List<DailyForecast> dailyForecastList;// 每日天气集合
	private Weather weather;// 基础天气实体类
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

	private DailyForecastView dfView;//七日天气自定义视图
	private WeatherCircleView cwcView;//圆环天气自定义视图
	private GridView nowGV;
	private GridView suggestGV;

	private GridAdapter nowGVApt;
	private GridAdapter suggestGVApt;

	private TextView aqiQltyTV;// 空气质量
	private ProgressBar aqiPB;// 空气指数

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	public static final String GET_ALL_CHINA_CITY = "https://api.heweather.com/x3/citylist?search=allchina&key=096a3e8e36984b81a10fb81c3819ccae";// 获取可获得天气中国城市的接口

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
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

		dailyForecastList = new ArrayList<DailyForecast>();

		// 初始化一周天气View
		dfView = (DailyForecastView) findViewById(R.id.daily_forecast);
		dfView.setDimensions(getScreenWidth(), getScreenHeight() / 2);
		
		//初始化圆环天气View
		cwcView = (WeatherCircleView) findViewById(R.id.weather_circle);
		cwcView.setDimension(getScreenWidth(), getScreenWidth());

		// 初始化GridView
		nowGV = (GridView) findViewById(R.id.grid_view_now);
		suggestGV = (GridView) findViewById(R.id.grid_view_suggest);

		aqiQltyTV = (TextView) findViewById(R.id.aqi_qlty);
		aqiPB = (ProgressBar) findViewById(R.id.aqi_progress);

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
		// int padding = weatherLayout.getPaddingTop();

		LinearLayout.LayoutParams topParams = (LayoutParams) topWeatherLayout
				.getLayoutParams();
		int topSpace = topParams.topMargin + topParams.bottomMargin;
		;

		// 设置margin
		LinearLayout.LayoutParams params = (LayoutParams) nowWeatherLayout
				.getLayoutParams();
		params.topMargin = getScreenHeight() - nowWeatherHeight
				- topWeatherHeight - topSpace * 2;

		zeroWeatherDB = ZeroWeatherDB.getInstance(this);

		// 设置焦点，ScrollView起始位置在顶部
		nowWeatherLayout.setFocusable(true);
		nowWeatherLayout.setFocusableInTouchMode(true);
		nowWeatherLayout.requestFocus();
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return 屏幕高度
	 */
	private int getScreenHeight() {
		WindowManager wm = (WindowManager) MainActivity.this
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return 屏幕宽度
	 */
	private int getScreenWidth() {
		WindowManager wm = (WindowManager) MainActivity.this
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
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
				// 解析JSON
				parseWeatherJson(responseString);
				// 刷新界面基础天气信息
				refreshView();
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
	 * 刷新界面
	 */
	private void refreshView() {
		if (weather != null) {
			if (Integer.valueOf(weather.getNowCondCode()) > 213
					&& Integer.valueOf(weather.getNowCondCode()) < 407) {
				weatherLayout
				.setBackgroundResource(R.color.deep_blue);
			} else {
				weatherLayout
				.setBackgroundResource(R.color.blue);
			}

			locatiionCityTV.setText(weather.getCity());
			try {
				dateTV.setText(Utils.splitDate(weather.getDate())+"  "+Utils.dayForWeek(weather.getDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			nowTmpTV.setText(weather.getNowTemp() + "°");
			nowCondTxtTV.setText(weather.getNowCondTxt());
			todayTmpScope.setText(weather.getTodayTempMax()
					+ "°/" + weather.getTodayTempMin() + "℃");

			aqiQltyTV.setText(weather.getQlty());
			aqiPB.setSecondaryProgress(Integer.valueOf(weather
					.getAqi()) / 3);
			/*
			 * 刷新GridView
			 */
			List<Grid> list1 = new ArrayList<Grid>();
			List<Grid> list2 = new ArrayList<Grid>();
			for (int i = 0; i < 3; i++) {
				Grid grid1 = new Grid();
				Grid grid2 = new Grid();
				if (i == 0) {
					grid1.setImgId(R.drawable.wind);
					grid1.setTitle(weather.getWindDir());
					grid1.setContent(weather.getWindSc() + "级");

					grid2.setImgId(R.drawable.sport);
					grid2.setTitle("运动指数");
					grid2.setContent(weather.getSportBrf());
				}
				if (i == 1) {
					grid1.setImgId(R.drawable.uv);
					grid1.setTitle("紫外线");
					grid1.setContent(weather.getUv());

					grid2.setImgId(R.drawable.cw);
					grid2.setTitle("洗车指数");
					grid2.setContent(weather.getCwBrf());
				}
				if (i == 2) {
					grid1.setImgId(R.drawable.hum);
					grid1.setTitle("湿度");
					grid1.setContent(weather.getHum() + "%");

					grid2.setImgId(R.drawable.drsg);
					grid2.setTitle("穿衣建议");
					grid2.setContent(weather.getDrsgBrf());
				}
				list1.add(grid1);
				list2.add(grid2);
			}

			nowGVApt = new GridAdapter(MainActivity.this,
					R.layout.item_grid, list1);
			nowGV.setAdapter(nowGVApt);

			suggestGVApt = new GridAdapter(MainActivity.this,
					R.layout.item_grid, list2);
			suggestGV.setAdapter(suggestGVApt);
		}
		// 刷新一周天气数据
		if (dailyForecastList != null) {
			dfView.setData(dailyForecastList);
		}
	}

	/**
	 * 解析天氣Json，保存具体信息到实体类
	 * 
	 * @param weatherJson
	 * @return
	 */
	public void parseWeatherJson(String weatherJson) {
		weather = new Weather();// 基础天气
		try {
			JSONObject jsonObj = new JSONObject(weatherJson);
			if (jsonObj.has("HeWeather data service 3.0")) {
				JSONArray heWeather = jsonObj
						.getJSONArray("HeWeather data service 3.0");
				JSONObject info = heWeather.getJSONObject(0);
				if (info.has("status")) {
					if (info.getString("status").equals("ok")) {
						// ---基础天气---
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
						// 风
						JSONObject wind = now.getJSONObject("wind");
						weather.setWindDir(wind.getString("dir"));// 风向
						weather.setWindSc(wind.getString("sc"));// 风力
						// 紫外线
						JSONObject suggestion = info
								.getJSONObject("suggestion");// 生活建议
						JSONObject uv = suggestion.getJSONObject("uv");
						weather.setUv(uv.getString("brf"));
						// 湿度
						weather.setHum(now.getString("hum"));
						// 运动指数
						JSONObject sport = suggestion.getJSONObject("sport");
						weather.setSportBrf(sport.getString("brf"));
						weather.setSportTxt(sport.getString("txt"));
						// 洗车指数
						JSONObject cw = suggestion.getJSONObject("cw");
						weather.setCwBrf(cw.getString("brf"));
						weather.setCwTxt(cw.getString("txt"));
						// 穿衣建议
						JSONObject drsg = suggestion.getJSONObject("drsg");
						weather.setDrsgBrf(drsg.getString("brf"));
						weather.setDrsgTxt(drsg.getString("txt"));
						// 空气
						JSONObject aqi = info.getJSONObject("aqi")
								.getJSONObject("city");
						weather.setAqi(aqi.getString("aqi"));// 空气指数
						weather.setQlty(aqi.getString("qlty"));// 空气质量
						// 白天天气代码
						weather.setNowCondCode(nowCond.getString("code"));
						// 日出日落时间
						weather.setSr(today.getJSONObject("astro").getString(
								"sr"));
						weather.setSs(today.getJSONObject("astro").getString(
								"ss"));

						// ---每日天气---
						for (int i = 0; i < daily.length(); i++) {
							DailyForecast dailyForecast = new DailyForecast();// 每日天气
							JSONObject day = daily.getJSONObject(i);
							dailyForecast.setDate(Utils.splitDate(day
									.getString("date")));
							dailyForecast.setWeek(Utils.dayForWeek(day
									.getString("date")));
							dailyForecast.setCond(day.getJSONObject("cond")
									.getString("code_d"));
							dailyForecast.setTmpMax(day.getJSONObject("tmp")
									.getString("max"));
							dailyForecast.setTmpMin(day.getJSONObject("tmp")
									.getString("min"));
							dailyForecastList.add(dailyForecast);
						}
						
						//---圆环天气---
						cwcView.setDatas(weather);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}



	@Override
	public void updateViewOnLocationStart() {

	}

	/**
	 * 定位完成时更新界面
	 */
	@Override
	public void updateViewOnLocationFinish(Message msg) {
		AMapLocation loc = (AMapLocation) msg.obj;
		Map<String, String> result = Utils.getLocationStr(loc);
		if (!result.toString().equals("{}")) {
			String district = Utils.parseDistrictName(result
					.get("district"));
			String city = Utils.parseCityName(result.get("city"));
			String province = Utils.parseProvinceName(result
					.get("province"));
			// 先通过区域名查询城市代码，查询不到则通过城市名查询。
			CityDetail cityDetail = null;
			InputStream is;
			try {
				is = MainActivity.this.getAssets().open("citylist.xml");
				cityDetail = Utils.parse(is, district, province);
				if (cityDetail == null) {
					is = MainActivity.this.getAssets().open(
							"citylist.xml");
					cityDetail = Utils.parse(is, city, province);
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
	}

}
