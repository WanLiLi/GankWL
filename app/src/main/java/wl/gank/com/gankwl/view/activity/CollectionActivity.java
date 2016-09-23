package wl.gank.com.gankwl.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import wl.gank.com.gankwl.Controller.adapter.CollectionAdapter;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.cache.CacheConfig;
import wl.gank.com.gankwl.cache.nativecache.CollectionCache;
import wl.gank.com.gankwl.cache.netcache.ListCacheFactory;
import wl.gank.com.gankwl.model.entity.Gank;
import wl.gank.com.gankwl.tools.MLog;
import wl.gank.com.gankwl.view.ContentActivity;

public class CollectionActivity extends ContentActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview_collection)
    RecyclerView recyclerviewCollection;

    private CollectionAdapter adapter;
    private List<Gank> gankList = Collections.synchronizedList(new ArrayList<>());


    //获取选中的gankList的下标
    private List<Integer> count = Collections.synchronizedList(new ArrayList<Integer>());

    private boolean isOpen;
    private List<Gank> gankList2;  //专门用来对比值的集合，不需要操作
    private Boolean isUpadte;      //是否有删除的操作

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("我的收藏");

        initView();
        initEvent();
    }

    @Override
    public void initView() {
        recyclerviewCollection.setLayoutManager(new LinearLayoutManager(this));
        gankList = CollectionCache.getCacheCollection(this);
    }

    @Override
    public void initEvent() {
        refreshData();
    }


    private void refreshData() {
        if (gankList != null) {
            int size = gankList.size();
            MLog.d("wanli", "size：" + size);
        } else {
            MLog.d("wanli", "gankList null：");
        }
        if (gankList != null && gankList.size() > 0) {
            for (Gank gank : gankList) {
                MLog.d("wanli", "getCache-collection: " + gankList.size() + "========" + gank.getUrl() + "--" + gank.getPublishedAt() + "--" + gank.getWho());
            }
        }
        //Collections.reverse(gankList);
        adapter = new CollectionAdapter(this) {
            @Override
            public void OnclickItem(int position, boolean isChecked) {
                if (isChecked) {
                    count.add(position);
                    for (int i = 0; i < count.size(); i++) {
                        Integer value = count.get(i);
                        MLog.d("wanli", "OnclickItem: " + value);
                    }

                } else {
                    //如果值等于我选中的下标
                    for (int i = 0; i < count.size(); i++) {
                        if (count.get(i) == position) {
                            //根据值移除
                            count.remove(Integer.valueOf(position));
                        }
                    }
                    for (int i = 0; i < count.size(); i++) {
                        Integer value = count.get(i);
                        MLog.d("wanli", "OnclickItem: " + value);
                    }
                }
            }
        };

        adapter.setData(gankList);
        recyclerviewCollection.setAdapter(adapter);
//        }

//        else {
//            ViewStub viewStub = (ViewStub) findViewById(R.id.viewStub);
//            viewStub.inflate();
//            //TextView txt = (TextView) viewStub.findViewById(R.id.txt_nodata);
//        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        ButterKnife.unbind(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collnection, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (isOpen) {
                    //关闭
                    isOpen = false;
                    isUpadte = false;

                    //新建一个用于比较删除的集合
                    gankList2 = new ArrayList<>();
                    gankList2.addAll(gankList);
                    //如果有选中的状态
                    for (int i = 0; i < count.size(); i++) {
                        int value = count.get(i);
                        MLog.d("wanli", "count value；" + value);


                        //用list的复杂问题就是删除多选数据，因为每次删除都会自动向上滚动，下标-1
                        //所以此处新建一个相同的集合，判断是否有有一样的值，然后删除，不管下标是多少，删除了就好
                        for (int j = 0; j < gankList.size(); j++) {
                            //如果匹配值相同，则删除当前下标
                            if (gankList.get(j).getUrl().equals(gankList2.get(value).getUrl())) {
                                adapter.notifyItemRemoved(j);
                                gankList.remove(j);
                                isUpadte = true;
                                break;
                            }
                        }
                    }

                    count.clear();  //清空
                    adapter.setIsOpen(isOpen);
                    adapter.setData(gankList);

                    //有选中删除的数据，延迟刷新
                    if (gankList != null && gankList.size() > 0) {
                        new Handler(getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        }, 350);
                        //没有选中的动画，不用延迟
                    } else {
                        adapter.notifyDataSetChanged();
                    }


                    //如果有删除的数据，则重新保存到文件
                    if (isUpadte) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ListCacheFactory listCacheFactory = new ListCacheFactory<Gank>(CollectionActivity.this, CacheConfig.MYCOLLECTION);
                                listCacheFactory.cacheList(gankList);
                            }
                        }).start();
                    }

                } else {
                    //打开
                    isOpen = true;
                    if (gankList != null && gankList.size() > 0) {
                        for (int i = 0; i < gankList.size(); i++) {
                            gankList.get(i).setCheck(false);
                            //MLog.d("wanli", " onOptionsItemSelected gankList；" + gankList.size() + "==" + gankList.get(i).getUrl());
                        }
                        adapter.setIsOpen(isOpen);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "快去收藏喜欢的知识", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startWeb(String uri) {
        startActivityToWeb(this, uri);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
