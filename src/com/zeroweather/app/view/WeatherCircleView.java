package com.zeroweather.app.view;

import com.zeroweather.app.R;
import com.zeroweather.app.R.color;
import com.zeroweather.app.model.Weather;
import com.zeroweather.app.util.CondPic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class WeatherCircleView extends View {
	private Context mContext;
	private Weather mWeather;
	private String mSr;//日出时间
	private String mSs;//日落时间
	private int mWidth;//画布宽
	private int mHeight;//画布高
	private Paint mArcPaint;//圆弧画笔
	private int mCircleWidth;//圆环宽度
	private int mCentre;//圆心
	private int mRadius;//半径
	private RectF mRect;//圆弧外轮廓矩形区域
	private static final int DAY = 1440;//一天时长
	private Paint mAstroPaint;//日出日落时间画笔
	private boolean isFirstDraw = true;
	private Paint mTmpPaint;//温度画笔

	public WeatherCircleView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);//消除锯齿
		mArcPaint.setStrokeWidth(7);//设置圆环宽度
		mArcPaint.setStyle(Paint.Style.STROKE);//设置空心
		mArcPaint.setColor(getResources().getColor(R.color.white));


		mAstroPaint = new Paint();
		mAstroPaint.setAntiAlias(true);
		mAstroPaint.setTextAlign(Align.RIGHT);
		mAstroPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
		mAstroPaint.setColor(getResources().getColor(R.color.white));
		
		mTmpPaint = new Paint();
		mTmpPaint.setAntiAlias(true);
		mTmpPaint.setTextAlign(Align.CENTER);
		mTmpPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 56, getResources().getDisplayMetrics()));
		mTmpPaint.setColor(getResources().getColor(R.color.white));
	}

	public WeatherCircleView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public WeatherCircleView(Context context) {
		this(context,null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mWidth, mHeight);
	}
	
	public void setDatas(Weather weather){
		mWeather = weather;
		mSr = mWeather.getSr();
		mSs = mWeather.getSs();
		isFirstDraw = false;
		postInvalidate();
	}

	public  void setDimension(int width,int height){
		mWidth = width;
		mHeight = height;
		mCircleWidth = mWidth*5/7 ;
		mCentre = mWidth /2;
		mRadius = mCircleWidth / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(isFirstDraw)
			return;
		
		mRect = new RectF(mCentre - mRadius,mCentre - mRadius,mCentre + mRadius,mCentre+mRadius);
		float dayStartAngle;//白天圆弧起始角度
		float nightStartAngle;//晚上圆弧起始角度
		float daySweepAngle;//白天圆弧扫过角度
		float nightSweepAngle;//晚上圆弧扫过角度

		int dayHour = Integer.valueOf(formatAstro(mSr)[0]);
		int dayMinute = Integer.valueOf(formatAstro(mSr)[1]);
		int nightHour = Integer.valueOf(formatAstro(mSs)[0]);
		int nightMinute = Integer.valueOf(formatAstro(mSs)[1]);


		dayStartAngle = (float) (90+dayHour*15+dayMinute*0.25);
		nightStartAngle = (float) (90+nightHour*15+nightMinute*0.25);

		daySweepAngle = nightStartAngle - dayStartAngle;
		nightSweepAngle = 360-daySweepAngle;
		canvas.drawArc(mRect,dayStartAngle+15,daySweepAngle-15,false,mArcPaint);//画白天圆弧
		mArcPaint.setColor(getResources().getColor(R.color.black_blue));
		canvas.drawArc(mRect,nightStartAngle+15,nightSweepAngle-15,false,mArcPaint);//画晚上圆弧
		float tmpY = mCentre+(mTmpPaint.getFontMetrics().bottom-mTmpPaint.getFontMetricsInt().top)/2;//温度Y坐标
		canvas.drawText(mWeather.getNowTemp()+"°", mCentre,  tmpY,mTmpPaint);//画当前温度
		mAstroPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
		float condY = tmpY - (mAstroPaint.getFontMetrics().bottom - mAstroPaint.getFontMetrics().top)-(mTmpPaint.getFontMetrics().bottom-mTmpPaint.getFontMetricsInt().top)/2;//天气Y坐标
		canvas.drawText(mWeather.getNowCondTxt(), mCentre, condY , mAstroPaint);//画天气状况描述
		Bitmap condBmp = CondPic.getSmallPic(mContext, Integer.valueOf(mWeather.getNowCondCode()), mWidth/9);//当前天气图片
		canvas.drawBitmap(condBmp, mCentre-mAstroPaint.measureText(mWeather.getNowCondTxt())-condBmp.getWidth(), condY-condBmp.getHeight()*2/3, null);//画当前天气图片
		
		/**
		 * 计算日出图片和时间位置
		 */
		double dAngle =  dayStartAngle + 7.5;
		float dx;
		float dy;
		if(dAngle<180){
			dAngle = 180 - dAngle;
			dAngle = Math.toRadians(dAngle);
			dx = (float) (mCentre - Math.cos(dAngle)*mRadius);
			dy = (float) (mCentre + Math.sin(dAngle)*mRadius);
		}else{
			dAngle = dAngle - 180;
			dAngle = Math.toRadians(dAngle);
			dx = (float) (mCentre - Math.cos(dAngle)*mRadius);
			dy =  (float) (mCentre - Math.sin(dAngle)*mRadius);
		}
		
		int bmpSize = mWidth / 7;//图片大小
		Bitmap srBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sr), bmpSize, bmpSize, true);//日出图片
		canvas.drawBitmap(srBmp, dx-bmpSize/2, dy-bmpSize/2, null);//画日出图片
		mAstroPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
		canvas.drawText(mSr, dx+bmpSize/2+mAstroPaint.measureText(mSr), dy+(mAstroPaint.getFontMetrics().bottom-mAstroPaint.getFontMetrics().top)/3, mAstroPaint);//画日出时间
		
		/**
		 * 计算日落图片和时间位置
		 */
		double nAngle =  nightStartAngle + 7.5;
		float nx;
		float ny;
		if(nAngle<360){
			nAngle = 360 - nAngle;
			nAngle = Math.toRadians(nAngle);
			nx = (float) (mCentre + Math.cos(nAngle)*mRadius);
			ny = (float) (mCentre - Math.sin(nAngle)*mRadius);
		}else{
			nAngle = nAngle - 360;
			nAngle = Math.toRadians(nAngle);
			nx = (float) (mCentre + Math.cos(nAngle)*mRadius);
			ny =  (float) (mCentre + Math.sin(nAngle)*mRadius);
		}
		Bitmap ssBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ss), bmpSize, bmpSize, true);//日落图片
		canvas.drawBitmap(ssBmp, nx-bmpSize/2, ny-bmpSize/2, null);//画日落图片
		canvas.drawText(mSs, nx-bmpSize/3, ny, mAstroPaint);//画日落时间

	}

	private String[] formatAstro(String astro){
		String[] astroArr = astro.split(":");
		return  astroArr;
	}

}
