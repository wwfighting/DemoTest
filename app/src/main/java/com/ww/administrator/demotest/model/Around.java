package com.ww.administrator.demotest.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/17.
 */
public class Around implements Serializable {

    private static final long serialVersionUID = -758459502806858414L;

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
