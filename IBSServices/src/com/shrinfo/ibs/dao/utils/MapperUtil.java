package com.shrinfo.ibs.dao.utils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsCustomer;
import com.shrinfo.ibs.gen.pojo.IbsCustomerEnquiry;
import com.shrinfo.ibs.gen.pojo.IbsDocumentTable;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompany;
import com.shrinfo.ibs.gen.pojo.IbsInsuredMaster;
import com.shrinfo.ibs.gen.pojo.IbsProductMaster;
import com.shrinfo.ibs.gen.pojo.IbsProductUwFields;
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionDetail;
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionHeader;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipDetail;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipHeader;
import com.shrinfo.ibs.gen.pojo.IbsStatusMaster;
import com.shrinfo.ibs.gen.pojo.IbsTask;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionDetail;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeader;
import com.shrinfo.ibs.vo.app.EnquiryType;
import com.shrinfo.ibs.vo.business.AddressVO;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.DocumentVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.PremiumVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.StatusVO;
import com.shrinfo.ibs.vo.business.TaskVO;

public class MapperUtil {

    /**
     * LOgger for this class
     */
    private static final Logger logger = Logger.getLogger(MapperUtil.class);

    public static void populateCustomerVO(CustomerVO customerVO, IbsCustomer ibsCustomer) {

        if (Utils.isEmpty(ibsCustomer)) {
            return;
        }
        if (Utils.isEmpty(customerVO)) {
            customerVO = new CustomerVO();
        }
        populateAuditFields(customerVO, ibsCustomer);

        if (!Utils.isEmpty(ibsCustomer.getId())) {
            customerVO.setCustomerId(ibsCustomer.getId());
        }
        customerVO.setName(ibsCustomer.getName());
        customerVO.setCategory(ibsCustomer.getCategory());
        customerVO.setClassification(ibsCustomer.getClassification());
        customerVO.setCustomerId(ibsCustomer.getId().longValue());
        customerVO.setGroup(ibsCustomer.getCustGroup());
        // customerVO.setIsStatusActive(ibsCustomer.get)
        customerVO.setSalutation(ibsCustomer.getSalutation());
        customerVO.setSalesExecutive(ibsCustomer.getSalesExecutive());
        customerVO.setSourceOfBusiness(ibsCustomer.getSource());

        ContactVO contactVO = new ContactVO();
        populateContactVO(contactVO, ibsCustomer.getIbsContact());
        customerVO.setContactAndAddrDets(contactVO);

    }

    public static void populateContactVO(ContactVO contactVO, IbsContact ibsContact) {

        if (Utils.isEmpty(ibsContact)) {
            return;
        }
        if (Utils.isEmpty(contactVO)) {
            contactVO = new ContactVO();
        }
        populateAuditFields(contactVO, ibsContact);

        if (!Utils.isEmpty(ibsContact.getId())) {
            contactVO.setContactId(ibsContact.getId());
        }
        contactVO.setAlternateEmailId1(ibsContact.getAlternateEmailId1());
        contactVO.setAlternateEmailId2(ibsContact.getAlternateEmailId2());
        contactVO.setAlternateLandlineNo1(ibsContact.getAlternateLandlineNo1());
        contactVO.setAlternateLandLineNo2(ibsContact.getAlternateLandlineNo2());
        contactVO.setAlternateMobileNo1(ibsContact.getAlternateMobileNo1());
        contactVO.setAlternateMobileNo2(ibsContact.getAlternateMobileNo2());
        contactVO.setEmailId(ibsContact.getPrimaryEmailId());
        contactVO.setFaxNo(ibsContact.getFaxno());
        contactVO.setFirstName(ibsContact.getFirstName());
        contactVO.setIsStatusActive(ibsContact.getIsStatusActive());
        contactVO.setLandlineNo(ibsContact.getPrimaryLandlineNo());
        contactVO.setLastName(ibsContact.getLastName());
        contactVO.setMiddleName(ibsContact.getMiddleName());
        contactVO.setMobileNo(ibsContact.getPrimaryMobileNo());
        contactVO.setTitle(ibsContact.getTitle());
        if (null != ibsContact.getRecordType()) {
            contactVO.setRecordType(ibsContact.getRecordType().intValue());
        }
        // contactVO.setRecordTypeDesc(ibsContact.getRecordType());
        AddressVO addressVO = new AddressVO();
        populateAddressVO(addressVO, ibsContact);
        contactVO.setAddressVO(addressVO);

    }

    public static void populateAddressVO(AddressVO addressVO, IbsContact ibsContact) {

        if (Utils.isEmpty(ibsContact)) {
            return;
        }
        if (Utils.isEmpty(addressVO)) {
            addressVO = new AddressVO();
        }
        addressVO.setAddress(ibsContact.getAddress());
        addressVO.setCity(ibsContact.getCity());
        addressVO.setCountry(ibsContact.getCountry());
        addressVO.setIsStatusActive(ibsContact.getIsStatusActive());
        if (!Utils.isEmpty(ibsContact.getLocationLattitude())) {
            addressVO.setLattitude(ibsContact.getLocationLattitude().toString());
        }
        if (!Utils.isEmpty(ibsContact.getLocationLongitude())) {
            addressVO.setLongitude(ibsContact.getLocationLongitude().toString());
        }
        addressVO.setPoBox(ibsContact.getPobox());

    }

    public static void populateProductVO(ProductVO productVO, IbsProductMaster ibsProductMaster) {

        if (Utils.isEmpty(ibsProductMaster)) {
            return;
        }
        if (Utils.isEmpty(productVO)) {
            productVO = new ProductVO();
        }
        populateAuditFields(productVO, ibsProductMaster);

        productVO.setCategory(ibsProductMaster.getCategorisation());
        productVO.setIsStatusActive(ibsProductMaster.getStatus());
        productVO.setName(ibsProductMaster.getName());
        productVO.setProductClass(ibsProductMaster.getClass_().intValue());
        productVO.setShortName(ibsProductMaster.getShortname());
        if (!Utils.isEmpty(ibsProductMaster.getSubclass())) {
            productVO.setSubClass(ibsProductMaster.getSubclass().intValue());
        }
        if (!Utils.isEmpty(ibsProductMaster.getIbsProductUwFieldses())) {
            List<ProductUWFieldVO> uwFieldsList = new ArrayList<ProductUWFieldVO>();
            ProductUWFieldVO productUWFieldVO = null;
            for (IbsProductUwFields fields : ibsProductMaster.getIbsProductUwFieldses()) {
                productUWFieldVO = new ProductUWFieldVO();
                populateProductUWFieldVO(productUWFieldVO, fields);
                uwFieldsList.add(productUWFieldVO);
            }
            productVO.setUwFieldsList(uwFieldsList);
        }

    }

    public static void populateProductUWFieldVO(ProductUWFieldVO productUWFieldVO,
            IbsProductUwFields fields) {

        if (Utils.isEmpty(fields)) {
            return;
        }
        if (Utils.isEmpty(productUWFieldVO)) {
            productUWFieldVO = new ProductUWFieldVO();
        }

        if (Utils.isEmpty(fields.getFieldLength())) {
            productUWFieldVO.setFieldLength(fields.getFieldLength().intValue());
        }

        productUWFieldVO.setFieldName(fields.getFieldName());
        if (null != fields.getSrlNo()) {
            productUWFieldVO.setFieldOrder(fields.getSrlNo().intValue());
        }
        productUWFieldVO.setFieldType(fields.getFieldType());
        // productUWFieldVO.setFieldValue(fields.get);
        productUWFieldVO.setIsMandatory(fields.getIsMandatory());
        productUWFieldVO.setIsStatusActive(fields.getStatus());
        // productUWFieldVO.setResponse(fields.getFieldName());
        productUWFieldVO.setFieldValueType(fields.getFieldValueType());
        if (!Utils.isEmpty(fields.getId())) {
            productUWFieldVO.setUwFieldId(fields.getId());
        }
        if (!Utils.isEmpty(fields.getIbsProductMaster())) {
            productUWFieldVO.setProductClass(fields.getIbsProductMaster().getClass_().intValue());
        }
    }

    public static void populateEnquiryVO(EnquiryVO enquiryVO, IbsCustomerEnquiry ibsCustomerEnquiry) {

        if (Utils.isEmpty(ibsCustomerEnquiry)) {
            return;
        }
        if (Utils.isEmpty(enquiryVO)) {
            enquiryVO = new EnquiryVO();
        }
        populateAuditFields(enquiryVO, ibsCustomerEnquiry);

        enquiryVO.setEnquiryNo(ibsCustomerEnquiry.getEnquiryNo().longValue());
        enquiryVO.setEnquirySme(ibsCustomerEnquiry.getEnquirySubjectmatterExpert());
        if (!Utils.isEmpty(ibsCustomerEnquiry.getType())) {
            enquiryVO.setType(EnquiryType.valueOf(ibsCustomerEnquiry.getType()));
        }
        CustomerVO customerVO = new CustomerVO();
        populateCustomerVO(customerVO, ibsCustomerEnquiry.getIbsCustomer());
        enquiryVO.setCustomerDetails(customerVO);
        ProductVO productVO = new ProductVO();
        populateProductVO(productVO, ibsCustomerEnquiry.getIbsProductMaster());
        enquiryVO.setProduct(productVO);
        enquiryVO.setDescription(ibsCustomerEnquiry.getEnquiryDescription());
        ContactVO contactVO = new ContactVO();
        populateContactVO(contactVO, ibsCustomerEnquiry.getIbsContact());
        enquiryVO.setEnquiryContact(contactVO);
        enquiryVO.setIsStatusActive(ibsCustomerEnquiry.getIsActive());

    }

    public static void populateInsuredVO(InsuredVO insuredVO, IbsInsuredMaster ibsInsuredMaster) {

        if (Utils.isEmpty(ibsInsuredMaster)) {
            return;
        }
        if (Utils.isEmpty(insuredVO)) {
            insuredVO = new InsuredVO();
        }
        populateAuditFields(insuredVO, ibsInsuredMaster);

        // insuredVO.setIsStatusActive(ibsInsuredMaster.get)
        insuredVO.setName(ibsInsuredMaster.getName());
        insuredVO.setSalutation(ibsInsuredMaster.getSalutation());
        insuredVO.setSourceOfBusiness(ibsInsuredMaster.getSource());
        ContactVO contactVO = new ContactVO();
        MapperUtil.populateContactVO(contactVO, ibsInsuredMaster.getIbsContact());
        insuredVO.setContactAndAddrDetails(contactVO);
        CustomerVO customerVO = new CustomerVO();
        MapperUtil.populateCustomerVO(customerVO, ibsInsuredMaster.getIbsCustomer());
        insuredVO.setCustomerDetails(customerVO);
        insuredVO.setId(ibsInsuredMaster.getId());
    }

    /**
     * Populates all the details irrespective of whether the records are active or not.
     * 
     * @param policyVO
     * @param ibsQuoteSlipHeader
     */
    public static void populatePolicyVO(PolicyVO policyVO, IbsQuoteSlipHeader ibsQuoteSlipHeader) {
        populatePolicyVO(policyVO, ibsQuoteSlipHeader, false);
    }

    /**
     * Populates Quote slip details business VO from quote slip hibernate POJO. Quote slip details:
     * details that were sent to Insurance company.
     * 
     * @param policyVO
     * @param ibsQuoteSlipHeader
     */
    public static void populatePolicyVO(PolicyVO policyVO, IbsQuoteSlipHeader ibsQuoteSlipHeader,
            Boolean filterInctive) {

        if (null == ibsQuoteSlipHeader) {
            return;
        }
        if (null == policyVO) {
            policyVO = new PolicyVO();
        }
        /*
         * ibsInsCmpnyQuoteDetails: Map containing IbsInsurance company code and List of
         * IbsQuoteSlipDetail. Using this map, another map containing InsCompanyVO and QuoteDetailVO
         * will be prepared, which will be set to PolicyVO.
         */
        Map<String, List<IbsQuoteSlipDetail>> ibsInsCmpnyQuoteDetailsMap =
            new HashMap<String, List<IbsQuoteSlipDetail>>();
        List<IbsQuoteSlipDetail> slipDetails = null;
        if (!Utils.isEmpty(ibsQuoteSlipHeader.getIbsQuoteSlipDetails())) {
            String insCopmnyCode = null;
            for (IbsQuoteSlipDetail slipDetail : ibsQuoteSlipHeader.getIbsQuoteSlipDetails()) {

                if (!Utils.isEmpty(slipDetail.getIbsStatusMaster())
                    && !Utils.isEmpty(slipDetail.getIbsStatusMaster().getCode())
                    && (1 != slipDetail.getIbsStatusMaster().getCode())) {
                    continue;
                }
                insCopmnyCode = slipDetail.getEnquiryCompanyCode();
                slipDetails = ibsInsCmpnyQuoteDetailsMap.get(insCopmnyCode);
                if (null == slipDetails) {
                    slipDetails = new ArrayList<IbsQuoteSlipDetail>();
                }
                slipDetails.add(slipDetail);
                ibsInsCmpnyQuoteDetailsMap.put(insCopmnyCode, slipDetails);
            }
        }

        // Map that will be set in policyVO
        Map<InsCompanyVO, QuoteDetailVO> insCmpnyQuoteDetails =
            new HashMap<InsCompanyVO, QuoteDetailVO>();
        Set<Entry<String, List<IbsQuoteSlipDetail>>> ibsInsCmpnyQuoteDetailsEntrySet =
            ibsInsCmpnyQuoteDetailsMap.entrySet();
        Iterator<Entry<String, List<IbsQuoteSlipDetail>>> ibsInsCmpnyQuoteDetailsIt =
            ibsInsCmpnyQuoteDetailsEntrySet.iterator();

        Entry<String, List<IbsQuoteSlipDetail>> ibsInsCmpnyQuoteDetailsEntry = null;
        InsCompanyVO insCompanyVO = null;
        QuoteDetailVO quoteDetailVO = null;
        while (ibsInsCmpnyQuoteDetailsIt.hasNext()) {
            ibsInsCmpnyQuoteDetailsEntry = ibsInsCmpnyQuoteDetailsIt.next();
            insCompanyVO = new InsCompanyVO();
            insCompanyVO.setCode(ibsInsCmpnyQuoteDetailsEntry.getKey());
            quoteDetailVO = new QuoteDetailVO();
            // Populate product UW fields and some more fields which exists only
            // in quote slip
            // details table
            populateQuoteSlipDetailVO(quoteDetailVO, ibsInsCmpnyQuoteDetailsEntry.getValue());
            // Populate fields into QuoteDeatilVO which are present in quote
            // slip header table
            quoteDetailVO.setCustomerId(ibsQuoteSlipHeader.getCustomerId());
            quoteDetailVO.setQuoteSlipId(ibsQuoteSlipHeader.getId().getId());
            quoteDetailVO.setQuoteSlipVersion(ibsQuoteSlipHeader.getId().getQuoteSlipVersion());
            quoteDetailVO.setQuoteSlipDate(ibsQuoteSlipHeader.getQuoteSlipDate());
            quoteDetailVO.setRemarks(ibsQuoteSlipHeader.getRemarks());
            if (!Utils.isEmpty(ibsQuoteSlipHeader.getIbsStatusMaster())) {
                quoteDetailVO.setStatusCode(ibsQuoteSlipHeader.getIbsStatusMaster().getCode()
                        .intValue());
            }
            /*
             * ProductVO productVO = new ProductVO(); populateProductVO(productVO,
             * ibsQuoteSlipHeader.getIbsProductMaster());
             * quoteDetailVO.setProductDetails(productVO);
             */
            insCmpnyQuoteDetails.put(insCompanyVO, quoteDetailVO);

        }
        policyVO.setQuoteDetails(insCmpnyQuoteDetails);
        EnquiryVO enquiryVO = new EnquiryVO();
        enquiryVO.setEnquiryNo(ibsQuoteSlipHeader.getEnquiryNo());
        policyVO.setEnquiryDetails(enquiryVO);
        InsuredVO insuredVO = new InsuredVO();
        insuredVO.setId(ibsQuoteSlipHeader.getInsuredId());
        insuredVO.setName(ibsQuoteSlipHeader.getInsuredName());
        policyVO.setInsuredDetails(insuredVO);

    }

    private static void populateQuoteSlipDetailVO(QuoteDetailVO quoteDetailVO,
            List<IbsQuoteSlipDetail> ibsQuoteSlipDetails) {

        if (Utils.isEmpty(ibsQuoteSlipDetails)) {
            return;
        }
        if (Utils.isEmpty(quoteDetailVO)) {
            quoteDetailVO = new QuoteDetailVO();
        }
        // sIbsQuoteSlipDetail ibsQuoteSlipDetail = ibsQuoteSlipDetails.get(0);
        ProductVO productVO = new ProductVO();
        List<ProductUWFieldVO> productUWFieldVOs = new ArrayList<ProductUWFieldVO>();
        ProductUWFieldVO productUWFieldVO = null;
        IbsQuoteSlipDetail ibsQuoteSlipDetail = null;
        Iterator<IbsQuoteSlipDetail> ibsQuoteSlipDetailsIt = ibsQuoteSlipDetails.iterator();
        while (ibsQuoteSlipDetailsIt.hasNext()) {
            ibsQuoteSlipDetail = ibsQuoteSlipDetailsIt.next();
            productUWFieldVO = new ProductUWFieldVO();
            populateProductUWFieldVO(productUWFieldVO, ibsQuoteSlipDetail.getIbsProductUwFields());
            productUWFieldVO.setResponse(ibsQuoteSlipDetail.getProductUwFieldAnswer());
            productUWFieldVO.setTableId(ibsQuoteSlipDetail.getId());
            productUWFieldVOs.add(productUWFieldVO);
        }
        productVO.setUwFieldsList(productUWFieldVOs);
        if (!Utils.isEmpty(ibsQuoteSlipDetail.getIbsProductMaster())) {
            IbsProductMaster ibsProductMaster = ibsQuoteSlipDetail.getIbsProductMaster();
            productVO.setCategory(ibsProductMaster.getCategorisation());
            productVO.setIsStatusActive(ibsProductMaster.getStatus());
            productVO.setName(ibsProductMaster.getName());
            if (!Utils.isEmpty(ibsProductMaster.getClass_())) {
                productVO.setProductClass(ibsProductMaster.getClass_().intValue());
            }
            productVO.setShortName(ibsProductMaster.getShortname());

        }

        quoteDetailVO.setProductDetails(productVO);
        if (!Utils.isEmpty(ibsQuoteSlipDetail.getIbsStatusMaster())) {
            quoteDetailVO.setIsStatusActive(String.valueOf(ibsQuoteSlipDetail.getIbsStatusMaster()
                    .getCode()));
        }
        quoteDetailVO.setIsClosingSlipEmailed(ibsQuoteSlipDetail.getQuoteSlipEmailed());
        quoteDetailVO.setQuoteSlipDate(ibsQuoteSlipDetail.getQuoteSlipDate());

    }


    public static void populatePolicyVO(PolicyVO policyVO,
            IbsQuoteComparisionHeader ibsQuoteComparisionHeader) {
        if (null == ibsQuoteComparisionHeader) {
            return;
        }
        if (null == policyVO) {
            policyVO = new PolicyVO();
        }
        /*
         * ibsInsCmpnyQuoteDetails: Map containing IbsInsurance company code and List of
         * IbsQuoteSlipDetail. Using this map, another map containing InsCompanyVO and QuoteDetailVO
         * will be prepared, which will be set to PolicyVO.
         */
        Map<String, List<IbsQuoteComparisionDetail>> ibsInsCmpnyQuoteDetailsMap =
            new HashMap<String, List<IbsQuoteComparisionDetail>>();
        List<IbsQuoteComparisionDetail> quoteDetails = null;
        if (!Utils.isEmpty(ibsQuoteComparisionHeader.getIbsQuoteComparisionDetails())) {
            String insCopmnyCode = null;
            for (IbsQuoteComparisionDetail quoteDetail : ibsQuoteComparisionHeader
                    .getIbsQuoteComparisionDetails()) {

                insCopmnyCode = quoteDetail.getQuotedCompanyCode();
                quoteDetails = ibsInsCmpnyQuoteDetailsMap.get(insCopmnyCode);
                if (null == quoteDetails) {
                    quoteDetails = new ArrayList<IbsQuoteComparisionDetail>();
                }
                quoteDetails.add(quoteDetail);
                ibsInsCmpnyQuoteDetailsMap.put(insCopmnyCode, quoteDetails);
            }
        }

        // Map that will be set in policyVO
        Map<InsCompanyVO, QuoteDetailVO> insCmpnyQuoteDetails =
            new HashMap<InsCompanyVO, QuoteDetailVO>();
        Set<Entry<String, List<IbsQuoteComparisionDetail>>> ibsInsCmpnyQuoteDetailsEntrySet =
            ibsInsCmpnyQuoteDetailsMap.entrySet();
        Iterator<Entry<String, List<IbsQuoteComparisionDetail>>> ibsInsCmpnyQuoteDetailsIt =
            ibsInsCmpnyQuoteDetailsEntrySet.iterator();

        Entry<String, List<IbsQuoteComparisionDetail>> ibsInsCmpnyQuoteDetailsEntry = null;
        InsCompanyVO insCompanyVO = null;
        QuoteDetailVO quoteDetailVO = null;
        while (ibsInsCmpnyQuoteDetailsIt.hasNext()) {
            ibsInsCmpnyQuoteDetailsEntry = ibsInsCmpnyQuoteDetailsIt.next();
            insCompanyVO = new InsCompanyVO();
            insCompanyVO.setCode(ibsInsCmpnyQuoteDetailsEntry.getKey());
            quoteDetailVO = new QuoteDetailVO();
            // Populate product UW fields and some more fields which exists only
            // in quote slip
            // details table
            populateQuotationDetailVO(quoteDetailVO, ibsInsCmpnyQuoteDetailsEntry.getValue());
            if (Utils.isEmpty(quoteDetailVO.getStatusCode())
                || (1 != quoteDetailVO.getStatusCode())) {
                quoteDetailVO = null;
                continue;
            }
            // Populate fields into QuoteDeatilVO which are present in quote
            // slip header table
            quoteDetailVO.setCustomerId(ibsQuoteComparisionHeader.getCustomerId());
            /*
             * 
             * ProductVO productVO = new ProductVO(); populateProductVO(productVO,
             * ibsQuoteComparisionHeader.getIbsProductMaster());
             * quoteDetailVO.setProductDetails(productVO);
             */
            quoteDetailVO.setRecommendationSummary(ibsQuoteComparisionHeader
                    .getRecommendationSummary());
            quoteDetailVO.setQuoteId(ibsQuoteComparisionHeader.getId());
            if (!Utils.isEmpty(ibsQuoteComparisionHeader.getIbsQuoteSlipHeader())) {
                quoteDetailVO.setQuoteSlipId(ibsQuoteComparisionHeader.getIbsQuoteSlipHeader()
                        .getId().getId());
                quoteDetailVO.setQuoteSlipVersion(ibsQuoteComparisionHeader.getIbsQuoteSlipHeader()
                        .getId().getQuoteSlipVersion());
            }


            insCmpnyQuoteDetails.put(insCompanyVO, quoteDetailVO);
        }
        policyVO.setQuoteDetails(insCmpnyQuoteDetails);
        EnquiryVO enquiryVO = new EnquiryVO();
        enquiryVO.setEnquiryNo(ibsQuoteComparisionHeader.getEnquiryNo());
        policyVO.setEnquiryDetails(enquiryVO);
        InsuredVO insuredVO = new InsuredVO();
        insuredVO.setId(ibsQuoteComparisionHeader.getInsuredId());
        insuredVO.setName(ibsQuoteComparisionHeader.getInsuredName());
        policyVO.setInsuredDetails(insuredVO);
    }

    private static void populateQuotationDetailVO(QuoteDetailVO quoteDetailVO,
            List<IbsQuoteComparisionDetail> ibsQuoteCompDetails) {

        if (Utils.isEmpty(ibsQuoteCompDetails)) {
            return;
        }
        if (Utils.isEmpty(quoteDetailVO)) {
            quoteDetailVO = new QuoteDetailVO();
        }
        // sIbsQuoteSlipDetail ibsQuoteSlipDetail = ibsQuoteSlipDetails.get(0);
        ProductVO productVO = new ProductVO();

        List<ProductUWFieldVO> productUWFieldVOs = new ArrayList<ProductUWFieldVO>();
        ProductUWFieldVO productUWFieldVO = null;
        IbsQuoteComparisionDetail ibsQuoteCompDetail = null;
        Iterator<IbsQuoteComparisionDetail> ibsQuoteSlipDetailsIt = ibsQuoteCompDetails.iterator();
        while (ibsQuoteSlipDetailsIt.hasNext()) {
            ibsQuoteCompDetail = ibsQuoteSlipDetailsIt.next();
            productUWFieldVO = new ProductUWFieldVO();
            populateProductUWFieldVO(productUWFieldVO, ibsQuoteCompDetail.getIbsProductUwFields());
            productUWFieldVO.setResponse(ibsQuoteCompDetail.getProductUwFieldAnswer());
            productUWFieldVOs.add(productUWFieldVO);
            populateProductVO(productVO, ibsQuoteCompDetail.getIbsProductMaster());
        }
        productVO.setUwFieldsList(productUWFieldVOs);
        quoteDetailVO.setProductDetails(productVO);
        if (!Utils.isEmpty(ibsQuoteCompDetail.getIbsStatusMaster())) {
            quoteDetailVO.setStatusCode(ibsQuoteCompDetail.getIbsStatusMaster().getCode()
                    .intValue());

        }
        quoteDetailVO.setIsClosingSlipEmailed(ibsQuoteCompDetail.getClosingSlipEmailed());
        quoteDetailVO.setIsClosingSlipGenerated(ibsQuoteCompDetail.getQuoteRecommended());
        quoteDetailVO.setIsQuoteRecommended(ibsQuoteCompDetail.getQuoteRecommended().equals("Y")
            ? true : false);
        quoteDetailVO.setQuoteNo(ibsQuoteCompDetail.getQuoteNo());
        quoteDetailVO.setQuoteDate(ibsQuoteCompDetail.getQuoteDate());
        quoteDetailVO.setCompanyCode(ibsQuoteCompDetail.getQuotedCompanyCode());
        if (!Utils.isEmpty(ibsQuoteCompDetail.getSumInsured())) {
            quoteDetailVO.setSumInsured(BigDecimal.valueOf(ibsQuoteCompDetail.getSumInsured()));
        }
        if (!Utils.isEmpty(ibsQuoteCompDetail.getPolicyTerm())) {
            quoteDetailVO.setPolicyTerm(ibsQuoteCompDetail.getPolicyTerm().intValue());
        }
        BigDecimal premium = null;
        if (!Utils.isEmpty(ibsQuoteCompDetail.getQuotedPremium())) {
            premium = ibsQuoteCompDetail.getQuotedPremium();
        }
        BigDecimal totalPremium = null;
        if (!Utils.isEmpty(ibsQuoteCompDetail.getTotalPremium())) {
            totalPremium = ibsQuoteCompDetail.getTotalPremium();
        }
        double discountPercentage = 0d;
        if (!Utils.isEmpty(ibsQuoteCompDetail.getDiscountPercentage())) {
            discountPercentage = ibsQuoteCompDetail.getDiscountPercentage();
        }
        double loadingPercentage = 0d;
        if (!Utils.isEmpty(ibsQuoteCompDetail.getLoadingPercentage())) {
            loadingPercentage = ibsQuoteCompDetail.getLoadingPercentage();
        }
        BigDecimal commissionAmt = null;
        if (!Utils.isEmpty(ibsQuoteCompDetail.getCommissionOnPrm())) {
            commissionAmt = ibsQuoteCompDetail.getCommissionOnPrm();
        }

        PremiumVO premiumVO =
            populatePremiumDetails(premium, totalPremium, discountPercentage, loadingPercentage,
                commissionAmt, ibsQuoteCompDetail.getCoverDescription());
        quoteDetailVO.setQuoteSlipPrmDetails(premiumVO);

    }

    private static PremiumVO populatePremiumDetails(BigDecimal premium, BigDecimal totalPremium,
            double discountPercentage, double loadingPercentage, BigDecimal commissionAmt,
            String coverDescription) {
        PremiumVO premiumDetails = new PremiumVO();
        premiumDetails.setCommissionAmt(commissionAmt);
        premiumDetails.setCoverDescription(coverDescription);
        premiumDetails.setDiscountPercentage(discountPercentage);
        // premiumDetails.setIsStatusActive(ibsUwTransactionDetail);
        premiumDetails.setLoadingPercentage(loadingPercentage);
        premiumDetails.setPremium(premium);
        premiumDetails.setTotalPremium(totalPremium);
        return premiumDetails;
    }

    private static void populateAuditFields(BaseVO baseVO, Object pojo) {

        if (null != getFieldValue(pojo, "getRecCreUserId")) {
            baseVO.setCreatedByUserId((Long) getFieldValue(pojo, "getRecCreUserId"));
        }
        if (null != getFieldValue(pojo, "getRecUpdUserId")) {
            baseVO.setUpdatedByUserId((Long) getFieldValue(pojo, "getRecUpdUserId"));
        }
        if (null != getFieldValue(pojo, "getRecCreDate")) {
            baseVO.setCreatedDate((Timestamp) getFieldValue(pojo, "getRecCreDate"));
        }
        if (null != getFieldValue(pojo, "getRecUpdDate")) {
            baseVO.setUpdatedDate((Timestamp) getFieldValue(pojo, "getRecUpdDate"));
        }
    }

    public static Object getFieldValue(Object obj, String method) {

        if (Utils.isEmpty(obj) || Utils.isEmpty(method)) {
            logger.error("Problem while getting field value");
        }
        try {
            Method m = obj.getClass().getMethod(method);
            // m.invoke(obj);
        } catch (SecurityException e) {
            logger.info(e, "No method with name:" + method);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.info(e, "No method with name:" + method);
        }
        return null;
    }

    /**
     * 
     * @param policyVO
     * @param ibsUwTransactionHeader
     */
    public static void populatePolicyVO(PolicyVO policyVO,
            IbsUwTransactionHeader ibsUwTransactionHeader) {

        if (null == ibsUwTransactionHeader) {
            return;
        }
        if (null == policyVO) {
            policyVO = new PolicyVO();
        }
        populateAuditFields(policyVO, ibsUwTransactionHeader);

        // Set policyID and policy version from header table
        if (!Utils.isEmpty(ibsUwTransactionHeader.getId())) {
            policyVO.setPolicyId(ibsUwTransactionHeader.getId().getId());
            if (!Utils.isEmpty(ibsUwTransactionHeader.getId().getPolicyVersion())) {
                policyVO.setPolicyVersion(ibsUwTransactionHeader.getId().getPolicyVersion()
                        .intValue());
            }
        }
        policyVO.setPolicyNo(ibsUwTransactionHeader.getPolicyNo());
        // set enquiry details
        EnquiryVO enquiryVO = new EnquiryVO();
        enquiryVO.setEnquiryNo(ibsUwTransactionHeader.getEnquiryNo());
        policyVO.setEnquiryDetails(enquiryVO);
        // set insured details
        InsuredVO insuredVO = new InsuredVO();
        insuredVO.setId(ibsUwTransactionHeader.getInsuredId());
        insuredVO.setName(ibsUwTransactionHeader.getInsuredName());
        policyVO.setInsuredDetails(insuredVO);

        /*
         * ibsInsCmpnyPolicyDetailsMap: Map containing IbsInsurance company code and List of
         * IbsUwTransactionDetail. Using this map, another map containing InsCompanyVO and
         * QuoteDetailVO will be prepared, which will be set to PolicyVO.
         */
        Map<String, List<IbsUwTransactionDetail>> ibsInsCmpnyPolicyDetailsMap =
            new HashMap<String, List<IbsUwTransactionDetail>>();
        List<IbsUwTransactionDetail> policyDetails = null;
        if (!Utils.isEmpty(ibsUwTransactionHeader.getIbsUwTransactionDetails())) {
            String insCopmnyCode = null;
            for (IbsUwTransactionDetail uwPolicyDetail : ibsUwTransactionHeader
                    .getIbsUwTransactionDetails()) {

                insCopmnyCode = uwPolicyDetail.getPolicyCompanyCode();
                policyDetails = ibsInsCmpnyPolicyDetailsMap.get(insCopmnyCode);
                if (null == policyDetails) {
                    policyDetails = new ArrayList<IbsUwTransactionDetail>();
                }
                policyDetails.add(uwPolicyDetail);
                ibsInsCmpnyPolicyDetailsMap.put(insCopmnyCode, policyDetails);
            }
        }

        // Map that will be set in policyVO
        Map<InsCompanyVO, QuoteDetailVO> insCmpnyPolicyDetails =
            new HashMap<InsCompanyVO, QuoteDetailVO>();
        Set<Entry<String, List<IbsUwTransactionDetail>>> ibsInsCmpnyPolicyDetailsEntrySet =
            ibsInsCmpnyPolicyDetailsMap.entrySet();
        Iterator<Entry<String, List<IbsUwTransactionDetail>>> ibsInsCmpnyPolicyDetailsIt =
            ibsInsCmpnyPolicyDetailsEntrySet.iterator();

        Entry<String, List<IbsUwTransactionDetail>> ibsInsCmpnyQuoteDetailsEntry = null;
        InsCompanyVO insCompanyVO = null;
        QuoteDetailVO quoteDetailVO = null;
        IbsUwTransactionDetail ibsUwTransactionDetail = null;
        while (ibsInsCmpnyPolicyDetailsIt.hasNext()) {
            ibsInsCmpnyQuoteDetailsEntry = ibsInsCmpnyPolicyDetailsIt.next();
            insCompanyVO = new InsCompanyVO();
            insCompanyVO.setCode(ibsInsCmpnyQuoteDetailsEntry.getKey());
            quoteDetailVO = new QuoteDetailVO();

            // Populate product UW fields and some more fields which exists only
            // in UE transaction details table
            populatePolicyDetailVO(quoteDetailVO, ibsInsCmpnyQuoteDetailsEntry.getValue());
            // Populate fields into QuoteDeatilVO which are present in quote
            // slip header table
            quoteDetailVO.setCustomerId(ibsUwTransactionHeader.getCustomerId());

            ProductVO productVO = new ProductVO();
            populateProductVO(productVO, ibsUwTransactionHeader.getIbsProductMaster());
            quoteDetailVO.setProductDetails(productVO);

            insCmpnyPolicyDetails.put(insCompanyVO, quoteDetailVO);

            // populate the values from detail table in policyVO
            if (!Utils.isEmpty(ibsInsCmpnyQuoteDetailsEntry.getValue())) {
                ibsUwTransactionDetail = ibsInsCmpnyQuoteDetailsEntry.getValue().get(0);

                // set premium details
                PremiumVO premiumDetails = new PremiumVO();
                premiumDetails.setCommissionAmt(ibsUwTransactionDetail.getCommissionOnPrm());
                premiumDetails.setCoverDescription(ibsUwTransactionDetail.getCoverDescription());
                premiumDetails
                        .setDiscountPercentage(ibsUwTransactionDetail.getDiscountPercentage());
                // premiumDetails.setIsStatusActive(ibsUwTransactionDetail);
                premiumDetails.setLoadingPercentage(ibsUwTransactionDetail.getLoadingPercentage());
                premiumDetails.setPremium(ibsUwTransactionDetail.getPolicyPremium());
                premiumDetails.setTotalPremium(ibsUwTransactionDetail.getTotalPremium());
                policyVO.setPremiumDetails(premiumDetails);
            }

            // there will be always one insurance company record for a policy.
            // So break out after
            // populating first insurance company details
            break;
        }
        policyVO.setQuoteDetails(insCmpnyPolicyDetails);

    }

    /**
     * 
     * @param quoteDetailVO
     * @param ibsPolivyUwDetails
     */
    private static void populatePolicyDetailVO(QuoteDetailVO quoteDetailVO,
            List<IbsUwTransactionDetail> ibsPolivyUwDetails) {
        if (Utils.isEmpty(ibsPolivyUwDetails)) {
            return;
        }
        if (Utils.isEmpty(quoteDetailVO)) {
            quoteDetailVO = new QuoteDetailVO();
        }
        // sIbsQuoteSlipDetail ibsQuoteSlipDetail = ibsQuoteSlipDetails.get(0);
        ProductVO productVO = new ProductVO();
        List<ProductUWFieldVO> productUWFieldVOs = new ArrayList<ProductUWFieldVO>();
        ProductUWFieldVO productUWFieldVO = null;
        IbsUwTransactionDetail ibsPolicyUwTranDetail = null;
        Iterator<IbsUwTransactionDetail> ibsQuoteSlipDetailsIt = ibsPolivyUwDetails.iterator();
        while (ibsQuoteSlipDetailsIt.hasNext()) {
            ibsPolicyUwTranDetail = ibsQuoteSlipDetailsIt.next();
            productUWFieldVO = new ProductUWFieldVO();
            populateProductUWFieldVO(productUWFieldVO,
                ibsPolicyUwTranDetail.getIbsProductUwFields());
            productUWFieldVO.setResponse(ibsPolicyUwTranDetail.getProductUwFieldAnswer());
            productUWFieldVOs.add(productUWFieldVO);
        }
        productVO.setUwFieldsList(productUWFieldVOs);
        quoteDetailVO.setProductDetails(productVO);
        if (!Utils.isEmpty(ibsPolicyUwTranDetail.getIbsStatusMaster())) {
            quoteDetailVO.setIsStatusActive(String.valueOf(ibsPolicyUwTranDetail
                    .getIbsStatusMaster().getCode()));
        }

    }

    /**
     * 
     * @param taskVO
     * @param ibsTask
     * @throws SQLException
     */
    public static void populateTaskVO(TaskVO taskVO, IbsTask ibsTask) {

        if (Utils.isEmpty(ibsTask)) {
            return;
        }

        if (Utils.isEmpty(taskVO)) {
            taskVO = new TaskVO();
        }
        UserVO assigneeUser = new IBSUserVO();
        assigneeUser.setUserId(ibsTask.getAssigneeUserId());
        taskVO.setAssigneeUser(assigneeUser);
        UserVO assignerUser = new IBSUserVO();
        assignerUser.setUserId(ibsTask.getAssignorUserId());
        taskVO.setAssignerUser(assignerUser);

        taskVO.setDesc(ibsTask.getReferralDesc());
        if (!Utils.isEmpty(ibsTask.getDocumentId())) {
            DocumentVO documentVO = new DocumentVO();
            documentVO.setId(Long.valueOf(ibsTask.getDocumentId()));
            taskVO.setDocument(documentVO);
        }
        EnquiryVO enquiryVO = new EnquiryVO();
        IbsCustomerEnquiry ibsEnquiry = new IbsCustomerEnquiry();
        ibsEnquiry.setEnquiryNo(ibsTask.getEnquiryNo());
        populateEnquiryVO(enquiryVO, ibsEnquiry);
        taskVO.setEnquiry(enquiryVO);
        taskVO.setId(ibsTask.getId());
        StatusVO statusVO = new StatusVO();
        populateStatusVO(statusVO, ibsTask.getIbsStatusMaster());
        taskVO.setStatusVO(statusVO);
    }

    private static void populateStatusVO(StatusVO statusVO, IbsStatusMaster ibsStatusMaster) {
        if (Utils.isEmpty(ibsStatusMaster)) {
            return;
        }
        if (Utils.isEmpty(statusVO)) {
            statusVO = new StatusVO();
        }
        statusVO.setCode(ibsStatusMaster.getCode().intValue());
        statusVO.setDesc(ibsStatusMaster.getDescription());
        // statusVO.setIsStatusActive(ibsStatusMaster.getDescription());

    }

    /**
     * 
     * @param documentVO
     * @param ibsDocument
     * @throws SQLException
     */
    public static void populateDocumentVO(DocumentVO documentVO, IbsDocumentTable ibsDocument)
            throws SQLException {
        if (Utils.isEmpty(ibsDocument)) {
            return;
        }
        if (Utils.isEmpty(documentVO)) {
            documentVO = new DocumentVO();
        }
        documentVO.setDocDescreption(ibsDocument.getDocRecievedDesc());
        documentVO.setDocSlipId(ibsDocument.getDocSlipId());
        if (!Utils.isEmpty(ibsDocument.getDocSlipVersion())) {
            documentVO.setDocSlipVersion(ibsDocument.getDocSlipVersion().intValue());
        }
        documentVO.setDocType(ibsDocument.getDocumentType());

        Blob blob = ibsDocument.getDocument();
        InputStream in = blob.getBinaryStream();

        // documentVO.setDocument(ibsDocument.getDocument().getBytes(0,
        // ibsDocument.getDocument().ge);
        EnquiryVO enquiryVO = new EnquiryVO();
        enquiryVO.setEnquiryNo(ibsDocument.getEnquiryNo());
        documentVO.setEnquiry(enquiryVO);
        documentVO.setId(ibsDocument.getId());
        // documentVO.setIsStatusActive(ibsDocument.gets);
    }


    public static void populateInsCompanyVO(InsCompanyVO insCompanyVO,
            IbsInsuranceCompany ibsInsuranceCompany) {

        if (Utils.isEmpty(ibsInsuranceCompany)) {
            return;
        }

        if (Utils.isEmpty(insCompanyVO)) {
            insCompanyVO = new InsCompanyVO();
        }

        insCompanyVO.setCode(ibsInsuranceCompany.getCode());
        ContactVO contactVO = new ContactVO();
        populateContactVO(contactVO, ibsInsuranceCompany.getIbsContact());
        populateAuditFields(contactVO, ibsInsuranceCompany.getIbsContact());
        insCompanyVO.setContactAndAddrDetails(contactVO);
        insCompanyVO.setIsStatusActive(ibsInsuranceCompany.getStatus());
        insCompanyVO.setName(ibsInsuranceCompany.getName());

        // TODO: set product wise contact details

        populateAuditFields(insCompanyVO, ibsInsuranceCompany);

    }
}
