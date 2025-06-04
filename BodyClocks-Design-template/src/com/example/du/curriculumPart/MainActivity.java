package com.example.du.curriculumPart;

import java.util.ArrayList;

import com.example.du15.R;
import com.hanheng.a53.beep.BeepClass;
import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.dotarray.DotArrayClass;
import com.hanheng.a53.dotarray.FontClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.seg7.Seg7Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Curriculum Part
 * 
 * basic hardware group order with: 1. 1 beep 2. 4 LEDs 3. 8 DIP switch 4. 1
 * seg7 (4 number) 5. 1 dot array (16*16)
 * 
 * basic software(Java) group order with: 1. toggle buttons 2. text views 3.
 * clock with point
 * 
 * to show different stages.
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	// 定义一些常量方便调用
	// 注意控制蜂鸣器BEEP和LED等的电平信号有所差异
	private final static int BEEP_ON = 0;
	private final static int BEEP_OFF = 1;
	private final static int LED_ON = 1;
	private final static int LED_OFF = 0;

	private final static int DIP_CLASS_READ_VALUE_MSG_FLAG = "DIP_CLASS_READ_VALUE_MSG_FLAG"
			.hashCode();

	private final static int MY_DEFAULT_PADDING = 50;
	private final static Bitmap.Config CONFIG = Bitmap.Config.ARGB_8888;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private final static String[] ORDER_READY = { "小组摘要", "组长未点名", "周志员未点名",
			"需求员未点名", " ", "优裁员未点名", "展示员未点名" };
	private final static String[] ORDER_OK = { "体联IVG17#", "组长孙致勉", "周志员陈恩宝",
			"需求员原神"," ", "优裁员贾程皓", "展示员张译仁" };
	private int clock_state = 0;
	private boolean thread_flag;

	private TextView[] text = new TextView[9];
	private ToggleButton[] tb = new ToggleButton[9];
	private Button exitBtn;
	private Button deepDesignBtn;
	private ImageView myClockImageView;
	private TextView[] textOrder = new TextView[7];
	private boolean isFZero;
	private Handler uiHandle = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == DIP_CLASS_READ_VALUE_MSG_FLAG)
				computed(msg.arg1);
		}
	};
	private int originalVal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.curriculum_part_activity);
		init();

	}

	private void init() {
		// hardware
		DipClass.Init();
		BeepClass.Init();
		isFZero = true;
		dipInit();
		LedClass.Init();
		Seg7Class.Init();
		FontClass.getInstance();

		// software
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;

		tb[0] = (ToggleButton) findViewById(R.id.toggleButton1);
		tb[1] = (ToggleButton) findViewById(R.id.toggleButton2);
		tb[2] = (ToggleButton) findViewById(R.id.toggleButton3);
		tb[3] = (ToggleButton) findViewById(R.id.toggleButton4);
		tb[4] = (ToggleButton) findViewById(R.id.toggleButton5);
		tb[5] = (ToggleButton) findViewById(R.id.toggleButton6);
		tb[6] = (ToggleButton) findViewById(R.id.toggleButton7);
		tb[7] = (ToggleButton) findViewById(R.id.toggleButton8);
		tb[8] = (ToggleButton) findViewById(R.id.toggleButton9);

		text[0] = (TextView) findViewById(R.id.textView1);
		text[1] = (TextView) findViewById(R.id.textView2);
		text[2] = (TextView) findViewById(R.id.textView3);
		text[3] = (TextView) findViewById(R.id.textView4);
		text[4] = (TextView) findViewById(R.id.textView5);
		text[5] = (TextView) findViewById(R.id.textView6);
		text[6] = (TextView) findViewById(R.id.textView7);
		text[7] = (TextView) findViewById(R.id.textView8);
		text[8] = (TextView) findViewById(R.id.textView9);

		exitBtn = (Button) findViewById(R.id.exit_btn1);
		deepDesignBtn = (Button) findViewById(R.id.deepDesign_btn);
		deepDesignBtn.setVisibility(View.INVISIBLE);

		textOrder[0] = (TextView) findViewById(R.id.groupInfoTextView);
		textOrder[1] = (TextView) findViewById(R.id.leaInfoTextView);
		textOrder[2] = (TextView) findViewById(R.id.logInfoTextView);
		textOrder[3] = (TextView) findViewById(R.id.reqInfoTextView);
		textOrder[4] = (TextView) findViewById(R.id.reqPlusInfoTextView);
		textOrder[5] = (TextView) findViewById(R.id.optInfoTextView);
		textOrder[6] = (TextView) findViewById(R.id.shoInfoTextView);

		myClockImageView = (ImageView) findViewById(R.id.my_clock);
		myClockImageView.setImageBitmap(drawMyClock());

		patternSwitch(0);

		thread_flag = true;
		openThread();

		exitBtn.setOnClickListener(this);
		deepDesignBtn.setOnClickListener(this);
		for (int i = 0; i < tb.length; i++) {
			this.tb[i].setOnClickListener(this);
		}

	}

	private Bitmap drawMyClock() {
		int clockPadding = MY_DEFAULT_PADDING;
		int clockWidth = screenHeight < screenWidth ? screenHeight
				: screenWidth;
		int clockHeight = clockWidth;

		float degree = (float) (360.0 / 7.0);
		float degree_last = 360 - 6 * degree;

		// 画表盘
		Bitmap bm = Bitmap.createBitmap(clockWidth, clockHeight, CONFIG);// 创建区域
		Canvas canvas = new Canvas(bm); // 设置画布
		Paint paintCircle = new Paint(); // 设置画笔画圆
		paintCircle.setStyle(Paint.Style.STROKE); // 画笔描边模式
		paintCircle.setStrokeWidth(5); // 设置描边宽度
		paintCircle.setAntiAlias(true); // 抗锯齿
		// 画外圆
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, clockWidth / 2
				- clockPadding, paintCircle);
		// 画圆心
		paintCircle.setStyle(Paint.Style.FILL);
		canvas.drawCircle(clockWidth / 2, clockHeight / 2, 5, paintCircle);

		// 画刻度…………………………………………………………………………………………………………………………………………………………得画六个
		Paint paintDegree = new Paint();
		for (int i = 0; i < 7; i++) {
			paintDegree.setStrokeWidth(5);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding + 30, paintDegree);
			if (i != 7)
				canvas.rotate(degree, clockWidth / 2, clockHeight / 2);
			else
				canvas.rotate(degree_last, clockWidth / 2, clockHeight / 2);

		}

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		ArrayList<String> stringList = new ArrayList<String>();
		for (int i = 0; i < 7; i++) {
			if (i < clock_state)
				stringList.add(ORDER_OK[i]);
			else
				stringList.add(ORDER_READY[i]);
		}
		int ok_num = clock_state;
		for (int i = 0; i < 7; i++) {
			String str = stringList.get(i);
			paintText.setColor(i < ok_num ? Color.GREEN : Color.RED);
			canvas.drawText(str, clockWidth / 2 - paintText.measureText(str)
					/ 2, clockPadding + 60, paintText);
			if (i != 7)
				canvas.rotate(degree, clockWidth / 2, clockHeight / 2);
			else
				canvas.rotate(degree_last, clockWidth / 2, clockHeight / 2);

		}
		canvas.restore();
		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(5);
		for (int i = 0; i < clock_state; i++) {
			if (i != clock_state - 1)
				canvas.rotate(degree, 0, 0);
			else
				canvas.rotate(degree_last, 0, 0);
		}
		canvas.drawLine(0, 0, 0, -150, paintPointer);
		canvas.restore();
		return bm;
	}

	protected void computed(int val) {
		int diff = val - originalVal; // 计算差值
		if (diff < 0) {
			diff = -diff;
			switch (diff) {
			case (1):
				changeState(0, 0);
				break;
			case (2):
				changeState(1, 0);
				break;
			case (4):
				changeState(2, 0);
				break;
			case (8):
				changeState(3, 0);
				break;
			case (16):
				changeState(4, 0);
				break;
			case (32):
				changeState(5, 0);
				break;
			case (64):
				changeState(6, 0);
				break;
			case (128):
				changeState(7, 0);
				break;
			}
		} else {
			switch (diff) {
			case (1):
				changeState(0, 1);
				break;
			case (2):
				changeState(1, 1);
				break;
			case (4):
				changeState(2, 1);
				break;
			case (8):
				changeState(3, 1);
				break;
			case (16):
				changeState(4, 1);
				break;
			case (32):
				changeState(5, 1);
				break;
			case (64):
				changeState(6, 1);
				break;
			case (128):
				changeState(7, 1);
				break;
			}
		}

		this.originalVal = val;
	}

	private void changeState(int i, int tag) {
		if (!isFZero)
			return;
		if (tag == 1) {
			switch (i) {
			case 0:
				patternSwitch(1); // stage 1：点名阶段·小组摘要
				tb[0].setChecked(true);
				show_group_number_open();
				break;
			case 1:
				tb[1].setChecked(true);
				show_first_name_lea_open();
				break;
			case 2:
				patternSwitch(2);
				tb[2].setChecked(true);
				show_first_name_log_open();
				break;
			case 3:
				tb[3].setChecked(true);
				show_first_name_req_open();
				break;
			case 4:
				tb[4].setChecked(true);
				show_first_name_req_plus_open();
				break;
			case 5:
				tb[5].setChecked(true);
				show_first_name_opt_open();
				break;
			case 6:
				tb[6].setChecked(true);
				show_first_name_sho_open();
				break;
			case 7:
				tb[7].setChecked(true);
				patternSwitch(3);
				tb[8].setChecked(true);
				patternSwitch(4);
				break;
			default:
				break;
			}
		} else {
			switch (i) {
			case 0:
				tb[0].setChecked(false);
				show_group_number_close();
				break;
			case 1:
				tb[1].setChecked(false);
				show_first_name_lea_close();
				break;
			case 2:
				tb[2].setChecked(false);
				show_first_name_log_close();
				break;
			case 3:
				tb[3].setChecked(false);
				show_first_name_req_close();
				break;
			case 4:
				tb[4].setChecked(false);
				show_first_name_req_plus_close();
				break;
			case 5:
				tb[5].setChecked(false);
				show_first_name_opt_close();
				break;
			case 6:
				tb[6].setChecked(false);
				show_first_name_sho_close();
				break;
			case 7:
				tb[7].setChecked(false);
				tb[8].setChecked(false);
				break;
			default:
				break;
			}
		}
	}

	private void updateText(final float f) {
		new Thread() {
			public void run() {
				Seg7Class.Seg7Show(f);
			};
		}.start();
	}

	private void show_group_number_open() {

		updateText((float) 0.015);
		textOrder[0].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[0].setText("体联IV——17#");
		String str = "十七";
		FontClass.getInstance().setContent(str, this.getAssets());

		updateClock(1);

	}

	private void show_group_number_close() {
		updateText(0);
	}

	private void show_first_name_lea_open() {
		updateText((float) 1230);
		textOrder[1].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[1].setText("组    长已点名");
		String str = "孙";
		FontClass.getInstance().setContent(str, this.getAssets());
		beepControl_Sun();
		updateClock(2);

	}

	private void show_first_name_lea_close() {
		updateText(0);
	}

	private void show_first_name_log_open() {
		updateText((float) 1303);
		textOrder[2].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[2].setText("周志员已点名");
		String str = "原";
		FontClass.getInstance().setContent(str, this.getAssets());
		beepControl_Yuan();
		updateClock(3);
	}

	private void show_first_name_log_close() {
		updateText(0);
	}

	private void show_first_name_req_open() {
		updateText((float) 1232);
		textOrder[3].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[3].setText("需求员已点名");
		String str = "陈";
		FontClass.getInstance().setContent(str, this.getAssets());
		beepControl_Chen();
		updateClock(4);
	}

	private void show_first_name_req_close() {
		updateText(0);
	}

	private void show_first_name_req_plus_open() {
		//updateText((float) 1133);
		//textOrder[4].setBackgroundColor(Color.rgb(0, 255, 0));
		//textOrder[4].setText("需求员+已点名");
		String str = "李";
		//FontClass.getInstance().setContent(str, this.getAssets());
		//beepControl(2);
		updateClock(5);
	}

	private void show_first_name_req_plus_close() {
		updateText(0);
	}

	private void show_first_name_opt_open() {
		updateText((float) 2323);
		textOrder[5].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[5].setText("优裁员已点名");
		String str = "贾";
		FontClass.getInstance().setContent(str, this.getAssets());
		beepControl_Jia();
		updateClock(6);
	}

	private void show_first_name_opt_close() {
		updateText(0);
	}

	private void show_first_name_sho_open() {
		updateText((float) 2323);
		textOrder[6].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[6].setText("展示员已点名");
		String str = "张";
		FontClass.getInstance().setContent(str, this.getAssets());
		beepControl_Zhang();
		updateClock(7);
	}

	private void updateClock(int state) {
		if (state <= 7) {
			clock_state = state;
			myClockImageView.setImageBitmap(drawMyClock());
		}
	}

	private void show_first_name_sho_close() {
		updateText(0);
	}

	private void openThread() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (thread_flag) {
					Message msg = new Message();
					int value = DipClass.ReadValue();
					msg.what = DIP_CLASS_READ_VALUE_MSG_FLAG;
					msg.arg1 = value;
					uiHandle.sendMessage(msg);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void patternSwitch(int pattern) {
		ledAllOff();
		switch (pattern) {
		case 0: // stage0：启动阶段 0001B
			LedClass.IoctlLed(3, LED_ON);
			beginShow();

			break;
		case 1: // stage1：点名阶段·小组摘要 0010B
			LedClass.IoctlLed(2, LED_ON);
			break;
		case 2: // stage1：点名阶段·组员点名 0100B
			LedClass.IoctlLed(1, LED_ON);
			break;
		case 3: // 浅度课设阶段结束
			endGroupOrder();
			LedClass.IoctlLed(0, LED_ON); // stage2：深度课设阶段 1000B
			break;
		case 4: // 进入深度课设阶段:
			showBtnForDeepDesign();
			break;
		default:
			break;
		}
	}

	private void beginShow() {
		updateText((float) 0.001);
		String str = "浅";
		FontClass.getInstance().setContent(str, this.getAssets());
	}

	private void showBtnForDeepDesign() {
		Toast.makeText(this, "请点击左下角按钮进入深度课设部分", Toast.LENGTH_SHORT).show();
		deepDesignBtn.setVisibility(View.VISIBLE);
	}

	private void endGroupOrder() {
		beepControl(3); // 三声短促的蜂鸣器鸣响“滴滴滴”，表示所有点名结束
		updateText(1000);
		String str = "深";
		FontClass.getInstance().setContent(str, this.getAssets());
	}

	private void beepControl(int number) {
		try {
			for (int i = 0; i < number; i++) {
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(200);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void beepControl_Shiqi(){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		int chen[]={1,3,3,3,3,3,1,1,1};
		try {
			for(int i = 0 ; i < chen.length; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(300*chen[i]);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(300);
			}
			} catch (InterruptedException e) { }
	}
	
	public void beepControl_Chen(){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		int chen[]={3,1,3,1};
		try {
			for(int i = 0 ; i < chen.length; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(300*chen[i]);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(300);
			}
			} catch (InterruptedException e) { }
	}
	public void beepControl_Sun(){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		int chen[]={1,1,1};
		try {
			for(int i = 0 ; i < chen.length; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(300*chen[i]);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(300);
			}
			} catch (InterruptedException e) { }
	}
	public void beepControl_Yuan(){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		int chen[]={3,1,3,3};
		try {
			for(int i = 0 ; i < chen.length; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(300*chen[i]);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(300);
			}
			} catch (InterruptedException e) { }
	}
	
	public void beepControl_Jia(){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		int chen[]={1,3,3,3};
		try {
			for(int i = 0 ; i < chen.length; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(300*chen[i]);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(300);
			}
			} catch (InterruptedException e) { }
	}
	
	public void beepControl_Zhang(){																//根据参数number控制蜂鸣器鸣响次数
		int BEEP_ON = 0;
		int BEEP_OFF = 1;
		int chen[]={3,3,1,1};
		try {
			for(int i = 0 ; i < chen.length; i++)
			{
				BeepClass.IoctlRelay(BEEP_ON);
				Thread.sleep(300*chen[i]);
				BeepClass.IoctlRelay(BEEP_OFF);
				Thread.sleep(300);
			}
			} catch (InterruptedException e) { }
	}
	private void ledAllOff() {
		LedClass.IoctlLed(0, LED_OFF);
		LedClass.IoctlLed(1, LED_OFF);
		LedClass.IoctlLed(2, LED_OFF);
		LedClass.IoctlLed(3, LED_OFF);
	}

	private void dipInit() {
		if (DipClass.ReadValue() != 0) {
			isFZero = false;
			BeepClass.IoctlRelay(BEEP_ON); // 若不正确，beep声音提示同时触摸屏显示提示信息，等待调整正确
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this)
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

	@Override
	public void onClick(View arg0) {
		int key = arg0.getId();
		switch (key) {
		case R.id.exit_btn1:
			exit();
			break;
		case R.id.deepDesign_btn:
			gotoDeepDesign();
			break;
		case R.id.toggleButton1:
			if (tb[0].isChecked()) {
				patternSwitch(1); // stage 1：点名阶段·小组摘要
				show_group_number_open(); // 四个LED灯代表的四位bit显示为0010B，代表进入点名阶段的小组摘要部分
			} else {
				show_group_number_close();
			}
			break;
		case R.id.toggleButton2:
			if (tb[1].isChecked()) {
				show_first_name_lea_open();
			} else {
				show_first_name_lea_close();
			}
			break;
		case R.id.toggleButton3:
			if (tb[2].isChecked()) {
				patternSwitch(2); // stage 1：点名阶段·组员点名
				show_first_name_log_open(); // 四个LED灯代表的四位bit显示为0100B，代表进入点名阶段的组员点名部分
			} else {
				show_first_name_log_close();
			}
			break;
		case R.id.toggleButton4:
			if (tb[3].isChecked()) {
				show_first_name_req_open();
			} else {
				show_first_name_req_close();
			}
			break;
		case R.id.toggleButton5:
			if (tb[4].isChecked()) {
				show_first_name_req_plus_open();
			} else {
				show_first_name_req_plus_close();
			}
			break;
		case R.id.toggleButton6:
			if (tb[5].isChecked()) {
				show_first_name_opt_open();
			} else {
				show_first_name_opt_close();
			}
			break;
		case R.id.toggleButton7:
			if (tb[6].isChecked()) {
				show_first_name_sho_open();
			} else {
				show_first_name_sho_close();
			}
			break;
		case R.id.toggleButton8:
			if (tb[7].isChecked()) {
				patternSwitch(3); // stage 2：深度课设阶段
			} else {
			}
			break;
		case R.id.toggleButton9:
			if (tb[8].isChecked()) {
				patternSwitch(4);
			} else {
			}
			break;
		default:
			break;
		}
	}

	private void gotoDeepDesign() {
		Intent intent = new Intent(this,
				com.example.du.deepPart.LoginActivity.class);
		startActivity(intent);
		endThisPage();
	}

	private void exit() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
		thread_flag = false;
		DipClass.Exit();
		BeepClass.Exit();
		DotArrayClass.Exit();
		LedClass.Exit();
		Seg7Class.Exit();
		this.finish(); // 操作结束
	}
}
