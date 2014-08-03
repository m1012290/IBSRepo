/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.RowEditEvent;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shrinfo.ibs.cmn.constants.CommonConstants;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.docgen.QuoteSlipPDFGenerator;
import com.shrinfo.ibs.helper.ReferralHelper;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.AppFlow;
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
    
	private String companyCodeForQuote;
    
    private Map<String,String> selectedInsCompaniesMap = new HashMap<String, String>();
        
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
        this.setEditApproved(Boolean.FALSE);
        this.setNavigationDisbled(Boolean.FALSE);
        this.setAppFlow(null);
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

    public String getCompanyCodeForQuote() {
		return companyCodeForQuote;
	}

	public void setCompanyCodeForQuote(String companyCodeForQuote) {
		this.companyCodeForQuote = companyCodeForQuote;
	}

	public Map<String, String> getSelectedInsCompaniesMap() {
		return selectedInsCompaniesMap;
	}

	public void setSelectedInsCompaniesMap(
			Map<String, String> selectedInsCompaniesMap) {
		this.selectedInsCompaniesMap = selectedInsCompaniesMap;
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
		this.quoteDetailVO.setCompanyCode(this.companyCodeForQuote);

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
        //set the company name also to quotedetailvo
    	Set<Entry<String,String>> entrySet = this.insCompanies.entrySet();
        Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while(iterator.hasNext()){
        	Entry<String,String> entryObj = iterator.next();
        	if(this.companyCodeForQuote.equals(entryObj.getValue())){
        		temp.setCompanyDesc(entryObj.getKey());
        		break;
        	}
        }
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

        productVO.setName(quoteDetailVO.getProductDetails().getName());

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
            // Before proceeding validate if this is referral approval flow
            FacesContext fc = FacesContext.getCurrentInstance();
            Map map=fc.getExternalContext().getSessionMap();        
            EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
            if(!Utils.isEmpty(editCustEnqDetailsMB.getAppFlow())){
                if(editCustEnqDetailsMB.getAppFlow().equals(AppFlow.REFERRAL_APPROVAL)){
                    /*
                    TaskVO taskVO = new TaskVO();
                    taskVO.setEnquiry(enquiryDetails);
                    TaskVO svcResponse = executeTaskSvcForTaskDetails(taskVO);
                    StatusVO statusVO = new StatusVO();
                    statusVO.setCode(Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_APPROVED")));
                    svcResponse.setStatusVO(statusVO);
                    */
                    this.policyDetails.setAppFlow(AppFlow.REFERRAL_APPROVAL);
                }
            }
            
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

                        // Based on the flow set the status flag.
                        if(!Utils.isEmpty(editCustEnqDetailsMB.getAppFlow())){
                            if(editCustEnqDetailsMB.getAppFlow().equals(AppFlow.REFERRAL_APPROVAL)){
                                quoteDetVO.setStatusCode(4);
                            } else if(AppFlow.REFERRAL_APPROVED.equals(editCustEnqDetailsMB.getAppFlow())) {
                                // task is already approved. Hence set the status to active.
                                quoteDetVO.setStatusCode(1);
                            } else if(!Utils.isEmpty(this.getSaveFromReferralDialog()) && "true".endsWith(this.getSaveFromReferralDialog())) {
                                quoteDetVO.setStatusCode(3);
                            }
                        }
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
            if((Utils.isEmpty(this.getSaveFromReferralDialog()) || "false".equalsIgnoreCase(this.getSaveFromReferralDialog())) 
                    && (!AppFlow.REFERRAL_APPROVED.equals(this.getAppFlow()) || this.getEditApproved())){
                TaskVO taskVO = ReferralHelper.checkForReferrals(this.policyDetails, SectionId.CLOSINGSLIP);
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
    	FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();        
        
        // Referral
        EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get("editCustEnqDetailsMB");
        LoginMB loginManageBean = (LoginMB) map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
        int closeSlipSectionCode = Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_CLOSESLIP"));
        this.loadQuoteSlipDetails();
        if(editCustEnqDetailsMB.getEnquiryVO().getType().getEnquiryType().equals(CommonConstants.ENQUIRY_TYPE_ENDORSEMENT)){
        	this.policyDetails = editCustEnqDetailsMB.getPolicyVO();
        }
        if(!editCustEnqDetailsMB.getEnquiryVO().getType().getEnquiryType().equals(CommonConstants.ENQUIRY_TYPE_ENDORSEMENT)){
        	this.loadQuotations();
        }
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
            if (closeSlipSectionCode <= this.getTaskVO().getTaskSectionType() && 3 == this.getTaskVO().getStatusVO().getCode()
                && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.screenFreeze = Boolean.TRUE;
            }
            if(3 == this.getTaskVO().getStatusVO().getCode() && closeSlipSectionCode == this.getTaskVO().getTaskSectionType()) {
                this.setAppFlow(AppFlow.REFERRAL_APPROVAL);
            }
            
            if(3 != this.getTaskVO().getStatusVO().getCode() && closeSlipSectionCode == this.getTaskVO().getTaskSectionType()) {
                this.setAppFlow(AppFlow.REFERRAL_APPROVED);
            }
        }
        
        // Check if there is a pending/Approval referral for current section/transaction for this enquiry number.
        this.setTaskVO(new TaskVO());
        this.getTaskVO().setEnquiry(editCustEnqDetailsMB.getEnquiryVO());
        this.getTaskVO().setTaskSectionType(closeSlipSectionCode);
        this.getTaskVO().setTaskType(Integer.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL")));
        this.setTaskVO(this.checkReferral(this.getTaskVO()));
        if(!Utils.isEmpty(this.getTaskVO())) {
            // Screen will be freezed if there is a approved referral not assigned to logged-in user for current screen. 
            if (closeSlipSectionCode == this.getTaskVO().getTaskSectionType()
                && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.screenFreeze = Boolean.TRUE;                        
            }
            // navigation will be disabled only if referral is pending for the current screen and is not assigned to logged in user
            if(3 == this.getTaskVO().getStatusVO().getCode() && closeSlipSectionCode == this.getTaskVO().getTaskSectionType()
               && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.setNavigationDisbled(Boolean.TRUE);
            }
            // Edit button will be visible only if current screen task is approved since screen will be freezed.
            if(3 != this.getTaskVO().getStatusVO().getCode() && closeSlipSectionCode == this.getTaskVO().getTaskSectionType()
               && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                this.setEditVisible(Boolean.TRUE);
            }
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
		//this will be used within print dialog box
        this.selectedInsCompaniesMap = this.getSelectedInsCompaniesMapFromList(this.insCompanies, this.selectedInsCompanies);
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
            QuoteDetailVO temp = entry.getValue();
            //set the company name also to quotedetailvo
        	Set<Entry<String,String>> entrySet = this.insCompanies.entrySet();
            Iterator<Entry<String, String>> iterator = entrySet.iterator();
            while(iterator.hasNext()){
            	Entry<String,String> entryObj = iterator.next();
            	if(temp.getCompanyCode().equals(entryObj.getValue())){
            		temp.setCompanyDesc(entryObj.getKey());
            		break;
            	}
            }
            //temp.setCompanyDesc(entry.getKey().getName());
        	quoteDetails.add(temp);
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
            
            for (Entry<InsCompanyVO, QuoteDetailVO> entry : this.policyDetails.getQuoteDetails()
                    .entrySet()) {
                QuoteDetailVO quoteDetVO = entry.getValue();
                InsCompanyVO insCompanyVO=new InsCompanyVO();


                for (QuoteDetailVO quoteDetailVO : this.quoteDetailList) {

                    if (entry.getKey().getCode().equals(quoteDetailVO.getCompanyCode())) {
                        if (quoteDetailVO.getIsQuoteRecommended()) {
                            this.recommendedFlagcnt++;
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
                            productVO.setName(this.quoteDetailVO.getProductDetails().getName());
                            if(Utils.isEmpty(productVO)){
                                //validResponseForUWFields = false;
                                continue; // there must be some validation error on underwriting fields hence proceed with next quote record 
                            }
                            quoteDetVO.setProductDetails(productVO);
                            quoteDetVO.setPolicyTerm(quoteDetailVO.getPolicyTerm());
                            
                            insCompanyVO.setCode(quoteDetailVO.getCompanyCode());
                            insCompanyVO =  (InsCompanyVO)ServiceTaskExecutor.INSTANCE.executeSvc("companySvc","getCompany",insCompanyVO);

                            quoteSlipPDFGenerator.generatePDFForCloseSlip(quoteDetVO, this.insuredDetails, insCompanyVO.getContactAndAddrDetails(),insCompanyVO.getName(), Utils.getSingleValueAppConfig("quoteSlipfilePath")+"_"+new Date().getTime(), Utils.getSingleValueAppConfig("imagePath"));
                        }
                        
                 
                    }

                }

            }
           /* Map<InsCompanyVO, QuoteDetailVO>  mapOfQuoteDets = this.policyDetails.getQuoteDetails();
            
            Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
            Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
            InsCompanyVO insCompanyVO = null;
            while(iterator.hasNext()){

                insCompanyVO = iterator.next();
                QuoteDetailVO qdVO=mapOfQuoteDets.get(insCompanyVO);
                insCompanyVO =  (InsCompanyVO)ServiceTaskExecutor.INSTANCE.executeSvc("companySvc","getCompany",insCompanyVO);
                quoteSlipPDFGenerator.generatePDFForCloseSlip(qdVO, this.insuredDetails, insCompanyVO.getContactAndAddrDetails(),insCompanyVO.getName(), Utils.getSingleValueAppConfig("quoteSlipfilePath")+"_"+new Date().getTime(), Utils.getSingleValueAppConfig("imagePath"));



            }*/
            FacesContext.getCurrentInstance().addMessage("SUCCESS_EMAIL_MSG", new FacesMessage(FacesMessage.SEVERITY_INFO,"Closingslip is successfully  emailed",null));

        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error generating quote details document, see the error log"));
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
        taskVO.setTaskSectionType(Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_CLOSESLIP")));
        this.saveReferralTask(taskVO);//perform referral save task
        return super.saveReferralTask();
    }
    
    public String back() {
        this.setEditApproved(Boolean.FALSE);
        FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();        
        
        EditCustEnqDetailsMB editCustEnqDetailsMB = (EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
        if(editCustEnqDetailsMB.getEnquiryVO().getType().getEnquiryType().equals(CommonConstants.ENQUIRY_TYPE_ENDORSEMENT)){
        	return "editenquiry";
        }
        return "quoteslip";
    }
    
    public String printDoc() throws IOException, DocumentException{
       FacesContext faces = FacesContext.getCurrentInstance();
       HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
         
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
                  Font.BOLD);
        Font prodFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
              Font.BOLD);
        Font insFont = new Font(Font.FontFamily.COURIER, 26,
                  Font.BOLD);
        Font lnFont = new Font(Font.FontFamily.COURIER, 24,
              Font.BOLD);
        
         
         String insuredname = insuredDetails.getName();
         QuoteDetailVO quoteDetails=this.quoteDetailVO;
         String prodName = quoteDetails.getProductDetails().getName();

         ProductVO products = quoteDetails.getProductDetails();
         java.util.List<ProductUWFieldVO> prodListFields = products.getUwFieldsList();
         
         for (Entry<InsCompanyVO, QuoteDetailVO> entry : this.policyDetails.getQuoteDetails()
                 .entrySet()) {
             QuoteDetailVO quoteDetVO = entry.getValue();
             InsCompanyVO insCompanyVO=new InsCompanyVO();
             for (QuoteDetailVO quoteDetailVO : this.quoteDetailList) {
                 if (entry.getKey().getCode().equals(quoteDetailVO.getCompanyCode())) {
                     if (quoteDetailVO.getIsQuoteRecommended()) {
                         this.recommendedFlagcnt++;
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
                         productVO.setName(this.quoteDetailVO.getProductDetails().getName());
                         if(Utils.isEmpty(productVO)){
                             //validResponseForUWFields = false;
                             continue; // there must be some validation error on underwriting fields hence proceed with next quote record 
                         }
                         quoteDetVO.setProductDetails(productVO);
                         quoteDetVO.setPolicyTerm(quoteDetailVO.getPolicyTerm());
                         
                       
                         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                         Document document = new Document();
                         PdfAction action = new PdfAction(PdfAction.PRINTDIALOG);
                         PdfWriter.getInstance(document, outputStream).setOpenAction(action);



                         // Inserting Image in PDF
                       /*  Image image = Image.getInstance(imagePath);
                         image.scaleAbsolute(120f, 60f);// image width,height
                */            
                         java.util.Iterator<ProductUWFieldVO> itr1 = prodListFields.iterator();


                         // Now Insert Every Thing Into PDF Document
                         document.open();// PDF document opened........



                         document.add(new Paragraph("         XYZ INSURANCE BROKERS  ",insFont));
                         
                         document.add(new Paragraph("===================================",lnFont));




                                 
                            InsCompanyVO inscompanyVO=new InsCompanyVO();
                            inscompanyVO.setCode(this.quoteDetailVO.getCompanyCode());
                            
                            inscompanyVO =  (InsCompanyVO)ServiceTaskExecutor.INSTANCE.executeSvc("companySvc","getCompany",inscompanyVO);
                    
                            document.add(new Paragraph(inscompanyVO.getName(),catFont));
                            
                            if(inscompanyVO.getContactAndAddrDetails().getAddressVO().getAddress()!=null){
                                document.add(new Paragraph("  "+inscompanyVO.getContactAndAddrDetails().getAddressVO().getAddress()));
                            } 
                            if(inscompanyVO.getContactAndAddrDetails().getAddressVO().getCity()!=null){
                                document.add(new Paragraph("  "+inscompanyVO.getContactAndAddrDetails().getAddressVO().getCity()));
                            } 
                         
                            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------"));
                            
                            document.add(new Paragraph("                                                        "+prodName+"                                        ",prodFont));
                            
                            document.add(new Paragraph("-----------------------------------------------------------------------------------------------------------------------------"));
                            
                            document.add(Chunk.NEWLINE); 
                         
                            PdfPTable table = new PdfPTable(2);
                            table.addCell(new Paragraph("Insured Name"));
                            if(insuredname!=null)
                            table.addCell(new Paragraph(insuredname));
                            else
                            table.addCell("");


                          
                            table.addCell(new Paragraph("Quotation Number"));
                            if(quoteDetailVO.getQuoteNo()!=null)
                            table.addCell(new Paragraph(quoteDetailVO.getQuoteNo()));
                            else
                            table.addCell("");
                            
                            
                            table.addCell(new Paragraph("Quoted Premium"));
                            if(quoteDetailVO.getQuoteSlipPrmDetails().getPremium()!=null || quoteDetailVO.getQuoteSlipPrmDetails().getPremium()!=new BigDecimal(0))
                            table.addCell(new Paragraph(String.valueOf(quoteDetailVO.getQuoteSlipPrmDetails().getPremium())));
                            else
                            table.addCell("");
                            
                            
                            table.addCell(new Paragraph("Sum  Insured"));
                            if(quoteDetailVO.getSumInsured()!=null || quoteDetailVO.getSumInsured()!=new BigDecimal(0))
                            table.addCell(new Paragraph(String.valueOf(quoteDetailVO.getSumInsured())));
                            else
                            table.addCell("");
                            
                            table.addCell(new Paragraph("Policy Term"));
                            if(quoteDetailVO.getPolicyTerm()!=null || quoteDetailVO.getPolicyTerm()!=0)
                            table.addCell(new Paragraph(String.valueOf(quoteDetailVO.getPolicyTerm())));
                            else
                            table.addCell("");
                            
                            table.addCell(new Paragraph("Cover Description"));
                            if(quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription()!=null)
                            table.addCell(new Paragraph(quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription()));
                            else
                            table.addCell("");
                            
                            table.addCell(new Paragraph("Recommendation Summary"));
                            if(quoteDetailVO.getRecommendationSummary()!=null)
                            table.addCell(new Paragraph(quoteDetailVO.getRecommendationSummary()));
                            else
                            table.addCell("");
                            
                            
                            while (itr1.hasNext()) {
                                ProductUWFieldVO prodFields = itr1.next();
                                table.addCell(new Paragraph(prodFields.getFieldName()));
                                if(prodFields.getResponse()!=null)
                                table.addCell(new Paragraph(prodFields.getResponse()));
                                else
                                table.addCell("");  
                            }
                            
                            document.add(table);
                            document.close();
                            byte[] outputBytes = outputStream.toByteArray();
                            response.setHeader("Pragma", "no-cache");  
                            response.setHeader("Cache-control", "private");  
                            response.setDateHeader("Expires", 0); 
                            response.setContentType("application/pdf");  
                            //response.setHeader("Content-Disposition", "attachment; filename=\"test.pdf\"");  
                              
                            if (outputBytes != null) {  
                                response.setContentLength(outputBytes.length);  
                                ServletOutputStream out = response.getOutputStream();  
                                out.write(outputBytes);
                                
                                out.flush();  
                                out.close();  
                            } 

                     }
                 }
             }

         }
        return "page1";
    }
}
