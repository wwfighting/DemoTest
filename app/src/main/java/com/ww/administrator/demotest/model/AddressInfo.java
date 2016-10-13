package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class AddressInfo {


    /**
     * code : 200
     * info : success
     * data : [{"aid":"786","receivername":"焦雁盛","phone":"8451856969","isdefault":"1","address":"淮海路311号","createtime":"2016-04-06 16:24:55"}]
     */

    private String code;
    private String info;
    /**
     * aid : 786
     * receivername : 焦雁盛
     * phone : 8451856969
     * isdefault : 1
     * address : 淮海路311号
     * createtime : 2016-04-06 16:24:55
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
        private String aid;
        private String receivername;
        private String phone;
        private String isdefault;
        private String address;
        private String createtime;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getReceivername() {
            return receivername;
        }

        public void setReceivername(String receivername) {
            this.receivername = receivername;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIsdefault() {
            return isdefault;
        }

        public void setIsdefault(String isdefault) {
            this.isdefault = isdefault;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
