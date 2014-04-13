package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.helper.ReferralHelper;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.TaskVO;

@ManagedBean(name="quoteSlipMB")
@SessionScoped
public class QuoteSlipMB  extends BaseManagedBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2101774372606912675L;
	private static final Logger logger = Logger.getLogger(QuoteSlipMB.class);
	
	public QuoteSlipMB(){
		super();
		logger.info("In QuoteSlipMB Constructor");
	}

	private List<String> selectedInsCompanies = new ArrayList<String>();
	private Map<String,String> insCompanies = new HashMap<String, String>();
	private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();
	private InsuredVO insuredDetails=new InsuredVO();
	private PolicyVO policyVO = new PolicyVO();
	
	public Map<String, String> getInsCompanies() {
		return insCompanies;
	}
	public void setInsCompanies(Map<String, String> insCompanies) {
		this.insCompanies = insCompanies;
	}
	public List<String> getSelectedInsCompanies() {
		return selectedInsCompanies;
	}
	public void setSelectedInsCompanies(List<String> selectedInsCompanies) {
		this.selectedInsCompanies = selectedInsCompanies;
	}
	
	public QuoteDetailVO getQuoteDetailVO() {
		return quoteDetailVO;
	}
	public void setQuoteDetailVO(QuoteDetailVO quoteDetailVO) {
		this.quoteDetailVO = quoteDetailVO;
	}	
	public InsuredVO getInsuredDetails() {
		return insuredDetails;
	}
	public void setInsuredDetails(InsuredVO insuredDetails) {
		this.insuredDetails = insuredDetails;
	}
	

	public PolicyVO getPolicyVO() {
		return policyVO;
	}
	public void setPolicyVO(PolicyVO policyVO) {
		this.policyVO = policyVO;
	}
	public String save(){
		PolicyVO policyVO=new PolicyVO();
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			Map<String, String> requestMap = fc.getExternalContext().getRequestParameterMap();
			
			if(!Utils.isEmpty(requestMap)){
				ProductVO productDetails = this.quoteDetailVO.getProductDetails();
				for(ProductUWFieldVO uwField : productDetails.getUwFieldsList()){
					String response = requestMap.get(Utils.concat("field_",String.valueOf(uwField.getFieldOrder())));
					uwField.setResponse(response);
				}
			}
			
			Map map=fc.getExternalContext().getSessionMap();
			EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
			EnquiryVO enquiryDetails=editCustEnqDetailsMB.getEnquiryVO();
			policyVO.setPolicyNo(editCustEnqDetailsMB.getPolicyNum());//added
			this.insuredDetails.setCustomerDetails(enquiryDetails.getCustomerDetails());
			List<String> companiesList=null;
			policyVO.setInsuredDetails(insuredDetails);
			policyVO.setEnquiryDetails(enquiryDetails);
			companiesList=this.selectedInsCompanies;
			
			Map<InsCompanyVO, QuoteDetailVO> quoteDetMap=new HashMap<InsCompanyVO, QuoteDetailVO>();
			//later remove it
			this.quoteDetailVO.setStatusCode(1);
			this.quoteDetailVO.setCustomerId(enquiryDetails.getCustomerDetails().getCustomerId());
			
			Iterator<String> compItr=companiesList.iterator();
			String compCodes;
			InsCompanyVO insComp;
			while(compItr.hasNext()){
				compCodes=compItr.next();
				insComp=new InsCompanyVO();
				insComp.setCode(compCodes);
				insComp.setName(insCompanies.get(compCodes));
				quoteDetMap.put(insComp, this.quoteDetailVO);	
			}
			policyVO.setQuoteDetails(quoteDetMap);
			// Before performing save operation let's check if there are any referrals
			TaskVO taskVO = ReferralHelper.checkForReferrals(policyVO, SectionId.QUOTESLIP);
			if(!Utils.isEmpty(taskVO)){
			    setReferralDesc(taskVO.getDesc());
			    RequestContext context = RequestContext.getCurrentInstance();
		        if( context.isAjaxRequest() ){
		            context.addCallbackParam("referral", Boolean.TRUE);
		            return null;
		        }
			}
			policyVO=(PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","createQuoteSlip",policyVO);
			this.policyVO = policyVO;
			Set<Entry<InsCompanyVO, QuoteDetailVO>> quouEntries = policyVO.getQuoteDetails().entrySet();
	        Iterator<Entry<InsCompanyVO, QuoteDetailVO>> it = quouEntries.iterator();
			this.quoteDetailVO = it.next().getValue();					
					
		}catch(BusinessException be){
	    	logger.error(be, "Exception ["+ be +"] encountered while saving customer/enquiry details");
	    	FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unexpected error encountered", null));
	    	return null;
	    }catch(SystemException se){
	    	logger.error(se, "Exception ["+ se +"] encountered while saving customer/enquiry details");
	    	FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
	    	return null;
	    }catch(Exception ex){
	        logger.error(ex, "Exception ["+ ex +"] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
	    }
        FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS", new FacesMessage(FacesMessage.SEVERITY_INFO,"Insured Details ","Data saved successfully. Quote slip NO:"+quoteDetailVO.getQuoteSlipId()));

        return null;
	}
	
	public String next(){
		
		return "closeslip";
	}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	public String retrieveProductUWFields(javax.faces.event.AjaxBehaviorEvent event){
		ProductVO productVO = new ProductVO();
		productVO.setProductClass(this.quoteDetailVO.getProductDetails().getProductClass());
		//Product UW Fields retrieval through service call
		ProductVO svcResponseVO = MasterDataRetrievalUtil.getProductUWDetails(productVO);
		this.quoteDetailVO.setProductDetails(svcResponseVO);
		
		LookupVO lookupVO = new LookupVO();
		lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMPPRODUCTLINK);
		lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getProductClass()));
		LookupVO responseVO = MasterDataRetrievalUtil.getInsCompanies(lookupVO);
		this.insCompanies = responseVO.getCodeDescMap();
		
		return null;
	}
	
	public String retrieveInsuredQuoteDetails(){
		
		// Fetch editcustenqdetailsMB from Session in order to retain search results required
		// for loading screens
		try{
			FacesContext fc = FacesContext.getCurrentInstance();
			Map map=fc.getExternalContext().getSessionMap();
			EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
			if(!Utils.isEmpty(editCustEnqDetailsMB.getInsuredDetails().getId())){
				this.insuredDetails.setId(editCustEnqDetailsMB.getInsuredDetails().getId());
				
				//Retrieving insured details based on insured id
				this.insuredDetails =  (InsuredVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getInsuredDetails",this.insuredDetails);
				
				//Retrieve quote slip details basis quote slip id retained on enquiry form through search results on home page
				this.quoteDetailVO.setQuoteSlipId(editCustEnqDetailsMB.getQuoteSlipId());
				this.quoteDetailVO.setQuoteSlipVersion(Long.valueOf(1));
				PolicyVO policyVO  =  (PolicyVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getQuoteSlipDetails",this.quoteDetailVO);
				
				policyVO.setPolicyNo(editCustEnqDetailsMB.getPolicyNum());//added
				Map<InsCompanyVO, QuoteDetailVO>  mapOfQuoteDets = policyVO.getQuoteDetails();
				
				 Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
				 Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
				 InsCompanyVO insCompanyVO = null;
				 while(iterator.hasNext()){
					 insCompanyVO = iterator.next();
					 this.selectedInsCompanies.add(insCompanyVO.getCode());
				 }
				 this.quoteDetailVO = mapOfQuoteDets.get(insCompanyVO);
				 
				 // Populate insurane companies drop down also based on product class selected
				 LookupVO lookupVO = new LookupVO();
				 lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMPPRODUCTLINK);
				 lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getUwFieldsList().get(0).getProductClass()));
				 LookupVO responseVO = MasterDataRetrievalUtil.getInsCompanies(lookupVO);
				 this.insCompanies = responseVO.getCodeDescMap();
				 
				 this.policyVO = policyVO;
				 
	
				
			}
		}catch(Exception ex){
			FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error loading quote details, please try again after sometime"));
		}
		return "quoteslip";
	}
}
