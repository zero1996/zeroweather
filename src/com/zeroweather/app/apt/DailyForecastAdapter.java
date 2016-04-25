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
			holder.lineIV = (ImageView) convertView.findViewById(R.id.dailyforecast_line);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		DailyForecast data = getItem(position);
		holder.dateTV.setText(data.getDate());
		holder.weekTV.setText(data.getWeek());
		holder.condIV.setImageResource(R.drawable.icon);
		holder.lineIV.setBackgroundColor(Color.WHITE);
		return convertView;
	}
	
	class ViewHolder{
		TextView dateTV;
		TextView weekTV;
		ImageView condIV;
		ImageView lineIV;
	}

}
