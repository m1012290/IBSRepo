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
    private String assignerUser;
    private Long assigneeUser;
    private AppFlow appFlow;
    private Boolean navigationDisbled;
    private TaskVO taskVO = new TaskVO();
    private Boolean editVisible;
    private Boolean editApproved = Boolean.FALSE;

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


    
    public String getAssignerUser() {
        return assignerUser;
    }


    
    public void setAssignerUser(String assignerUser) {
        this.assignerUser = assignerUser;
    }


    
    public Boolean getNavigationDisbled() {
        return navigationDisbled;
    }


    
    public void setNavigationDisbled(Boolean navigationDisbled) {
        this.navigationDisbled = navigationDisbled;
    }


    
    public TaskVO getTaskVO() {
        return taskVO;
    }


    
    public void setTaskVO(TaskVO taskVO) {
        this.taskVO = taskVO;
    }


    
    public Boolean getEditVisible() {
        return editVisible;
    }


    
    public void setEditVisible(Boolean editVisible) {
        this.editVisible = editVisible;
    }


    
    public Boolean getEditApproved() {
        return editApproved;
    }


    
    public void setEditApproved(Boolean editApproved) {
        this.editApproved = editApproved;
    }
    
    public void makeTransactionEditable() {
        this.setEditApproved(Boolean.TRUE);
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
        //perform redirection to home page by invoking redirectToHomePage on menuMB. This will take care of performing reinitialization
        //task on all the beans in session
        MenuMB menuMB = new MenuMB();
        menuMB.redirectToHomePage();
        return null;
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
        TaskVO userTask= null;
        try{
            userTask = (TaskVO) ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "getTask", taskVO);
            
          //update AppFlow to REFERRAL_APPROVED in case task record status has changed to STATUS_APPROVED 
            if(!Utils.isEmpty(userTask)) {
                if(userTask.getStatusVO().getCode().intValue() == Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_APPROVED")).intValue()){
                    this.appFlow = AppFlow.REFERRAL_APPROVED;
                }
            }
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
        //update AppFlow to REFERRAL_APPROVED in case task record status has changed to STATUS_APPROVED 
        if(userTask.getStatusVO().getCode().intValue() == Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_APPROVED")).intValue()){
            this.appFlow = AppFlow.REFERRAL_APPROVED;
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
    
    public boolean validateReferralFields(){
        boolean validFields = true;
        if(Utils.isEmpty(this.referralDesc)){
            validFields = false;
            FacesContext.getCurrentInstance().addMessage("REFERRAL_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Referral Desc cannot be blank", null));
        }
        if(Utils.isEmpty(this.assignerUser)){
            validFields = false;
            FacesContext.getCurrentInstance().addMessage("REFERRAL_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Assigned by cannot be blank", null));
        }
        if(Utils.isEmpty(this.assigneeUser) || this.assigneeUser == 0l){
            validFields = false;
            FacesContext.getCurrentInstance().addMessage("REFERRAL_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please select a user as assignee", null));
        }
        // Validating if the user is selecting self as assignee user
        Map beansMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        LoginMB loginMB = (LoginMB) beansMap.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
        if( loginMB.getUserDetails().getUserId().longValue() == this.assigneeUser.longValue() ){
            validFields = false;
            FacesContext.getCurrentInstance().addMessage("REFERRAL_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please don't select yourself as assignee", null));
        }
        return validFields;
    }
}