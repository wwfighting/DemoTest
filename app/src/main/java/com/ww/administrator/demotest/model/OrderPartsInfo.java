package com.ww.administrator.demotest.model;

/**
 * Created by Administrator on 2016/10/8.
 */
public class OrderPartsInfo<T> {


    /**
     * code : 200
     * info : success
     * data : {"superbillid":99902818,"price":"5000","allSchedprice":200000,"goodsName":"小康美厨_雕刻时光A","imgurl":"uploads/1449650214.PNG"}
     */

    private String code;
    private String info;
    /**
     * superbillid : 99902818
     * price : 5000
     * allSchedprice : 200000
     * goodsName : 小康美厨_雕刻时光A
     * imgurl : uploads/1449650214.PNG
     */

    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class T {
        private int superbillid;
        private String price;
        private int allSchedprice;
        private String goodsName;
        private String imgurl;

        public int getSuperbillid() {
            return superbillid;
        }

        public void setSuperbillid(int superbillid) {
            this.superbillid = superbillid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getAllSchedprice() {
            return allSchedprice;
        }

        public void setAllSchedprice(int allSchedprice) {
            this.allSchedprice = allSchedprice;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
