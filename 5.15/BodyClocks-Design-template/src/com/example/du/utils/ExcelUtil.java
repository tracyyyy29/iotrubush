package com.example.du.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import android.content.Context;
/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * for lookUp Excels
 * 
 * lookUp01-lookUp06
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class ExcelUtil {
	
//	public static int test(Context context)
//	{
//		try {
//			AssetManager am=context.getAssets();
//			Log.e("ExcelUtilTest","step 1");
//			InputStream inputStream=context.getAssets().open("test.xls");
//			Log.e("ExcelUtilTest","step 2");
//			Workbook workbook=Workbook.getWorkbook(inputStream);
//			Log.e("ExcelUtilTest","step 3");
//			Sheet sheet=workbook.getSheet(0);
//			Log.e("ExcelUtilTest","step 4");
//			String str=sheet.getCell(0,0).getContents();
//			Log.e("ExcelUtilTest","step 5");
//			return Integer.valueOf(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return -1;
//	}
	
	
	public final static int GONG_LI=0;
	public final static int YIN_LI=1;
	//公历阴历转换
	public static Date lookUp01(Context context,Date date, int date_flag) {

        Calendar result = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        try {

            InputStream inputStream = context.getAssets().open("xls/BodyNetIV01-yl-30.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();
            if (date_flag == YIN_LI) {
                for (int i = 1; i < rowCount; i++) {
                    // 获取需要的单元格，getContents（）函数可以获取单元格内容并返回一个String值
                    Cell cell0 = sheet.getCell(0, i);
                    Cell cell1 = sheet.getCell(1, i);
                    Cell cell2 = sheet.getCell(2, i);
                    if ((cell0.getContents().equals(year)) && (cell1.getContents().equals(month)) && (cell2.getContents().equals(day))) {
                        String y = sheet.getCell(3, i).getContents();
                        String m = sheet.getCell(4, i).getContents();
                        String d = sheet.getCell(5, i).getContents();
                        result.set(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d));
                        break;
                    }
                }
            } else if (date_flag == GONG_LI) {
                for (int i = 1; i < rowCount; i++) {
                    // 获取需要的单元格，getContents（）函数可以获取单元格内容并返回一个String值
                    Cell cell0 = sheet.getCell(3, i);
                    Cell cell1 = sheet.getCell(4, i);
                    Cell cell2 = sheet.getCell(5, i);
                    if ((cell0.getContents().equals(year)) && (cell1.getContents().equals(month)) && (cell2.getContents().equals(day))) {
                        String y = sheet.getCell(0, i).getContents();
                        String m = sheet.getCell(1, i).getContents();
                        String d = sheet.getCell(2, i).getContents();
                        result.set(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getTime();
    }
	
	
	//获取太阳真时
	public static Date lookUp02(Context context,Date date) {
        Calendar result = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int deltaMin = 0, deltaSec = 0;
        try {

            InputStream inputStream = context.getAssets().open("xls/BodyNetIV02-TureSunTime.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();

            for (int i = 1; i < rowCount; i++) {
                // 获取需要的单元格，getContents（）函数可以获取单元格内容并返回一个String值
                Cell cell0 = sheet.getCell(0, i);
                Cell cell1 = sheet.getCell(1, i);
                if ((cell0.getContents().equals(Integer.toString(month + 1))) && (cell1.getContents().equals(Integer.toString(day)))) {
                    deltaMin = Integer.parseInt(sheet.getCell(2, i).getContents());
                    deltaSec = Integer.parseInt(sheet.getCell(3, i).getContents());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        minute += deltaMin;
        second += deltaSec;
        result.set(year, month, day, hour, minute, second);
        return result.getTime();
    }
	
	//获取节气
	public static String lookUp04(Context context,Date date) {
        String result = "";
        String backward = "";
        String forward = "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        try {

            InputStream inputStream = context.getAssets().open("xls/BodyNetIV04-jq-30.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();

            for (int i = 1; i < rowCount; i++) {
                // 获取需要的单元格，getContents（）函数可以获取单元格内容并返回一个String值
                Cell cell0 = sheet.getCell(0, i);
                if (cell0.getContents().equals(Integer.toString(year))) {
                    int month = Integer.parseInt(sheet.getCell(1, i).getContents()) - 1;
                    int day = Integer.parseInt(sheet.getCell(2, i).getContents());
                    Calendar calendar1 = Calendar.getInstance();
                    calendar.set(year, month, day);
                    int days = calendar.get(Calendar.DAY_OF_YEAR);
                    if (days == dayOfYear) {
                        return sheet.getCell(3, i).getContents();
                    } else if (days < dayOfYear) {
                        backward = sheet.getCell(3, i).getContents();
                    } else if (days > dayOfYear) {
                        forward = sheet.getCell(3, i).getContents();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backward + "~" + forward;
    }

    public static String lookUp05(Context context,Calendar calendar) {
        String result = "";

        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        try {
            InputStream inputStream = context.getAssets().open("xls/BodyNetIV05-GanZhiXuShi.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();

            for (int i = 1; i < rowCount; i++) {
                // 获取需要的单元格，getContents（）函数可以获取单元格内容并返回一个String值
                Cell cell0 = sheet.getCell(0, i);
                if (cell0.getContents().equals(year)) {
                    Cell cell1 = sheet.getCell(1, i);
                    Cell cell2 = sheet.getCell(2, i);

                    if (cell1.getContents().equals(month) && cell2.getContents().equals(day)) {
                        result = sheet.getCell(3, i).getContents() + " " + sheet.getCell(4, i).getContents() + " " + sheet.getCell(5, i).getContents() + " ";
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<String> lookUp06(Context context,int acupointNum) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            InputStream inputStream = context.getAssets().open("xls/BodyNetIV06-kxb.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();
            int columnCount = sheet.getColumns();
            for (int i = 1; i < rowCount; i++) {
                // 获取需要的单元格，getContents（）函数可以获取单元格内容并返回一个String值
                Cell cell0 = sheet.getCell(0, i);
                if (cell0.getContents().equals(Integer.toString(acupointNum))) {
                    for (int j = 1; j < columnCount; j++) {
                        result.add(sheet.getCell(j, i).getContents());
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    //获取表格一共有多少行
    public static int lookUp07(Context context)
    {
    	int rowCount = 0;
    	 try {
             InputStream inputStream = context.getAssets().open("xls/BodyNetIV08-psi_record.xls");
             Workbook workbook = Workbook.getWorkbook(inputStream);
             Sheet sheet = workbook.getSheet(0);
             rowCount = sheet.getRows();
             
         } catch (Exception e) {
             e.printStackTrace();
         }
    	 //实际天数是行数减一
    	return rowCount-1;
    }
    
    public static ArrayList<Integer> lookUpP(Context context) {
    	ArrayList<Integer> result = new ArrayList<Integer>();

        try {
            InputStream inputStream = context.getAssets().open("xls/BodyNetIV08-psi_record.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();

            for (int i = 1; i < rowCount; i++) {
            	result.add(Integer.parseInt(sheet.getCell(3, i).getContents()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static ArrayList<Integer> lookUpS(Context context) {
    	ArrayList<Integer> result = new ArrayList<Integer>();

        try {
            InputStream inputStream = context.getAssets().open("xls/BodyNetIV08-psi_record.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();

            for (int i = 1; i < rowCount; i++) {
            	result.add(Integer.parseInt(sheet.getCell(4, i).getContents()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static ArrayList<Integer> lookUpI(Context context) {
    	ArrayList<Integer> result = new ArrayList<Integer>();

        try {
            InputStream inputStream = context.getAssets().open("xls/BodyNetIV08-psi_record.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();

            for (int i = 1; i < rowCount; i++) {
            	result.add(Integer.parseInt(sheet.getCell(5, i).getContents()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    //测试函数
    public static int test(Context context) { 
    	int result = 10086;
        try {
            InputStream inputStream = context.getAssets().open("xls/BodyNetIV05-GanZhiXuShi.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rowCount = sheet.getRows();
            result = rowCount;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
