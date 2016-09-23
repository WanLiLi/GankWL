package wl.gank.com.gankwl.Controller.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import wl.gank.com.gankwl.R;
import wl.gank.com.gankwl.func.OnclickItem;
import wl.gank.com.gankwl.model.MeiZhiData;
import wl.gank.com.gankwl.model.entity.Meizhi;
import wl.gank.com.gankwl.rxhttp.GankRetrofit;
import wl.gank.com.gankwl.rxhttp.MeiZhiApi;
import wl.gank.com.gankwl.tools.MLog;

/**
 * Created by wanli on 2016/6/13.
 */
public class MainAdaper extends RecyclerView.Adapter<MainAdaper.ViewHolder> {


    private Context mContext;
    private OnclickItem onclickItem;
    private List<Meizhi> meizhis = new ArrayList<Meizhi>();


    public MainAdaper(Context mContext) {
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


        Meizhi meizhi = meizhis.get(position);

        //.transform(new Transformation[0])
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


    public int page = 0;

    public void loadData() {
        page += 1;
        okHttpMethod(page);
    }

    //String uri = "http://gank.io/api/data/福利/10/";
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
//        Retrofit.Builder builder = new Retrofit.Builder();
//        builder.baseUrl(baseUrl);
//        builder.addConverterFactory(GsonConverterFactory.create());
//        Retrofit retrofit = builder.build();
//        MeiZhiService meiZhiService = retrofit.create(MeiZhiService.class);
//
//        retrofit2.Call<MeiZhiData> Call = meiZhiService.getMeiZhi(10, page);
//
//        Call.enqueue(new retrofit2.Callback<MeiZhiData>() {
//            @Override
//            public void onResponse(retrofit2.Call<MeiZhiData> call, retrofit2.Response<MeiZhiData> response) {
//                //TODO.可以直接更改UI，因为onResponse方法已经在UI线程中
//                String res = response.body().toString();
//                MLog.d("wanli", "res_retrofit" + res);
//
//                /**
//                 *
//                 * 解析过的Json
//                 * */
//                meizhis.addAll(response.body().getResults());
//                //new Handler(getMainLooper()).post(() -> {
//                if (mOnRefreshingListener != null)
//                    mOnRefreshingListener.setRefreshing(false);
//                notifyDataSetChanged();
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
         * */
        GankRetrofit.getMeiZhiApi().getMeiZhiRx(10, page)
                .map(new Func1<MeiZhiData, List<Meizhi>>() {
                    @Override
                    public List<Meizhi> call(MeiZhiData meiZhiData) {
                        return meiZhiData.getResults();
                    }
                })
                .flatMap(new Func1<List<Meizhi>, Observable<Meizhi>>() {
                    @Override
                    public Observable<Meizhi> call(List<Meizhi> meizhis) {
                        return Observable.from(meizhis);
                    }
                })
                .toSortedList(new Func2<Meizhi, Meizhi, Integer>() {
                    @Override
                    public Integer call(Meizhi meizhi, Meizhi meizhi2) {
                        return meizhi2.getPublishedAt().compareTo(meizhi.getPublishedAt());
                    }
                })                                                                               // toSortedList后将还是发送List<Meizhi>类型数据，因为rxjava知道你用flatMap只是为了数据的排序，
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {                                                   // subscribe调用后并且在事件发送前，执行，做为准备工作
                    @Override
                    public void call() {
                        mOnRefreshingListener.setRefreshing(true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())                                      //在 doOnSubscribe()的后面跟一个 subscribeOn() ，就能指定准备工作的线程了
                .observeOn(AndroidSchedulers.mainThread())                                        //指定 Subscriber(观察者机器人) 的回调发生在主线程  //指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                .subscribe(observer);


        //                   new Action1<List<Meizhi>>() {
//                            @Override
//                            public void call(List<Meizhi> meizhi) {
//                                meizhis.addAll(meizhi);
//                                notifyDataSetChanged();
//                                mOnRefreshingListener.setRefreshing(false);
//                            }
//                        }

    }

    Subscriber<List<Meizhi>> subscriber = new Subscriber<List<Meizhi>>() {
        //        它总是在 subscribe 所发生的线程被调用，而不能指定线程
        //它会在 subscribe 刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，例如数据的清零或重置。
        @Override
        public void onStart() {
            super.onStart();
        }


        @Override
        public void onNext(List<Meizhi> meizhi) {
            meizhis.addAll(meizhi);
            notifyDataSetChanged();
        }

        //当你执行了观察者回调了subscriber.onCompleted();，那么这个订阅关系就会解绑，解绑了你的onNext将不会执行。
        @Override
        public void onCompleted() {
            mOnRefreshingListener.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {

        }
    };


    public void unsubscribeMy() {
        if (subscriber.isUnsubscribed()) {
            /**因为在 subscribe() 之后， Observable 会持有 Subscriber 的引用，这个引用如果不能及时被释放，将有内存泄露的风险。*/
            subscriber.unsubscribe();
        }
    }


    /**
     * Observer 也总是会先被转换成一个 Subscriber 再使用。所以如果你只想使用基本功能，选择 Observer 和 Subscriber 是完全一样的，但是Observer在 subscribe()过程中最终会被转换成Subscriber对象。
     * 所以，你用了Observer和在订阅后其实就相当于你用了Subscriber。
     * http://www.jianshu.com/p/44d789d8c09c
     */
    Observer<List<Meizhi>> observer = new Observer<List<Meizhi>>() {
        @Override
        public void onCompleted() {
            mOnRefreshingListener.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Meizhi> meizhi) {
            meizhis.addAll(meizhi);
            notifyDataSetChanged();
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
