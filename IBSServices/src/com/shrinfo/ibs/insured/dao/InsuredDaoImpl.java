package com.shrinfo.ibs.insured.dao;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsInsuredMaster;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.InsuredVO;


public class InsuredDaoImpl extends BaseDBDAO implements InsuredDao {

    @Override
    public BaseVO getInsuredetails(BaseVO baseVO) {

        InsuredVO insuredVO = new InsuredVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof InsuredVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }

        IbsInsuredMaster ibsInsuredMaster = null;
        try {
        	/*
            ibsInsuredMaster =
                (IbsInsuredMaster) getHibernateTemplate().find(
                    " from IbsInsuredMaster ibsInsuredMaster where ibsInsuredMaster.id = ? "
                        + "and ibsInsuredMaster.version = ?", ((InsuredVO) baseVO).getId(),
                    ((InsuredVO) baseVO).getVersion()).get(0); */
        	  ibsInsuredMaster =
                      (IbsInsuredMaster) getHibernateTemplate().find(
                          " from IbsInsuredMaster ibsInsuredMaster where ibsInsuredMaster.id = ? ", ((InsuredVO) baseVO).getId()).get(0);
        	
        } catch (HibernateException hibernateException) {
            throw new BusinessException("ibs.gi.couldNotGetInsuredDetails", hibernateException,
                "Error while insured search");
        }
        MapperUtil.populateInsuredVO(insuredVO, ibsInsuredMaster);
        return insuredVO;
    }


    @Override
    public BaseVO createInsuredDetails(BaseVO baseVO) {

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof InsuredVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }

        InsuredVO insuredVO = (InsuredVO) baseVO;
        IbsContact ibsContact = null;
        try {
            ibsContact =
                DAOUtils.constructIbsContactForRecType(insuredVO, RecordType.INSURED);
            saveOrUpdate(ibsContact);
            IbsInsuredMaster ibsInsuredMaster =
                (IbsInsuredMaster) ((ibsContact.getIbsInsuredMasters().toArray())[0]);
            insuredVO.setId(ibsInsuredMaster.getId());
            insuredVO.getContactAndAddrDetails().setContactId(ibsContact.getId());
            // insuredVO.setVersion((ibsInsuredMaster.getVersion());
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveInsuredDetails", hibernateException,
                "Error while saving insured data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveInsuredDetails", exception,
                "Error while saving insured data");
        }
        return insuredVO;
    }

}
