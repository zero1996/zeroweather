package com.zeroweather.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.zeroweather.app.model.DailyForecast;
import com.zeroweather.app.util.CondPic;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class DailyForecastView extends View {
	private List<DailyForecast> mDatas;//数据源
	private Paint mTextPaint;//文字 画笔
	private Paint mPointPaint;//点 画笔 
	private Paint mLinePaint;//线 画笔
	private Paint mPathPaint;//路径画笔
	private int w;//控件宽
	private int h;//控件高
	private int x[] = new int[7];
	private int mTmpSpace = 20;//点的单位间距
	private int mTmpMax;
	private Bitmap[] mCondPics;//天气状态图片
	private Context mContext;
	private int mRadius = 8;
	private boolean isFirstDraw = true;
	
	private List<Map<String,Float>> dots;


	public DailyForecastView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mContext = context;
		
		dots = new LinkedList<Map<String,Float>>();
		
		
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
		mLinePaint.setColor(Color.parseColor("#E0E0E0"));
		mLinePaint.setStrokeWidth(4);
		mLinePaint.setAlpha(40);
		mLinePaint.setStyle(Style.FILL);
		
		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setColor(Color.parseColor("#E0E0E0"));
		mPathPaint.setAlpha(80);
		mPathPaint.setStyle(Style.FILL);
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
		x[0] = w*1/14;
		x[1] = w*3/14;
		x[2] = w*5/14;
		x[3] = w*7/14;
		x[4] = w*9/14;
		x[5] = w*11/14;
		x[6] = w*13/14;
		
		this.h = height;
		mTmpSpace = h/2/50;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(w, h);
	}

	public void setData(List<DailyForecast> datas){
		isFirstDraw = false;
		this.mDatas = datas;
		/*
		 * 获取七日最高温度
		 */
		for(int i =0;i<mDatas.size();i++){
			DailyForecast d = mDatas.get(i);
			mCondPics [i] = CondPic.getSmallPic(mContext, Integer.valueOf(d.getCond()), w/14*19/20) ;
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
		if(isFirstDraw)
			return;
		int baseH = h/2;//画图基线
		FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		
		int lastMaxX = 0;
		int lastMaxY = 0;
		int lastMinX = 0;
		int lastMinY = 0;
		for(int i=0;i<18;i++){
			dots.add(new HashMap<String,Float>());
		}
		
		for(int i=0;i<mDatas.size();i++){
			DailyForecast d = mDatas.get(i);
			/*
			 * 画高温点
			 */
			int max = Integer.valueOf(d.getTmpMax());//高温
			int tmpMaxSpace = (mTmpMax - max)*mTmpSpace*2;//高温距基线的距离
			int tmpMaxY = baseH + tmpMaxSpace; //高温点Y坐标
			canvas.drawCircle(x[i], tmpMaxY, mRadius, mPointPaint);//画高温点
			/*
			 *画高温折线
			 */
			
			Map m = new HashMap<String,Float>();
			m.put("x", (float)x[i]);
			m.put("y", (float)tmpMaxY);
			dots.set(i+1, m);
			
			if(i != 0){
//				canvas.drawLine(lastMaxX, lastMaxY, x[i], tmpMaxY, mLinePaint);
			}
			if(i ==1){
				int startY = (tmpMaxY-lastMaxY)/2*(-1)+lastMaxY;
//				canvas.drawLine(0, startY, lastMaxX, lastMaxY, mLinePaint);
				Map dot = new HashMap<String,Float>();
				dot.put("x", (float)0);
				dot.put("y", (float)startY);
				dots.set(0, dot);
				
			}
			if(i==6){
				int endY = (tmpMaxY-lastMaxY)/2+tmpMaxY;
//				canvas.drawLine(x[i], tmpMaxY,w , endY, mLinePaint);
				Map dot = new HashMap<String,Float>();
				dot.put("x", (float)w);
				dot.put("y", (float)endY);
				dots.set(8, dot);
				
			}
			lastMaxX = x[i];
			lastMaxY = tmpMaxY;
			/*
			 * 画高温温度
			 */
			float tmpTextSpace = fontMetrics.bottom -fontMetrics.top;
			int tmpMaxTextY = (int) (tmpMaxY -tmpTextSpace);
			canvas.drawText(d.getTmpMax()+"°", x[i], tmpMaxTextY, mTextPaint);
			
			/*
			 * 画天气状态图标
			 */
			int fontBitmapY = baseH - mTmpSpace*25;
			canvas.drawBitmap(mCondPics[i], x[i]-mCondPics[i].getWidth()/2, fontBitmapY, null);
			/*
			 * 画日期
			 */
			int weekY = fontBitmapY - mTmpSpace*10;
			canvas.drawText(d.getWeek(), x[i], weekY, mTextPaint);
			int dateY = weekY - mTmpSpace*7;
			canvas.drawText(d.getDate(), x[i], dateY, mTextPaint);
			/*
			 * 画低温点
			 */
			int min = Integer.valueOf(d.getTmpMin());
			int tmpMinY = tmpMaxY + (max-min)*mTmpSpace*2;
			canvas.drawCircle(x[i], tmpMinY, mRadius, mPointPaint);
			/*
			 *画低温折线
			 */
			
			Map ma = new HashMap<String,Float>();
			ma.put("x", (float)x[i]);
			ma.put("y", (float)tmpMinY);
			dots.set(16-i, ma);
			
			if(i != 0){
//				canvas.drawLine(lastMinX, lastMinY, x[i], tmpMinY, mLinePaint);
				
			}
			if(i ==1){
				int startY = (tmpMinY-lastMinY)/2*(-1)+lastMinY;
//				canvas.drawLine(0, startY, lastMinX, lastMinY, mLinePaint);
				Map dot = new HashMap<String,Float>();
				dot.put("x", (float)0);
				dot.put("y", (float)startY);
				dots.set(17, dot);
				
			}
			if(i==6){
				int endY = (tmpMinY-lastMinY)/2+tmpMinY;
//				canvas.drawLine(x[i], tmpMinY,w , endY, mLinePaint);
				Map dot = new HashMap<String,Float>();
				dot.put("x", (float)w);
				dot.put("y", (float)endY);
				dots.set(9, dot);
			}
			lastMinX = x[i];
			lastMinY = tmpMinY;
			
			/*
			 * 画低温温度
			 */
			int tmpMinTextY = (int) (tmpMinY + tmpTextSpace);
			canvas.drawText(d.getTmpMin()+"°", x[i], tmpMinTextY, mTextPaint);
			
			if(i != 6)
			canvas.drawLine(x[i]+w*1/14, 0, x[i]+w*1/14, baseH+mTmpSpace*50, mLinePaint);
		}
		Path path = new Path();
		for(int i = 0;i<dots.size();i++){
			Map<String,Float> m = dots.get(i);
			if(i == 0){
				path.moveTo(m.get("x"),m.get("y"));
			}else{
				path.lineTo(m.get("x"),m.get("y"));
			}
		}
		path.close();
		canvas.drawPath(path, mPathPaint);
	}

}
