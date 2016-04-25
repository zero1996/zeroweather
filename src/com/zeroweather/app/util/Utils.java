/**
 * 
 */
package com.zeroweather.app.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.amap.api.location.AMapLocation;
import com.zeroweather.app.model.CityDetail;

/**
 * 辅助工具类
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class Utils {
	/**
	 *  开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP= 2;
	/**
	 * 载入城市细节信息
	 */
	public static final int  MSG_LOAD_CITY_DETAIL = 4;
	
	public final static String KEY_URL = "URL";
	public final static String URL_H5LOCATION = "file:///android_asset/location.html";
	/**
	 * 根据定位结果返回定位信息的字符串
	 * @param loc
	 * @return
	 */
	public synchronized static Map<String,String> getLocationStr(AMapLocation location){
		if(null == location){
			return null;
		}
//		StringBuffer sb = new StringBuffer();
		Map<String,String> map = new HashMap<String,String>();
		//errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
		if(location.getErrorCode() == 0){
//			sb.append("定位成功" + "\n");
//			sb.append("定位类型: " + location.getLocationType() + "\n");
//			sb.append("经    度    : " + location.getLongitude() + "\n");
//			sb.append("纬    度    : " + location.getLatitude() + "\n");
//			sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//			sb.append("提供者    : " + location.getProvider() + "\n");
			if (location.getProvider().equalsIgnoreCase(
					android.location.LocationManager.GPS_PROVIDER)) {
				// 以下信息只有提供者是GPS时才会有
//				sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//				sb.append("角    度    : " + location.getBearing() + "\n");
//				// 获取当前提供定位服务的卫星个数
//				sb.append("星    数    : "
//						+ location.getSatellites() + "\n");
			} else {
				// 提供者是GPS时是没有以下信息的
//				sb.append("国    家    : " + location.getCountry() + "\n");
//				sb.append("省            : " + location.getProvince() + "\n");
//				sb.append("市            : " + location.getCity() + "\n");
//				sb.append("城市编码 : " + location.getCityCode() + "\n");
//				sb.append("区            : " + location.getDistrict() + "\n");
//				sb.append("区域 码   : " + location.getAdCode() + "\n");
//				sb.append("地    址    : " + location.getAddress() + "\n");
//				sb.append("兴趣点    : " + location.getPoiName() + "\n");
				map.put("country", location.getCountry());
				map.put("province", location.getProvince());
				map.put("city", location.getCity());
				map.put("district", location.getDistrict());
				map.put("address", location.getAddress());
			}
		} else {
			//定位失败
//			sb.append("定位失败" + "\n");
//			sb.append("错误码:" + location.getErrorCode() + "\n");
//			sb.append("错误信息:" + location.getErrorInfo() + "\n");
//			sb.append("错误描述:" + location.getLocationDetail() + "\n");
		}
		return map;
	}
	
	/**
	 * 解析定位的区域名
	 */
	public static String parseDistrictName(String district){
		String districtName = district;
		char tag = districtName.charAt(districtName.length()-1);
		if("区".equals(tag+"")){
			districtName = districtName.substring(0, districtName.length() - 1);
		}
		return districtName;
	}
	/**
	 * 解析定位的城市名
	 */
	public static String parseCityName(String city){
		String cityName = city;
		char tag = cityName.charAt(cityName.length()-1);
		if("市".equals(tag+"")){
			cityName = cityName.substring(0, cityName.length() - 1);
		}
		return cityName;
	}
	
	/**
	 * 解析定位的省份名
	 */
	public static String parseProvinceName(String province){
		String provinceName = province;
		char tag = provinceName.charAt(provinceName.length()-1);
		if("省".equals(tag+"")){
			provinceName = provinceName.substring(0, provinceName.length() - 1);
		}
		return provinceName;
	}
	
	/**
	 * 根据日期返回周几
	 * @param time 日期
	 * @return 周几
	 * @throws ParseException
	 */
	public static String dayForWeek(String time) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(time);
		 String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};  
	        Calendar cal = Calendar.getInstance();  
	        cal.setTime(date);  
	        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
	        if(week_index<0){  
	            week_index = 0;  
	        }   
	        return weeks[week_index];
	}
	
	public static String splitDate(String time){
		String [] date = time.split("-");
		return date[1]+"/"+date[2];
	}
	
	
}
