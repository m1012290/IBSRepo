package com.shrinfo.ibs.quoteslip.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface QuoteSlipDao {

    public BaseVO getQuoteSlipDetails(BaseVO baseVO);

    public BaseVO createQuoteSlip(BaseVO baseVO);
    
    public BaseVO updateEmailedQuoteSlipFlag(BaseVO baseVO);

}
