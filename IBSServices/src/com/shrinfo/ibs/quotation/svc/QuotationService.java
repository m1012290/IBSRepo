package com.shrinfo.ibs.quotation.svc;

import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public interface QuotationService {

    public List<BaseVO> getQuotations(BaseVO baseVO);

    public BaseVO getQuotation(BaseVO baseVO);

    public BaseVO createQuotation(BaseVO baseVO);

    public BaseVO closeQuotation(BaseVO baseVO);

    public BaseVO emailQuotation(BaseVO baseVO);

}
