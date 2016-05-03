package com.zeroweather.app.view;

import java.util.List;

import com.zeroweather.app.model.DailyForecast;
import com.zeroweather.app.util.CondPic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

public class DailyForecastView extends View {
	private List<DailyForecast> mDatas;//数据源
	private Paint mTextPaint;//文字 画笔
	private Paint mPointPaint;//点 画笔 
	private Paint mLinePaint;//折线 画笔
	private int w;//控件宽
	private int h;//控件高
	private int x[] = new int[7];
	private int mTmpSpace = 12;//点的单位间距
	private int mTmpMax;
	private Bitmap[] mCondPics;//天气状态图片
	private Context mContext;


	public DailyForecastView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		
		mCondPics = new Bitmap[7];
		/*
		 * 初始化各个画笔
		 */
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
		mTextPaint.setTextAlign(Align.CENTER);

		mPointPaint = new Paint();
		mPointPaint.setAntiAlias(true);
		mPointPaint.setColor(Color.WHITE);

		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setColor(Color.WHITE);
		mLinePaint.setStrokeWidth(4);
		mLinePaint.setStyle(Style.FILL);
	}

	public DailyForecastView(Context context, AttributeSet attrs) {
		this(context, attrs,0); 
	}

	public DailyForecastView(Context context) {
		this(context,null);
	}


	public void setDimensions(int width,int height){
		w = width;
		h = height;
		Log.i("w:", w+"");
		Log.i("h:", h+"");
		x[0] = w*1/14;
		x[1] = w*3/14;
		x[2] = w*5/14;
		x[3] = w*7/14;
		x[4] = w*9/14;
		x[5] = w*11/14;
		x[6] = w*13/14;
		
		this.h = height;
	}

	public void setData(List<DailyForecast> datas){
		this.mDatas = datas;
		/*
		 * 获取七日最高温度
		 */
		for(int i =0;i<mDatas.size();i++){
			DailyForecast d = mDatas.get(i);
			mCondPics [i] = CondPic.getSmallPic(mContext, Integer.valueOf(d.getCond()), w/14*3/5) ;
			if(i == 0){
				mTmpMax = Integer.valueOf(d.getTmpMax());
			}else{
				int max = Integer.valueOf(d.getTmpMax());
				if(max > mTmpMax){
					mTmpMax = max;
				}
			}
		}
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int baseH = h/2;
		Log.i("baseH:", baseH+"");
		for(int i=0;i<mDatas.size();i++){
			int max = Integer.valueOf(mDatas.get(i).getTmpMax());
			int space = (mTmpMax - max)*mTmpSpace;
			Log.i("baseH-space:", baseH - space+"");
			canvas.drawCircle(x[i], baseH - space, 8, mPointPaint);
		}
	}

}
