package com.shrinfo.ibs.customer.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface CustomerService {

    public BaseVO getCustomer(BaseVO baseVO);

    public BaseVO createCustomer(BaseVO baseVO);
}
