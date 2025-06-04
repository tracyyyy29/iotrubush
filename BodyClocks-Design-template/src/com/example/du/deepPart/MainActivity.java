package com.example.du.deepPart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.du.ANRWatchDog.ANRError;
import com.example.du.ANRWatchDog.ANRWatchDog;
import com.example.du15.R;
import com.example.du.deepPart.PSIInputUtil;
import com.hanheng.a53.beep.BeepClass;
import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.dotarray.DotArrayClass;
import com.hanheng.a53.dotarray.DotUseClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.seg7.Seg7Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//package home.equipmentControl.brightnessAdjust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * basic hardware with: 1. 1 beep 2. 4 LEDs 3. 8 DIP switch 4. 1 seg7 (4 number)
 * 5. 1 dot array (16*16)
 * 
 * basic software(Java) with: 1. lots of data 2. 5 clocks show
 * 
 * to show different functions.
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * 记录是否进入最简模式
	 */
	private boolean isSimplest;
	private static PSIInputUtil psiUtil;
	/**
	 * 最简表模型的点阵状态控制
	 */
	private int dotDynamicState;
	/**
	 * 最简表模型动态线程控制
	 */
	private boolean isDotDynamic;
	/**
	 * ���뿪�ع�λ������
	 */
	private boolean isFZero;
	/**
	 * 用于一些线程的控制
	 */
	private boolean isStart;
	/**
	 * 用于最简模式判断
	 */
	private boolean isSimple = false;
	private boolean isStatic = false;
	/**
	 * 用于点阵显示档位
	 */
	private int dotCondition = 1;
	/**
	 * 用于是否禁用屏幕判断
	 */
	private boolean forbidden = false;
	/**
	 * 记录是否输入生日信息
	 */
	private boolean isBirthTimeOK;

	/**
	 * 本地广播，用于接受生日的设置
	 */
	private LocalBroadcastManager localBroadcastManager;
	/**
	 * 用于广播的注册
	 */
	private IntentFilter intentFilter;
	/**
	 * 用于广播的注册
	 */
	private BroadcastReceiver broadcastReceiver;

	/**
	 * 左边部分选择控制的ListView
	 */
	private List<ListViewItem> listViewList = new ArrayList<ListViewItem>();
	/**
	 * 中间部分的加载五时表盘的ImageView
	 */
	private ImageView clockImageView;
	/**
	 * 右边部分的附加信息的TextView
	 */
	private TextView infoTextView;
	/**
	 * 右边部分的测试信息的TextView
	 */
	private static TextView testTextView;
	/**
	 * 右边部分的测试信息的ScrollView
	 */
	private static ScrollView scrollView;
	/**
	 * 控制是否显示测试信息
	 */
	private static boolean isShowTestText;
	/**
	 * 右边部分的设置出生日期的Button
	 */
	private Button setBirthButton;
	/**
	 * PSI记录button
	 */
	private Button PSIrecord;
	/**
	 * 右边部分的PSI消创反馈的Button
	 */
	private Button setPSIButton;
	/**
	 * 最下面最简模式的Button
	 */
	private Button simplestButton;
	/**
	 * 最下面退出的Button
	 */
	private Button exitButton;
	/**
	 * 最上面设备温度的TextView
	 */
	private TextView watchTemperatureText;
	/**
	 * 最上面设备电量的TextView
	 */
	private TextView watchRemainPowerText;
	/**
	 * 最上面设备状态的TextView
	 */
	private TextView watchWorkStateText;

	/**
	 * 设备温度
	 */
	private float watchTemperature;
	/**
	 * 设备剩余电量
	 */
	private int watchRemainPower;
	/**
	 * 设备工作状态
	 */
	private String watchWorkState;
	/**
	 * 用户输入的生日转换为毫秒
	 */
	protected long milliseconds;
	protected int birthHourIndex;

	public boolean isBirthTimeOK() {
		return isBirthTimeOK;
	}

	/**
	 * 程序入口点
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_part_activity);

		init();

		// 本地广播，用于接受生日的设置
		localBroadcastManager = LocalBroadcastManager.getInstance(this);
		intentFilter = new IntentFilter(
				"com.example.du.deepPart.MainActivity.localbroadcast");
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action
						.equals("com.example.du.deepPart.MainActivity.localbroadcast")) {
					milliseconds = intent.getLongExtra("birthTime", 0);
					birthHourIndex = intent.getIntExtra("hourIndex", 0);

					infoTextView.setText("附加信息加载中……"
					// "天时信息"
					// +"\r\n"+"【太阳标准时】"
					// +"\r\n"+"【太阳真时】"
					// +"\r\n"+"【当地时间】"
					// +"\r\n"+"【公历】"
					// +"\r\n"+"【农历】"
					// +"\r\n"+"【当前节气】"
					//
					// +"\r\n"+"地时信息"
					// +"\r\n"+"【经纬度】"
					// +"\r\n"+"【海拔】"
					// +"\r\n"+"【气压】"
					// +"\r\n"+"【湿度】"
					// +"\r\n"+"【风力】"
					// +"\r\n"+"【当前干支】"
					// +"\r\n"+"人时信息"
					// +"\r\n"+"【您的生日】"+dateString
					// +"\r\n"+"【生日干支】"
					// +"\r\n"+"【P】"
					// +"\r\n"+"【S】"
					// +"\r\n"+"【I】"
					// +"\r\n"+"【心率】"
					// +"\r\n"+"【血压】"
					// +"\r\n"+"【血氧】"
					// +"\r\n"+"【体温】"
					// +"\r\n"+"穴位时信息"
					// +"\r\n"+"【当前时辰】"
					// +"\r\n"+"【对应穴位】"
					// +"\r\n"+"经络时信息"
					// +"\r\n"+"【当前时辰】"
					// +"\r\n"+"【对应经络】"
							);

					setBirthButton.setVisibility(Button.INVISIBLE);
				}
			}
		};
		localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

	}

	// 重载onDestroy函数
	@Override
	protected void onDestroy() {
		super.onDestroy();
		testTextView = null;
		scrollView = null;
		// 另外，建议把退出的处理函数 gotoEnd()的代码除了保留this.finish()外，也都写在这里
		// 如......

		isStart = false;
		localBroadcastManager.unregisterReceiver(broadcastReceiver);

		DipClass.Exit();
		BeepClass.Exit();
		DotArrayClass.Exit();
		LedClass.Exit();
		Seg7Class.Exit();

		AudioControl.getInstance().release();

		// 其他......
	}

	/**
	 * 初始化
	 */
	private void init() {
		hardwareInit();
		isStart = true;
		isBirthTimeOK = false;
		isShowTestText = false;
		isSimplest = false;
		dotDynamicState = -1;
		isDotDynamic = true;

		// isShowTestText = true;
		softwareInit();

		updateData();

		updateHardwareUI();

		setCanvasSize();
		updateSoftwareUI();

		setpsiUtil();
		openThread();

	}

	private void setpsiUtil() {
		// TODO Auto-generated method stub
		psiUtil = new PSIInputUtil(this, PSIInputUtil.TABLE_NAME + ".db", null,
				1);
		psiUtil.getWritableDatabase();

		// insertData(2003, 6, 4, 7, 40, 1, 2, 1, 3, 1, 2, 3);
		// insertData(2003, 6, 4, 7, 40, 2, 2, 1, 3, 2, 1, 1);
		// insertData(2003, 6, 4, 7, 40, 3, 2, 1, 3, 3, 2, 2);
		// insertData(2003, 6, 4, 7, 40, 4, 2, 1, 3, 2, 3, 2);
		// insertData(2003, 6, 4, 7, 40, 5, 2, 1, 3, 2, 2, 1);
		// insertData(2003, 6, 4, 7, 40, 6, 2, 1, 3, 3, 2, 3);
	}

	public static void insertData(int time_year, int time_month, int time_day,
			int time_hour, int time_minute, int time_second, int p_in_theory,
			int s_in_theory, int i_in_theory, int p_in_fact, int s_in_fact,
			int i_in_fact) {
		SQLiteDatabase db = psiUtil.getWritableDatabase();
		ContentValues values = new ContentValues();

		String time = String.format("[%d-%02d-%02d %02d:%02d:%02d]", time_year,
				time_month, time_day, time_hour, time_minute, time_second);
		values.put(PSIInputUtil.TIME, time);

		values.put(PSIInputUtil.P_IN_THEORY, p_in_theory);
		values.put(PSIInputUtil.S_IN_THEORY, s_in_theory);
		values.put(PSIInputUtil.I_IN_THEORY, i_in_theory);

		values.put(PSIInputUtil.P_IN_FACT, p_in_fact);
		values.put(PSIInputUtil.S_IN_FACT, s_in_fact);
		values.put(PSIInputUtil.I_IN_FACT, i_in_fact);

		db.insert(PSIInputUtil.TABLE_NAME, null, values);
	}

	/**
	 * 开启一系列的线程，WatchDog机制，监测设备状态
	 */
	private void openThread() {
		openWatchTemperatureMonitorThread();
		refreshUIThread();
		openDotDynamicThread();
		// openANRWatchDog();
	}

	private void openANRWatchDog() {
		// TODO Auto-generated method stub
		new ANRWatchDog(3000).setANRListener(new ANRWatchDog.ANRListener() {

			@Override
			public void onAppNotResponding(ANRError error) {
				// TODO Auto-generated method stub
				final Intent intent = getPackageManager()
						.getLaunchIntentForPackage(getPackageName());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		}).start();
	}

	/**
	 * 刷新页面线程
	 */
	private void refreshUIThread() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				while (isStart) {
					Message msg = new Message();
					msg.what = Constants.REFRESH_UI_FLAG;
					uiHandler.sendMessage(msg);
					try {
						Thread.sleep(Constants.REFRESH_THREAD_SLEEP_TIME);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

	/**
	 * 温度监控线程
	 */
	private void openWatchTemperatureMonitorThread() {
		new Thread() {
			@Override
			public void run() {
				while (isStart) {
					Message msg = new Message();
					msg.what = Constants.WATCH_TEMPERATURE_MONITOR_FLAG;
					uiHandler.sendMessage(msg);
					try {
						Thread.sleep(Constants.DEFAULT_THREAD_SLEEP_TIME);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start();
	}

	/**
	 * 刷新点阵线程
	 */
	private void openDotDynamicThread() {
		new Thread() {
			public void run() {
				while (isDotDynamic) {
					Message msg = new Message();
					msg.what = Constants.DOT_DYNAMIC_UPDATE_FLAG;
					uiHandler.sendMessage(msg);
					try {
						Thread.sleep(Constants.DOT_DYNAMIC_UPDATE_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 中级UI的更新（硬件）
	 */
	private void updateHardwareUI() {
		updateSegText(watchTemperature);
		// updateDotShow();

	}

	/**
	 * 数码管显示浮点数方法
	 * 
	 * @param f
	 */
	private void updateSegText(final float f) {
		new Thread() {
			@Override
			public void run() {
				Seg7Class.Seg7Show(f);
			}
		}.start();
	}

	// /**
	// * 点阵显示最简表模型
	// */
	// private void updateDotShow() {
	// DotUseClass dot = DotUseClass.getInstance();
	// dot.clean();
	// setCircleDot();
	//
	// switch (dotCondition) {
	// case (1):
	// // 档位1
	// setDot(dot, dotCondition);
	// dotCondition = 2;
	// break;
	// case (2):
	// // 档位2
	// setDot(dot, dotCondition);
	// dotCondition = 3;
	// break;
	// case (3):
	// // 档位3
	// setDot(dot, dotCondition);
	// dotCondition = 4;
	// break;
	// case (4):
	// // 档位4
	// setDot(dot, dotCondition);
	// dotCondition = 5;
	// break;
	// case (5):
	// // 档位5
	// setDot(dot, dotCondition);
	// dotCondition = 6;
	// break;
	// case (6):
	// // 档位6
	// setDot(dot, dotCondition);
	// dotCondition = 7;
	// break;
	// case (7):
	// // 档位7
	// setDot(dot, dotCondition);
	// dotCondition = 8;
	// break;
	// case (8):
	// // 档位8
	// setDot(dot, dotCondition);
	// dotCondition = 1;
	// break;
	// }
	// dot.show();
	// }
	//
	// /**
	// * 最简表模型表盘绘制
	// */
	// private void setCircleDot() {
	// DotUseClass dot = DotUseClass.getInstance();
	// double c_x = 7.5, c_y = 7.5, c_r = 1, r = 6.5;
	// int count = 0;
	// for (int i = 0; i < 16; i++) {
	// for (int j = 0; j < 16; j++) {
	// double dis = pointDistance(c_x, c_y, i, j);
	// if (dis < c_r) {
	// dot.setPoint(i, j);
	// } else if (dis >= (int) r) {
	// if (!isStatic) { // 动态全显示
	// dot.setPoint(i, j);
	// } else if (dis <= (int) r + 1) {// 静态最简显示
	// if (count++ == 0) {
	// dot.setPoint(i, j);
	// }
	// if (count == 4) {
	// count = 0;
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// /**
	// * 最简表模型的显示
	// */
	// private void setDot(DotUseClass dot, int minute) {
	//
	//
	// // 分钟分为八个档位 7.5秒切换一个档位
	// switch (minute) {
	// case (1):
	// // 档位1
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(6, 6, false);
	// // dot.setPoint(5, 5, false);
	// // dot.setPoint(4, 4, false);
	//
	// dot.setPoint(8, 2);
	// dot.setPoint(8, 3);
	// dot.setPoint(8, 4);
	// dot.setPoint(8, 5);
	// dot.setPoint(8, 6);
	//
	// break;
	// case (2):
	// // 档位2
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(8, 2, false);
	// // dot.setPoint(8, 3, false);
	// // dot.setPoint(8, 4, false);
	// // dot.setPoint(8, 5, false);
	// // dot.setPoint(8, 6, false);
	//
	// dot.setPoint(9, 6);
	// dot.setPoint(10, 5);
	// dot.setPoint(11, 4);
	// break;
	// case (3):
	// // 档位3
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(9, 6, false);
	// // dot.setPoint(10, 5, false);
	// // dot.setPoint(11, 4, false);
	//
	// dot.setPoint(9, 8);
	// dot.setPoint(10, 8);
	// dot.setPoint(11, 8);
	// dot.setPoint(12, 8);
	// dot.setPoint(13, 8);
	// break;
	// case (4):
	// // 档位4
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(9, 8, false);
	// // dot.setPoint(10, 8, false);
	// // dot.setPoint(11, 8, false);
	// // dot.setPoint(12, 8, false);
	// // dot.setPoint(13, 8, false);
	//
	// dot.setPoint(9, 9);
	// dot.setPoint(10, 10);
	// dot.setPoint(11, 11);
	// break;
	// case (5):
	// // 档位5
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(9, 9, false);
	// // dot.setPoint(10, 10, false);
	// // dot.setPoint(11, 11, false);
	//
	// dot.setPoint(8, 9);
	// dot.setPoint(8, 10);
	// dot.setPoint(8, 11);
	// dot.setPoint(8, 12);
	// dot.setPoint(8, 13);
	// break;
	// case (6):
	// // 档位6
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(8, 9, false);
	// // dot.setPoint(8, 10, false);
	// // dot.setPoint(8, 11, false);
	// // dot.setPoint(8, 12, false);
	// // dot.setPoint(8, 13, false);
	//
	// dot.setPoint(7, 9);
	// dot.setPoint(6, 10);
	// dot.setPoint(5, 11);
	// dot.setPoint(4, 12);
	// break;
	// case (7):
	// // 档位7
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(7, 9, false);
	// // dot.setPoint(6, 10, false);
	// // dot.setPoint(5, 11, false);
	// // dot.setPoint(4, 12, false);
	//
	// dot.setPoint(2, 8);
	// dot.setPoint(3, 8);
	// dot.setPoint(4, 8);
	// dot.setPoint(5, 8);
	// dot.setPoint(6, 8);
	// break;
	// case (8):
	// // 档位8
	// // if(isStatic)
	// // {
	// // break;
	// // }
	// // dot.setPoint(2, 8, false);
	// // dot.setPoint(3, 8, false);
	// // dot.setPoint(4, 8, false);
	// // dot.setPoint(5, 8, false);
	// // dot.setPoint(6, 8, false);
	//
	// dot.setPoint(6, 6);
	// dot.setPoint(5, 5);
	// dot.setPoint(4, 4);
	// break;
	// }
	// }

	/**
	 * 点阵显示最简表模型
	 */
	private void updateDotShow() {
		DotUseClass dot = DotUseClass.getInstance();
		dot.clean();
		setDot(dot);
		dot.show();
	}

	/**
	 * 最简表模型显示
	 */
	private void setDot(DotUseClass dot) {
		double c_x = 7.5, c_y = 7.5, c_r = 1, r = 6.5;
		int count = 0;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				double dis = pointDistance(c_x, c_y, i, j);
				if (dis < c_r) {
					dot.setPoint(i, j);
				} else if (dis >= (int) r) {
					if (isDotDynamic) { // 动态全显示
						dot.setPoint(i, j);
					} else if (dis <= (int) r + 1) {// 静态最简显示
						if (count++ == 0) {
							dot.setPoint(i, j);
						}
						if (count == 4) {
							count = 0;
						}
					}
				}
			}
		}

		for (int j = 0; j < Constants.DOT_CLOCK_POINTER_X[dotDynamicState].length; j++) {
			dot.setPoint(Constants.DOT_CLOCK_POINTER_X[dotDynamicState][j],
					Constants.DOT_CLOCK_POINTER_Y[dotDynamicState][j]);
		}
	}

	/**
	 * 计算两点距离的封装，供点阵显示调用
	 * 
	 * @param p1_x
	 * @param p1_y
	 * @param p2_x
	 * @param p2_y
	 * @return distance
	 */
	private double pointDistance(double p1_x, double p1_y, int p2_x, int p2_y) {
		return Math.sqrt((p1_x - p2_x) * (p1_x - p2_x) + (p1_y - p2_y)
				* (p1_y - p2_y));
	}

	/**
	 * 通过获取的屏幕像素，设置对应的画布大小
	 */
	private void setCanvasSize() {

		int screenWidth = getResources().getDisplayMetrics().widthPixels;
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		int size = screenHeight < screenWidth ? screenHeight : screenWidth;
		float cavasSize = (float) (0.7 * size);
		float cavasPadding = (float) (0.05 * size);

		SunTimeControl.getInstance().setCanvasSize(cavasSize, cavasSize,
				cavasPadding);
		LocalTimeControl.getInstance().setCanvasSize(cavasSize, cavasSize,
				cavasPadding);
		BirthTimeControl.getInstance().setCanvasSize(cavasSize, cavasSize,
				cavasPadding);
		MeridianTimeControl.getInstance().setCanvasSize(cavasSize, cavasSize,
				cavasPadding);
		AcupointTimeControl.getInstance().setCanvasSize(cavasSize, cavasSize,
				cavasPadding);

	}

	/**
	 * 高级UI的更新
	 */
	private void updateSoftwareUI() {
		int item_pos = Vars.getInstance().getMyListViewItemChoicePosition();
		switch (item_pos) {
		case 0:
			infoTextView.setText(SunTimeControl.getInstance().getInfoText());
			clockImageView.setImageBitmap(SunTimeControl.getInstance()
					.drawClock());

			break;
		case 1:
			infoTextView.setText(LocalTimeControl.getInstance().getInfoText());
			clockImageView.setImageBitmap(LocalTimeControl.getInstance()
					.drawClock());
			break;
		case 2:
			infoTextView.setText(BirthTimeControl.getInstance().getInfoText());
			clockImageView.setImageBitmap(BirthTimeControl.getInstance()
					.drawClock());

			break;
		case 3:
			infoTextView.setText(MeridianTimeControl.getInstance()
					.getInfoText());
			clockImageView.setImageBitmap(MeridianTimeControl.getInstance()
					.drawClock());

			break;
		case 4:
			infoTextView.setText(AcupointTimeControl.getInstance()
					.getInfoText());
			clockImageView.setImageBitmap(AcupointTimeControl.getInstance()
					.drawClock());

			break;
		default:
			break;
		}

	}

	/**
	 * 更新后端数据
	 */
	private void updateData() {
		getwatchRemainPower();

		SunTimeControl.getInstance().updateData();
		LocalTimeControl.getInstance().updateData();
		BirthTimeControl.getInstance().updateData();
		MeridianTimeControl.getInstance().updateData();
		AcupointTimeControl.getInstance().updateData();
	}

	/**
	 * 硬件初始化
	 */
	private void hardwareInit() {
		DipClass.Init();
		BeepClass.Init();
		dipInit();
		LedClass.Init();
		Seg7Class.Init();
		DotUseClass.getInstance();
	}

	/**
	 * 拨码开关的初始化（主要检测是否全置零）
	 */
	private void dipInit() {
		if (DipClass.ReadValue() != 0) {
			isFZero = false;
			BeepClass.IoctlRelay(Constants.BEEP_ON); // 若不正确，beep声音提示同时触摸屏显示提示信息，等待调整正确
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
									BeepClass.IoctlRelay(Constants.BEEP_OFF); // 拨码开关位置调整正确后关闭蜂鸣器
								}
							});
			dialog.create();
			dialog.show();
		}
	}

	/**
	 * 软件初始化 主要是控件的设置和监听器的设置等
	 */
	private void softwareInit() {

		setContext();
		dataInit();
		listViewInit();
		otherViewInit();
		setListener();

		Vars.getInstance().setDefaultFontColorInt(
				watchTemperatureText.getCurrentTextColor());
	}

	private void setContext() {
		SunTimeControl.getInstance().setContext(this);
		LocalTimeControl.getInstance().setContext(this);
		BirthTimeControl.getInstance().setContext(this);
		MeridianTimeControl.getInstance().setContext(this);
		AcupointTimeControl.getInstance().setContext(this);
		AudioControl.getInstance().setContext(this);
	}

	/**
	 * 设备参数初始化
	 */
	private void dataInit() {
		watchTemperature = Constants.ENVIRONMENT_TEMPERATURE;
		watchRemainPower = Constants.ORIGIN_REMIND_POWER;
		watchWorkState = Constants.STATE_NORMAL;
	}

	/**
	 * 设置监听器
	 */
	private void setListener() {
		setBirthButton.setOnClickListener(this);
		setPSIButton.setOnClickListener(this);
		simplestButton.setOnClickListener(this);
		PSIrecord.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}

	/**
	 * 除ListView外控件的初始化
	 */
	private void otherViewInit() {
		clockImageView = (ImageView) findViewById(R.id.clock_image);
		infoTextView = (TextView) findViewById(R.id.info_text);
		testTextView = (TextView) findViewById(R.id.test_text);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		setBirthButton = (Button) findViewById(R.id.set_birth_btn);
		setPSIButton = (Button) findViewById(R.id.set_PSI_btn);
		PSIrecord = (Button) findViewById(R.id.psi_record_btn);
		simplestButton = (Button) findViewById(R.id.simplest_btn);
		exitButton = (Button) findViewById(R.id.exit_btn2);
		watchTemperatureText = (TextView) findViewById(R.id.watchTemperature);
		watchRemainPowerText = (TextView) findViewById(R.id.watchRemainPower);
		watchWorkStateText = (TextView) findViewById(R.id.watchWorkState);

		watchTemperatureText.setText(String.format("%.2f", watchTemperature));
		watchRemainPowerText.setText(String.valueOf(watchRemainPower));
		watchWorkStateText.setText(watchWorkState);

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String dateString = dateFormat.format(new Date(milliseconds));
		infoTextView.setText("请先设置用户生日！");
	}

	/**
	 * ListView的初始化
	 */
	private void listViewInit() {
		ListViewItem listViewItem1 = new ListViewItem("天     时",
				R.drawable.sun_clock_icon);
		listViewList.add(listViewItem1);
		ListViewItem listViewItem2 = new ListViewItem("地     时",
				R.drawable.local_clock_icon);
		listViewList.add(listViewItem2);
		ListViewItem listViewItem3 = new ListViewItem("人     时",
				R.drawable.birth_clock_icon);
		listViewList.add(listViewItem3);
		ListViewItem listViewItem4 = new ListViewItem("经络时",
				R.drawable.meridian_clock_icon);
		listViewList.add(listViewItem4);
		ListViewItem listViewItem5 = new ListViewItem("穴位时",
				R.drawable.acupoint_clock_icon);
		listViewList.add(listViewItem5);

		final ListViewAdapter adapter = new ListViewAdapter(MainActivity.this,
				R.layout.listview_item_layout, listViewList);

		ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Vars.getInstance().setMyListViewItemChoicePosition(position);
				adapter.notifyDataSetInvalidated();
				//
				// SimpleDateFormat dateFormat = new
				// SimpleDateFormat("yyyy-MM-dd ");
				// String dateString = dateFormat.format(new
				// Date(milliseconds));
				// dateString+=Constants.LUNAR_HOUR_STRINGS[birthHourIndex-1];
				//
				switch (position) {
				case 0:
					clockImageView.setImageBitmap(SunTimeControl.getInstance()
							.drawClock());
					infoTextView.setText(SunTimeControl.getInstance()
							.getInfoText());

					break;
				case 1:
					clockImageView.setImageBitmap(LocalTimeControl
							.getInstance().drawClock());
					infoTextView.setText(LocalTimeControl.getInstance()
							.getInfoText());
					break;
				case 2:
					clockImageView.setImageBitmap(BirthTimeControl
							.getInstance().drawClock());
					Toast.makeText(MainActivity.this, "请确保出生时间已设置",
							Toast.LENGTH_SHORT).show();
					infoTextView.setText(BirthTimeControl.getInstance()
							.getInfoText());
					break;
				case 3:
					clockImageView.setImageBitmap(MeridianTimeControl
							.getInstance().drawClock());
					infoTextView.setText(MeridianTimeControl.getInstance()
							.getInfoText());
					break;
				case 4:
					clockImageView.setImageBitmap(AcupointTimeControl
							.getInstance().drawClock());
					infoTextView.setText(AcupointTimeControl.getInstance()
							.getInfoText());
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 按钮的点击响应
	 */
	@Override
	public void onClick(View view) {
		if (forbidden) {
			// 如果禁用屏幕触控 直接退出 不判断click了什么
			return;
		}
		int id = view.getId();
		switch (id) {
		case R.id.set_birth_btn:
			gotoSetBirth();
			break;
		case R.id.set_PSI_btn:
			// Log.e("test1", "gotopsi");
			gotoSetPSI();
			break;
		case R.id.simplest_btn:
			gotoSimplest();
			break;
		case R.id.psi_record_btn:
			gotoPSIrecord();
			break;
		case R.id.exit_btn2:
			exit();
			break;
		default:
			break;
		}

	}

	private void gotoPSIrecord() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, PSIrecord.class);
		startActivity(intent);

	}

	/**
	 * 设置出生时间
	 */
	private void gotoSetBirth() {
		Intent intent = new Intent(this, BirthInputActivity.class);
		startActivity(intent);
		isBirthTimeOK = true;
	}

	/**
	 * PSI消创性
	 */
	private void gotoSetPSI() {
		Intent intent = new Intent(this, ConsumerInnovationActivity.class);
		startActivity(intent);
	}

	/**
	 * 最简模式
	 */
	// private void gotoSimplest() {
	// // 屏幕亮度默认1 范围0-1
	// if (isSimple == false) {
	// isSimple = true;
	// setAppScreenBrightness((float) 0.1);
	// } else {
	// isSimple = false;
	// setAppScreenBrightness((float) 1);
	// }
	// // setAppScreenBrightness((float)0.1);
	// }
	private void gotoSimplest() {
		new Thread() {
			public void run() {
				BeepClass.IoctlRelay(Constants.BEEP_ON);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BeepClass.IoctlRelay(Constants.BEEP_OFF);
			};
		}.start();
		Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.screenBrightness = 0;
		window.setAttributes(layoutParams);
		isSimplest = true;
	}

	/**
	 * 退出确认
	 */
	private void exit() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("程序退出").setMessage("您确定要退出吗？")
				.setIcon(R.drawable.ic_launcher);
		dialog.setCancelable(false);
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gotoEnd();

			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dialog.show();
	}

	/**
	 * 结束
	 */
	private void gotoEnd() {
		isStart = false;
		localBroadcastManager.unregisterReceiver(broadcastReceiver);

		DipClass.Exit();
		BeepClass.Exit();
		DotArrayClass.Exit();
		LedClass.Exit();
		Seg7Class.Exit();
		AudioControl.getInstance().release();
		this.finish(); // 操作结束
	}

	/**
	 * 温控模拟
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_NUMPAD_ADD: // "+"
			watchTemperature++;
			updateSegText(watchTemperature);
			watchTemperatureText.setText(String
					.format("%.2f", watchTemperature));
			return true;
		case KeyEvent.KEYCODE_NUMPAD_SUBTRACT: // "-"
			watchTemperature--;
			updateSegText(watchTemperature);
			watchTemperatureText.setText(String
					.format("%.2f", watchTemperature));
			return true;
		case KeyEvent.KEYCODE_NUMPAD_0:
			setTestText();
			return true;
		case KeyEvent.KEYCODE_NUMPAD_1:
			simulateBlocking();
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void simulateBlocking() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(Constants.ERROR_THREAD_SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void setTestText() {
		// TODO Auto-generated method stub
		if (isShowTestText) {
			testTextView.setText("测试信息显示已禁用，请按“0”恢复");
		} else {
			testTextView.setText("");
		}
		isShowTestText = !isShowTestText;
	}

	/**
	 * 测试信息显示控件
	 */
	public static TextView getTestTextView() {
		return testTextView;
	}

	public static boolean isShowTestText() {
		return isShowTestText;
	}

	public static ScrollView getScrollView() {
		return scrollView;
	}

	/**
	 * 电量减少模拟
	 */
	public int getwatchRemainPower() {
		// 每秒钟掉电1 百分之十的时候字体变红 弹出提示框 百分之零的时候退出程序
//		watchRemainPower--;
		watchRemainPowerText.setTextColor(Color.BLACK);
		watchRemainPowerText.setText(String.valueOf(watchRemainPower));

		// 这里需要改变一下耗电的速度，但为了展示效果
		// 我决定每秒钟掉电一点
		// 30的时候显示电量低
		// 10的时候显示十秒内关机
		if (watchRemainPower == 30) {
			Toast.makeText(MainActivity.this, "电量不足！请尽快充电！", Toast.LENGTH_SHORT)
					.show();
			// try {
			// MediaPlayer mediaPlayer = new MediaPlayer();
			// AssetFileDescriptor fd =
			// getAssets().openFd("audio/remainpower.mp3");
			// mediaPlayer.setDataSource(fd.getFileDescriptor());
			// mediaPlayer.prepare();
			// mediaPlayer.start();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		if (watchRemainPower <= 30) {
			watchRemainPowerText.setTextColor(Color.RED);
		}
		if (watchRemainPower == 10) {
			// Toast.makeText(MainActivity.this, "设备将在十秒内关机！",
			// Toast.LENGTH_SHORT).show();
			// try {
			// MediaPlayer mediaPlayer = new MediaPlayer();
			// AssetFileDescriptor fd =
			// getAssets().openFd("audio/10seconds.mp3");
			// mediaPlayer.setDataSource(fd.getFileDescriptor());
			// mediaPlayer.prepare();
			// mediaPlayer.start();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			AudioControl.getInstance().setAudioData("10seconds.mp3",
					AudioControl.Priority.WARNING);
			AudioControl.getInstance().startAudio();
			beepControl(3);
			// watchRemainPower = 100;
		}
		if (watchRemainPower == 0) {
			gotoEnd();
		}
		return watchRemainPower;
	}

	/**
	 * uiHandler to send and handle msg
	 */
	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.what;
			if (type == Constants.WATCH_TEMPERATURE_MONITOR_FLAG) {
				parseTemperature();
			} else if (type == Constants.REFRESH_UI_FLAG) {
				refreshDateAndUI();
			} else if (type == Constants.DOT_DYNAMIC_UPDATE_FLAG) {
				parseDotDynamicUpdate();
			}
		}
	};

	private void parseTemperature() {
		// int color;
		// if (watchTemperature >= Constants.HIGEST_TEMPERATURE
		// || watchTemperature <= Constants.LOWEST_TEMPERATURE) {
		// // BeepClass.IoctlRelay(Constants.BEEP_ON);
		// // beepControl(3);
		// color = Color.RED;
		// watchWorkState = Constants.STATE_ERROR;
		// setAppScreenBrightness((float) 0);
		// forbidden = true;
		// isSimple = true;
		// if(watchTemperature>=60 || watchTemperature<=-20)
		// {
		//
		// isStatic = true;
		//
		// }
		// else
		// {
		// isStatic = false;
		// refreshDotThread();
		// }
		//
		// } else {
		// BeepClass.IoctlRelay(Constants.BEEP_OFF);
		// color = Vars.getInstance().getDefaultFontColorInt();
		// watchWorkState = Constants.STATE_NORMAL;
		// if (isSimple == false) {
		// setAppScreenBrightness((float) 1);
		// } else {
		// setAppScreenBrightness((float) 0.1);
		// }
		// forbidden = false;
		// }
		// watchTemperatureText.setTextColor(color);
		// watchWorkStateText.setTextColor(color);
		// watchWorkStateText.setText(watchWorkState);
		int color;
		boolean flag0 = (watchTemperature < Constants.HIGE_TEMPERATURE)
				&& (watchTemperature > Constants.LOW_TEMPERATURE);
		boolean flag1 = ((watchTemperature >= Constants.HIGE_TEMPERATURE) && (watchTemperature < Constants.HIGEST_TEMPERATURE))
				|| ((watchTemperature <= Constants.LOW_TEMPERATURE) && (watchTemperature > Constants.LOWEST_TEMPERATURE));
		boolean flag2 = (watchTemperature >= Constants.HIGEST_TEMPERATURE)
				|| (watchTemperature <= Constants.LOWEST_TEMPERATURE);
		if (flag0) {
			color = Vars.getInstance().getDefaultFontColorInt();
			watchWorkState = Constants.STATE_NORMAL;
			if (isSimplest)
				gotoOutSimplest();
		} else {
			color = Color.RED;
			watchWorkState = Constants.STATE_ERROR;
			if (flag1) {
				if (!isSimplest) {
					gotoSimplest();
				}
				if (!isDotDynamic) {
					isDotDynamic = true;
					openDotDynamicThread();
				}
			} else if (flag2 && isDotDynamic) {
				gotoDotStaticSimplest();
			}

		}

		watchTemperatureText.setTextColor(color);
		watchWorkStateText.setTextColor(color);
		watchWorkStateText.setText(watchWorkState);
	}

	private void gotoDotStaticSimplest() {
		isDotDynamic = false;
		new Thread() {
			public void run() {
				BeepClass.IoctlRelay(Constants.BEEP_ON);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BeepClass.IoctlRelay(Constants.BEEP_OFF);
			};
		}.start();
		updateDotShow();

	}

	private void gotoOutSimplest() {
		new Thread() {
			public void run() {
				BeepClass.IoctlRelay(Constants.BEEP_ON);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				BeepClass.IoctlRelay(Constants.BEEP_OFF);
			};
		}.start();
		Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.screenBrightness = 1f;
		window.setAttributes(layoutParams);
		isSimplest = false;
	}

	public void parseDotDynamicUpdate() {
		if (dotDynamicState == -1
				|| ++dotDynamicState == Constants.DOT_CLOCK_POINTER_X.length) {
			dotDynamicState = 0;
		}
		updateDotShow();
	}

	public void refreshDateAndUI() {
		// TODO Auto-generated method stub
		updateData();
		updateSoftwareUI();
	}

	// 蜂鸣器控制
	private void beepControl(int number) {
		try {
			for (int i = 0; i < number; i++) {
				BeepClass.IoctlRelay(1);
				Thread.sleep(200);
				BeepClass.IoctlRelay(0);
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// //获取当前屏幕亮度
	// private float getScreenBrightness(Activity activity)
	// {
	// ContentResolver contentResolver = activity.getContentResolver();
	// int defVal = 125;
	// return Settings.System.getInt(contentResolver,
	// Settings.System.SCREEN_BRIGHTNESS, defVal);
	//
	// }

	// 修改APP内部亮度
	private void setAppScreenBrightness(float brightnessValue) {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightnessValue;
		window.setAttributes(lp);
	}

	public static PSIInputUtil getpsiUtil() {
		return psiUtil;
	}
}
