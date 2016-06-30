package wl.gank.com.gankwl.cache.netcache;

import android.content.Context;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wl.gank.com.gankwl.cache.ListCache;

/**
 * Created by wanli on 2016/6/15.
 */
public class ListCacheFactory<T> {

    private File cacheFile;
    private ListCache<T> listCache;


    public ListCacheFactory(Context context, String fileName) {
        File dir = context.getFilesDir();  // data/data/com.mbox.cn/files
        if (!dir.exists()) dir.mkdir();
        File eidDir = new File(dir + File.separator);
        if (!eidDir.exists()) eidDir.mkdir();  // data/data/com.mbox.cn/files/
        String path = eidDir.getAbsolutePath() + File.separator + fileName; // data/data/com.mbox.cn/files/ fileName
        cacheFile = new File(path);
        if (!cacheFile.exists()) {
            try {
                cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        listCache = new ListCache<T>();
        listCache.setPath(path);       //保存文件路径
    }

    public List<T> getList() {
        return listCache.getCacheData();
    }




    public void cacheList(List<T> list) {
        listCache.putCacheData(list);
    }



    /**
     * 保存至第一个
     *
     * */
    public void cacheObjectOfList(T t) {
        //先读取然后在缓存
        List<T> list = listCache.getCacheData();
        if (list == null) list = new ArrayList<T>();
        list.add(0,t);  //保存到下标0
        listCache.putCacheData(list);
    }


    /***
     * 删除指定的
     *
     *
     * */
    public void removeObjectOfList(int position) {
        List<T> list = listCache.getCacheData();
        if (list == null || list.size() == 0) return;
        list.remove(position);
        listCache.putCacheData(list);
    }


    /***
     * 删除所有的
     *
     *
     * */
    public void removeObjectOfList() {
        List<T> list = listCache.getCacheData();
        if (list == null || list.size() == 0) return;
        list.removeAll(list);
        listCache.putCacheData(list);
    }
}
