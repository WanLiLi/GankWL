package wl.gank.com.gankwl.cache.nativecache;

import android.content.Context;

import java.util.List;

import wl.gank.com.gankwl.cache.CacheConfig;
import wl.gank.com.gankwl.cache.netcache.ListCacheFactory;
import wl.gank.com.gankwl.model.entity.Gank;
import wl.gank.com.gankwl.tools.MLog;

/**
 * Created by wanli on 2016/6/16.
 */
public class CollectionCache {


    /**
     * 获取收藏的
     *
     * */
    public static  List<Gank> getCacheCollection(Context context) {
        ListCacheFactory listCacheFactory = new ListCacheFactory(context, CacheConfig.MYCOLLECTION);
        List<Gank> ganks = listCacheFactory.getList();
        return ganks;
    }

}
