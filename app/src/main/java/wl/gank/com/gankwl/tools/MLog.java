package wl.gank.com.gankwl.tools;

import android.util.Log;


public class MLog {

    private static final String TAG = "escort";
//    private static final boolean SHOW = BuildConfig.DEBUG;
//    private static final boolean SHOW = true;

    public static void d(String msg) {
//        if(!SHOW) return;
        Log.d(TAG, msg);
    }
    public static void d(String tag,String msg) {
//        if(!SHOW) return;
        Log.d(tag, msg);
    }
}
