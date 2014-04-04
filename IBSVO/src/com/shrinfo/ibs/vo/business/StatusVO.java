package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public class StatusVO extends BaseVO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4691986990373878852L;

    private Integer code;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
