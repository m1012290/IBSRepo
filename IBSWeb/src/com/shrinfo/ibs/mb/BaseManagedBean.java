/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.ui.customcomponent.UWDetailsComponent;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.EnquiryType;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.TaskVO;

/**
 * @author Sunil Kumar
 */
public abstract class BaseManagedBean implements Serializable {
    
    Logger logger = Logger.getLogger(BaseManagedBean.class);

	private static final long serialVersionUID = -4604529567842501787L;
	private Map<String,String> titles = new HashMap<String, String>();
	private Map<String,String> custCategories = new HashMap<String, String>();
	private Map<String,String> custClassifications = new HashMap<String, String>();
	private Map<String,String> salutations=new HashMap<String, String>();
	private Map<String,EnquiryType>  enquiryTypes= new HashMap<String, EnquiryType>();
	private Map<String, String> products = new HashMap<String, String>();
	private Map<String, String> uwFieldValueTypes = new HashMap<String, String>();

	private Map<String, String> usersList = new HashMap<String, String>();
	private String referralDesc = new String();
	private String saveFromReferralDialog;
	private Long assigneeUser;
	private AppFlow appFlow;

	public Map<String, String> getTitles() {
		return this.titles;
	}


	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
	}


	public Map<String, String> getCustCategories() {
		return this.custCategories;
	}


	public void setCustCategories(Map<String, String> custCategories) {
		this.custCategories = custCategories;
	}


	public Map<String, String> getCustClassifications() {
		return this.custClassifications;
	}


	public void setCustClassifications(Map<String, String> custClassifications) {
		this.custClassifications = custClassifications;
	}


	public Map<String, String> getSalutations() {
		return this.salutations;
	}


	public void setSalutations(Map<String, String> salutations) {
		this.salutations = salutations;
	}

	public Map<String, EnquiryType> getEnquiryTypes() {
		return this.enquiryTypes;
	}


	public void setEnquiryTypes(Map<String, EnquiryType> enquiryTypes) {
		this.enquiryTypes = enquiryTypes;
	}


	public Map<String, String> getProducts() {
		return this.products;
	}


	public void setProducts(Map<String, String> products) {
		this.products = products;
	}

	public String getReferralDesc() {
		return this.referralDesc;
	}

	public void setReferralDesc(String referralDesc) {
		this.referralDesc = referralDesc;
	}



	public Map<String, String> getUsersList() {
		return this.usersList;
	}



	public void setUsersList(Map<String, String> usersList) {
		this.usersList = usersList;
	}



	public String getSaveFromReferralDialog() {
		return this.saveFromReferralDialog;
	}



	public void setSaveFromReferralDialog(String saveFromReferralDialog) {
		this.saveFromReferralDialog = saveFromReferralDialog;
	}



	public Long getAssigneeUser() {
		return this.assigneeUser;
	}



	public void setAssigneeUser(Long assigneeUser) {
		this.assigneeUser = assigneeUser;
	}


    
    public Map<String, String> getUwFieldValueTypes() {
        return uwFieldValueTypes;
    }


    
    public void setUwFieldValueTypes(Map<String, String> uwFieldValueTypes) {
        this.uwFieldValueTypes = uwFieldValueTypes;
    }


    public AppFlow getAppFlow() {
		return appFlow;
	}


	public void setAppFlow(AppFlow appFlow) {
		this.appFlow = appFlow;
	}


	//public constructor
	public BaseManagedBean(){
		this.titles.put("Business Executive ", "Business Executive");
		this.titles.put(" Govt Servant", "Govt Servant");
		this.titles.put(" Private Sector", "Private Sector");

		this.custCategories.put("Retail", "Retail");
		this.custCategories.put("Corporate","Corporate");

		this.custClassifications.put("Govt Limited", "Govt Limited");
		this.custClassifications.put("Pvt Limited","Pvt Limited");
		this.custClassifications.put("Public Limited", "Public Limited");

		this.salutations.put("Mr", "Mr");
		this.salutations.put("Mrs", "Mrs");
		this.salutations.put("M/S", "M/S");

		this.enquiryTypes.put("Policy", EnquiryType.POLICY);
		this.enquiryTypes.put("Endorsement", EnquiryType.ENDORSEMENT);
		this.enquiryTypes.put("Renewal",EnquiryType.RENEWAL);
		this.enquiryTypes.put("Claim", EnquiryType.CLAIMS);

		this.products = MasterDataRetrievalUtil.getProductDetails();
		this.usersList = MasterDataRetrievalUtil.getAvailableUsers();
		uwFieldValueTypes.put(AppConstants.UW_FIELD_VALUE_TYPE_NUMERIC, AppConstants.UW_FIELD_VALUE_TYPE_NUMERIC);
		uwFieldValueTypes.put(AppConstants.UW_FIELD_VALUE_TYPE_CHARACTERS, AppConstants.UW_FIELD_VALUE_TYPE_CHARACTERS);
		uwFieldValueTypes.put(AppConstants.UW_FIELD_VALUE_TYPE_ALPHANUMERIC, AppConstants.UW_FIELD_VALUE_TYPE_ALPHANUMERIC);
		uwFieldValueTypes.put(AppConstants.UW_FIELD_VALUE_TYPE_DATE, AppConstants.UW_FIELD_VALUE_TYPE_DATE);
	}

	//to be implemented by each of the child beans to reinitialize the state of declared instance fields
	protected abstract void reinitializeBeanFields();

	/**
	 * 
	 * @param uwFieldVO
	 * @return
	 */
	public String getComponentClientId(ProductUWFieldVO uwFieldVO){
		return this.getComponentClientId(uwFieldVO, null);
	}

	/**
	 * 
	 * @param uwFieldVO
	 * @return
	 */
	public String getComponentClientId(ProductUWFieldVO uwFieldVO, String prefix){
		if(Utils.isEmpty(prefix)) {
			prefix = new String();
		}
		prefix = prefix + "field_";
		String componentClientId = null;
		if(uwFieldVO.getFieldType().equalsIgnoreCase("datepicker")){
			componentClientId = Utils.concat(prefix,String.valueOf(uwFieldVO.getFieldOrder()), "_input");
		}else{
			componentClientId = Utils.concat(prefix,String.valueOf(uwFieldVO.getFieldOrder()));
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
	
	public TaskVO checkReferral(TaskVO taskVO) {
		return checkReferral(taskVO, 3);
	}
	
	/**
	 * 
	 * @param taskVO
	 * @param statusCode
	 * @return
	 */
	public TaskVO checkReferral(TaskVO taskVO, Integer statusCode) {
	    if(null == statusCode || 3 != statusCode) {
	        return null;
	    }
        TaskVO userTask= null;
        try{
            userTask = (TaskVO) ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "getTask", taskVO);
        }catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(
                    "ERROR_TASK_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "Error retrieving referral task details, please try again after sometime"));
            logger.error(ex, "error retreiving task details");
        }
        return userTask;
    }
	/**
	 * 
	 * @param userVO : Logged in user details
	 * @param taskVO : contains enquiry details
	 * @param statusCode : status of transaction. if 3, then status=referral
	 * @return
	 */
	public TaskVO checkReferral(UserVO userVO, TaskVO taskVO, Integer statusCode) {
        if(null == statusCode || 3 != statusCode) {
            return null;
        }
        
        /*
        try{
            userTask = (TaskVO) ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "getTask", taskVO);
        }catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(
                    "ERROR_TASK_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "Error retrieving referral task details, please try again after sometime"));
            ex.printStackTrace();
        }*/
        TaskVO userTask = executeTaskSvcForTaskDetails(taskVO);
        if(Utils.isEmpty(userTask)) {
            return null;
        }
        if(userTask.getAssigneeUser().getUserId().longValue() != userVO.getUserId().longValue()) {
            return userTask;
        }
        return null;
    }
	
	public TaskVO executeTaskSvcForTaskDetails(TaskVO taskVO){
		TaskVO userTask= null;
        try{
            userTask = (TaskVO) ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "getTask", taskVO);
        }catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(
                    "ERROR_TASK_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "Error retrieving referral task details, please try again after sometime"));
            ex.printStackTrace();
            return null;
        }
        return userTask;
	}
	
	
	public boolean validateUWFieldResponseIsEmpty(ProductUWFieldVO uwFieldVO){
	    boolean isEmpty = false;
	    if("Y".equalsIgnoreCase(uwFieldVO.getIsMandatory()) && Utils.isEmpty(uwFieldVO.getResponse())){
	        isEmpty = true;
            FacesContext.getCurrentInstance().addMessage(
                "",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, Utils.concat("Please enter value for ", uwFieldVO.getFieldName()),
                    Utils.concat("Please enter value for ", uwFieldVO.getFieldName())));
        }
	    return isEmpty;
	}
	
	public boolean validateUWFieldIsNumeric(ProductUWFieldVO uwFieldVO){
	    return Utils.isNumber(uwFieldVO.getResponse());
	}
	
	public boolean validateUWFieldIsDate(ProductUWFieldVO uwFieldVO){
	    return Utils.isValidDate(uwFieldVO.getResponse());
	}
}