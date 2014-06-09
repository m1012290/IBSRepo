package com.shrinfo.ibs.quoteslip.svc;

import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;


public interface QuoteSlipService {

    public BaseVO getQuoteSlipDetails(BaseVO baseVO);

    public BaseVO getInsuredDetails(BaseVO baseVO);

    public BaseVO createQuoteSlip(BaseVO baseVO);

    public BaseVO createQuoteSlipFile(QuoteDetailVO quoteDetails, InsuredVO insuredDetails,
            ContactVO contacts, String companyName, String filePath, String imagePath);

    public BaseVO emailQuoteSlip(BaseVO baseVO, List<BaseVO> companies);

    public BaseVO updateEmailedQuoteSlipFlag(BaseVO baseVO);
    
    public BaseVO createInsuredDetails(BaseVO baseVO);
}
