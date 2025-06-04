package com.example.du.deepPart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Criteria;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.example.du.utils.ExcelUtil;

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
 * LocalTimeControl extends BaseTimeControl 
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */

public class LocalTimeControl extends AbstractTimeControl{
	private static LocalTimeControl instance=null;
	public static LocalTimeControl getInstance(){
		if(instance==null)
			instance=new LocalTimeControl();
		return instance;
	}
	
	private LocalTimeControl(){}
	
	@Override
	public void updateData() {
		getCurrentTime();
		getBirthday();
		getLongitude();
		getLatitude();
		getAltitude();
		getUltraviolet();
		getSunRiseTime();
		getSunSetTime();
		getInfoText() ;
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
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5,
				paintCircle);

		
		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 60; i++) {
			if(i%5==0){
				paintDegree.setStrokeWidth(3);
				canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding * 2-15, paintDegree);}
			else{
				paintDegree.setStrokeWidth(2);
				canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding*2-40, paintDegree);
			}
			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}

		
		/* 画指针 */
		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(5);
		paintPointer.setAntiAlias(true);
		
		float hri=(float)calculateAngle(sunRiseTime,true);
		float mri=(float)calculateAngle(sunRiseTime,false);
		float hse=(float)calculateAngle(sunSetTime,true);
		float mse=(float)calculateAngle(sunSetTime,false);
		float pur=ultraviolet*30;
		paintPointer.setColor(Color.GREEN);
		//日出分钟
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(mri,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//日出小时
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(hri,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		paintPointer.setColor(Color.RED);
		//日落分钟
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(mse,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//日落小时
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(hse,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		paintPointer.setColor(Color.DKGRAY);
		//紫外线强度
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(pur,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		
		// 画文字
		canvas.translate(-clockWidth/2 , -clockHeight/2);
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		paintText.setAntiAlias(true);
		String str1 = "地", str2 = "时";
		canvas.drawText(
			str1,
			clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
			clockHeight / 2, paintText);
		canvas.drawText(
			str2,
			clockWidth / 2 + clockPadding - paintText.measureText(str2) / 2,
			clockHeight / 2, paintText);

		paintText.setTextSize((float)(10));
		paintText.setAntiAlias(true);
		

		for (int i = 0; i < 12; i++) {

			String str21 = Constants.CLOCK_HOUR_STRINGS[i];

			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str21, clockWidth / 2 - offset2, clockPadding
				* 2, paintText);

			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}

		canvas.translate(clockWidth / 2, clockHeight / 2);
		//这里需要解耦 添加get函数，把数据放到get函数里
		String str3 = "经纬：" + longitude + "°E" + " " + latitude + "°N";
		String str4 = "海拔 ：" + altitude + "m";
		String str5 = "气压：" +  "1008hPa";
		String str6 = "湿度：" +  "50%";
		String str7 = "紫外线：" +ultraviolet+  "级";


		float offset = paintText.descent() - paintText.ascent();
		canvas.drawText(str3,
			-paintText.measureText(str3)
			/ 2, -2 * offset, paintText);
		canvas.drawText(str4,
			-paintText.measureText(str4)
			/ 2, -3 * offset, paintText);
		canvas.drawText(str5,
			-paintText.measureText(str5)
			/ 2, 2 * offset, paintText);
		canvas.drawText(str6,
			-paintText.measureText(str6)
			/ 2, 3 * offset, paintText);
		canvas.drawText(str7,
			-paintText.measureText(str7)
			/ 2, 4 * offset, paintText);

		canvas.restore();
		
		return bm;
	}
	
	private String longitude;//经度
	private String latitude;//纬度
	private double altitude;//海拔
	private Date localDate;//本地时间
	private int ultraviolet;//紫外线强度
	private String sunRiseTime;//日出时间
	private String sunSetTime;//日落时间
    private long birthTime;

    /**
   	 * 生日 得有初始值
   	 */
    private Date birthday;

	public String getInfoText() {
		String str =
			"地时信息"
			+ "\r\n" + "【经纬度】" + longitude + "°E" + " " + latitude + "°N"
			+ "\r\n" + "【海拔】" + altitude + "m"
			+ "\r\n" + "【气压】" +"1008hPa"
			+ "\r\n" + "【湿度】" + "50%"
			+ "\r\n" + "【紫外线强度】" + ultraviolet +"级"
			+ "\r\n" + "【日出时间】" + sunRiseTime
			+ "\r\n" + "【日落时间】" + sunSetTime
			;
		SetTestText.addText("地时表获取infoText");
		return str;
	}


	
	public String getLongitude() {
		longitude="117.2E";	
		SetTestText.addText("获取经度");
		return longitude;
	}

	public String getLatitude() {
		latitude="39.1N";
		SetTestText.addText("获取纬度");
		return latitude;
	}

	public double getAltitude() {
		altitude=3.3;
		SetTestText.addText("获取海拔");
		return altitude;
	}

	public Date getLocalDate() {
		localDate = new Date(System.currentTimeMillis());
		SetTestText.addText("获取日期");
		return localDate;
	}

	public String getSunRiseTime() {
		BigDecimal x = new BigDecimal(117.3574);
		BigDecimal y = new BigDecimal(39.0888);
		Date sunTime = new Date(System.currentTimeMillis());
		sunRiseTime = SunSet.getSunrise(x, y, sunTime);
		SetTestText.addText("获取日出时间");
		return sunRiseTime;
	}
	public int getUltraviolet(){
		ultraviolet=2;
		return ultraviolet;
	}
	
	public String getSunSetTime() {
		BigDecimal x = new BigDecimal(117.3574);
		BigDecimal y = new BigDecimal(39.0888);
		Date sunTime = new Date(System.currentTimeMillis());
		sunSetTime = SunSet.getSunset(x, y, sunTime);
		SetTestText.addText("获取日落时间");
		return sunSetTime;
	}
	
	public Calendar getCurrentTime() {
		Calendar currentTime=Calendar.getInstance();
		return currentTime;
	}
	
    public Date getBirthday() {
    	Date date = new Date(birthTime);
    	birthday = date;
    	return birthday;
    }

 public static double calculateAngle(String time,boolean s) {
        String[] splitTime = time.split(":");
        int hour = Integer.parseInt(splitTime[0]);
        int minute = Integer.parseInt(splitTime[1]);
        double hourAngle = (hour % 12 + minute / 60.0) * 30;
        double minuteAngle = minute * 6;
        if(s)return hourAngle;
        else return minuteAngle;
    }
 
}