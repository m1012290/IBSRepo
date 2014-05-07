package com.shrinfo.ibs.claims.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface ClaimsService {
    
    public BaseVO getclaim(BaseVO baseVO);
    
    public BaseVO saveClaim(BaseVO baseVO);

}
