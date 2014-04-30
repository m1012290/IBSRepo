package com.shrinfo.ibs.mb;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.SearchItemVO;
import com.shrinfo.ibs.vo.business.TaskVO;



@ManagedBean(name = "editCustEnqDetailsMB")
@SessionScoped
public class EditCustEnqDetailsMB extends BaseManagedBean implements Serializable {

    private static final long serialVersionUID = 6311980649197374413L;

    private static final Logger logger = Logger.getLogger(EditCustEnqDetailsMB.class);

    private String custName;

    private String custGroup;

    private String firstName;

    private String middleName;

    private String lastName;

    private String primaryEmailID;

    private String primaryMobNum;

    private String primaryLandLineNum;

    private String faxNum;

    private String city;

    private String country;

    private String address;

    private String poBox;

    private String locLattitude;

    private String locLongitude;

    private String altEmailID1;

    private String altMobNum1;

    private String altLandLineNum1;

    private String altEmailID2;

    private String altMobNum2;

    private String altLandLineNum2;

    private String sourceOfBusiness;

    private String salesExecutive;

    private String title;

    private String custCategory;

    private String custClassification;

    private String salutation;

    private Long enquiryNum;

    private String enquiryType;

    private String enquiryDesc;

    private String recCreatedUserID;

    private Timestamp recCreatedDate;

    private String recUpdatedUserID;

    private Timestamp recUpdatedDate;

    private Long quotationNum;

    private String policyNum;

    private Long quoteVersion;

    private Long endorsementNum;

    private Long quoteSlipId;

    private EnquiryVO enquiryVO = new EnquiryVO();

    private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();

    private PolicyVO policyVO = new PolicyVO();

    private InsuredVO insuredDetails = new InsuredVO();
    
    private Boolean screenFreeze = Boolean.FALSE;

        
    public Boolean getScreenFreeze() {
        return screenFreeze;
    }

    
    public void setScreenFreeze(Boolean screenFreeze) {
        this.screenFreeze = screenFreeze;
    }

    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.custName = null;
        this.custGroup = null;
        this.firstName = null;
        this.middleName = null;
        this.lastName = null;
        this.primaryEmailID = null;
        this.primaryMobNum = null;
        this.primaryLandLineNum = null;
        this.faxNum = null;
        this.city = null;
        this.country = null;
        this.address = null;
        this.poBox = null;
        this.locLattitude = null;
        this.locLongitude = null;
        this.altEmailID1 = null;
        this.altMobNum1 = null;
        this.altLandLineNum1 = null;
        this.altEmailID2 = null;
        this.altMobNum2 = null;
        this.altLandLineNum2 = null;
        this.sourceOfBusiness = null;
        this.salesExecutive = null;

        this.title = null;
        this.custCategory = null;
        this.custClassification = null;
        this.salutation = null;

        this.enquiryNum = null;
        this.enquiryType = null;
        this.enquiryDesc = null;
        this.recCreatedUserID = null;
        this.recCreatedDate = null;
        this.recUpdatedUserID = null;
        this.recUpdatedDate = null;

        this.quotationNum = null;
        this.policyNum = null;
        this.quoteVersion = null;
        this.endorsementNum = null;
        this.quoteSlipId = null;

        this.enquiryVO = new EnquiryVO();
        this.quoteDetailVO = new QuoteDetailVO();
        this.policyVO = new PolicyVO();
        this.insuredDetails = new InsuredVO();
        this.screenFreeze = Boolean.FALSE;
        this.setAppFlow(null);//reinitialize appflow value
    }

    public EditCustEnqDetailsMB() {
        super();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustGroup() {
        return custGroup;
    }

    public void setCustGroup(String custGroup) {
        this.custGroup = custGroup;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimaryEmailID() {
        return primaryEmailID;
    }

    public void setPrimaryEmailID(String primaryEmailID) {
        this.primaryEmailID = primaryEmailID;
    }

    public String getPrimaryMobNum() {
        return primaryMobNum;
    }

    public void setPrimaryMobNum(String primaryMobNum) {
        this.primaryMobNum = primaryMobNum;
    }

    public String getPrimaryLandLineNum() {
        return primaryLandLineNum;
    }

    public void setPrimaryLandLineNum(String primaryLandLineNum) {
        this.primaryLandLineNum = primaryLandLineNum;
    }

    public String getFaxNum() {
        return faxNum;
    }

    public void setFaxNum(String faxNum) {
        this.faxNum = faxNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getQuoteSlipId() {
        return quoteSlipId;
    }

    public void setQuoteSlipId(Long quoteSlipId) {
        this.quoteSlipId = quoteSlipId;
    }

    public String getPoBox() {
        return poBox;
    }

    public void setPoBox(String poBox) {
        this.poBox = poBox;
    }

    public String getLocLattitude() {
        return locLattitude;
    }

    public void setLocLattitude(String locLattitude) {
        this.locLattitude = locLattitude;
    }

    public InsuredVO getInsuredDetails() {
        return insuredDetails;
    }

    public void setInsuredDetails(InsuredVO insuredDetails) {
        this.insuredDetails = insuredDetails;
    }

    public EnquiryVO getEnquiryVO() {
        return enquiryVO;
    }

    public void setEnquiryVO(EnquiryVO enquiryVO) {
        this.enquiryVO = enquiryVO;
    }

    public String getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(String locLongitude) {
        this.locLongitude = locLongitude;
    }

    public String getAltEmailID1() {
        return altEmailID1;
    }

    public void setAltEmailID1(String altEmailID1) {
        this.altEmailID1 = altEmailID1;
    }

    public String getAltMobNum1() {
        return altMobNum1;
    }

    public void setAltMobNum1(String altMobNum1) {
        this.altMobNum1 = altMobNum1;
    }

    public String getAltLandLineNum1() {
        return altLandLineNum1;
    }

    public void setAltLandLineNum1(String altLandLineNum1) {
        this.altLandLineNum1 = altLandLineNum1;
    }

    public String getAltEmailID2() {
        return altEmailID2;
    }

    public void setAltEmailID2(String altEmailID2) {
        this.altEmailID2 = altEmailID2;
    }

    public String getAltMobNum2() {
        return altMobNum2;
    }

    public void setAltMobNum2(String altMobNum2) {
        this.altMobNum2 = altMobNum2;
    }

    public String getAltLandLineNum2() {
        return altLandLineNum2;
    }

    public void setAltLandLineNum2(String altLandLineNum2) {
        this.altLandLineNum2 = altLandLineNum2;
    }

    public String getSourceOfBusiness() {
        return sourceOfBusiness;
    }

    public void setSourceOfBusiness(String sourceOfBusiness) {
        this.sourceOfBusiness = sourceOfBusiness;
    }

    public String getSalesExecutive() {
        return salesExecutive;
    }

    public void setSalesExecutive(String salesExecutive) {
        this.salesExecutive = salesExecutive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustCategory() {
        return custCategory;
    }

    public void setCustCategory(String custCategory) {
        this.custCategory = custCategory;
    }

    public String getCustClassification() {
        return custClassification;
    }

    public void setCustClassification(String custClassification) {
        this.custClassification = custClassification;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Long getEnquiryNum() {
        return enquiryNum;
    }

    public void setEnquiryNum(Long enquiryNum) {
        this.enquiryNum = enquiryNum;
    }

    public String getEnquiryType() {
        return enquiryType;
    }

    public void setEnquiryType(String enquiryType) {
        this.enquiryType = enquiryType;
    }

    public String getEnquiryDesc() {
        return enquiryDesc;
    }

    public void setEnquiryDesc(String enquiryDesc) {
        this.enquiryDesc = enquiryDesc;
    }

    public String getRecCreatedUserID() {
        return recCreatedUserID;
    }

    public void setRecCreatedUserID(String recCreatedUserID) {
        this.recCreatedUserID = recCreatedUserID;
    }

    public Timestamp getRecCreatedDate() {
        return recCreatedDate;
    }

    public void setRecCreatedDate(Timestamp recCreatedDate) {
        this.recCreatedDate = recCreatedDate;
    }

    public String getRecUpdatedUserID() {
        return recUpdatedUserID;
    }

    public void setRecUpdatedUserID(String recUpdatedUserID) {
        this.recUpdatedUserID = recUpdatedUserID;
    }

    public Timestamp getRecUpdatedDate() {
        return recUpdatedDate;
    }

    public void setRecUpdatedDate(Timestamp recUpdatedDate) {
        this.recUpdatedDate = recUpdatedDate;
    }

    public Long getQuotationNum() {
        return quotationNum;
    }

    public void setQuotationNum(long quotationNum) {
        this.quotationNum = quotationNum;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }

    public Long getQuoteVersion() {
        return quoteVersion;
    }

    public void setQuoteVersion(long quoteVersion) {
        this.quoteVersion = quoteVersion;
    }

    public Long getEndorsementNum() {
        return endorsementNum;
    }

    public void setEndorsementNum(long endorsementNum) {
        this.endorsementNum = endorsementNum;
    }

    public QuoteDetailVO getQuoteDetailVO() {
        return quoteDetailVO;
    }

    public void setQuoteDetailVO(QuoteDetailVO quoteDetailVO) {
        this.quoteDetailVO = quoteDetailVO;
    }


    public PolicyVO getPolicyVO() {
        return policyVO;
    }

    public void setPolicyVO(PolicyVO policyVO) {
        this.policyVO = policyVO;
    }

    public void onTaskSelect(SelectEvent event) {

        try {
            TaskVO taskVO = (TaskVO) event.getObject();
            this.enquiryVO.setEnquiryNo(taskVO.getEnquiry().getEnquiryNo());
            this.enquiryVO =
                (EnquiryVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerEnquirySvc",
                    "getCustomerEnquiry", this.enquiryVO);
            setReferralFlag();
            this.quoteDetailVO.setEnquiryNum(this.enquiryVO.getEnquiryNo());
            PolicyVO policyVO  =  (PolicyVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getQuoteSlipDetails",this.quoteDetailVO);
            if(!Utils.isEmpty(policyVO)) {
                this.quoteSlipId = policyVO.getQuoteDetails().entrySet().iterator().next().getValue().getQuoteSlipId();
            }
            this.setAppFlow(AppFlow.REFERRAL_APPROVAL);
            FacesContext.getCurrentInstance().getExternalContext().redirect("editenquiry.xhtml");
        } catch (IOException e) {
            logger.error(e, "Exception [" + e
                + "] encountered while loading customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_ENQUIRY_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "encountered while loading customer/enquiry details", null));
        } catch (BusinessException be) {
            logger.error(be, "Exception [" + be
                + "] encountered while loading customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_ENQUIRY_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "encountered while loading customer/enquiry details", null));

        } catch (SystemException se) {
            logger.error(se, "Exception [" + se
                + "] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_ENQUIRY_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "Unexpected error encountered, please try again after sometime"));
        }


    }

    /**
     * 
     * @param event
     */
    public void onRowSelect(SelectEvent event) {

        try {
            SearchItemVO searchItemVO = (SearchItemVO) event.getObject();
            this.enquiryVO.setEnquiryNo(searchItemVO.getEnquiryNum());
            this.enquiryVO =
                (EnquiryVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerEnquirySvc",
                    "getCustomerEnquiry", this.enquiryVO);
            this.insuredDetails.setId(searchItemVO.getInsuredId());
            this.quoteSlipId = searchItemVO.getQuotationNum();
            this.policyNum = searchItemVO.getPolicyNum();
            this.policyVO.setPolicyNo(this.policyNum);
            setReferralFlag();
            FacesContext.getCurrentInstance().getExternalContext().redirect("editenquiry.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BusinessException be) {
            logger.error(be, "Exception [" + be
                + "] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance()
                    .addMessage(
                        "ERROR_ENQUIRY_SAVE",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Unexpected error encountered", null));

        } catch (SystemException se) {
            logger.error(se, "Exception [" + se
                + "] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_ENQUIRY_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "Unexpected error encountered, please try again after sometime"));
        }

    }
    
    private void setReferralFlag() {
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();
        // Referral
        TaskVO taskVO = new TaskVO();               
        taskVO.setEnquiry(this.enquiryVO);
        LoginMB loginManageBean = (LoginMB) map.get("loginBean");
        taskVO = this.checkReferral(loginManageBean.getUserDetails(), taskVO, 3);
        if(!Utils.isEmpty(taskVO)) {
            this.screenFreeze = Boolean.TRUE;         
        }
    }

    // Save Customer and Enquiry details
    /**
     * 
     * @return
     */
    public String save() {
        EnquiryVO responseVO = null;
        try {

            ContactVO enquiryContact = new ContactVO();
            if (!Utils.isEmpty(this.enquiryVO.getEnquiryContact())) {
                enquiryContact.setContactId(this.enquiryVO.getEnquiryContact().getContactId());
            }
            enquiryContact.setAddressVO(this.enquiryVO.getCustomerDetails().getContactAndAddrDets()
                    .getAddressVO());
            enquiryContact.setAlternateEmailId1(this.altEmailID1);
            enquiryContact.setAlternateEmailId2(this.altEmailID2);
            enquiryContact.setAlternateMobileNo1(this.altMobNum1);
            enquiryContact.setAlternateMobileNo2(this.altMobNum2);
            enquiryContact.setAlternateLandlineNo1(this.altLandLineNum1);
            enquiryContact.setAlternateLandLineNo2(this.altLandLineNum2);
            enquiryContact.setFirstName(this.firstName);
            enquiryContact.setMiddleName(this.middleName);
            enquiryContact.setLastName(this.lastName);
            enquiryContact.setEmailId(this.primaryEmailID);
            enquiryContact.setMobileNo(this.primaryMobNum);
            enquiryContact.setLandlineNo(this.primaryLandLineNum);
            enquiryContact.setFaxNo(this.faxNum);

            this.enquiryVO.setEnquiryContact(enquiryContact);
            this.policyVO.setPolicyNo(this.policyNum);
            responseVO =
                (EnquiryVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerEnquirySvc",
                    "createCustomerEnquiry", this.enquiryVO);
        } catch (BusinessException be) {
            logger.error(be, "Exception [" + be
                + "] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance()
                    .addMessage(
                        "ERROR_ENQUIRY_SAVE",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Unexpected error encountered", null));
            return null;
        } catch (SystemException se) {
            logger.error(se, "Exception [" + se
                + "] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_ENQUIRY_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "Unexpected error encountered, please try again after sometime"));
            return null;
        }
        logger.info("Customer and enquiry details saved successfully");
        this.enquiryVO = responseVO;
        FacesContext.getCurrentInstance().addMessage(
            "MESSAGE_SUCCESS",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Enquiry Data saved successfully. Enquiry No:" + responseVO.getEnquiryNo(),
                "Data saved successfully. Enquiry No:" + responseVO.getEnquiryNo()));
        return null;
    }

    /**
     * Action method which is invoked upon next button click from Enquiry details form
     * @return
     */
    public String next() {
        //perform save operation first on click of next button
        save();
        //next check if quote slip mb is already available in session if so then invoke retrieveInsuredQuoteDetails method
        //on the bean
        QuoteSlipMB quoteSlipMB = (QuoteSlipMB)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AppConstants.BEAN_NAME_QUOTE_SLIP_PAGE);
        if(!Utils.isEmpty(quoteSlipMB)){
            quoteSlipMB.retrieveInsuredQuoteDetails();
        }
        
        return "quoteslip";
    }
}
