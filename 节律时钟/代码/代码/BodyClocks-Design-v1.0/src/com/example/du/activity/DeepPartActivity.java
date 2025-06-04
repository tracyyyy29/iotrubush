package com.example.du.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xslf.model.geom.SqrtExpression;

import com.example.du.R;
import com.example.du.R.drawable;
import com.example.du.R.id;
import com.example.du.R.layout;
import com.example.du.R.menu;
import com.example.du.utils.ExcelUtil;
import com.hanheng.a53.beep.BeepClass;
import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.dotarray.DotArrayClass;
import com.hanheng.a53.dotarray.FontClass;
import com.hanheng.a53.dotarray.ViewClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.seg7.Seg7Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;

/**
 * Body IV Design Deep Part
 * 
 * @author <achdu0000@163.com>
 * 
 */

public class DeepPartActivity extends Activity implements OnClickListener {

	// 定义一些常量，方便调用

	// 注意控制蜂鸣器BEEP和LED等的电平信号有所差异
	private final static int BEEP_ON = 0;
	private final static int BEEP_OFF = 1;
	private final static int LED_ON = 1;
	private final static int LED_OFF = 0;

	private final static String STATE_NORMAL = "正常";// 设备状态正常的字符串
	private final static String STATE_WARNING = "警告";// 设备状态警告的字符串
	private final static String STATE_ERROR = "异常";// 设备状态异常的字符串

	private final static Bitmap.Config CONFIG = Bitmap.Config.ARGB_8888;

	private final static String[] CLOCK_HOUR_STRINGS = { "12", "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "10", "11" };
	private final static String[] SOLAR_TERM_STRINGS = { "春分", "清明", "谷雨",
			"立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露",
			"霜降", "立冬", "小雪", "大雪", "冬至", "小寒", "大寒", "立春", "雨水", "惊蛰" };

	private static final float ORIGIN_HIGHEST_HEART_PRESSURE = 120f; // 默认最高血压120
	private static final float ORIGIN_LOWEST_HEART_PRESSURE = 80f; // 默认最低血压80
	private static final float ORIGIN_BODY_TEMPERATURE = 36.5f; // 默认初始化体温
	private static final int ORIGIN_HEART_RATE = 75; // 初始化心率，默认为75%
	private static final float ORIGIN_BLOOD_OXYGEN = 98f; // 初始化血氧浓度，默认为98%
	private static final float ENVIRONMENT_TEMPERATURE = 20.1f; // 环境温度，这里当做常量模拟输入
	private static final float HIGEST_TEMPERATURE = 50; // 设备温度的最高阈值50℃，高于则报警
	private static final float LOWEST_TEMPERATURE = -10; // 设备温度的最低阈值-10℃，低于则报警
	private static final int ORIGIN_REMIND_POWER = 100; // 初始化剩余电量。默认为100%
	private static final int WARNING_POWER = 5; // 设备电量的提醒阈值，小于则警告

	private int DEFAULT_FONT_COLOR_INT; // 获取系统默认的字体颜色，由于涉及到字体颜色的变化，需要再变回去

	private boolean isStart; // 用于一些线程的控制
	private boolean isFZero; // 用于拨码开关的控制
	private boolean isBirthTimeOK; // 记录是否输入生日信息

	private LocalBroadcastManager localBroadcastManager; // 本地广播，用于接受生日的设置
	private IntentFilter intentFilter; // 用于广播的注册
	private BroadcastReceiver broadcastReceiver; // 用于广播的注册

	private int screenWidth = 0; // 屏幕宽度
	private int screenHeight = 0; // 屏幕高度
	private float largeClockSize = 0; // 中间大表盘的尺寸
	private float largePaddingSize = 0; // 中间大表盘的填充尺寸
	private float largeStandardPaintWidth = 0; // 中间大表盘的标准画笔宽度
	private float largeStandardPaintTextSize = 0; // 中间大表盘的标准文字尺寸
	private float smallClockSize = 0; // 四周小表盘的尺寸
	private float smallPaddingSize = 0; // 四周小表盘的填充尺寸
	private float smallStandardPaintWidth = 0; // 四周小表盘的标准画笔宽度
	private float smallStandardPaintTextSize = 0; // 四周小表盘的标准文字尺寸

	private Date currentDate; // 当前日期
	private String sunStandardDate; // 太阳标准时
	private String sunRealDate; // 太阳真时
	private double longitude; // 经度值
	private double latitude; // 纬度值
	private double altitude; // 海拔
	private String localTime; // 本地时间
	// private GanZhi localGanZhi; // 本地干支
	// private Season season; //当前季节
	private String sunRiseTime; // 日出时间
	private String sunSetTime; // 日落时间

	private Date birthTime; // 生日时间
	// private GanZhi birthGanZhi; // 生日干支

	private String meridian; // 当下开合经络
	private String acupoint; // 当下开合穴位

	private float bodyTemperature; // 体温
	private int heartRate; // 心率
	private float[] bloodPressure = { ORIGIN_HIGHEST_HEART_PRESSURE,
			ORIGIN_LOWEST_HEART_PRESSURE }; // 血压
	private float bloodOxygen; // 血氧浓度
	private String sportsTips; // 运动指示节律
	private String cognitionTips; // 认知指示节律

	private float watchTemperature; // 设备温度
	private int watchRemainPower; // 设备剩余电量
	private String watchWorkState; // 设备工作状态

	private Button exitBtn; // 退出按钮
	private Button simplestBtn; // 最简模式按钮
	private ImageView mySunClockImageView; // 天时表盘
	private ImageView myLocalClockImageView; // 地时表盘
	private ImageView myBirthClockImageView; // 人时表盘
	private ImageView myMeridianClockImageView; // 经络时表盘
	private ImageView myAcupointClockImageView; // 穴位时表盘

	private TextView watchTemperatureText; // 设备温度
	private TextView watchRemainPowerText; // 剩余电量
	private TextView watchWorkStateText; // 工作状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_part_activity);
		init();

		// 本地广播，用于接受生日的设置
		localBroadcastManager = LocalBroadcastManager.getInstance(this);
		intentFilter = new IntentFilter(
				"com.example.du.activity.DeepPartActivity.localbroadcast");
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action
						.equals("com.example.du.activity.DeepPartActivity.localbroadcast")) {
					long milliseconds = intent.getLongExtra("birthTime", 0);
					setBirthTimeAndGanZhi(milliseconds);
				}
			}
		};
		localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}

	private void setBirthTimeAndGanZhi(long milliseconds) {
		Log.e("ms", String.valueOf(milliseconds));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
	         // 执行数字8的操作
			watchTemperature++;
			updateTemperature();
			checkTemperature();
	         return true;
	     } 
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
	         // 执行数字2的操作
	    	 watchTemperature--;
	    	 updateTemperature();
	    	 checkTemperature();
	         return true;
	     }
		return super.onKeyDown(keyCode, event);
	}
	public void updateTemperature(){
		new Thread() {
			public void run() {
				updateSegText(watchTemperature);
				watchTemperatureText.setText(String.format("%.2f", watchTemperature));
			};
		}.start();
	}
	//
	public void checkTemperature(){
		new Thread() {
			public void run() {
				if(watchTemperature>HIGEST_TEMPERATURE||watchTemperature<LOWEST_TEMPERATURE){
					watchWorkStateText.setText(STATE_WARNING);
					BeepClass.IoctlRelay(BEEP_ON);
				}
				else{
					watchWorkStateText.setText(STATE_NORMAL);
					BeepClass.IoctlRelay(BEEP_OFF);
				}
			};
		}.start();
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_setbirthday:
			gotoSetBirthday();
			return true;
		case R.id.action_setPSI:
			gotoSetPSI();
			return true;
		case R.id.action_settings:
			return true;
		case R.id.action_about:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void gotoSetPSI() {

	}

	private void gotoSetBirthday() {
		Intent intent = new Intent(this, BirthInputActivity.class);
		startActivity(intent);
	}

	private void init() {
		// hardware
		DipClass.Init();
		BeepClass.Init();
		dipInit();
		LedClass.Init();
		Seg7Class.Init();
		ViewClass.getInstance();

		// software
		isStart = true;

		exitBtn = (Button) findViewById(R.id.exit_btn2);
		simplestBtn = (Button) findViewById(R.id.simplest_btn);

		watchTemperatureText = (TextView) findViewById(R.id.watchTemperature);
		watchRemainPowerText = (TextView) findViewById(R.id.watchRemainPower);
		watchWorkStateText = (TextView) findViewById(R.id.watchWorkState);

		mySunClockImageView = (ImageView) findViewById(R.id.my_sun_clock);
		myLocalClockImageView = (ImageView) findViewById(R.id.my_local_clock);
		myBirthClockImageView = (ImageView) findViewById(R.id.my_birth_clock);
		myMeridianClockImageView = (ImageView) findViewById(R.id.my_meridian_clock);
		myAcupointClockImageView = (ImageView) findViewById(R.id.my_acupoint_clock);

		bodyTemperature = ORIGIN_BODY_TEMPERATURE; // 体温初始化为初始体温
		heartRate = ORIGIN_HEART_RATE; // 心率初始化为初始心率
		// 血压已经初始化
		bloodOxygen = ORIGIN_BLOOD_OXYGEN; // 血氧初始化为初始血氧
		watchTemperature = ENVIRONMENT_TEMPERATURE; // 设备维度初始化为环境温度
		watchRemainPower = ORIGIN_REMIND_POWER; // 设备温度初始化为初始剩余电量

		watchTemperatureText.setText(String.format("%.2f", watchTemperature));
		watchRemainPowerText.setText(String.valueOf(watchRemainPower));
		watchWorkStateText.setText(STATE_NORMAL);
		DEFAULT_FONT_COLOR_INT = watchTemperatureText.getCurrentTextColor();

		getUI();

		exitBtn.setOnClickListener(this);
		simplestBtn.setOnClickListener(this);

	}

	private void dipInit() {

		if (DipClass.ReadValue() != 0) {
			isFZero = false;
			BeepClass.IoctlRelay(BEEP_ON); // 若不正确，beep声音提示同时触摸屏显示提示信息，等待调整正确
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					DeepPartActivity.this)
					.setTitle("警告！")
					.setMessage("拨码开关初始未置0，请检查调整确认无误后点击确定")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (DipClass.ReadValue() != 0) {
										dipInit();
									}
									isFZero = true;
									int BEEP_OFF = 1;
									BeepClass.IoctlRelay(BEEP_OFF); // 拨码开关位置调整正确后关闭蜂鸣器
								}
							});
			dialog.create();
			dialog.show();
		}

	}

	private void getUI() {
		getLocation();
		getSunStandardDate();
		getSunRealDate();
		getLocalTime();

		getHardwareUI();

		drawClocks();
	}

	private void getHardwareUI() {
		updateSegText(watchTemperature);
		updateDotShow();

	}

	private void updateDotShow() {
		ViewClass dot = ViewClass.getInstance();
		dot.clean();
		setDot(dot);
		dot.show();
	}

	private double pointDistance(double p1_x, double p1_y, double p2_x,
			double p2_y) {
		return Math.sqrt((p1_x - p2_x) * (p1_x - p2_x) + (p1_y - p2_y)
				* (p1_y - p2_y));
	}

	/**
	 * 
	 * @param dot
	 */
	private void setDot(ViewClass dot) {
		double c_x = 7.5, c_y = 7.5, c_r = 1, r = 6.5;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				double dis = pointDistance(c_x, c_y, i, j);
				if (dis < c_r) {
					dot.setPoint(i, j);
				} else if (dis >= (int) r && dis <= (int) r + 1) {
					dot.setPoint(i, j);
				}
			}
		}

	}

	private void updateSegText(final float f) {
		new Thread() {
			public void run() {
				Seg7Class.Seg7Show(f);
			};
		}.start();
	}

	private void getLocalTime() {
		SimpleDateFormat simepleDateFormat = new SimpleDateFormat("HH:mm:ss");
		localTime = simepleDateFormat.format(currentDate);

	}

	private void getSunRealDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		sunRealDate = simpleDateFormat.format(new ExcelUtil()
				.lookUp02(currentDate));
	}

	private void getSunStandardDate() {
		currentDate = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		sunStandardDate = simpleDateFormat.format(currentDate);
	}

	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

		@Override
		public void onProviderEnabled(String arg0) {
			getLocationInfo(null);
		}

		@Override
		public void onProviderDisabled(String arg0) {
			getLocationInfo(null);
		}

		@Override
		public void onLocationChanged(Location location) {
			getLocationInfo(location);
		}
	};

	private void getLocation() {
		/*
		 * LocationManager
		 * locationManager=(LocationManager)getSystemService(Context
		 * .LOCATION_SERVICE); String provider=LocationManager.GPS_PROVIDER; if
		 * (Activity.checkSelfPermission(this,
		 * AndroidManifest.permission.ACCESS_FINE_LOCATION) !=
		 * PackageManager.PERMISSION_GRANTED &&
		 * ActivityCompat.checkSelfPermission(this,
		 * Manifest.permission.ACCESS_COARSE_LOCATION) !=
		 * PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling //
		 * ActivityCompat#requestPermissions // here to request the missing
		 * permissions, and then overriding // public void
		 * onRequestPermissionsResult(int requestCode, String[] permissions, //
		 * int[] grantResults) // to handle the case where the user grants the
		 * permission. See the documentation // for
		 * ActivityCompat#requestPermissions for more details. return ; }
		 * Location location = locationManager.getLastKnownLocation(provider);
		 * getLocationInfo(location);
		 * locationManager.requestLocationUpdates(provider
		 * ,2000,100,locationListener);
		 */
		// 由于实验箱android环境限制，无法获取GPS定位，只能模拟
		getLocationInfo(null);
	}

	private void getLocationInfo(Location location) {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			double alt = location.getAltitude();

		} else {
			// Toast.makeText(this, "无法定位，请确保GPS开启！",
			// Toast.LENGTH_SHORT).show();
			// 由于实验箱android环境限制，无法获取GPS定位，只能模拟
			latitude = 38.9;
			longitude = 117.3;
		}

	}

	private void drawClocks() {

		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;

		int clockSize = screenHeight < screenWidth ? screenHeight : screenWidth;
		largeClockSize = (float) (0.7 * clockSize);
		largePaddingSize = (float) (0.05 * clockSize);
		largeStandardPaintWidth = (float) 5.0;
		largeStandardPaintTextSize = 20;
		smallClockSize = (float) (0.5 * largeClockSize);
		smallPaddingSize = (float) (largePaddingSize);
		smallStandardPaintWidth = (float) 2.5;
		smallStandardPaintTextSize = 15;

		mySunClockImageView.setImageBitmap(drawMySunClock());
		myLocalClockImageView.setImageBitmap(drawMyLocalClock());
		myBirthClockImageView.setImageBitmap(drawMyBirthClock());
		myMeridianClockImageView.setImageBitmap(drawMyMeridianClock());
		myAcupointClockImageView.setImageBitmap(drawMyAcupointClock());
	}

	private Bitmap drawMySunClock() {
		float clockWidth = smallClockSize;
		float clockHeight = smallClockSize;
		float clockPadding = smallPaddingSize;
		float paintWidth = smallStandardPaintWidth;
		float paintTextSize = smallStandardPaintTextSize;
		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(paintWidth); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 60; i++) {
			if (i % 5 == 0) {
				paintDegree.setStrokeWidth(paintWidth);
				canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
						(float) (clockPadding * 1.5), paintDegree);
			} else {
				paintDegree.setStrokeWidth(paintWidth / 2);
				canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
						(float) (clockPadding * 1.25), paintDegree);
			}
			canvas.rotate(6, clockWidth / 2, clockHeight / 2);
		}

		canvas.save();
		// 画文字

		Paint paintText = new Paint();
		paintText.setTextSize((float) (paintTextSize * 0.7));
		paintText.setAntiAlias(true);
		for (int i = 0; i < 24; i++) {
			if (i % 2 == 1) {
				paintText.setTypeface(Typeface.DEFAULT);
				String str = SOLAR_TERM_STRINGS[i];
				float offset = paintText.measureText(str) / 2;
				canvas.drawText(str, clockWidth / 2 - offset, clockPadding
						- offset / 2, paintText);
			} else {
				paintText.setTypeface(Typeface.DEFAULT_BOLD);
				String str1 = SOLAR_TERM_STRINGS[i];
				String str2 = CLOCK_HOUR_STRINGS[i / 2];
				float offset1 = paintText.measureText(str1) / 2;
				float offset2 = paintText.measureText(str2) / 2;
				canvas.drawText(str1, clockWidth / 2 - offset1, clockPadding
						- offset1 / 2, paintText);
				canvas.drawText(str2, clockWidth / 2 - offset2,
						(float) (clockPadding * 1.5 + offset1), paintText);
			}
			canvas.rotate(15, clockWidth / 2, clockHeight / 2);
		}
		canvas.restore();

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);

		/* 画指针 */
		// 时间
		Paint paintTimePointer = new Paint();
		paintTimePointer.setStrokeWidth(paintWidth);
		paintTimePointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0, clockHeight / 2 - 2 * clockPadding,
				paintTimePointer);

		// 节气
		Paint paintSolarTermPointer = new Paint();
		paintSolarTermPointer.setColor(Color.GRAY);
		paintTimePointer.setStrokeWidth(paintWidth / 2);
		paintTimePointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0,
				(float) (clockPadding * 0.4 - clockHeight / 2),
				paintSolarTermPointer);

		// 时间
		Paint paintRealSunTimePointer = new Paint();
		paintRealSunTimePointer.setColor(Color.RED);
		paintRealSunTimePointer.setStrokeWidth((float) (paintWidth * 1.5));
		paintRealSunTimePointer.setAntiAlias(true);
		canvas.drawLine(0, 0, (float) (clockWidth / 2 - 2.5 * clockPadding), 0,
				paintRealSunTimePointer);

		canvas.restore();

		canvas.save();
		paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "天", str2 = "时";
		canvas.drawText(str1,
				clockWidth / 2 - clockPadding / 2 - paintText.measureText(str1)
						/ 2, clockHeight / 2, paintText);
		canvas.drawText(str2,
				clockWidth / 2 + clockPadding / 2 - paintText.measureText(str2)
						/ 2, clockHeight / 2, paintText);

		canvas.restore();
		return bm;

	}

	private Bitmap drawMyLocalClock() {
		float clockWidth = smallClockSize;
		float clockHeight = smallClockSize;
		float clockPadding = smallPaddingSize;
		float paintWidth = smallStandardPaintWidth;
		float paintTextSize = smallStandardPaintTextSize;
		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(paintWidth); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 6; i++) {
			paintDegree.setStrokeWidth(paintWidth);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding * 2, paintDegree);
			canvas.rotate(60, clockWidth / 2, clockHeight / 2);
		}

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "地", str2 = "时";
		canvas.drawText(str1,
				clockWidth / 2 - clockPadding / 2 - paintText.measureText(str1)
						/ 2, clockHeight / 2, paintText);
		canvas.drawText(str2,
				clockWidth / 2 + clockPadding / 2 - paintText.measureText(str2)
						/ 2, clockHeight / 2, paintText);
		canvas.restore();

		return bm;
	}

	private Bitmap drawMyMeridianClock() {
		float clockWidth = smallClockSize;
		float clockHeight = smallClockSize;
		float clockPadding = smallPaddingSize;
		float paintWidth = smallStandardPaintWidth;
		float paintTextSize = smallStandardPaintTextSize;
		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(paintWidth); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 6; i++) {
			paintDegree.setStrokeWidth(paintWidth);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding * 2, paintDegree);
			canvas.rotate(60, clockWidth / 2, clockHeight / 2);
		}

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);

		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "经", str2 = "络", str3 = "时";
		canvas.drawText(str1,
				clockWidth / 2 - clockPadding / 2 - paintText.measureText(str1)
						/ 2, clockHeight / 2, paintText);
		canvas.drawText(str2, clockWidth / 2 - paintText.measureText(str2) / 2,
				clockHeight / 2 - paintText.measureText(str2), paintText);
		canvas.drawText(str3,
				clockWidth / 2 + clockPadding / 2 - paintText.measureText(str3)
						/ 2, clockHeight / 2, paintText);
		canvas.restore();

		return bm;
	}

	private Bitmap drawMyAcupointClock() {
		float clockWidth = smallClockSize;
		float clockHeight = smallClockSize;
		float clockPadding = smallPaddingSize;
		float paintWidth = smallStandardPaintWidth;
		float paintTextSize = smallStandardPaintTextSize;
		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(paintWidth); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 6; i++) {
			paintDegree.setStrokeWidth(paintWidth);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding * 2, paintDegree);
			canvas.rotate(60, clockWidth / 2, clockHeight / 2);
		}

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);

		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "穴", str2 = "位", str3 = "时";
		canvas.drawText(str1,
				clockWidth / 2 - clockPadding / 2 - paintText.measureText(str1)
						/ 2, clockHeight / 2, paintText);
		canvas.drawText(str2, clockWidth / 2 - paintText.measureText(str2) / 2,
				clockHeight / 2 - paintText.measureText(str2) / 2, paintText);
		canvas.drawText(str3,
				clockWidth / 2 + clockPadding / 2 - paintText.measureText(str3)
						/ 2, clockHeight / 2, paintText);
		canvas.restore();

		return bm;
	}

	private Bitmap drawMyBirthClock() {
		float clockWidth = largeClockSize;
		float clockHeight = largeClockSize;
		float clockPadding = largePaddingSize;
		float paintWidth = largeStandardPaintWidth;
		float paintTextSize = largeStandardPaintTextSize;

		// 画表盘
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(paintWidth); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// 画刻度
		Paint paintDegree = new Paint();
		paintDegree.setAntiAlias(true);
		for (int i = 0; i < 6; i++) {
			paintDegree.setStrokeWidth(paintWidth);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding * 2, paintDegree);
			canvas.rotate(60, clockWidth / 2, clockHeight / 2);
		}

		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "人", str2 = "时";
		canvas.drawText(
				str1,
				clockWidth / 2 - clockPadding - paintText.measureText(str1) / 2,
				clockHeight / 2, paintText);
		canvas.drawText(
				str2,
				clockWidth / 2 + clockPadding - paintText.measureText(str2) / 2,
				clockHeight / 2, paintText);
		canvas.restore();

		return bm;
	}

	@Override
	public void onClick(View arg0) {
		int key = arg0.getId();
		switch (key) {
		case R.id.exit_btn2:
			exit();
			break;
		case R.id.simplest_btn:
			break;
		default:
			break;
		}
	}

	private void exit() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				DeepPartActivity.this);
		dialog.setTitle("程序退出").setMessage("您确定要退出吗？")
				.setIcon(R.drawable.ic_launcher);
		dialog.setCancelable(false);
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				endThisPage();
			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dialog.show();
	}

	private void endThisPage() {
		isStart = false;
		localBroadcastManager.unregisterReceiver(broadcastReceiver);

		DipClass.Exit();
		BeepClass.Exit();
		DotArrayClass.Exit();
		LedClass.Exit();
		Seg7Class.Exit();
		this.finish(); // 操作结束
	}
}
