package com.zeroweather.app.apt;

import java.util.List;

import com.zeroweather.app.R;
import com.zeroweather.app.model.Grid;
import com.zeroweather.app.model.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends ArrayAdapter<Grid> {
	private Context mContext;
	private int mResId;
	private List<Grid> mDatas;
	private int i;

	public GridAdapter(Context context, int resource,
			List<Grid> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mResId = resource;
		this.mDatas = objects;
	}

	@Override
	public Grid getItem(int position) {
		return mDatas.get(position);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Grid g = mDatas.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(mResId, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.grid_img);
			holder.title = (TextView) convertView.findViewById(R.id.grid_title);
			holder.content = (TextView) convertView.findViewById(R.id.grid_content);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setImageResource(g.getImgId());
		holder.title.setText(g.getTitle());
		holder.content.setText(g.getContent());

		return convertView;
	}
	class ViewHolder{
		ImageView img;
		TextView title;
		TextView content;
	}

}
