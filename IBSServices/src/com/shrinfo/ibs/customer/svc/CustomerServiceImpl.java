package com.shrinfo.ibs.customer.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.customer.dao.CustomerDao;


public class CustomerServiceImpl extends BaseService implements CustomerService {

    CustomerDao customerDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getCustomer".equals(methodName)) {
            returnValue = getCustomer((BaseVO) args[0]);
        }
        if ("createCustomerEnquiry".equals(methodName)) {
            returnValue = createCustomer((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO getCustomer(BaseVO baseVO) {
        return customerDao.getCustomer(baseVO);
    }

    @Override
    public BaseVO createCustomer(BaseVO baseVO) {
        return customerDao.createCustomer(baseVO);
    }


    /**
     * @param customerDao the customerDao to set
     */
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

}
