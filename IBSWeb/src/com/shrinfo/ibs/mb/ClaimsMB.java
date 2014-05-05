package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.SearchItemVO;
import com.shrinfo.ibs.vo.business.SearchVO;

@ManagedBean(name = "claimsMB")
@SessionScoped
public class ClaimsMB extends BaseManagedBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2909580737203256643L;

    private String custName;

    private String mobNumber;

    private String emailID;

    private String insuredName;

    private Long enquiryNumber;

    private Long quoteSlipNumber;

    private String policyNumber;

    private List<SearchItemVO> searchList;

    private SearchItemVO searchItem;

    private SearchItemVODataModel searchItemVODataModel;
    
    private PolicyVO policyVO = new PolicyVO();

    //This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields(){
        this.custName = null;
        this.mobNumber = null;
        this.emailID = null;
        this.insuredName = null;
        this.enquiryNumber = null;
        this.quoteSlipNumber = null;
        this.policyNumber = null;
        this.searchList = null;
        this.searchItem = null;
        this.searchItemVODataModel = null;
        this.policyVO = new PolicyVO();
    }


    public ClaimsMB() {
        super();
    }


    public ClaimsMB(String custName, String mobNumber, String emailID, String insuredName,
            Long enquiryNumber, Long quoteSlipNumber, String policyNumber) {
        super();
        this.custName = custName;
        this.mobNumber = mobNumber;
        this.emailID = emailID;
        this.insuredName = insuredName;
        this.enquiryNumber = enquiryNumber;
        this.quoteSlipNumber = quoteSlipNumber;
        this.policyNumber = policyNumber;

    }


    public String getCustName() {
        return custName;
    }


    public void setCustName(String custName) {
        this.custName = custName;
    }


    public String getMobNumber() {
        return mobNumber;
    }


    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }


    public String getEmailID() {
        return emailID;
    }


    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }


    public String getInsuredName() {
        return insuredName;
    }


    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }


    public Long getEnquiryNumber() {
        return enquiryNumber;
    }


    public void setEnquiryNumber(Long enquiryNumber) {
        this.enquiryNumber = enquiryNumber;
    }


    public Long getQuoteSlipNumber() {
        return quoteSlipNumber;
    }


    public void setQuoteSlipNumber(Long quoteSlipNumber) {
        this.quoteSlipNumber = quoteSlipNumber;
    }


    public String getPolicyNumber() {
        return policyNumber;
    }


    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }



    public List<SearchItemVO> getSearchList() {
        return searchList;
    }


    public void setSearchList(List<SearchItemVO> searchList) {
        this.searchList = searchList;
    }

    public SearchItemVO getSearchItem() {
        return searchItem;
    }


    public void setSearchItem(SearchItemVO searchItem) {
        this.searchItem = searchItem;
    }


    public SearchItemVODataModel getSearchItemVODataModel() {
        return searchItemVODataModel;
    }


    public void setSearchItemVODataModel(SearchItemVODataModel searchItemVODataModel) {
        this.searchItemVODataModel = searchItemVODataModel;
    }  


    
    public PolicyVO getPolicyVO() {
        return policyVO;
    }


    
    public void setPolicyVO(PolicyVO policyVO) {
        this.policyVO = policyVO;
    }
    
    public void onRowSelect(SelectEvent event) {

        try {
            SearchItemVO searchItemVO = (SearchItemVO) event.getObject();
            if(!Utils.isEmpty(searchItemVO.getPolicyNum())) {
                this.policyVO.setPolicyId(Long.valueOf(searchItemVO.getPolicyNum()));
                this.policyVO = (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "getPolicy",
                    this.policyVO);
            } else {
                FacesContext.getCurrentInstance()
                .addMessage(
                    "ERROR_POLICY_VIEW",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Selected record doen not have Valid associated policy", null));
            }
            
        } catch (BusinessException be) {
            logger.error(be, "Exception [" + be
                + "] encountered retreiving policy details");
            FacesContext.getCurrentInstance()
                    .addMessage(
                        "ERROR_ENQUIRY_SAVE",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error encountered retreiving policy details", null));

        } catch (SystemException se) {
            logger.error(se, "Exception [" + se
                + "] encountered retreiving policy details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_ENQUIRY_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "encountered retreiving policy details, please try again after sometime"));
        }

    }


    public String submit() {
        
        // TODO: this is a temporary fix.
        if(!Utils.isEmpty(this.enquiryNumber) && 0 == this.enquiryNumber){
            this.enquiryNumber = null;
        }        
        if(!Utils.isEmpty(this.quoteSlipNumber) && 0 == this.quoteSlipNumber){
            this.quoteSlipNumber = null;
        }

        SearchItemVO inputSearchItem = new SearchItemVO();
        inputSearchItem.setCustomerName(this.custName);
        inputSearchItem.setCustomerMob(this.mobNumber);
        inputSearchItem.setCustomerEmail(this.emailID);
        inputSearchItem.setInsuredName(this.insuredName);
        inputSearchItem.setEnquiryNum(this.enquiryNumber);
        inputSearchItem.setQuotationNum(this.quoteSlipNumber);
        inputSearchItem.setPolicyNum(this.policyNumber);

        // Calling the Service
        SearchVO searchVO =
            (SearchVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerInsuredSearchSvcBean",
                "getSearchResult", inputSearchItem);
        searchList = searchVO.getSearchItems();
        searchItemVODataModel = new SearchItemVODataModel(searchList);

        return null;
    }    
}
