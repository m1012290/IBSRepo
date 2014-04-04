package com.shrinfo.ibs.insured.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface InsuredDao {

    public BaseVO getInsuredetails(BaseVO baseVO);

    public BaseVO createInsuredDetails(BaseVO baseVO);

}
