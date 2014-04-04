package com.shrinfo.ibs.vo.business;

import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public class SearchVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = 7014452341462513452L;

    List<SearchItemVO> searchItems;


    public List<SearchItemVO> getSearchItems() {
        return searchItems;
    }


    public void setSearchItems(List<SearchItemVO> searchItems) {
        this.searchItems = searchItems;
    }


}
