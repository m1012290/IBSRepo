/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.InsuredVO;

/**
 * @author sunilk
 * This ManagedBean is used for insuredmaster.xhtml screen which is used to create new insured
 * against an existing customer. This functionality already exists on quote slip screen where in
 * new insured is created everytime.
 */
@ManagedBean(name="insuredMasterMB")
@SessionScoped
public class InsuredMasterMB extends BaseManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsuredMasterMB(){
		super();
	}
	
	private InsuredVO insuredDetails = new InsuredVO();
	
	
	private List<CustomerVO> selectedCustomersList;
	
	//This property will be injected through dependency injection
	@ManagedProperty("#{customerMasterMB}")
	private CustomerMasterMB customerMasterMB;
	
	
	private CustomerVO customerVO;
	
	@Override
	protected void reinitializeBeanFields() {
		// TODO Auto-generated method stub
	}

	public InsuredVO getInsuredDetails() {
		return insuredDetails;
	}

	public void setInsuredDetails(InsuredVO insuredDetails) {
		this.insuredDetails = insuredDetails;
	}
	

	/**
	 * Action method which performs save operation i.e. on click of save on insuredmaster.xhtml screen
	 * @return
	 */
	public String save(){
		this.insuredDetails.setCustomerDetails(this.customerVO);
		try{
			InsuredVO insuredVO = (InsuredVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","createInsuredDetails",this.insuredDetails);
			FacesContext.getCurrentInstance().addMessage(
                    "SUCCESS_INSUREDMASTER_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Insured details saved successfully with id : "+insuredVO.getId(), null));
            
		}catch(BusinessException be){
			logger.error(be, "Exception [" + be
                    + "] encountered while saving customer details");
            FacesContext.getCurrentInstance().addMessage(
                    "ERROR_INSUREDMASTER_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                        "Unexpected error encountered, please try again after sometime"));
            return null;
		}catch(SystemException se){
			logger.error(se, "Exception [" + se
                    + "] encountered while saving customer details");
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_INSUREDMASTER_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                        "Unexpected error encountered, please try again after sometime"));
            return null;
		}
		return null;
	}

	public CustomerMasterMB getCustomerMasterMB() {
		return customerMasterMB;
	}

	public void setCustomerMasterMB(CustomerMasterMB customerMasterMB) {
		this.customerMasterMB = customerMasterMB;
	}
	
	public List<CustomerVO> getSelectedCustomersList() {
		return selectedCustomersList;
	}

	public void setSelectedCustomersList(List<CustomerVO> selectedCustomersList) {
		this.selectedCustomersList = selectedCustomersList;
	}

	public CustomerVO getCustomerVO() {
		return customerVO;
	}

	public void setCustomerVO(CustomerVO customerVO) {
		this.customerVO = customerVO;
	}

	/**
	 * This is the method which will provide filtered customers list depending
	 * on queried value from Customer
	 * @param query
	 * @return
	 */
	public List<CustomerVO> completeCustomersListMethod(String query){
		List<CustomerVO> allCustomers = customerMasterMB.getCustomersList();
		List<CustomerVO> filteredCustomers = new ArrayList<CustomerVO>();
		for(CustomerVO customerVO : allCustomers){
			if(customerVO.getName().toLowerCase().startsWith(query)){
				filteredCustomers.add(customerVO);
			}
		}
		return filteredCustomers;
	}
}