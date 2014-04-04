package com.shrinfo.ibs.enquiry.dao;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsCustomer;
import com.shrinfo.ibs.gen.pojo.IbsCustomerEnquiry;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.EnquiryVO;


public class EnquiryDaoImpl extends BaseDBDAO implements EnquiryDao {


    @Override
    public BaseVO getEnquiryDetail(BaseVO baseVO) {

        EnquiryVO enquiryVO = new EnquiryVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof EnquiryVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }

        IbsCustomerEnquiry ibsCustomerEnquiry = null;

        try {
            ibsCustomerEnquiry =
                (IbsCustomerEnquiry) getHibernateTemplate().find(
                    " from IbsCustomerEnquiry ibsCustomerEnquiry where ibsCustomerEnquiry.id = ?",
                    ((EnquiryVO) baseVO).getEnquiryNo()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCustDetails", hibernateException,
                "Error while insured search");
        }

        MapperUtil.populateEnquiryVO(enquiryVO, ibsCustomerEnquiry);

        return enquiryVO;
    }


    @Override
    public BaseVO createEnquiry(BaseVO baseVO) {

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }

        if (!(baseVO instanceof EnquiryVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }
        EnquiryVO enquiryVO = (EnquiryVO) baseVO;
        IbsContact ibsContactForEnquiry = null;
        try {

            ibsContactForEnquiry =
                DAOUtils.constructIbsContactForRecType(enquiryVO, RecordType.ENQUIRY_CONTACT);
            IbsCustomerEnquiry customerEnquiry =
                (IbsCustomerEnquiry) (ibsContactForEnquiry.getIbsCustomerEnquiries().toArray())[0];
            IbsCustomer customerFromSession =
                (IbsCustomer) getHibernateTemplate().get(IbsCustomer.class,
                    enquiryVO.getCustomerDetails().getCustomerId());
            customerEnquiry.setIbsCustomer(customerFromSession);
            saveOrUpdate(ibsContactForEnquiry);

            IbsCustomerEnquiry ibsEnquiry =
                (IbsCustomerEnquiry) ((ibsContactForEnquiry.getIbsCustomerEnquiries().toArray())[0]);
            enquiryVO.setEnquiryNo(ibsEnquiry.getEnquiryNo());
            enquiryVO.getEnquiryContact().setContactId(ibsContactForEnquiry.getId());
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveEnquiryDetails", hibernateException,
                "Error while saving enquiry data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveEnquiryDetails", exception,
                "Error while saving enquiry data");
        }
        return enquiryVO;
    }

}
