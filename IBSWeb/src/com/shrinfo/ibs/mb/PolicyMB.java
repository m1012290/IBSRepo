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

import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
@ManagedBean(name="policyMB")
@SessionScoped
public class PolicyMB extends BaseManagedBean implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2569790041665517181L;
	private QuoteDetailVO quoteDetailVO=new QuoteDetailVO();
	private PolicyVO policyDetails=new PolicyVO();
	private InsuredVO insuredDetails=new InsuredVO();
	
	
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

	
	public void calculatePremiumBasedOnDiscPercentage(AjaxBehaviorEvent event){
		
		BigDecimal premium=this.policyDetails.getPremiumDetails().getPremium();
		double premiumDiscount=this.policyDetails.getPremiumDetails().getDiscountPercentage();
		BigDecimal premiumDiscountValue=premium.multiply(BigDecimal.valueOf(premiumDiscount)).divide(BigDecimal.valueOf(100));
		this.policyDetails.getPremiumDetails().setTotalPremium(premium.subtract(premiumDiscountValue));	
		
	}
	
	public void calculatePremiumBasedOnLoadPercentage(AjaxBehaviorEvent event){
		
		BigDecimal premium=this.policyDetails.getPremiumDetails().getPremium();
		double premiumDiscount=this.policyDetails.getPremiumDetails().getLoadingPercentage();
		BigDecimal premiumDiscountValue=premium.multiply(BigDecimal.valueOf(premiumDiscount)).divide(BigDecimal.valueOf(100));
		this.policyDetails.getPremiumDetails().setTotalPremium(premium.add(premiumDiscountValue));	
		
	}
	public String save(){
		PolicyVO policyDetailsOP=null;
		try {
			QuotationMB quotation = (QuotationMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("quotationMB");
			this.quoteDetailVO=quotation.getQuoteDetailVO();
			this.insuredDetails=quotation.getInsuredDetails();
			this.quoteDetailVO.setStatusCode(1);
			
			Map<InsCompanyVO, QuoteDetailVO> quoteDetailsMap=new HashMap<InsCompanyVO, QuoteDetailVO>();
			InsCompanyVO insComp=new InsCompanyVO();
			insComp.setName(this.quoteDetailVO.getCompanyCode());
			quoteDetailsMap.put(insComp, this.quoteDetailVO);
			this.policyDetails.setQuoteDetails(quoteDetailsMap);
			
			this.policyDetails.setInsuredDetails(this.insuredDetails);
			policyDetailsOP=(PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc","createPolicy",this.policyDetails);
		} catch(Exception ex){
			//FacesContext.getCurrentInstance().addMessage("ERROR_QUOTATION_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error saving Policy details, please try again after sometime"));
		    ex.printStackTrace();
		}
			FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS", new FacesMessage(FacesMessage.SEVERITY_INFO,"Policy Details Saved "," successfully"));
			return null;
		}

	
	public String loadQuotationDetails(){
		
		QuotationMB quotation = (QuotationMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("quotationMB");
		this.quoteDetailVO=quotation.getQuoteDetailVO();
		this.insuredDetails=quotation.getInsuredDetails();
		
		if(null!=quotation.getPolicyDetails().getPolicyNo()){
			this.policyDetails.setPolicyId(Long.parseLong(quotation.getPolicyDetails().getPolicyNo()));
			this.policyDetails.setPolicyVersion(1);
			this.policyDetails=(PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc","getPolicy",this.policyDetails);
			this.policyDetails.getPolicyNo();
			this.policyDetails.getPolicyId();
			this.policyDetails.getPremiumDetails();


		}
		this.policyDetails.setInsuredDetails(insuredDetails);
		//this.policyDetails.setPolicyId(thi);
		
		
	
	
		return "policy";

	}
	
	

}
