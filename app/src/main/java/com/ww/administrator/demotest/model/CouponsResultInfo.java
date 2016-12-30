package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CouponsResultInfo {


    /**
     * code : 200
     * info : success
     * data : [{"lpGoodid":"3215","discount":"1","lastTime":"2016-05-31 23:59:59","name":"JJ61221449112187","status":"1","mode":"1、参加12.5-12.27当期活动，缴纳6000元订金后生效，签订合同时使用，一套橱柜产品仅能使用一张奖券。 2、2016新品尝鲜活动价活动不参与本活动（不可使用礼券）。 3、本礼券不可单独使用、折现等其他用途。"}]
     */

    private String code;
    private String info;
    /**
     * lpGoodid : 3215
     * discount : 1
     * lastTime : 2016-05-31 23:59:59
     * name : JJ61221449112187
     * status : 1
     * mode : 1、参加12.5-12.27当期活动，缴纳6000元订金后生效，签订合同时使用，一套橱柜产品仅能使用一张奖券。 2、2016新品尝鲜活动价活动不参与本活动（不可使用礼券）。 3、本礼券不可单独使用、折现等其他用途。
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
        private String lpGoodid;
        private String lpPrice;
        private String discount;
        private String lastTime;
        private String name;
        private String status;
        private String mode;
        private String title;
        private String lpmode;

        public String getLpmode() {
            return lpmode;
        }

        public void setLpmode(String lpmode) {
            this.lpmode = lpmode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLpPrice() {
            return lpPrice;
        }

        public void setLpPrice(String lpPrice) {
            this.lpPrice = lpPrice;
        }

        public String getLpGoodid() {
            return lpGoodid;
        }

        public void setLpGoodid(String lpGoodid) {
            this.lpGoodid = lpGoodid;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
