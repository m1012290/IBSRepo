package com.shrinfo.ibs.customerenquiry.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.customer.dao.CustomerDao;
import com.shrinfo.ibs.enquiry.dao.EnquiryDao;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.CustomersListVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;


public class CustomerEnquiryServiceImpl extends BaseService implements CustomerEnquiryService {

    CustomerDao customerDao;

    EnquiryDao enquiryDao;


    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getCustomerEnquiry".equals(methodName)) {
            returnValue = getCustomerEnquiry((BaseVO) args[0]);
        }
        if ("createCustomerEnquiry".equals(methodName)) {
            returnValue = createCustomerEnquiry((BaseVO) args[0]);
        }
        if("createCustomer".equals(methodName)){
        	returnValue = createCustomer((BaseVO) args[0]);
        }
        if("getAllCustomers".equals(methodName)){
        	returnValue = getAllCustomers((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO getCustomerEnquiry(BaseVO baseVO) {
        return enquiryDao.getEnquiryDetail(baseVO);
    }

    @Override
    public BaseVO createCustomerEnquiry(BaseVO baseVO) {
        EnquiryVO enquiryVO = (EnquiryVO)baseVO;
        CustomerVO customerVO = (CustomerVO) customerDao.createCustomer(enquiryVO.getCustomerDetails());
        enquiryVO.setCustomerDetails(customerVO);
        enquiryVO = (EnquiryVO) enquiryDao.createEnquiry(baseVO);        
        return enquiryVO;
    }

    @Override
    public BaseVO createCustomer(BaseVO baseVO) {
	   EnquiryVO enquiryVO = (EnquiryVO)baseVO;
       CustomerVO customerVO = (CustomerVO) customerDao.createCustomer(enquiryVO.getCustomerDetails());
       enquiryVO.setCustomerDetails(customerVO);
       return enquiryVO;
    }
    
    @Override
    public BaseVO getAllCustomers(BaseVO baseVO) {
    	CustomersListVO customersListVO = (CustomersListVO) customerDao.getAllCustomers(baseVO);
    	return customersListVO;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void setEnquiryDao(EnquiryDao enquiryDao) {
        this.enquiryDao = enquiryDao;
    }

}