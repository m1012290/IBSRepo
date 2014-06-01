package com.shrinfo.ibs.userdets.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public interface UserDetailsSvc {

    public BaseVO getUserDetails(BaseVO baseVO);

    public BaseVO saveUserDetails(BaseVO baseVO);
}
