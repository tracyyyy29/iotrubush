package com.example.du.deepPart;

import com.example.du.deepPart.timeData.TimeInfo;
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
 * AcupointTimeControl extends BaseTimeControl 
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class AcupointTimeControl extends AbstractTimeControl{
	private static AcupointTimeControl instance=null;
	private  Context context=null;

	public static AcupointTimeControl getInstance(Context context1){
		if(instance==null)
			instance=new AcupointTimeControl(context1);
		
		return instance;
	}
	
	private AcupointTimeControl(Context context1){
		this.context=context1;
	}

	@Override
	public Bitmap drawClock() {
		float clockWidth = canvasWidth;
		float clockHeight = canvasHeight;
		float clockPadding = canvasPadding;


		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);
		Canvas canvas = new Canvas(bm);
		Paint paintCircle = new Paint(); 
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setStrokeWidth(5); 
		paintCircle.setAntiAlias(true); 
		
		
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.pic1);
		Matrix matrix = new Matrix();
		matrix.postScale((clockWidth - clockPadding * 2) / bmp.getWidth(), (clockHeight - clockPadding * 2) / bmp.getHeight());
        
        Bitmap dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);
        canvas.drawBitmap(dstbmp, (int) clockPadding, (int) clockPadding, null);
		

		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);

		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5, paintCircle);



		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);

		Paint paintPointer = new Paint();
		paintPointer.setColor(Color.RED);
		paintPointer.setStrokeWidth(4);
		paintPointer.setAntiAlias(true);

		 canvas.drawLine(0, 0, getStopx((clockWidth-4 * clockPadding) / 2), getStopY((clockWidth-4 * clockPadding) / 2),
	                paintPointer);
		canvas.restore();



		return bm;
	}

	@Override
	public void updateData() {
		
	}
	
    private float getStopx(float r) {
        int mins = DateStemBranchUtils.mins();
        int hous =DateStemBranchUtils.hous();

        int du = 360 / 24 * hous + (360 / (24 * 60)) * mins;
        float v = (float) (Math.sin(du *Math.PI / 180) * r);
        return v;
    }

    private float getStopY(float r) {
        int mins = DateStemBranchUtils.mins();
        int hous =DateStemBranchUtils.hous();

        int du = 360 / 24 * hous + (360 / (24 * 60)) * mins+180;
        float v = (float) (Math.cos(du *Math.PI / 180) * r);
        return v;
    }
    
    
    
    public String getInfoText() {
		
    	TimeInfo info =timeData.getInfo(DateStemBranchUtils.calHourStemBranch());
	 	String str = "穴位时信息\n【当前时辰】"+info.getTime()+
				"\n【对应穴位】"+info.getAcupoin()+
				"\n【对应位置】"+info.getLocation()+
				"\n【功能作用】"+info.getRole();
	 	SetTestText.addText("获取时辰穴位");
		return str;
	}
    
    
}
