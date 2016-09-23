package wl.gank.com.gankwl.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import wl.gank.com.gankwl.Controller.adapter.MainAdaper;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.func.OnclickItem;
import wl.gank.com.gankwl.model.entity.Meizhi;
import wl.gank.com.gankwl.model.entity.PgyUpdate;
import wl.gank.com.gankwl.tools.ConverTool;
import wl.gank.com.gankwl.tools.DateUtils;
import wl.gank.com.gankwl.tools.MLog;
import wl.gank.com.gankwl.tools.PermissionUtil;
import wl.gank.com.gankwl.tools.Shares;
import wl.gank.com.gankwl.tools.VersionHelper;
import wl.gank.com.gankwl.view.ContentActivity;

public class MainActivity extends ContentActivity implements View.OnClickListener, MainAdaper.OnRefreshingListener {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefresh;

    private Button btn_collection, btn_github, btn_csnd, btn_tougao;

    private View[] views;
    private NavigationView navigationView;
    public boolean isOpen;


    public RelativeLayout fabReLayout;

    //存储空间,电话,麦克风
    //所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            PermissionUtil.WRITE_EXTERNAL_STORAGE_PERMISSION,
            PermissionUtil.READ_PHONE_STATE_PERMISSION,
            PermissionUtil.RECORD_AUDIO
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
    }


    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(this);

        fabReLayout = (RelativeLayout) findViewById(R.id.fab_reLayout);

        btn_collection = (Button) findViewById(R.id.btn_collection);
        btn_collection.setOnClickListener(this);
        btn_github = (Button) findViewById(R.id.btn_github);
        btn_github.setOnClickListener(this);
        btn_csnd = (Button) findViewById(R.id.btn_csnd);
        btn_csnd.setOnClickListener(this);
        btn_tougao = (Button) findViewById(R.id.btn_tougao);
        btn_tougao.setOnClickListener(this);

        views = new View[]{fab, btn_collection, btn_github, btn_csnd, btn_tougao};


        requestEscortPermission(new PermissionUtil.PermissionCallBack() {
            @Override
            public void handleProceed(String permission) {

            }

            @Override
            public void handleDenied(String permission) {
                Toast.makeText(MainActivity.this, getString(R.string.denied_permission, permission),
                        Toast.LENGTH_SHORT).show();
            }
        }, PERMISSIONS);


        //第一：默认初始化
        Bmob.initialize(this, "9455ea24a37ed59590b0b6e38cc7f23b");

        //
        //PgyUpdateManager.register(MainActivity.this);

        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        MLog.d("wanli", "result :" + result);
                        Gson gson = new Gson();
                        PgyUpdate pgyUpdate = gson.fromJson(result, PgyUpdate.class);
                        String uriUpdate = pgyUpdate.getData().getAppUrl();

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("更新")
                                .setMessage("1.新增分享功能\n2.摇一摇即可用户反馈")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startActivityToWeb(MainActivity.this, uriUpdate);

                                                //安装apk文件
//                                                Intent it = new Intent(Intent.ACTION_VIEW);
//                                                it.setDataAndType(Uri.parse(uriUpdate),
//                                                        "application/vnd.android.package-archive");
//                                                startActivity(it);

                                                //Toast.makeText(MainActivity.this, "测试...", Toast.LENGTH_LONG).show();
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        //不需要更新
                        //Toast.makeText(MainActivity.this, "onNoUpdateAvailable...", Toast.LENGTH_LONG).show();
                    }
                });


        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1000);

        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(MainActivity.this);

        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
        // 打开沉浸式,默认为false
        // FeedbackActivity.setBarImmersive(true);
        PgyFeedbackShakeManager.register(MainActivity.this, false);


        //建表。只需要一次，之后屏蔽。
        //BmobUpdateAgent.initAppVersion(this);


        //考虑到用户流量的限制，目前我们默认在WiFi接入情况下才进行自动提醒。如需要在任意网络环境下都进行更新自动提醒，则请在update调用之前添加以下代码
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        //最常见的自动更新模式是：当用户进入应用首页后，如果处于wifi环境则检测更新，如果有更新，弹出对话框提示有新版本，用户点选更新开始下载更新。实现的方法是，在应用程序入口Activity里的OnCreate()方法中调用如下代码
        //BmobUpdateAgent.update(this);


        /**
         * 手动更新
         *
         * 许多应用的设置界面中都会有检查更新等类似功能，需要用户主动触发而检测更新。它的默认行为基本和自动更新基本一致。
         * 它和自动更新的主要区别是：在这种手动更新的情况下，无论网络状况是否Wifi，
         * 无论用户是否忽略过该版本的更新，都可以像下面的示例一样在按钮的回调中发起更新检查，代替update(Context context)：
         *
         */
        BmobUpdateAgent.forceUpdate(this);

        /**如果你发现调用update方法无反应，可使用下面自定义功能中的监听检测更新的结果提到的方法来监听自动更新的结果,具体如下：*/
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {

                //根据updateStatus来判断更新是否成功
                if (updateStatus == UpdateStatus.Yes) {//版本有更新
                    MLog.d("", "有新版本哦");
                    //Toast.makeText(MainActivity.this, "版本有更新", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.No) {
                    //Toast.makeText(MainActivity.this, "最新版本", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                    Toast.makeText(MainActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.IGNORED) {
                    Toast.makeText(MainActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                    Toast.makeText(MainActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.TimeOut) {
                    Toast.makeText(MainActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);


        //设置对对话框按钮的点击事件的监听
        BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {
            @Override
            public void onClick(int status) {
                // TODO Auto-generated method stub
                switch (status) {
                    case UpdateStatus.Update:
                        Toast.makeText(MainActivity.this, "立即更新", Toast.LENGTH_SHORT).show();
                        String uri = "http://www.pgyer.com/apiv1/app/install?aId=93cfde2afa60e9bd675e4438490fa020&_api_key=cb0e7e6c00fe97cd4eb8ee8bcde9be53";
                        startActivityToWeb(MainActivity.this, uri);
                        break;
                    case UpdateStatus.NotNow:
                        //Toast.makeText(MainActivity.this, "点击了以后再说按钮" , Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Close://只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
                        //Toast.makeText(MainActivity.this, "点击了对话框关闭按钮" , Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();


//            List<Gank> ganks = CollectionCache.getCacheCollection(this);
//            for (Gank gank : ganks) {
//                MLog.d("wanli", "getCache: " + ganks.size() + "========" + gank.getUrl() + "--" + gank.getPublishedAt() + "--" + gank.getWho());
//            }

            if (isOpen) {
                closeAnimation(views);
            } else {
                startAnimation(views);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }


    private int lastPosition;
    private MainAdaper adaperMain;

    @Override
    public void initEvent() {
        swipeRefresh.setColorSchemeResources(R.color.blue, R.color.red, R.color.yellow);

        //主要监听下拉刷新，假请求，只是UI效果
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> {
                    swipeRefresh.setRefreshing(false);
                }, 1500);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        adaperMain = new MainAdaper(this);
        adaperMain.setOnRefreshingListener(this);
        adaperMain.loadData();
        recyclerView.setAdapter(adaperMain);


        adaperMain.setOnclickItem(new OnclickItem() {
            @Override
            public void click(View v, Meizhi meizhi) {
                startGankActivity(v, meizhi);
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                MLog.d("lastPosition", "lastPosition ; " + lastPosition);
                //如果是最后一个
                if (adaperMain.getItemCount() == lastPosition + 1 && dy > 0) {
                    MLog.d("loadMore: ", " 更多");
                    adaperMain.loadData();
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startGankActivity(View shareView, Meizhi meizhi) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_URI, meizhi.getUrl());
        intent.putExtra(DetailsActivity.EXTRA_DATE, DateUtils.dateToStr(meizhi.getPublishedAt()));  //2016-06-15T11:55:46.992Z

        if (VersionHelper.isAtLeast()) {
            //startActivtyByAnimation(intent);
            ImageView image = (ImageView) shareView.findViewById(R.id.imag_item);
            startActivtyByAnimation(intent, image, getString(R.string.transition_fab));
        } else {
            startActivity(intent);
        }
    }

    ObjectAnimator objectAnimator;

    public void startAnimation(View[] views) {
        isOpen = true;
        fabReLayout.setVisibility(View.VISIBLE);
        if (VersionHelper.isAtLeast()) {
            /**
             *ObjectAnimator
             * */
            for (int i = 0; i < views.length; i++) {
                objectAnimator = ObjectAnimator.ofFloat(views[i], "translationY", 0, -(i * 250));
                objectAnimator.setStartDelay(i * 150);
                objectAnimator.setDuration(700);
                objectAnimator.setInterpolator(new BounceInterpolator());
                objectAnimator.start();
            }
        } else {
            /**
             *ViewCompat
             * */
            for (int i = 0; i < views.length; i++) {
                ViewCompat.animate(views[i]).translationY(i * -ConverTool.px2dip(this, 400)).setDuration(600).setInterpolator(new BounceInterpolator()).start();
            }
        }
    }


    private void closeAnimation(View[] views) {
        isOpen = false;
        if (VersionHelper.isAtLeast()) {
            /**
             *ObjectAnimator
             * */
            for (int i = 0; i < views.length; i++) {
                objectAnimator = ObjectAnimator.ofFloat(views[i], "translationY", i * 250, 0);
                objectAnimator.setStartDelay(i * 150);
                objectAnimator.setDuration(500);
                objectAnimator.setInterpolator(new BounceInterpolator());
                objectAnimator.start();
            }
        } else {
            /**
             *ViewCompat
             * */
            for (int i = 0; i < views.length; i++) {
                ViewCompat.animate(views[i]).setDuration(500).translationY(0).start();
            }
        }
        fabReLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_collection:
                if (VersionHelper.isAtLeast()) {
                    startActivtyByAnimation(new Intent(this, CollectionActivity.class));
                } else {
                    startActivity(new Intent(this, CollectionActivity.class));
                }
                break;
            case R.id.btn_github:
                String uri = "http://gank.io/download";
                startActivityToWeb(MainActivity.this, uri);
                break;
            case R.id.btn_csnd:
                startActivity(new Intent(this, AuthorActivity.class));
                break;
            case R.id.toolbar:
                recyclerView.smoothScrollToPosition(lastPosition - 10 <= 0 ? 0 : lastPosition - 10);
                break;
            default:
                Toast.makeText(MainActivity.this, "待开发", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String shareText = getString(R.string.author) + getString(R.string.share_from);
                Shares.share(this, shareText);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        //在oncreate方法中setRefreshing(true)不起作用的原因是因为在oncreate中,onLayout都不知道自己的尺寸，所以当oncreate中网络请求的时候需要刷新，但是
        //还不知道自己的尺寸，没有办法启动setRefreshing(true),所以判断是否已经onLayout完毕。如果没有完毕则强制偏移，否则正常刷新。
        //如果没有此判断会影响onLayout完毕后，刷新仍然偏移
        if (swipeRefresh.getMeasuredWidth() == 0) {
            swipeRefresh.setProgressViewOffset(false, 0, ConverTool.dip2px(this, 24));
        }
        swipeRefresh.setRefreshing(refreshing);
    }


    @Override
    protected void onStop() {
        super.onStop();
        adaperMain.unsubscribeMy();
    }
}
