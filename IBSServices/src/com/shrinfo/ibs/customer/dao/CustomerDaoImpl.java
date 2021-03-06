package com.shrinfo.ibs.customer.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsCustomer;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.CustomersListVO;


public class CustomerDaoImpl extends BaseDBDAO implements CustomerDao {



    @Override
    public BaseVO getCustomer(BaseVO baseVO) {

        CustomerVO customerVO = new CustomerVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof CustomerVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }

        IbsCustomer ibsCustomer = null;

        try {
            ibsCustomer =
                (IbsCustomer) getHibernateTemplate().find(
                    " from IbsCustomer ibsCustomer where ibsCustomer.id = ?",
                    ((CustomerVO) baseVO).getCustomerId()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCustDetails", hibernateException,
                "Error while insured search");
        }

        MapperUtil.populateCustomerVO(customerVO, ibsCustomer);

        return customerVO;
    }


    @Override
    public BaseVO createCustomer(BaseVO baseVO) {

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }

        if (!(baseVO instanceof CustomerVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }
        CustomerVO customerVO = (CustomerVO) baseVO;
        IbsContact ibsContact = null;
        if(Utils.isEmpty(customerVO.getName()) || Utils.isEmpty(customerVO.getContactAndAddrDets()) || Utils.isEmpty(customerVO.getContactAndAddrDets().getMobileNo()) || Utils.isEmpty(Utils.isEmpty(customerVO.getContactAndAddrDets().getEmailId()))){
        	//mandatory data is not completely available hence throwing an exception
        	 throw new SystemException("ins.gi.couldNotSaveCustomerDetails", null,
                     "Error while saving enquiry data as mandatory data not completely available for the service");
        }
        try {
            ibsContact = DAOUtils.constructIbsContactForRecType(customerVO, RecordType.CUSTOMER);
            saveOrUpdate(ibsContact);

            IbsCustomer ibsCustomer = (IbsCustomer) ((ibsContact.getIbsCustomers().toArray())[0]);
            customerVO.setCustomerId(ibsCustomer.getId());
            customerVO.getContactAndAddrDets().setContactId(ibsContact.getId());

        } catch (HibernateException hibernateException) {
            throw new BusinessException("ibs.gi.couldNotSaveCustomerDetails", hibernateException,
                "Error while saving enquiry data");
        } catch (Exception exception) {
            throw new SystemException("ins.gi.couldNotSaveCustomerDetails", exception,
                "Error while saving enquiry data");
        }
        return customerVO;
    }

    @SuppressWarnings("unchecked")
	@Override
    public BaseVO getAllCustomers(BaseVO baseVO) {
        List<IbsCustomer> ibsCustomerList = null;

        try {
        	ibsCustomerList =
                (List<IbsCustomer>) getHibernateTemplate().find(
                    " from IbsCustomer ibsCustomer");
        } catch (HibernateException he) {
            throw new SystemException("pas.gi.couldNotGetCustDetails", he,
                "Error while retrieving all the existing customers in the system");
        } catch(Exception ex){
        	throw new SystemException("pas.gi.couldNotGetCustDetails", ex,
                    "Error while retrieving all the existing customers in the system");
        }
        
        CustomersListVO customersListVO = new  CustomersListVO();
    	MapperUtil.populateCustomersListVO(customersListVO, ibsCustomerList);
    	return customersListVO;
    }

}
