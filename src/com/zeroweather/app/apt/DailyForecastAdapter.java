package com.zeroweather.app.apt;

import java.util.List;

import com.zeroweather.app.R;
import com.zeroweather.app.model.DailyForecast;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DailyForecastAdapter extends ArrayAdapter<DailyForecast> {
	private Context context;
	private int resId;
	private List<DailyForecast> datas;

	public DailyForecastAdapter(Context context, int resource,
			 List<DailyForecast> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resId  = resource;
		this.datas = objects;
	}
	
	
	@Override
	public DailyForecast getItem(int position) {
		return datas.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(resId, null);
			holder = new ViewHolder();
			holder.dateTV = (TextView) convertView.findViewById(R.id.dailyforecast_date);
			holder.weekTV = (TextView) convertView.findViewById(R.id.dailyforecast_week);
			holder.condIV = (ImageView) convertView.findViewById(R.id.dailyforecast_cond);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		DailyForecast data = getItem(position);
		holder.dateTV.setText(data.getDate());
		holder.weekTV.setText(data.getWeek());
		switch (Integer.valueOf(data.getCond())) {
		case 100:
			holder.condIV.setImageResource(R.drawable.sunny);
			break;
		case 101:
			holder.condIV.setImageResource(R.drawable.cloudy);
			break;
		case 102:
			holder.condIV.setImageResource(R.drawable.few_clouds);
			break;
		case 103:
			holder.condIV.setImageResource(R.drawable.partly_cloudy);
			break;
		case 104:
			holder.condIV.setImageResource(R.drawable.overcast);
			break;
		case 200:
			holder.condIV.setImageResource(R.drawable.windy);
			break;
		case 201:
			holder.condIV.setImageResource(R.drawable.clam);
			break;
		case 202:
			holder.condIV.setImageResource(R.drawable.light_breeze);
			break;
		case 203:
			holder.condIV.setImageResource(R.drawable.gentle_breeze);
			break;
		case 204:
			holder.condIV.setImageResource(R.drawable.fresh_breeze);
			break;
		case 205:
			holder.condIV.setImageResource(R.drawable.strong_breeze);
			break;
		case 206:
			holder.condIV.setImageResource(R.drawable.high_wind);
			break;
		case 207:
			holder.condIV.setImageResource(R.drawable.gale);
			break;
		case 208:
			holder.condIV.setImageResource(R.drawable.strong_gale);
			break;
		case 209:
			holder.condIV.setImageResource(R.drawable.storm);
			break;
		case 210:
			holder.condIV.setImageResource(R.drawable.violent_storm);
			break;
		case 211:
			holder.condIV.setImageResource(R.drawable.hurricane);
			break;
		case 212:
			holder.condIV.setImageResource(R.drawable.tornado);
			break;
		case 213:
			holder.condIV.setImageResource(R.drawable.tropical_storm);
			break;
		case 300:
			holder.condIV.setImageResource(R.drawable.shower_rain);
			break;
		case 301:
			holder.condIV.setImageResource(R.drawable.heavy_shower_rain);
			break;
		case 302:
			holder.condIV.setImageResource(R.drawable.thundershower);
			break;
		case 303:
			holder.condIV.setImageResource(R.drawable.heavy_thunderstorm);
			break;
		case 304:
			holder.condIV.setImageResource(R.drawable.hail);
			break;
		case 305:
			holder.condIV.setImageResource(R.drawable.light_rain);
			break;
		case 306:
			holder.condIV.setImageResource(R.drawable.moderate_rain);
			break;
		case 307:
			holder.condIV.setImageResource(R.drawable.heavy_rain);
			break;
		case 308:
			holder.condIV.setImageResource(R.drawable.extreme_rain);
			break;
		case 309:
			holder.condIV.setImageResource(R.drawable.drizzle_rain);
			break;
		case 310:
			holder.condIV.setImageResource(R.drawable.storm);
			break;
		case 311:
			holder.condIV.setImageResource(R.drawable.heavy_storm);
			break;
		case 312:
			holder.condIV.setImageResource(R.drawable.severe_storm);
			break;
		case 313:
			holder.condIV.setImageResource(R.drawable.freezing_rain);
			break;
		case 400:
			holder.condIV.setImageResource(R.drawable.light_snow);
			break;
		case 401:
			holder.condIV.setImageResource(R.drawable.moderate_snow);
			break;
		case 402:
			holder.condIV.setImageResource(R.drawable.heavy_snow);
			break;
		case 403:
			holder.condIV.setImageResource(R.drawable.snowstorm);
			break;
		case 404:
			holder.condIV.setImageResource(R.drawable.sleet);
			break;
		case 405:
			holder.condIV.setImageResource(R.drawable.rain_and_snow);
			break;
		case 406:
			holder.condIV.setImageResource(R.drawable.shower_snow);
			break;
		case 407:
			holder.condIV.setImageResource(R.drawable.snow_flurry);
			break;
		case 500:
			holder.condIV.setImageResource(R.drawable.mist);
			break;
		case 501:
			holder.condIV.setImageResource(R.drawable.foggy);
			break;
		case 502:
			holder.condIV.setImageResource(R.drawable.haze);
			break;
		case 503:
			holder.condIV.setImageResource(R.drawable.sand);
			break;
		case 505:
			holder.condIV.setImageResource(R.drawable.dust);
			break;
		case 508:
			holder.condIV.setImageResource(R.drawable.sandstorm);
			break;

		default:
			holder.condIV.setImageResource(R.drawable.unknown);
			break;
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView dateTV;
		TextView weekTV;
		ImageView condIV;
	}

}
