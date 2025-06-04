package com.example.du.ANRWatchDog;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * A watchdog timer thread that detects when the UI thread has frozen.
 */
public class ANRWatchDog extends Thread {

    private ANRWDSSeter seter = new ANRWDSSeter();

    private Handler handler = new Handler(Looper.getMainLooper());

    private int tick = 0;

    private final int interval;

    public interface ANRListener {
        /**
         * Called when an ANR is detected.
         * @param error The error describing the ANR.
         */
        void onAppNotResponding(ANRError error);
    }

    private static final ANRListener DEFAULT_ANR_LISTENER = new ANRListener() {
        @Override
        public void onAppNotResponding( ANRError error) {
            throw error;
        }
    };

    private ANRListener anrListener = DEFAULT_ANR_LISTENER;

    /**
     * Sets an interface for when an ANR is detected.
     * If not set, the default behavior is to throw an error and crash the application.
     *
     * @param listener The new listener or null
     * @return itself for chaining.
     */
    public ANRWatchDog setANRListener(ANRListener listener) {
        if (listener == null) {
            anrListener = DEFAULT_ANR_LISTENER;
        } else {
            anrListener = listener;
        }
        return this;
    }

    /**
     * Constructs a watchdog that checks the ui thread every given interval
     * @param interval The interval, in milliseconds, between to checks of the UI thread.
     *                        It is therefore the maximum time the UI may freeze before being reported as ANR.
     */
    public ANRWatchDog(int interval) {
        super();
        this.interval = interval;
    }


    /**
     * Constructs a watchdog that checks the ui thread every 5000 milliseconds
     */
    public ANRWatchDog() {
        this(5000);
    }

    private class ANRWDSSeter implements Runnable {
        @Override
        public void run() {
            tick = (tick + 1) % 10;
//			Log.i("ANRWatchDog", "Setting tick: " + (tick));
        }
    }

    @Override
    public void run() {
        try {
            int lastTick = 0;
            for (;;) {
                lastTick = tick;
                handler.post(seter);
                Thread.sleep(interval);

                if (tick == lastTick) {
                    Log.e("ANRWatchDog", "ANR DETECTED");
                    anrListener.onAppNotResponding(new ANRError());
                }
            }
        }
        catch (InterruptedException e) {
            Log.i("ANRWatchDog", "Interrupted");
        }
    }

}
