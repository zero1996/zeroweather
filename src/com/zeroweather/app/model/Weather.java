package com.zeroweather.app.model;

public class Weather {
	private String city;//城市名
	private String date;//日期
	private String nowTemp;//当前温度
	private String nowCondTxt;//当前天气状况描述
	private String todayTempMax;//当日最高温度
	private String todayTempMin;//当日最低温度
	private String windDir;//风向
	private String windSc;//风力
	private String hum;//湿度
	private String uv;//紫外线指数
	private String sportBrf;//运动指数,简要描述
	private String cwBrf;//洗车指数,简要描述
	private String drsgBrf;//穿衣指数,简要描述
	private String sportTxt;//运动指数,详细描述
	private String cwTxt;//洗车指数,详细描述
	private String drsgTxt;//穿衣指数,详细描述
	private String aqi;//空气质量指数
	private String qlty;//空气质量类别
	
	public String getWindDir() {
		return windDir;
	}
	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}
	public String getWindSc() {
		return windSc;
	}
	public void setWindSc(String windSc) {
		this.windSc = windSc;
	}
	public String getHum() {
		return hum;
	}
	public void setHum(String hum) {
		this.hum = hum;
	}
	public String getUv() {
		return uv;
	}
	public void setUv(String uv) {
		this.uv = uv;
	}
	public String getSportBrf() {
		return sportBrf;
	}
	public void setSportBrf(String sportBrf) {
		this.sportBrf = sportBrf;
	}
	public String getCwBrf() {
		return cwBrf;
	}
	public void setCwBrf(String cwBrf) {
		this.cwBrf = cwBrf;
	}
	public String getDrsgBrf() {
		return drsgBrf;
	}
	public void setDrsgBrf(String drsgBrf) {
		this.drsgBrf = drsgBrf;
	}
	public String getSportTxt() {
		return sportTxt;
	}
	public void setSportTxt(String sportTxt) {
		this.sportTxt = sportTxt;
	}
	public String getCwTxt() {
		return cwTxt;
	}
	public void setCwTxt(String cwTxt) {
		this.cwTxt = cwTxt;
	}
	public String getDrsgTxt() {
		return drsgTxt;
	}
	public void setDrsgTxt(String drsgTxt) {
		this.drsgTxt = drsgTxt;
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	public String getQlty() {
		return qlty;
	}
	public void setQlty(String qlty) {
		this.qlty = qlty;
	}
	
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
