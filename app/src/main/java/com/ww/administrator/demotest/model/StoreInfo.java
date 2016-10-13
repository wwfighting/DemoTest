package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class StoreInfo {


    /**
     * code : 200
     * info : success
     * data : [{"storeId":"629","city":"南京","trueName":"王庆霞","phone":"15062285736","address":"白下区光华路2号石林家居广场10号","storeName":"南京公司光华门店","userType":"2","salerno":"NJ214"},{"storeId":"242","city":"南京","trueName":"田艳","phone":"15358106566","address":"雨花区卡子门大街88号石林家乐家","storeName":"南京公司家乐家店","userType":"2","salerno":"NJ155"},{"storeId":"540","city":"南京","trueName":"王静","phone":"15358106880","address":"大桥北路48号弘阳广场弘阳家居A2馆2315号","storeName":"南京公司桥北店","userType":"2","salerno":"NJ150"},{"storeId":"119","city":"南京","trueName":"经芳","phone":"15358106759","address":"雨花区卡子门红星美凯龙负一楼F8381号","storeName":"南京公司红星店","userType":"2","salerno":"NJ020"},{"storeId":"635","city":"南京","trueName":"李青青","phone":"13914485566","address":"快特兰汀家居有限公司","storeName":"南京公司达美店","userType":"2","salerno":"NJ205"},{"storeId":"185","city":"南京","trueName":"陈君","phone":"15358106127","address":"江宁区东山镇金宝装饰城精品洁具地板厅2号","storeName":"南京公司金宝店","userType":"2","salerno":"NJ101"},{"storeId":"108","city":"南京","trueName":"徐云","phone":"15358106312","address":"建邺区江东中路80号-1楼B21-23号","storeName":"南京公司金盛店","userType":"2","salerno":"NJ008"}]
     */

    private String code;
    private String info;
    /**
     * storeId : 629
     * city : 南京
     * trueName : 王庆霞
     * phone : 15062285736
     * address : 白下区光华路2号石林家居广场10号
     * storeName : 南京公司光华门店
     * userType : 2
     * salerno : NJ214
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
        private String storeId;
        private String city;
        private String trueName;
        private String phone;
        private String address;
        private String storeName;
        private String userType;
        private String salerno;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getSalerno() {
            return salerno;
        }

        public void setSalerno(String salerno) {
            this.salerno = salerno;
        }
    }
}
