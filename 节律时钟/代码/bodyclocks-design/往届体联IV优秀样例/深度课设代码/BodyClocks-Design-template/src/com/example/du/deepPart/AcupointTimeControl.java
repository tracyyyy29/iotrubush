package com.example.du.deepPart;

import java.util.Calendar;
import java.util.Date;

import com.example.du15.R;

import android.content.Context;
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
 * AcupointTimeControl extends BaseTimeControl 
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class AcupointTimeControl extends AbstractTimeControl{
	private static AcupointTimeControl instance=null;
	public static AcupointTimeControl getInstance(){
		if(instance==null)
			instance=new AcupointTimeControl();
		return instance;
	}
	
	private AcupointTimeControl(){}

	@Override
	public Bitmap drawClock() {
		float clockWidth = canvasWidth;
		float clockHeight = canvasHeight;
		float clockPadding = canvasPadding;

		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				Constants.CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint();// ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(3); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		
		//������ͼ
		Bitmap baguaImage =  BitmapFactory.decodeResource(context.getResources(),R.drawable.baguashuimo);
		int baguaImageWidth = baguaImage.getWidth();
		int baguaImageHeight = baguaImage.getHeight();
		//�������ű���
		int newWidth,newHeight;
		float scale = (2*(clockWidth / 5- clockPadding)-10)/baguaImageWidth;
		Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
		Bitmap newBaguaImage = Bitmap.createBitmap(baguaImage,0,0,baguaImageWidth,baguaImageHeight,matrix,true);
		
		canvas.drawBitmap(newBaguaImage,clockWidth/2-clockWidth / 5+ clockPadding+5,
				clockWidth/2-clockWidth / 5+ clockPadding+5,paintCircle);
		
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);

		//������Ȧ ����ͼ����Ȧ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 5
				- clockPadding, paintCircle);
		//���ӳ���î��������Ȧ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 3
				- clockPadding, paintCircle);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 2*clockWidth / 5
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5, paintCircle);
		
		
		

		// ���̶�
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 24; i++) {

			paintDegree.setStrokeWidth(2);
			canvas.drawLine(clockWidth / 2, clockWidth/2-clockWidth/5+clockPadding, clockWidth / 2,
					clockWidth/2-clockWidth/5+clockPadding-(float) (clockPadding * 0.5), paintDegree);

			canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		}
		canvas.save();
		
		canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		for(int i = 0;i<12;i++)
		{
			//���ָ���
			canvas.drawLine(clockWidth/2, clockPadding, 
					clockWidth/2, clockPadding+clockWidth/2-clockWidth/3, paintDegree);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		canvas.restore();
		canvas.save();
		
		
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* ��ָ��*/
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(3);
		paintPointer.setColor(Color.RED);
		paintPointer.setAntiAlias(true);
		int hour = currentDate.getHours();
		int minute = currentDate.getMinutes();
		float degree_hour;
		degree_hour = 15*hour+minute/4;
		canvas.rotate(degree_hour);
		canvas.drawLine(0, 0, 0, -clockWidth / 2 + 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// ������
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(10);
		paintText.setAntiAlias(true);
		String str1 = "Ѩ", str2 = "λ", str3 = "ʱ";
		canvas.drawText(
				str1,
				clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
				clockHeight / 2, paintText);
		paintText.setColor(Color.WHITE);
		canvas.drawText(str2, clockWidth / 2 - paintText.measureText(str2) / 2,
				clockHeight / 2 - clockPadding, paintText);
		canvas.drawText(
				str3,
				clockWidth / 2 + clockPadding - paintText.measureText(str3) / 2,
				clockHeight / 2, paintText);
		
		
		paintText.setColor(Color.BLACK);
		for (int i = 0; i < 24; i++) {
			
			String str31 = Constants.CLOCK_24HOUR_STRINGS[i];
			float offset = paintText.descent() - paintText.ascent();
			float offset3 = paintText.measureText(str31) / 2;
			canvas.drawText(str31, clockWidth / 2 - offset3,
					clockWidth/2-clockWidth/5+clockPadding-(float) (clockPadding * 0.25)-offset, paintText);
			canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		}
		
		for (int i = 0; i < 12; i++) {

			String str11 = Constants.ACUPOINT_TERM_STRINGS[i];
			String str21 = Constants.LUNAR_HOUR_STRINGS[i];
			float offset = paintText.descent() - paintText.ascent();
			float offset1 = paintText.measureText(str11) / 2;
			float offset2 = paintText.measureText(str21) / 2;
			canvas.drawText(str11, clockWidth / 2 - offset1, 
					clockWidth/2-2*clockWidth / 5
					+ clockPadding-offset2/2, paintText);
			canvas.drawText(str21, clockWidth / 2 - offset2,
					clockWidth/2-clockWidth / 3
					+ clockPadding-offset2/2, paintText);
			canvas.rotate(30, clockWidth / 2, clockHeight / 2);
		}
		
		canvas.restore();

		return bm;
	}

	@Override
	public void updateData() {
		getCurrentDate();
	}
	
	
	/**
	 * ��ǰ����
	 */
    protected static Date currentDate;
    
    
    public Date getCurrentDate() {
		currentDate=new Date(System.currentTimeMillis());
		SetTestText.addText("Ѩλʱ���ȡcurrentDate");
		return currentDate;
	}
    public String getInfoText() {
		String str=
				"Ѩλʱ��Ϣ"
						+"\r\n"+"��ǰʱ��"+Constants.LUNAR_HOUR_STRINGS[(AcupointTimeControl.currentDate.getHours()+1)/2%12]
						+"\r\n"+"��ӦѨλ"+Constants.ACUPOINT_TERM_STRINGS[(AcupointTimeControl.currentDate.getHours()+1)/2%12]
						+"\r\n"+"������Ϣ"
						+"\r\n"+Constants.ACUPOINT_ADD_STRINGS_A[(AcupointTimeControl.currentDate.getHours()+1)/2%12]
						+"\r\n"+Constants.ACUPOINT_ADD_STRINGS_B[(AcupointTimeControl.currentDate.getHours()+1)/2%12]		
						+"\r\n"+Constants.ACUPOINT_ADD_STRINGS_C[(AcupointTimeControl.currentDate.getHours()+1)/2%12]
								;
		SetTestText.addText("Ѩλʱ���ȡinfoText");
		return str;
	}

//	@Override
//	public Bitmap drawClock() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
