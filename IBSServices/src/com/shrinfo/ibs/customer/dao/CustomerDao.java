package com.shrinfo.ibs.customer.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface CustomerDao {

    public BaseVO getCustomer(BaseVO baseVO);

    public BaseVO getAllCustomers(BaseVO baseVO);
    
    public BaseVO createCustomer(BaseVO baseVO);

}
