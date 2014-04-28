package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import com.shrinfo.ibs.docgen.QuoteSlipPDFGenerator;
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

		//invoke load quoteslip details method in order to retrieve existing quote slip
		//details if required (existing quote record)
		this.retrieveInsuredQuoteDetails();
	}

	private List<String> selectedInsCompanies = new ArrayList<String>();
	private Map<String,String> insCompanies = new HashMap<String, String>();
	private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();
	private InsuredVO insuredDetails=new InsuredVO();
	private PolicyVO policyVO = new PolicyVO();
	private Boolean renderCustomUWComponent = Boolean.FALSE;
	private Boolean screenFreeze = Boolean.FALSE;

	//This is an important method which is overriden from parent managed bean
	// this is an reinitializer block which includes all the instance fields which are bound to form
	// this method is necessary as managed beans are defined as sessionscoped beans
	@Override
	protected void reinitializeBeanFields(){
		this.selectedInsCompanies = new ArrayList<String>();
		this.insCompanies = new HashMap<String, String>();
		this.quoteDetailVO = new QuoteDetailVO();
		this.insuredDetails=new InsuredVO();
		this.policyVO = new PolicyVO();
		this.renderCustomUWComponent = Boolean.FALSE;
		this.screenFreeze = Boolean.FALSE;
	}

	public Map<String, String> getInsCompanies() {
		return this.insCompanies;
	}
	public void setInsCompanies(Map<String, String> insCompanies) {
		this.insCompanies = insCompanies;
	}
	public List<String> getSelectedInsCompanies() {
		return this.selectedInsCompanies;
	}
	public void setSelectedInsCompanies(List<String> selectedInsCompanies) {
		this.selectedInsCompanies = selectedInsCompanies;
	}

	public QuoteDetailVO getQuoteDetailVO() {
		return this.quoteDetailVO;
	}
	public void setQuoteDetailVO(QuoteDetailVO quoteDetailVO) {
		this.quoteDetailVO = quoteDetailVO;
	}
	public InsuredVO getInsuredDetails() {
		return this.insuredDetails;
	}
	public void setInsuredDetails(InsuredVO insuredDetails) {
		this.insuredDetails = insuredDetails;
	}


	public PolicyVO getPolicyVO() {
		return this.policyVO;
	}
	public void setPolicyVO(PolicyVO policyVO) {
		this.policyVO = policyVO;
	}

	public boolean isRenderCustomUWComponent() {
		return this.renderCustomUWComponent;
	}


	public void setRenderCustomUWComponent(boolean renderCustomUWComponent) {
		this.renderCustomUWComponent = renderCustomUWComponent;
	}

	
    public Boolean getScreenFreeze() {
        return screenFreeze;
    }

    
    public void setScreenFreeze(Boolean screenFreeze) {
        this.screenFreeze = screenFreeze;
    }

    public String save(){
		PolicyVO policyVO=new PolicyVO();

		try {
			FacesContext fc = FacesContext.getCurrentInstance();

			Map<String, String> requestMap = fc.getExternalContext().getRequestParameterMap();

			if(!Utils.isEmpty(requestMap)){
				ProductVO productDetails = this.quoteDetailVO.getProductDetails();
				boolean allUWFieldValid = true;
				for(ProductUWFieldVO uwField : productDetails.getUwFieldsList()){
					String response = requestMap.get(this.getComponentClientId(uwField));
					uwField.setResponse(response);
					//validate underwriting answers in order to check if answer formats are in 
					//accordance to values defined in the table
					if(validateUWFieldResponseIsEmpty(uwField)){
					    allUWFieldValid = false;
					    
					    continue;//continue with next field value is empty though the field is defined as mandatory
					}
					
					if(uwField.getFieldValueType().equalsIgnoreCase(AppConstants.UW_FIELD_VALUE_TYPE_NUMERIC)){
					    if(!validateUWFieldIsNumeric(uwField)){
					        allUWFieldValid = false;
					        FacesContext.getCurrentInstance().addMessage(
				                "",
				                new FacesMessage(FacesMessage.SEVERITY_ERROR, Utils.concat("Please enter valid numeric value for ", uwField.getFieldName()),
				                    Utils.concat("Please enter value for ", uwField.getFieldName())));
					        continue;//invalid value type entered for the field
					    }
					}
					
					if(uwField.getFieldValueType().equalsIgnoreCase(AppConstants.UW_FIELD_VALUE_TYPE_DATE)){
					    if(!validateUWFieldIsDate(uwField)){
					        allUWFieldValid = false;
					        FacesContext.getCurrentInstance().addMessage(
				                "",
				                new FacesMessage(FacesMessage.SEVERITY_ERROR, Utils.concat("Please enter valid date for ", uwField.getFieldName()),
				                    Utils.concat("Please enter value for ", uwField.getFieldName())));
					        continue;//invalid date format for the field
					    }
					}
				}
				//recheck again if uw fields validations are through
				if(!allUWFieldValid){
                    return null;
                }
			}

			Map map=fc.getExternalContext().getSessionMap();
			EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
			EnquiryVO enquiryDetails=editCustEnqDetailsMB.getEnquiryVO();
			policyVO.setPolicyNo(editCustEnqDetailsMB.getPolicyNum());//added
			this.insuredDetails.setCustomerDetails(enquiryDetails.getCustomerDetails());
			List<String> companiesList=null;
			policyVO.setInsuredDetails(this.insuredDetails);
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
				insComp.setName(this.insCompanies.get(compCodes));
				quoteDetMap.put(insComp, this.quoteDetailVO);
			}
			policyVO.setQuoteDetails(quoteDetMap);
			// Before performing save operation let's check if there are any referrals
			TaskVO taskVO = ReferralHelper.checkForReferrals(policyVO, SectionId.QUOTESLIP);
			if(!Utils.isEmpty(taskVO)){
				this.setReferralDesc(taskVO.getDesc());
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
		FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS", new FacesMessage(FacesMessage.SEVERITY_INFO,"Insured Details: Data saved successfully. Quote slip NO:"+this.quoteDetailVO.getQuoteSlipId(),"Data saved successfully. Quote slip NO:"+this.quoteDetailVO.getQuoteSlipId()));

		return "SUCCESS";
	}

	/**
	 * 
	 * @return
	 */
	public String next(){

		//perform save operation first on click of next button
		String response = this.save();
		if(Utils.isEmpty(response)){//some issue with save hence breaking it here
		    return null;
		}
		//next check if quote slip mb is already available in session if so then invoke retrieveInsuredQuoteDetails method
		//on the bean
		QuotationMB quoteMB = (QuotationMB)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AppConstants.BEAN_NAME_CLOSING_SLIP_PAGE);
		if(!Utils.isEmpty(quoteMB)){
			quoteMB.loadQuotationsDetail();
		}
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
		//enable rendering of custom underwriting component
		this.renderCustomUWComponent = Boolean.TRUE;

		return null;
	}

	/**
	 * This method loads insured plus quote details while loading quote slip details
	 * screen
	 * @return
	 */
	public String retrieveInsuredQuoteDetails(){

		// Fetch editcustenqdetailsMB from Session in order to retain search results required
		// for loading screens
		try{
			FacesContext fc = FacesContext.getCurrentInstance();
			Map map=fc.getExternalContext().getSessionMap();
			EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
			if(!Utils.isEmpty(editCustEnqDetailsMB.getQuoteSlipId())){
				this.renderCustomUWComponent = true;
				this.insuredDetails.setId(editCustEnqDetailsMB.getInsuredDetails().getId());

				//Retrieve quote slip details basis quote slip id retained on enquiry form through search results on home page
				this.quoteDetailVO.setQuoteSlipId(editCustEnqDetailsMB.getQuoteSlipId());
				this.quoteDetailVO.setQuoteSlipVersion(Long.valueOf(1));
				PolicyVO policyVO  =  (PolicyVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getQuoteSlipDetails",this.quoteDetailVO);

				this.insuredDetails.setId(policyVO.getInsuredDetails().getId());
				//Retrieving insured details based on insured id
				this.insuredDetails =  (InsuredVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getInsuredDetails",this.insuredDetails);

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
				
				// Referral
				TaskVO taskVO = new TaskVO();				
				taskVO.setEnquiry(policyVO.getEnquiryDetails());
				LoginMB loginManageBean = (LoginMB) map.get("loginBean");
				taskVO = this.checkReferral(loginManageBean.getUserDetails(), taskVO, 3);
				if(!Utils.isEmpty(taskVO)) {
				    this.screenFreeze = Boolean.TRUE;		    
				}

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
	public String generatePDFForQuoteSlip(){

		try {
			QuoteSlipPDFGenerator quoteSlipPDFGenerator=new QuoteSlipPDFGenerator();
			Map<InsCompanyVO, QuoteDetailVO>  mapOfQuoteDets = this.policyVO.getQuoteDetails();

			Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
			Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
			InsCompanyVO insCompanyVO = null;
			while(iterator.hasNext()){
				insCompanyVO = iterator.next();
				//this.selectedInsCompanies.add(insCompanyVO.getCode());
				insCompanyVO =  (InsCompanyVO)ServiceTaskExecutor.INSTANCE.executeSvc("companySvc","getPolicy",insCompanyVO);
				quoteSlipPDFGenerator.generatePDF(this.quoteDetailVO, this.insuredDetails, insCompanyVO.getContactAndAddrDetails(),insCompanyVO.getName(), Utils.getSingleValueAppConfig("quoteSlipfilePath")+"_"+new Date().getTime(), Utils.getSingleValueAppConfig("imagePath"));

			}

			FacesContext.getCurrentInstance().addMessage("SUCCESS_EMAIL_MSG", new FacesMessage(FacesMessage.SEVERITY_INFO,"Quoteslipdoc is successfully emailed to the selected insurance companies",null));

			/*Iterator<String> companyItr=selectedInsCompanies.iterator();
			while(companyItr.hasNext()){
				String insComp=companyItr.next();
				quoteSlipPDFGenerator.generatePDF(this.quoteDetailVO, this.insuredDetails, insCompanyVO.getContactAndAddrDetails(),insComp, Utils.getSingleValueAppConfig("quoteSlipfilePath")+"_"+new Date().getTime(), Utils.getSingleValueAppConfig("imagePath"));
			}*/

		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error generating quote details document, see the error log"));

		}

		return null;
	}

}
