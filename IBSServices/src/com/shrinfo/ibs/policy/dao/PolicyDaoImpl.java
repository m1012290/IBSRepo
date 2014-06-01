package com.shrinfo.ibs.policy.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.dao.utils.NextSequenceValue;
import com.shrinfo.ibs.gen.pojo.IbsStatusMaster;
import com.shrinfo.ibs.gen.pojo.IbsTask;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionDetail;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeader;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeaderId;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.SearchItemVO;
import com.shrinfo.ibs.vo.business.SearchVO;

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

        if (!Utils.isEmpty(((PolicyVO) baseVO).getPolicyId())) {
            ibsUwTransactionHeader = getPolicyBasedOnPolicyId(baseVO);
        } else if (!Utils.isEmpty(((PolicyVO) baseVO).getQuoteId())) {
            ibsUwTransactionHeader = getPolicyBasedOnQuotaton(baseVO);
        } else if (!Utils.isEmpty(((PolicyVO) baseVO).getPolicyNo())) {
            ibsUwTransactionHeader = getPolicyBasedOnPolicyNumber(baseVO);
        }

        if (Utils.isEmpty(ibsUwTransactionHeader)) {
            return null;
        }

        MapperUtil.populatePolicyVO(policyVO, ibsUwTransactionHeader);
        return policyVO;
    }

    private IbsUwTransactionHeader getPolicyBasedOnPolicyId(BaseVO baseVO) {

        try {
            return (IbsUwTransactionHeader) getHibernateTemplate().find(
                " from IbsUwTransactionHeader ibsUwTransactionHeader "
                    + "where ibsUwTransactionHeader.id.id = ?", ((PolicyVO) baseVO).getPolicyId())
                    .get(0);

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

    private IbsUwTransactionHeader getPolicyBasedOnPolicyNumber(BaseVO baseVO) {
        List headerList = null;
        try {
            headerList =
                getHibernateTemplate().findByNamedParam(
                    " from IbsUwTransactionHeader ibsUwTransactionHeader "
                        + "where ibsUwTransactionHeader.policyNo =:policyNo", "policyNo",
                    ((PolicyVO) baseVO).getPolicyNo());

            if (Utils.isEmpty(headerList)) {
                logger.info("No policy data found for Policy Number :"
                    + ((PolicyVO) baseVO).getPolicyNo());
                return null;
            }

            return (IbsUwTransactionHeader) headerList.get(0);

        } catch (HibernateException hibernateException) {
            logger.error(hibernateException, "Error while policy search based on policy Number:"
                + ((PolicyVO) baseVO).getQuoteId());
            throw new BusinessException("pas.gi.couldNotGetPolicyDetails", hibernateException,
                "Error while policy search based on policy number:"
                    + ((PolicyVO) baseVO).getPolicyNo());
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
            
            //once save is performed check if we are in referral approval then we also need to
            //update task table records status.
            PolicyVO inputVO = (PolicyVO)baseVO;
            if(!Utils.isEmpty(inputVO.getAppFlow())){
                if(AppFlow.REFERRAL_APPROVAL.equals(inputVO.getAppFlow())){
                    //retrieve existing task details
                    IbsTask ibsTask = DAOUtils.queryTaskTblForEnquiryNo(getHibernateTemplate(), inputVO.getEnquiryDetails().getEnquiryNo(), 
                        Long.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL")), Long.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_POLICY")));
                    getHibernateTemplate().evict(ibsTask);
                    //check now if we need to be updating task table status through task_section_type
                    //value saved to task table
                    if(ibsTask.getTaskSectionType().intValue() == SectionId.POLICY.getSectionId() ){
                        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
                        ibsStatusMaster.setCode(Long.valueOf(Utils.getSingleValueAppConfig("STATUS_APPROVED")));
                        ibsTask.setIbsStatusMaster(ibsStatusMaster);
                        //perform saveorupdate of ibsTask so that we have status as approved within task table
                        update(ibsTask);
                    }
                }
            }
            

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
    public SearchVO getPolicies(BaseVO baseVO) {
    	if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Search Details cannot be null");
        }
        if (!(baseVO instanceof SearchItemVO)) {
            throw new BusinessException("cmn.unknownError", null, "Search Details are invalid");
        }
        SearchItemVO searchItemVO = (SearchItemVO) baseVO;
        return getPoliciesListWithinExpiryDate(searchItemVO);
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
    
    private SearchVO getPoliciesListWithinExpiryDate(SearchItemVO searchItemVO){
    	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createSQLQuery(constructQueryForPoliciesRetrieval(searchItemVO));
        List<Object[]> result = null;
        try {
            result = (List<Object[]>) query.list();
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCustDetails", hibernateException,
                "Error while insured search");
        }
        SearchVO searchVO = new SearchVO();
        SearchItemVO itemVO = null;
        List<SearchItemVO> searchResultList = new ArrayList<SearchItemVO>();
        Iterator<Object[]> it = result.iterator();
        Object[] object = null;
        while (it.hasNext()) {
            object = it.next();
            itemVO = new SearchItemVO();
            BigDecimal customerId = (BigDecimal)object[0];
            itemVO.setCustomerId(customerId.longValue());
            itemVO.setInsuredName((String)object[1]);
            itemVO.setPolicyNum((String)object[2]);
            Timestamp effectiveDate = (Timestamp)object[3];
            itemVO.setPolicyEffectiveDate((Date)effectiveDate);
            itemVO.setPolicyExpiryDate((Date)object[4]);
            searchResultList.add(itemVO);
        }
        searchVO.setSearchItems(searchResultList);
        
        return searchVO;
    }
    
    private String constructQueryForPoliciesRetrieval(SearchItemVO searchItemVO){
    	DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	StringBuffer queryString = new StringBuffer();
    	queryString.append("select customer_id, insured_name , policy_no, policy_start_date, policy_expiry_date from ibs_uw_transaction_header where ");
    	queryString.append("policy_expiry_date >= to_date(\'"+sdf.format(searchItemVO.getPolicyExpiryStartDate())+"\', \'dd/MM/yyyy\') and ");
    	queryString.append("policy_expiry_date <= to_date(\'"+sdf.format(searchItemVO.getPolicyExpiryEndDate())+"\', \'dd/MM/yyyy\')");
    	return queryString.toString();
    }


}
