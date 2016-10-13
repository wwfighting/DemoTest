package com.ww.administrator.demotest.model;

import com.ww.administrator.demotest.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class BannerInfo {

    /**
     * code : 200
     * info : success
     * data : [{"id":"27","type":"1","imgurl":"uploads/1461914226.jpg","href":"productDetical.php?id=3234","sort":"-1","status":"1","createtime":"2016-04-29 15:19:01"},{"id":"29","type":"1","imgurl":"uploads/1464942882.jpg","href":"http://www.jvawa.com/new/game/AprilSpecial/special.php","sort":"1","status":"1","createtime":"2016-06-03 16:22:32"},{"id":"30","type":"1","imgurl":"http://www.jvawa.com/uploads/1466997596.png","href":"peijianDetical.php?id=3475","sort":"-1","status":"1","createtime":"2016-06-27 11:04:31"}]
     */

    private String code;
    private String info;
    /**
     * id : 27
     * type : 1
     * imgurl : uploads/1461914226.jpg
     * href : productDetical.php?id=3234
     * sort : -1
     * status : 1
     * createtime : 2016-04-29 15:19:01
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String type;
        private String imgurl;
        private String href;
        private String sort;
        private String status;
        private String createtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImgurl() {
            return Constants.BASE_IMG_URL + imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
