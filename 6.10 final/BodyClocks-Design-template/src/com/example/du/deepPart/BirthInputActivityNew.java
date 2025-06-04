package com.example.du.deepPart;

import java.util.Calendar;
import java.util.Date;

import com.example.du15.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 
 * NK2024 Embedded System Design Body IV Design Deep Part
 * 
 * for user's birthday input
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */

public class BirthInputActivityNew extends Activity implements OnClickListener {

	private CalendarView calendarView;
	private RadioGroup radioGroup;
	private Button okButton;
	private int y;
	private int m;
	private int d;
	private int hourIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birth_input_activity);

		calendarView = (CalendarView) findViewById(R.id.calendar);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		okButton = (Button) findViewById(R.id.seletced_ok);

		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView arg0, int arg1, int arg2,
					int arg3) {
				y = arg1;
				m = arg2;
				d = arg3;
			}
		});

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				hourIndex = arg1;
			}
		});

		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.seletced_ok) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(y, m, d, (hourIndex - 1) * 2, 0, 0);
			Date date = calendar.getTime();
			long milliseconds = calendar.getTimeInMillis();
			LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
			Intent intent = new Intent("com.example.du.deepPart.MainActivity.localbroadcast");

			intent.putExtra("birthTime", milliseconds);
			intent.putExtra("hourIndex", hourIndex);

			BirthTimeControl.getInstance().getBirthTime(milliseconds);
			BirthTimeControl.getInstance().getHourIndex(hourIndex);

			// 淇濆瓨鐢熸棩淇℃伅鍒版暟鎹簱涓�
			// 銆愭棩鏈熴�戯細2024-04-27
			// 銆愮紪鍐欒�呫�戯細GXXLeaXX
			SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
			String username = sharedPreferences.getString("username", "");

			// 淇濆瓨鐢熸棩淇℃伅鍒版暟鎹簱涓�
			UserRecord dbHelper = new UserRecord(this);
			dbHelper.updateBirth(username, y, m, d, (hourIndex - 1) * 2);

			localBroadcastManager.sendBroadcast(intent);
			this.finish();
		}
	}

}
