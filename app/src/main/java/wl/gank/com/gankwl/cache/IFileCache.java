package wl.gank.com.gankwl.cache;

/**
 * Created by wanli on 2016/6/15.
 *
 */
public interface IFileCache<T> {
    public void setPath(String path);
    public boolean putCacheData(T t);
    public T getCacheData();


}
