/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.CustomersListVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;
/**
 * @author sunilk
 * Created for customermaster.xhtml which is used to create new customer in the system...
 */
@ManagedBean(name = "customerMasterMB")
@SessionScoped
public class CustomerMasterMB extends BaseManagedBean implements Serializable{
	
	private static final long serialVersionUID = 6311980649197374413L;

    private static final Logger logger = Logger.getLogger(EditCustEnqDetailsMB.class);

	public CustomerMasterMB(){
		//public constructor
		super();
	}
	
	private EnquiryVO enquiryVO = new EnquiryVO();

	private List<CustomerVO> customersList;
    //private Map<String,String> salutations=new HashMap<String, String>();

	
    public EnquiryVO getEnquiryVO() {
		return enquiryVO;
	}

    /*
	public void setEnquiryVO(EnquiryVO enquiryVO) {
		this.enquiryVO = enquiryVO;
	}

	public Map<String, String> getSalutations() {
        return this.salutations;
    }


    public void setSalutations(Map<String, String> salutations) {
        this.salutations = salutations;
    }*/

    /**
     * Action method which performs save operation i.e. creates a new customer in the system
     * @return
     */
    public String save(){
    	try{
    	   EnquiryVO responseVO = (EnquiryVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerEnquirySvc",
                   "createCustomer", this.enquiryVO);
    	   
    	   FacesContext.getCurrentInstance().addMessage(
                   "SUCCESS_CUSTOMERMASTER_SAVE",
                   new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer created successfully with id : "+responseVO.getCustomerDetails().getCustomerId(),
                       null));
       
    	}catch(BusinessException be){
    		logger.error(be, "Exception [" + be
                    + "] encountered while saving customer details");
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_CUSTOMERMASTER_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                        "Unexpected error encountered, please try again after sometime"));
            return null;
    	}catch(SystemException se){
    		logger.error(se, "Exception [" + se
                    + "] encountered while saving customer details");
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_CUSTOMERMASTER_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                        "Unexpected error encountered, please try again after sometime"));
            return null;
    	}
    	return null;
    }

    /**
     * This method returns all the customers list available in the system. This method can be used wherever 
     * we need list of customers ex :- on customer list autocomplete drop down.
     * @return
     */
    public List<CustomerVO> getCustomersList(){
    	try{
    	   //if(Utils.isEmpty(this.customersList)){
	     	   CustomersListVO responseVO = (CustomersListVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerEnquirySvc",
	                    "getAllCustomers", this.enquiryVO);
	     	   this.customersList = responseVO.getCustomersList();
    	   //}
     	   return this.customersList;
     	}catch(BusinessException be){
     		logger.error(be, "Exception [" + be
                     + "] encountered while saving customer details");
                 FacesContext.getCurrentInstance().addMessage(
                     "ERROR_CUSTOMER_LIST_RETRIEVAL",
                     new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                         "Unexpected error encountered, please try again after sometime"));
                 return null;
     	}catch(SystemException se){
     		logger.error(se, "Exception [" + se
                     + "] encountered while saving customer details");
                 FacesContext.getCurrentInstance().addMessage(
                     "ERROR_CUSTOMERMASTER_SAVE",
                     new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                         "Unexpected error encountered, please try again after sometime"));
                 return null;
     	}
     
    }
    
	@Override
	protected void reinitializeBeanFields() {
		this.enquiryVO = new EnquiryVO();
	}
}