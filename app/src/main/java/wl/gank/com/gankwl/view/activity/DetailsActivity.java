package wl.gank.com.gankwl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wl.gank.com.gankwl.Controller.adapter.DetailsAdapter;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.cache.nativecache.CollectionCache;
import wl.gank.com.gankwl.model.GankData;
import wl.gank.com.gankwl.model.entity.Gank;
import wl.gank.com.gankwl.tools.MLog;
import wl.gank.com.gankwl.tools.VersionHelper;
import wl.gank.com.gankwl.view.ContentActivity;
import wl.gank.com.gankwl.widget.BaseApi;

/**
 * Created by wanli on 2016/6/14.
 */
public class DetailsActivity extends ContentActivity {

    public static String EXTRA_DATE = "extra_date";
    public static String EXTRA_URI = "extra_uri";


    @Bind(R.id.image_collapsing)
    ImageView imageCollapsing;
    @Bind(R.id.toolbar_collapsing)
    Toolbar toolbarCollapsing;
    @Bind(R.id.fab_detail)
    FloatingActionButton fabDetail;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;


    String uri;
    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbarCollapsing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    String uriGank = BaseApi.URI + BaseApi.PATH_GANK;

    @Override
    public void initEvent() {
        uri = getIntent().getStringExtra(EXTRA_URI);
        date = getIntent().getStringExtra(EXTRA_DATE);

        setTitle(date);

        uriGank = uriGank + "/" + date;

        //crossFade 淡入淡出  thumbnail 缩略图
        Glide.with(this).load(uri).thumbnail(0.1f).into(imageCollapsing);
        loadGank();
    }


    public void loadGank() {
        gankList = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(uriGank).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                MLog.d("wanli", "res_loadGank " + res);

                Gson gson = new Gson();
                GankData gankData = gson.fromJson(res, GankData.class);
                GankData.Result Results = gankData.getResults();
                addResults(Results);
                new Handler(getMainLooper()).post(() -> refreshGank());
            }
        });
    }


    private DetailsAdapter adapter;

    private void refreshGank() {
        if (gankList == null || gankList.size() == 0) {
            Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
        } else {
            if (adapter != null) {
                adapter.setData(gankList);
                adapter.notifyDataSetChanged();
            } else {
                adapter = new DetailsAdapter(this);
                adapter.setData(gankList);
                recyclerview.setAdapter(adapter);
            }
        }
    }


    private List<Gank> gankList;

    public List<Gank> addResults(GankData.Result results) {
        gankList.addAll(results.getAndroid());
        gankList.addAll(results.getiOS());
        return gankList;
    }


    @OnClick({R.id.image_collapsing, R.id.toolbar_collapsing, R.id.fab_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_collapsing:
                Intent intent = PictureActivity.newIntent(this, uri);
                if (VersionHelper.isAtLeast()) {
                    startActivtyByAnimation(intent, imageCollapsing, PictureActivity.TRANSIT_NAME);
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.toolbar_collapsing:
                break;
            case R.id.fab_detail:
                List<Gank> ganks = CollectionCache.getCacheCollection(this);

                if (ganks != null && ganks.size() > 0) {
                    for (Gank gank : ganks) {
                        MLog.d("wanli", "getCache: " + ganks.size() + "========" + gank.getUrl() + "--" + gank.getPublishedAt() + "--" + gank.getWho());
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
