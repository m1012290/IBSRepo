package com.shrinfo.ibs.claims.dao;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.gen.pojo.IbsClaims;
import com.shrinfo.ibs.vo.business.ClaimsVO;


public class ClaimsDaoImpl extends BaseDBDAO implements ClaimsDao {

    @Override
    public BaseVO getclaim(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO saveClaim(BaseVO baseVO) {
        if (null == baseVO || !(baseVO instanceof ClaimsVO)) {
            throw new BusinessException("cmn.unknownError", null, "Invalid Claim details");
        } 
        ClaimsVO claimsVO = (ClaimsVO) baseVO;
        IbsClaims ibsClaims = null;
        try {
            ibsClaims = DAOUtils.constructIbsClaims(claimsVO);
            saveOrUpdate(ibsClaims);

            claimsVO.setId(ibsClaims.getId().longValue());

        } catch (HibernateException hibernateException) {
            throw new BusinessException("ibs.gi.couldNotSaveCustomerDetails", hibernateException,
                "Error while saving enquiry data");
        } catch (Exception exception) {
            throw new SystemException("ins.gi.couldNotSaveCustomerDetails", exception,
                "Error while saving enquiry data");
        }
        return claimsVO;
    }

}
