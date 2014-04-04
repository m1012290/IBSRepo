package com.shrinfo.ibs.policy.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.dao.utils.NextSequenceValue;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeader;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeaderId;
import com.shrinfo.ibs.vo.business.PolicyVO;

public class PolicyDaoImpl extends BaseDBDAO implements PolicyDao {

    @Override
    public BaseVO getPolicy(BaseVO baseVO) {


        if (null == baseVO || !(baseVO instanceof PolicyVO)
            || Utils.isEmpty(((PolicyVO) baseVO).getPolicyId())
            || Utils.isEmpty(((PolicyVO) baseVO).getPolicyVersion())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid Policy details. No data passed to fetch details");
        }

        PolicyVO policyVO = new PolicyVO();

        IbsUwTransactionHeader ibsUwTransactionHeader = null;

        try {
            ibsUwTransactionHeader =
                (IbsUwTransactionHeader) getHibernateTemplate().find(
                    " from IbsUwTransactionHeader ibsUwTransactionHeader "
                        + "where ibsUwTransactionHeader.id.id = ?  and "
                        + "ibsUwTransactionHeader.id.policyVersion = ?",
                    ((PolicyVO) baseVO).getPolicyId(),
                    ((PolicyVO) baseVO).getPolicyVersion().longValue()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetPolicyDetails", hibernateException,
                "Error while insured search");
        }
        MapperUtil.populatePolicyVO(policyVO, ibsUwTransactionHeader);
        return policyVO;
    }

    @Override
    public BaseVO createPolicy(BaseVO baseVO) {
        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Policy details cannot be null");
        }
        if (!(baseVO instanceof PolicyVO)) {
            throw new BusinessException("cmn.unknownError", null, "Policy details are invalid");
        }
        PolicyVO policyVO = (PolicyVO) baseVO;

        IbsUwTransactionHeader ibsUwTransactionHeader = null;

        try {
            ibsUwTransactionHeader = DAOUtils.constructIbsUwTransactionHeader(policyVO);
            IbsUwTransactionHeaderId headerId = new IbsUwTransactionHeaderId();

            if (!Utils.isEmpty(ibsUwTransactionHeader.getId())
                && !Utils.isEmpty(ibsUwTransactionHeader.getId().getId())) {
                headerId.setId(ibsUwTransactionHeader.getId().getId());
                Long policyVersion = ibsUwTransactionHeader.getId().getPolicyVersion();
                Long newPolicyVersion = (null == policyVersion) ? 1 : ++policyVersion;
                //headerId.setPolicyVersion(newPolicyVersion);
                headerId.setPolicyVersion(1l);
            } else {
                headerId.setId(NextSequenceValue.getNextSequence("IBS_QUOTE_SLIP_HEADER_SEQ",
                    getHibernateTemplate()));
                headerId.setPolicyVersion(1l);
            }
            ibsUwTransactionHeader.setId(headerId);

            saveOrUpdate(ibsUwTransactionHeader);

            policyVO.setPolicyId(ibsUwTransactionHeader.getId().getId());
            policyVO.setPolicyVersion(ibsUwTransactionHeader.getId().getPolicyVersion().intValue());

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSavePolicyDetails", hibernateException,
                "Error while saving Quotation data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSavePolicyDetails", exception,
                "Error while saving Quotation data");
        }
        return policyVO;
    }

    @Override
    public BaseVO uploadPolicyDoc(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BaseVO> getPolicies(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

}
