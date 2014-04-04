package com.shrinfo.ibs.helper.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * 
 * @author Rahul
 * 
 */
public interface ReferralService {

    public BaseVO isReferralApplicable(BaseVO baseVO);

    public BaseVO refer(BaseVO baseVO);

}
