/**
 * 
 */
package com.shrinfo.ibs.dao.utils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.lob.BlobImpl;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.gen.pojo.IbsClaims;
import com.shrinfo.ibs.gen.pojo.IbsCompany;
import com.shrinfo.ibs.gen.pojo.IbsCompanyBranch;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsCustomer;
import com.shrinfo.ibs.gen.pojo.IbsCustomerEnquiry;
import com.shrinfo.ibs.gen.pojo.IbsDocumentTable;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompProdLink;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompany;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompanyContact;
import com.shrinfo.ibs.gen.pojo.IbsInsuredMaster;
import com.shrinfo.ibs.gen.pojo.IbsProductMaster;
import com.shrinfo.ibs.gen.pojo.IbsProductUwFields;
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionDetail;
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionHeader;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipDetail;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipHeader;
import com.shrinfo.ibs.gen.pojo.IbsQuoteSlipHeaderId;
import com.shrinfo.ibs.gen.pojo.IbsRoles;
import com.shrinfo.ibs.gen.pojo.IbsStatusMaster;
import com.shrinfo.ibs.gen.pojo.IbsTask;
import com.shrinfo.ibs.gen.pojo.IbsUser;
import com.shrinfo.ibs.gen.pojo.IbsUserRoleLink;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionDetail;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeader;
import com.shrinfo.ibs.gen.pojo.IbsUwTransactionHeaderId;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.BranchVO;
import com.shrinfo.ibs.vo.business.ClaimsVO;
import com.shrinfo.ibs.vo.business.CompanyVO;
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

/**
 * @author Sunil Kumar
 * 
 */
public class DAOUtils {

    public static final Logger logger = Logger.getLogger(DAOUtils.class);

    @SuppressWarnings("unchecked")
    public static IbsContact constructIbsContactForRecType(BaseVO baseVO, RecordType recordType) {
        IbsContact ibsContact = null;
        if (!Utils.isEmpty(baseVO)) {

            switch (recordType) {
                case USER:
                    IBSUserVO ibsUserVO = (IBSUserVO) baseVO;
                    ibsContact = constructIbsContact(ibsUserVO.getContactDetails());
                    IbsUser ibsUser = constructIbsUser(baseVO);
                    ibsUser.setIbsContact(ibsContact);
                    ibsContact.getIbsUsers().add(ibsUser);
                    break;
                case CUSTOMER:
                    CustomerVO customerVO = (CustomerVO) baseVO;
                    ibsContact = constructIbsContact(customerVO.getContactAndAddrDets());
                    IbsCustomer ibsCustomer = constructIbsCustomer(baseVO);
                    ibsCustomer.setIbsContact(ibsContact);
                    ibsContact.setIbsCustomers(constructSetOfPOJOForRecType(recordType
                            .getPojoClass()));
                    ibsContact.getIbsCustomers().add(ibsCustomer);
                    break;
                case COMPANY:
                    ibsContact = new IbsContact();
                    IbsCompany ibsCompany = constructIbsCompany(baseVO);
                    ibsContact.setIbsCompanies(constructSetOfPOJOForRecType(recordType
                            .getPojoClass()));
                    ibsContact.getIbsCompanies().add(ibsCompany);
                    break;
                case INSURANCE_COMPANY:
                    InsCompanyVO insCompanyVO = (InsCompanyVO) baseVO;
                    ibsContact = constructIbsContact(insCompanyVO.getContactAndAddrDetails());

                    IbsInsuranceCompany ibsInsuranceCompany = constructIbsInsCompany(baseVO);
                    ibsInsuranceCompany.setIbsContact(ibsContact);
                    ibsContact.setIbsInsuranceCompanies(constructSetOfPOJOForRecType(recordType
                        .getPojoClass()));
                    ibsContact.getIbsInsuranceCompanies().add(ibsInsuranceCompany);
                    break;
                case COMPANY_BRANCH:
                    BranchVO branchVO=(BranchVO)baseVO;
                    ibsContact = constructIbsContact(branchVO.getInchargeContactAndAddrDets()); 
                    break;
                case PORTFOLIO:
                    break;
                case INSURED:
                    InsuredVO insuredVO = (InsuredVO) baseVO;
                    ibsContact = constructIbsContact(insuredVO.getContactAndAddrDetails());
                    IbsInsuredMaster ibsInsuredMaster = constructIbsInsuredMaster(baseVO);
                    ibsInsuredMaster.setIbsContact(ibsContact);
                    ibsContact.setIbsInsuredMasters(constructSetOfPOJOForRecType(recordType
                            .getPojoClass()));
                    ibsContact.getIbsInsuredMasters().add(ibsInsuredMaster);
                    break;
                case ENQUIRY_CONTACT:
                    EnquiryVO enquiryVO = (EnquiryVO) baseVO;
                    ibsContact = constructIbsContact(enquiryVO.getEnquiryContact());
                    IbsCustomerEnquiry ibsCustomerEnquiry = constructIbsCustEnquiry(baseVO);
                    ibsCustomerEnquiry.setIbsContact(ibsContact);
                    ibsContact.setIbsCustomerEnquiries(constructSetOfPOJOForRecType(recordType
                            .getPojoClass()));
                    ibsContact.getIbsCustomerEnquiries().add(ibsCustomerEnquiry);
                    break;
            }
        }
        return ibsContact;
    }

    /**
     * 
     * @param baseVO
     * @return
     */
    private static IbsUser constructIbsUser(BaseVO baseVO) {
        IbsUser ibsUser = new IbsUser();
        if(Utils.isEmpty(baseVO)) {
            return null;
        }
        IBSUserVO ibsUserVO = (IBSUserVO) baseVO;
        ibsUser.setDescription(ibsUserVO.getContactDetails().getFirstName() +" "+ibsUserVO.getContactDetails().getLastName());
        ibsUser.setFreezeFlag(ibsUserVO.getIsFreezed());
        ibsUser.setLoginName(ibsUserVO.getLoginName());
        ibsUser.setPassword(ibsUserVO.getPassword());
        ibsUser.setStatus(ibsUserVO.getIsActive());
        
        //ibsUser.setIbsUserRoleLinks(constructIbsUserRoleLinks(ibsUserVO));
        
        IbsCompanyBranch branch = new IbsCompanyBranch();
        branch.setCode(ibsUserVO.getBranchDetails().getCode());        
        ibsUser.setIbsCompanyBranch(branch);
        
        return ibsUser;
    }

    /**
     * 
     * @param ibsUserVO
     * @return
     */
    public static Set<IbsUserRoleLink> constructIbsUserRoleLinks(IBSUserVO ibsUserVO) {
        Set<IbsUserRoleLink> ibsUserRoleLinks = new HashSet<IbsUserRoleLink>();
        if(Utils.isEmpty(ibsUserVO)) {
            return null;
        }
        IbsUserRoleLink ibsUserRoleLink = new IbsUserRoleLink();
        IbsUser ibsUser = new IbsUser();
        ibsUser.setId(ibsUserVO.getUserId());
        ibsUserRoleLink.setIbsUser(ibsUser);
        ibsUserRoleLink.setStatus(ibsUserVO.getIsActive());
        
        IbsRoles ibsRoles = new IbsRoles();
        ibsRoles.setId(ibsUserVO.getRoles().get(0).getId());
        ibsUserRoleLink.setIbsRoles(ibsRoles);
        
        ibsUserRoleLinks.add(ibsUserRoleLink);
        return ibsUserRoleLinks;
    }

    /**
     * 
     * @param baseVO
     * @return
     */
    private static IbsContact constructIbsContact(BaseVO baseVO) {
        IbsContact ibsContact = new IbsContact();
        if (null == baseVO) {
            return ibsContact;
        }
        ContactVO contactDets = (ContactVO) baseVO;

        if (null != contactDets.getAddressVO())
            ibsContact.setAddress(contactDets.getAddressVO().getAddress());
        if (!Utils.isEmpty(contactDets.getContactId())) {
            ibsContact.setId(contactDets.getContactId());
        }
        ibsContact.setAlternateEmailId1(contactDets.getAlternateEmailId1());
        ibsContact.setAlternateEmailId2(contactDets.getAlternateEmailId2());
        ibsContact.setAlternateLandlineNo1(contactDets.getAlternateLandlineNo1());
        ibsContact.setAlternateLandlineNo2(contactDets.getAlternateLandLineNo2());
        ibsContact.setAlternateMobileNo1(contactDets.getAlternateMobileNo1());
        ibsContact.setAlternateMobileNo2(contactDets.getAlternateMobileNo2());
        ibsContact.setCity(contactDets.getAddressVO().getCity());
        ibsContact.setCountry(contactDets.getAddressVO().getCountry());
        ibsContact.setFaxno(contactDets.getFaxNo());
        ibsContact.setFirstName(contactDets.getFirstName());
        ibsContact.setLastName(contactDets.getLastName());
        if (!Utils.isEmpty(contactDets.getAddressVO().getLattitude())
            && !Utils.isEmpty(Utils.isEmpty(contactDets.getAddressVO().getLongitude()))) {
            ibsContact.setLocationLattitude(Integer.valueOf(contactDets.getAddressVO()
                    .getLattitude()));
            ibsContact.setLocationLongitude(Integer.valueOf(contactDets.getAddressVO()
                    .getLongitude()));
        }
        ibsContact.setMiddleName(contactDets.getMiddleName());
        ibsContact.setPobox(contactDets.getAddressVO().getPoBox());
        ibsContact.setPrimaryEmailId(contactDets.getEmailId());
        ibsContact.setPrimaryLandlineNo(contactDets.getLandlineNo());
        ibsContact.setPrimaryMobileNo(contactDets.getMobileNo());
        ibsContact.setTitle(contactDets.getTitle());
        // ibsContact.setSalutation(salutation);
        return ibsContact;
    }

    //
    private static <T> Set<T> constructSetOfPOJOForRecType(Class<T> setType) {
        return new HashSet<T>();
    }

    private static IbsCustomer constructIbsCustomer(BaseVO baseVO) {

        IbsCustomer ibsCustomer = new IbsCustomer();
        if (null == baseVO) {
            return ibsCustomer;
        }
        CustomerVO customerVO = (CustomerVO) baseVO;
        ibsCustomer.setId(customerVO.getCustomerId());
        ibsCustomer.setCategory(customerVO.getCategory());
        ibsCustomer.setClassification(customerVO.getClassification());
        ibsCustomer.setCustGroup(customerVO.getGroup());
        ibsCustomer.setName(customerVO.getName());
        ibsCustomer.setSalesExecutive(customerVO.getSalesExecutive());
        ibsCustomer.setSalutation(customerVO.getSalutation());
        ibsCustomer.setSource(customerVO.getSourceOfBusiness());
        // bsCustomer.setStatus(customerVO.getIsStatusActive());
        long currentTime = System.currentTimeMillis();
        ibsCustomer.setRecCreDate(new Date(currentTime));
        ibsCustomer.setRecUpdDate(new Date(currentTime));
        return ibsCustomer;
    }

    private static IbsCompany constructIbsCompany(BaseVO baseVO) {
        
        if(Utils.isEmpty(baseVO)) {
            return null;
        }
        CompanyVO companyVO = (CompanyVO) baseVO;
        IbsCompany ibsCompany = new IbsCompany();
        ibsCompany.setCode(companyVO.getCode());
        ibsCompany.setName(companyVO.getName());
        ibsCompany.setShortname(companyVO.getShortName());
        ibsCompany.setStatus(companyVO.getIsStatusActive() == "Y"? "1" : "0");
        return ibsCompany;
    }

    private static IbsCompanyBranch constructIbsCompanyBranch(BaseVO baseVO) {
        return null;
    }

    private static IbsInsuranceCompany constructIbsInsCompany(BaseVO baseVO) {
        
        if(Utils.isEmpty(baseVO)) {
            return null;
        }
        InsCompanyVO companyVO = (InsCompanyVO) baseVO;
        IbsInsuranceCompany ibsInsCompany = new IbsInsuranceCompany();
        ibsInsCompany.setCode(companyVO.getCode());
        ibsInsCompany.setName(companyVO.getName());
        ibsInsCompany.setShortname(companyVO.getShortName());
        ibsInsCompany.setStatus(companyVO.getIsStatusActive());
        
        //populate company product details
        

        return ibsInsCompany;
    }

/**
     * 
     * @param baseVO
     * @return
     */
    public static List<IbsInsuranceCompanyContact> constructIbsInsCompanyContact(BaseVO baseVO) {
        
        if(Utils.isEmpty(baseVO)) {
            return null;
        }
        List<IbsInsuranceCompanyContact> contacts = new ArrayList<IbsInsuranceCompanyContact>();
        InsCompanyVO insCompanyVO = (InsCompanyVO) baseVO;
        
        IbsInsuranceCompanyContact contact = null;
        for(Entry<ProductVO, ContactVO> entry : insCompanyVO.getContactForProduct().entrySet()) {
            contact = new IbsInsuranceCompanyContact();
            contact.setIbsProductMaster(constructIbsProduct(entry.getKey()));
            contact.setIbsContact(constructIbsContact(entry.getValue()));
            contact.setStatus("Y");
            contact.setCompanyName(insCompanyVO.getName());
            IbsInsuranceCompany ibsInsCompany = new IbsInsuranceCompany();
            ibsInsCompany.setCode(insCompanyVO.getCode());
            contact.setIbsInsuranceCompany(ibsInsCompany);
            contacts.add(contact);
        }
        
        return contacts;
    }
    
    /**
     * 
     * @param baseVO
     * @return
     */
    public static List<IbsInsuranceCompProdLink> constructIbsInsuranceCompProdLink(BaseVO baseVO) {
        if(Utils.isEmpty(baseVO)) {
            return null;
        }
        List<IbsInsuranceCompProdLink> compProdLinkList = new ArrayList<IbsInsuranceCompProdLink>();
        InsCompanyVO insCompanyVO = (InsCompanyVO) baseVO;
        
        IbsInsuranceCompProdLink compProdLink = null;
        for(Entry<ProductVO, ContactVO> entry : insCompanyVO.getContactForProduct().entrySet()) {
            compProdLink = new IbsInsuranceCompProdLink();
            compProdLink.setCompanyCode(insCompanyVO.getCode());
            compProdLink.setStatus("Y");
            compProdLink.setIbsProductMaster(constructIbsProduct(entry.getKey()));
            
            compProdLinkList.add(compProdLink);
        }
        
        return compProdLinkList;
    }

    private static IbsInsuredMaster constructIbsInsuredMaster(BaseVO baseVO) {
        IbsInsuredMaster ibsInsured = new IbsInsuredMaster();
        if (null == baseVO) {
            return ibsInsured;
        }
        InsuredVO insuredVO = (InsuredVO) baseVO;
        ibsInsured.setId(insuredVO.getId());
        ibsInsured.setName(insuredVO.getName());
        ibsInsured.setSalutation(insuredVO.getSalutation());
        ibsInsured.setSource(insuredVO.getSourceOfBusiness());
        ibsInsured.setIbsCustomer(constructIbsCustomer(insuredVO.getCustomerDetails()));
        return ibsInsured;
    }

    private static IbsCustomerEnquiry constructIbsCustEnquiry(BaseVO baseVO) {

        EnquiryVO enquiryVO = (EnquiryVO) baseVO;
        IbsCustomerEnquiry customerEnquiry = new IbsCustomerEnquiry();
        customerEnquiry.setEnquiryDescription(enquiryVO.getDescription());
        customerEnquiry.setEnquiryNo(enquiryVO.getEnquiryNo());
        customerEnquiry.setEnquirySubjectmatterExpert(enquiryVO.getEnquirySme());
        // customerEnquiry.setIbsProductMaster(constructIbsProduct(enquiryVO.getProduct()));
        customerEnquiry.setIbsCustomer(constructIbsCustomer(enquiryVO.getCustomerDetails()));
        // customerEnquiry.setIbsContact(constructIbsContact(enquiryVO.getEnquiryContact()));
        customerEnquiry.setIsActive(enquiryVO.getIsStatusActive());
        if (null != enquiryVO.getType())
            customerEnquiry.setType(enquiryVO.getType().getEnquiryType());

        return customerEnquiry;
    }

    public static IbsQuoteSlipHeader constructIbsQuoteSlipHeader(BaseVO baseVO) {

        PolicyVO policyVO = (PolicyVO) baseVO;
        IbsQuoteSlipHeader slipHeader = new IbsQuoteSlipHeader();

        Map<InsCompanyVO, QuoteDetailVO> quoteDetails = policyVO.getQuoteDetails();
        if (Utils.isEmpty(quoteDetails)) {
            return null;
        }
        Set<IbsQuoteSlipDetail> ibsQuoteSlipDetailsAll = new HashSet<IbsQuoteSlipDetail>();
        Set<Entry<InsCompanyVO, QuoteDetailVO>> quouEntries = quoteDetails.entrySet();
        Iterator<Entry<InsCompanyVO, QuoteDetailVO>> it = quouEntries.iterator();
        Entry<InsCompanyVO, QuoteDetailVO> entry = null;
        QuoteDetailVO quoteDetailVO = null;
        InsCompanyVO insCompanyVO = null;
        while (it.hasNext()) {
            entry = it.next();
            insCompanyVO = entry.getKey();
            quoteDetailVO = entry.getValue();
            Set<IbsQuoteSlipDetail> ibsQuoteSlipDetails =
                constructIbsQuoteSlipDetails(quoteDetailVO);

            for (IbsQuoteSlipDetail slipDetail : ibsQuoteSlipDetails) {
                // this is to set detail IDs in header table
                slipDetail.setIbsQuoteSlipHeader(slipHeader);
                if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
                    slipDetail.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
                } else {
                    logger.warn("Enquiry details missing");
                }
                slipDetail.setEnquiryCompanyCode(insCompanyVO.getCode());
                // set status as always active=1
                StatusVO statusVO = new StatusVO();
                statusVO.setCode(1);
                slipDetail.setIbsStatusMaster(constructIbsStatusMaster(statusVO));
            }
            ibsQuoteSlipDetailsAll.addAll(ibsQuoteSlipDetails);
            slipHeader.setRemarks(quoteDetailVO.getRemarks());

            // set quote slip header ID if already present.
            if (!Utils.isEmpty(quoteDetailVO.getQuoteSlipId())) {
                IbsQuoteSlipHeaderId ibsQuoteSlipHeaderId = new IbsQuoteSlipHeaderId();
                ibsQuoteSlipHeaderId.setId(quoteDetailVO.getQuoteSlipId());
                ibsQuoteSlipHeaderId.setQuoteSlipVersion(quoteDetailVO.getQuoteSlipVersion());
                slipHeader.setId(ibsQuoteSlipHeaderId);
            }

        }
        slipHeader.setIbsQuoteSlipDetails(ibsQuoteSlipDetailsAll);
        if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
            slipHeader.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
        }
        // following details in slip header will be same in each of insurance
        // company quote details
        if (!Utils.isEmpty(quoteDetailVO.getQuoteSlipId())) {
            IbsQuoteSlipHeaderId slipHeaderId = new IbsQuoteSlipHeaderId();
            slipHeaderId.setId(quoteDetailVO.getQuoteSlipId());
            slipHeaderId.setQuoteSlipVersion(null == quoteDetailVO.getQuoteSlipVersion() ? 1
                : quoteDetailVO.getQuoteSlipVersion());
            slipHeader.setId(slipHeaderId);
        }
        slipHeader.setCustomerId(policyVO.getEnquiryDetails().getCustomerDetails().getCustomerId());
        slipHeader.setIbsProductMaster(constructIbsProduct(quoteDetailVO.getProductDetails()));
        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        if (quoteDetailVO.getStatusCode() != null) {
            ibsStatusMaster.setCode((long) quoteDetailVO.getStatusCode());
        }
        slipHeader.setIbsStatusMaster(ibsStatusMaster);
        if (!Utils.isEmpty(policyVO.getInsuredDetails())) {
            slipHeader.setInsuredId(policyVO.getInsuredDetails().getId());
            slipHeader.setInsuredName(policyVO.getInsuredDetails().getName());
        }
        slipHeader.setPolicyStartDate(constructSqlDate(policyVO.getPolicyEffectiveDate()));
        slipHeader.setPolicyExpiryDate(constructSqlDate(policyVO.getPolicyExpiryDate()));

        slipHeader.setQuoteSlipDate(constructSqlDate(quoteDetailVO.getQuoteSlipDate()));

        slipHeader.setQuoteSlipEmailed(quoteDetailVO.getIsClosingSlipEmailed());
        if (null != quoteDetailVO.getProductDetails()) {
            slipHeader.setSubClass(String.valueOf(quoteDetailVO.getProductDetails().getSubClass()));
        }

        return slipHeader;

    }

    private static Set<IbsQuoteSlipDetail> constructIbsQuoteSlipDetails(QuoteDetailVO quoteDetailVO) {

        ProductVO productVO = quoteDetailVO.getProductDetails();
        Set<IbsQuoteSlipDetail> slipDetailSet = new HashSet<IbsQuoteSlipDetail>();
        for (ProductUWFieldVO uwFieldVO : productVO.getUwFieldsList()) {
            IbsQuoteSlipDetail slipDetail = new IbsQuoteSlipDetail();
            slipDetail.setIbsProductMaster(constructIbsProduct(productVO));
            slipDetail.setProductUwFieldAnswer(uwFieldVO.getFieldValue());
            slipDetail.setIbsProductUwFields(constructIbsProductUwField(uwFieldVO));
            // slipDetail.setId(uwFieldVO.getTableId());
            slipDetail.setProductUwFieldAnswer(uwFieldVO.getResponse());
            slipDetail.setQuoteSlipDate(constructSqlDate(quoteDetailVO.getQuoteDate()));
            slipDetail.setQuoteSlipEmailed(quoteDetailVO.getIsClosingSlipEmailed());
            slipDetailSet.add(slipDetail);
        }
        return slipDetailSet;
    }

    public static IbsProductUwFields constructIbsProductUwField(ProductUWFieldVO uwFieldVO) {
        IbsProductUwFields field = new IbsProductUwFields();
        field.setId(uwFieldVO.getUwFieldId());
        field.setFieldName(uwFieldVO.getFieldName());
        field.setFieldType(uwFieldVO.getFieldType());
        if (!Utils.isEmpty(uwFieldVO.getFieldLength())) {
            field.setFieldLength((long) uwFieldVO.getFieldLength());
        }
        field.setIsMandatory(uwFieldVO.getIsMandatory());
        if (!Utils.isEmpty(uwFieldVO.getFieldOrder())) {
            field.setSrlNo((long) uwFieldVO.getFieldOrder());
        }
        field.setFieldValueType(uwFieldVO.getFieldValueType());
        field.setStatus(uwFieldVO.getIsStatusActive());
        return field;
    }

    public static IbsProductMaster constructIbsProduct(ProductVO productDetails) {

        IbsProductMaster ibsProductMaster = new IbsProductMaster();
        if (Utils.isEmpty(productDetails)) {
            return null;
        }
        if (null != productDetails.getProductClass()) {
            ibsProductMaster.setClass_(Long.valueOf(productDetails.getProductClass()));
        }
        ibsProductMaster.setCategorisation(productDetails.getCategory());
        ibsProductMaster.setName(productDetails.getName());
        ibsProductMaster.setShortname(productDetails.getShortName());
        ibsProductMaster.setStatus(productDetails.getIsStatusActive());
        if (null != productDetails.getSubClass()) {
            ibsProductMaster.setSubclass(Long.valueOf(productDetails.getSubClass()));
        }

        return ibsProductMaster;
    }

    /**
     * 
     * 
     * @param baseVO
     * @return
     */
    public static IbsQuoteComparisionHeader constructIbsQuoteComparisionHeader(BaseVO baseVO) {

        PolicyVO policyVO = (PolicyVO) baseVO;
        IbsQuoteComparisionHeader quoteHeader = new IbsQuoteComparisionHeader();

        Map<InsCompanyVO, QuoteDetailVO> quoteDetails = policyVO.getQuoteDetails();
        if (Utils.isEmpty(quoteDetails)) {
            return null;
        }
        IbsQuoteSlipHeader ibsQuoteSlipHeader = new IbsQuoteSlipHeader();
        IbsQuoteSlipHeaderId headerId = new IbsQuoteSlipHeaderId();

        Set<IbsQuoteComparisionDetail> ibsQuoteDetailsAll =
            new HashSet<IbsQuoteComparisionDetail>();
        QuoteDetailVO quoteDetailVO = null;
        InsCompanyVO insCompanyVO = null;
        for (Entry<InsCompanyVO, QuoteDetailVO> entry : quoteDetails.entrySet()) {

            insCompanyVO = entry.getKey();
            quoteDetailVO = entry.getValue();
            Set<IbsQuoteComparisionDetail> ibsQuoteSlipDetails =
                constructIbsComparisionDetails(quoteDetailVO);

            for (IbsQuoteComparisionDetail quoteDetail : ibsQuoteSlipDetails) {
                // this is to set detail IDs in header table
                quoteDetail.setIbsQuoteComparisionHeader(quoteHeader);
                if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
                    quoteDetail.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
                } else {
                    logger.warn("Enquiry details missing");
                }
                quoteDetail.setQuotedCompanyCode(insCompanyVO.getCode());

                headerId.setId(quoteDetailVO.getQuoteSlipId());
                headerId.setQuoteSlipVersion(quoteDetailVO.getQuoteSlipVersion());
                ibsQuoteSlipHeader.setId(headerId);
                quoteDetail.setIbsQuoteSlipHeader(ibsQuoteSlipHeader);

                if (!Utils.isEmpty(quoteDetailVO.getQuoteId())) {
                    quoteHeader.setId(quoteDetailVO.getQuoteId());
                }
                if (quoteDetailVO.getIsQuoteRecommended()
                    && !Utils.isEmpty(quoteDetailVO.getRecommendationSummary())) {
                    quoteHeader.setRecommendationSummary(quoteDetailVO.getRecommendationSummary());
                }
            }
            ibsQuoteDetailsAll.addAll(ibsQuoteSlipDetails);

        }
        quoteHeader.setIbsQuoteComparisionDetails(ibsQuoteDetailsAll);
        if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
            quoteHeader.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
        }
        // following details in quote comparison header will be same in each of
        // insurance company
        // quote details

        quoteHeader.setCustomerId(quoteDetailVO.getCustomerId());
        quoteHeader.setIbsProductMaster(constructIbsProduct(quoteDetailVO.getProductDetails()));
        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        if (!Utils.isEmpty(quoteDetailVO.getStatusCode())) {
            // ibsStatusMaster.setCode((long) quoteDetailVO.getStatusCode());
            ibsStatusMaster.setCode(1l);
        } else {
            // default record will be active. i.e active code=1
            ibsStatusMaster.setCode(1l);
        }
        quoteHeader.setIbsStatusMaster(ibsStatusMaster);
        if (!Utils.isEmpty(policyVO.getInsuredDetails())) {
            quoteHeader.setInsuredId(policyVO.getInsuredDetails().getId());
            quoteHeader.setInsuredName(policyVO.getInsuredDetails().getName());
        }
        quoteHeader.setPolicyStartDate(constructSqlDate(policyVO.getPolicyEffectiveDate()));
        quoteHeader.setPolicyExpiryDate(constructSqlDate(policyVO.getPolicyExpiryDate()));
        if (null != quoteDetailVO.getProductDetails()) {
            quoteHeader
                    .setSubClass(String.valueOf(quoteDetailVO.getProductDetails().getSubClass()));
        }

        quoteHeader.setIbsQuoteSlipHeader(ibsQuoteSlipHeader);

        return quoteHeader;
    }

    private static Set<IbsQuoteComparisionDetail> constructIbsComparisionDetails(
            QuoteDetailVO quoteDetailVO) {

        Set<IbsQuoteComparisionDetail> comparisionDetails =
            new HashSet<IbsQuoteComparisionDetail>();

        ProductVO productVO = quoteDetailVO.getProductDetails();
        IbsQuoteComparisionDetail ibsQuoteDetail = null;
        for (ProductUWFieldVO uwFieldVO : productVO.getUwFieldsList()) {
            ibsQuoteDetail = new IbsQuoteComparisionDetail();
            // ibsQuoteDetail.setId(uwFieldVO.getTableId());
            // ibsQuoteDetail.setEnquiryNo(quoteDetailVO.getEnquiryNum());
            // slipDetail.setIbsProductMaster(ibsProductMaster)
            ibsQuoteDetail.setClosingSlipEmailed(quoteDetailVO.getIsClosingSlipEmailed());
            if (!Utils.isEmpty(quoteDetailVO.getPolicyTerm())) {
                ibsQuoteDetail.setPolicyTerm(BigDecimal.valueOf(quoteDetailVO.getPolicyTerm()));
            }

            PremiumVO premiumVO = quoteDetailVO.getQuoteSlipPrmDetails();
            if (null != premiumVO) {

                ibsQuoteDetail.setCommissionOnPrm(premiumVO.getCommissionAmt());
                ibsQuoteDetail.setCoverDescription(premiumVO.getCoverDescription());

                ibsQuoteDetail.setDiscountPercentage((long) premiumVO.getDiscountPercentage());
                ibsQuoteDetail.setQuotedPremium(premiumVO.getPremium());
                if (!Utils.isEmpty(quoteDetailVO.getSumInsured())) {
                    ibsQuoteDetail.setSumInsured(quoteDetailVO.getSumInsured().longValue());
                }
            }
            // ibsQuoteDetail.setEnquiryNo(quoteDetailVO.getEnquiryNum());
            // ibsQuoteDetail.setEnquiryType(quoteDetailVO.gete);
            ibsQuoteDetail.setIbsProductMaster(constructIbsProduct(quoteDetailVO
                    .getProductDetails()));
            ibsQuoteDetail.setIbsProductUwFields(constructIbsProductUwField(uwFieldVO));
            ibsQuoteDetail.setProductUwFieldAnswer(uwFieldVO.getResponse());
            StatusVO statusVO = new StatusVO();
            statusVO.setCode(quoteDetailVO.getStatusCode());
            ibsQuoteDetail.setIbsStatusMaster(constructIbsStatusMaster(statusVO));

            ibsQuoteDetail.setQuoteApprovedBy(String.valueOf(quoteDetailVO
                    .getQuoteSlipApprovedByUserId()));
            ibsQuoteDetail.setQuoteDate(constructSqlDate(quoteDetailVO.getQuoteDate()));

            ibsQuoteDetail.setQuoteNo(quoteDetailVO.getQuoteNo());
            ibsQuoteDetail.setQuoteRecommended(quoteDetailVO.getIsQuoteRecommended() ? "Y" : "N");

            ibsQuoteDetail.setClosingSlipEmailed(quoteDetailVO.getIsClosingSlipEmailed());

            comparisionDetails.add(ibsQuoteDetail);
        }

        return comparisionDetails;
    }

    private static IbsStatusMaster constructIbsStatusMaster(StatusVO statusVO) {

        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        ibsStatusMaster.setCode(Long.valueOf(statusVO.getCode()));

        return ibsStatusMaster;
    }

    private static IbsInsuranceCompany constructIbsInsuranceCompany(BaseVO baseVO) {
        InsCompanyVO insCompanyVO = (InsCompanyVO) baseVO;
        IbsInsuranceCompany ibsInsCompany = new IbsInsuranceCompany();
        ibsInsCompany.setCode(insCompanyVO.getCode());
        return ibsInsCompany;
    }

    private static Date constructSqlDate(java.util.Date date) {

        if (Utils.isEmpty(date)) {
            return null;
        }
        Date sqlDate = new Date(date.getTime());

        return sqlDate;
    }

    /**
     * 
     * @param baseVO
     * @return
     */
    public static IbsUwTransactionHeader constructIbsUwTransactionHeader(BaseVO baseVO) {

        PolicyVO policyVO = (PolicyVO) baseVO;
        IbsUwTransactionHeader ibsUwTranHeader = new IbsUwTransactionHeader();
        IbsUwTransactionHeaderId ibsUwTransactionHeaderId = new IbsUwTransactionHeaderId();
        ibsUwTransactionHeaderId.setId(policyVO.getPolicyId());
        if (!Utils.isEmpty(policyVO.getPolicyVersion()))
            ibsUwTransactionHeaderId.setPolicyVersion(policyVO.getPolicyVersion().longValue());
        ibsUwTranHeader.setId(ibsUwTransactionHeaderId);

        Map<InsCompanyVO, QuoteDetailVO> quoteDetails = policyVO.getQuoteDetails();
        if (Utils.isEmpty(quoteDetails)) {
            return null;
        }
        IbsQuoteSlipHeader ibsQuoteSlipHeader = new IbsQuoteSlipHeader();
        IbsQuoteSlipHeaderId ibsQuoteSlipHeaderId = new IbsQuoteSlipHeaderId();

        IbsQuoteComparisionHeader ibsQuoteComparisionHeader = new IbsQuoteComparisionHeader();


        Set<IbsUwTransactionDetail> ibsUwTranDetails = new HashSet<IbsUwTransactionDetail>();
        QuoteDetailVO quoteDetailVO = null;
        InsCompanyVO insCompanyVO = null;
        for (Entry<InsCompanyVO, QuoteDetailVO> entry : quoteDetails.entrySet()) {

            insCompanyVO = entry.getKey();
            quoteDetailVO = entry.getValue();
            
            
                
            ibsUwTranDetails = constructIbsUwTransactionDetails(quoteDetailVO);

            for (IbsUwTransactionDetail ibsUwTransDetail : ibsUwTranDetails) {
                // this is to set detail IDs in header table
                ibsUwTransDetail.setIbsUwTransactionHeader(ibsUwTranHeader);
                ibsUwTransDetail.setPolicyNo(policyVO.getPolicyNo());// added by Hafeezur
                if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
                    ibsUwTransDetail.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
                } else {
                    logger.warn("Enquiry details missing");
                }
                ibsUwTransDetail.setPolicyCompanyCode(insCompanyVO.getCode());
                ibsQuoteSlipHeaderId.setId(quoteDetailVO.getQuoteSlipId());
                ibsQuoteSlipHeaderId.setQuoteSlipVersion(quoteDetailVO.getQuoteSlipVersion());
                ibsQuoteSlipHeader.setId(ibsQuoteSlipHeaderId);
                ibsUwTransDetail.setIbsQuoteSlipHeader(ibsQuoteSlipHeader);

                // Premium details
                PremiumVO premiumVO = policyVO.getPremiumDetails();
                if (null != premiumVO) {
                    ibsUwTransDetail.setCommissionOnPrm(premiumVO.getCommissionAmt());
                    ibsUwTransDetail.setCoverDescription(premiumVO.getCoverDescription());
                    ibsUwTransDetail
                            .setDiscountPercentage((long) premiumVO.getDiscountPercentage());
                    ibsUwTransDetail.setLoadingPercentage((long) premiumVO.getLoadingPercentage());
                    ibsUwTransDetail.setPolicyPremium(premiumVO.getPremium());
                    
                    //Added by Hafeezur:8th June 2014
                    ibsUwTransDetail.setTotalPremium(premiumVO.getTotalPremium());

                    if (!Utils.isEmpty(premiumVO.getTotalPremium())) {
                        ibsUwTransDetail.setSumInsured(premiumVO.getTotalPremium().longValue());
                    }
                }

                //Added by Hafeezur:8th June 2014
               ibsUwTransDetail.setPolicyStartDate(constructSqlDate(policyVO.getPolicyEffectiveDate()));
               ibsUwTransDetail.setPolicyExpiryDate(constructSqlDate(policyVO.getPolicyExpiryDate()));
               ibsUwTransDetail.setPolicyTerm(BigDecimal.valueOf(quoteDetailVO.getPolicyTerm()));
               ibsUwTransDetail.setCoverDescription(quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription());
               
               
               ibsQuoteComparisionHeader.setId(quoteDetailVO.getQuoteId());
            }
        
        // since there will be only one insurance company record for a policy, break out after
           // first iteration
            break;
        }
        ibsUwTranHeader.setIbsUwTransactionDetails(ibsUwTranDetails);
        if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
            ibsUwTranHeader.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
        }

        ibsUwTranHeader.setCustomerId(quoteDetailVO.getCustomerId());
        ibsUwTranHeader.setIbsProductMaster(constructIbsProduct(quoteDetailVO.getProductDetails()));
        IbsStatusMaster ibsStatusMaster = new IbsStatusMaster();
        ibsStatusMaster.setCode((long) quoteDetailVO.getStatusCode());
        ibsUwTranHeader.setIbsStatusMaster(ibsStatusMaster);

        // Populate policy related datas here
        ibsUwTranHeader.setPolicyNo(policyVO.getPolicyNo());
        if (!Utils.isEmpty(policyVO.getInsuredDetails())) {
            ibsUwTranHeader.setInsuredId(policyVO.getInsuredDetails().getId());
            ibsUwTranHeader.setInsuredName(policyVO.getInsuredDetails().getName());
        }
        ibsUwTranHeader.setPolicyStartDate(constructSqlDate(policyVO.getPolicyEffectiveDate()));
        ibsUwTranHeader.setPolicyExpiryDate(constructSqlDate(policyVO.getPolicyExpiryDate()));
        if (null != quoteDetailVO.getProductDetails()) {
            ibsUwTranHeader.setSubClass(String.valueOf(quoteDetailVO.getProductDetails()
                    .getSubClass()));
        }

        ibsUwTranHeader.setIbsQuoteSlipHeader(ibsQuoteSlipHeader);
        ibsUwTranHeader.setIbsQuoteComparisionHeader(ibsQuoteComparisionHeader);

        if (!Utils.isEmpty(policyVO.getEnquiryDetails())) {
            ibsUwTranHeader.setEnquiryNo(policyVO.getEnquiryDetails().getEnquiryNo());
        }

        return ibsUwTranHeader;

    }

    /**
     * 
     * @param quoteDetailVO
     * @return
     */
    private static Set<IbsUwTransactionDetail> constructIbsUwTransactionDetails(
            QuoteDetailVO quoteDetailVO) {
        Set<IbsUwTransactionDetail> comparisionDetails = new HashSet<IbsUwTransactionDetail>();

        ProductVO productVO = quoteDetailVO.getProductDetails();
        IbsUwTransactionDetail ibsQuoteDetail = null;
        for (ProductUWFieldVO uwFieldVO : productVO.getUwFieldsList()) {
            ibsQuoteDetail = new IbsUwTransactionDetail();
            ibsQuoteDetail.setPolicyCompanyCode(quoteDetailVO.getCompanyCode());
            
            
            ibsQuoteDetail.setProductUwFieldAnswer(uwFieldVO.getResponse());
            PremiumVO premiumVO = quoteDetailVO.getQuoteSlipPrmDetails();
            ibsQuoteDetail.setIbsProductUwFields(constructIbsProductUwField(uwFieldVO));
            StatusVO statusVO = new StatusVO();
            statusVO.setCode(quoteDetailVO.getStatusCode());
            ibsQuoteDetail.setIbsStatusMaster(constructIbsStatusMaster(statusVO));
            if (null != premiumVO) {
                ibsQuoteDetail.setCommissionOnPrm(premiumVO.getCommissionAmt());
                ibsQuoteDetail.setCoverDescription(premiumVO.getCoverDescription());
                ibsQuoteDetail.setDiscountPercentage((long) premiumVO.getDiscountPercentage());
                ibsQuoteDetail.setLoadingPercentage((long) premiumVO.getLoadingPercentage());
                ibsQuoteDetail.setPolicyPremium(premiumVO.getPremium());
                if (!Utils.isEmpty(premiumVO.getTotalPremium())) {
                    ibsQuoteDetail.setSumInsured(premiumVO.getTotalPremium().longValue());
                }
            }
            if (!Utils.isEmpty(quoteDetailVO.getSumInsured())) {
                ibsQuoteDetail.setSumInsured(quoteDetailVO.getSumInsured().longValue());
            }
            ibsQuoteDetail.setIbsProductMaster(constructIbsProduct(quoteDetailVO
                    .getProductDetails()));
           // ibsQuoteDetail.setIbsProductUwFields(constructIbsProductUwField(uwFieldVO));
            ibsQuoteDetail.setQuoteNo(quoteDetailVO.getQuoteNo());

            IbsQuoteComparisionHeader ibsQuoteComparisionHeader = new IbsQuoteComparisionHeader();
            ibsQuoteComparisionHeader.setId(quoteDetailVO.getQuoteId());
            ibsQuoteDetail.setIbsQuoteComparisionHeader(ibsQuoteComparisionHeader);
            
            comparisionDetails.add(ibsQuoteDetail);
        }

        return comparisionDetails;
    }

    /**
     * 
     * @param taskVO
     * @return
     */
    public static IbsTask constructIbsTask(TaskVO taskVO) {

        if (Utils.isEmpty(taskVO)) {
            return null;
        }
        IbsTask ibsTask = new IbsTask();
        if (!Utils.isEmpty(taskVO.getAssigneeUser())) {
            ibsTask.setAssigneeUserId(taskVO.getAssigneeUser().getUserId());
        } else {
            logger.warn("Assignee user details can never be null in task. Please check data");
        }
        if (!Utils.isEmpty(taskVO.getAssignerUser())) {
            ibsTask.setAssignorUserId(taskVO.getAssignerUser().getUserId());
        } else {
            logger.warn("Assigner user details can never be null in task. Please check data");
        }
        if (!Utils.isEmpty(taskVO.getDocument())
            && !Utils.isEmpty(taskVO.getDocument().getDocSlipId())) {
            ibsTask.setDocumentId(taskVO.getDocument().getDocSlipId().toString());
        } else {
            logger.warn("Document can not be null in task. Please check data");
        }
        ibsTask.setReferralDesc(taskVO.getDesc());
        if (!Utils.isEmpty(taskVO.getEnquiry())) {
            ibsTask.setEnquiryNo(taskVO.getEnquiry().getEnquiryNo());
        } else {
            logger.warn("Enquiry details can not be null in task. Please check data");
        }
        if(!Utils.isEmpty(taskVO.getTaskType())) {
            ibsTask.setTaskType(Long.valueOf(taskVO.getTaskType().longValue()));
        }
        if(!Utils.isEmpty(taskVO.getTaskSectionType())) {
            ibsTask.setTaskSectionType(Long.valueOf(taskVO.getTaskSectionType().longValue()));
        }        
        
        ibsTask.setIbsStatusMaster(constructIbsStatusMaster(taskVO.getStatusVO()));
        ibsTask.setId(taskVO.getId());
        
        ibsTask.setPriority(taskVO.getPriority());
        ibsTask.setDueDate(taskVO.getDueDate());

        return ibsTask;
    }

    public static IbsDocumentTable constructIbsDocumentTable(DocumentVO documentVO) {

        if (Utils.isEmpty(documentVO)) {
            return null;
        }
        IbsDocumentTable ibsDocument = new IbsDocumentTable();
        ibsDocument.setDocRecievedDesc(documentVO.getDocDescreption());
        ibsDocument.setDocSlipId(documentVO.getDocSlipId());
        if (!Utils.isEmpty(documentVO.getDocSlipVersion())) {
            ibsDocument.setDocSlipVersion(documentVO.getDocSlipVersion().longValue());
        }
        if(!Utils.isEmpty(documentVO.getDocument())){
            Blob blob = new BlobImpl(documentVO.getDocument());
            ibsDocument.setDocument(blob);
        }        
        ibsDocument.setDocumentType(documentVO.getDocType());
        if (!Utils.isEmpty(documentVO.getEnquiry())) {
            ibsDocument.setEnquiryNo(documentVO.getEnquiry().getEnquiryNo());
            if (!Utils.isEmpty(documentVO.getEnquiry().getType())) {
                ibsDocument.setEnquiryType(documentVO.getEnquiry().getType().getEnquiryType());
            }
        }
        ibsDocument.setId(documentVO.getId());

        return ibsDocument;
    }
    
    /**
     * Returns IbsTask object based on the enquiry no passed to this method
     * @param hibernateTemplate
     * @param enquiryNo
     * @return null in case enquiry no is null
     */
    public static IbsTask queryTaskTblForEnquiryNo(HibernateTemplate hibernateTemplate, Long enquiryNo, Long taskType, Long sectionType){
        if(Utils.isEmpty(enquiryNo)){
            return null;
        }
        
        List objList = null;
        try {
            objList =  hibernateTemplate.find(
                " from IbsTask ibsTask where ibsTask.enquiryNo = ? ORDER BY ibsTask.id DESC",
                enquiryNo);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetTaskDetails", hibernateException,
                "Error while fecthing Task details");
        }
        if(!Utils.isEmpty(objList)) {
            return (IbsTask)objList.get(0);
        }
        return null;
    }

    /**
     * 
     * @param claimsVO
     * @return
     */
    public static IbsClaims constructIbsClaims(ClaimsVO claimsVO) {
        IbsClaims ibsClaims = new IbsClaims(); 
        if(!Utils.isEmpty(claimsVO.getId())) {
            ibsClaims.setId(claimsVO.getId());
        }
        if(!Utils.isEmpty(claimsVO.getCustomerDetails())) {
            ibsClaims.setIbsCustomer(constructIbsCustomer(claimsVO.getCustomerDetails()));
        }
        if(!Utils.isEmpty(claimsVO.getInsuredDetails())) {
            ibsClaims.setIbsInsuredMaster(constructIbsInsuredMaster(claimsVO.getInsuredDetails()));
        }
        if(!Utils.isEmpty(claimsVO.getPolicyDetails()) && !Utils.isEmpty(claimsVO.getPolicyDetails().getPolicyId())) {
            IbsUwTransactionHeader ibsUwTransactionHeader = new IbsUwTransactionHeader();
            IbsUwTransactionHeaderId ibsUwTransactionHeaderId = new IbsUwTransactionHeaderId();
            ibsUwTransactionHeaderId.setId(claimsVO.getPolicyDetails().getPolicyId());
            if(!Utils.isEmpty(claimsVO.getPolicyDetails().getPolicyVersion())) {
                ibsUwTransactionHeaderId.setPolicyVersion(claimsVO.getPolicyDetails().getPolicyVersion().longValue());
            } else {
                ibsUwTransactionHeaderId.setPolicyVersion(1l);
            }            
            ibsUwTransactionHeader.setId(ibsUwTransactionHeaderId);
            ibsClaims.setIbsUwTransactionHeader(ibsUwTransactionHeader);
        }
        if(!Utils.isEmpty(claimsVO.getLossAmountEstimate())) {
            ibsClaims.setLossAmount(claimsVO.getLossAmountEstimate());
        }
        
        ibsClaims.setLossDate(constructSqlDate(claimsVO.getLossDateTime()));        
        ibsClaims.setLossDescription(claimsVO.getLossDescription());
        
        return ibsClaims;

    }

}
