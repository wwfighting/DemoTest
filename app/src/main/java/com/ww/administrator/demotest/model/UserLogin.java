package com.ww.administrator.demotest.model;

/**
 * Created by Administrator on 2016/8/19.
 */
public class UserLogin {


    /**
     * code : 200
     * info : 登录成功！
     * data : {"id":"139","username":"8990","nickname":"俞妃","password":"757575","phone":"15358106068","wx_openid":"o78U2v6Zh5RtUjIJwKPZFYYKMWnI","status":"100","createtime":"2015-09-27 01:21:34","headImg":"http://wx.qlogo.cn/mmopen/ajNVdqHZLLAq8171DGiaEfaHdPDetF5XavppaYOicZ7r1mPHNhnkVlZfw4pcsLdAEkjwWsSjJvPuVXoA6rAHEHibw/0","area":"","credit":"1","lasttime":"2016-05-30 12:52:27","num":"50","auth_key":null,"updated_at":null,"email":"null"}
     */

    private String code;
    private String info;
    /**
     * id : 139
     * username : 8990
     * nickname : 俞妃
     * password : 757575
     * phone : 15358106068
     * wx_openid : o78U2v6Zh5RtUjIJwKPZFYYKMWnI
     * status : 100
     * createtime : 2015-09-27 01:21:34
     * headImg : http://wx.qlogo.cn/mmopen/ajNVdqHZLLAq8171DGiaEfaHdPDetF5XavppaYOicZ7r1mPHNhnkVlZfw4pcsLdAEkjwWsSjJvPuVXoA6rAHEHibw/0
     * area :
     * credit : 1
     * lasttime : 2016-05-30 12:52:27
     * num : 50
     * auth_key : null
     * updated_at : null
     * email : null
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String username;
        private String nickname;
        private String password;
        private String phone;
        private String wx_openid;
        private String status;
        private String createtime;
        private String headImg;
        private String area;
        private String credit;
        private String lasttime;
        private String num;
        private Object auth_key;
        private Object updated_at;
        private String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWx_openid() {
            return wx_openid;
        }

        public void setWx_openid(String wx_openid) {
            this.wx_openid = wx_openid;
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

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getLasttime() {
            return lasttime;
        }

        public void setLasttime(String lasttime) {
            this.lasttime = lasttime;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public Object getAuth_key() {
            return auth_key;
        }

        public void setAuth_key(Object auth_key) {
            this.auth_key = auth_key;
        }

        public Object getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(Object updated_at) {
            this.updated_at = updated_at;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
