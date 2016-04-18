package com.zeroweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ZeroWeatherOpenHelper extends SQLiteOpenHelper {

//	public static final String CREATE_PROVINCE = "create table Province (id integer primary key autoincrement,province_name text,province_code text)";
//	public static final String CREATE_CITY = "create table City (id integer primary key autoincrement,city_name text,city_code text,province_id integer)";
//	public static final String CREATE_COUNTY = "create table COUNTY (id integer primary key autoincrement,county_name text,county_code text,city_id integer)";
	public static final String CREATE_CITY_DETAIL = "create table CityDetail (id integer primary key autoincrement,country text,province text,city,code text)";

	public ZeroWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(CREATE_PROVINCE);// ����Province��
//		db.execSQL(CREATE_CITY);// ����City��
//		db.execSQL(CREATE_COUNTY);// ����County��
		db.execSQL(CREATE_CITY_DETAIL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
