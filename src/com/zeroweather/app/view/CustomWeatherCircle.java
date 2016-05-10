package com.zeroweather.app.view;

import com.zeroweather.app.model.Weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CustomWeatherCircle extends View {
	private String mSr;//日出时间
	private String mSs;//日落时间
	private int mWidth;//画布宽
	private int mHeight;//画布高
	private Paint mDayPaint;//白天画笔
	private Paint mNightPaint;//晚上画笔
	private int mCircleWidth;//圆环宽度
	private int mCentre;//圆心
	private int mRadius;//半径
	private RectF mDayRect;//白天圆弧外轮廓矩形区域
	private RectF mNightRect;//晚上圆弧外轮廓矩形区域
	private static final int DAY = 1440;//一天时长
	private Paint mTmpPaint;//温度画笔
	
	public CustomWeatherCircle(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mDayPaint = new Paint();
		mDayPaint.setAntiAlias(true);//消除锯齿
		mDayPaint.setStrokeWidth(10);//设置圆环宽度
		mDayPaint.setStyle(Paint.Style.STROKE);//设置空心
		mDayPaint.setColor(Color.BLUE);
		
		mNightPaint = new Paint();
		mNightPaint.setAntiAlias(true);//消除锯齿
		mNightPaint.setStrokeWidth(10);//设置圆环宽度
		mNightPaint.setStyle(Paint.Style.STROKE);//设置空心
		mNightPaint.setColor(Color.BLACK);
		
		mTmpPaint = new Paint();
		mTmpPaint.setAntiAlias(true);
		mTmpPaint.setTextAlign(Align.CENTER);
		mTmpPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
		mTmpPaint.setColor(Color.BLACK);
	}

	public CustomWeatherCircle(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public CustomWeatherCircle(Context context) {
		this(context,null);
	}
	
	public void setDatas(Weather weather){
		mSr = weather.getSr();
		mSs = weather.getSs();
		postInvalidate();
	}
	
	public  void setDemension(int width,int height){
		mWidth = width;
		mHeight = height;
		mCircleWidth = mWidth*5/7 ;
		mCentre = mWidth /2;
		mRadius = mCircleWidth / 2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		mDayRect = new RectF(mCentre - mRadius,mCentre - mRadius,mCentre + mRadius,mCentre+mRadius);
		mNightRect = new RectF(mCentre - mRadius,mCentre - mRadius,mCentre + mRadius,mCentre+mRadius);
		float dayStartAngle;//白天圆弧起始角度
		float nightStartAngle;//晚上圆弧起始角度
		float daySweepAngle;//白天圆弧扫过角度
		float nightSweepAngle;//晚上圆弧扫过角度
		
		int dayHour = Integer.valueOf(formatAstro(mSr)[0]);
		int dayMinute = Integer.valueOf(formatAstro(mSr)[1]);
		int nightHour = Integer.valueOf(formatAstro(mSs)[0]);
		int nightMinute = Integer.valueOf(formatAstro(mSs)[1]);
		
//		int srLen = dayHour *60+dayMinute;//分钟化日出时间
//		int ssLen = nightHour *60+nightMinute;//分钟化日落时间
//		int dayLen = ssLen - srLen;//白天长度
//		float dayPct  = dayLen  *100 / DAY;//白天长度占一天长度的百分比
//		float daySweep = dayPct  * 360 /100 ;//白天圆弧扫过的角度
		
		dayStartAngle = (float) (90+dayHour*15+dayMinute*0.25);
		nightStartAngle = (float) (90+nightHour*15+nightMinute*0.25);
		
		daySweepAngle = nightStartAngle - dayStartAngle;
		nightSweepAngle = 360-daySweepAngle;
		canvas.drawArc(mDayRect,dayStartAngle,daySweepAngle,false,mDayPaint);
		canvas.drawArc(mNightRect,nightStartAngle,nightSweepAngle,false,mNightPaint);
//		float a;
//		if(daySweep > 180 ){
//			a = daySweep -180;
//			dayStartAngle = 180  - a;
//		}else{
//			a  = 180  - daySweep;
//			dayStartAngle = 180  +  a;
//		}
//		double h = Math.sin(a)*mRadius;
//		double w = Math.cos(a)*mRadius;
//		int x = (int) (mCentre - w);
//		int y = (int) (mCentre+h);
//		daySweepAngle = daySweep;
//		canvas.drawArc(mDayRect,dayStartAngle,daySweepAngle,false,mDayPaint);
//		canvas.drawArc(mNightRect,dayStartAngle+daySweepAngle,360 -daySweepAngle,false,mNightPaint); 
//		
//		FontMetrics tmpFM = mTmpPaint.getFontMetrics();
//		canvas.drawText("20°", mCentre, mCentre, mTmpPaint);
//		canvas.drawText("日出时间", mCentre-x, mCentre+y, mTmpPaint);
		
	}
	
	private String[] formatAstro(String astro){
		String[] astroArr = astro.split(":");
		return  astroArr;
	}

}
