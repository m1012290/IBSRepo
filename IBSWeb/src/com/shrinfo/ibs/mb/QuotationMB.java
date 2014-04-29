/**
 * 
 */
package com.shrinfo.ibs.mb;

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
import org.primefaces.event.CloseEvent;
import org.primefaces.event.RowEditEvent;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.docgen.QuoteSlipPDFGenerator;
import com.shrinfo.ibs.helper.ReferralHelper;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.PremiumVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.StatusVO;
import com.shrinfo.ibs.vo.business.TaskVO;

/**
 * @author rahmahaf
 * 
 */
@ManagedBean(name = "quotationMB")
@SessionScoped
public class QuotationMB extends BaseManagedBean implements java.io.Serializable {

	// private ProductVO productVO = new ProductVO();
	private List<String> selectedInsCompanies = new ArrayList<String>();

	private Map<String, String> insCompanies = new HashMap<String, String>();

	private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();

	private List<ProductUWFieldVO> slipUwList = new ArrayList<ProductUWFieldVO>();

	private QuoteDetailVO selectedQuoteDetailVO = new QuoteDetailVO();

	private ProductVO quoteProductDetails = new ProductVO();

	private QuoteDetailVO quoteDetailVOClosed = new QuoteDetailVO();

	private InsuredVO insuredDetails = new InsuredVO();

	private List<QuoteDetailVO> quoteDetailList = new ArrayList<QuoteDetailVO>();

	private PolicyVO policyDetails = new PolicyVO();

	private QuoteDetailVODataModel quoteDetailVODataModel;

	private QuoteDetailVO quoteDetSelection = new QuoteDetailVO();

	private List<QuoteDetailVO> quoteDetailListClosed = new ArrayList<QuoteDetailVO>();

	private int recommendedFlagcnt = 0;
	
	private Boolean screenFreeze = Boolean.FALSE;
	
		
    public Boolean getScreenFreeze() {
        return screenFreeze;
    }
    
    public void setScreenFreeze(Boolean screenFreeze) {
        this.screenFreeze = screenFreeze;
    }
    public QuotationMB(){
		super();
		//invoke loadQuotationsDetail method to retrieve quotation details in case of existing
		//quote
		this.loadQuotationsDetail();
	}
	//This is an important method which is overriden from parent managed bean
	// this is an reinitializer block which includes all the instance fields which are bound to form
	// this method is necessary as managed beans are defined as sessionscoped beans
	@Override
	protected void reinitializeBeanFields(){
		this.selectedInsCompanies = new ArrayList<String>();
		this.insCompanies = new HashMap<String, String>();
		this.quoteDetailVO = new QuoteDetailVO();
		this.selectedQuoteDetailVO = new QuoteDetailVO();
		this.quoteProductDetails = new ProductVO();
		this.quoteDetailVOClosed = new QuoteDetailVO();
		this.insuredDetails = new InsuredVO();
		this.quoteDetailList = new ArrayList<QuoteDetailVO>();
		this.policyDetails = new PolicyVO();

		this.quoteDetailVODataModel = null;
		this.quoteDetSelection = new QuoteDetailVO();
		this.quoteDetailListClosed = new ArrayList<QuoteDetailVO>();
		this.setSaveFromReferralDialog("false");
		this.screenFreeze = Boolean.FALSE;
	}

	/**
	 * @return the quoteDetSelection
	 */
	public QuoteDetailVO getQuoteDetSelection() {
		return this.quoteDetSelection;
	}


	/**
	 * @param quoteDetSelection the quoteDetSelection to set
	 */
	public void setQuoteDetSelection(QuoteDetailVO quoteDetSelection) {
		this.quoteDetSelection = quoteDetSelection;
	}
	public PolicyVO getPolicyDetails() {
		return this.policyDetails;
	}

	public void setPolicyDetails(PolicyVO policyDetails) {
		this.policyDetails = policyDetails;
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



	public QuoteDetailVO getSelectedQuoteDetailVO() {
		return this.selectedQuoteDetailVO;
	}


	public void setSelectedQuoteDetailVO(QuoteDetailVO selectedQuoteDetailVO) {
		this.selectedQuoteDetailVO = selectedQuoteDetailVO;
	}


	public ProductVO getQuoteProductDetails() {
		return this.quoteProductDetails;
	}


	public void setQuoteProductDetails(ProductVO quoteProductDetails) {
		this.quoteProductDetails = quoteProductDetails;
	}

	public QuoteDetailVO getQuoteDetailVOClosed() {
		return this.quoteDetailVOClosed;
	}


	public void setQuoteDetailVOClosed(QuoteDetailVO quoteDetailVOClosed) {
		this.quoteDetailVOClosed = quoteDetailVOClosed;
	}

	public InsuredVO getInsuredDetails() {
		return this.insuredDetails;
	}

	public void setInsuredDetails(InsuredVO insuredDetails) {
		this.insuredDetails = insuredDetails;
	}

	public List<QuoteDetailVO> getQuoteDetailList() {
		return this.quoteDetailList;
	}

	public void setQuoteDetailList(List<QuoteDetailVO> quoteDetailList) {
		this.quoteDetailList = quoteDetailList;
	}

	/**
	 * This adds a new quote into UI table. Here data which is edited in
	 * the table itself will not be captured.


	 * 
	 * @return
	 */
	public String addAction() {
		if(this.quoteDetailList.contains(this.quoteDetailVO)){
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selected company record is already added",
							"Selected company record is already added"));
			return null;
		}

		/**
		 * Validation for quotation details being added
		 */
		boolean fieldsValid = true;
		if(Utils.isEmpty(this.quoteDetailVO.getCompanyCode())) {
			fieldsValid = false;
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please choose company name",
							"Please choose company name"));
		}
		if(Utils.isEmpty(this.quoteDetailVO.getQuoteNo())) {
			fieldsValid = false;
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid quotation number",
							"Please enter valid quotation number"));
		}
		if(!Utils.isNonZeroNumber(this.quoteDetailVO.getQuoteSlipPrmDetails().getPremium().toString())) {
			fieldsValid = false;
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid quoted premium amount",
							"Please enter valid quoted premium amount"));
		}
		if(!Utils.isNonZeroNumber(this.quoteDetailVO.getSumInsured().toString())) {
			fieldsValid = false;
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid sum insured",
							"Please enter valid sum insured"));
		}
		if(!Utils.isNonZeroNumber(this.quoteDetailVO.getPolicyTerm().toString())) {
			fieldsValid = false;
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid Policy term",
							"Please enter valid Policy term"));
		}
		if(Utils.isEmpty(this.quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription())) {
			fieldsValid = false;
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid cover description",
							"Please enter valid cover description"));
		}
		if(!fieldsValid) {
			return null;
		}


		QuoteDetailVO temp = this.getQuoteDetailTableData(this.quoteDetailVO);
		ProductVO productVO = this.getProductFieldVOTableData(this.quoteDetailVO, "quoteAdding");
		if(Utils.isEmpty(productVO)){
		    return null; //there must be some validation failure of underwriting fields hence breaking it here
		}
		temp.setProductDetails(productVO);
		this.quoteDetailList.add(temp);
		this.quoteDetailVODataModel = new QuoteDetailVODataModel(this.getQuoteDetailList());

		this.quoteDetailVO.getProductDetails().setUwFieldsList(this.slipUwList);
		return null;
	}

	/**
	 * Constructs a new quote detail object containing data present in UI quotation table only
	 * 
	 * @param quoteDetailVO
	 * @return
	 */
	private QuoteDetailVO getQuoteDetailTableData(QuoteDetailVO quoteDetailVO) {
		QuoteDetailVO detailVO = new QuoteDetailVO();
		detailVO.setQuoteNo(quoteDetailVO.getQuoteNo());
		detailVO.setCompanyCode(quoteDetailVO.getCompanyCode());
		detailVO.setQuoteSlipPrmDetails(quoteDetailVO.getQuoteSlipPrmDetails());
		detailVO.setSumInsured(quoteDetailVO.getSumInsured());
		detailVO.setPolicyTerm(quoteDetailVO.getPolicyTerm());
		detailVO.setRecommendationSummary(quoteDetailVO.getRecommendationSummary());
		detailVO.setIsQuoteRecommended(quoteDetailVO.getIsQuoteRecommended());
		PremiumVO premiumVO = new PremiumVO();
		premiumVO.setPremium(quoteDetailVO.getQuoteSlipPrmDetails().getPremium());
		premiumVO.setCoverDescription(quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription());
		detailVO.setQuoteSlipPrmDetails(premiumVO);

		ProductVO productVO = new ProductVO();
		productVO.setProductClass(quoteDetailVO.getProductDetails().getProductClass());
		List<ProductUWFieldVO> uwFieldList = new ArrayList<ProductUWFieldVO>();
		for(ProductUWFieldVO uwField : quoteDetailVO.getProductDetails().getUwFieldsList()){
			uwFieldList.add(this.getProductUwFieldVOClone(uwField));
		}

		productVO.setUwFieldsList(uwFieldList);
		detailVO.setProductDetails(productVO);

		return detailVO;
	}

	private ProductVO getProductFieldVOTableData(QuoteDetailVO quoteDetailVO, String prefix){

		// populate UW Field details
		ProductVO productVO = new ProductVO();
		productVO.setProductClass(quoteDetailVO.getProductDetails().getProductClass());


		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> requestMap = fc.getExternalContext().getRequestParameterMap();

		List<ProductUWFieldVO> uwFieldList = new ArrayList<ProductUWFieldVO>();
		boolean allUWFieldValid = true;
		if (!Utils.isEmpty(requestMap)) {
			String response = null;
			for (ProductUWFieldVO uwField : quoteDetailVO.getProductDetails()
					.getUwFieldsList()) {
				response = requestMap.get(this.getComponentClientId(uwField, prefix + "_"));
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
				uwFieldList.add(this.getProductUwFieldVOClone(uwField));
			}
		}
	    if(!allUWFieldValid){
            return null;
        }
		
		productVO.setUwFieldsList(uwFieldList);

		return productVO;
	}

	private ProductUWFieldVO getProductUwFieldVOClone(ProductUWFieldVO fieldVO){
		ProductUWFieldVO clone = new ProductUWFieldVO();
		clone.setFieldName(fieldVO.getFieldName());
		clone.setFieldOrder(fieldVO.getFieldOrder());
		clone.setFieldType(fieldVO.getFieldType());
		clone.setFieldValue(fieldVO.getFieldValue());
		clone.setIsMandatory(fieldVO.getIsMandatory());
		clone.setIsStatusActive(fieldVO.getIsStatusActive());
		clone.setProductClass(fieldVO.getProductClass());
		clone.setResponse(fieldVO.getResponse());
		clone.setUwFieldId(fieldVO.getUwFieldId());
		clone.setFieldLength(fieldVO.getFieldLength());
		clone.setFieldValueType(fieldVO.getFieldValueType());
		return clone;



	}



	/**
	 * This is to take care of saving latest edited data from the table
	 * 
	 * @param event
	 */
	public void onEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Item Edited");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * 
	 * @param event
	 */
	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Item Cancelled");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		this.quoteDetailList.remove(event.getObject());
	}

	public void handleClose(CloseEvent event) {
		// Panel Component is retrieved from closeevent which was fired when a grid
		// is being removed i.e. one of the quote details pertaining to a company
		// is removed...
		String companyCode = (String) event.getComponent().getAttributes().get("header");
		QuoteDetailVO tempQuoteDetailVO = new QuoteDetailVO();
		tempQuoteDetailVO.setCompanyCode(companyCode);
		this.quoteDetailList.remove(tempQuoteDetailVO);
		this.quoteDetailListClosed = new ArrayList<QuoteDetailVO>();
		for (QuoteDetailVO detailVO : this.quoteDetailList) {
			QuoteDetailVO temp = this.getQuoteDetailTableData(detailVO);
			this.quoteDetailListClosed.add(temp);
		}

		/*
		 * Iterator<QuoteDetailVO> it = this.quoteDetailList.iterator(); while (it.hasNext()) { if
		 * (it.next().getCompanyCode().endsWith(companyCode)) { it.remove(); break; } }
		 */
	}

	public String save() {
		if (!Utils.isEmpty(this.quoteDetailListClosed)) {
			this.quoteDetailList = this.quoteDetailListClosed;
		}
		// At least one quote detail data should be added to save quote details
		if (Utils.isEmpty(this.quoteDetailList)) {
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quote Details save: At least one quote should be added",
							" At least one quote should be added"));
			return null;
		}
		PolicyVO policyDetails = new PolicyVO();
		try {
			Map<InsCompanyVO, QuoteDetailVO> addedQuotes =
					new HashMap<InsCompanyVO, QuoteDetailVO>();

			this.recommendedFlagcnt = 0;
			boolean validResponseForUWFields = true;
			for (Entry<InsCompanyVO, QuoteDetailVO> entry : this.policyDetails.getQuoteDetails()
					.entrySet()) {
				QuoteDetailVO quoteDetVO = entry.getValue();

				for (QuoteDetailVO quoteDetailVO : this.quoteDetailList) {

					if (entry.getKey().getCode().equals(quoteDetailVO.getCompanyCode())) {
						if (quoteDetailVO.getIsQuoteRecommended()) {
							this.recommendedFlagcnt++;
						}
						quoteDetVO.setCompanyCode(quoteDetailVO.getCompanyCode());
						quoteDetVO.setQuoteNo(quoteDetailVO.getQuoteNo());
						quoteDetVO.setIsQuoteRecommended(quoteDetailVO.getIsQuoteRecommended());
						quoteDetVO.setRecommendationSummary(quoteDetailVO
								.getRecommendationSummary());
						quoteDetVO.getQuoteSlipPrmDetails().setCoverDescription(
								quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription());
						quoteDetVO.setSumInsured(quoteDetailVO.getSumInsured());
						quoteDetVO.setQuoteDate(quoteDetailVO.getQuoteDate());
						PremiumVO premiumVO = new PremiumVO();
						premiumVO.setPremium(quoteDetailVO.getQuoteSlipPrmDetails().getPremium());
						premiumVO.setCoverDescription(quoteDetailVO.getQuoteSlipPrmDetails()
								.getCoverDescription());
						quoteDetVO.setQuoteSlipPrmDetails(premiumVO);
						// status as active
						quoteDetVO.setStatusCode(1);
						// populate UW Field details
						ProductVO productVO = this.getProductFieldVOTableData(quoteDetailVO, "quoteAdded_"+quoteDetailVO.getCompanyCode());
						if(Utils.isEmpty(productVO)){
						    validResponseForUWFields = false;
						    continue; // there must be some validation error on underwriting fields hence proceed with next quote record 
						}
						quoteDetVO.setProductDetails(productVO);
						quoteDetVO.setPolicyTerm(quoteDetailVO.getPolicyTerm());

						addedQuotes.put(entry.getKey(), quoteDetVO);

						break;
					}
				}
			}
			if(!validResponseForUWFields){
			    return null;// break here as there are validation errors for Underwriting fields under quote comparison section
			}
			Map<InsCompanyVO, QuoteDetailVO> tempMap = new HashMap<InsCompanyVO, QuoteDetailVO>();
			tempMap.putAll(this.policyDetails.getQuoteDetails());

			this.policyDetails.setQuoteDetails(addedQuotes);

			if (1 < this.recommendedFlagcnt) {
				FacesContext.getCurrentInstance().addMessage(
						"ERROR_QUOTATION_SAVE",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select only one quotation to recommend/close",
								"Please select only one quotation to recommend"));
				return null;

			}

			// Before performing save operation let's check if there are any referrals
			//if(!Utils.isEmpty(this.getSaveFromReferralDialog()) && "true".equalsIgnoreCase(this.getSaveFromReferralDialog())){
			if(Utils.isEmpty(this.getSaveFromReferralDialog())){
			    TaskVO taskVO = ReferralHelper.checkForReferrals(this.policyDetails, SectionId.QUOTESLIP);
				if(!Utils.isEmpty(taskVO)){
					this.setReferralDesc(taskVO.getDesc());
					RequestContext context = RequestContext.getCurrentInstance();
					if( context.isAjaxRequest() ){
						context.addCallbackParam("referral", Boolean.TRUE);
						return null;
					}
				}
			}

			policyDetails =
					(PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("quotationSvc",
							"createQuotation", this.policyDetails);

			this.policyDetails.setQuoteDetails(tempMap);
			String tempCompCode = null;
			for (Entry<InsCompanyVO, QuoteDetailVO> entry : policyDetails.getQuoteDetails()
					.entrySet()) {
				this.policyDetails.getQuoteDetails().put(entry.getKey(), entry.getValue());
				if(entry.getValue().getIsQuoteRecommended()){
					// temp =

					this.quoteDetailVOClosed = entry.getValue();
					tempCompCode = entry.getKey().getCode();
					this.quoteDetailVOClosed.setCompanyCode(tempCompCode);
				}

			}

		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
							"Error saving quote details, please try again after sometime"));
			ex.printStackTrace();
			return null;
		}
		FacesContext.getCurrentInstance()
		.addMessage(
				"MESSAGE_SUCCESS",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Quote Details Captured Successfully",
						" successfully"));
		return this.loadQuotationsDetail();

	}

	public String next() {
		//perform save operation first on click of next button
		if(Utils.isEmpty(this.save())){
			return null;
		}

		if (0 == this.recommendedFlagcnt) {
			FacesContext.getCurrentInstance().addMessage(
					"ERROR_QUOTATION_SAVE",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please recommend a quotation before proceeding to policy screen",
							"Please recommend a quotation before proceeding to policy screen"));
			return null;

		}
		//next check if quote slip mb is already available in session if so then invoke retrieveInsuredQuoteDetails method
		//on the bean
		PolicyMB policyMB = (PolicyMB)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AppConstants.BEAN_NAME_POLICY_PAGE);
		if(!Utils.isEmpty(policyMB)){
			policyMB.loadQuotationDetails();
		}

		return "policy";
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public String retrieveProductUWFields(javax.faces.event.AjaxBehaviorEvent event) {
		ProductVO productVO = new ProductVO();
		productVO.setProductClass(this.quoteDetailVO.getProductDetails().getProductClass());
		// Product UW Fields retrieval through service call
		ProductVO svcResponseVO = MasterDataRetrievalUtil.getProductUWDetails(productVO);
		this.quoteDetailVO.setProductDetails(svcResponseVO);

		LookupVO lookupVO = new LookupVO();
		lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMPPRODUCTLINK);
		lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getProductClass()));
		LookupVO responseVO = MasterDataRetrievalUtil.getInsCompanies(lookupVO);
		this.insCompanies = responseVO.getCodeDescMap();

		return null;
	}

	/**
	 * This method will load quotation details while loading closing slip screen
	 * @return
	 */
	public String loadQuotationsDetail() {

		this.loadQuoteSlipDetails();
		this.loadQuotations();
		
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

		return "closeslip";
	}


	public String loadQuoteSlipDetails() {
		QuoteSlipMB quoteSlipMB =
				(QuoteSlipMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("quoteSlipMB");
		this.policyDetails = quoteSlipMB.getPolicyVO();
		this.insuredDetails = this.policyDetails.getInsuredDetails();
		this.quoteDetailVO = quoteSlipMB.getQuoteDetailVO();
		List<ProductUWFieldVO> uwFieldList = new ArrayList<ProductUWFieldVO>();
		for(ProductUWFieldVO uwField : this.quoteDetailVO.getProductDetails().getUwFieldsList()){
			uwFieldList.add(this.getProductUwFieldVOClone(uwField));
		}
		this.slipUwList = uwFieldList;

		LookupVO lookupVO = new LookupVO();
		lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMPPRODUCTLINK);
		lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getProductClass()));
		LookupVO responseVO = MasterDataRetrievalUtil.getInsCompanies(lookupVO);
		this.insCompanies = responseVO.getCodeDescMap();


		Map<InsCompanyVO, QuoteDetailVO> mapOfQuoteDets = this.policyDetails.getQuoteDetails();

		if (!Utils.isEmpty(mapOfQuoteDets)) {
			Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
			Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
			InsCompanyVO insCompanyVO = null;
			while (iterator.hasNext()) {
				insCompanyVO = iterator.next();
				this.selectedInsCompanies.add(insCompanyVO.getCode()); // Added by Hafeezur
			}
		}


		// Getting the Company names in the drop down


		return "closeslip";
	}

	public void loadQuotations() {

		PolicyVO policyVO =
				(PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("quotationSvc", "getQuotation",
						this.quoteDetailVO);

		if (Utils.isEmpty(policyVO)) {
			return;
		}
		List<QuoteDetailVO> quoteDetails = new ArrayList<QuoteDetailVO>();
		for (Entry<InsCompanyVO, QuoteDetailVO> entry : policyVO.getQuoteDetails().entrySet()) {
			quoteDetails.add(entry.getValue());
			this.policyDetails.getQuoteDetails().put(entry.getKey(), entry.getValue());
			if(entry.getValue().getIsQuoteRecommended()){
				this.quoteDetailVOClosed = entry.getValue();
				this.quoteDetailVOClosed.setCompanyCode(entry.getKey().getCode());
			}

		}
		this.quoteDetailList = quoteDetails;
		this.quoteDetailVODataModel = new QuoteDetailVODataModel(this.quoteDetailList);

	}

	/**
	 * @return the quoteDetailVODataModel
	 */
	public QuoteDetailVODataModel getQuoteDetailVODataModel() {
		return this.quoteDetailVODataModel;
	}


	/**
	 * @param quoteDetailVODataModel the quoteDetailVODataModel to set
	 */
	public void setQuoteDetailVODataModel(QuoteDetailVODataModel quoteDetailVODataModel) {
		this.quoteDetailVODataModel = quoteDetailVODataModel;
	}
 
	public String generatePDFForCloseSlip(){
	    //perform save operation first on click of next button
        /*
	    if(Utils.isEmpty(this.save())){
            return null;
        }*/
		try {
			QuoteSlipPDFGenerator quoteSlipPDFGenerator=new QuoteSlipPDFGenerator();
			Map<InsCompanyVO, QuoteDetailVO>  mapOfQuoteDets = this.policyDetails.getQuoteDetails();

			Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
			Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
			InsCompanyVO insCompanyVO = null;
			while(iterator.hasNext()){

				insCompanyVO = iterator.next();
				insCompanyVO =  (InsCompanyVO)ServiceTaskExecutor.INSTANCE.executeSvc("companySvc","getPolicy",insCompanyVO);
				quoteSlipPDFGenerator.generatePDFForCloseSlip(this.quoteDetailVO, this.insuredDetails, insCompanyVO.getContactAndAddrDetails(),insCompanyVO.getName(), Utils.getSingleValueAppConfig("quoteSlipfilePath")+"_"+new Date().getTime(), Utils.getSingleValueAppConfig("imagePath"));


			}
			FacesContext.getCurrentInstance().addMessage("SUCCESS_EMAIL_MSG", new FacesMessage(FacesMessage.SEVERITY_INFO,"Closingslip is successfully  emailed",null));

		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error generating quote details document, see the error log"));
		}
		return null;
	}

	@Override
	public String saveReferralTask() {
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
		taskVO.setTaskSectionType(Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_CLOSESLIP")));
		this.saveReferralTask(taskVO);//perform referral save task
		return super.saveReferralTask();
	}
}