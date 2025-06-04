package com.example.du.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public final static int GONG_LI=0;
	public final static int YIN_LI=1;
	
	public Date lookUp01(Date date,int flag)
	{
		Calendar result=Calendar.getInstance();
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		
		String year=String.valueOf(calendar.get(Calendar.YEAR));
		String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
		String day=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		
		try {
			InputStream inputStream = new FileInputStream("/assets/xls/BodyNetIV01-yl-30.xls");
			XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
			XSSFSheet sheet=workbook.getSheetAt(0);
			int rowCount=sheet.getLastRowNum();
			 if (flag == YIN_LI) {
	                for (int i = 1; i < rowCount; i++) {
	                    // 获取需要的单元格，getStringCellValue（）函数可以获取单元格内容并返回一个String值
	                    XSSFCell cell0 = sheet.getRow(0).getCell(i);
	                    XSSFCell cell1 = sheet.getRow(1).getCell(i);
	                    XSSFCell cell2 = sheet.getRow(2).getCell(i);
	                    if ((cell0.getStringCellValue().equals(year)) && (cell1.getStringCellValue().equals(month)) && (cell2.getStringCellValue().equals(day))) {
	                        String y = sheet.getRow(3).getCell(i).getStringCellValue();
	                        String m = sheet.getRow(4).getCell(i).getStringCellValue();
	                        String d = sheet.getRow(5).getCell(i).getStringCellValue();
	                        result.set(Integer.parseInt(y), Integer.parseInt(m) - 1, Integer.parseInt(d));
	                        break;
	                    }
	                }
	            } else if (flag == GONG_LI) {
	                for (int i = 1; i < rowCount; i++) {
	                    // 获取需要的单元格，getStringCellValue（）函数可以获取单元格内容并返回一个String值
	                    XSSFCell cell0 = sheet.getRow(3).getCell(i);
	                    XSSFCell cell1 = sheet.getRow(4).getCell(i);
	                    XSSFCell cell2 = sheet.getRow(5).getCell(i);
	                    if ((cell0.getStringCellValue().equals(year)) && (cell1.getStringCellValue().equals(month)) && (cell2.getStringCellValue().equals(day))) {
	                        String y = sheet.getRow(0).getCell(i).getStringCellValue();
	                        String m = sheet.getRow(1).getCell(i).getStringCellValue();
	                        String d = sheet.getRow(2).getCell(i).getStringCellValue();
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
	
	 public Date lookUp02(Date date) {
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

	            InputStream inputStream = new FileInputStream("/assets/xls/BodyNetIV02-TureSunTime.xls");
	            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            int rowCount = sheet.getLastRowNum();

	            for (int i = 1; i < rowCount; i++) {
	                // 获取需要的单元格，getStringCellValue（）函数可以获取单元格内容并返回一个String值
	                XSSFCell cell0 = sheet.getRow(0).getCell(i);
	                XSSFCell cell1 = sheet.getRow(1).getCell(i);
	                if ((cell0.getStringCellValue().equals(Integer.toString(month + 1))) && (cell1.getStringCellValue().equals(Integer.toString(day)))) {
	                    deltaMin = Integer.parseInt(sheet.getRow(2).getCell(i).getStringCellValue());
	                    deltaSec = Integer.parseInt(sheet.getRow(3).getCell(i).getStringCellValue());
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

	    public String lookUp04(Date date) {
	        String result = "";
	        String backward = "";
	        String forward = "";

	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);

	        int year = calendar.get(Calendar.YEAR);
	        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

	        try {

	            InputStream inputStream = new FileInputStream("/assets/xls/BodyNetIV04-jq-30.xls");
	            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            int rowCount = sheet.getLastRowNum();

	            for (int i = 1; i < rowCount; i++) {
	                // 获取需要的单元格，getStringCellValue（）函数可以获取单元格内容并返回一个String值
	                XSSFCell cell0 = sheet.getRow(0).getCell(i);
	                if (cell0.getStringCellValue().equals(Integer.toString(year))) {
	                    int month = Integer.parseInt(sheet.getRow(1).getCell(i).getStringCellValue()) - 1;
	                    int day = Integer.parseInt(sheet.getRow(2).getCell(i).getStringCellValue());
	                    Calendar calendar1 = Calendar.getInstance();
	                    calendar.set(year, month, day);
	                    int days = calendar.get(Calendar.DAY_OF_YEAR);
	                    if (days == dayOfYear) {
	                        return sheet.getRow(3).getCell(i).getStringCellValue();
	                    } else if (days < dayOfYear) {
	                        backward = sheet.getRow(3).getCell(i).getStringCellValue();
	                    } else if (days > dayOfYear) {
	                        forward = sheet.getRow(3).getCell(i).getStringCellValue();
	                        break;
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return backward + "~" + forward;
	    }

	    public String lookUp05(Calendar calendar) {
	        String result = "";

	        String year = Integer.toString(calendar.get(Calendar.YEAR));
	        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
	        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

	        try {
	            InputStream inputStream = new FileInputStream("/assets/xls/BodyNetIV05-GanZhiXuShi.xls");
	            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            int rowCount = sheet.getLastRowNum();

	            for (int i = 1; i < rowCount; i++) {
	                // 获取需要的单元格，getStringCellValue（）函数可以获取单元格内容并返回一个String值
	                XSSFCell cell0 = sheet.getRow(0).getCell(i);
	                if (cell0.getStringCellValue().equals(year)) {
	                    XSSFCell cell1 = sheet.getRow(1).getCell(i);
	                    XSSFCell cell2 = sheet.getRow(2).getCell(i);

	                    if (cell1.getStringCellValue().equals(month) && cell2.getStringCellValue().equals(day)) {
	                        result = sheet.getRow(3).getCell(i).getStringCellValue() + " " + sheet.getRow(4).getCell(i).getStringCellValue() + " " + sheet.getRow(5).getCell(i).getStringCellValue() + " ";
	                        break;
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }

	    public ArrayList<String> lookUp06(int acupointNum) {
	        ArrayList<String> result = new ArrayList<String>();
	        try {
	            InputStream inputStream = new FileInputStream("/assets/xls/BodyNetIV06-kxb.xls");
	            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            int rowCount = sheet.getLastRowNum();
	            int columnCount = sheet.getRow(0).getLastCellNum();
	            for (int i = 1; i < rowCount; i++) {
	                // 获取需要的单元格，getStringCellValue（）函数可以获取单元格内容并返回一个String值
	                XSSFCell cell0 = sheet.getRow(0).getCell(i);
	                if (cell0.getStringCellValue().equals(Integer.toString(acupointNum))) {
	                    for (int j = 1; j < columnCount; j++) {
	                        result.add(sheet.getRow(j).getCell(i).getStringCellValue());
	                    }
	                    break;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
}
