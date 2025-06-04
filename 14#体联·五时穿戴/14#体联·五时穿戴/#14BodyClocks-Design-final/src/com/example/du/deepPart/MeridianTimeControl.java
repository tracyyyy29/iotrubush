package com.example.du.deepPart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.du14.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

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
	private  Context context=null;
	public static MeridianTimeControl getInstance(Context context1) {
		if (instance == null)
			instance = new MeridianTimeControl(context1);
		return instance;
	}

	private MeridianTimeControl(Context context1) {
		this.context=context1;
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
		paintCircle.setStrokeWidth(5); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		
		
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.pic2);
		Matrix matrix = new Matrix();
		// 缩小一倍
		matrix.postScale((clockWidth - clockPadding * 2) / bmp.getWidth(), (clockHeight - clockPadding * 2) / bmp.getHeight());
        Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);
        canvas.drawBitmap(dstbmp, (int) clockPadding, (int) clockPadding, null);
		
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5, paintCircle);

		// 画刻度
//		Paint paintDegree = new Paint();
//		paintDegree.setAntiAlias(true);
//		for (int i = 0; i < 6; i++) {
//			paintDegree.setStrokeWidth(5);
//			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
//					clockPadding * 2, paintDegree);
//			canvas.rotate(60, clockWidth / 2, clockHeight / 2);
//		}

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setColor(Color.RED);
		paintPointer.setStrokeWidth(4);
		paintPointer.setAntiAlias(true);
//		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
//				paintPointer);
		 canvas.drawLine(0, 0, getStopx((clockWidth-4 * clockPadding) / 2), getStopY((clockWidth-4 * clockPadding) / 2),
	                paintPointer);
		canvas.restore();

		// 画文字
		canvas.save();
//		Paint paintText = new Paint();
//		paintText.setTextSize(20);
//		paintText.setAntiAlias(true);
//		String str1 = "经", str2 = "络", str3 = "时";
//		canvas.drawText(
//				str1,
//				clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
//				clockHeight / 2, paintText);
//		canvas.drawText(str2, clockWidth / 2 - paintText.measureText(str2) / 2,
//				clockHeight / 2 - clockPadding, paintText);
//		canvas.drawText(
//				str3,
//				clockWidth / 2 + clockPadding - paintText.measureText(str3) / 2,
//				clockHeight / 2, paintText);
//		canvas.restore();

		return bm;
	}

	@Override
	public void updateData() {

	}
	 private float getStopx(float r) {
	        int mins = DateStemBranchUtils.mins();
	        int hous =DateStemBranchUtils.hous();
	        //搴︽暟
	        int du = 360 / 24 * hous + (360 / (24 * 60)) * mins;
	        float v = (float) (Math.sin(du *Math.PI / 180) * r);
	        return v;
	 }

	 private float getStopY(float r) {
	        int mins = DateStemBranchUtils.mins();
	        int hous =DateStemBranchUtils.hous();
	        //搴︽暟
	        int du = 360 / 24 * hous + (360 / (24 * 60)) * mins+180;
	        float v = (float) (Math.cos(du *Math.PI / 180) * r);
	        return v;
	 }
	 
	 public String getInfoText() {
			
		 	com.example.du.deepPart.jlData.TimeInfo info =jlData.getInfo(DateStemBranchUtils.calHourStemBranch());
		 	String str = "穴位时信息\n【当前时辰】"+info.getTime()+
					"\n【对应经络】"+info.getAcupoin()+
					"\n【其他内容】"+info.getRole();
		 	SetTestText.addText("获取时辰经络");
			return str;
		}
	    
	  
}
