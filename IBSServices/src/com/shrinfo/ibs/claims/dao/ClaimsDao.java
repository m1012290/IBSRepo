package com.shrinfo.ibs.claims.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface ClaimsDao {

    public BaseVO getclaim(BaseVO baseVO);

    public BaseVO saveClaim(BaseVO baseVO);
}
