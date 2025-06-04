package com.example.du.deepPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;

//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
////import org.apache.poi.ss.usermodel.Sheet;
////import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.example.du.utils.ExcelUtil;
import com.example.du15.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ConsumerInnovationActivity extends Activity implements OnClickListener
{
	protected Context context;
	private Button okButton;
	private Button saveButton;
	private Button checkButton;
	//	private RadioButton p_radioButton1,p_radioButton2,p_radioButton3;
	//	private RadioButton s_radioButton1,s_radioButton2,s_radioButton3;
	//	private RadioButton i_radioButton1,i_radioButton2,i_radioButton3;
	private RadioGroup radioGroup_p;
	private RadioGroup radioGroup_s;
	private RadioGroup radioGroup_i;

	private TextView textview_p;
	private TextView textview_s;
	private TextView textview_i;

	private int P_Stage;
	private int S_Stage;
	private int I_Stage;


	//	public Context getContext() {
	//		return context;
	//	}
	//
	//	public void setContext(Context context) {
	//		this.context = context;
	//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consumer_innovation_activity);
		//		Log.e("test1", "oncreate1");

		okButton = (Button)findViewById(R.id.button_set_ok);
		checkButton = (Button)findViewById(R.id.button_check_record);
		saveButton = (Button)findViewById(R.id.button_save);


		textview_p =(TextView)findViewById(R.id.p_value);
		textview_s =(TextView)findViewById(R.id.s_value);
		textview_i =(TextView)findViewById(R.id.i_value);

		radioGroup_p=(RadioGroup)findViewById(R.id.p_choose);
		radioGroup_s=(RadioGroup)findViewById(R.id.s_choose);
		radioGroup_i=(RadioGroup)findViewById(R.id.i_choose);
		//		Log.e("test1", "oncreate2");
		//监听器
		okButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		checkButton.setOnClickListener(this);


		//		radioGroup_p.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		//			@Override
		//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
		//				P_Stage=4-arg1;
		//			}
		//		});
		radioGroup_p.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if(arg1==R.id.p_high){
					P_Stage=3;
				}else if(arg1==R.id.p_middle){
					P_Stage=2;
				}else if(arg1==R.id.p_low){
					P_Stage=1;
				}

			}
		});

		radioGroup_s.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if(arg1==R.id.s_high){
					S_Stage=3;
				}else if(arg1==R.id.s_middle){
					S_Stage=2;
				}else if(arg1==R.id.s_low){
					S_Stage=1;
				}

			}
		});

		radioGroup_i.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if(arg1==R.id.i_high){
					I_Stage=3;
				}else if(arg1==R.id.i_middle){
					I_Stage=2;
				}else if(arg1==R.id.i_low){
					I_Stage=1;
				}

			}
		});


		//		radioGroup_s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		//			@Override
		//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
		//				S_Stage=7-arg1;
		//			}
		//		});
		//		radioGroup_i.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		//			@Override
		//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
		//				I_Stage=10-arg1;
		//			}
		//		});

		float pStage = BirthTimeControl.getInstance().getPStage();
		float sStage = BirthTimeControl.getInstance().getSStage();
		float iStage = BirthTimeControl.getInstance().getIStage();

		if(pStage == 1)
		{
			textview_p.setText("低潮");
		}
		else if(pStage == 2)
		{
			textview_p.setText("临界");
		}
		else
		{
			textview_p.setText("高潮");
		}

		if(sStage == 1)
		{
			textview_s.setText("低潮");
		}
		else if(sStage == 2)
		{
			textview_s.setText("临界");
		}
		else
		{
			textview_s.setText("高潮");
		}

		if(iStage == 1)
		{
			textview_i.setText("低潮");
		}
		else if(iStage == 2)
		{
			textview_i.setText("临界");
		}
		else
		{
			textview_i.setText("高潮");
		}
		//		int number = ExcelUtil.test(this);
		//		saveButton.setText(String.format("行数是%d",number));

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.button_set_ok){

			this.finish();
		}
		if(v.getId()==R.id.button_save){

			//存储到表格中去 应该有三个参数 分别是PSI值 日期可以自行获取
			saveExcel(P_Stage,S_Stage,I_Stage);
		}
		if(v.getId()==R.id.button_check_record){

			this.finish();
			Intent intent = new Intent(this, PSIrecord.class);
			startActivity(intent);
		}
	}


	//	private void saveExcel(int P,int S,int I) {
	//		try{	
	////			WritableWorkbook writeBook = Workbook.createWorkbook(new File("xls/BodyNetIV08-psi_record.xls"));
	////			WritableWorkbook writeBook = Workbook.createWorkbook(new File("D:/adt-bundle-windows-x86-20140702/BodyClocks-Design-template/assets/xls/BodyNetIV08-psi_record.xls"));
	//			
	//			
	//			OutputStream outputStream = new FileOutputStream("xls/BodyNetIV08-psi_record.xls");
	////			OutputStream outputStream = this.getAssets().
	//			WritableWorkbook writeBook = Workbook.createWorkbook(outputStream);
	//			
	//			WritableSheet sheet = writeBook.getSheet(0);
	//			int rowCount = sheet.getRows();
	//
	//
	//			Calendar currentTime = Calendar.getInstance();
	//			String year_str = String.format("%d",currentTime.get(Calendar.YEAR));
	//			String month_str = String.format("%d",currentTime.get(Calendar.MONTH));
	//			String day_str = String.format("%d",currentTime.get(Calendar.DAY_OF_MONTH));
	//			sheet.addCell(new Label(0,rowCount+1,year_str));
	//			sheet.addCell(new Label(1,rowCount+1,month_str));
	//			sheet.addCell(new Label(2,rowCount+1,day_str));
	//			
	//			String P_str = String.valueOf(P);
	//			String S_str = String.valueOf(S);
	//			String I_str = String.valueOf(I);
	//			sheet.addCell(new Label(3,rowCount+1,P_str));
	//			sheet.addCell(new Label(4,rowCount+1,S_str));
	//			sheet.addCell(new Label(5,rowCount+1,I_str));
	//
	//			writeBook.write();
	//			writeBook.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}
	//}

	private void saveExcel(int P,int S,int I) {
		Calendar currentTime = Calendar.getInstance();
		//		String year_str = String.format("%d",currentTime.get(Calendar.YEAR));
		//		String month_str = String.format("%d",currentTime.get(Calendar.MONTH));
		//		String day_str = String.format("%d",currentTime.get(Calendar.DAY_OF_MONTH));
		Log.e("SQLiteData","p_:"+String.valueOf(P));
		Log.e("SQLiteData","s_:"+String.valueOf(S));
		Log.e("SQLiteData","i_:"+String.valueOf(I));
		MainActivity.insertData(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH),
				currentTime.get(Calendar.HOUR), currentTime.get(Calendar.MINUTE),currentTime.get(Calendar.SECOND),
				(int) BirthTimeControl.getInstance().getPStage(), (int)BirthTimeControl.getInstance().getSStage(), (int)BirthTimeControl.getInstance().getIStage(),
				P, S, I);
	}
}


//try{
//			OutputStream outputStream = new FileOutputStream(new File("xls/BodyNetIV08-psi_record.xls"), true);
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			HSSFSheet sheet = workbook.getSheetAt(0);
//
//			int rowCount = sheet.getLastRowNum();
//
//			HSSFRow row = sheet.createRow(rowCount+1);
//
//			//一行有六个单元格
//			HSSFCell cell0 = row.createCell(0);
//			HSSFCell cell1 = row.createCell(1);
//			HSSFCell cell2 = row.createCell(2);
//
//			Calendar currentTime = Calendar.getInstance();
//			String year_str = String.format("%d",currentTime.get(Calendar.YEAR));
//			String month_str = String.format("%d",currentTime.get(Calendar.MONTH));
//			String day_str = String.format("%d",currentTime.get(Calendar.DAY_OF_MONTH));
//
//			cell0.setCellValue(year_str);
//			cell1.setCellValue(month_str);
//			cell2.setCellValue(day_str);
//
//			HSSFCell cell3 = row.createCell(3);
//			HSSFCell cell4 = row.createCell(4);
//			HSSFCell cell5 = row.createCell(5);
//			String P_str = String.valueOf(P);
//			cell3.setCellValue(P_str);
//			String S_str = String.valueOf(S);
//			cell4.setCellValue(S_str);
//			String I_str = String.valueOf(I);
//			cell5.setCellValue(I_str);
//
//			workbook.write(outputStream);
//			outputStream.close();
//			workbook.close(); // 关闭workbook对象
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
