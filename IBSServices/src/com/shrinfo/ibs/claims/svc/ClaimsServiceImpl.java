package com.shrinfo.ibs.claims.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.claims.dao.ClaimsDao;
import com.shrinfo.ibs.cmn.vo.BaseVO;


public class ClaimsServiceImpl extends BaseService implements ClaimsService {

    private ClaimsDao claimsDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getclaim".equals(methodName)) {
            returnValue = getclaim((BaseVO) args[0]);
        }
        if ("saveClaim".equals(methodName)) {
            returnValue = saveClaim((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO getclaim(BaseVO baseVO) {
        return claimsDao.getclaim(baseVO);
    }

    @Override
    public BaseVO saveClaim(BaseVO baseVO) {
        return claimsDao.saveClaim(baseVO);
    }


    public void setClaimsDao(ClaimsDao claimsDao) {
        this.claimsDao = claimsDao;
    }


}
