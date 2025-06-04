package com.example.du.deepPart;

import java.util.Calendar;
import java.util.Date;

public class SetTestText {
	
	private static SetTestText instance = null;
	public static SetTestText getInstance() {
		if (instance == null)
			instance = new SetTestText();
		return instance;
	}
	
	
	public static void addText(String text)
	{
		
		if(!MainActivity.isShowTestText())
			return;
		Calendar currentTime = Calendar.getInstance();
		String str = String.format("[%02d:%02d:%02d]",currentTime.get(Calendar.HOUR_OF_DAY),currentTime.get(Calendar.MINUTE),currentTime.get(Calendar.SECOND));
		MainActivity.getTestTextView().append(str+text+"\n");
		
		MainActivity.getScrollView().post(new Runnable() {
            @Override
            public void run() {
                    MainActivity.getScrollView().smoothScrollTo(0, MainActivity.getTestTextView().getBottom());
            }
    });
	}

}
