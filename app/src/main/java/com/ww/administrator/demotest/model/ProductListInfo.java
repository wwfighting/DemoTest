package com.ww.administrator.demotest.model;

import com.ww.administrator.demotest.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
public class ProductListInfo {


    /**
     * code : 200
     * info : success
     * data : [{"id":"3235","picurl":"uploads/1449556706.PNG;uploads/1449556714.PNG;uploads/1449556720.PNG","goodsname":"精英悦厨_丝丽卡B","myordercount":284,"mycommentcount":50,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"},{"id":"2821","picurl":"uploads/1449555160.png;uploads/1449555275.png;uploads/1449555284.png","goodsname":"精英悦厨_简爱A","myordercount":355,"mycommentcount":27,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"},{"id":"2825","picurl":"uploads/1444889885.jpg;uploads/1444889889.jpg;uploads/1444889894.jpg;uploads/1444889898.jpg","goodsname":"精英悦厨_迈森B","myordercount":158,"mycommentcount":55,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"},{"id":"2832","picurl":"uploads/1444889688.jpg;uploads/1444889691.jpg","goodsname":"精英悦厨_挪威海岸","myordercount":215,"mycommentcount":57,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"},{"id":"2835","picurl":"uploads/1449556706.PNG;uploads/1449556714.PNG;uploads/1449556720.PNG","goodsname":"精英悦厨_丝丽卡C","myordercount":281,"mycommentcount":18,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"},{"id":"2838","picurl":"uploads/1459317400.jpg","goodsname":"精英悦厨_纸牌屋","myordercount":222,"mycommentcount":36,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"},{"id":"2897","picurl":"uploads/1443142494.jpg;uploads/1443142502.jpg;uploads/1443142512.jpg","goodsname":"精英悦厨","myordercount":387,"mycommentcount":4,"price":"10999","price_old":"20996","price1":"28999","price_old1":"74985"}]
     */

    private String code;
    private String info;
    /**
     * id : 3235
     * picurl : uploads/1449556706.PNG;uploads/1449556714.PNG;uploads/1449556720.PNG
     * goodsname : 精英悦厨_丝丽卡B
     * myordercount : 284
     * mycommentcount : 50
     * price : 10999
     * price_old : 20996
     * price1 : 28999
     * price_old1 : 74985
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
        private String picurl;
        private String goodsname;
        private int myordercount;
        private int mycommentcount;
        private String price;
        private String price_old;
        private String price1;
        private String price_old1;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicurl() {
            if (picurl.contains(";")){
                String[] url = picurl.split(";");
                return Constants.BASE_IMG_URL + url[0];
            }else {
                return Constants.BASE_IMG_URL + picurl;
            }
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public int getMyordercount() {
            return myordercount;
        }

        public void setMyordercount(int myordercount) {
            this.myordercount = myordercount;
        }

        public int getMycommentcount() {
            return mycommentcount;
        }

        public void setMycommentcount(int mycommentcount) {
            this.mycommentcount = mycommentcount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice_old() {
            return price_old;
        }

        public void setPrice_old(String price_old) {
            this.price_old = price_old;
        }

        public String getPrice1() {
            return price1;
        }

        public void setPrice1(String price1) {
            this.price1 = price1;
        }

        public String getPrice_old1() {
            return price_old1;
        }

        public void setPrice_old1(String price_old1) {
            this.price_old1 = price_old1;
        }
    }
}
