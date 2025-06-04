package com.example.du.ANRWatchDog;

import android.os.Looper;
import android.util.Log;

/**
 * Error thrown by ANRWatchDog when an ANR is detected.
 * Contains the stack trace of the frozen UI thread.
 */
public class ANRError extends Error {
    private static final long serialVersionUID = 1L;
    public ANRError() {
        super("Application Not Responding");
    }
    @Override
    public Throwable fillInStackTrace() {

        Log.e("ANRError", "Filling stack trace");

        setStackTrace(Looper.getMainLooper().getThread().getStackTrace());

        return this;
    }
}