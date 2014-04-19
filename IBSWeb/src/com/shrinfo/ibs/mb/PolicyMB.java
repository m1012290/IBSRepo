package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

@ManagedBean(name = "policyMB")
@SessionScoped
public class PolicyMB extends BaseManagedBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2569790041665517181L;

    private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();

    private PolicyVO policyDetails = new PolicyVO();

    private InsuredVO insuredDetails = new InsuredVO();
    
    //This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields(){
        this.quoteDetailVO = new QuoteDetailVO();
        this.policyDetails = new PolicyVO();
        this.insuredDetails = new InsuredVO();
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
            policyDetailsOP =
                (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "createPolicy",
                    this.policyDetails);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(
                        "ERROR_QUOTATION_SAVE",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "Error saving Policy details."));
        }
        FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Policy Details Saved ", " successfully"));
        return null;
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
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quote Details : At least one quote has to be recommended",
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

        return "policy";

    }

}
