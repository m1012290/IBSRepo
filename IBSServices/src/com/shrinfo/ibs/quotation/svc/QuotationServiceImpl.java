package com.shrinfo.ibs.quotation.svc;

import java.util.List;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.quotation.dao.QuotationDao;

public class QuotationServiceImpl extends BaseService implements QuotationService {

    QuotationDao quotationDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getQuotations".equals(methodName)) {
            returnValue = getQuotations((BaseVO) args[0]);
        }
        if ("getQuotation".equals(methodName)) {
            returnValue = getQuotation((BaseVO) args[0]);
        }
        if ("createQuotation".equals(methodName)) {
            returnValue = createQuotation((BaseVO) args[0]);
        }
        if ("closeQuotation".equals(methodName)) {
            returnValue = closeQuotation((BaseVO) args[0]);
        }
        if ("emailQuotation".equals(methodName)) {
            returnValue = emailQuotation((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public List<BaseVO> getQuotations(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO getQuotation(BaseVO baseVO) {
        return quotationDao.getQuotation(baseVO);
    }

    @Override
    public BaseVO createQuotation(BaseVO baseVO) {
        return quotationDao.createQuotation(baseVO);
    }

    @Override
    public BaseVO closeQuotation(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO emailQuotation(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setQuotationDao(QuotationDao quotationDao) {
        this.quotationDao = quotationDao;
    }

}
