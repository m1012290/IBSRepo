package com.shrinfo.ibs.company.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.company.dao.CompanyDao;


public class CompanyServiceImpl extends BaseService implements CompanyService {

    CompanyDao companyDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getCompany".equals(methodName)) {
            returnValue = getCompany((BaseVO) args[0]);
        }
        if ("createCompany".equals(methodName)) {
            returnValue = createCompany((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO getCompany(BaseVO baseVO) {
        return companyDao.getCompany(baseVO);
    }

    @Override
    public BaseVO createCompany(BaseVO baseVO) {
        return companyDao.createCompany(baseVO);
    }


    /**
     * @param companyDao the companyDao to set
     */
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

}
