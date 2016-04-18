package com.zeroweather.app.model;

public class Weather {
	private String city;//城市名
	private String date;//日期
	private String nowTemp;//当前温度
	private String nowCondTxt;//当前天气状况描述
	private String todayTempMax;//当日最高温度
	private String todayTempMin;//当日最低温度
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNowTemp() {
		return nowTemp;
	}
	public void setNowTemp(String nowTemp) {
		this.nowTemp = nowTemp;
	}
	public String getNowCondTxt() {
		return nowCondTxt;
	}
	public void setNowCondTxt(String nowCondTxt) {
		this.nowCondTxt = nowCondTxt;
	}
	public String getTodayTempMax() {
		return todayTempMax;
	}
	public void setTodayTempMax(String todayTempMax) {
		this.todayTempMax = todayTempMax;
	}
	public String getTodayTempMin() {
		return todayTempMin;
	}
	public void setTodayTempMin(String todayTempMin) {
		this.todayTempMin = todayTempMin;
	}
	

}
