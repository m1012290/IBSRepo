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
}