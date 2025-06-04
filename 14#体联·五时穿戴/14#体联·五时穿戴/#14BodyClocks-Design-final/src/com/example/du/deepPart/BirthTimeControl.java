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
		//����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding*2, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5,
				paintCircle);

		// ���̶�
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
		// ������
		int hour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
		int minute = getCurrentTime().get(Calendar.MINUTE);
		int second = getCurrentTime().get(Calendar.SECOND);
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		paintText.setAntiAlias(true);
		String str1 = "��  ��", str2 = "��  ע",str5=Constants.BIRTH_HOUR_STRINGS[((hour+1)%24)/2];
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

		/* ��ָ�� */
		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(5);
		paintPointer.setAntiAlias(true);
		//����ʱ��ָ��
				float degree_minute,degree_hour;
				degree_minute = 6*minute+second/10;
				degree_hour = (hour)*15+minute/4;
				//degree_second=6*second;
				//����ʱ�����
				paintPointer.setStrokeWidth(2);
				canvas.save();
				canvas.rotate(degree_minute,0,0);
				canvas.drawLine(0, 0, 0, -clockHeight/2+3*clockPadding, paintPointer);
				canvas.restore();
				//����ʱ��Сʱ
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
		 * ����ʱ�䣬��ֱ�����������ý��洫������
		 */
    private long birthTime;
	    /**
	   	 * ���� ���г�ʼֵ
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
		SetTestText.addText("��ȡʱ��");
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
		SetTestText.addText("��ȡ����ʱ");
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
    	SetTestText.addText("��ȡ����");
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
	         // ��GPS��λ��Ϣ�����ı�ʱ�����¶�λ
	         updateShow(location);
	     }

	     @Override
	     public void onStatusChanged(String provider, int status, Bundle extras) {

	     }

	     @Override
	     public void onProviderEnabled(String provider) {
	         // ��GPS LocationProvider����ʱ�����¶�λ
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
            sb.append("��ǰ��λ����Ϣ��\n");
            sb.append("���ȣ�" + decimalFormat.format(location.getLongitude()) + "\n");
            sb.append("γ�ȣ�" + decimalFormat.format(location.getLatitude()) + "\n");
            sb.append("�ٶȣ�" + location.getSpeed() + "\n");
            sb.append("����" + location.getBearing() + "\n");
            String str=sb.toString();
            SetTestText.addText("��ȡGPS��λ");
            return str;
        }
        return null;
    }

    
	 public String getInfoText() {
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
	    	//GPS
			Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);   //���
            criteria.setAltitudeRequired(true);  //�ܹ��ṩ�߶���Ϣ
            criteria.setBearingRequired(true);   //�ܹ��ṩ������Ϣ
	    	String str=
					"��ʱ��Ϣ"
							+"\r\n"+"���������ա�"+formatter.format(birthday)+" "+Constants.LUNAR_HOUR_STRINGS[hourIndex]
							+"\r\n"+"����ǰʱ�䡿"+getTimeStr()
							+"\r\n"+"����ǰ������"+getMeridianStr()
							+"\r\n"+getShouldDoStr()
							+"\r\n"+updateShow(lc);
			return str;
		}
}
