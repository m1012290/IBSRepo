package com.shrinfo.ibs.policy.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.dao.utils.NextSequenceValue;
import com.shrinfo.ibs.gen.pojo.IbsStatusMaster;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionDetail;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeader;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeaderId;
import com.shrinfo.ibs.vo.business.PolicyVO;

public class PolicyDaoImpl extends BaseDBDAO implements PolicyDao {

    Logger logger = Logger.getLogger(PolicyDaoImpl.class);

    @Override
    public BaseVO getPolicy(BaseVO baseVO) {


        if (null == baseVO || !(baseVO instanceof PolicyVO)) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid Policy details. No data passed to fetch details");
        }

        PolicyVO policyVO = new PolicyVO();

        IbsUwTransactionHeader ibsUwTransactionHeader = null;
        
        if(!Utils.isEmpty(((PolicyVO)baseVO).getPolicyId())) {
            ibsUwTransactionHeader = getPolicyBasedOnPolicyId(baseVO);
        } else if (!Utils.isEmpty(((PolicyVO) baseVO).getQuoteId())) {
            ibsUwTransactionHeader = getPolicyBasedOnQuotaton(baseVO);
        }
        
        if(Utils.isEmpty(ibsUwTransactionHeader)) {
            return null;
        }
       
        MapperUtil.populatePolicyVO(policyVO, ibsUwTransactionHeader);
        return policyVO;
    }
    
    private IbsUwTransactionHeader getPolicyBasedOnPolicyId(BaseVO baseVO) {
        
        try {
                 return (IbsUwTransactionHeader) getHibernateTemplate().find(
                    " from IbsUwTransactionHeader ibsUwTransactionHeader "
                        + "where ibsUwTransactionHeader.id.id = ?",
                    ((PolicyVO) baseVO).getPolicyId()).get(0);

        } catch (HibernateException hibernateException) {
            logger.error(hibernateException, "Error while policy search based on quote ID:"
                + ((PolicyVO) baseVO).getQuoteId());
            throw new BusinessException("pas.gi.couldNotGetPolicyDetails", hibernateException,
                "Error while policy search based on quote ID:" + ((PolicyVO) baseVO).getQuoteId());
        }
        
    }
    
    private IbsUwTransactionHeader getPolicyBasedOnQuotaton(BaseVO baseVO) {
        List headerList = null;

        try {
            headerList =
                getHibernateTemplate().find(
                    " from IbsUwTransactionHeader ibsUwTransactionHeader "
                        + "where ibsUwTransactionHeader.ibsQuoteComparisionHeader.id = ?",
                    ((PolicyVO) baseVO).getQuoteId());

            if (Utils.isEmpty(headerList)) {
                logger.info("No policy data found for Quotation ID:"
                    + ((PolicyVO) baseVO).getQuoteId());
                return null;
            }

            return (IbsUwTransactionHeader) headerList.get(0);

        } catch (HibernateException hibernateException) {
            logger.error(hibernateException, "Error while policy search based on quote ID:"
                + ((PolicyVO) baseVO).getQuoteId());
            throw new BusinessException("pas.gi.couldNotGetPolicyDetails", hibernateException,
                "Error while policy search based on quote ID:" + ((PolicyVO) baseVO).getQuoteId());
        }
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

        IbsUwTransactionHeader ibsUwTranHeaderToBeSaved = null;

        IbsUwTransactionHeader ibsUwTranHeaderRetrieved = null;

        try {
            ibsUwTranHeaderToBeSaved = DAOUtils.constructIbsUwTransactionHeader(policyVO);
            IbsUwTransactionHeaderId headerId = new IbsUwTransactionHeaderId();

            if (!Utils.isEmpty(ibsUwTranHeaderToBeSaved.getId())
                && !Utils.isEmpty(ibsUwTranHeaderToBeSaved.getId().getId())) {
                headerId.setId(ibsUwTranHeaderToBeSaved.getId().getId());
                logger.info("This is a policy edit flow. Plicy ID:"
                    + ibsUwTranHeaderToBeSaved.getId().getId());

                ibsUwTranHeaderRetrieved =
                    (IbsUwTransactionHeader) getHibernateTemplate()
                            .find(
                                " from IbsUwTransactionHeader ibsUwTransactionHeader "
                                    + "where ibsUwTransactionHeader.id.id = ? and ibsUwTransactionHeader.id.policyVersion = ?",
                                ibsUwTranHeaderToBeSaved.getId().getId(), 1l).get(0);
                ibsUwTranHeaderToBeSaved.setIbsUwTransactionDetails(getPolicyDetailsToBePersisted(
                    ibsUwTranHeaderToBeSaved.getIbsUwTransactionDetails(),
                    ibsUwTranHeaderRetrieved.getIbsUwTransactionDetails()));

                getHibernateTemplate().evict(ibsUwTranHeaderRetrieved);
                for (IbsUwTransactionDetail detail : ibsUwTranHeaderRetrieved
                        .getIbsUwTransactionDetails()) {
                    getHibernateTemplate().evict(detail);
                }

            } else {
                headerId.setId(NextSequenceValue.getNextSequence("IBS_QUOTE_SLIP_HEADER_SEQ",
                    getHibernateTemplate()));

            }
            headerId.setPolicyVersion(1l);
            ibsUwTranHeaderToBeSaved.setId(headerId);

            saveOrUpdate(ibsUwTranHeaderToBeSaved);

            policyVO.setPolicyId(ibsUwTranHeaderToBeSaved.getId().getId());
            policyVO.setPolicyVersion(ibsUwTranHeaderToBeSaved.getId().getPolicyVersion()
                    .intValue());

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSavePolicyDetails", hibernateException,
                "Error while saving Quotation data");
        } catch (Exception exception) {
            throw new BusinessException("pas.gi.couldNotSavePolicyDetails", exception,
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

    private Set<IbsUwTransactionDetail> getPolicyDetailsToBePersisted(
            Set<IbsUwTransactionDetail> ibsPolicyDetailsNew,
            Set<IbsUwTransactionDetail> ibsPolicyDetailsExisting) {

        Set<IbsUwTransactionDetail> mergedPolicyDetails = new HashSet<IbsUwTransactionDetail>();

        if (Utils.isEmpty(ibsPolicyDetailsExisting)) {
            // since no existing records are present
            mergedPolicyDetails.addAll(ibsPolicyDetailsNew);
        } else if (Utils.isEmpty(ibsPolicyDetailsNew)) {
            // no new records to add or update
        } else {
            // Add records to be created or updated
            for (IbsUwTransactionDetail policyDtlNw : ibsPolicyDetailsNew) {
                // This flag represents whether new record to be saved, already exists in DB or not.
                boolean existsFlag = false;
                for (IbsUwTransactionDetail policyDtlExstng : ibsPolicyDetailsExisting) {

                    if (policyDtlExstng.getPolicyCompanyCode().equals(
                        policyDtlNw.getPolicyCompanyCode())
                        && policyDtlExstng.getIbsProductUwFields().getId()
                                .equals(policyDtlNw.getIbsProductUwFields().getId())) {
                        policyDtlNw.setId(policyDtlExstng.getId());
                        // record to be updated since it exists already
                        mergedPolicyDetails.add(policyDtlNw);
                        existsFlag = true;
                        break;
                    }
                }
                // New records to be created
                if (!existsFlag) {
                    mergedPolicyDetails.add(policyDtlNw);
                }
            }
        }


        // Add records to be deleted. status code=5
        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        ibsStatusMaster.setCode(5l);
        for (IbsUwTransactionDetail slpDtlExstng : ibsPolicyDetailsExisting) {
            // This flag represents whether record to be saved, already exists in DB or not.
            boolean existsFlag = false;
            if (!Utils.isEmpty(ibsPolicyDetailsNew)) {
                for (IbsUwTransactionDetail slpDtlNw : ibsPolicyDetailsNew) {

                    if (slpDtlExstng.getPolicyCompanyCode().equals(slpDtlNw.getPolicyCompanyCode())
                        && slpDtlExstng.getIbsProductUwFields().getId()
                                .equals(slpDtlNw.getIbsProductUwFields().getId())) {
                        existsFlag = true;
                        break;
                    }
                }
            }
            // record to be deleted
            if (!existsFlag) {
                slpDtlExstng.setIbsStatusMaster(ibsStatusMaster);
                mergedPolicyDetails.add(slpDtlExstng);
            }
        }
        return mergedPolicyDetails;
    }

}
