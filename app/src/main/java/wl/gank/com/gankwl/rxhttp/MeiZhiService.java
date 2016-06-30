package wl.gank.com.gankwl.rxhttp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wl.gank.com.gankwl.model.MeiZhiData;


/**
 * Created by wanli on 2016/6/22.
 */
public interface MeiZhiService {
//    //http://gank.io/api/

    @GET("data/福利/{number}/{page}")
    Call<MeiZhiData> getMeiZhi(@Path("number") int number, @Path("page") int page);


    @GET("data/福利/{number}/{page}")
    Observable<MeiZhiData> getMeiZhiRx(@Path("number") int number, @Path("page") int page);
}
