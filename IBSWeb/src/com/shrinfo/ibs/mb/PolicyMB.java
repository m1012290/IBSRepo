package com.shrinfo.ibs.mb;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
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

import org.primefaces.model.UploadedFile;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.DocumentListVO;
import com.shrinfo.ibs.vo.business.DocumentVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
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
    
    private Boolean screenFreeze = Boolean.FALSE;

    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.quoteDetailVO = new QuoteDetailVO();
        this.policyDetails = new PolicyVO();
        this.insuredDetails = new InsuredVO();
        this.screenFreeze = Boolean.FALSE;
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

    public PolicyVO getPolicyVO() {
        return policyDetails;
    }

    public void setPolicyVO(PolicyVO policyVO) {
        this.policyDetails = policyVO;
    }

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
    
    public void loadFile(UploadedFile file) {
        this.file = file;
    }
    
    
    public Boolean getScreenFreeze() {
        return screenFreeze;
    }

    
    public void setScreenFreeze(Boolean screenFreeze) {
        this.screenFreeze = screenFreeze;
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

            Map<InsCompanyVO, QuoteDetailVO> quoteDetailsMap =
                new HashMap<InsCompanyVO, QuoteDetailVO>();
            InsCompanyVO insComp = new InsCompanyVO();
            insComp.setCode(this.quoteDetailVO.getCompanyCode());
            quoteDetailsMap.put(insComp, this.quoteDetailVO);
            this.policyDetails.setQuoteDetails(quoteDetailsMap);

            this.policyDetails.setInsuredDetails(this.insuredDetails);
            DocumentListVO documentListVO = new DocumentListVO();
            List<DocumentVO> docVOList = new ArrayList<DocumentVO>();
            
            DocumentVO document = new DocumentVO();
            document.setEnquiry(this.policyDetails.getEnquiryDetails());
            // document.setDocSlipId(this.policyDetails.getPolicyId());
            document.setDocType("POLICY");
            document.setDocument(getFilaDataAsArray(this.file));            
            
            docVOList.add(document);
            documentListVO.setDocumentVOs(docVOList);
            this.policyDetails.setDocListUploaded(documentListVO);
            policyDetailsOP =
                (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "createPolicy",
                    this.policyDetails);
        } catch (Exception ex) {
            logger.error(ex, "Error saving Policy details");
            FacesContext.getCurrentInstance()
                    .addMessage(
                        "ERROR_QUOTATION_SAVE",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving policy details, please try again later",
                            "Error saving Policy details."));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Policy Details Captured Successfully ", " successfully"));
        return null;
    }

    /**
     * 
     * @throws IOException
     */
    public byte[] getFilaDataAsArray(UploadedFile file) throws IOException {

        if (null == file) {
            return null;
        }
        InputStream in = file.getInputstream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int totalRead = 0;
        if ((null != in) && (null != out)) {
            int bufferSize = 16;
            byte[] ba = new byte[1024 * bufferSize];
            int len = 0;

            while (-1 != (len = in.read(ba))) {
                out.write(ba, 0, len);
                totalRead += len;
            }

            close(in);
            close(out);
        }
        return out.toByteArray();

    }

    public boolean close(Closeable c) {
        if (null == c) {
            return true;
        }
        try {
            if (c instanceof Flushable) {
                ((Flushable) c).flush();
            }
        } catch (Exception e) {
            logger.warn(e, "couldn't close stream");
        }

        try {
            c.close();
        } catch (IOException e) {
            logger.warn(e, "couldn't close stream");
            return false;
        }
        return true;
    }


    public String loadQuotationDetails() {

        FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();
        
        QuotationMB quotation = (QuotationMB) map.get("quotationMB");
        QuoteSlipMB quoteSlipMB = (QuoteSlipMB) map.get("quoteSlipMB");
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
        EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
        LoginMB loginManageBean = (LoginMB) map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
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
