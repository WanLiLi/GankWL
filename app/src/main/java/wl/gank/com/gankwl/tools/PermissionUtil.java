package wl.gank.com.gankwl.tools;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import wl.gank.com.gankwl.R;


/**
 * Created by songzeyang on 16/4/21.
 */
public class PermissionUtil {

    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static final String WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_PHONE_STATE_PERMISSION = Manifest.permission.READ_PHONE_STATE;
    public static final String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    public static final int PERMISSION_REQ = 107;

    private static final String PACKAGE_URL_SCHEME = "package:";

    //请求权限
    public static void requestPermissions(Activity context,int reqCode,String... permissions) {
        ActivityCompat.requestPermissions(context, permissions, reqCode);
    }

    // 显示缺失权限提示
    public static void showMissingPermissionDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);

        // 拒绝
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
            }
        });

        // 设置
        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(context);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    // 启动应用的设置
    private static void startAppSettings(Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        context.startActivity(intent);
    }

    public interface PermissionCallBack {
        void handleProceed(String permission);
        void handleDenied(String permission);
    }
}
