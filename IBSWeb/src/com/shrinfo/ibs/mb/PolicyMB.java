package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import com.shrinfo.ibs.cmn.constants.CommonConstants;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.dao.utils.IOUtil;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.helper.ReferralHelper;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.DocumentListVO;
import com.shrinfo.ibs.vo.business.DocumentVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.StatusVO;
import com.shrinfo.ibs.vo.business.TaskVO;

@ManagedBean(name = "policyMB")
@SessionScoped
public class PolicyMB extends BaseManagedBean implements Serializable {


    Logger logger = Logger.getLogger(PolicyMB.class);

    /**
	 * 
	 */
    private static final long serialVersionUID = -2569790041665517181L;

    private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();

    private PolicyVO policyDetails = new PolicyVO();

    private InsuredVO insuredDetails = new InsuredVO();

    private UploadedFile file;
    
    private UploadedFile file2;
    
    private UploadedFile file3;
    
    private Boolean screenFreeze = Boolean.FALSE;

    private Boolean endorsementFlow = Boolean.FALSE;
    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.quoteDetailVO = new QuoteDetailVO();
        this.policyDetails = new PolicyVO();
        this.insuredDetails = new InsuredVO();
        this.screenFreeze = Boolean.FALSE;
        this.setSaveFromReferralDialog("false");
        this.screenFreeze = Boolean.FALSE;
        this.setEditApproved(Boolean.FALSE);
        this.setNavigationDisbled(Boolean.FALSE);
        this.setEndorsementFlow(Boolean.FALSE);
        this.setAppFlow(null);
    }
    
    public PolicyMB(){
        super();
        //invoke load quotation details method to retrieve existing quote details in case quote is an existing quote
        loadQuotationDetails();
    }
    
    public QuoteDetailVO getQuoteDetailVO() {
        return quoteDetailVO;
    }

    public void setQuoteDetailVO(QuoteDetailVO quoteDetailVO) {
        this.quoteDetailVO = quoteDetailVO;
    }

    public PolicyVO getPolicyDetails() {
		return policyDetails;
	}

	public void setPolicyDetails(PolicyVO policyDetails) {
		this.policyDetails = policyDetails;
	}

	/*
	public PolicyVO getPolicyVO() {
        return policyDetails;
    }

    public void setPolicyVO(PolicyVO policyVO) {
        this.policyDetails = policyVO;
    }*/

    public InsuredVO getInsuredDetails() {
        return insuredDetails;
    }

    public void setInsuredDetails(InsuredVO insuredDetails) {
        this.insuredDetails = insuredDetails;
    }


    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }


    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    
    /**
     * @return the file2
     */
    public UploadedFile getFile2() {
        return file2;
    }

    
    /**
     * @param file2 the file2 to set
     */
    public void setFile2(UploadedFile file2) {
        this.file2 = file2;
    }

    
    /**
     * @return the file3
     */
    public UploadedFile getFile3() {
        return file3;
    }

    
    /**
     * @param file3 the file3 to set
     */
    public void setFile3(UploadedFile file3) {
        this.file3 = file3;
    }
    
    
    public Boolean getScreenFreeze() {
        return screenFreeze;
    }

    
    public void setScreenFreeze(Boolean screenFreeze) {
        this.screenFreeze = screenFreeze;
    }

    public Boolean getEndorsementFlow() {
		return endorsementFlow;
	}

	public void setEndorsementFlow(Boolean endorsementFlow) {
		this.endorsementFlow = endorsementFlow;
	}

	public void calculatePremiumBasedOnPremiumChange(AjaxBehaviorEvent event) {

        BigDecimal premium = this.policyDetails.getPremiumDetails().getPremium();
        double premiumDiscount = this.policyDetails.getPremiumDetails().getDiscountPercentage();
        double premiumLoading = this.policyDetails.getPremiumDetails().getLoadingPercentage();
        if(0 == premiumDiscount && 0== premiumLoading) {
            this.policyDetails.getPremiumDetails().setTotalPremium(premium);
        }
        if (0 != premiumDiscount) {
            BigDecimal premiumDiscountValue =
                premium.multiply(BigDecimal.valueOf(premiumDiscount)).divide(
                    BigDecimal.valueOf(100));
            this.policyDetails.getPremiumDetails().setLoadingPercentage(0);
            this.policyDetails.getPremiumDetails().setTotalPremium(
                premium.subtract(premiumDiscountValue));
        }

        
        if (0 != premiumLoading) {
            this.policyDetails.getPremiumDetails().setDiscountPercentage(0);
            BigDecimal premiumDiscountValue =
                premium.multiply(BigDecimal.valueOf(premiumLoading))
                        .divide(BigDecimal.valueOf(100));
            this.policyDetails.getPremiumDetails().setTotalPremium(
                premium.add(premiumDiscountValue));
        }

    }


    public void calculatePremiumBasedOnDiscPercentage(AjaxBehaviorEvent event) {

        BigDecimal premium = this.policyDetails.getPremiumDetails().getPremium();
        double premiumDiscount = this.policyDetails.getPremiumDetails().getDiscountPercentage();
        if(0 != premiumDiscount) {
            BigDecimal premiumDiscountValue =
                    premium.multiply(BigDecimal.valueOf(premiumDiscount)).divide(BigDecimal.valueOf(100));
                this.policyDetails.getPremiumDetails().setLoadingPercentage(0);
                this.policyDetails.getPremiumDetails().setTotalPremium(
                    premium.subtract(premiumDiscountValue));
        }
        
        this.calculatePremiumBasedOnPremiumChange(event);

    }

    public void calculatePremiumBasedOnLoadPercentage(AjaxBehaviorEvent event) {

        BigDecimal premium = this.policyDetails.getPremiumDetails().getPremium();
        double premiumLoading = this.policyDetails.getPremiumDetails().getLoadingPercentage();
        if(0 != premiumLoading) {
            this.policyDetails.getPremiumDetails().setDiscountPercentage(0);
            BigDecimal premiumDiscountValue =
                premium.multiply(BigDecimal.valueOf(premiumLoading)).divide(BigDecimal.valueOf(100));
            this.policyDetails.getPremiumDetails().setTotalPremium(premium.add(premiumDiscountValue));
        }
        this.calculatePremiumBasedOnPremiumChange(event);

    }

    public String save() {
        PolicyVO policyDetailsOP = null;
        try {
            if(this.getPolicyDetails().getPolicyEffectiveDate().getTime() >= this.getPolicyDetails().getPolicyExpiryDate().getTime()) {
                FacesContext.getCurrentInstance()
                .addMessage(
                    "ERROR_POLICY_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Policy Expiry date should be greater than Effective date",
                        "Error saving Policy details."));
                return null;
            }
            // Before proceeding validate if this is referral approval flow
            FacesContext fc = FacesContext.getCurrentInstance();
            Map map=fc.getExternalContext().getSessionMap();        
            EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
            if(!Utils.isEmpty(editCustEnqDetailsMB.getAppFlow())){
                if(editCustEnqDetailsMB.getAppFlow().equals(AppFlow.REFERRAL_APPROVAL)){
                    this.policyDetails.setAppFlow(AppFlow.REFERRAL_APPROVAL);
                }
            }
            //this.policyDetails.getEnquiryDetails().setEnquiryNo(editCustEnqDetailsMB.getEnquiryVO().getEnquiryNo());
            this.policyDetails.setEnquiryDetails(editCustEnqDetailsMB.getEnquiryVO());
            Map<InsCompanyVO, QuoteDetailVO> quoteDetailsMap =
                new HashMap<InsCompanyVO, QuoteDetailVO>();
            InsCompanyVO insComp = new InsCompanyVO();
            insComp.setCode(this.quoteDetailVO.getCompanyCode());
            
            // Based on the flow set the status flag.
            if(!Utils.isEmpty(editCustEnqDetailsMB.getAppFlow())){
                if(editCustEnqDetailsMB.getAppFlow().equals(AppFlow.REFERRAL_APPROVAL)){
                	this.quoteDetailVO.setStatusCode(4);
                } else if(AppFlow.REFERRAL_APPROVED.equals(editCustEnqDetailsMB.getAppFlow())) {
                    // task is already approved. Hence set the status to active.
                	this.quoteDetailVO.setStatusCode(1);
                } else if(!Utils.isEmpty(this.getSaveFromReferralDialog()) && "true".endsWith(this.getSaveFromReferralDialog())) {
                	this.quoteDetailVO.setStatusCode(3);
                }
            }
            quoteDetailsMap.put(insComp, this.quoteDetailVO);
            this.policyDetails.setQuoteDetails(quoteDetailsMap);

            this.policyDetails.setInsuredDetails(this.insuredDetails);
            DocumentListVO documentListVO = new DocumentListVO();
            List<DocumentVO> docVOList = new ArrayList<DocumentVO>();
            
            //Document upload           
            if(!Utils.isEmpty(this.file)) {
                DocumentVO document = new DocumentVO();
                document.setEnquiry(this.policyDetails.getEnquiryDetails());
                document.setDocType("POLICY");
                document.setDocument(IOUtil.getFilaDataAsArray(this.file.getInputstream()));
                docVOList.add(document);
            }
            
            if(!Utils.isEmpty(this.file2)) {
                DocumentVO document2 = new DocumentVO();
                document2.setEnquiry(this.policyDetails.getEnquiryDetails());
                document2.setDocType("POLICY");
                document2.setDocument(IOUtil.getFilaDataAsArray(this.file2.getInputstream()));
                docVOList.add(document2);
            }
            
            if(!Utils.isEmpty(this.file3)) {
                DocumentVO document3 = new DocumentVO();
                document3.setEnquiry(this.policyDetails.getEnquiryDetails());
                document3.setDocType("POLICY");
                document3.setDocument(IOUtil.getFilaDataAsArray(this.file3.getInputstream()));
                docVOList.add(document3);
            }
            
            documentListVO.setDocumentVOs(docVOList);
            this.policyDetails.setDocListUploaded(documentListVO);
            // Before performing save operation let's check if there are any referrals
            //if(!Utils.isEmpty(this.getSaveFromReferralDialog()) && "true".equalsIgnoreCase(this.getSaveFromReferralDialog())){
            if((Utils.isEmpty(this.getSaveFromReferralDialog()) || "false".equalsIgnoreCase(this.getSaveFromReferralDialog())) 
                    && (!AppFlow.REFERRAL_APPROVED.equals(this.getAppFlow()) || this.getEditApproved())){
                TaskVO taskVO = ReferralHelper.checkForReferrals(this.policyDetails, SectionId.POLICY);
                if(!Utils.isEmpty(taskVO)){
                    this.setReferralDesc(taskVO.getDesc());
                    RequestContext context = RequestContext.getCurrentInstance();
                    if( context.isAjaxRequest() ){
                        context.addCallbackParam("referral", Boolean.TRUE);
                        return null;
                    }
                }
            }
            
            policyDetailsOP =
                (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "createPolicy",
                    this.policyDetails);
            FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Policy Details Captured Successfully ", " successfully"));
        } catch (Exception ex) {
            logger.error(ex, "Error saving Policy details");
            FacesContext.getCurrentInstance()
                    .addMessage(
                        "ERROR_POLICY_SAVE",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving policy details, please try again later",
                            "Error saving Policy details."));
        }
        
        return null;
    }
   
    @Override
    public String saveReferralTask() {
        //validate the referral window fields
        if(!validateReferralFields()){
            return null; //return as some field values are invalid on referral window.
        }
        this.setSaveFromReferralDialog("true");//highlight that save is getting invoked from referral dialog window
        this.save();//perform save operation first and then save the referral data

        Map map=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
        QuoteSlipMB quoteSlipMB = (QuoteSlipMB) map.get(AppConstants.BEAN_NAME_QUOTE_SLIP_PAGE);
        LoginMB loginMB = (LoginMB)map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
        //construct TaskVO to save referral desc
        TaskVO taskVO = new TaskVO();
        taskVO.setDesc(this.getReferralDesc());
        StatusVO statusVO = new StatusVO();
        statusVO.setCode(Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_REFERRED")));//referred status
        statusVO.setDesc("Referred");
        taskVO.setStatusVO(statusVO);
        taskVO.setEnquiry(editCustEnqDetailsMB.getEnquiryVO());
        taskVO.setDocumentId(String.valueOf(quoteSlipMB.getQuoteDetailVO().getQuoteSlipId()));
        taskVO.setAssignerUser(loginMB.getUserDetails());
        UserVO assigneeUser = new IBSUserVO();
        assigneeUser.setUserId(this.getAssigneeUser());
        taskVO.setAssigneeUser(assigneeUser);
        taskVO.setTaskType(Integer.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL")));
        taskVO.setTaskSectionType(Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_POLICY")));
        this.saveReferralTask(taskVO);//perform referral save task
        return super.saveReferralTask();
    }


    public String loadQuotationDetails() {

        FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();
        
        QuotationMB quotation = (QuotationMB) map.get(AppConstants.BEAN_NAME_CLOSING_SLIP_PAGE);
        QuoteSlipMB quoteSlipMB = (QuoteSlipMB) map.get(AppConstants.BEAN_NAME_QUOTE_SLIP_PAGE);
        EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
        LoginMB loginManageBean = (LoginMB) map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
        
        this.setEndorsementFlow(editCustEnqDetailsMB.getEnquiryVO().getType().getEnquiryType().equals(CommonConstants.ENQUIRY_TYPE_ENDORSEMENT)?Boolean.TRUE:Boolean.FALSE);
        this.policyDetails = quotation.getPolicyDetails();
        
        if(editCustEnqDetailsMB.getEnquiryVO().getType().getEnquiryType().equals(CommonConstants.ENQUIRY_TYPE_ENDORSEMENT)){
        	//this is in case of new endorsement where in we want to create new record in UW_TXN_HEADER
        	// and UW_TXN_DETAILS table. Hence we ensure that policy id available
        	this.policyDetails.setPolicyId(null);
        }
        this.quoteDetailVO = quotation.getQuoteDetailVOClosed();
        this.quoteDetailVO.setQuoteSlipDate(quoteSlipMB.getQuoteDetailVO().getQuoteSlipDate());
        this.insuredDetails = quotation.getInsuredDetails();
        if (Utils.isEmpty(this.quoteDetailVO.getQuoteId())) {
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_QUOTATION_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Quote Details : At least one quote has to be recommended",
                    "Quote Details : At least one quote has to be recommended"));
            return null;
        }
        this.policyDetails.setQuoteId(this.quoteDetailVO.getQuoteId());
        this.policyDetails.setInsuredDetails(insuredDetails);
        PolicyVO policyVO =
            (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "getPolicy",
                this.policyDetails);

        if (!Utils.isEmpty(policyVO)) {
            this.policyDetails = policyVO;
        }
              
        // Referral
        int policySectioncode = Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_POLICY"));
        
        this.setAssignerUser(loginManageBean.getUserDetails().getUserName());
        UserVO loggedInUser = loginManageBean.getUserDetails();
        
        // Check if there is a pending referral for any of the section/transaction for this enquiry number.
        this.setTaskVO(new TaskVO());
        this.getTaskVO().setEnquiry(editCustEnqDetailsMB.getEnquiryVO());
        this.setTaskVO(this.checkReferral(this.getTaskVO()));        
        
        this.setNavigationDisbled(Boolean.FALSE);
        this.screenFreeze = Boolean.FALSE;
        this.setEditVisible(Boolean.FALSE);
        this.setAppFlow(null);
        
        if(!Utils.isEmpty(this.getTaskVO()) /*&& 2 == this.getTaskVO().getTaskSectionType()*/) {
            // Screen will be freezed if there is a pending referral not assigned to logged-in user . 
            // This referral may be for current screen or the next screens. 
            if (policySectioncode <= this.getTaskVO().getTaskSectionType() && 3 == this.getTaskVO().getStatusVO().getCode()
                && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.screenFreeze = Boolean.TRUE;
            }
            if(3 == this.getTaskVO().getStatusVO().getCode() && policySectioncode == this.getTaskVO().getTaskSectionType()) {
                this.setAppFlow(AppFlow.REFERRAL_APPROVAL);
            }
            
            if(3 != this.getTaskVO().getStatusVO().getCode() && policySectioncode == this.getTaskVO().getTaskSectionType()) {
                this.setAppFlow(AppFlow.REFERRAL_APPROVED);
            }
        }
        
        // Check if there is a pending/Approval referral for current section/transaction for this enquiry number.
        this.setTaskVO(new TaskVO());
        this.getTaskVO().setEnquiry(editCustEnqDetailsMB.getEnquiryVO());
        this.getTaskVO().setTaskSectionType(policySectioncode);
        this.getTaskVO().setTaskType(Integer.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL")));
        this.setTaskVO(this.checkReferral(this.getTaskVO()));
        if(!Utils.isEmpty(this.getTaskVO())) {
            // Screen will be freezed if there is a approved referral not assigned to logged-in user for current screen. 
            if (policySectioncode == this.getTaskVO().getTaskSectionType()
                && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.screenFreeze = Boolean.TRUE;                        
            }
            // navigation will be disabled only if referral is pending for the current screen and is not assigned to logged in user
            if(3 == this.getTaskVO().getStatusVO().getCode() && policySectioncode == this.getTaskVO().getTaskSectionType()
               && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.setNavigationDisbled(Boolean.TRUE);
            }
            // Edit button will be visible only if current screen task is approved since screen will be freezed.
            if(3 != this.getTaskVO().getStatusVO().getCode() && policySectioncode == this.getTaskVO().getTaskSectionType()
               && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.setEditVisible(Boolean.TRUE);
            }
        }

        return "policy";

    }

    public String back() {
        return "closeslip";
    }
}