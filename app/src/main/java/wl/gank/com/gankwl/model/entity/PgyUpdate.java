package wl.gank.com.gankwl.model.entity;

/**
 * Created by wanli on 2016/6/21.
 */
public class PgyUpdate extends Soul {


    /**
     * code : 0
     * message :
     * data : {"lastBuild":"2","downloadURL":"http://app2.pgyer.com/016542c1aeedf132a4d1c522ead56559.apk?v=1.1&sign=2b5c7910092a06313fedc75a6b2c39ef&t=5768dfca&attname=app-gankWL2.apk","versionCode":"2","versionName":"1.1","appUrl":"http://www.pgyer.com/GankWL","build":"2","releaseNote":"新增分享"}
     */

    private int code;
    private String message;
    /**
     * lastBuild : 2
     * downloadURL : http://app2.pgyer.com/016542c1aeedf132a4d1c522ead56559.apk?v=1.1&sign=2b5c7910092a06313fedc75a6b2c39ef&t=5768dfca&attname=app-gankWL2.apk
     * versionCode : 2
     * versionName : 1.1
     * appUrl : http://www.pgyer.com/GankWL
     * build : 2
     * releaseNote : 新增分享
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String lastBuild;
        private String downloadURL;
        private String versionCode;
        private String versionName;
        private String appUrl;
        private String build;
        private String releaseNote;

        public String getLastBuild() {
            return lastBuild;
        }

        public void setLastBuild(String lastBuild) {
            this.lastBuild = lastBuild;
        }

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public String getBuild() {
            return build;
        }

        public void setBuild(String build) {
            this.build = build;
        }

        public String getReleaseNote() {
            return releaseNote;
        }

        public void setReleaseNote(String releaseNote) {
            this.releaseNote = releaseNote;
        }
    }
}
