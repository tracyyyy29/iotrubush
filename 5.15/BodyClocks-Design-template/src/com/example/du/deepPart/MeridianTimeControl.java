package com.example.du.deepPart;

import java.util.Calendar;
import java.util.Date;

import com.example.du15.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.Log;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * MeridianTimeControl extends BaseTimeControl
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class MeridianTimeControl extends AbstractTimeControl {
	private static MeridianTimeControl instance = null;

	public static MeridianTimeControl getInstance() {
		if (instance == null)
			instance = new MeridianTimeControl();
		return instance;
	}

	private MeridianTimeControl() {
	}

	@Override
	public Bitmap drawClock() {
		float clockWidth = canvasWidth;
		float clockHeight = canvasHeight;
		float clockPadding = canvasPadding;

		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(3); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		
		//画八卦图
		Bitmap baguaImage =  BitmapFactory.decodeResource(context.getResources(),R.drawable.baguashuimo);
		int baguaImageWidth = baguaImage.getWidth();
		int baguaImageHeight = baguaImage.getHeight();
		//计算缩放比例
		int newWidth,newHeight;
		float scale = (2*(clockWidth / 5- clockPadding)-10)/baguaImageWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBaguaImage = Bitmap.createBitmap(baguaImage,0,0,baguaImageWidth,baguaImageHeight,matrix,true);

		canvas.drawBitmap(newBaguaImage,clockWidth/2-clockWidth / 5+ clockPadding+5,
				clockWidth/2-clockWidth / 5+ clockPadding+5,paintCircle);

		
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);

		//画最内圈 八卦图的外圈
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 5
				- clockPadding, paintCircle);
		//画子丑寅卯的内外两圈
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 3
				- clockPadding, paintCircle);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 2*clockWidth / 5
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5, paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 24; i++) {

			paintDegree.setStrokeWidth(2);
			canvas.drawLine(clockWidth / 2, clockWidth/2-clockWidth/5+clockPadding, clockWidth / 2,
					clockWidth/2-clockWidth/5+clockPadding-(float) (clockPadding * 0.5), paintDegree);

			canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		}
		canvas.save();
		
		canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		for(int i = 0;i<12;i++)
		{
			//画分割线
			canvas.drawLine(clockWidth/2, clockPadding, 
					clockWidth/2, clockPadding+clockWidth/2-clockWidth/3, paintDegree);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		canvas.restore();
		canvas.save();
		
		
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(3);
		paintPointer.setColor(Color.RED);
		paintPointer.setAntiAlias(true);
		int hour = currentDate.getHours();
		int minute = currentDate.getMinutes();
		float degree_hour;
		degree_hour = 15*hour+minute/4;
		canvas.rotate(degree_hour);
		canvas.drawLine(0, 0, 0, -clockWidth / 2 + 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(10);
		paintText.setAntiAlias(true);
		String str1 = "经", str2 = "络", str3 = "时";
		canvas.drawText(
				str1,
				clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
				clockHeight / 2, paintText);
		paintText.setColor(Color.WHITE);
		canvas.drawText(str2, clockWidth / 2 - paintText.measureText(str2) / 2,
				clockHeight / 2 - clockPadding, paintText);
		canvas.drawText(
				str3,
				clockWidth / 2 + clockPadding - paintText.measureText(str3) / 2,
				clockHeight / 2, paintText);
		
		paintText.setColor(Color.BLACK);
		for (int i = 0; i < 24; i++) {
			
			String str31 = Constants.CLOCK_24HOUR_STRINGS[i];
			float offset = paintText.descent() - paintText.ascent();
			float offset3 = paintText.measureText(str31) / 2;
			canvas.drawText(str31, clockWidth / 2 - offset3,
					clockWidth/2-clockWidth/5+clockPadding-(float) (clockPadding * 0.25)-offset, paintText);
			canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		}
		
		for (int i = 0; i < 12; i++) {

			String str11 = Constants.MERIDIAN_TERM_STRINGS[i];
			String str21 = Constants.LUNAR_HOUR_STRINGS[i];
			float offset = paintText.descent() - paintText.ascent();
			float offset1 = paintText.measureText(str11) / 2;
			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str11, clockWidth / 2 - offset1, 
					clockWidth/2-2*clockWidth / 5
					+ clockPadding-offset2/2, paintText);
			canvas.drawText(str21, clockWidth / 2 - offset2,
					clockWidth/2-clockWidth / 3
					+ clockPadding-offset2/2, paintText);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		
		canvas.restore();

		return bm;
	}

	@Override
	public void updateData() {
		getCurrentDate();
	}
	
	
	/**
	 * 当前日期
	 */
    protected static Date currentDate;
    
    
    public Date getCurrentDate() {
		currentDate=new Date(System.currentTimeMillis());
		SetTestText.addText("经络时表获取currentDate");
		return currentDate;
	}
    public String getInfoText() {
    	//Log.e("tag", String.valueOf(MeridianTimeControl.currentDate.getHours()/2+1));
		String str=
				"经络时信息"
					+"\r\n"+"【当前时辰】"+Constants.LUNAR_HOUR_STRINGS[(MeridianTimeControl.currentDate.getHours()+1)/2%12]
					+"\r\n"+"【对应经络】"+Constants.MERIDIAN_TERM_STRINGS[(MeridianTimeControl.currentDate.getHours()+1)/2%12]
					+"\r\n"+"【其他信息】"+Constants.MERIDIAN_ADD_STRINGS[(MeridianTimeControl.currentDate.getHours()+1)/2%12]
							;
		SetTestText.addText("经络时表获取infoText");
		return str;
	}
}
