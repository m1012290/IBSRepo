/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.EncryptionUtil;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.TaskItemsVO;
import com.shrinfo.ibs.vo.business.TaskVO;

/**
 * @author Sunil Kumar
 * 
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginMB extends BaseManagedBean implements Serializable {
    
    private static Logger logger = Logger.getLogger(LoginMB.class);

    private String userName;

    private String password;

    private UserVO userDetails;
    
    List<TaskVO> referralTaskList = new ArrayList<TaskVO>();
    
    List<TaskVO> customTaskList = new ArrayList<TaskVO>();

    private TaskItemVODataModel taskDataModel;
    
    private TaskItemVODataModel customTaskDataModel;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserVO getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserVO userDetails) {
        this.userDetails = userDetails;
    }


    /**
     * @return the taskDataModel
     */
    public TaskItemVODataModel getTaskDataModel() {
        return taskDataModel;
    }



    /**
     * @param taskDataModel the taskDataModel to set
     */
    public void setTaskDataModel(TaskItemVODataModel taskDataModel) {
        this.taskDataModel = taskDataModel;
    }    

    
    /**
     * @return the referralTaskList
     */
    public List<TaskVO> getReferralTaskList() {
        return referralTaskList;
    }

    
    /**
     * @param referralTaskList the referralTaskList to set
     */
    public void setReferralTaskList(List<TaskVO> referralTaskList) {
        this.referralTaskList = referralTaskList;
    }

    
    /**
     * @return the customTaskList
     */
    public List<TaskVO> getCustomTaskList() {
        return customTaskList;
    }

    
    /**
     * @param customTaskList the customTaskList to set
     */
    public void setCustomTaskList(List<TaskVO> customTaskList) {
        this.customTaskList = customTaskList;
    }

    
    /**
     * @return the customTaskDataModel
     */
    public TaskItemVODataModel getCustomTaskDataModel() {
        return customTaskDataModel;
    }

    
    /**
     * @param customTaskDataModel the customTaskDataModel to set
     */
    public void setCustomTaskDataModel(TaskItemVODataModel customTaskDataModel) {
        this.customTaskDataModel = customTaskDataModel;
    }

    public String submit() {
        IBSUserVO ibsUserVO = new IBSUserVO();
        ibsUserVO.setLoginName(userName);
        ibsUserVO.setPassword(password);

        try {
            this.userDetails =
                (UserVO) ServiceTaskExecutor.INSTANCE.executeSvc("loginDetsSvc", "getUserDetails",
                    ibsUserVO);
        } catch (BusinessException be) {
            //System.out.println("BusinessException [" + be + "] encountered");
            // logger.error(be, "Exception ["+ be
            // +"] encountered while retrieving available products ");
            // FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTUW_FIELDS_RETRIEVAL", new
            FacesContext.getCurrentInstance().addMessage("MESSAGE_FAILURE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username or password entered doesn't match with our records, please check the same",""));
             return null;
        } catch (SystemException se) {
            System.out.println("SystemException [" + se + "] encountered");
            // logger.error(se, "Exception ["+ se
            // +"] encountered while saving customer/enquiry details");
            // FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTUW_FIELDS_RETRIEVAL", new
            // FacesMessage(FacesMessage.SEVERITY_ERROR,null,
            // "Product underwriting fields couldn't be loaded , please try after sometime"));
            // return null;
        }

        try {
            if (password.equals(EncryptionUtil.decrypt(ibsUserVO.getPassword()))) {
                System.out.println("Authentication successful");
                FacesContext fc = FacesContext.getCurrentInstance();
                Map map = fc.getExternalContext().getSessionMap();
                loadTaskList();
                FacesContext.getCurrentInstance().getExternalContext().redirect("enquiry.xhtml");
            }
            else {
                FacesContext.getCurrentInstance().addMessage("MESSAGE_FAILURE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username or password entered doesn't match with our records, please check the same",""));
                return null;
            }
        } catch (Exception e) {
            logger.error(e, "Error logging-in");
            FacesContext.getCurrentInstance().addMessage("MESSAGE_FAILURE", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error encountered, please try again after sometime", "Unexpected error encountered, please try again after sometime"));
            return null;
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void loadTaskList() {

        FacesContext fc = FacesContext.getCurrentInstance();
        Map map = fc.getExternalContext().getSessionMap();
        LoginMB loginManageBean = (LoginMB) map.get("loginBean");
        
        TaskItemsVO itemsVO = (TaskItemsVO) ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "getTasks",
            loginManageBean.getUserDetails());
        
        this.referralTaskList = new ArrayList<TaskVO>();
        this.customTaskList = new ArrayList<TaskVO>();
        if(!Utils.isEmpty(itemsVO.getTaskVOs())) {
            for(TaskVO taskVO : itemsVO.getTaskVOs()) {
                if(taskVO.getTaskType() == Integer.parseInt(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL"))) {
                    referralTaskList.add(taskVO);
                } else if (taskVO.getTaskType() == Integer.parseInt(Utils.getSingleValueAppConfig("TASK_TYPE_CUSTOM"))) {
                    customTaskList.add(taskVO);
                }
            }
        }       
            
        this.taskDataModel = new TaskItemVODataModel(this.referralTaskList);
        this.customTaskDataModel = new TaskItemVODataModel(this.customTaskList);
    }
    
    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/faces/pages/login.xhtml");
    }

    @Override
    protected void reinitializeBeanFields() {
        // TODO Auto-generated method stub
        loadTaskList();
    }
}