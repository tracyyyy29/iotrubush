package com.hanheng.a53.dotarray;

import java.util.Arrays;

public class ViewClass implements Runnable {
	private byte[] byteArr = new byte[32];
	private int showTime;

	private static ViewClass instance = new ViewClass();

	private ViewClass() {
		this.setShowTime(3);
	}

	public void setShowTime(int showTime) {
		Arrays.fill(byteArr, (byte) 0);
		this.showTime = showTime;
	}

	public static ViewClass getInstance() {
		if (instance == null) {
			instance = new ViewClass();
		}
		int err = DotArrayClass.Init();
		return instance;
	}

	public byte[] getByteArray() {
		return byteArr;
	}

	@Override
	public void run() {
		new Thread() {
			public void run() {
				DotArrayClass.DotShow(byteArr);
			};
		}.start();
		try {
			Thread.sleep(this.showTime * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void clean() {
		Arrays.fill(byteArr, (byte) 0x00);
	}

	public void fill() {
		Arrays.fill(byteArr, (byte) 0xff);
	}

	public void setPoint(int x, int y) {
		setPoint(x, y, true);
	}

	public void setPoint(int x, int y, boolean state) {
		if(x<0||x>15||y<0||y>15)return;
		int index=x*2+(y<8?0:1);
		y%=8;
		byte t=(byte) 0x01;
		t<<=y;
		if(state){
			byteArr[index]|=t;
		}else{
			t=(byte) ~t;
			byteArr[index]&=t;
		}
	}
	
	public void show()
	{
		new Thread(this).start();
	}
	
	public void exit()
	{
		DotArrayClass.Exit();
	}
	
	@Override
	public String toString() {
		String str="";
		for (int i = 0; i < byteArr.length; i++) {
			str+=String.format("%02x ", byteArr[i]);
		}
		return str;
	}
}