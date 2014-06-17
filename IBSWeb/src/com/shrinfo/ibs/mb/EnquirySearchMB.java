package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.SearchItemVO;
import com.shrinfo.ibs.vo.business.SearchVO;
import com.shrinfo.ibs.vo.business.StatusVO;
import com.shrinfo.ibs.vo.business.TaskVO;

@ManagedBean(name = "enquirySearchMB")
@SessionScoped
public class EnquirySearchMB extends BaseManagedBean implements Serializable {

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
    
    private TaskVO referralTask = new TaskVO();
    
    private TaskVO customTask = new TaskVO();
    
    private TaskVO selectedCustomTask = new TaskVO();

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
        this.referralTask = new TaskVO();
        this.customTask = new TaskVO();
        this.customTask.setAssigneeUser(new IBSUserVO());
    }


    public EnquirySearchMB() {
        super();
    }


    public EnquirySearchMB(String custName, String mobNumber, String emailID, String insuredName,
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


    /**
     * @return the referralTask
     */
    public TaskVO getReferralTask() {
        return referralTask;
    }


    /**
     * @param referralTask the referralTask to set
     */
    public void setReferralTask(TaskVO referralTask) {
        this.referralTask = referralTask;
    }    


    
    /**
     * @return the customTask
     */
    public TaskVO getCustomTask() {
        return customTask;
    }


    
    /**
     * @param customTask the customTask to set
     */
    public void setCustomTask(TaskVO customTask) {
        this.customTask = customTask;
    }


    
    /**
     * @return the selectedCustomTask
     */
    public TaskVO getSelectedCustomTask() {
        return selectedCustomTask;
    }


    
    /**
     * @param selectedCustomTask the selectedCustomTask to set
     */
    public void setSelectedCustomTask(TaskVO selectedCustomTask) {
        this.selectedCustomTask = selectedCustomTask;
    }
    
    public void onCustomTaskSelect(SelectEvent event) {
        TaskVO selectedTask = (TaskVO) event.getObject();
        IBSUserVO assigneeUser = new IBSUserVO();
        assigneeUser.setUserId(selectedTask.getAssigneeUser().getUserId());                
        this.customTask.setAssigneeUser(assigneeUser);
        IBSUserVO assignerUser = new IBSUserVO();
        assignerUser.setUserId(selectedTask.getAssignerUser().getUserId());
        this.customTask.setAssignerUser(assignerUser);
        this.customTask.setDesc(selectedTask.getDesc());
        this.customTask.setDueDate(selectedTask.getDueDate());
        this.customTask.setId(selectedTask.getId());
        this.customTask.setPriority(selectedTask.getPriority());
        StatusVO statusVO = new StatusVO();
        statusVO.setCode(selectedTask.getStatusVO().getCode());
        this.customTask.setStatusVO(statusVO);
        this.customTask.setTaskSectionType(selectedTask.getTaskSectionType());
        this.customTask.setTaskType(selectedTask.getTaskType());
    }
    
    public void onTaskPanelHide() {
        this.customTask = new TaskVO();
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
    
    public String saveCustomTask() {
        
        try {
            Map map=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            
            LoginMB loginMB = (LoginMB)map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
            
            Boolean validFields = Boolean.TRUE;
            //validate the task fields
            if(Utils.isEmpty(this.customTask.getDesc())){
                validFields = false;
                FacesContext.getCurrentInstance().addMessage("TASK_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Referral Desc cannot be blank", null));
            }
            if(Utils.isEmpty(this.customTask.getAssigneeUser().getUserId()) && 0 == this.customTask.getAssigneeUser().getUserId()){
                validFields = false;
                FacesContext.getCurrentInstance().addMessage("TASK_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please select a user as assignee", null));
            }
            Date currenDate = new Date();
            if(Utils.isEmpty(this.customTask.getDueDate()) || currenDate.getTime() > this.customTask.getDueDate().getTime()) {
                validFields = false;
                FacesContext.getCurrentInstance().addMessage("TASK_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please select Valid Due date", null));
            }
            
            if(!validFields) {
                return null;
            }        
            
            //construct TaskVO to save referral desc
            //StatusVO statusVO = new StatusVO();
            //statusVO.setCode(Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_ACTIVE")));//task status
            //statusVO.setDesc("Referred");
            //this.customTask.setStatusVO(statusVO);
            this.customTask.setAssignerUser(loginMB.getUserDetails());
            this.customTask.setTaskType(Integer.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_CUSTOM")));
            //this.customTask.setTaskSectionType(Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_QUOTESLIP")));
            this.saveReferralTask(this.customTask);//perform referral save task
            return super.saveReferralTask();
        }catch(Exception e) {
            FacesContext.getCurrentInstance().addMessage("TASK_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error creating custom task", null));
            return null;
        }
        
    }

    public String newEnquiry() {

        // reset the values
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getExternalContext().getSessionMap().containsKey("editCustEnqDetailsMB")) {
            fc.getExternalContext().getSessionMap().remove("editCustEnqDetailsMB");
            fc.getExternalContext().getSessionMap().remove("enquirySearchBean");
        }

        return "editenquiry";
    }
    
}
