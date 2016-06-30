package wl.gank.com.gankwl.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import wl.gank.com.gankwl.model.entity.Gank;

/**
 * Created by wanli on 2016/6/14.
 */
public class GankData {
    private String error;
    private Result results;
    private List<String> category;

    public class Result{
        private List<Gank> Android;
        private List<Gank> iOS;
        private List<Gank> 休息视频;
        private List<Gank> 前端;

        public List<Gank> get福利() {
            return 福利;
        }

        public void set福利(List<Gank> 福利) {
            this.福利 = 福利;
        }

        public List<Gank> getAndroid() {
            return Android;
        }

        public void setAndroid(List<Gank> android) {
            Android = android;
        }

        public List<Gank> getiOS() {
            return iOS;
        }

        public void setiOS(List<Gank> iOS) {
            this.iOS = iOS;
        }

        public List<Gank> get休息视频() {
            return 休息视频;
        }

        public void set休息视频(List<Gank> 休息视频) {
            this.休息视频 = 休息视频;
        }

        public List<Gank> get前端() {
            return 前端;
        }

        public void set前端(List<Gank> 前端) {
            this.前端 = 前端;
        }

        private List<Gank> 福利;
    }



    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Result getResults() {
        return results;
    }

    public void setResults(Result results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



}
