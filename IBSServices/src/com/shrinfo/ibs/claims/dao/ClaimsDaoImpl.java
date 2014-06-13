package com.shrinfo.ibs.claims.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsClaims;
import com.shrinfo.ibs.vo.business.ClaimsVO;
import com.shrinfo.ibs.vo.business.PolicyVO;


public class ClaimsDaoImpl extends BaseDBDAO implements ClaimsDao {

    @Override
    public BaseVO getclaim(BaseVO baseVO) {
        ClaimsVO claimsVO = new ClaimsVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof PolicyVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }

        IbsClaims ibsClaims = null;
        List claimsList = null;

        try {
            claimsList =
                getHibernateTemplate().find(
                    " from IbsClaims ibsClaims where ibsClaims.ibsUwTransactionHeader.id.id = ?",
                    ((PolicyVO) baseVO).getPolicyId());
            if (!Utils.isEmpty(claimsList)) {
                ibsClaims = (IbsClaims) claimsList.get(0);
                MapperUtil.populateClaimsVO(claimsVO, ibsClaims);
            }

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCustDetails", hibernateException,
                "Error while insured search");
        }

        return claimsVO;
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
