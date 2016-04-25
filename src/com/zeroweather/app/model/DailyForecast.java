package com.zeroweather.app.model;
/**
 * 每日预报实体类 
 * @author Zero
 */
public class DailyForecast {
	private String date;//日期
	private String week;//周几
	private String cond;//天气
	private String tmpMax;//最高温
	private String tmpMin;//最低温
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getCond() {
		return cond;
	}
	public void setCond(String cond) {
		this.cond = cond;
	}
	public String getTmpMax() {
		return tmpMax;
	}
	public void setTmpMax(String tmpMax) {
		this.tmpMax = tmpMax;
	}
	public String getTmpMin() {
		return tmpMin;
	}
	public void setTmpMin(String tmpMin) {
		this.tmpMin = tmpMin;
	}

}
