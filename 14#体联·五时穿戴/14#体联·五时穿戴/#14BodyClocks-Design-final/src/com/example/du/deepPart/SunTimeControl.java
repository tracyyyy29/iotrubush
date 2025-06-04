package com.example.du.deepPart;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

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
	
	
	private GregorianCalendar calendar;
	private SunTimeExcel ste = new SunTimeExcel();
 
	
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
		
	}
	
	public Calendar subtractSeconds(Calendar calendar, int seconds) {
        calendar.add(Calendar.SECOND, -seconds);
        return calendar;
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
		paintPointer.setAntiAlias(true);
		
		//����ʱ��ָ��
		calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR); 
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		float degree_minute,degree_hour,degree_sec;
		degree_minute = 6*minute+second/10;
		degree_hour = (hour%12)*30+minute/2;
		degree_sec = second * 6;
		//����ʱ�����
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//����ʱ��Сʱ
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(degree_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();	
		//����ʱ����
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_sec,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+(int)0.5*clockPadding, paintPointer);
		canvas.restore();
//		
		//̫����׼ʱָ��
		GregorianCalendar calendar1 = (GregorianCalendar) subtractSeconds(calendar, 768);
		int standardsunhour = calendar1.get(Calendar.HOUR_OF_DAY);
		int standardsunminute = calendar1.get(Calendar.MINUTE);
		int standardsunsecond = calendar1.get(Calendar.SECOND);
		float degree_standardsun_minute,degree_standardsun_hour;
		degree_standardsun_minute = 6*standardsunminute+standardsunsecond/10;
		degree_standardsun_hour = (standardsunhour%12)*30+standardsunminute/2;
		paintPointer.setColor(Color.YELLOW);
		//̫����׼ʱ����
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_standardsun_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//̫����׼ʱСʱ
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(degree_standardsun_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
//		
		//̫����ʱָ��
		Calendar calendar_real = getSunRealTime();
		int realsunhour = calendar_real.get(Calendar.HOUR);
		int realsunminute = calendar_real.get(Calendar.MINUTE);
		int realsunsecond = calendar_real.get(Calendar.SECOND);
		
		float degree_realsun_minute,degree_realsun_hour;
		degree_realsun_minute = 6*realsunminute+realsunsecond/10;
		degree_realsun_hour = (realsunhour%12)*30+realsunminute/2;
		paintPointer.setColor(Color.RED);
		//̫����ʱ����
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_realsun_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//̫����ʱСʱ
		paintPointer.setStrokeWidth(5);
		canvas.save();
		canvas.rotate(degree_realsun_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
//		
//		
		//����ָ��
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

		return bm;
	}
	public void repaint(){
		
	}

	/**
	 * ��ǰ����
	 */
	private Date currentDate;
    /**
	 * ��ǰ���� ������ʽ ��ȡyear month day�ķ���û��̭
	 */
	private Calendar currentTime;
    /**
     * ũ��
     */
	private Date lunarDate;
    /**
     * ̫����׼ʱ
     */
	private Date sunStandardDate;
    /**
     * ̫����ʱ
     */
	private Date sunRealDate;
    /**
     * ��ǰ����
     */
    private int season=-1;
    /**
     * ��ǰ����
     */
    private int solarTerm=-1;
    /**
     * ������־��Ϊ���ʱ���ʾ�����䣬Ϊһ��ʱ���ʾ������ĳ��������
     */
    private int solarTermFlag = -1;
    
    
    public Date getCurrentDate() {
		currentDate=new Date();
		//SetTestText.addText("��ʱ���ȡcurrentDate");
		return currentDate;
	}
	

	
	public Calendar getSunRealTime() {
		Calendar calendar = new GregorianCalendar();
		int currentMonth = calendar.get(Calendar.MONTH) + 1;
		int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
		int offsetSeconds = ste.getSunTimeExcel(currentMonth, currentDate);
		SetTestText.addText("��ȡ̫����ʱ");
		calendar.add(Calendar.SECOND, offsetSeconds);
        return calendar;

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
        SetTestText.addText("��ȡ����");
		return season;
	}
	
	public String getSeasonStr(){
		SetTestText.addText("��ʱ���ȡSeasonStr");
		return Constants.SEASON_STRING[getSeason()];
	}

	//��ȡ����������int�� ��Ҫ��������� ǡ���ǽ������������ 0 1 0ֱ����index 1��Ҫ��index�Ļ�����ƫ�ư������
	public float getSolarTerm() {
		String str = getSolarTermStr();
		String lstr = str.substring(str.length() - 2);
		SetTestText.addText("��ȡ����");
		int t = Arrays.asList(Constants.SOLAR_TERM_STRING).indexOf(lstr);
		float tt = (float)t - (float)0.5;
		if(str.length()>2){
			return tt;
		}
		return t;
	}
	
	//��ȡ�����ַ����������ǵ������� Ҳ�����ǽ�������
	public String getSolarTermStr(){
		Lunar l = new Lunar(System.currentTimeMillis());
		return l.getTermString();
	}

	public String getInfoText() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��hhʱmm��ss��");
		Calendar calendar2 = new GregorianCalendar();

		Calendar calendar1 =  subtractSeconds(calendar2, 768);

		Calendar calendar_real = getSunRealTime();

		Lunar l = new Lunar(System.currentTimeMillis());
		SetTestText.addText("��ȡũ��");
		String str2 = sdf.format(calendar2.getTime()); 
		String str3 = sdf.format(calendar1.getTime()); 
		String str4 = sdf.format(calendar_real.getTime()); 
		String str5 = l.getLunarDateString(); 
		String str6 = l.getTermString(); 
		String str7 = l.getCyclicalDateString(); 
		SetTestText.addText("��ȡ��֧��");
		String str= 
			    "��ʱ��Ϣ"
						+"\r\n"+"������ʱ�䡿    "+str2
						+"\r\n"+"��̫����ʱ��    "+str3
						+"\r\n"+"��̫����׼ʱ��"+str4
						+"\r\n"+"��ũ����"+str5
						+"\r\n"+"����ǰ������"+str6
						+"\r\n"+"����֧����"+str7;
		return str;
	}
	
}
