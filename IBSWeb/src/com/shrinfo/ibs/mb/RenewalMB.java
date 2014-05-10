/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.SearchItemVO;
import com.shrinfo.ibs.vo.business.SearchVO;

/**
 * @author Sunilk
 *
 */
@ManagedBean(name = "renewalMB")
@SessionScoped
public class RenewalMB extends BaseManagedBean implements Serializable{

	private SearchItemVO searchItemVO = new SearchItemVO();
	
	private SearchVO searchVO = new SearchVO();
	
    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.searchItemVO = new SearchItemVO();
        this.searchVO = new SearchVO();
    }

	
	public SearchVO getSearchVO() {
		return searchVO;
	}

	public void setSearchVO(SearchVO searchVO) {
		this.searchVO = searchVO;
	}

	public SearchItemVO getSearchItemVO() {
		return searchItemVO;
	}

	public void setSearchItemVO(SearchItemVO searchItemVO) {
		this.searchItemVO = searchItemVO;
	}

	public String searchPoliciesWithinExpiryDate(){	
		boolean allFieldsValid = true;
		if(Utils.isEmpty(searchItemVO.getPolicyExpiryStartDate())){
	        FacesContext.getCurrentInstance().addMessage(
                    "ERROR_RENEWAL_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a value for expiry start date",
                        null));
	        allFieldsValid = false;
		}
		if(Utils.isEmpty(searchItemVO.getPolicyExpiryEndDate())){
			FacesContext.getCurrentInstance().addMessage(
                    "ERROR_RENEWAL_POLICY_SEARCH",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a value for expiry end date",
                        null));
			allFieldsValid = false;
		}
		if(!allFieldsValid){
			return null;
		}
		
		if(searchItemVO.getPolicyExpiryEndDate().before(searchItemVO.getPolicyExpiryStartDate())){
			FacesContext.getCurrentInstance().addMessage(
                    "ERROR_RENEWAL_POLICY_SEARCH",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Policy Expiry End Date cannot be greater than Expiry Start Date",
                        null));
			return null;
		}
		try{
	        // Calling the Service
	        SearchVO searchVO = (SearchVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "getPolicies", this.searchItemVO);
	        this.searchVO = searchVO;
		}catch(Exception ex){
			FacesContext.getCurrentInstance().addMessage(
                    "ERROR_RENEWAL_POLICY_SEARCH",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected exception encountered please try again later",
                        null));
			
		}
	    return null;
	}
	
	public String generateRenewalNotice(){
		FacesContext.getCurrentInstance().addMessage(
                "RENEWAL_NOTICE_GENERATION_SUCCESS",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Renewal Notice Generated Successfully",
                    null));
		return null;
	}
}