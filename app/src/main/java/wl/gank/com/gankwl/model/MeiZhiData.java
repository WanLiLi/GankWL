package wl.gank.com.gankwl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wl.gank.com.gankwl.model.entity.Meizhi;

/**
 * Created by wanli on 2016/6/14.
 */
public class MeiZhiData  extends BaseData{

    /**
     * error : false
     * results : [{"_id":"575e0824421aa9296bddf5a4","createdAt":"2016-06-13T09:11:00.530Z","desc":"直接看图，，不用看描述。","publishedAt":"2016-06-14T11:52:47.320Z","source":"web","type":"福利","url":"http://ww3.sinaimg.cn/mw690/81309c56jw1f4sx4ybttdj20ku0vd0ym.jpg","used":true,"who":"龙龙童鞋"},{"_id":"575cbba1421aa96b24382520","createdAt":"2016-06-12T09:32:17.746Z","desc":"跟上一个是一个系列的。","publishedAt":"2016-06-13T11:38:17.247Z","source":"web","type":"福利","url":"http://ww4.sinaimg.cn/mw690/9844520fjw1f4fqrpw1fvj21911wlb2b.jpg","used":true,"who":"龙龙童鞋"}]
     */

    private boolean error;
    /**
     * _id : 575e0824421aa9296bddf5a4
     * createdAt : 2016-06-13T09:11:00.530Z
     * desc : 直接看图，，不用看描述。
     * publishedAt : 2016-06-14T11:52:47.320Z
     * source : web
     * type : 福利
     * url : http://ww3.sinaimg.cn/mw690/81309c56jw1f4sx4ybttdj20ku0vd0ym.jpg
     * used : true
     * who : 龙龙童鞋
     */

    private List<Meizhi> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Meizhi> getResults() {
        return results;
    }

    public void setResults(List<Meizhi> results) {
        this.results = results;
    }
}
