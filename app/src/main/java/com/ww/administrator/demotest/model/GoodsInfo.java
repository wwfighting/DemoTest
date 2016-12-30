package com.ww.administrator.demotest.model;

import com.ww.administrator.demotest.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class GoodsInfo {


    /**
     * code : 200
     * info : success
     * data : [{"id":"2896","goodsno":"2015CG.01.01.01.001","goodsname":"小康美厨","subtitle":"分类","picurl":"uploads/1443078651.jpg;uploads/1443078666.jpg","price":"6999","classifyid":"3","goodsdesc":"","isrecom":"1","buylimit":"6","tag":"雕刻时光A;15,17,18,19,20;4","brandsid":"5","status":"1","createtime":"2015-10-10 17:14:56","updatetime":"2016-03-2408:51:22","ordercount":"18","unit":"米","recomtype":"0","recomgoods":"","adminid":"1","type":"0","reviewmark":"pass","stock":"22","guige":"3-10米地柜 + 3-10米台面 + 1-5米吊柜","price_old":"13996","price1":"24999","price_old1":"49996","standard_fittings_id":"1"},{"id":"2897","goodsno":"2015CG.03.01.01.001","goodsname":"精英悦厨","subtitle":"分类","picurl":"uploads/1443142494.jpg;uploads/1443142502.jpg;uploads/1443142512.jpg","price":"10999","classifyid":"5","goodsdesc":"","isrecom":"1","buylimit":"6","tag":"迈森B,简爱A,丝丽卡B,丝丽卡C,纸牌屋,挪威海岸;15,17,18,19,20;4,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,22,23,80,","brandsid":"5","status":"1","createtime":"2015-10-10 17:16:24","updatetime":"2016-03-24 09:03:34","ordercount":"15","unit":"米","recomtype":"0","recomgoods":"","adminid":"1","type":"0","reviewmark":"pass","stock":"18","guige":"3-10米地柜 + 3-10米台面 + 1-5米吊柜","price_old":"20996","price1":"28999","price_old1":"74985","standard_fittings_id":"1"}]
     */

    private String code;
    private String info;
    /**
     * id : 2896
     * goodsno : 2015CG.01.01.01.001
     * goodsname : 小康美厨
     * subtitle : 分类
     * picurl : uploads/1443078651.jpg;uploads/1443078666.jpg
     * price : 6999
     * classifyid : 3
     * goodsdesc :
     * isrecom : 1
     * buylimit : 6
     * tag : 雕刻时光A;15,17,18,19,20;4
     * brandsid : 5
     * status : 1
     * createtime : 2015-10-10 17:14:56
     * updatetime : 2016-03-2408:51:22
     * ordercount : 18
     * unit : 米
     * recomtype : 0
     * recomgoods :
     * adminid : 1
     * type : 0
     * reviewmark : pass
     * stock : 22
     * guige : 3-10米地柜 + 3-10米台面 + 1-5米吊柜
     * price_old : 13996
     * price1 : 24999
     * price_old1 : 49996
     * standard_fittings_id : 1
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
        private String goodsno;
        private String goodsname;
        private String subtitle;
        private String picurl;
        private String price;
        private String classifyid;
        private String goodsdesc;
        private String isrecom;
        private String buylimit;
        private String tag;
        private String brandsid;
        private String status;
        private String createtime;
        private String updatetime;
        private String ordercount;
        private String unit;
        private String recomtype;
        private String recomgoods;
        private String adminid;
        private String type;
        private String reviewmark;
        private String stock;
        private String guige;
        private String price_old;
        private String price1;
        private String price_old1;
        private String standard_fittings_id;
        private String img1;
        private String img2;
        private String qid;

        public String getImg1() {
            return img1;
        }

        public void setImg1(String img1) {
            this.img1 = img1;
        }

        public String getImg2() {
            return img2;
        }

        public void setImg2(String img2) {
            this.img2 = img2;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsno() {
            return goodsno;
        }

        public void setGoodsno(String goodsno) {
            this.goodsno = goodsno;
        }

        public String getGoodsname() {
            return goodsname;
        }

        public void setGoodsname(String goodsname) {
            this.goodsname = goodsname;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getClassifyid() {
            return classifyid;
        }

        public void setClassifyid(String classifyid) {
            this.classifyid = classifyid;
        }

        public String getGoodsdesc() {
            return goodsdesc;
        }

        public void setGoodsdesc(String goodsdesc) {
            this.goodsdesc = goodsdesc;
        }

        public String getIsrecom() {
            return isrecom;
        }

        public void setIsrecom(String isrecom) {
            this.isrecom = isrecom;
        }

        public String getBuylimit() {
            return buylimit;
        }

        public void setBuylimit(String buylimit) {
            this.buylimit = buylimit;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getBrandsid() {
            return brandsid;
        }

        public void setBrandsid(String brandsid) {
            this.brandsid = brandsid;
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

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getOrdercount() {
            return ordercount;
        }

        public void setOrdercount(String ordercount) {
            this.ordercount = ordercount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getRecomtype() {
            return recomtype;
        }

        public void setRecomtype(String recomtype) {
            this.recomtype = recomtype;
        }

        public String getRecomgoods() {
            return recomgoods;
        }

        public void setRecomgoods(String recomgoods) {
            this.recomgoods = recomgoods;
        }

        public String getAdminid() {
            return adminid;
        }

        public void setAdminid(String adminid) {
            this.adminid = adminid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReviewmark() {
            return reviewmark;
        }

        public void setReviewmark(String reviewmark) {
            this.reviewmark = reviewmark;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getGuige() {
            return guige;
        }

        public void setGuige(String guige) {
            this.guige = guige;
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

        public String getStandard_fittings_id() {
            return standard_fittings_id;
        }

        public void setStandard_fittings_id(String standard_fittings_id) {
            this.standard_fittings_id = standard_fittings_id;
        }
    }
}
