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

import org.primefaces.event.RowEditEvent;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.PremiumVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

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

    private InsuredVO insuredDetails = new InsuredVO();

    private List<QuoteDetailVO> quoteDetailList = new ArrayList<QuoteDetailVO>();

    private PolicyVO policyDetails = new PolicyVO();




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

    public InsuredVO getInsuredDetails() {
        return insuredDetails;
    }

    public void setInsuredDetails(InsuredVO insuredDetails) {
        this.insuredDetails = insuredDetails;
    }

    public List<QuoteDetailVO> getQuoteDetailList() {
        return quoteDetailList;
    }

    public void setQuoteDetailList(List<QuoteDetailVO> quoteDetailList) {
        this.quoteDetailList = quoteDetailList;
    }

    public String addAction() {

        QuoteDetailVO detailVO = new QuoteDetailVO();
        detailVO.setQuoteNo(this.quoteDetailVO.getQuoteNo());
        detailVO.setCompanyCode(this.quoteDetailVO.getCompanyCode());
        detailVO.setQuoteSlipPrmDetails(this.quoteDetailVO.getQuoteSlipPrmDetails());
        detailVO.setSumInsured(this.quoteDetailVO.getSumInsured());
        detailVO.setPolicyTerm(this.quoteDetailVO.getPolicyTerm());
        detailVO.setRecommendationSummary(this.quoteDetailVO.getRecommendationSummary());
        PremiumVO premiumVO = new PremiumVO();
        premiumVO.setPremium(this.quoteDetailVO.getQuoteSlipPrmDetails().getPremium());
        premiumVO.setCoverDescription(this.quoteDetailVO.getQuoteSlipPrmDetails()
                .getCoverDescription());
        detailVO.setQuoteSlipPrmDetails(premiumVO);

        quoteDetailList.add(detailVO);
        return null;
    }

    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        quoteDetailList.remove((QuoteDetailVO) event.getObject());
    }

    public String save() {
        PolicyVO policyDetails = new PolicyVO();
        try {
            QuoteSlipMB quoteSlipMB =
                (QuoteSlipMB) FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().get("quoteSlipMB");
            this.policyDetails = quoteSlipMB.getPolicyVO();

            InsCompanyVO companyVO = null;
            /*
             * Map<InsCompanyVO, QuoteDetailVO> companyQuotationMap = new HashMap<InsCompanyVO,
             * QuoteDetailVO>(); for(QuoteDetailVO quoteDetailVO : this.quoteDetailList ){ companyVO
             * = new InsCompanyVO(); companyVO.setCode(quoteDetailVO.getCompanyCode());
             * QuoteDetailVO quoteDetVO = this.policyDetails.getQuoteDetails().get(companyVO);
             * quoteDetVO.setIsQuoteRecommended(quoteDetailVO.getIsQuoteRecommended());
             * quoteDetVO.getQuoteSlipPrmDetails
             * ().setCoverDescription(quoteDetailVO.getQuoteSlipPrmDetails().getCoverDescription());
             * quoteDetVO.setSumInsured(quoteDetailVO.getSumInsured());
             * quoteDetVO.setQuoteNo(quoteDetailVO.getQuoteNo());
             * quoteDetVO.setQuoteDate(quoteDetailVO.getQuoteDate());
             * companyQuotationMap.put(companyVO, quoteDetVO); }
             */
            for (Entry<InsCompanyVO, QuoteDetailVO> entry : this.policyDetails.getQuoteDetails()
                    .entrySet()) {
                QuoteDetailVO quoteDetVO = entry.getValue();
                boolean quotationFlag = false;
                for (QuoteDetailVO quoteDetailVO : this.quoteDetailList) {

                    if (entry.getKey().getCode().equals(quoteDetailVO.getCompanyCode())) {
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
                        quotationFlag = true;
                        break;
                    }
                }
                if (!quotationFlag) {
                    // status as pending. i.e. quotation from this company has not come and hence
                    // details are not being added
                    quoteDetVO.setStatusCode(2);
                }

            }

            policyDetails =
                (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("quotationSvc",
                    "createQuotation", this.policyDetails);

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

        if(Utils.isEmpty(policyVO)){
            return;
        }
        List<QuoteDetailVO> quoteDetails = new ArrayList<QuoteDetailVO>();
        for (Entry<InsCompanyVO, QuoteDetailVO> entry : policyVO.getQuoteDetails().entrySet()) {
            quoteDetails.add(entry.getValue());
        }
        this.quoteDetailList = quoteDetails;
    }

}