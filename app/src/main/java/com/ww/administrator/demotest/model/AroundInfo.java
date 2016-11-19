package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/17.
 */
public class AroundInfo{


    /**
     * code : 200
     * info : success
     * data : [{"uid":"138","nickname":"JOE","phone":"15358106777","latitude":"31.928787","longitude":"118.846325"},{"uid":"139","nickname":"俞妃","phone":"15358106068","latitude":"31.919787","longitude":"118.836315"},{"uid":"255","nickname":"Fringe","phone":"18115168660","latitude":"31.916687","longitude":"118.856415"}]
     */

    private String code;
    private String info;
    /**
     * uid : 138
     * nickname : JOE
     * phone : 15358106777
     * latitude : 31.928787
     * longitude : 118.846325
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
        private String uid;
        private String nickname;
        private String phone;
        private String latitude;
        private String longitude;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
