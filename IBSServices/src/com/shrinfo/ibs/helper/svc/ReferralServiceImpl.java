package com.shrinfo.ibs.helper.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;

public class ReferralServiceImpl extends BaseService implements ReferralService {

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("isReferralApplicable".equals(methodName)) {
            returnValue = isReferralApplicable((BaseVO) args[0]);
        }
        if ("refer".equals(methodName)) {
            returnValue = refer((BaseVO) args[0]);
        }
        return returnValue;
    }


    @Override
    public BaseVO isReferralApplicable(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO refer(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }


}
