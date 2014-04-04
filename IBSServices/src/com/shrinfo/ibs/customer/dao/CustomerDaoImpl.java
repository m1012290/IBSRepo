package com.shrinfo.ibs.customer.dao;

import java.math.BigDecimal;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsCustomer;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.CustomerVO;


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
                    BigDecimal.valueOf(((CustomerVO) baseVO).getCustomerId())).get(0);
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


}
