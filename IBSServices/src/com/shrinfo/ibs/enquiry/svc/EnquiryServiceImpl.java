package com.shrinfo.ibs.enquiry.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.enquiry.dao.EnquiryDao;


public class EnquiryServiceImpl extends BaseService implements EnquiryService {

    EnquiryDao enquiryDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getEnquiryDetail".equals(methodName)) {
            returnValue = getEnquiryDetail((BaseVO) args[0]);
        }
        if ("createEnquiry".equals(methodName)) {
            returnValue = createEnquiry((BaseVO) args[0]);
        }

        return returnValue;
    }


    @Override
    public BaseVO getEnquiryDetail(BaseVO baseVO) {
        return enquiryDao.getEnquiryDetail(baseVO);
    }

    @Override
    public BaseVO createEnquiry(BaseVO baseVO) {
        return enquiryDao.createEnquiry(baseVO);
    }



    public void setEnquiryDao(EnquiryDao enquiryDao) {
        this.enquiryDao = enquiryDao;
    }


}
