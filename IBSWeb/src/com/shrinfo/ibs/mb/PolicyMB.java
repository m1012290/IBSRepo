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
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
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
        if (0 != premiumDiscount) {
            BigDecimal premiumDiscountValue =
                premium.multiply(BigDecimal.valueOf(premiumDiscount)).divide(
                    BigDecimal.valueOf(100));
            this.policyDetails.getPremiumDetails().setLoadingPercentage(0);
            this.policyDetails.getPremiumDetails().setTotalPremium(
                premium.subtract(premiumDiscountValue));
        }

        double premiumLoading = this.policyDetails.getPremiumDetails().getLoadingPercentage();
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
        BigDecimal premiumDiscountValue =
            premium.multiply(BigDecimal.valueOf(premiumDiscount)).divide(BigDecimal.valueOf(100));
        this.policyDetails.getPremiumDetails().setLoadingPercentage(0);
        this.policyDetails.getPremiumDetails().setTotalPremium(
            premium.subtract(premiumDiscountValue));

    }

    public void calculatePremiumBasedOnLoadPercentage(AjaxBehaviorEvent event) {

        BigDecimal premium = this.policyDetails.getPremiumDetails().getPremium();
        double premiumLoading = this.policyDetails.getPremiumDetails().getLoadingPercentage();
        this.policyDetails.getPremiumDetails().setDiscountPercentage(0);
        BigDecimal premiumDiscountValue =
            premium.multiply(BigDecimal.valueOf(premiumLoading)).divide(BigDecimal.valueOf(100));
        this.policyDetails.getPremiumDetails().setTotalPremium(premium.add(premiumDiscountValue));

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
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "Error saving Policy details."));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Policy Details Saved ", " successfully"));
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

        QuotationMB quotation =
            (QuotationMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get("quotationMB");
        this.quoteDetailVO = quotation.getQuoteDetailVOClosed();
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
        
        // referral
        FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();        
        // Referral
        EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
        TaskVO taskVO = new TaskVO();               
        taskVO.setEnquiry(editCustEnqDetailsMB.getEnquiryVO());
        LoginMB loginManageBean = (LoginMB) map.get("loginBean");
        taskVO = this.checkReferral(loginManageBean.getUserDetails(), taskVO, 3);
        if(!Utils.isEmpty(taskVO)) {
            this.screenFreeze = Boolean.TRUE;         
        }

        return "policy";

    }

}
