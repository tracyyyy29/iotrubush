package com.example.du.activity;

import java.util.ArrayList;

import com.example.du.R;
import com.example.du.R.drawable;
import com.example.du.R.id;
import com.example.du.R.layout;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Body IV Design Curriculum Part
 * 
 * @author <achdu0000@163.com>
 * 
 */

public class CurriculumPartActivity extends Activity implements OnClickListener {

	// 定义一些常量方便调用
	// 注意控制蜂鸣器BEEP和LED等的电平信号有所差异
	private final static int BEEP_ON = 0;
	private final static int BEEP_OFF = 1;
	private final static int LED_ON = 1;
	private final static int LED_OFF = 0;

	protected final static int DIP_CLASS_READ_VALUE_MSG_FLAG = 0;

	private final static int MY_DEFAULT_PADDING = 50;
	private final static Bitmap.Config CONFIG = Bitmap.Config.ARGB_8888;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private final static String[] ORDER_READY = { "小组摘要", "组长未点名", "周志员未点名", "需求员未点名",
			"优裁员未点名", "展示员未点名" };
	private final static String[] ORDER_OK = { "体联IVXX#", "组长张三", "周志员XXX", "需求员XXX",
			"优裁员XXX", "展示员XXX" };
	private int clock_state = 0;
	private boolean thread_flag;

	private TextView[] text = new TextView[8];
	private ToggleButton[] tb = new ToggleButton[8];
	private Button exitBtn;
	private Button deepDesignBtn;
	private ImageView myClockImageView;
	private TextView[] textOrder = new TextView[6];
	private boolean isFZero;
	private Handler uiHandle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DIP_CLASS_READ_VALUE_MSG_FLAG:
				computed(msg.arg1);
				break;
			default:
				break;
			}
		};
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
		text[0] = (TextView) findViewById(R.id.textView1);
		text[1] = (TextView) findViewById(R.id.textView2);
		text[2] = (TextView) findViewById(R.id.textView3);
		text[3] = (TextView) findViewById(R.id.textView4);
		text[4] = (TextView) findViewById(R.id.textView5);
		text[5] = (TextView) findViewById(R.id.textView6);
		text[6] = (TextView) findViewById(R.id.textView7);
		text[7] = (TextView) findViewById(R.id.textView8);
		exitBtn = (Button) findViewById(R.id.exit_btn1);
		deepDesignBtn=(Button)findViewById(R.id.deepDesign_btn);
		deepDesignBtn.setVisibility(View.INVISIBLE);

		textOrder[0] = (TextView) findViewById(R.id.groupInfoTextView);
		textOrder[1] = (TextView) findViewById(R.id.leaInfoTextView);
		textOrder[2] = (TextView) findViewById(R.id.logInfoTextView);
		textOrder[3] = (TextView) findViewById(R.id.reqInfoTextView);
		textOrder[4] = (TextView) findViewById(R.id.optInfoTextView);
		textOrder[5] = (TextView) findViewById(R.id.shoInfoTextView);

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

		// 画刻度
		Paint paintDegree = new Paint();
		for (int i = 0; i < 6; i++) {
			paintDegree.setStrokeWidth(5);
			canvas.drawLine(clockWidth / 2, clockPadding, clockWidth / 2,
					clockPadding + 30, paintDegree);
			canvas.rotate(60, clockWidth / 2, clockHeight / 2);
		}

		// 画文字
		canvas.save();
		Paint paintText = new Paint();
		paintText.setTextSize(20);
		ArrayList<String> stringList = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			if (i < clock_state)
				stringList.add(ORDER_OK[i]);
			else
				stringList.add(ORDER_READY[i]);
		}
		int ok_num = clock_state;
		for (int i = 0; i < 6; i++) {
			String str = stringList.get(i);
			paintText.setColor(i < ok_num ? Color.GREEN : Color.RED);
			canvas.drawText(str, clockWidth / 2 - paintText.measureText(str)
					/ 2, clockPadding + 60, paintText);
			canvas.rotate(60, clockWidth / 2, clockHeight / 2);

		}
		canvas.restore();
		canvas.save();
		canvas.translate(clockWidth / 2, clockHeight / 2);
		/* 画指针 */
		Paint paintPointer = new Paint();
		paintPointer.setStrokeWidth(5);
		for (int i = 0; i < clock_state; i++) {
			canvas.rotate(60, 0, 0);
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
				show_first_name_reg_open();
				break;
			case 4:
				tb[4].setChecked(true);
				show_first_name_opt_open();
				break;
			case 5:
				tb[5].setChecked(true);
				show_first_name_sho_open();
				break;
			case 6:
				tb[6].setChecked(true);
				patternSwitch(3);
				break;
			case 7:
				tb[7].setChecked(true);
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
				show_first_name_reg_close();
				break;
			case 4:
				tb[4].setChecked(false);
				show_first_name_opt_close();
				break;
			case 5:
				tb[5].setChecked(false);
				show_first_name_sho_close();
				break;
			case 6:
				tb[6].setChecked(false);
				break;
			case 7:
				tb[7].setChecked(false);
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

		updateText((float) 0032);
		textOrder[0].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[0].setText("体联IV——14#");
		String str = "十四";
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());

		updateClock(1);

	}

	private void show_group_number_close() {
		updateText(0);
	}

	private void show_first_name_lea_open() {
		updateText((float) 1321);
		textOrder[1].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[1].setText("组    长已点名");
		String str = "杨";
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());
		beepControl(2);
		updateClock(2);

	}

	private void show_first_name_lea_close() {
		updateText(0);
	}

	private void show_first_name_log_open() {
		updateText((float) 1220);
		textOrder[2].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[2].setText("周志员已点名");
		String str = "黄";
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());
		beepControl(2);
		updateClock(3);
	}

	private void show_first_name_log_close() {
		updateText(0);
	}

	private void show_first_name_reg_open() {
		updateText((float) 1213);
		textOrder[3].setBackgroundColor(Color.rgb(0, 255, 0));
		textOrder[3].setText("需求员已点名");
		String str = "郭";
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());
		beepControl(2);
		updateClock(4);
	}

	private void show_first_name_reg_close() {
		updateText(0);
	}

	private void show_first_name_opt_open() {
		updateText((float) 1321); 
		textOrder[4].setBackgroundColor(Color.rgb(0, 255, 0)); 
		textOrder[4].setText("优裁员已点名");
		String str = "于"; 
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());
		beepControl(2);
		updateClock(5);
	}

	private void show_first_name_opt_close() {
		updateText(0);
	}

	private void show_first_name_sho_open() {
		updateText((float) 1222); 
		textOrder[5].setBackgroundColor(Color.rgb(0, 255, 0)); 
		textOrder[5].setText("展示员已点名");
		String str = "金"; 
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());
		beepControl(2);
		updateClock(6);
	}

	private void updateClock(int state) {
		if (state <= 6) {
			clock_state=state;
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
		case 4: //进入深度课设阶段:
			showBtnForDeepDesign();
			break;
		default:
			break;
		}
	}

	private void beginShow() {
		updateText((float) 0.001);
		String str="浅";
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());		
	}

	private void showBtnForDeepDesign() {
		Toast.makeText(this, "请点击左下角按钮进入深度课设部分",Toast.LENGTH_SHORT).show();
		deepDesignBtn.setVisibility(View.VISIBLE);
	}

	private void endGroupOrder() {
		beepControl(3); // 三声短促的蜂鸣器鸣响“滴滴滴”，表示所有点名结束
		updateText(1000);
		String str = "深";
		byte[][] data = FontClass.getInstance().setContent(str,
				this.getAssets());
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
					CurriculumPartActivity.this)
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
				show_first_name_reg_open();
			} else {
				show_first_name_reg_close();
			}
			break;
		case R.id.toggleButton5:
			if (tb[4].isChecked()) {
				show_first_name_opt_open();
			} else {
				show_first_name_opt_close();
			}
			break;
		case R.id.toggleButton6:
			if (tb[5].isChecked()) {
				show_first_name_sho_open();
			} else {
				show_first_name_sho_close();
			}
			break;
		case R.id.toggleButton7:
			if (tb[6].isChecked()) {
				patternSwitch(3); // stage 2：深度课设阶段
			} else {
			}
			break;
		case R.id.toggleButton8:
			if (tb[7].isChecked()) {
				patternSwitch(4);
			} else {
			}
			break;
		default:
			break;
		}
	}
	private void gotoDeepDesign()
	{
		Intent intent =new Intent(this,DeepPartActivity.class);
		startActivity(intent);
		endThisPage();
	}
	private void exit() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				CurriculumPartActivity.this);
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
