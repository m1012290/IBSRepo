package com.shrinfo.ibs.policy.svc;


import com.shrinfo.ibs.cmn.vo.BaseVO;

public interface PolicyService {

    public BaseVO getPolicy(BaseVO baseVO);

    public BaseVO getPolicies(BaseVO baseVO);

    public BaseVO createPolicy(BaseVO baseVO);

    public BaseVO uploadPolicyDoc(BaseVO baseVO);

}
