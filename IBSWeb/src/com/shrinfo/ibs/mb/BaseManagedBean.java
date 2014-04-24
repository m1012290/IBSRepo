/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.EnquiryType;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.TaskVO;

/**
 * @author Sunil Kumar
 */
public abstract class BaseManagedBean implements Serializable {

	private static final long serialVersionUID = -4604529567842501787L;
	private Map<String,String> titles = new HashMap<String, String>();
	private Map<String,String> custCategories = new HashMap<String, String>(); 
	private Map<String,String> custClassifications = new HashMap<String, String>(); 
	private Map<String,String> salutations=new HashMap<String, String>();
	private Map<String,EnquiryType>  enquiryTypes= new HashMap<String, EnquiryType>();
	private Map<String, String> products = new HashMap<String, String>();
	
	private Map<String, String> usersList = new HashMap<String, String>();
	private String referralDesc = new String();
	private String saveFromReferralDialog;
	private Long assigneeUser;
	
	public Map<String, String> getTitles() {
		return titles;
	}


	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
	}


	public Map<String, String> getCustCategories() {
		return custCategories;
	}


	public void setCustCategories(Map<String, String> custCategories) {
		this.custCategories = custCategories;
	}


	public Map<String, String> getCustClassifications() {
		return custClassifications;
	}


	public void setCustClassifications(Map<String, String> custClassifications) {
		this.custClassifications = custClassifications;
	}


	public Map<String, String> getSalutations() {
		return salutations;
	}


	public void setSalutations(Map<String, String> salutations) {
		this.salutations = salutations;
	}

	public Map<String, EnquiryType> getEnquiryTypes() {
		return enquiryTypes;
	}


	public void setEnquiryTypes(Map<String, EnquiryType> enquiryTypes) {
		this.enquiryTypes = enquiryTypes;
	}


	public Map<String, String> getProducts() {
		return products;
	}


	public void setProducts(Map<String, String> products) {
		this.products = products;
	}

    public String getReferralDesc() {
        return referralDesc;
    }
    
    public void setReferralDesc(String referralDesc) {
        this.referralDesc = referralDesc;
    }


    
    public Map<String, String> getUsersList() {
        return usersList;
    }


    
    public void setUsersList(Map<String, String> usersList) {
        this.usersList = usersList;
    }


    
    public String getSaveFromReferralDialog() {
        return saveFromReferralDialog;
    }


    
    public void setSaveFromReferralDialog(String saveFromReferralDialog) {
        this.saveFromReferralDialog = saveFromReferralDialog;
    }


    
    public Long getAssigneeUser() {
        return assigneeUser;
    }


    
    public void setAssigneeUser(Long assigneeUser) {
        this.assigneeUser = assigneeUser;
    }


    //public constructor
	public BaseManagedBean(){
		titles.put("Business Executive ", "Business Executive");
		titles.put(" Govt Servant", "Govt Servant");
		titles.put(" Private Sector", "Private Sector");
		
		custCategories.put("Retail", "Retail");
		custCategories.put("Corporate","Corporate");
		
		custClassifications.put("Govt Limited", "Govt Limited");
		custClassifications.put("Pvt Limited","Pvt Limited");
		custClassifications.put("Public Limited", "Public Limited");
		
		salutations.put("Mr", "Mr");
		salutations.put("Mrs", "Mrs");
		salutations.put("M/S", "M/S");
		
		enquiryTypes.put("Policy", EnquiryType.POLICY);
		enquiryTypes.put("Endorsement", EnquiryType.ENDORSEMENT);
		enquiryTypes.put("Renewal",EnquiryType.RENEWAL);
		enquiryTypes.put("Claim", EnquiryType.CLAIMS);
		
		products = MasterDataRetrievalUtil.getProductDetails();
		usersList = MasterDataRetrievalUtil.getAvailableUsers();
	}
	
	//to be implemented by each of the child beans to reinitialize the state of declared instance fields
	protected abstract void reinitializeBeanFields();
	
	/**
	 * 
	 * @param uwFieldVO
	 * @return
	 */
	public String getComponentClientId(ProductUWFieldVO uwFieldVO){
        String componentClientId = null;
	    if(uwFieldVO.getFieldType().equalsIgnoreCase("datepicker")){
            componentClientId = Utils.concat("field_",String.valueOf(uwFieldVO.getFieldOrder()), "_input");
        }else{
            componentClientId = Utils.concat("field_",String.valueOf(uwFieldVO.getFieldOrder()));
        }
	    return componentClientId;
	}
	
	/**
	 * @return
	 */
	public String saveReferralTask(){
	    //to be overriden by child beans to perform referral save task
	    return "enquiry";
	}
	
	protected boolean saveReferralTask(TaskVO taskVO){
	    boolean savedSuccessfully = false;
	    if(Utils.isEmpty(taskVO)){
	        return savedSuccessfully;
	    }
	    
	    try{
	        ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "createTask", taskVO);
	        savedSuccessfully = true;
	    }catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_TASK_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "Error saving referral task details, please try again after sometime"));
            ex.printStackTrace();
            return savedSuccessfully;
    	}
	    return savedSuccessfully;
	}
}