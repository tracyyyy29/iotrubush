package com.example.du.deepPart;
/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * define some vars for different class using
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class Vars {
	private static Vars vars=null;
	private Vars(){}
	public static Vars getInstance()
	{
		if(vars==null){
			vars=new Vars();
		}
		return vars;
	}
	
	/**
	 * ListViewѡȡλ�õļ�¼ 
	 */
	private int myListViewItemChoicePosition=0;
	public int getMyListViewItemChoicePosition() {
		return myListViewItemChoicePosition;
	}
	public void setMyListViewItemChoicePosition(int myListViewItemChoicePosition) {
		this.myListViewItemChoicePosition = myListViewItemChoicePosition;
	}
	
	/**
	 * ϵͳĬ�ϵ�������ɫ��¼
	 */
	private int defaultFontColorInt=-1;
	public int getDefaultFontColorInt() {
		return defaultFontColorInt;
	}
	public void setDefaultFontColorInt(int defaultFontColorInt) {
		this.defaultFontColorInt = defaultFontColorInt;
	}
	
	
	
}
