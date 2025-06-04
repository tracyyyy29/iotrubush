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
	 * ��¼�Ƿ�������ģʽ
	 */
	private boolean isSimplest;
	private static PSIInputUtil psiUtil;
	/**
	 * ����ģ�͵ĵ���״̬����
	 */
	private int dotDynamicState;
	/**
	 * ����ģ�Ͷ�̬�߳̿���
	 */
	private boolean isDotDynamic;
	/**
	 * ���뿪�ع�λ������
	 */
	private boolean isFZero;
	/**
	 * ����һЩ�̵߳Ŀ���
	 */
	private boolean isStart;
	/**
	 * �������ģʽ�ж�
	 */
	private boolean isSimple = false;
	private boolean isStatic = false;
	/**
	 * ���ڵ�����ʾ��λ
	 */
	private int dotCondition = 1;
	/**
	 * �����Ƿ������Ļ�ж�
	 */
	private boolean forbidden = false;
	/**
	 * ��¼�Ƿ�����������Ϣ
	 */
	private boolean isBirthTimeOK;

	/**
	 * ���ع㲥�����ڽ������յ�����
	 */
	private LocalBroadcastManager localBroadcastManager;
	/**
	 * ���ڹ㲥��ע��
	 */
	private IntentFilter intentFilter;
	/**
	 * ���ڹ㲥��ע��
	 */
	private BroadcastReceiver broadcastReceiver;

	/**
	 * ��߲���ѡ����Ƶ�ListView
	 */
	private List<ListViewItem> listViewList = new ArrayList<ListViewItem>();
	/**
	 * �м䲿�ֵļ�����ʱ���̵�ImageView
	 */
	private ImageView clockImageView;
	/**
	 * �ұ߲��ֵĸ�����Ϣ��TextView
	 */
	private TextView infoTextView;
	/**
	 * �ұ߲��ֵĲ�����Ϣ��TextView
	 */
	private static TextView testTextView;
	/**
	 * �ұ߲��ֵĲ�����Ϣ��ScrollView
	 */
	private static ScrollView scrollView;
	/**
	 * �����Ƿ���ʾ������Ϣ
	 */
	private static boolean isShowTestText;
	/**
	 * �ұ߲��ֵ����ó������ڵ�Button
	 */
	private Button setBirthButton;
	/**
	 * PSI��¼button
	 */
	private Button PSIrecord;
	/**
	 * �ұ߲��ֵ�PSI����������Button
	 */
	private Button setPSIButton;
	/**
	 * ���������ģʽ��Button
	 */
	private Button simplestButton;
	/**
	 * �������˳���Button
	 */
	private Button exitButton;
	/**
	 * �������豸�¶ȵ�TextView
	 */
	private TextView watchTemperatureText;
	/**
	 * �������豸������TextView
	 */
	private TextView watchRemainPowerText;
	/**
	 * �������豸״̬��TextView
	 */
	private TextView watchWorkStateText;

	/**
	 * �豸�¶�
	 */
	private float watchTemperature;
	/**
	 * �豸ʣ�����
	 */
	private int watchRemainPower;
	/**
	 * �豸����״̬
	 */
	private String watchWorkState;
	/**
	 * �û����������ת��Ϊ����
	 */
	protected long milliseconds;
	protected int birthHourIndex;

	public boolean isBirthTimeOK() {
		return isBirthTimeOK;
	}

	/**
	 * ������ڵ�
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_part_activity);

		init();

		// ���ع㲥�����ڽ������յ�����
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

					infoTextView.setText("������Ϣ�����С���"
					// "��ʱ��Ϣ"
					// +"\r\n"+"��̫����׼ʱ��"
					// +"\r\n"+"��̫����ʱ��"
					// +"\r\n"+"������ʱ�䡿"
					// +"\r\n"+"��������"
					// +"\r\n"+"��ũ����"
					// +"\r\n"+"����ǰ������"
					//
					// +"\r\n"+"��ʱ��Ϣ"
					// +"\r\n"+"����γ�ȡ�"
					// +"\r\n"+"�����Ρ�"
					// +"\r\n"+"����ѹ��"
					// +"\r\n"+"��ʪ�ȡ�"
					// +"\r\n"+"��������"
					// +"\r\n"+"����ǰ��֧��"
					// +"\r\n"+"��ʱ��Ϣ"
					// +"\r\n"+"���������ա�"+dateString
					// +"\r\n"+"�����ո�֧��"
					// +"\r\n"+"��P��"
					// +"\r\n"+"��S��"
					// +"\r\n"+"��I��"
					// +"\r\n"+"�����ʡ�"
					// +"\r\n"+"��Ѫѹ��"
					// +"\r\n"+"��Ѫ����"
					// +"\r\n"+"�����¡�"
					// +"\r\n"+"Ѩλʱ��Ϣ"
					// +"\r\n"+"����ǰʱ����"
					// +"\r\n"+"����ӦѨλ��"
					// +"\r\n"+"����ʱ��Ϣ"
					// +"\r\n"+"����ǰʱ����"
					// +"\r\n"+"����Ӧ���硿"
							);

					setBirthButton.setVisibility(Button.INVISIBLE);
				}
			}
		};
		localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

	}

	// ����onDestroy����
	@Override
	protected void onDestroy() {
		super.onDestroy();
		testTextView = null;
		scrollView = null;
		// ���⣬������˳��Ĵ����� gotoEnd()�Ĵ�����˱���this.finish()�⣬Ҳ��д������
		// ��......

		isStart = false;
		localBroadcastManager.unregisterReceiver(broadcastReceiver);

		DipClass.Exit();
		BeepClass.Exit();
		DotArrayClass.Exit();
		LedClass.Exit();
		Seg7Class.Exit();

		AudioControl.getInstance().release();

		// ����......
	}

	/**
	 * ��ʼ��
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
	 * ����һϵ�е��̣߳�WatchDog���ƣ�����豸״̬
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
	 * ˢ��ҳ���߳�
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
	 * �¶ȼ���߳�
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
	 * ˢ�µ����߳�
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
	 * �м�UI�ĸ��£�Ӳ����
	 */
	private void updateHardwareUI() {
		updateSegText(watchTemperature);
		// updateDotShow();

	}

	/**
	 * �������ʾ����������
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
	// * ������ʾ����ģ��
	// */
	// private void updateDotShow() {
	// DotUseClass dot = DotUseClass.getInstance();
	// dot.clean();
	// setCircleDot();
	//
	// switch (dotCondition) {
	// case (1):
	// // ��λ1
	// setDot(dot, dotCondition);
	// dotCondition = 2;
	// break;
	// case (2):
	// // ��λ2
	// setDot(dot, dotCondition);
	// dotCondition = 3;
	// break;
	// case (3):
	// // ��λ3
	// setDot(dot, dotCondition);
	// dotCondition = 4;
	// break;
	// case (4):
	// // ��λ4
	// setDot(dot, dotCondition);
	// dotCondition = 5;
	// break;
	// case (5):
	// // ��λ5
	// setDot(dot, dotCondition);
	// dotCondition = 6;
	// break;
	// case (6):
	// // ��λ6
	// setDot(dot, dotCondition);
	// dotCondition = 7;
	// break;
	// case (7):
	// // ��λ7
	// setDot(dot, dotCondition);
	// dotCondition = 8;
	// break;
	// case (8):
	// // ��λ8
	// setDot(dot, dotCondition);
	// dotCondition = 1;
	// break;
	// }
	// dot.show();
	// }
	//
	// /**
	// * ����ģ�ͱ��̻���
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
	// if (!isStatic) { // ��̬ȫ��ʾ
	// dot.setPoint(i, j);
	// } else if (dis <= (int) r + 1) {// ��̬�����ʾ
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
	// * ����ģ�͵���ʾ
	// */
	// private void setDot(DotUseClass dot, int minute) {
	//
	//
	// // ���ӷ�Ϊ�˸���λ 7.5���л�һ����λ
	// switch (minute) {
	// case (1):
	// // ��λ1
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
	// // ��λ2
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
	// // ��λ3
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
	// // ��λ4
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
	// // ��λ5
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
	// // ��λ6
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
	// // ��λ7
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
	// // ��λ8
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
	 * ������ʾ����ģ��
	 */
	private void updateDotShow() {
		DotUseClass dot = DotUseClass.getInstance();
		dot.clean();
		setDot(dot);
		dot.show();
	}

	/**
	 * ����ģ����ʾ
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
					if (isDotDynamic) { // ��̬ȫ��ʾ
						dot.setPoint(i, j);
					} else if (dis <= (int) r + 1) {// ��̬�����ʾ
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
	 * �����������ķ�װ����������ʾ����
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
	 * ͨ����ȡ����Ļ���أ����ö�Ӧ�Ļ�����С
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
	 * �߼�UI�ĸ���
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
	 * ���º������
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
	 * Ӳ����ʼ��
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
	 * ���뿪�صĳ�ʼ������Ҫ����Ƿ�ȫ���㣩
	 */
	private void dipInit() {
		if (DipClass.ReadValue() != 0) {
			isFZero = false;
			BeepClass.IoctlRelay(Constants.BEEP_ON); // ������ȷ��beep������ʾͬʱ��������ʾ��ʾ��Ϣ���ȴ�������ȷ
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this)
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
									BeepClass.IoctlRelay(Constants.BEEP_OFF); // ���뿪��λ�õ�����ȷ��رշ�����
								}
							});
			dialog.create();
			dialog.show();
		}
	}

	/**
	 * �����ʼ�� ��Ҫ�ǿؼ������úͼ����������õ�
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
	 * �豸������ʼ��
	 */
	private void dataInit() {
		watchTemperature = Constants.ENVIRONMENT_TEMPERATURE;
		watchRemainPower = Constants.ORIGIN_REMIND_POWER;
		watchWorkState = Constants.STATE_NORMAL;
	}

	/**
	 * ���ü�����
	 */
	private void setListener() {
		setBirthButton.setOnClickListener(this);
		setPSIButton.setOnClickListener(this);
		simplestButton.setOnClickListener(this);
		PSIrecord.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}

	/**
	 * ��ListView��ؼ��ĳ�ʼ��
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
		infoTextView.setText("���������û����գ�");
	}

	/**
	 * ListView�ĳ�ʼ��
	 */
	private void listViewInit() {
		ListViewItem listViewItem1 = new ListViewItem("��     ʱ",
				R.drawable.sun_clock_icon);
		listViewList.add(listViewItem1);
		ListViewItem listViewItem2 = new ListViewItem("��     ʱ",
				R.drawable.local_clock_icon);
		listViewList.add(listViewItem2);
		ListViewItem listViewItem3 = new ListViewItem("��     ʱ",
				R.drawable.birth_clock_icon);
		listViewList.add(listViewItem3);
		ListViewItem listViewItem4 = new ListViewItem("����ʱ",
				R.drawable.meridian_clock_icon);
		listViewList.add(listViewItem4);
		ListViewItem listViewItem5 = new ListViewItem("Ѩλʱ",
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
					Toast.makeText(MainActivity.this, "��ȷ������ʱ��������",
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
	 * ��ť�ĵ����Ӧ
	 */
	@Override
	public void onClick(View view) {
		if (forbidden) {
			// ���������Ļ���� ֱ���˳� ���ж�click��ʲô
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
	 * ���ó���ʱ��
	 */
	private void gotoSetBirth() {
		Intent intent = new Intent(this, BirthInputActivity.class);
		startActivity(intent);
		isBirthTimeOK = true;
	}

	/**
	 * PSI������
	 */
	private void gotoSetPSI() {
		Intent intent = new Intent(this, ConsumerInnovationActivity.class);
		startActivity(intent);
	}

	/**
	 * ���ģʽ
	 */
	// private void gotoSimplest() {
	// // ��Ļ����Ĭ��1 ��Χ0-1
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
	 * �˳�ȷ��
	 */
	private void exit() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("�����˳�").setMessage("��ȷ��Ҫ�˳���")
				.setIcon(R.drawable.ic_launcher);
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gotoEnd();

			}
		});

		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dialog.show();
	}

	/**
	 * ����
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
		this.finish(); // ��������
	}

	/**
	 * �¿�ģ��
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
			testTextView.setText("������Ϣ��ʾ�ѽ��ã��밴��0���ָ�");
		} else {
			testTextView.setText("");
		}
		isShowTestText = !isShowTestText;
	}

	/**
	 * ������Ϣ��ʾ�ؼ�
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
	 * ��������ģ��
	 */
	public int getwatchRemainPower() {
		// ÿ���ӵ���1 �ٷ�֮ʮ��ʱ�������� ������ʾ�� �ٷ�֮���ʱ���˳�����
//		watchRemainPower--;
		watchRemainPowerText.setTextColor(Color.BLACK);
		watchRemainPowerText.setText(String.valueOf(watchRemainPower));

		// ������Ҫ�ı�һ�ºĵ���ٶȣ���Ϊ��չʾЧ��
		// �Ҿ���ÿ���ӵ���һ��
		// 30��ʱ����ʾ������
		// 10��ʱ����ʾʮ���ڹػ�
		if (watchRemainPower == 30) {
			Toast.makeText(MainActivity.this, "�������㣡�뾡���磡", Toast.LENGTH_SHORT)
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
			// Toast.makeText(MainActivity.this, "�豸����ʮ���ڹػ���",
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

	// ����������
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

	// //��ȡ��ǰ��Ļ����
	// private float getScreenBrightness(Activity activity)
	// {
	// ContentResolver contentResolver = activity.getContentResolver();
	// int defVal = 125;
	// return Settings.System.getInt(contentResolver,
	// Settings.System.SCREEN_BRIGHTNESS, defVal);
	//
	// }

	// �޸�APP�ڲ�����
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
