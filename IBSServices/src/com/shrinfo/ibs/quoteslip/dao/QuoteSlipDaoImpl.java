package com.shrinfo.ibs.quoteslip.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.dao.utils.NextSequenceValue;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipDetail;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipHeader;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipHeaderId;
import com.shrinfo.ibs.gen.pojo.IbsStatusMaster;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

public class QuoteSlipDaoImpl extends BaseDBDAO implements QuoteSlipDao {

    Logger logger = Logger.getLogger(QuoteSlipDaoImpl.class);

    @Override
    public BaseVO getQuoteSlipDetails(BaseVO baseVO) {

        PolicyVO policyVO = new PolicyVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof QuoteDetailVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }

        IbsQuoteSlipHeader ibsQuoteSlipHeader = null;

        try {
            ibsQuoteSlipHeader =
                (IbsQuoteSlipHeader) getHibernateTemplate().find(
                    " from IbsQuoteSlipHeader ibsQuoteSlipHeader where ibsQuoteSlipHeader.id.id = ? "
                        + "and ibsQuoteSlipHeader.id.quoteSlipVersion = ?",
                    ((QuoteDetailVO) baseVO).getQuoteSlipId(),
                    ((QuoteDetailVO) baseVO).getQuoteSlipVersion()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCustDetails", hibernateException,
                "Error while insured search");
        }

        MapperUtil.populatePolicyVO(policyVO, ibsQuoteSlipHeader);

        return policyVO;
    }

    @Override
    public BaseVO createQuoteSlip(BaseVO baseVO) {

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Customer details cannot be null");
        }
        if (!(baseVO instanceof PolicyVO)) {
            throw new BusinessException("cmn.unknownError", null, "Customer details are invalid");
        }
        PolicyVO policyVO = (PolicyVO) baseVO;
        IbsQuoteSlipHeader quoteSlipHeaderToBeSaved = null;
        IbsQuoteSlipHeader quoteSlipHeaderRetrieved = null;

        try {
            quoteSlipHeaderToBeSaved = DAOUtils.constructIbsQuoteSlipHeader(policyVO);
            // Generate quote slip ID only if it does not exist

            IbsQuoteSlipHeaderId quoteSlipHeaderId = new IbsQuoteSlipHeaderId();
            if (!Utils.isEmpty(quoteSlipHeaderToBeSaved.getId())
                && !Utils.isEmpty(quoteSlipHeaderToBeSaved.getId().getId())
                && 0 < quoteSlipHeaderToBeSaved.getId().getId()) {
                logger.info("This is an edit quote slip flow");
                /**
                 * Get the data from slip details table. Set the status code to 5 (deleted) if they
                 * are not in the update request
                 */
                quoteSlipHeaderRetrieved =
                    (IbsQuoteSlipHeader) getHibernateTemplate().find(
                        " from IbsQuoteSlipHeader ibsQuoteSlipHeader where ibsQuoteSlipHeader.id.id = ? "
                            + "and ibsQuoteSlipHeader.id.quoteSlipVersion = ?",
                        quoteSlipHeaderToBeSaved.getId().getId(),
                        quoteSlipHeaderToBeSaved.getId().getQuoteSlipVersion()).get(0);
                quoteSlipHeaderToBeSaved.setIbsQuoteSlipDetails(getMergedQuoteSlipDetails(
                    quoteSlipHeaderToBeSaved.getIbsQuoteSlipDetails(),
                    quoteSlipHeaderRetrieved.getIbsQuoteSlipDetails()));

                getHibernateTemplate().evict(quoteSlipHeaderRetrieved);
                for (IbsQuoteSlipDetail detail : quoteSlipHeaderRetrieved.getIbsQuoteSlipDetails()) {
                    getHibernateTemplate().evict(detail);
                }

                quoteSlipHeaderId.setId(quoteSlipHeaderToBeSaved.getId().getId());
                quoteSlipHeaderId.setQuoteSlipVersion(1l);
            } else {

                logger.info("This is a create quote slip flow");
                quoteSlipHeaderId.setId(NextSequenceValue.getNextSequence(
                    "IBS_QUOTE_SLIP_HEADER_SEQ", getHibernateTemplate()));
                quoteSlipHeaderId.setQuoteSlipVersion(1l);
            }
            quoteSlipHeaderToBeSaved.setId(quoteSlipHeaderId);

            saveOrUpdate(quoteSlipHeaderToBeSaved);

            MapperUtil.populatePolicyVO(policyVO, quoteSlipHeaderToBeSaved);

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveQuoteSlipDetails", hibernateException,
                "Error while saving Quote Slip data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveQuoteSlipDetails", exception,
                "Error while saving Quote Slip data");
        }
        return policyVO;
    }

    /**
     * 
     * @param ibsQuoteSlipDetailsNew
     * @param ibsQuoteSlipDetailsExisting
     * @return
     */
    private Set<IbsQuoteSlipDetail> getMergedQuoteSlipDetails(
            Set<IbsQuoteSlipDetail> ibsQuoteSlipDetailsNew,
            Set<IbsQuoteSlipDetail> ibsQuoteSlipDetailsExisting) {
        Set<IbsQuoteSlipDetail> mergedQuoteSlipDetails = new HashSet<IbsQuoteSlipDetail>();

        // Add records to be created or updated
        for (IbsQuoteSlipDetail slpDtlNw : ibsQuoteSlipDetailsNew) {
            // This flag represents whether new record to be saved is already exists in DB or not.
            boolean existsFlag = false;
            for (IbsQuoteSlipDetail slpDtlExstng : ibsQuoteSlipDetailsExisting) {

                if (slpDtlExstng.getEnquiryCompanyCode().equals(slpDtlNw.getEnquiryCompanyCode())
                    && slpDtlExstng.getIbsProductUwFields().getId()
                            .equals(slpDtlNw.getIbsProductUwFields().getId())) {
                    slpDtlNw.setId(slpDtlExstng.getId());
                    // record to be updated since it exists already
                    mergedQuoteSlipDetails.add(slpDtlNw);
                    existsFlag = true;
                    break;
                }
            }
            // New records to be created
            if (!existsFlag) {
                mergedQuoteSlipDetails.add(slpDtlNw);
            }
        }

        // Add records to be deleted
        // status code 5= deleted
        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        ibsStatusMaster.setCode(5l);
        for (IbsQuoteSlipDetail slpDtlExstng : ibsQuoteSlipDetailsExisting) {

            // This flag represents whether existing DB record is part of new details to be updated.
            boolean existsFlag = false;
            for (IbsQuoteSlipDetail slpDtlNw : ibsQuoteSlipDetailsNew) {

                if (slpDtlExstng.getEnquiryCompanyCode().equals(slpDtlNw.getEnquiryCompanyCode())
                    && slpDtlExstng.getIbsProductUwFields().getId()
                            .equals(slpDtlNw.getIbsProductUwFields().getId())) {
                    existsFlag = true;
                    break;
                }
            }
            if (!existsFlag) {

                slpDtlExstng.setIbsStatusMaster(ibsStatusMaster);
                mergedQuoteSlipDetails.add(slpDtlExstng);
            }
        }

        return mergedQuoteSlipDetails;
    }
}
