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
		
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(3); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		
		//����ͼ
		Bitmap baguaImage =  BitmapFactory.decodeResource(context.getResources(),R.drawable.earth2);
		int baguaImageWidth = baguaImage.getWidth();
		int baguaImageHeight = baguaImage.getHeight();
		//�������ű���
		int newWidth,newHeight;
		float scale = (2*(clockWidth / 2- clockPadding)-10)/baguaImageWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap newBaguaImage = Bitmap.createBitmap(baguaImage,0,0,baguaImageWidth,baguaImageHeight,matrix,true);

		canvas.drawBitmap(newBaguaImage,clockWidth/2-clockWidth / 2+ clockPadding+5,
				clockWidth/2-clockWidth /2+ clockPadding+5,paintCircle);

		
		
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5,
				paintCircle);

		// ���̶�
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
		/* ��ָ�� */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(3);
		paintPointer.setAntiAlias(true);
		//�ճ�ʱ��ָ��
		int rise_hour = sunRiseTime_todraw.getHours();
		int rise_minute = sunRiseTime_todraw.getMinutes();
		int rise_second = sunRiseTime_todraw.getSeconds();
		float degree_rise_minute,degree_rise_hour;
		degree_rise_minute = 6*rise_minute+rise_second/10;
		degree_rise_hour = (rise_hour%12)*30+rise_minute/2;
		paintPointer.setColor(Color.YELLOW);
		//����
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_rise_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//Сʱ
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_rise_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		//����ʱ��ָ��
		int set_hour = sunSetTime_todraw.getHours();
		int set_minute = sunSetTime_todraw.getMinutes();
		int set_second = sunSetTime_todraw.getSeconds();
		float degree_set_minute,degree_set_hour;
		degree_set_minute = 6*set_minute+set_second/10;
		degree_set_hour = (set_hour%12)*30+set_minute/2;
		paintPointer.setColor(Color.RED);
		//����
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_set_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//Сʱ
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_set_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		
		//��ָ֧��
		float degree_ganzhi = localGanZhi*6;
		paintPointer.setColor(Color.argb(100, 225, 165, 0));
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_ganzhi, 0, 0);
		canvas.drawLine(0, 0, 0,
				(float) (clockPadding * 0.4 - clockHeight / 2),
				paintPointer);	
		
		canvas.restore();

		// ������
		canvas.translate(-clockWidth / 2, -clockHeight / 2);
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		paintText.setAntiAlias(true);
		String str1 = "��", str2 = "ʱ";
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
		//������Ҫ���� ���get�����������ݷŵ�get������
		String str3 = "��γ��"+longitude+"��E"+" "+latitude+"��N";
		String str4 = "���� ��"+altitude+"m";
		String str5 = "��ѹ��"+barometric+"hPa";
		String str6 = "ʪ�ȣ�"+humidity+"%";
		String str7 = "������"+wind+"��";
		String str8 = "��֧��"+localGanZhiStr;
		
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
	 * ����
	 */
	private double longitude;
	private BigDecimal longitude1;
	/**
	 * γ��
	 */
	private double latitude;
	private BigDecimal latitude1;
	/**
	 * ����
	 */
	private double altitude;
	/**
	 * ��ѹ
	 */
	private double barometric;
	/**
	 * ����ʱ��
	 */
	private Date localDate;
	/**
	 * ���ظ�֧ ����һ��������� ����Ҫ�ǵ�get������ȡ���֮��ת��������
	 */
	private int localGanZhi;
	/**
	 * ���ظ�֧ �������յĸ�֧ �м��пո�
	 */
	private String localGanZhiStr;
	/**
	 * �ճ�ʱ�� ���ַ��� �������ڻ�ָ��
	 */
	private String sunRiseTime;
	/**
	 * ����ʱ�� ���ַ��� �������ڻ�ָ��
	 */
	private String sunSetTime;
	/**
	 * �ճ�ʱ�� ���ڻ�ָ��
	 */
	private Date sunRiseTime_todraw;
	/**
	 * ����ʱ�� ���ڻ�ָ��
	 */
	private Date sunSetTime_todraw;
	/**
	 * ʪ��
	 */
	private double humidity;
	/**
	 * ����
	 */
	private int wind;
	
	public double getLongitude() {
		longitude = 117.3574;
		longitude1 =new BigDecimal(117.3574);
		SetTestText.addText("��ʱ���ȡlongitude");
		return longitude;
	}

	public double getLatitude() {
		latitude = 39.0888;
		latitude1 = new BigDecimal(39.0888);
		SetTestText.addText("��ʱ���ȡlatitude");
		return latitude;
	}

	public double getAltitude() {
		altitude = 3.5;
		SetTestText.addText("��ʱ���ȡaltitude");
		return altitude;
	}
	
	public double getBarometric()
	{
		barometric = 1022;
		SetTestText.addText("��ʱ���ȡbarometric");
		return barometric;
	}
	
	public double getHumidity()
	{
		humidity = 61;
		SetTestText.addText("��ʱ���ȡhumidity");
		return humidity;
	}
	
	public int getWind()
	{
		wind = 2;
		SetTestText.addText("��ʱ���ȡwind");
		return wind;
	}

	public Date getLocalDate() {
		localDate=new Date(System.currentTimeMillis());
		SetTestText.addText("��ʱ���ȡlocaldate");
		return localDate;
	}

	public int getLocalGanZhi() {
		//ָ���ʾ���ǵ��ո�֧ ���Ի�ȡ��������ַ�
		String lastTwoChars = getLocalGanZhiStr().substring(6, 8);
		localGanZhi = Arrays.asList(Constants.SIXTY_GANZHI_STRING).indexOf(lastTwoChars);
		SetTestText.addText("��ʱ���ȡlocalGanZhi");
		return localGanZhi;
	}
	
	public String getLocalGanZhiStr()
	{
		Calendar currentTime=Calendar.getInstance();
		localGanZhiStr = ExcelUtil.lookUp05(context,currentTime);
		SetTestText.addText("��ʱ���ȡlocalGanZhiStr");
		return localGanZhiStr;
	}

	//�ճ�����ʱ���ȡ ���ص����ַ��� ��ʱ����
	public String getSunRiseTime() {
		
		sunRiseTime = SunRiseSet.getSunrise(longitude1, latitude1, localDate);
		SetTestText.addText("��ʱ���ȡsunRiseTime");
		return sunRiseTime;
	}

	public String getSunSetTime() {
		
		sunSetTime = SunRiseSet.getSunset(longitude1, latitude1, localDate);
		SetTestText.addText("��ʱ���ȡsunSetTime");
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
		SetTestText.addText("��ʱ���ȡsunRiseTime_todraw");
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
		SetTestText.addText("��ʱ���ȡsunSetTime_todraw");
		return sunSetTime_todraw;
	}
	
	public String getInfoText() {
		String str=
			    "��ʱ��Ϣ"
			    		+"\r\n"+"����γ�ȡ�"+longitude+"��E"+" "+latitude+"��N"
						+"\r\n"+"�����Ρ�"+altitude+"m"
						+"\r\n"+"����ѹ��"+barometric+"hPa"
						+"\r\n"+"��ʪ�ȡ�"+humidity+"%"
						+"\r\n"+"��������"+wind+"��"
						+"\r\n"+"����ǰ��֧��"+localGanZhiStr
						+"\r\n"+"���ճ�ʱ�䡿"+sunRiseTime
						+"\r\n"+"������ʱ�䡿"+sunSetTime
						;
		SetTestText.addText("��ʱ���ȡinfoText");
		return str;
	}

}
