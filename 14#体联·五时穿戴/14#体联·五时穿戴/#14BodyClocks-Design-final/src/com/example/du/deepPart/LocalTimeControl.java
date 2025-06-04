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
		
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(3); // ������߿��
		paintCircle.setAntiAlias(true); // �����
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

		
		/* ��ָ�� */
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
		//�ճ�����
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(mri,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//�ճ�Сʱ
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(hri,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		paintPointer.setColor(Color.RED);
		//�������
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(mse,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//����Сʱ
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(hse,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		paintPointer.setColor(Color.DKGRAY);
		//������ǿ��
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(pur,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		
		// ������
		canvas.translate(-clockWidth/2 , -clockHeight/2);
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
		//������Ҫ���� ���get�����������ݷŵ�get������
		String str3 = "��γ��" + longitude + "��E" + " " + latitude + "��N";
		String str4 = "���� ��" + altitude + "m";
		String str5 = "��ѹ��" +  "1008hPa";
		String str6 = "ʪ�ȣ�" +  "50%";
		String str7 = "�����ߣ�" +ultraviolet+  "��";


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
	
	private String longitude;//����
	private String latitude;//γ��
	private double altitude;//����
	private Date localDate;//����ʱ��
	private int ultraviolet;//������ǿ��
	private String sunRiseTime;//�ճ�ʱ��
	private String sunSetTime;//����ʱ��
    private long birthTime;

    /**
   	 * ���� ���г�ʼֵ
   	 */
    private Date birthday;

	public String getInfoText() {
		String str =
			"��ʱ��Ϣ"
			+ "\r\n" + "����γ�ȡ�" + longitude + "��E" + " " + latitude + "��N"
			+ "\r\n" + "�����Ρ�" + altitude + "m"
			+ "\r\n" + "����ѹ��" +"1008hPa"
			+ "\r\n" + "��ʪ�ȡ�" + "50%"
			+ "\r\n" + "��������ǿ�ȡ�" + ultraviolet +"��"
			+ "\r\n" + "���ճ�ʱ�䡿" + sunRiseTime
			+ "\r\n" + "������ʱ�䡿" + sunSetTime
			;
		SetTestText.addText("��ʱ���ȡinfoText");
		return str;
	}


	
	public String getLongitude() {
		longitude="117.2E";	
		SetTestText.addText("��ȡ����");
		return longitude;
	}

	public String getLatitude() {
		latitude="39.1N";
		SetTestText.addText("��ȡγ��");
		return latitude;
	}

	public double getAltitude() {
		altitude=3.3;
		SetTestText.addText("��ȡ����");
		return altitude;
	}

	public Date getLocalDate() {
		localDate = new Date(System.currentTimeMillis());
		SetTestText.addText("��ȡ����");
		return localDate;
	}

	public String getSunRiseTime() {
		BigDecimal x = new BigDecimal(117.3574);
		BigDecimal y = new BigDecimal(39.0888);
		Date sunTime = new Date(System.currentTimeMillis());
		sunRiseTime = SunSet.getSunrise(x, y, sunTime);
		SetTestText.addText("��ȡ�ճ�ʱ��");
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
		SetTestText.addText("��ȡ����ʱ��");
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