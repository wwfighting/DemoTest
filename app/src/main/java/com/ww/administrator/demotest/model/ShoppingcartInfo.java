package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShoppingcartInfo {


    /**
     * code : 200
     * info : success
     * data : [{"id":"2862","goodsname":"名家雅厨_普瑞达A","imgurl":"uploads/1446711418.jpg","price":"14999","subtitle":"","buycount":"1"},{"id":"3062","goodsname":"二级过滤净水器-OLO-JS-02","imgurl":"uploads/1446513097.png","price":"1248","subtitle":"配件","buycount":"5"},{"id":"3130","goodsname":"台面边型-LMB07","imgurl":"uploads/1466391728.png","price":"1629","subtitle":"配件","buycount":"1"},{"id":"2991","goodsname":"米箱OLO-MX25","imgurl":"uploads/1446449359.png","price":"1012","subtitle":"配件","buycount":"3"}]
     */

    private String code;
    private String info;
    /**
     * id : 2862
     * goodsname : 名家雅厨_普瑞达A
     * imgurl : uploads/1446711418.jpg
     * price : 14999
     * subtitle :
     * buycount : 1
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
        private String goodsname;
        private String imgurl;
        private String price;
        private String subtitle;
        private String buycount;
        private boolean isSelected;
        private int orderMoney;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getOrderMoney() {
            return orderMoney;
        }

        public void setOrderMoney(int orderMoney) {
            this.orderMoney = orderMoney;
        }

        public String getGoodsname() {

            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getImgurl() {
            return imgurl;
        }

        public boolean getisSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getBuycount() {
            return buycount;
        }

        public void setBuycount(String buycount) {
            this.buycount = buycount;
        }
    }
}
