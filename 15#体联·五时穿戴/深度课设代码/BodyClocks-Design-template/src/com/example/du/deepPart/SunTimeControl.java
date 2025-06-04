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
		
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(3); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		
		//����ͼ
		Bitmap baguaImage =  BitmapFactory.decodeResource(context.getResources(),R.drawable.sun1);
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
		paintPointer.setAntiAlias(true);
		
		//����ʱ��ָ��
		int hour = currentDate.getHours();
		int minute = currentDate.getMinutes();
		int second = currentDate.getSeconds();
		float degree_minute,degree_hour;
		degree_minute = 6*minute+second/10;
		degree_hour = (hour%12)*30+minute/2;
		//����ʱ�����
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//����ʱ��Сʱ
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		//̫����׼ʱָ��
		int standardsunhour = sunStandardDate.getHours();
		int standardsunminute = sunStandardDate.getMinutes();
		int standardsunsecond = sunStandardDate.getSeconds();
		float degree_standardsun_minute,degree_standardsun_hour;
		degree_standardsun_minute = 6*standardsunminute+standardsunsecond/10;
		degree_standardsun_hour = (standardsunhour%12)*30+standardsunminute/2;
		paintPointer.setColor(Color.YELLOW);
		//̫����׼ʱ����
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_standardsun_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//̫����׼ʱСʱ
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_standardsun_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
		
		//̫����ʱָ��
		int realsunhour = sunRealDate.getHours();
		int realsunminute = sunRealDate.getMinutes();
		int realsunsecond = sunRealDate.getSeconds();
		float degree_realsun_minute,degree_realsun_hour;
		degree_realsun_minute = 6*realsunminute+realsunsecond/10;
		degree_realsun_hour = (realsunhour%12)*30+realsunminute/2;
		paintPointer.setColor(Color.RED);
		//̫����ʱ����
		paintPointer.setStrokeWidth(1);
		canvas.save();
		canvas.rotate(degree_realsun_minute,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+2*clockPadding, paintPointer);
		canvas.restore();
		//̫����ʱСʱ
		paintPointer.setStrokeWidth(3);
		canvas.save();
		canvas.rotate(degree_realsun_hour,0,0);
		canvas.drawLine(0, 0, 0, -clockHeight/2+4*clockPadding, paintPointer);
		canvas.restore();
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
		float offset = paintText.descent() - paintText.ascent();
		
		canvas.drawText("����ʱ��"+currentDate,
				- paintText.measureText("����ʱ��"+currentDate)
						/ 2, -2*offset, paintText);
		canvas.drawText("̫����ʱ"+sunRealDate,
				- paintText.measureText("̫����ʱ"+sunRealDate)
						/ 2, -3*offset, paintText);
		canvas.drawText("̫����׼ʱ"+sunStandardDate,
				- paintText.measureText("̫����׼ʱ"+sunStandardDate)
						/ 2, -4*offset, paintText);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
		canvas.drawText("ũ����"+formatter.format(lunarDate),
						- paintText.measureText("ũ����"+formatter.format(lunarDate))
						/ 2, 2*offset, paintText);
//		canvas.drawText("������"+currentTime.get(Calendar.YEAR)+"��"
//						+(currentTime.get(Calendar.MONTH)+1)+"��"
//						+currentTime.get(Calendar.DAY_OF_MONTH)+"��",
//						- paintText.measureText("������"+currentTime.get(Calendar.YEAR)+"��"
//						+(currentTime.get(Calendar.MONTH)+1)+"��"
//						+currentTime.get(Calendar.DAY_OF_MONTH)+"��")
//						/ 2, 3*offset, paintText);
		canvas.drawText("������"+formatter.format(currentDate),
				- paintText.measureText("������"+formatter.format(currentDate))
				/ 2, 3*offset, paintText);
		canvas.restore();

		return bm;
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
		currentDate=new Date(System.currentTimeMillis());
		SetTestText.addText("��ʱ���ȡcurrentDate");
		return currentDate;
	}
	
	public Calendar getCurrentTime() {
		currentTime=Calendar.getInstance();
		SetTestText.addText("��ʱ���ȡcurrentTime");
		return currentTime;
	}

	public Date getLunarDate(){
		lunarDate=ExcelUtil.lookUp01(context,getCurrentDate(),ExcelUtil.GONG_LI);
		SetTestText.addText("��ʱ���ȡLunarDate");
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
		//Ŀǰ�������� ̫����׼ʱ���ڱ���ʱ���������
		second -= 2;
		result.set(year, month, day, hour, minute, second);
		sunStandardDate = result.getTime();
		SetTestText.addText("��ʱ���ȡSunStandardDate");
		return sunStandardDate;
	}


	public Date getSunRealDate() {
		//sunRealDate=getCurrentDate();
		sunRealDate=ExcelUtil.lookUp02(context,getCurrentDate());
		SetTestText.addText("��ʱ���ȡSunRealDate");
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
        SetTestText.addText("��ʱ���ȡSeason");
		return season;
	}
	
	public String getSeasonStr(){
		SetTestText.addText("��ʱ���ȡSeasonStr");
		return Constants.SEASON_STRING[getSeason()];
	}

	//��ȡ����������int�� ��Ҫ��������� ǡ���ǽ������������ 0 1 0ֱ����index 1��Ҫ��index�Ļ�����ƫ�ư������
	public int getSolarTerm() {
		//��Ҫ��ȡ�����ַ�����ǰ��λ�����Ľ�����
		String firstTwoChars = getSolarTermStr().substring(0, 2);
		SetTestText.addText("��ʱ���ȡSolarTerm");
		return Arrays.asList(Constants.SOLAR_TERM_STRING).indexOf(firstTwoChars);
	}
	
	//��ȡ�����ַ����������ǵ������� Ҳ�����ǽ�������
	public String getSolarTermStr(){
		String solarTermStr=ExcelUtil.lookUp04(context,getCurrentDate());
		//�����ַ�Ҳռһλ
		if(solarTermStr.length()>2)
		{
			solarTermFlag = 0;
		}
		else
		{
			solarTermFlag = 1;
		}
		SetTestText.addText("��ʱ���ȡSolarTermStr");
		return solarTermStr;
	}

	public String getInfoText() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
		String str=
			    "��ʱ��Ϣ"
						+"\r\n"+"������ʱ�䡿    "+currentDate
						+"\r\n"+"��̫����ʱ��    "+sunRealDate
						+"\r\n"+"��̫����׼ʱ��"+sunStandardDate
						+"\r\n"+"��������"+formatter.format(currentDate)
						+"\r\n"+"��ũ����"+formatter.format(lunarDate)
						+"\r\n"+"����ǰ������"+getSolarTermStr();
//		int number = ExcelUtil.test(context);
//		String str = String.format("������%d",number);
		SetTestText.addText("��ʱ���ȡInfoText");
		return str;
	}
	
}

