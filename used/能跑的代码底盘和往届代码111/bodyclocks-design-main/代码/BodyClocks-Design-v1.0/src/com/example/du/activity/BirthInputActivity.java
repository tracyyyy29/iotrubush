package com.example.du.activity;

import java.util.Calendar;
import java.util.Date;

import com.example.du.R;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Body IV Birth Input Activity Part
 * 
 * @author <achdu0000@163.com>
 * 
 */

public class BirthInputActivity extends Activity implements OnClickListener {

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
		
		calendarView=(CalendarView) findViewById(R.id.calendar);
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
		okButton=(Button)findViewById(R.id.seletced_ok);
		
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView arg0, int arg1, int arg2,
					int arg3) {
				y=arg1;
				m=arg2;
				d=arg3;
			}
		});
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				hourIndex=arg1;
			}
		});
		
		okButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		if(arg0.getId()==R.id.seletced_ok){
			Calendar calendar=Calendar.getInstance();
			calendar.set(y, m, d, (hourIndex-1)*2, 0, 0);
			Date date=calendar.getTime();
			long milliseconds=calendar.getTimeInMillis();
			Log.e("ms o", String.valueOf(milliseconds));
			LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
			Intent intent=new Intent("com.example.du.activity.DeepPartActivity.localbroadcast");
			intent.putExtra("birthTime", milliseconds);
			localBroadcastManager.sendBroadcast(intent);
			
			this.finish();
		}
	}

}
