package com.shrinfo.ibs.enquiry.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * 
 * @author Rahul
 * 
 */

public interface EnquiryService {

    public BaseVO getEnquiryDetail(BaseVO baseVO);

    public BaseVO createEnquiry(BaseVO baseVO);


}
