package com.zeroweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.zeroweather.app.model.City;
import com.zeroweather.app.model.CityDetail;
import com.zeroweather.app.model.County;
import com.zeroweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ZeroWeatherDB {
	public static final String DB_NAME = "zero_weather";
	public static final int VERSION = 1;
	private static ZeroWeatherDB zeroWeatherDB;
	private SQLiteDatabase db;

	private ZeroWeatherDB(Context context) {
		ZeroWeatherOpenHelper dbHelper = new ZeroWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public synchronized static ZeroWeatherDB getInstance(Context context) {
		if (zeroWeatherDB == null) {
			zeroWeatherDB = new ZeroWeatherDB(context);
		}
		return zeroWeatherDB;
	}

	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getcityName());
			values.put("city_code", city.getcityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	public List<City> loadCity(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setcityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setcityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(provinceId));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getcountyName());
			values.put("county_code", county.getcountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	public List<County> loadCounty(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setcountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setcountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;

	}

	/**
	 * 储存城市细节信息
	 */
	public void saveCityDetail(CityDetail cityDetail) {
		if (cityDetail != null) {
			ContentValues values = new ContentValues();
			values.put("country", cityDetail.getCountry());
			values.put("province", cityDetail.getProvince());
			values.put("city", cityDetail.getCity());
			values.put("code", cityDetail.getId());
			db.insert("CityDetail", null, values);
		}
	}

	public List<String> loadCityDetail() {
		List<String> datas = new ArrayList<String>();
		Cursor cursor = db.query("CityDetail", null, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				datas.add(cursor.getString(cursor.getColumnIndex("city")));
			} while (cursor.moveToNext());
		}
		return datas;

	}

}
