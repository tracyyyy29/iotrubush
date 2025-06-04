package com.example.du.deepPart;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.example.du15.R;
import com.example.du.utils.ExcelUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


import java.util.GregorianCalendar;
//import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
//import com.luckycatlabs.sunrisesunset.dto.Location;

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
		getLongitude();
		getLatitude();
		getAltitude();
		getBarometric();
		getHumidity();
		getWind();
		getLocalDate();
		getLocalGanZhiStr();
		getLocalGanZhi();
		getSunRiseTime();
		getSunSetTime();
		getSunRiseTimeToDraw();
		getSunSetTimeToDraw();
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
		
		//画底图
		Bitmap baguaImage =  BitmapFactory.decodeResource(context.getResources(),R.drawable.earth2);
		int baguaImageWidth = baguaImage.getWidth();
		int baguaImageHeight = baguaImage.getHeight();
		//计算缩放比例
		int newWidth,newHeight;
		float scale = (2*(clockWidth / 2- clockPadding)-10)/baguaImageWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBaguaImage = Bitmap.createBitmap(baguaImage,0,0,baguaImageWidth,baguaImageHeight,matrix,true);

		canvas.drawBitmap(newBaguaImage,clockWidth/2-clockWidth / 2+ clockPadding+5,
				clockWidth/2-clockWidth /2+ clockPadding+5,paintCircle);

		
		
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
			if (i % 5 == 0) {
				paintDegree.setStrokeWidth(3);
				canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
						(float) (clockPadding * 1.5), paintDegree);
			} else {
				paintDegree.setStrokeWidth(2);
				canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
						(float) (clockPadding * 1.25), paintDegree);
			}
			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(3);
		paintPointer.setAntiAlias(true);
		//日出时间指针
		int rise_hour = sunRiseTime_todraw.getHours();
		int rise_minute = sunRiseTime_todraw.getMinutes();
		int rise_second = sunRiseTime_todraw.getSeconds();
		float degree_rise_minute,degree_rise_hour;
		degree_rise_minute = 6*rise_minute+rise_second/10;
		degree_rise_hour = (rise_hour%12)*30+rise_minute/2;
		paintPointer.setColor(Color.YELLOW);
		//分钟
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_rise_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//小时
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_rise_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		//日落时间指针
		int set_hour = sunSetTime_todraw.getHours();
		int set_minute = sunSetTime_todraw.getMinutes();
		int set_second = sunSetTime_todraw.getSeconds();
		float degree_set_minute,degree_set_hour;
		degree_set_minute = 6*set_minute+set_second/10;
		degree_set_hour = (set_hour%12)*30+set_minute/2;
		paintPointer.setColor(Color.RED);
		//分钟
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_set_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//小时
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_set_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		
		//干支指针
		float degree_ganzhi = localGanZhi*6;
		paintPointer.setColor(Color.argb(100, 225, 165, 0));
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_ganzhi, 0, 0);
		canvas.drawLine(0, 0, 0,
				(float) (clockPadding * 0.4 - clockHeight / 2),
				paintPointer);	
		
		canvas.restore();

		// 画文字
		canvas.translate(-clockWidth / 2, -clockHeight / 2);
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
		
		paintText.setTextSize((float) (10));
		paintText.setAntiAlias(true);
		for (int i = 0; i < 60; i++) {
			String str21 = Constants.SIXTY_GANZHI_STRING[i];

			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str21, clockWidth / 2 - offset2, clockPadding
					- offset2 / 2, paintText);

			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}
		
		for (int i = 0; i < 12; i++) {

			String str21 = Constants.CLOCK_HOUR_STRINGS[i];

			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str21, clockWidth / 2 - offset2, clockPadding
					*2, paintText);

			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		
		canvas.translate(clockWidth/2, clockHeight/2);
		//这里需要解耦 添加get函数，把数据放到get函数里
		String str3 = "经纬："+longitude+"°E"+" "+latitude+"°N";
		String str4 = "海拔 ："+altitude+"m";
		String str5 = "气压："+barometric+"hPa";
		String str6 = "湿度："+humidity+"%";
		String str7 = "风力："+wind+"级";
		String str8 = "干支："+localGanZhiStr;
		
		float offset = paintText.descent() - paintText.ascent();
		canvas.drawText(str3,
				- paintText.measureText(str3)
						/ 2, -2*offset, paintText);
		canvas.drawText(str4,
				- paintText.measureText(str4)
						/ 2, -3*offset, paintText);
		canvas.drawText(str5,
				- paintText.measureText(str5)
						/ 2, 2*offset, paintText);
		canvas.drawText(str6,
				- paintText.measureText(str6)
						/ 2, 3*offset, paintText);
		canvas.drawText(str7,
				- paintText.measureText(str7)
						/ 2, 4*offset, paintText);
		canvas.drawText(str8,
				- paintText.measureText(str8)
						/ 2, 5*offset, paintText);
		
		
		canvas.restore();

		return bm;
	}
	
	/**
	 * 经度
	 */
	private double longitude;
	private BigDecimal longitude1;
	/**
	 * 纬度
	 */
	private double latitude;
	private BigDecimal latitude1;
	/**
	 * 海拔
	 */
	private double altitude;
	/**
	 * 气压
	 */
	private double barometric;
	/**
	 * 本地时间
	 */
	private Date localDate;
	/**
	 * 本地干支 这是一个索引序号 所以要记得get函数读取表格之后转换成索引
	 */
	private int localGanZhi;
	/**
	 * 本地干支 是年月日的干支 中间有空格
	 */
	private String localGanZhiStr;
	/**
	 * 日出时间 是字符串 不能用于画指针
	 */
	private String sunRiseTime;
	/**
	 * 日落时间 是字符串 不能用于画指针
	 */
	private String sunSetTime;
	/**
	 * 日出时间 用于画指针
	 */
	private Date sunRiseTime_todraw;
	/**
	 * 日落时间 用于画指针
	 */
	private Date sunSetTime_todraw;
	/**
	 * 湿度
	 */
	private double humidity;
	/**
	 * 风力
	 */
	private int wind;
	
	public double getLongitude() {
		longitude = 117.3574;
		longitude1 =new BigDecimal(117.3574);
		SetTestText.addText("地时表获取longitude");
		return longitude;
	}

	public double getLatitude() {
		latitude = 39.0888;
		latitude1 = new BigDecimal(39.0888);
		SetTestText.addText("地时表获取latitude");
		return latitude;
	}

	public double getAltitude() {
		altitude = 3.5;
		SetTestText.addText("地时表获取altitude");
		return altitude;
	}
	
	public double getBarometric()
	{
		barometric = 1022;
		SetTestText.addText("地时表获取barometric");
		return barometric;
	}
	
	public double getHumidity()
	{
		humidity = 61;
		SetTestText.addText("地时表获取humidity");
		return humidity;
	}
	
	public int getWind()
	{
		wind = 2;
		SetTestText.addText("地时表获取wind");
		return wind;
	}

	public Date getLocalDate() {
		localDate=new Date(System.currentTimeMillis());
		SetTestText.addText("地时表获取localdate");
		return localDate;
	}

	public int getLocalGanZhi() {
		//指针表示的是当日干支 所以获取最后两个字符
		String lastTwoChars = getLocalGanZhiStr().substring(6, 8);
		localGanZhi = Arrays.asList(Constants.SIXTY_GANZHI_STRING).indexOf(lastTwoChars);
		SetTestText.addText("地时表获取localGanZhi");
		return localGanZhi;
	}
	
	public String getLocalGanZhiStr()
	{
		Calendar currentTime=Calendar.getInstance();
		localGanZhiStr = ExcelUtil.lookUp05(context,currentTime);
		SetTestText.addText("地时表获取localGanZhiStr");
		return localGanZhiStr;
	}

	//日出日落时间获取 返回的是字符串 几时几分
	public String getSunRiseTime() {
		
		sunRiseTime = SunRiseSet.getSunrise(longitude1, latitude1, localDate);
		SetTestText.addText("地时表获取sunRiseTime");
		return sunRiseTime;
	}

	public String getSunSetTime() {
		
		sunSetTime = SunRiseSet.getSunset(longitude1, latitude1, localDate);
		SetTestText.addText("地时表获取sunSetTime");
		return sunSetTime;
	}
	
	public Date getSunRiseTimeToDraw() {
		sunRiseTime_todraw = new Date(System.currentTimeMillis());
		String[] rq = sunRiseTime.split(":");
		int hour = Integer.parseInt(rq[0]);
		int minute = Integer.parseInt(rq[1]);
		sunRiseTime_todraw.setHours(hour);
		sunRiseTime_todraw.setMinutes(minute);
		sunRiseTime_todraw.setSeconds(0);
		SetTestText.addText("地时表获取sunRiseTime_todraw");
		return sunRiseTime_todraw;
	}

	public Date getSunSetTimeToDraw() {
		sunSetTime_todraw = new Date(System.currentTimeMillis());
	
		
		
		String[] rq = sunSetTime.split(":");
		int hour = Integer.parseInt(rq[0]);
		int minute = Integer.parseInt(rq[1]);
		sunSetTime_todraw.setHours(hour);
		sunSetTime_todraw.setMinutes(minute);
		sunSetTime_todraw.setSeconds(0);
		SetTestText.addText("地时表获取sunSetTime_todraw");
		return sunSetTime_todraw;
	}
	
	public String getInfoText() {
		String str=
			    "地时信息"
			    		+"\r\n"+"【经纬度】"+longitude+"°E"+" "+latitude+"°N"
						+"\r\n"+"【海拔】"+altitude+"m"
						+"\r\n"+"【气压】"+barometric+"hPa"
						+"\r\n"+"【湿度】"+humidity+"%"
						+"\r\n"+"【风力】"+wind+"级"
						+"\r\n"+"【当前干支】"+localGanZhiStr
						+"\r\n"+"【日出时间】"+sunRiseTime
						+"\r\n"+"【日落时间】"+sunSetTime
						;
		SetTestText.addText("地时表获取infoText");
		return str;
	}

}
