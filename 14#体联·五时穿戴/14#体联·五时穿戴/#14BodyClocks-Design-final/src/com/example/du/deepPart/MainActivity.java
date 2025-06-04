package com.example.du.deepPart;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.example.du14.R;
import com.hanheng.a53.beep.BeepClass;
import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.dotarray.DotArrayClass;
import com.hanheng.a53.dotarray.DotUseClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.seg7.Seg7Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActivityManager.TaskDescription;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
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
import java.util.TimerTask;

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

	private int enflag = 0;
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
	/**
	 * ���ڵ�����ʾ��λ
	 */
	private int dotCondition = 0;
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

	private ImageView imageView;
	private SlideMenu slideMenu;
	
	private int sta;
	private boolean isfir;
	
	public LocationManager lm;
	public static String locat;
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

		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
							);

					setBirthButton.setVisibility(Button.INVISIBLE);
				}
			}
		};
		localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
		
		slideMenu = (SlideMenu) findViewById(R.id.slidemenu);
		imageView = (ImageView) findViewById(R.id.menuico);
		
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				slideMenu.switchMenu();
			}
		});
		
		//Location lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //updateShow(lc);
	 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, new LocationListener() {
         @Override
         public void onLocationChanged(Location location) {
             // ��GPS��λ��Ϣ�����ı�ʱ�����¶�λ
             updateShow(location);
         }

         @Override
         public void onStatusChanged(String provider, int status, Bundle extras) {

         }

         @Override
         public void onProviderEnabled(String provider) {
             // ��GPS LocationProvider����ʱ�����¶�λ
             updateShow(lm.getLastKnownLocation(provider));
         }

         @Override
         public void onProviderDisabled(String provider) {
             updateShow(null);
         }
     });
	 

	}
	
	 //����һ��������ʾ�ķ���

	private String updateShow(Location location) {
	    if (location != null) {
	        StringBuilder sb = new StringBuilder();
	        sb.append("��ǰ��λ����Ϣ��\n");
	        sb.append("���ȣ�" + location.getLongitude() + "\n");
	        sb.append("γ�ȣ�" + location.getLatitude() + "\n");
	        sb.append("�߶ȣ�" + location.getAltitude() + "\n");
	        sb.append("�ٶȣ�" + location.getSpeed() + "\n");
	        sb.append("����" + location.getBearing() + "\n");
	        sb.append("��λ���ȣ�" + location.getAccuracy() + "\n");
	        String str=sb.toString();
	        return str;
	    }
	    return null;
	}
	
	//����onDestroy����
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    testTextView = null;
	    scrollView = null; 
	    //���⣬������˳��Ĵ����� gotoEnd()�Ĵ�����˱���this.finish()�⣬Ҳ��д������
	    //��......
	    AudioControl.getInstance().release();
	    DipClass.Exit();
	    //����......
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		hardwareInit();
		isStart = true;
		isBirthTimeOK = false;
		isfir = true;
		//isShowTestText = false;
		isShowTestText = true;
		setCircleDot();
		softwareInit();

		updateData();

		updateHardwareUI();

		setCanvasSize();
		updateSoftwareUI();
		openThread();
	}

	/**
	 * ����һϵ�е��̣߳�WatchDog���ƣ�����豸״̬
	 */
	private void openThread() {
		openWatchTemperatureMonitorThread();
		refreshUIThread();
		refreshDotThread();
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
	private void refreshDotThread() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				while (isStart) {
					updateDotShow();
					try {
						Thread.sleep(Constants.REFRESH_DOT_THREAD_SLEEP_TIME);
					} catch (InterruptedException e) {
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
		updateDotShow();

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

	/**
	 * ������ʾ����ģ��
	 */
	private void updateDotShow() {
		DotUseClass dot = DotUseClass.getInstance();
		// dot.clean();

		switch (dotCondition) {
		case (0):
			dotCondition = 1;
			break;
		case (1):
			// ��λ1
			setDot(dot, dotCondition);
			dotCondition = 2;
			break;
		case (2):
			// ��λ2
			setDot(dot, dotCondition);
			dotCondition = 3;
			break;
		case (3):
			// ��λ3
			setDot(dot, dotCondition);
			dotCondition = 4;
			break;
		case (4):
			// ��λ4
			setDot(dot, dotCondition);
			dotCondition = 5;
			break;
		case (5):
			// ��λ5
			setDot(dot, dotCondition);
			dotCondition = 6;
			break;
		case (6):
			// ��λ6
			setDot(dot, dotCondition);
			dotCondition = 7;
			break;
		case (7):
			// ��λ7
			setDot(dot, dotCondition);
			dotCondition = 8;
			break;
		case (8):
			// ��λ8
			setDot(dot, dotCondition);
			dotCondition = 1;
			break;
		}
		// dot.show();
	}

	/**
	 * ����ģ�ͱ��̻���
	 */
	private void setCircleDot() {
		DotUseClass dot = DotUseClass.getInstance();
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
		dot.show();
	}

	/**
	 * ����ģ�͵���ʾ
	 */
	private void setDot(DotUseClass dot, int minute) {

		// ���ӷ�Ϊ�˸���λ 7.5���л�һ����λ
		switch (minute) {
		case (1):
			// ��λ1
			dot.setPoint(6, 6, false);
			dot.setPoint(5, 5, false);
			dot.setPoint(4, 4, false);

			dot.setPoint(8, 2);
			dot.setPoint(8, 3);
			dot.setPoint(8, 4);
			dot.setPoint(8, 5);
			dot.setPoint(8, 6);

			break;
		case (2):
			// ��λ2
			dot.setPoint(8, 2, false);
			dot.setPoint(8, 3, false);
			dot.setPoint(8, 4, false);
			dot.setPoint(8, 5, false);
			dot.setPoint(8, 6, false);

			dot.setPoint(9, 6);
			dot.setPoint(10, 5);
			dot.setPoint(11, 4);
			break;
		case (3):
			// ��λ3
			dot.setPoint(9, 6, false);
			dot.setPoint(10, 5, false);
			dot.setPoint(11, 4, false);

			dot.setPoint(9, 8);
			dot.setPoint(10, 8);
			dot.setPoint(11, 8);
			dot.setPoint(12, 8);
			dot.setPoint(13, 8);
			break;
		case (4):
			// ��λ4
			dot.setPoint(9, 8, false);
			dot.setPoint(10, 8, false);
			dot.setPoint(11, 8, false);
			dot.setPoint(12, 8, false);
			dot.setPoint(13, 8, false);

			dot.setPoint(9, 9);
			dot.setPoint(10, 10);
			dot.setPoint(11, 11);
			break;
		case (5):
			// ��λ5
			dot.setPoint(9, 9, false);
			dot.setPoint(10, 10, false);
			dot.setPoint(11, 11, false);

			dot.setPoint(8, 9);
			dot.setPoint(8, 10);
			dot.setPoint(8, 11);
			dot.setPoint(8, 12);
			dot.setPoint(8, 13);
			break;
		case (6):
			// ��λ6
			dot.setPoint(8, 9, false);
			dot.setPoint(8, 10, false);
			dot.setPoint(8, 11, false);
			dot.setPoint(8, 12, false);
			dot.setPoint(8, 13, false);

			dot.setPoint(7, 9);
			dot.setPoint(6, 10);
			dot.setPoint(5, 11);
			dot.setPoint(4, 12);
			break;
		case (7):
			// ��λ7
			dot.setPoint(7, 9, false);
			dot.setPoint(6, 10, false);
			dot.setPoint(5, 11, false);
			dot.setPoint(4, 12, false);

			dot.setPoint(2, 8);
			dot.setPoint(3, 8);
			dot.setPoint(4, 8);
			dot.setPoint(5, 8);
			dot.setPoint(6, 8);
			break;
		case (8):
			// ��λ8
			dot.setPoint(2, 8, false);
			dot.setPoint(3, 8, false);
			dot.setPoint(4, 8, false);
			dot.setPoint(5, 8, false);
			dot.setPoint(6, 8, false);

			dot.setPoint(6, 6);
			dot.setPoint(5, 5);
			dot.setPoint(4, 4);
			break;
		}
		dot.show();
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
		MeridianTimeControl.getInstance((Context)MainActivity.this).setCanvasSize(cavasSize, cavasSize,
				cavasPadding);
		AcupointTimeControl.getInstance((Context)MainActivity.this).setCanvasSize(cavasSize, cavasSize,
				cavasPadding);

	}

	/**
	 * �߼�UI�ĸ���
	 */
	private void updateSoftwareUI() {
		int item_pos = Vars.getInstance().getMyListViewItemChoicePosition();
		sta = item_pos;
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
			infoTextView.setText(BirthTimeControl.getInstance()
					.getInfoText());
			clockImageView.setImageBitmap(BirthTimeControl.getInstance()
					.drawClock());
			BirthTimeControl.getInstance().setGPS(lm);
			break;
		case 3:
			infoTextView.setText(MeridianTimeControl.getInstance((Context)MainActivity.this).getInfoText());
			clockImageView.setImageBitmap(MeridianTimeControl
					.getInstance((Context)MainActivity.this).drawClock());
			break;
		case 4:
			infoTextView.setText(AcupointTimeControl.getInstance((Context)MainActivity.this).getInfoText());
			clockImageView.setImageBitmap(AcupointTimeControl
					.getInstance((Context)MainActivity.this).drawClock());
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
		
		if(isfir){
			SunTimeControl.getInstance().updateData();
			LocalTimeControl.getInstance().updateData();
			BirthTimeControl.getInstance().updateData();
			MeridianTimeControl.getInstance((Context)MainActivity.this).updateData();
			AcupointTimeControl.getInstance((Context)MainActivity.this).updateData();
			isfir = false;
		}
		
		switch (sta) {
		case 0:
			SunTimeControl.getInstance().updateData();
			break;
		case 1:
			LocalTimeControl.getInstance().updateData();
			break;
		case 2:
			BirthTimeControl.getInstance().updateData();
			break;
		case 3:
			MeridianTimeControl.getInstance((Context)MainActivity.this).updateData();
			break;
		case 4:
			AcupointTimeControl.getInstance((Context)MainActivity.this).updateData();
			break;
		default:
			break;
		}
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
				sta = position;
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
							.getInstance((Context)MainActivity.this).drawClock());
					infoTextView.setText(MeridianTimeControl.getInstance((Context)MainActivity.this)
							.getInfoText());
					break;
				case 4:
					clockImageView.setImageBitmap(AcupointTimeControl
							.getInstance((Context)MainActivity.this).drawClock());
					infoTextView.setText(AcupointTimeControl.getInstance((Context)MainActivity.this)
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
			gotoSetPSI();
			break;
		case R.id.simplest_btn:
			gotoSimplest();
			break;
		case R.id.exit_btn2:
			exit();
			break;
		default:
			break;
		}

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

	}

	/**
	 * ���ģʽ
	 */
	private void gotoSimplest() {
		// ��Ļ����Ĭ��1 ��Χ0-1
		if (isSimple == false) {
			isSimple = true;
			setAppScreenBrightness((float) 0.1);
		} else {
			isSimple = false;
			setAppScreenBrightness((float) 1);
		}
		// setAppScreenBrightness((float)0.1);
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
				AudioControl.getInstance().release();
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
		this.finish(); // ��������
	}

	/**
	 * �¿�ģ��
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_NUMPAD_ADD: // "+"
			watchTemperature++;//�¶�����
			updateSegText(watchTemperature);//�¶���ʾ����
			watchTemperatureText.setText(String
					.format("%.2f", watchTemperature));//�¶���ʾ����
			return true;
		case KeyEvent.KEYCODE_NUMPAD_SUBTRACT: // "-"
			watchTemperature--;//�¶Ƚ���
			updateSegText(watchTemperature);//��ʾ����
			watchTemperatureText.setText(String
					.format("%.2f", watchTemperature));
			return true;
		case KeyEvent.KEYCODE_NUMPAD_0:
			setTestText();
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setTestText() {

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
		enflag = (enflag + 1)%3;
		if(enflag == 0){
			watchRemainPower--;
		}
		watchRemainPowerText.setTextColor(Color.BLACK);
		watchRemainPowerText.setText(String.valueOf(watchRemainPower));

		
		if (watchRemainPower == 30) {
			Toast.makeText(MainActivity.this, "�������㣡�뾡���磡", Toast.LENGTH_SHORT)
					.show();
			
		}
		if (watchRemainPower <= 30) {
			watchRemainPowerText.setTextColor(Color.RED);
		}
		if (watchRemainPower == 10) {
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
			}
			// else if(type == Constants.REFRESH_DOT_FLAG)
			// {
			// updateDotShow();
			// }
		}
	};

	private void parseTemperature() {
		int color;
		if (watchTemperature >= Constants.HIGEST_TEMPERATURE
				|| watchTemperature <= Constants.LOWEST_TEMPERATURE) {
			BeepClass.IoctlRelay(Constants.BEEP_ON);
			color = Color.RED;
			watchWorkState = Constants.STATE_ERROR;
			setAppScreenBrightness((float) 0);
			forbidden = true;

		} else {
			BeepClass.IoctlRelay(Constants.BEEP_OFF);
			color = Vars.getInstance().getDefaultFontColorInt();
			watchWorkState = Constants.STATE_NORMAL;
			if (isSimple == false) {
				setAppScreenBrightness((float) 1);
			} else {
				setAppScreenBrightness((float) 0.1);
			}
			forbidden = false;
		}
		watchTemperatureText.setTextColor(color);
		watchWorkStateText.setTextColor(color);
		watchWorkStateText.setText(watchWorkState);
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

	// �޸�APP�ڲ�����
	private void setAppScreenBrightness(float brightnessValue) {
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.screenBrightness = brightnessValue;
		window.setAttributes(lp);
	}
}