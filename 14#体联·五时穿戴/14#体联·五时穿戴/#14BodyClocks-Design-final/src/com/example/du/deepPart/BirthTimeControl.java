package com.example.du.deepPart;

import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * BirthTimeControl extends BaseTimeControl 
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class BirthTimeControl extends AbstractTimeControl{
	private static BirthTimeControl instance=null;
	public static BirthTimeControl getInstance(){
		if(instance==null)
			instance=new BirthTimeControl();
		return instance;
	}
	private BirthTimeControl(){
		
	}

	@Override
	public Bitmap drawClock() {
		float clockWidth=canvasWidth;
		float clockHeight=canvasHeight;
		float clockPadding=canvasPadding;
		
		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(3); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		//画内圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding*2, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5,
				paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 12; i++) {
			paintDegree.setStrokeWidth(2);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding +15, paintDegree);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		for (int i = 0; i < 60; i++) {
			paintDegree.setStrokeWidth(1);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding +8, paintDegree);
			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}
		
		canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		for (int i = 0; i < 12; i++) {
			paintDegree.setStrokeWidth(2);
			canvas.drawLine(clockWidth / 2, clockPadding*2-5, clockWidth / 2,
					clockPadding *2+5, paintDegree);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		canvas.rotate(-15, clockWidth / 2, clockHeight / 2);
		// 画文字
		int hour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
		int minute = getCurrentTime().get(Calendar.MINUTE);
		int second = getCurrentTime().get(Calendar.SECOND);
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		paintText.setAntiAlias(true);
		String str1 = "子  午", str2 = "流  注",str5=Constants.BIRTH_HOUR_STRINGS[((hour+1)%24)/2];
		canvas.drawText(
				str1,
				clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
				clockHeight / 2, paintText);
		canvas.drawText(
				str2,
				clockWidth / 2 + clockPadding - paintText.measureText(str2) / 2,
				clockHeight / 2, paintText);
		paintText.setTextSize(30);
		canvas.drawText(
				str5,
				clockWidth / 2 - paintText.measureText(str5) / 2,
				clockHeight / 2+30, paintText);
		paintText.setTextSize(10);
		for (int i = 0; i < 60; i++) {
			String str3 = Constants.CLOCK_MINUTE_STRINGS[i];
			float offset2 = paintText.measureText(str3) / 2;
			canvas.drawText(str3, clockWidth / 2 - offset2, (float)(clockPadding
					*1.7), paintText);
			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}
		paintText.setTextSize(20);
		for (int i = 0; i < 12; i++) {
			String str4 = Constants.TWELVE_DIZHI_STRING[i];
			float offset3 = paintText.measureText(str4) / 2;
			canvas.drawText(str4, clockWidth / 2 - offset3, clockPadding
					*2+30, paintText);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		canvas.restore();

		/* 画指针 */
		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(5);
		paintPointer.setAntiAlias(true);
		//本地时间指针
				float degree_minute,degree_hour;
				degree_minute = 6*minute+second/10;
				degree_hour = (hour)*15+minute/4;
				//degree_second=6*second;
				//本地时间分钟
				paintPointer.setStrokeWidth(2);
				canvas.save();
				canvas.rotate(degree_minute,0,0);
				canvas.drawLine(0, 0, 0, -clockHeight/2+3*clockPadding, paintPointer);
				canvas.restore();
				//本地时间小时
				paintPointer.setStrokeWidth(3);
				canvas.save();
				canvas.rotate(degree_hour,0,0);
				canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
				canvas.restore();
				
		return bm;
	}
	public LocationManager lm;
	public Location lc;
	//private Calendar currentTime;
	 /**
		 * 生日时间，是直接由生日设置界面传过来的
		 */
    private long birthTime;
	    /**
	   	 * 生日 得有初始值
	   	 */
    private Date birthday;
    private int hourIndex;
	@Override
	public void updateData() {
		getCurrentTime();
		getBirthday();
	}
	
	public Calendar getCurrentTime() {
		Calendar currentTime=Calendar.getInstance();
		SetTestText.addText("获取时间");
		return currentTime;
	}
	public String getTimeStr()
	{
		int hour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
		int minute = getCurrentTime().get(Calendar.MINUTE);
		int second = getCurrentTime().get(Calendar.SECOND);
		String str=hour+":"+minute+":"+second;
		return str;
	}
	public String getMeridianStr()
	{
		int hour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
		String str=Constants.MERIDIAN_STRINGS[(hour+1)%24/2];
		SetTestText.addText("获取子午时");
		return str;
	}
	public String getShouldDoStr()
	{ 
		int hour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
		String str=Constants.SHOULDDO_STRINGS[(hour+1)%24/2];
		return str;
	}
	 public long getBirthTime(long birthtime){
	    	birthTime = birthtime;
	    	return birthTime;
	    }
	    
	    public int getHourIndex(int hourindex)
	    {
	    	hourIndex = hourindex;
	    	return hourIndex;
	    }

    public Date getBirthday()
    {
    	Date date = new Date(birthTime);
    	birthday = date;
    	SetTestText.addText("获取生日");
    	return birthday;
    }
	public void setGPS(LocationManager location)
	{
		lm=location;
		lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    updateShow(lc);
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, new LocationListener() {
	     @Override
	     public void onLocationChanged(Location location) {
	         // 当GPS定位信息发生改变时，更新定位
	         updateShow(location);
	     }

	     @Override
	     public void onStatusChanged(String provider, int status, Bundle extras) {

	     }

	     @Override
	     public void onProviderEnabled(String provider) {
	         // 当GPS LocationProvider可用时，更新定位
	         updateShow(lm.getLastKnownLocation(provider));
	     }

	     @Override
	     public void onProviderDisabled(String provider) {
	         updateShow(null);
	     }
	 });
	}
	
    private String updateShow(Location location) {
        if (location != null) {
        	DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            StringBuilder sb = new StringBuilder();
            sb.append("当前的位置信息：\n");
            sb.append("精度：" + decimalFormat.format(location.getLongitude()) + "\n");
            sb.append("纬度：" + decimalFormat.format(location.getLatitude()) + "\n");
            sb.append("速度：" + location.getSpeed() + "\n");
            sb.append("方向：" + location.getBearing() + "\n");
            String str=sb.toString();
            SetTestText.addText("获取GPS定位");
            return str;
        }
        return null;
    }

    
	 public String getInfoText() {
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
	    	//GPS
			Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);   //免费
            criteria.setAltitudeRequired(true);  //能够提供高度信息
            criteria.setBearingRequired(true);   //能够提供方向信息
	    	String str=
					"人时信息"
							+"\r\n"+"【您的生日】"+formatter.format(birthday)+" "+Constants.LUNAR_HOUR_STRINGS[hourIndex]
							+"\r\n"+"【当前时间】"+getTimeStr()
							+"\r\n"+"【当前经脉】"+getMeridianStr()
							+"\r\n"+getShouldDoStr()
							+"\r\n"+updateShow(lc);
			return str;
		}
}
