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

	// ����һЩ�������������

	// ע����Ʒ�����BEEP��LED�ȵĵ�ƽ�ź���������
	private final static int BEEP_ON = 0;
	private final static int BEEP_OFF = 1;
	private final static int LED_ON = 1;
	private final static int LED_OFF = 0;

	private final static String STATE_NORMAL = "����";// �豸״̬�������ַ���
	private final static String STATE_WARNING = "����";// �豸״̬������ַ���
	private final static String STATE_ERROR = "�쳣";// �豸״̬�쳣���ַ���

	private final static Bitmap.Config CONFIG = Bitmap.Config.ARGB_8888;

	private final static String[] CLOCK_HOUR_STRINGS = { "12", "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "10", "11" };
	private final static String[] SOLAR_TERM_STRINGS = { "����", "����", "����",
			"����", "С��", "â��", "����", "С��", "����", "����", "����", "��¶", "���", "��¶",
			"˪��", "����", "Сѩ", "��ѩ", "����", "С��", "��", "����", "��ˮ", "����" };

	private static final float ORIGIN_HIGHEST_HEART_PRESSURE = 120f; // Ĭ�����Ѫѹ120
	private static final float ORIGIN_LOWEST_HEART_PRESSURE = 80f; // Ĭ�����Ѫѹ80
	private static final float ORIGIN_BODY_TEMPERATURE = 36.5f; // Ĭ�ϳ�ʼ������
	private static final int ORIGIN_HEART_RATE = 75; // ��ʼ�����ʣ�Ĭ��Ϊ75%
	private static final float ORIGIN_BLOOD_OXYGEN = 98f; // ��ʼ��Ѫ��Ũ�ȣ�Ĭ��Ϊ98%
	private static final float ENVIRONMENT_TEMPERATURE = 20.1f; // �����¶ȣ����ﵱ������ģ������
	private static final float HIGEST_TEMPERATURE = 50; // �豸�¶ȵ������ֵ50�棬�����򱨾�
	private static final float LOWEST_TEMPERATURE = -10; // �豸�¶ȵ������ֵ-10�棬�����򱨾�
	private static final int ORIGIN_REMIND_POWER = 100; // ��ʼ��ʣ�������Ĭ��Ϊ100%
	private static final int WARNING_POWER = 5; // �豸������������ֵ��С���򾯸�

	private int DEFAULT_FONT_COLOR_INT; // ��ȡϵͳĬ�ϵ�������ɫ�������漰��������ɫ�ı仯����Ҫ�ٱ��ȥ

	private boolean isStart; // ����һЩ�̵߳Ŀ���
	private boolean isFZero; // ���ڲ��뿪�صĿ���
	private boolean isBirthTimeOK; // ��¼�Ƿ�����������Ϣ

	private LocalBroadcastManager localBroadcastManager; // ���ع㲥�����ڽ������յ�����
	private IntentFilter intentFilter; // ���ڹ㲥��ע��
	private BroadcastReceiver broadcastReceiver; // ���ڹ㲥��ע��

	private int screenWidth = 0; // ��Ļ���
	private int screenHeight = 0; // ��Ļ�߶�
	private float largeClockSize = 0; // �м����̵ĳߴ�
	private float largePaddingSize = 0; // �м����̵����ߴ�
	private float largeStandardPaintWidth = 0; // �м����̵ı�׼���ʿ��
	private float largeStandardPaintTextSize = 0; // �м����̵ı�׼���ֳߴ�
	private float smallClockSize = 0; // ����С���̵ĳߴ�
	private float smallPaddingSize = 0; // ����С���̵����ߴ�
	private float smallStandardPaintWidth = 0; // ����С���̵ı�׼���ʿ��
	private float smallStandardPaintTextSize = 0; // ����С���̵ı�׼���ֳߴ�

	private Date currentDate; // ��ǰ����
	private String sunStandardDate; // ̫����׼ʱ
	private String sunRealDate; // ̫����ʱ
	private double longitude; // ����ֵ
	private double latitude; // γ��ֵ
	private double altitude; // ����
	private String localTime; // ����ʱ��
	// private GanZhi localGanZhi; // ���ظ�֧
	// private Season season; //��ǰ����
	private String sunRiseTime; // �ճ�ʱ��
	private String sunSetTime; // ����ʱ��

	private Date birthTime; // ����ʱ��
	// private GanZhi birthGanZhi; // ���ո�֧

	private String meridian; // ���¿��Ͼ���
	private String acupoint; // ���¿���Ѩλ

	private float bodyTemperature; // ����
	private int heartRate; // ����
	private float[] bloodPressure = { ORIGIN_HIGHEST_HEART_PRESSURE,
			ORIGIN_LOWEST_HEART_PRESSURE }; // Ѫѹ
	private float bloodOxygen; // Ѫ��Ũ��
	private String sportsTips; // �˶�ָʾ����
	private String cognitionTips; // ��ָ֪ʾ����

	private float watchTemperature; // �豸�¶�
	private int watchRemainPower; // �豸ʣ�����
	private String watchWorkState; // �豸����״̬

	private Button exitBtn; // �˳���ť
	private Button simplestBtn; // ���ģʽ��ť
	private ImageView mySunClockImageView; // ��ʱ����
	private ImageView myLocalClockImageView; // ��ʱ����
	private ImageView myBirthClockImageView; // ��ʱ����
	private ImageView myMeridianClockImageView; // ����ʱ����
	private ImageView myAcupointClockImageView; // Ѩλʱ����

	private TextView watchTemperatureText; // �豸�¶�
	private TextView watchRemainPowerText; // ʣ�����
	private TextView watchWorkStateText; // ����״̬

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_part_activity);
		init();

		// ���ع㲥�����ڽ������յ�����
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
	         // ִ������8�Ĳ���
			watchTemperature++;
			updateTemperature();
			checkTemperature();
	         return true;
	     } 
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
	         // ִ������2�Ĳ���
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

		bodyTemperature = ORIGIN_BODY_TEMPERATURE; // ���³�ʼ��Ϊ��ʼ����
		heartRate = ORIGIN_HEART_RATE; // ���ʳ�ʼ��Ϊ��ʼ����
		// Ѫѹ�Ѿ���ʼ��
		bloodOxygen = ORIGIN_BLOOD_OXYGEN; // Ѫ����ʼ��Ϊ��ʼѪ��
		watchTemperature = ENVIRONMENT_TEMPERATURE; // �豸ά�ȳ�ʼ��Ϊ�����¶�
		watchRemainPower = ORIGIN_REMIND_POWER; // �豸�¶ȳ�ʼ��Ϊ��ʼʣ�����

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
			BeepClass.IoctlRelay(BEEP_ON); // ������ȷ��beep������ʾͬʱ��������ʾ��ʾ��Ϣ���ȴ�������ȷ
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					DeepPartActivity.this)
					.setTitle("���棡")
					.setMessage("���뿪�س�ʼδ��0���������ȷ���������ȷ��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (DipClass.ReadValue() != 0) {
										dipInit();
									}
									isFZero = true;
									int BEEP_OFF = 1;
									BeepClass.IoctlRelay(BEEP_OFF); // ���뿪��λ�õ�����ȷ��رշ�����
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
		// ����ʵ����android�������ƣ��޷���ȡGPS��λ��ֻ��ģ��
		getLocationInfo(null);
	}

	private void getLocationInfo(Location location) {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			double alt = location.getAltitude();

		} else {
			// Toast.makeText(this, "�޷���λ����ȷ��GPS������",
			// Toast.LENGTH_SHORT).show();
			// ����ʵ����android�������ƣ��޷���ȡGPS��λ��ֻ��ģ��
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
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(paintWidth); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// ���̶�
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
		// ������

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

		/* ��ָ�� */
		// ʱ��
		Paint paintTimePointer = new Paint();
		paintTimePointer.setStrokeWidth(paintWidth);
		paintTimePointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0, clockHeight / 2 - 2 * clockPadding,
				paintTimePointer);

		// ����
		Paint paintSolarTermPointer = new Paint();
		paintSolarTermPointer.setColor(Color.GRAY);
		paintTimePointer.setStrokeWidth(paintWidth / 2);
		paintTimePointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0,
				(float) (clockPadding * 0.4 - clockHeight / 2),
				paintSolarTermPointer);

		// ʱ��
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
		String str1 = "��", str2 = "ʱ";
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
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(paintWidth); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// ���̶�
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
		/* ��ָ�� */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// ������
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "��", str2 = "ʱ";
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
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(paintWidth); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// ���̶�
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
		/* ��ָ�� */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);

		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// ������
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "��", str2 = "��", str3 = "ʱ";
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
		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(paintWidth); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// ���̶�
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
		/* ��ָ�� */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);

		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// ������
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
		paintText.setAntiAlias(true);
		String str1 = "Ѩ", str2 = "λ", str3 = "ʱ";
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

		// ������
		Bitmap bm = Bitmap.createBitmap((int) clockWidth, (int) clockHeight,
				CONFIG);// ��������
		Canvas canvas = new Canvas(bm); // ���û���
		Paint paintCircle = new Paint(); // ���û��ʻ�Բ
		paintCircle.setStyle(Paint.Style.STROKE); // �������ģʽ
		paintCircle.setStrokeWidth(paintWidth); // ������߿��
		paintCircle.setAntiAlias(true); // �����
		// ����Բ
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// ��Բ��
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, paintWidth,
				paintCircle);

		// ���̶�
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
		/* ��ָ�� */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(paintWidth);
		paintPointer.setAntiAlias(true);
		canvas.drawLine(0, 0, 0, clockWidth / 2 - 3 * clockPadding,
				paintPointer);
		canvas.restore();

		// ������
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(paintTextSize);
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
		dialog.setTitle("�����˳�").setMessage("��ȷ��Ҫ�˳���")
				.setIcon(R.drawable.ic_launcher);
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				endThisPage();
			}
		});

		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
		this.finish(); // ��������
	}
}
