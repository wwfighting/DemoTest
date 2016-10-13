package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CateInfo {


    /**
     * code : 200
     * info : success
     * data : [{"id":"1","name":"橱柜","status":"1","parentid":"0"},{"id":"2","name":"全屋定制","status":"1","parentid":"0"},{"id":"64","name":"五金","status":"1","parentid":"0"},{"id":"65","name":"台面、边型及垫条","status":"1","parentid":"0"},{"id":"66","name":"水槽、龙头","status":"1","parentid":"0"},{"id":"67","name":"净水器","status":"1","parentid":"0"},{"id":"68","name":"厨电","status":"1","parentid":"0"},{"id":"69","name":"照明","status":"1","parentid":"0"},{"id":"74","name":"礼品","status":"1","parentid":"0"},{"id":"3","name":"小康美厨","status":"1","parentid":"1"}]
     */

    private String code;
    private String info;
    /**
     * id : 1
     * name : 橱柜
     * status : 1
     * parentid : 0
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
        private String name;
        private String status;
        private String parentid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }
    }
}
