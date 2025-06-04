package com.example.du.deepPart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.example.du.deepPart.AudioControl.Priority;
import com.example.du.utils.ExcelUtil;
import com.hanheng.a53.beep.BeepClass;

import android.R;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Environment;
import android.renderscript.Sampler.Value;
import android.util.Log;

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
		
		//久坐时间 扇形和指针
		float hour_degree = stayTime/10;
		
		//久坐时间扇形
		//canvas.translate(clockWidth / 2, clockHeight / 2);
		Paint paintArc = new Paint();
		paintArc.setColor(Color.LTGRAY);
		RectF oval = new RectF(-clockWidth/2+2*clockPadding,-clockWidth/2+2*clockPadding,
								clockWidth/2-2*clockPadding,clockWidth/2-2*clockPadding);
		canvas.drawArc(oval, -90, hour_degree, true, paintArc);
	
		//久坐时间指针
		paintPointer.setColor(Color.BLUE);
		canvas.rotate(hour_degree);
		paintPointer.setStrokeWidth(2);
		canvas.drawLine(0, 0, 0, -clockWidth/2+2*clockPadding,
				paintPointer);
		canvas.restore();
		
		//干支指针 需要在画完扇形之后盖上去
		canvas.translate(clockWidth / 2, clockHeight / 2);
		float ganzhi_degree = 6*birthGanZhi;
		paintPointer.setColor(Color.argb(100, 0, 100, 100));
		canvas.save();
		canvas.rotate(ganzhi_degree);
		canvas.drawLine(0, 0, 0,
				(float) (clockPadding * 0.4 - clockHeight / 2),
				paintPointer);
		canvas.restore();
		
		canvas.save();
		
		//画柱状图
		//canvas.translate(clockWidth/2, clockHeight/2);
		//画横线
		Paint paintImage = new Paint();
		paintImage.setStrokeWidth(2);
		paintImage.setAntiAlias(true);
		canvas.drawLine(-clockWidth/2+2*clockPadding, 0, 
				clockWidth/2-2*clockPadding, 0, paintImage);
		
		float heightHeartRate,heightBloodHighPressure,
		heightBloodLowPressure,heightBloodOxygen,heightBodyTemperature;
		float heightP,heightS,heightI;
		float width = (clockWidth-4*clockPadding)/10;
		float maxHeight = 3*(clockWidth/2-2*clockPadding)/5;
		
		//心率
		paintImage.setColor(Color.GREEN);
		heightHeartRate = maxHeight*heartRate/200;
		if(heartRate>=170 || heartRate<=60)
		{paintImage.setColor(Color.RED);}
		canvas.drawLine(-7*width/2, 0, -7*width/2, -heightHeartRate, paintImage);
		canvas.drawLine(-5*width/2, 0, -5*width/2, -heightHeartRate, paintImage);
		canvas.drawLine(-7*width/2, -heightHeartRate,-5*width/2, -heightHeartRate, paintImage);
		
		// 血压 
		paintImage.setColor(Color.GREEN);
		heightBloodHighPressure= maxHeight*highBloodPressure/150;
		heightBloodLowPressure = maxHeight*lowBloodPressure/150;
		if(highBloodPressure>=140 || lowBloodPressure>=90||highBloodPressure<=90 || lowBloodPressure<=60)
		{paintImage.setColor(Color.RED);}
		canvas.drawLine(-3*width/2, 0, -3*width/2, -heightBloodLowPressure, paintImage);
		canvas.drawLine(-2*width/2, 0, -2*width/2, -heightBloodLowPressure, paintImage);
		canvas.drawLine(-2*width/2, 0, -2*width/2, -heightBloodHighPressure, paintImage);
		canvas.drawLine(-1*width/2, 0, -1*width/2, -heightBloodHighPressure, paintImage);
		canvas.drawLine(-3*width/2, -heightBloodLowPressure,-2*width/2, -heightBloodLowPressure, paintImage);
		canvas.drawLine(-2*width/2, -heightBloodHighPressure,-1*width/2, -heightBloodHighPressure, paintImage);

		//血氧	
		paintImage.setColor(Color.GREEN);
		heightBloodOxygen= maxHeight*bloodOxygen/100;
		if(bloodOxygen<90)
		{paintImage.setColor(Color.RED);}
		canvas.drawLine(3*width/2, 0, 3*width/2, -heightBloodOxygen, paintImage);
		canvas.drawLine(1*width/2, 0, 1*width/2, -heightBloodOxygen, paintImage);
		canvas.drawLine(3*width/2, -heightBloodOxygen,1*width/2, -heightBloodOxygen, paintImage);
		
		// 体温
		paintImage.setColor(Color.GREEN);
		heightBodyTemperature= maxHeight*bodyTemperature/50;
		if(bodyTemperature<36||bodyTemperature>37)
		{paintImage.setColor(Color.RED);}
		canvas.drawLine(7*width/2, 0, 7*width/2, -heightBodyTemperature, paintImage);
		canvas.drawLine(5*width/2, 0, 5*width/2, -heightBodyTemperature, paintImage);
		canvas.drawLine(7*width/2, -heightBodyTemperature,5*width/2, -heightBodyTemperature, paintImage);
		
		
		paintImage.setColor(Color.argb(100, 100, 0, 100));
		//P
		heightP = (PStage+1)*maxHeight/4;
		canvas.drawLine(-5*width/2, 0, -5*width/2, heightP, paintImage);
		canvas.drawLine(-3*width/2, 0, -3*width/2, heightP, paintImage);
		canvas.drawLine(-5*width/2, heightP,-3*width/2, heightP, paintImage);
		
		//S
		heightS = (SStage+1)*maxHeight/4;
		canvas.drawLine(-width/2, 0, -width/2, heightS, paintImage);
		canvas.drawLine(width/2, 0, width/2, heightS, paintImage);
		canvas.drawLine(-width/2, heightS, width/2, heightS, paintImage);
		
		//I
		heightI = (IStage+1)*maxHeight/4;
		canvas.drawLine(3*width/2, 0, 3*width/2, heightI, paintImage);
		canvas.drawLine(5*width/2, 0, 5*width/2, heightI, paintImage);
		canvas.drawLine(3*width/2, heightI, 5*width/2, heightI, paintImage);
		
		
		
		// 画文字
		canvas.translate(-clockWidth/2, -clockHeight/2);
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		paintText.setAntiAlias(true);
		String str1 = "人", str2 = "时";
		canvas.drawText(
				str1,
				clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
				clockHeight / 2, paintText);
		canvas.drawText(
				str2,
				clockWidth / 2 + clockPadding - paintText.measureText(str2) / 2,
				clockHeight / 2, paintText);
		
		paintText.setTextSize(10);
		for (int i = 0; i < 60; i++) {
			String str21 = Constants.SIXTY_GANZHI_STRING[i];

			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str21, clockWidth / 2 - offset2, clockPadding
					- offset2 / 2, paintText);

			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}
		
		for (int i = 0; i < 60; i++) {

			String str21 = Constants.CLOCK_MINUTE_STRINGS[i];

			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str21, clockWidth / 2 - offset2, clockPadding
					*2, paintText);

			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}
		
		
		//柱状图文字
		canvas.translate(clockWidth/2, clockHeight/2);
		paintText.setTextSize(10);
		
		//说明文字
		String str3 = "心率";
		String str4 = "P";
		String str5 = "血压";
		String str6 = "S";
		String str7 = "血氧";
		String str8 = "I";
		String str9 = "体温";
		float offset3 = paintText.measureText(str3) / 2;
		float offset4 = paintText.measureText(str4) / 2;
		float offset = paintText.descent() - paintText.ascent();
		canvas.drawText(str3, -6*width/2 - offset3, offset, paintText);
		canvas.drawText(str4, -4*width/2 - offset4, offset, paintText);
		canvas.drawText(str5, -2*width/2 - offset3, offset, paintText);
		canvas.drawText(str6,            - offset4, offset, paintText);
		canvas.drawText(str7,  2*width/2 - offset3, offset, paintText);
		canvas.drawText(str8,  4*width/2 - offset4, offset, paintText);
		canvas.drawText(str9,  6*width/2 - offset3, offset, paintText);

		//数据
		String str10 = String.format("%.1f", heartRate);
		//String str11 = "P";
		String str12 = String.format("%.1f,%.1f", lowBloodPressure,highBloodPressure);
		//String str13 = "S";
		String str14 = String.format("%.1f", bloodOxygen);
		//String str15 = "I";
		String str16 = String.format("%.1f", bodyTemperature);
		float offset10 = paintText.measureText(str10) / 2;
		float offset11 = paintText.measureText(pstr) / 2;
		float offset12 = paintText.measureText(str12) / 2;
		float offset13 = paintText.measureText(sstr) / 2;
		float offset14 = paintText.measureText(str14) / 2;
		float offset15 = paintText.measureText(istr) / 2;
		float offset16 = paintText.measureText(str16) / 2;
		
		canvas.drawText(str10, -6*width/2 - offset10, 2*offset, paintText);
		canvas.drawText(pstr, -4*width/2 - offset11, 2*offset, paintText);
		canvas.drawText(str12, -2*width/2 - offset12, 2*offset, paintText);
		canvas.drawText(sstr,            - offset13, 2*offset, paintText);
		canvas.drawText(str14,  2*width/2 - offset14, 2*offset, paintText);
		canvas.drawText(istr,  4*width/2 - offset15, 2*offset, paintText);
		canvas.drawText(str16,  6*width/2 - offset16, 2*offset, paintText);

		
		
		
		canvas.restore();

		return bm;
	}

	@Override
	public void updateData() {
		getCurrentTime();
		getStayTime();

		getHeartRate();
		getHighBloodPressure();
		getLowBloodPressure();
		getBloodOxygen();
		getBodyTemperature();
		getTotalDay();
		getBirthday();
		getBirthGanZhiStr();
		getBirthGanZhi();
		getPStage();
		getSStage();
		getIStage();
	}
	/**
	 * 
	 */
	/**
	 * 久坐时间 int类型 范围0-3599秒
	 */
	private int stayTime;
//    /**
//	 * 人时对应的干支 也是一个索引 0-59
//	 */
//    private int birthGanZhi;
    /**
	 * 心率
	 */
    private float heartRate = 40;
    /**
	 * 血压 高压
	 */
    private float highBloodPressure = 120;
    /**
	 * 血压 低压
	 */
    private float lowBloodPressure= 80;
    /**
	 * 血氧
	 */
    private float bloodOxygen = 98;
    /**
	 * 体温
	 */
    private float bodyTemperature = 36;
    
    /**
	 * 体力 分为三档 低潮1 临界2 高潮3
	 */
    private int PStage;
    private String pstr;
    /**
	 * 智力
	 */
    private int SStage;
    private String sstr;
    /**
	 * 情绪
	 */
    private int IStage;
    private String istr;
    /**
	 * 当前日期 日历格式 获取year month day的方法没淘汰
	 */
    private Calendar currentTime;        
    /**
	 * 生日时间，是直接由生日设置界面传过来的
	 */
    private long birthTime;
    /**
   	 * 生日 得有初始值
   	 */
    private Date birthday;
    /**
   	 * 小时索引
   	 */
    private int hourIndex;
    /**
	 * 差值总天数
	 */
    private int total_day;
	private int birthGanZhi;
	private String birthGanZhiStr;
    
    public Calendar getCurrentTime() {
		currentTime=Calendar.getInstance();
		SetTestText.addText("人时表获取currentTime");
		return currentTime;
	}
    
    
    public long getBirthTime(long birthtime){
    	birthTime = birthtime;
    	SetTestText.addText("人时表获取birthTime");
    	return birthTime;
    }
    
    public int getHourIndex(int hourindex)
    {
    	hourIndex = hourindex;
    	SetTestText.addText("人时表获取hourIndex");
    	return hourIndex;
    }
    
    public Date getBirthday()
    {
    	Date date = new Date(birthTime);
    	birthday = date;
    	SetTestText.addText("人时表获取birthday");
    	return birthday;
    }
    
    
    public float getStayTime() {
    	if(stayTime == 30)
    	{
    		stayTime =0;
    		//如果满一小时未起身，语音提醒，并且清零
//            try {
//            	MediaPlayer mediaPlayer = new MediaPlayer();
//            	AssetFileDescriptor fd = context.getAssets().openFd("audio/staytime.mp3");
//				mediaPlayer.setDataSource(fd.getFileDescriptor());
//            	mediaPlayer.prepare();
//				mediaPlayer.start();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
    		AudioControl.getInstance().setAudioData("staytime.mp3",AudioControl.Priority.WARNING); 
			AudioControl.getInstance().startAudio();
			//AudioControl.getInstance().release();
    	}
    	else
    	{
    		stayTime+=1;
    	} 	
    	SetTestText.addText("人时表获取stayTime");
    	return stayTime;
    }
    
//    public int getLocalGanZhi() {
//    	birthGanZhi = 10;
//		return birthGanZhi;
//	}
    
    public float getHeartRate()
    {
    	//模拟心率获取 范围0-200
    	if(heartRate == 200)
    	{
    		heartRate = 40;
    	}
    	else
    	{
    		heartRate += 2;
    	}
//    	try {
//        	MediaPlayer mediaPlayer = new MediaPlayer();
//        	AssetFileDescriptor fd = context.getAssets().openFd("audio/heartrate.mp3");
//			mediaPlayer.setDataSource(fd.getFileDescriptor());
//        	mediaPlayer.prepare();
//			mediaPlayer.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	SetTestText.addText("人时表获取heartRate");
    	return heartRate;
    }
    
    public float getHighBloodPressure()
    {
    	if(highBloodPressure == 150)
    	{
    		highBloodPressure = 90;
    	}
    	else
    	{
    		highBloodPressure+=2;
    	}
//    	try {
//        	MediaPlayer mediaPlayer = new MediaPlayer();
//        	AssetFileDescriptor fd = context.getAssets().openFd("audio/bloodpressure.mp3");
//			mediaPlayer.setDataSource(fd.getFileDescriptor());
//        	mediaPlayer.prepare();
//			mediaPlayer.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	SetTestText.addText("人时表获取highBloodPressure");
    	return highBloodPressure;
    }
    
    public float getLowBloodPressure()
    {
    	if(lowBloodPressure == 90)
    	{
    		lowBloodPressure = 50;
    	}
    	else
    	{
    		lowBloodPressure+=2;
    	}
//    	try {
//        	MediaPlayer mediaPlayer = new MediaPlayer();
//        	AssetFileDescriptor fd = context.getAssets().openFd("audio/bloodpressure.mp3");
//			mediaPlayer.setDataSource(fd.getFileDescriptor());
//        	mediaPlayer.prepare();
//			mediaPlayer.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	SetTestText.addText("人时表获取lowBloodPressure");
    	return lowBloodPressure;
    }
    
    public float getBloodOxygen()
    {
    	if(bloodOxygen == 100)
    	{
    		bloodOxygen = 85;
    	}
    	else
    	{
    		bloodOxygen++;
    	}
//    	try {
//        	MediaPlayer mediaPlayer = new MediaPlayer();
//        	AssetFileDescriptor fd = context.getAssets().openFd("audio/bloodoxygen.mp3");
//			mediaPlayer.setDataSource(fd.getFileDescriptor());
//        	mediaPlayer.prepare();
//			mediaPlayer.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	SetTestText.addText("人时表获取bloodOxygen");
    	return bloodOxygen;
    }
    
    public float getBodyTemperature()
    {
    	
    	//这里判断条件不太对 不知道为什么
    	if(bodyTemperature >= 40.0f)
    	{
    		bodyTemperature = 35;
    	}
    	else
    	{
    		bodyTemperature+=0.1f;
    	}
//    	try {
//        	MediaPlayer mediaPlayer = new MediaPlayer();
//        	AssetFileDescriptor fd = context.getAssets().openFd("audio/bodytemperature.mp3");
//			mediaPlayer.setDataSource(fd.getFileDescriptor());
//        	mediaPlayer.prepare();
//			mediaPlayer.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    	SetTestText.addText("人时表获取bodyTemperature");
    	return bodyTemperature;
    }
    public int getTotalDay()
    {
    	//当前时间对应的毫秒数字
    	long current_millis = currentTime.getTimeInMillis();
    	
    	long chazhi = current_millis-birthTime;
    	total_day = (int)(chazhi/86400000);
    	
    	//MainActivity.clockImageView.setText();
    	SetTestText.addText("人时表获取total_day");
    	return total_day;
    }
    public float getPStage()
    {
    	int yushu = total_day%23;
    	//Log.e("test_millis",String.format("%d",birthTime));
    	if(yushu>=0 &&yushu <= 10)
    	{
    		PStage = 3;
    		pstr = "高潮";
    	}
    	else if(yushu == 11 ||yushu == 12)
    	{
    		PStage = 2;
    		pstr = "临界";
    	}
    	else 
    	{
    		PStage = 1;
    		pstr = "低潮";
    	}
    	SetTestText.addText("人时表获取PStage");
    	return PStage;
    }
    
    public float getSStage()
    {
    	int yushu = total_day%28;
    	if(yushu>=0 &&yushu <= 13)
    	{
    		SStage = 3;
    		sstr = "高潮";
    	}
    	else if(yushu == 14)
    	{
    		SStage = 2;
    		sstr = "临界";
    	}
    	else 
    	{
    		SStage = 1;
    		sstr = "低潮";
    	}
    	SetTestText.addText("人时表获取SStage");
    	return SStage;
    }
    
    public float getIStage()
    {
    	int yushu = total_day%33;
    	if(yushu>=0 &&yushu <= 15)
    	{
    		IStage = 3;
    		istr = "高潮";
    	}
    	else if(yushu == 16 ||yushu == 17)
    	{
    		IStage = 2;
    		istr = "临界";
    	}
    	else 
    	{
    		IStage = 1;
    		istr = "低潮";
    	}
    	SetTestText.addText("人时表获取IStage");
    	return IStage;
    }
    
    
    //蜂鸣器控制
    private void beepControl(int number) {
		try {
			for (int i = 0; i < number; i++) {
				BeepClass.IoctlRelay(1);
				Thread.sleep(200);
				BeepClass.IoctlRelay(0);
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
	public int getBirthGanZhi() {
		//指针表示的是生日干支 所以获取最后两个字符
		String lastTwoChars = getBirthGanZhiStr().substring(6, 8);
		birthGanZhi = Arrays.asList(Constants.SIXTY_GANZHI_STRING).indexOf(lastTwoChars);
		SetTestText.addText("人时表获取birthGanZhi");
		return birthGanZhi;
	}
	
	public String getBirthGanZhiStr()
	{
		Calendar birthCalendar=Calendar.getInstance();
		birthCalendar.setTime(birthday);
		//birthGanZhiStr = "test";下面这行没问题
		birthGanZhiStr = ExcelUtil.lookUp05(context,birthCalendar);
		SetTestText.addText("人时表获取birthGanZhiStr");
		return birthGanZhiStr;
	}
    
    public String getInfoText() {
    	String str16 = String.format("%.1f", bodyTemperature);
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
    	String str=
				"人时信息"
						+"\r\n"+"【您的生日】"+formatter.format(birthday)+" "+Constants.LUNAR_HOUR_STRINGS[hourIndex]
						+"\r\n"+"【生日干支】"+birthGanZhiStr
						+"\r\n"+"【久坐时间】"+stayTime/60+"分钟"
						+"\r\n"+"【P】"+pstr
						+"\r\n"+"【S】"+sstr
						+"\r\n"+"【I】"+istr
						+"\r\n"+"【心率】"+heartRate
						+"\r\n"+"【血压】"+lowBloodPressure+","+highBloodPressure
						+"\r\n"+"【血氧】"+bloodOxygen
						+"\r\n"+"【体温】"+str16;
    	SetTestText.addText("人时表获取infoText");
		return str;
	}
}
