package wl.gank.com.gankwl.view;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import wl.gank.com.gankwl.tools.PermissionUtil;

/**
 * Created by wanli on 2016/6/13.
 */
/**
 *
 * 编写动画步骤：
 *     1.在styles21中设置<item name="android:windowContentTransitions">true</item> 和 style动画
 *     2.在mainfest.xml中的activity中设置theme
 *     3.编写以下代码
 *
 * 编写携带共享元素的动画只需要：
 *     1.在此方法中设置两个参数makeSceneTransitionAnimation（指定当前界面要共享的view，字符串标记）
 *     2.在第二个界面里的需要共享的view属性，设置 android:transitionName="字符串标记"
 *
 *
 *
 * 当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，例如用户回到home，或进入其他程序，
 * 经过一段时间后再返回之前的应用。可通过接口：MobclickAgent.setSessionContinueMillis(long interval) 来自定义这个间隔（参数单位为毫秒）
 * */
public abstract class ContentActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.setSessionContinueMillis(1000*60);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public abstract void initView();
    public abstract void initEvent();


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



    //没有此方法，退出的时候有问题
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void startActivityToWeb(Context context,String uri){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }


    /**
     * 携带共享元素的动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public  void startActivtyByAnimation(Intent intent, View view, String name) {
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, view, name);
        try {
            ActivityCompat.startActivity(this, intent, options.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.startActivity(intent);
        }
    }


    /**
     * 未携带共享元素的动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startActivtyByAnimation(Intent intent) {
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
















    private PermissionUtil.PermissionCallBack callBack;

    protected void requestEscortPermission(PermissionUtil.PermissionCallBack callBack, String... permission) {
        if (lacksPermissions(permission)) {
            PermissionUtil.requestPermissions(this, PermissionUtil.PERMISSION_REQ, permission);
        }
        this.callBack = callBack;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_REQ && hasAllPermissionsGranted(grantResults)) {
            if (callBack != null) callBack.handleProceed(permissions[0]);
        } else {
            if (callBack != null) callBack.handleDenied(permissions[0]);
            PermissionUtil.showMissingPermissionDialog(this);
        }
    }


    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
