package com.sap.dcode.util;

import android.util.Log;

import com.sap.dcode.online.AgencyApplication;
import com.sap.dcode.online.R;

public class TraceLog {
    private static String appName;

    /**
     * Initialize the Logs. 
     * @param app Reference to the application class.
     */
    public static void initialize(AgencyApplication app) {
        appName = app.getString(R.string.app_name);
    }

    public static void d(String msg) {
        Log.d(appName, msg);
    }

    public static void d(String msg, Throwable t) {
        Log.d(appName, msg, t);
    }

    public static void e(String msg) {
        Log.e(appName, msg);
    }

    public static void e(String msg, Throwable t) {
        Log.e(appName, msg, t);
    }

    public static void i(String msg) {
        Log.i(appName, msg);
    }

    public static void i(String msg, Throwable t) {
        Log.i(appName, msg, t);
    }

    public static ScopedTraceLog scoped(Object o) {
        ScopedTraceLog tl = new ScopedTraceLog();
        tl.classContext = o.getClass().getSimpleName();
        return tl;
    }

    public static class ScopedTraceLog {

        private String classContext;

        public void d(String method) {
            StringBuilder sb = new StringBuilder(classContext);
            TraceLog.d(sb.append("::").append(method).append("()").toString());
        }

        public void d(String method, String msg) {
            StringBuilder sb = new StringBuilder(classContext);
            sb.append("::").append(method).append("()");
            if (msg != null) {
                sb.append(" - ").append(msg);
            }
            TraceLog.d(sb.toString());
        }

    }

}
