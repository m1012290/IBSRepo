package com.shrinfo.ibs.vo.business;

import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public class InsCompanyListVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -8788820912130223020L;

    private List<InsCompanyVO> companiList;


    /**
     * @return the companiList
     */
    public List<InsCompanyVO> getCompaniList() {
        return companiList;
    }


    /**
     * @param companiList the companiList to set
     */
    public void setCompaniList(List<InsCompanyVO> companiList) {
        this.companiList = companiList;
    }


    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
