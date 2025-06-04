package com.example.du.deepPart;

import java.util.ArrayList;
import com.example.du.deepPart.PSIInputUtil;
import java.util.Arrays;
import java.util.Calendar;

import com.example.du.utils.ExcelUtil;
import com.example.du15.R;
import com.example.du.deepPart.PSIInputUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PSIrecord extends Activity implements OnClickListener
{

	private ImageView PSIImageView;
	protected Context context;
	private String result;
	private int PSIrecordNumber = 0;
	private ArrayList<Integer>p_list=null;
	private ArrayList<Integer>s_list=null;
	private ArrayList<Integer>i_list=null;

	private boolean isDraw = false;


//	public Context getContext() {
//		return context;
//	}
//
//	public void setContext(Context context) {
//		this.context = context;
//	}
	
	public Bitmap drawGraph()
	{
		int bmOffset=50;
		int bmWidth = getResources().getDisplayMetrics().widthPixels-bmOffset;
		int bmHeight = getResources().getDisplayMetrics().heightPixels-bmOffset;
		
		Log.e("PSIRecord",String.valueOf(bmWidth));
		Log.e("PSIRecord",String.valueOf(bmHeight));
		int padding = 100;

		Bitmap bm = Bitmap.createBitmap(bmWidth, bmHeight,
				Constants.CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布

		Paint paint_background = new Paint();
		paint_background.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paint_background.setStrokeWidth(3); // 设置描边宽度
		paint_background.setAntiAlias(true); // 抗锯齿

		canvas.drawLine(padding,bmHeight-padding,bmWidth-padding,bmHeight-padding,paint_background);
		canvas.drawLine(padding,padding,padding,bmHeight-padding,paint_background);

		//画文字
		String x_str = "日期";
		String y_str = "PSI档位";
		String example_str = "图例";
		String P_str = "P";
		String S_str = "S";
		String I_str = "I";

		paint_background.setColor(Color.BLACK);
		paint_background.setTextSize(20);
		paint_background.setStrokeWidth(1);
		paint_background.setAntiAlias(true);
		float offset_example = paint_background.descent() - paint_background.ascent();
		canvas.drawText(x_str, bmWidth-padding*2, bmHeight-padding/2, paint_background);
		canvas.drawText(y_str, padding/2, padding/2, paint_background);
		canvas.drawText(example_str, bmWidth-padding*2, padding/2, paint_background);
		canvas.drawText(P_str, bmWidth-padding*4, padding/2+offset_example, paint_background);
		canvas.drawText(S_str, bmWidth-padding*4, padding/2+offset_example*2, paint_background);
		canvas.drawText(I_str, bmWidth-padding*4, padding/2+offset_example*3, paint_background);
		paint_background.setColor(Color.RED);
		canvas.drawLine(bmWidth-padding*3, padding/2+offset_example, bmWidth-padding, padding/2+offset_example, paint_background);
		paint_background.setColor(Color.GREEN);
		canvas.drawLine(bmWidth-padding*3, padding/2+offset_example*2, bmWidth-padding, padding/2+offset_example*2, paint_background);
		paint_background.setColor(Color.BLUE);
		canvas.drawLine(bmWidth-padding*3, padding/2+offset_example*3, bmWidth-padding, padding/2+offset_example*3, paint_background);
		paint_background.setColor(Color.BLACK);

		//画x轴刻度
		//行数减是点的个数
		//这里为了测试效果先给一个值 实际上应该通过get来获取 最大给到100
//		PSIrecordNumber = 100;
		//计算间隔
		int offset = (bmWidth-padding*2)/PSIrecordNumber;
		for(int i = 0;i<PSIrecordNumber;i++)
		{
			canvas.drawLine(padding+offset*i,bmHeight-padding,padding+offset*i,bmHeight-padding+10,paint_background);
		}

		//画y轴刻度
		int offset2 = (bmHeight-padding*2)/4;
		paint_background.setColor(Color.GRAY);
		paint_background.setStrokeWidth(1);
		canvas.drawLine(padding, bmHeight-padding-offset2, bmWidth-padding, bmHeight-padding-offset2, paint_background);
		canvas.drawLine(padding, bmHeight-padding-offset2*2, bmWidth-padding, bmHeight-padding-offset2*2, paint_background);
		canvas.drawLine(padding, bmHeight-padding-offset2*3, bmWidth-padding, bmHeight-padding-offset2*3, paint_background);

		//画x轴日期文字
		//画y轴档位文字
		String str_3 = "高潮";
		String str_2 = "临界";
		String str_1 = "低潮";
		float offset_text = paint_background.measureText(str_3)/2;
		paint_background.setStrokeWidth(1);
		paint_background.setColor(Color.BLACK);
		canvas.drawText(str_3, padding/2-offset_text, bmHeight-padding-offset2*3, paint_background);
		canvas.drawText(str_2, padding/2-offset_text, bmHeight-padding-offset2*2, paint_background);
		canvas.drawText(str_1, padding/2-offset_text, bmHeight-padding-offset2, paint_background);

		//		canvas.drawText(result, padding/2-offset_text, screenHeight-padding-offset2, paint_background);

		//		//信息显示
		//		canvas.drawText(String.format("%d",PSIrecordNumber), screenWidth/2, screenHeight/2, paint_background);
		//		
		//		String psi_str10 = result.substring(0, 3);
		//		String psi_str09 = result.substring(4, 7);
		//		String psi_str08 = result.substring(8, 11);
		//		String psi_str07 = result.substring(12, 15);
		//		String psi_str06 = result.substring(16, 19);
		//		String psi_str05 = result.substring(20, 23);
		//		String psi_str04 = result.substring(24, 27);
		//		String psi_str03 = result.substring(28, 31);
		//		String psi_str02 = result.substring(32, 35);
		//		String psi_str01 = result.substring(36, 39);
		//		int i01 = Integer.valueOf(psi_str01);
		//		int i02 = Integer.valueOf(psi_str02);
		//		int i03 = Integer.valueOf(psi_str03);
		//		int i04 = Integer.valueOf(psi_str04);
		//		int i05 = Integer.valueOf(psi_str05);
		//		int i06 = Integer.valueOf(psi_str06);
		//		int i07 = Integer.valueOf(psi_str07);
		//		int i08 = Integer.valueOf(psi_str08);
		//		int i09 = Integer.valueOf(psi_str09);
		//		int i10 = Integer.valueOf(psi_str10);
		
		
		
		//画P曲线、点
		paint_background.setStyle(Paint.Style.FILL);
		paint_background.setColor(Color.RED);

		for(int i = 0;i<PSIrecordNumber;i++)
		{
			//画点
			canvas.drawCircle(padding+offset*i, bmHeight-padding-offset2*p_list.get(i), 3,
					paint_background);
			//画线
			if(i==PSIrecordNumber-1)
			{
				break;
			}
			paint_background.setStrokeWidth(2);
			canvas.drawLine(padding+offset*i, bmHeight-padding-offset2*p_list.get(i)
					, padding+offset*(i+1), bmHeight-padding-offset2*p_list.get(i+1)
					, paint_background);
		}
		//效果展示
//		canvas.drawCircle(padding+offset*1, screenHeight-padding-offset2, 10,
//				paint_background);
//		canvas.drawCircle(padding+offset*2, screenHeight-padding-offset2*2, 10,
//				paint_background);
//		canvas.drawCircle(padding+offset*3, screenHeight-padding-offset2*3, 10,
//				paint_background);
//		canvas.drawCircle(padding+offset*4, screenHeight-padding-offset2*3, 10,
//				paint_background);
//		canvas.drawCircle(padding+offset*5, screenHeight-padding-offset2, 10,
//				paint_background);
//		canvas.drawCircle(padding+offset*6, screenHeight-padding-offset2, 10,
//				paint_background);
//		paint_background.setStrokeWidth(2);
//		canvas.drawLine(padding+offset*1, screenHeight-padding-offset2
//				, padding+offset*2, screenHeight-padding-offset2*2, paint_background);
//		canvas.drawLine(padding+offset*2, screenHeight-padding-offset2*2
//				, padding+offset*3, screenHeight-padding-offset2*3, paint_background);
//		canvas.drawLine(padding+offset*3, screenHeight-padding-offset2*3
//				, padding+offset*4, screenHeight-padding-offset2*3, paint_background);
//		canvas.drawLine(padding+offset*4, screenHeight-padding-offset2*3
//				, padding+offset*5, screenHeight-padding-offset2, paint_background);
//		canvas.drawLine(padding+offset*5, screenHeight-padding-offset2
//				, padding+offset*6, screenHeight-padding-offset2, paint_background);

		//画S曲线、点
		paint_background.setColor(Color.GREEN);
		for(int i = 0;i<PSIrecordNumber;i++)
		{
			//画点
			canvas.drawCircle(padding+offset*i, bmHeight-padding-offset2*s_list.get(i), 3,
					paint_background);
			//画线
			if(i==PSIrecordNumber-1)
			{
				break;
			}
			paint_background.setStrokeWidth(2);
			canvas.drawLine(padding+offset*i, bmHeight-padding-offset2*s_list.get(i)
					, padding+offset*(i+1), bmHeight-padding-offset2*s_list.get(i+1)
					, paint_background);

			//canvas.drawText(String.format("长度是%d",s_list.length), screenWidth/2, screenHeight/2, paint_background);
		}


		//画I曲线、点
		paint_background.setColor(Color.BLUE);

		for(int i = 0;i<PSIrecordNumber;i++)
		{
			//画点
			canvas.drawCircle(padding+offset*i, bmHeight-padding-offset2*i_list.get(i), 3,
					paint_background);
			//画线
			if(i==PSIrecordNumber-1)
			{
				break;
			}
			paint_background.setStrokeWidth(2);
			canvas.drawLine(padding+offset*i, bmHeight-padding-offset2*i_list.get(i)
					, padding+offset*(i+1), bmHeight-padding-offset2*i_list.get(i+1)
					, paint_background);
		}
//		canvas.drawText(String.format("长度是%d行",PSIrecordNumber), bmWidth/2, bmWidth/2, paint_background);
		canvas.drawText(String.format("PSI记录共有%d天",PSIrecordNumber), bmWidth/2, bmHeight-offset_text, paint_background);
		
		return bm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.psi_record_activity);

		PSIImageView = (ImageView) findViewById(R.id.psi_image);

		//		getPSIrecord();
//		getPSIrecordnumber();
//		getPList();
//		getSList();
//		getIList();
		getPSIData();
		
		if(isDraw)
		{
			PSIImageView.setImageBitmap(drawGraph());
		}
	}


	
	private void getPSIData() {
		p_list=new ArrayList<Integer>();
		s_list=new ArrayList<Integer>();
		i_list=new ArrayList<Integer>();
		PSIInputUtil psiUtil = MainActivity.getpsiUtil();
		SQLiteDatabase db = psiUtil.getReadableDatabase();
		Cursor cursor = db.query(PSIInputUtil.TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			isDraw=true;
			do {
				PSIrecordNumber++;
				int id = cursor.getInt(cursor.getColumnIndex(PSIInputUtil.ID));
				String time = cursor.getString(cursor
						.getColumnIndex(PSIInputUtil.TIME));
				int p_in_theory = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.P_IN_THEORY));
				int s_in_theory = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.S_IN_THEORY));
				int i_in_theory = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.I_IN_THEORY));
				int p_in_fact = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.P_IN_FACT));
				int s_in_fact = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.S_IN_FACT));
				int i_in_fact = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.I_IN_FACT));
				p_list.add(p_in_fact);
				s_list.add(s_in_fact);
				i_list.add(i_in_fact);
				
			} while (cursor.moveToNext());
		} else {
			
//			String str = "0\tnull\tnull\tnull\tnull\tnull\tnull\tnull";
		}
		Log.e("SQLiteData","p_list:"+p_list.toString());
		Log.e("SQLiteData","s_list:"+s_list.toString());
		Log.e("SQLiteData","i_list:"+i_list.toString());
		
		cursor.close();
	}
	/*
	//这三个函数实际上应该是通过Lookup 08 09 10 分别获取一列 因为行数获取有问题 暂时搁置
	private void getPList() {
		// TODO Auto-generated method stub
//		ArrayList<Integer> PArrayList = ExcelUtil.lookUpP(this);
		ArrayList<Integer> PArrayList = queryAllData(0);
		int[] PArray = new int[PArrayList.size()];
		for(int i = 0;i<PArrayList.size();i++)
		{
			PArray[i] = PArrayList.get(i);
		}
		p_list = Arrays.copyOf(PArray, PSIrecordNumber);
	}	

	private void getSList() {
		// TODO Auto-generated method stub

//		ArrayList<Integer> SArrayList = ExcelUtil.lookUpS(this);
		ArrayList<Integer> SArrayList = queryAllData(1);
		int[] SArray = new int[SArrayList.size()];
		for(int i = 0;i<SArrayList.size();i++)
		{
			SArray[i] = SArrayList.get(i);
		}
		s_list = Arrays.copyOf(SArray, PSIrecordNumber);
	}

	private void getIList() {
		// TODO Auto-generated method stub

//		ArrayList<Integer> IArrayList = ExcelUtil.lookUpI(this);
		ArrayList<Integer> IArrayList = queryAllData(2);
		int[] IArray = new int[IArrayList.size()];
		for(int i = 0;i<IArrayList.size();i++)
		{
			IArray[i] = IArrayList.get(i);
		}
		i_list = Arrays.copyOf(IArray, PSIrecordNumber);
	}

	private ArrayList<Integer> queryAllData(int index) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		PSIInputUtil psiUtil = MainActivity.getpsiUtil();
		SQLiteDatabase db = psiUtil.getReadableDatabase();
		Cursor cursor = db.query(PSIInputUtil.TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(PSIInputUtil.ID));
				String time = cursor.getString(cursor
						.getColumnIndex(PSIInputUtil.TIME));
				int p_in_theory = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.P_IN_THEORY));
				int s_in_theory = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.S_IN_THEORY));
				int i_in_theory = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.I_IN_THEORY));
				int p_in_fact = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.P_IN_FACT));
				int s_in_fact = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.S_IN_FACT));
				int i_in_fact = cursor.getInt(cursor
						.getColumnIndex(PSIInputUtil.I_IN_FACT));
				//我也不知道为什么用了psiInputUtil之后存入的时候分别要额外加个1 4 7
				//反正不加就是不行
				//但我也不知道为什么不行
				if(index == 0)
				{
					result.add(p_in_fact);
				}
				else if(index == 1)
				{
					result.add(s_in_fact);
				}
				else
				{
					result.add(i_in_fact);
				}
				
			} while (cursor.moveToNext());
		} else {
//			String str = "0\tnull\tnull\tnull\tnull\tnull\tnull\tnull";
		}
		cursor.close();
		return result;
	}
	



	//	private String getPSIrecord()
	//	{
	//		//这里有个日期索引的问题，我还没想好用日期的长串数字还是int类型的数字
	//		//如果最近没存 也不能直接找今天的对应psi 这里需要大改 
	//		//我想的是直接查表 从后往前取十行数据
	//		
	//		result = ExcelUtil.lookUp08(context);
	//		Log.e("test_psi", "lookup");
	//		
	//		return result;
	//	}

	//获取psi记录的行数
	private int getPSIrecordnumber()
	{
//		PSIrecordNumber = ExcelUtil.lookUp07(this);
		
		//这里也是为了弥补表格长度获取有问题而加的一行
//		PSIrecordNumber = 101;
		
		PSIrecordNumber = 0;
		PSIInputUtil psiUtil = MainActivity.getpsiUtil();
		SQLiteDatabase db = psiUtil.getReadableDatabase();
		Cursor cursor = db.query(PSIInputUtil.TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				PSIrecordNumber++;
				
			} while (cursor.moveToNext());
		} else {
//			String str = "0\tnull\tnull\tnull\tnull\tnull\tnull\tnull";
		}
		cursor.close();
		
		if(PSIrecordNumber != 0)
		{
			isDraw = true;
		}
		
		
		return PSIrecordNumber;
	}
	*/
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}


}
