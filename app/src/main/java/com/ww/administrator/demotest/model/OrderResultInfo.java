package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class OrderResultInfo {


    /**
     * code : 200
     * info : success
     * data : [{"erpstatus":"商机单","subbillid":"99940894","superbillid":"99901539","picurl":"uploads/1449555160.png;uploads/1449555275.png;uploads/1449555284.png","goodsname":"精英悦厨_简爱A","billprice":"10999","status":"1","schedprice":"2000","color":"diy预约，预约金","createtime":"2016-02-26 11:25:52","yyid":"SJ2016022617561","price":"10999","buynum":"1"},{"erpstatus":"商机单","subbillid":"99941008","superbillid":"99901653","picurl":"uploads/1449555160.png;uploads/1449555275.png;uploads/1449555284.png","goodsname":"精英悦厨_简爱A","billprice":"10999","status":"1","schedprice":"2000","color":"diy预约，预约金","createtime":"2016-02-29 15:23:53","yyid":"SJ2016022917839","price":"10999","buynum":"1"},{"erpstatus":"商机单","subbillid":"99940960","superbillid":"99901605","picurl":"uploads/1449650214.PNG;uploads/1449650223.PNG","goodsname":"小康美厨_雕刻时光A","billprice":"6999","status":"1","schedprice":"2000","color":"diy预约，预约金","createtime":"2016-02-27 18:31:17","yyid":"SJ2016022717633","price":"6999","buynum":"1"}]
     */

    private String code;
    private String info;
    /**
     * erpstatus : 商机单
     * subbillid : 99940894
     * superbillid : 99901539
     * picurl : uploads/1449555160.png;uploads/1449555275.png;uploads/1449555284.png
     * goodsname : 精英悦厨_简爱A
     * billprice : 10999
     * status : 1
     * schedprice : 2000
     * color : diy预约，预约金
     * createtime : 2016-02-26 11:25:52
     * yyid : SJ2016022617561
     * price : 10999
     * buynum : 1
     */

    private List<Databean> data;

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

    public List<Databean> getData() {
        return data;
    }

    public void setData(List<Databean> data) {
        this.data = data;
    }

    public static class Databean {
        private String erpstatus;
        private String subbillid;
        private String superbillid;
        private String picurl;
        private String goodsname;
        private String billprice;
        private String status;
        private String schedprice;
        private String color;
        private String createtime;
        private String yyid;
        private String price;
        private String buynum;

        public String getErpstatus() {
            return erpstatus;
        }

        public void setErpstatus(String erpstatus) {
            this.erpstatus = erpstatus;
        }

        public String getSubbillid() {
            return subbillid;
        }

        public void setSubbillid(String subbillid) {
            this.subbillid = subbillid;
        }

        public String getSuperbillid() {
            return superbillid;
        }

        public void setSuperbillid(String superbillid) {
            this.superbillid = superbillid;
        }

        public String getPicurl() {
            return picurl;
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

        public String getBillprice() {
            return billprice;
        }

        public void setBillprice(String billprice) {
            this.billprice = billprice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSchedprice() {
            return schedprice;
        }

        public void setSchedprice(String schedprice) {
            this.schedprice = schedprice;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getYyid() {
            return yyid;
        }

        public void setYyid(String yyid) {
            this.yyid = yyid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBuynum() {
            return buynum;
        }

        public void setBuynum(String buynum) {
            this.buynum = buynum;
        }
    }
}
