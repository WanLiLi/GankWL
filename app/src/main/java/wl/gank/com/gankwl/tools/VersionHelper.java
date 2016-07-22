package wl.gank.com.gankwl.tools;

import android.os.Build;

/**
 * Created by wanli on 2016/6/20.
 */
public class VersionHelper {
    private VersionHelper() {
        //no instance
    }


    /**
     *  大于或等于 制定版本
     *
     */
    public static boolean isAtLeast(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }


    /**
     *  大于或等于 5.0
     *
     * */
    public static boolean isAtLeast() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


}
