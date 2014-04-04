package com.shrinfo.ibs.enquiry.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface EnquiryDao {
    
    public BaseVO getEnquiryDetail(BaseVO baseVO);

    public BaseVO createEnquiry(BaseVO baseVO);

}
