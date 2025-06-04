package com.example.du.deepPart;


import java.util.Calendar;
import java.util.Date;

import com.example.du15.R;

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

public class NewWindow extends Activity implements OnClickListener {

	private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.New_Window); // Ensure this matches your new XML layout filename

        backButton = (Button) findViewById(R.id.login);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.login) {
            // Navigate back to BirthInputActivity
            Intent intent = new Intent(NewWindow.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}