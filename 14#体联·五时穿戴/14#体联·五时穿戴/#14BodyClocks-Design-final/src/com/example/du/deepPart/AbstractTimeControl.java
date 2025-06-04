package com.example.du.deepPart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * Abstract Time Control for extends by SunTimeControl/LocalTimeControl/BirthTimeControl/MeridianTimeControl/AcupointTimeControl
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public abstract class AbstractTimeControl {
	protected AbstractTimeControl(){
		canvas=new Canvas();
		canvasHeight=0;
		canvasWidth=0;
		canvasPadding=0;
	}
	
	protected Context context;
	
	protected Canvas canvas;
	protected float canvasWidth;
	protected float canvasHeight;
	protected float canvasPadding;

	
	public void setCanvasSize(float canvasWidth,float canvasHeight,float canvasPadding) {
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.canvasPadding = canvasPadding;
	}

	public float getCanvasWidth() {
		return canvasWidth;
	}


	public float getCanvasHeight() {
		return canvasHeight;
	}

	public float getCanvasPadding() {
		return canvasPadding;
	}
	
	



	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 前端UI的更新
	 * @return
	 */
	public abstract Bitmap drawClock();
	/**
	 * 后端数据的更新
	 */
	public abstract void updateData();
}