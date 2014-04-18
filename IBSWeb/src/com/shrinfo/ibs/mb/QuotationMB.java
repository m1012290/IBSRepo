/**
 * 
 */
package com.shrinfo.ibs.mb;

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

import org.primefaces.event.CloseEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.helper.ReferralHelper;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.PremiumVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
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

    private QuoteDetailVO selectedQuoteDetailVO = new QuoteDetailVO();

    private ProductVO quoteProductDetails = new ProductVO();

    private QuoteDetailVO quoteDetailVOClosed = new QuoteDetailVO();

    private InsuredVO insuredDetails = new InsuredVO();

    private List<QuoteDetailVO> quoteDetailList = new ArrayList<QuoteDetailVO>();

    private PolicyVO policyDetails = new PolicyVO();

    private QuoteDetailVODataModel quoteDetailVODataModel;

    private QuoteDetailVO quoteDetSelection = new QuoteDetailVO();

    private List<QuoteDetailVO> quoteDetailListClosed = new ArrayList<QuoteDetailVO>();


    //This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
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
    }
    
    /**
     * @return the quoteDetSelection
     */
    public QuoteDetailVO getQuoteDetSelection() {
        return quoteDetSelection;
    }


    /**
     * @param quoteDetSelection the quoteDetSelection to set
     */
    public void setQuoteDetSelection(QuoteDetailVO quoteDetSelection) {
        this.quoteDetSelection = quoteDetSelection;
    }
    public PolicyVO getPolicyDetails() {
        return policyDetails;
    }

    public void setPolicyDetails(PolicyVO policyDetails) {
        this.policyDetails = policyDetails;
    }

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



    public QuoteDetailVO getSelectedQuoteDetailVO() {
        return selectedQuoteDetailVO;
    }


    public void setSelectedQuoteDetailVO(QuoteDetailVO selectedQuoteDetailVO) {
        this.selectedQuoteDetailVO = selectedQuoteDetailVO;
    }


    public ProductVO getQuoteProductDetails() {
        return quoteProductDetails;
    }


    public void setQuoteProductDetails(ProductVO quoteProductDetails) {
        this.quoteProductDetails = quoteProductDetails;
    }

    public QuoteDetailVO getQuoteDetailVOClosed() {
        return quoteDetailVOClosed;
    }


    public void setQuoteDetailVOClosed(QuoteDetailVO quoteDetailVOClosed) {
        this.quoteDetailVOClosed = quoteDetailVOClosed;
    }

    public InsuredVO getInsuredDetails() {
        return insuredDetails;
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
        this.quoteDetailList.add(getQuoteDetailTableData(this.quoteDetailVO));
        this.quoteDetailVODataModel = new QuoteDetailVODataModel(this.getQuoteDetailList());
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
        PremiumVO premiumVO = new PremiumVO();
        premiumVO.setPremium(quoteDetailVO.getQuoteSlipPrmDetails().getPremium());
        premiumVO.setCoverDescription(quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription());
        detailVO.setQuoteSlipPrmDetails(premiumVO);
        

        // populate UW Field details
        ProductVO productVO = new ProductVO();
        productVO.setProductClass(this.quoteDetailVO.getProductDetails().getProductClass());
        

        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> requestMap = fc.getExternalContext().getRequestParameterMap();

        if (!Utils.isEmpty(requestMap)) {
            String response = null;
            for (ProductUWFieldVO uwField : this.quoteDetailVO.getProductDetails()
                    .getUwFieldsList()) {
                response =
                    requestMap.get(Utils.concat("field_", String.valueOf(uwField.getFieldOrder())));
                uwField.setResponse(response);
            }
        }
        productVO.setUwFieldsList(this.quoteDetailVO.getProductDetails().getUwFieldsList());
        detailVO.setProductDetails(productVO);

        return detailVO;
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
        quoteDetailList.remove((QuoteDetailVO) event.getObject());
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
        for (QuoteDetailVO detailVO : quoteDetailList) {
            this.quoteDetailListClosed.add(getQuoteDetailTableData(detailVO));
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
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quote Details save:",
                    " At least one quote should be added"));
            return null;
        }
        PolicyVO policyDetails = new PolicyVO();
        try {
            Map<InsCompanyVO, QuoteDetailVO> addedQuotes =
                new HashMap<InsCompanyVO, QuoteDetailVO>();

int recommendedFlagcnt = 0;
            for (Entry<InsCompanyVO, QuoteDetailVO> entry : this.policyDetails.getQuoteDetails()
                    .entrySet()) {
                QuoteDetailVO quoteDetVO = entry.getValue();

                for (QuoteDetailVO quoteDetailVO : this.quoteDetailList) {

                    if (entry.getKey().getCode().equals(quoteDetailVO.getCompanyCode())) {
if (quoteDetailVO.getIsQuoteRecommended()) {
                            recommendedFlagcnt++;
                        }
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
                        ProductVO productVO = new ProductVO();
                        productVO.setProductClass(quoteDetailVO.getProductDetails()
                                .getProductClass());
                        productVO.setUwFieldsList(quoteDetailVO.getProductDetails()
                                .getUwFieldsList());
                        quoteDetVO.setProductDetails(productVO);
						quoteDetVO.setPolicyTerm(quoteDetailVO.getPolicyTerm());

                        addedQuotes.put(entry.getKey(), quoteDetVO);

                        break;
                    }
                }
            }
            Map<InsCompanyVO, QuoteDetailVO> tempMap = new HashMap<InsCompanyVO, QuoteDetailVO>();
            tempMap.putAll(this.policyDetails.getQuoteDetails());

            this.policyDetails.setQuoteDetails(addedQuotes);
			
			if (1 < recommendedFlagcnt) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_QUOTATION_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                        "Please select only one quotation to recommend"));
                return null;

            }

         // Before performing save operation let's check if there are any referrals
            TaskVO taskVO = ReferralHelper.checkForReferrals(this.policyDetails, SectionId.QUOTESLIP);
            if(!Utils.isEmpty(taskVO)){
                setReferralDesc(taskVO.getDesc());
                RequestContext context = RequestContext.getCurrentInstance();
                if( context.isAjaxRequest() ){
                    context.addCallbackParam("referral", Boolean.TRUE);





                    return null;
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
        }
        FacesContext.getCurrentInstance()
                .addMessage(
                    "MESSAGE_SUCCESS",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Quote Details Captured ",
                        " successfully"));
        return null;
    }

    public String next() {

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

    public String loadQuotationsDetail() {

        loadQuoteSlipDetails();
        loadQuotations();

        return "closeslip";
    }


    public String loadQuoteSlipDetails() {
        QuoteSlipMB quoteSlipMB =
            (QuoteSlipMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get("quoteSlipMB");
        this.policyDetails = quoteSlipMB.getPolicyVO();
        this.insuredDetails = policyDetails.getInsuredDetails();
        this.quoteDetailVO = quoteSlipMB.getQuoteDetailVO();

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
        return quoteDetailVODataModel;
    }


    /**
     * @param quoteDetailVODataModel the quoteDetailVODataModel to set
     */
    public void setQuoteDetailVODataModel(QuoteDetailVODataModel quoteDetailVODataModel) {
        this.quoteDetailVODataModel = quoteDetailVODataModel;
    }
}