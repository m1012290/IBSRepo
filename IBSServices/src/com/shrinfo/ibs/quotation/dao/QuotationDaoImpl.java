package com.shrinfo.ibs.quotation.dao;

import java.util.HashSet;
import java.util.List;
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
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionDetail;
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionHeader;
import com.shrinfo.ibs.gen.pojo.IbsStatusMaster;
import com.shrinfo.ibs.gen.pojo.IbsTask;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

public class QuotationDaoImpl extends BaseDBDAO implements QuotationDao {

    Logger logger = Logger.getLogger(QuotationDaoImpl.class);

    @Override
    public List<BaseVO> getQuotations(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO getQuotation(BaseVO baseVO) {
        PolicyVO policyVO = new PolicyVO();

        if (null == baseVO || !(baseVO instanceof QuoteDetailVO)
            || Utils.isEmpty(((QuoteDetailVO) baseVO).getQuoteSlipId())
            || Utils.isEmpty(((QuoteDetailVO) baseVO).getQuoteSlipVersion())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid Quotation details. No quote slip data passed to fetch details");
        }
        IbsQuoteComparisionHeader ibsQuoteComparisionHeader = null;

        List headerList = null;

        try {
            headerList =
                getHibernateTemplate().find(
                    " from IbsQuoteComparisionHeader ibsQuoteComparisionHeader "
                        + "where ibsQuoteComparisionHeader.ibsQuoteSlipHeader.id.id = ? and "
                        + "ibsQuoteComparisionHeader.ibsQuoteSlipHeader.id.quoteSlipVersion = ?",
                    ((QuoteDetailVO) baseVO).getQuoteSlipId(),
                    ((QuoteDetailVO) baseVO).getQuoteSlipVersion());
            if (Utils.isEmpty(headerList)) {
                return null;
            } else {
                ibsQuoteComparisionHeader = (IbsQuoteComparisionHeader) headerList.get(0);
            }
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetQuotationDetails", hibernateException,
                "Error while insured search");
        }

        MapperUtil.populatePolicyVO(policyVO, ibsQuoteComparisionHeader);

        return policyVO;
    }

    @Override
    public BaseVO createQuotation(BaseVO baseVO) {
        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null,
                "Quotation details cannot be null");
        }
        if (!(baseVO instanceof PolicyVO)) {
            throw new BusinessException("cmn.unknownError", null, "Quotation details are invalid");
        }
        PolicyVO policyVO = new PolicyVO();

        IbsQuoteComparisionHeader ibsQuoteHeaderToBeSaved = null;

        IbsQuoteComparisionHeader ibsQuoteHeaderRetrieved = null;

        try {
            ibsQuoteHeaderToBeSaved = DAOUtils.constructIbsQuoteComparisionHeader((PolicyVO) baseVO);
            if (Utils.isEmpty(ibsQuoteHeaderToBeSaved)
                || Utils.isEmpty(ibsQuoteHeaderToBeSaved.getIbsQuoteComparisionDetails())) {
                throw new BusinessException("cmn.unknownError", null,
                    "Quotation details are invalid");
            }

            if (!Utils.isEmpty(ibsQuoteHeaderToBeSaved.getId())) {
                logger.info("This is a quotation edit fow. Quotation id="
                    + ibsQuoteHeaderToBeSaved.getId());

                /**
                 * Get the data from quotation details table. If the records for quoted company
                 * exists, then update those records, else create.
                 */
                ibsQuoteHeaderRetrieved =
                    (IbsQuoteComparisionHeader) getHibernateTemplate().find(
                        " from IbsQuoteComparisionHeader ibsQuoteHeaderRetrieved "
                            + "where ibsQuoteHeaderRetrieved.id = ? ",
                        ibsQuoteHeaderToBeSaved.getId()).get(0);

                ibsQuoteHeaderToBeSaved
                        .setIbsQuoteComparisionDetails(getQuotationDetailsToBePersisted(
                            ibsQuoteHeaderToBeSaved.getIbsQuoteComparisionDetails(),
                            ibsQuoteHeaderRetrieved.getIbsQuoteComparisionDetails()));

                // Remove retrieved persistent objects from session
                getHibernateTemplate().evict(ibsQuoteHeaderRetrieved);
                for (IbsQuoteComparisionDetail quoteDetail : ibsQuoteHeaderRetrieved
                        .getIbsQuoteComparisionDetails()) {
                    getHibernateTemplate().evict(quoteDetail);
                }

            } else {
                logger.info("This is a quotation create flow.");
            }
            saveOrUpdate(ibsQuoteHeaderToBeSaved);
            //once save is performed check if we are in referral approval then we also need to
            //update task table records status.
            PolicyVO inputVO = (PolicyVO)baseVO;
            if(!Utils.isEmpty(inputVO.getAppFlow())){
            	if(AppFlow.REFERRAL_APPROVAL.equals(inputVO.getAppFlow())){
            		//retrieve existing task details
            		IbsTask ibsTask = DAOUtils.queryTaskTblForEnquiryNo(getHibernateTemplate(), inputVO.getEnquiryDetails().getEnquiryNo());
            		//check now if we need to be updating task table status through task_section_type
            		//value saved to task table
            		if(ibsTask.getTaskSectionType().intValue() == SectionId.CLOSINGSLIP.getSectionId() ){
            			IbsStatusMaster ibsStatusMaster = ibsTask.getIbsStatusMaster();
            			ibsStatusMaster.setCode(Long.valueOf(Utils.getSingleValueAppConfig("STATUS_APPROVED")));
            			ibsTask.setIbsStatusMaster(ibsStatusMaster);
            			//perform saveorupdate of ibsTask so that we have status as approved within task table
            			saveOrUpdate(ibsTask);
            		}
            	}
            }

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveQuotationDetails", hibernateException,
                "Error while saving Quotation data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveQuotationDetails", exception,
                "Error while saving Quotation data");
        }
        MapperUtil.populatePolicyVO(policyVO, ibsQuoteHeaderToBeSaved);
        return policyVO;
    }

    @Override
    public BaseVO closeQuotation(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * 
     * @param ibsQuotationDetailsNew
     * @param ibsQuotationDetailsExisting
     * @return
     */
    private Set<IbsQuoteComparisionDetail> getQuotationDetailsToBePersisted(
            Set<IbsQuoteComparisionDetail> ibsQuotationDetailsNew,
            Set<IbsQuoteComparisionDetail> ibsQuotationDetailsExisting) {

        Set<IbsQuoteComparisionDetail> mergedQuotationDetails =
            new HashSet<IbsQuoteComparisionDetail>();

        if (Utils.isEmpty(ibsQuotationDetailsNew)) {
            return null;
        }
        if (Utils.isEmpty(ibsQuotationDetailsExisting)) {
            mergedQuotationDetails.addAll(ibsQuotationDetailsNew);
        }
        // Add records to be created or updated
        for (IbsQuoteComparisionDetail slpDtlNw : ibsQuotationDetailsNew) {
            // This flag represents whether new record to be saved, already exists in DB or not.
            boolean existsFlag = false;
            for (IbsQuoteComparisionDetail slpDtlExstng : ibsQuotationDetailsExisting) {

                if (slpDtlExstng.getQuotedCompanyCode().equals(slpDtlNw.getQuotedCompanyCode())
                    && slpDtlExstng.getIbsProductUwFields().getId()
                            .equals(slpDtlNw.getIbsProductUwFields().getId())) {
                    slpDtlNw.setId(slpDtlExstng.getId());
                    // record to be updated since it exists already
                    mergedQuotationDetails.add(slpDtlNw);
                    existsFlag = true;
                    break;
                }
            }
            // New records to be created
            if (!existsFlag) {
                mergedQuotationDetails.add(slpDtlNw);
            }
        }

        // Add records to be deleted. status code=5
        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        ibsStatusMaster.setCode(5l);
        for (IbsQuoteComparisionDetail slpDtlExstng : ibsQuotationDetailsExisting) {
            // This flag represents whether new record to be saved, already exists in DB or not.
            boolean existsFlag = false;
            for (IbsQuoteComparisionDetail slpDtlNw : ibsQuotationDetailsNew) {

                if (slpDtlExstng.getQuotedCompanyCode().equals(slpDtlNw.getQuotedCompanyCode())
                    && slpDtlExstng.getIbsProductUwFields().getId()
                            .equals(slpDtlNw.getIbsProductUwFields().getId())) {
                    existsFlag = true;
                    break;
                }
            }
            // record to be deleted
            if (!existsFlag) {
                slpDtlExstng.setIbsStatusMaster(ibsStatusMaster);
                mergedQuotationDetails.add(slpDtlExstng);
            }
        }
        return mergedQuotationDetails;
    }

}
