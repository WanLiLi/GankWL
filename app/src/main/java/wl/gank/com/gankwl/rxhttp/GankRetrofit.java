package wl.gank.com.gankwl.rxhttp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wanli on 2016/9/22.
 */
public class GankRetrofit {
    private static MeiZhiApi meiZhiApi;
    private static String baseUrl = "http://gank.io/api/";
    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public static MeiZhiApi getMeiZhiApi() {
        if (meiZhiApi == null) {
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .readTimeout(12, TimeUnit.SECONDS)
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder();
            Retrofit retrofit = builder
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            meiZhiApi = retrofit.create(MeiZhiApi.class);
        }
        return meiZhiApi;
    }

}
