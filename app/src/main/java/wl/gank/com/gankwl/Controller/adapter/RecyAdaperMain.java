package wl.gank.com.gankwl.Controller.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.func.OnclickItem;
import wl.gank.com.gankwl.model.MeiZhiData;
import wl.gank.com.gankwl.model.entity.Meizhi;
import wl.gank.com.gankwl.rxhttp.MeiZhiService;
import wl.gank.com.gankwl.tools.MLog;
import wl.gank.com.gankwl.widget.BaseApi;

import static android.os.Looper.getMainLooper;

/**
 * Created by wanli on 2016/6/13.
 */
public class RecyAdaperMain extends RecyclerView.Adapter<RecyAdaperMain.ViewHolder> {


    private Context mContext;
    private OnclickItem onclickItem;
    private List<Meizhi> meizhis = new ArrayList<Meizhi>();


    public RecyAdaperMain(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnclickItem(OnclickItem onclickItem) {
        this.onclickItem = onclickItem;
    }


    public int getViewId() {
        return R.layout.item_recy_main;
    }


    @Override
    public int getItemCount() {
        return meizhis.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getViewId(), parent, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        List<MeiZhiData.ResultsBean>  results = Meizhis.get(position).getResults();


        //.transform(new Transformation[0])

        Meizhi meizhi = meizhis.get(position);

        //DiskCacheStrategy:  让Glide既缓存全尺寸又缓存其他尺寸
        //具体说来就是：假如在第一个页面有一个200x200的ImageView，在第二个页面有一个100x100的ImageView，这两个ImageView本来是要显示同一张图片，却需要下载两次。
        String uri = meizhi.getUrl();
        //.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(mContext).load(uri).into(holder.imagItem);



        String desc = meizhi.getDesc();
        holder.txtItem.setText(desc.length() > 20 ? desc.substring(0, 20) + "...." : desc);


        holder.itemView.setOnClickListener(view -> onclickItem.click(view, meizhi));
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imag_item)
        ImageView imagItem;
        @Bind(R.id.cardview_item)
        CardView cardviewItem;
        @Bind(R.id.txt_item)
        TextView txtItem;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    //String uri = "http://gank.io/api/data/福利/10/";


    public int page = 0;

    public void loadData() {
        page += 1;
        okHttpMethod(page);
    }


    public void okHttpMethod(int page) {
        String baseUrl = "http://gank.io/api/";
        //mOnRefreshingListener.setRefreshing(true);

        MLog.d("wanli", "page_okHttpMethod " + page);
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder().url(uri + page).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                MLog.d("wanli", "res_onFailure:" + e.getMessage().toString());
//                new Handler(getMainLooper()).post(() -> {
//                    Toast.makeText(mContext,"请检查网络",Toast.LENGTH_LONG).show();
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String res = response.body().string();
//                MLog.d("wanli", "res_onResponse:" + res);
//
//                Gson gson = new Gson();
//                MeiZhiData meiZhiData = gson.fromJson(res, MeiZhiData.class);
//                meizhis.addAll(meiZhiData.getResults());
//
//                new Handler(getMainLooper()).post(() -> {
//                    if (mOnRefreshingListener != null)
//                        mOnRefreshingListener.setRefreshing(false);
//                    notifyDataSetChanged();
//                });
//            }
//        });


        /**
         * Retrofit
         *
         * */
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        MeiZhiService meiZhiService = retrofit.create(MeiZhiService.class);
//
//        retrofit2.Call<MeiZhiData> Call = meiZhiService.getMeiZhi(10, page);
//
//        Call.enqueue(new retrofit2.Callback<MeiZhiData>() {
//            @Override
//            public void onResponse(retrofit2.Call<MeiZhiData> call, retrofit2.Response<MeiZhiData> response) {
//                String res = response.body().toString();
//                MLog.d("wanli", "res_retrofit" + res);
//                meizhis.addAll(response.body().getResults());
//                //new Handler(getMainLooper()).post(() -> {
//                    if (mOnRefreshingListener != null)
//                        mOnRefreshingListener.setRefreshing(false);
//                    notifyDataSetChanged();
//                //});
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<MeiZhiData> call, Throwable t) {
//                mOnRefreshingListener.setRefreshing(false);
//            }
//        });


        /**
         * Retrofit + RxAndroid
         *
         * 注意doOnSubscribe ，将可以指定进度条，不用在之前写了
         * */
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        MeiZhiService meiZhiService = retrofit.create(MeiZhiService.class);

        meiZhiService.getMeiZhiRx(10, page)
                .map(new Func1<MeiZhiData, List<Meizhi>>() {
                    @Override
                    public List<Meizhi> call(MeiZhiData meiZhiData) {
                        return meiZhiData.getResults();
                    }
                })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mOnRefreshingListener.setRefreshing(true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()) //在 doOnSubscribe()的后面跟一个 subscribeOn() ，就能指定准备工作的线程了
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(observer);
    }





    Observer<List<Meizhi>> observer = new Observer<List<Meizhi>>() {

        @Override
        public void onCompleted() {
              //Toast.makeText(mContext,"加载完毕",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Meizhi> meizhi) {
            meizhis.addAll(meizhi);
            notifyDataSetChanged();
            mOnRefreshingListener.setRefreshing(false);
        }
    };


    public OnRefreshingListener mOnRefreshingListener;

    public interface OnRefreshingListener {
        public void setRefreshing(boolean refreshing);
    }

    public void setOnRefreshingListener(OnRefreshingListener onRefreshingListener) {
        this.mOnRefreshingListener = onRefreshingListener;
    }
}
