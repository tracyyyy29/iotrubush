package com.example.du.deepPart;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.example.du15.R;
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
 * SunTimeControl extends BaseTimeControl 
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class SunTimeControl extends AbstractTimeControl {
	private static SunTimeControl instance=null;
	public static SunTimeControl getInstance(){
		if(instance==null)
			instance=new SunTimeControl();
		return instance;
	}
	
	private SunTimeControl(){
		
	}
	@Override
	public void updateData() {
		getCurrentDate();
		getLunarDate();
		getSunStandardDate();
		getSunRealDate();
		getSolarTermStr();
		getSolarTerm();
		getCurrentTime();
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
		Bitmap baguaImage =  BitmapFactory.decodeResource(context.getResources(),R.drawable.sun1);
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
		paintPointer.setAntiAlias(true);
		
		//本地时间指针
		int hour = currentDate.getHours();
		int minute = currentDate.getMinutes();
		int second = currentDate.getSeconds();
		float degree_minute,degree_hour;
		degree_minute = 6*minute+second/10;
		degree_hour = (hour%12)*30+minute/2;
		//本地时间分钟
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//本地时间小时
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		//太阳标准时指针
		int standardsunhour = sunStandardDate.getHours();
		int standardsunminute = sunStandardDate.getMinutes();
		int standardsunsecond = sunStandardDate.getSeconds();
		float degree_standardsun_minute,degree_standardsun_hour;
		degree_standardsun_minute = 6*standardsunminute+standardsunsecond/10;
		degree_standardsun_hour = (standardsunhour%12)*30+standardsunminute/2;
		paintPointer.setColor(Color.YELLOW);
		//太阳标准时分钟
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_standardsun_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//太阳标准时小时
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_standardsun_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		//太阳真时指针
		int realsunhour = sunRealDate.getHours();
		int realsunminute = sunRealDate.getMinutes();
		int realsunsecond = sunRealDate.getSeconds();
		float degree_realsun_minute,degree_realsun_hour;
		degree_realsun_minute = 6*realsunminute+realsunsecond/10;
		degree_realsun_hour = (realsunhour%12)*30+realsunminute/2;
		paintPointer.setColor(Color.RED);
		//太阳真时分钟
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_realsun_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//太阳真时小时
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_realsun_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		//节气指针
		float degree_term = 15*getSolarTerm();
		if(solarTermFlag==0)
		{
			degree_term+=7.5;
		}
		paintPointer.setColor(Color.GRAY);
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_term, 0, 0);
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
		String str1 = "天", str2 = "时";
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
		for (int i = 0; i < 24; i++) {
			if (i % 2 == 1) {
				String str = Constants.SOLAR_TERM_STRING[i];
				float offset = paintText.measureText(str) / 2;
				canvas.drawText(str, clockWidth / 2 - offset, clockPadding
						- offset / 2, paintText);
			} else {
				String str11 = Constants.SOLAR_TERM_STRING[i];
				String str21 = Constants.CLOCK_HOUR_STRINGS[i / 2];
				float offset1 = paintText.measureText(str11) / 2;
				float offset2 = paintText.measureText(str21) / 2;
				canvas.drawText(str11, clockWidth / 2 - offset1, clockPadding
						- offset1 / 2, paintText);
				canvas.drawText(str21, clockWidth / 2 - offset2,
						clockPadding*2, paintText);
			}
			canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		}
		
		
		canvas.translate(clockWidth / 2, clockHeight / 2);
		float offset = paintText.descent() - paintText.ascent();
		
		canvas.drawText("本地时间"+currentDate,
				- paintText.measureText("当地时间"+currentDate)
						/ 2, -2*offset, paintText);
		canvas.drawText("太阳真时"+sunRealDate,
				- paintText.measureText("太阳真时"+sunRealDate)
						/ 2, -3*offset, paintText);
		canvas.drawText("太阳标准时"+sunStandardDate,
				- paintText.measureText("太阳标准时"+sunStandardDate)
						/ 2, -4*offset, paintText);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		canvas.drawText("农历："+formatter.format(lunarDate),
						- paintText.measureText("农历："+formatter.format(lunarDate))
						/ 2, 2*offset, paintText);
//		canvas.drawText("公历："+currentTime.get(Calendar.YEAR)+"年"
//						+(currentTime.get(Calendar.MONTH)+1)+"月"
//						+currentTime.get(Calendar.DAY_OF_MONTH)+"日",
//						- paintText.measureText("公历："+currentTime.get(Calendar.YEAR)+"年"
//						+(currentTime.get(Calendar.MONTH)+1)+"月"
//						+currentTime.get(Calendar.DAY_OF_MONTH)+"日")
//						/ 2, 3*offset, paintText);
		canvas.drawText("公历："+formatter.format(currentDate),
				- paintText.measureText("公历："+formatter.format(currentDate))
				/ 2, 3*offset, paintText);
		canvas.restore();

		return bm;
	}

	/**
	 * 当前日期
	 */
	private Date currentDate;
    /**
	 * 当前日期 日历格式 获取year month day的方法没淘汰
	 */
	private Calendar currentTime;
    /**
     * 农历
     */
	private Date lunarDate;
    /**
     * 太阳标准时
     */
	private Date sunStandardDate;
    /**
     * 太阳真时
     */
	private Date sunRealDate;
    /**
     * 当前季节
     */
    private int season=-1;
    /**
     * 当前节气
     */
    private int solarTerm=-1;
    /**
     * 节气标志（为零的时候表示在区间，为一的时候表示正好是某个节气）
     */
    private int solarTermFlag = -1;
    
	public Date getCurrentDate() {
		currentDate=new Date(System.currentTimeMillis());
		SetTestText.addText("天时表获取currentDate");
		return currentDate;
	}
	
	public Calendar getCurrentTime() {
		currentTime=Calendar.getInstance();
		SetTestText.addText("天时表获取currentTime");
		return currentTime;
	}

	public Date getLunarDate(){
		lunarDate=ExcelUtil.lookUp01(context,getCurrentDate(),ExcelUtil.GONG_LI);
		SetTestText.addText("天时表获取LunarDate");
		return lunarDate; 
	}
	
	public Date getSunStandardDate() {
		Calendar result = Calendar.getInstance();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		//目前在天津地区 太阳标准时等于北京时间减少两秒
		second -= 2;
		result.set(year, month, day, hour, minute, second);
		sunStandardDate = result.getTime();
		SetTestText.addText("天时表获取SunStandardDate");
		return sunStandardDate;
	}


	public Date getSunRealDate() {
		//sunRealDate=getCurrentDate();
		sunRealDate=ExcelUtil.lookUp02(context,getCurrentDate());
		SetTestText.addText("天时表获取SunRealDate");
		return sunRealDate;
	}


	public int getSeason() {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		float f=(float) (month+0.01*day);
        if (f >= Constants.CHUN_FEN && f < Constants.XIA_ZHI) {
            season = 0;
        } else if (f >= Constants.XIA_ZHI && f< Constants.QIU_FEN) {
            season = 1;
        } else if (f >= Constants.QIU_FEN && f< Constants.DONG_ZHI) {
            season = 2;
        } else {
            season = 3;
        }
        SetTestText.addText("天时表获取Season");
		return season;
	}
	
	public String getSeasonStr(){
		SetTestText.addText("天时表获取SeasonStr");
		return Constants.SEASON_STRING[getSeason()];
	}

	//获取节气索引（int） 需要分情况讨论 恰好是节气和其他情况 0 1 0直接用index 1需要在index的基础上偏移半个格子
	public int getSolarTerm() {
		//需要获取节气字符串的前两位（中文节气）
		String firstTwoChars = getSolarTermStr().substring(0, 2);
		SetTestText.addText("天时表获取SolarTerm");
		return Arrays.asList(Constants.SOLAR_TERM_STRING).indexOf(firstTwoChars);
	}
	
	//获取节气字符串（可能是单个节气 也可能是节气区间
	public String getSolarTermStr(){
		String solarTermStr=ExcelUtil.lookUp04(context,getCurrentDate());
		//中文字符也占一位
		if(solarTermStr.length()>2)
		{
			solarTermFlag = 0;
		}
		else
		{
			solarTermFlag = 1;
		}
		SetTestText.addText("天时表获取SolarTermStr");
		return solarTermStr;
	}

	public String getInfoText() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String str=
			    "天时信息"
						+"\r\n"+"【当地时间】    "+currentDate
						+"\r\n"+"【太阳真时】    "+sunRealDate
						+"\r\n"+"【太阳标准时】"+sunStandardDate
						+"\r\n"+"【公历】"+formatter.format(currentDate)
						+"\r\n"+"【农历】"+formatter.format(lunarDate)
						+"\r\n"+"【当前节气】"+getSolarTermStr();
//		int number = ExcelUtil.test(context);
//		String str = String.format("行数是%d",number);
		SetTestText.addText("天时表获取InfoText");
		return str;
	}
	
}

