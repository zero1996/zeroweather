package com.zeroweather.app.util;

import com.zeroweather.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CondPic {
	
	public static Bitmap getPic(Context context,int index){
		Bitmap bmp = null;
		switch (index) {
		case 100:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.sunny);
			break;
		case 101:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.cloudy);
			break;
		case 102:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.few_clouds);
			break;
		case 103:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.partly_cloudy);
			break;
		case 104:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.overcast);
			break;
		case 200:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.windy);
			break;
		case 201:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.clam);
			break;
		case 202:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.light_breeze);
			break;
		case 203:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.gentle_breeze);
			break;
		case 204:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.fresh_breeze);
			break;
		case 205:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.strong_breeze);
			break;
		case 206:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.high_wind);
			break;
		case 207:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.gale);
			break;
		case 208:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.strong_gale);
			break;
		case 209:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.storm);
			break;
		case 210:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.violent_storm);
			break;
		case 211:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.hurricane);
			break;
		case 212:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.tornado);
			break;
		case 213:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.tropical_storm);
			break;
		case 300:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.shower_rain);
			break;
		case 301:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.heavy_shower_rain);
			break;
		case 302:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.thundershower);
			break;
		case 303:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.heavy_thunderstorm);
			break;
		case 304:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.hail);
			break;
		case 305:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.light_rain);
			break;
		case 306:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.moderate_rain);
			break;
		case 307:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.heavy_rain);
			break;
		case 308:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.extreme_rain);
			break;
		case 309:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.drizzle_rain);
			break;
		case 310:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.storm);
			break;
		case 311:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.heavy_storm);
			break;
		case 312:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.severe_storm);
			break;
		case 313:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.freezing_rain);
			break;
		case 400:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.light_snow);
			break;
		case 401:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.moderate_snow);
			break;
		case 402:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.heavy_snow);
			break;
		case 403:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.snowstorm);
			break;
		case 404:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.sleet);
			break;
		case 405:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.rain_and_snow);
			break;
		case 406:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.shower_snow);
			break;
		case 407:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.snow_flurry);
			break;
		case 500:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.mist);
			break;
		case 501:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.foggy);
			break;
		case 502:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.haze);
			break;
		case 503:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.sand);
			break;
		case 505:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.dust);
			break;
		case 508:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.sandstorm);
			break;

		default:
			bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.unknown);
			break;
		}
		return bmp;
	}
	
	public static Bitmap getSmallPic(Context context,int index,int dimension){
		return Bitmap.createScaledBitmap(getPic(context, index), dimension, dimension, true);
	}
	

}
