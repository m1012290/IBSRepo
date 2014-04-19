/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.EncryptionUtil;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.TaskItemsVO;

/**
 * @author Sunil Kumar
 * 
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginMB extends BaseManagedBean implements Serializable {

    private String userName;

    private String password;

    private UserVO userDetails;

    private TaskItemsVO referralTaskItems = new TaskItemsVO();   

    private TaskItemVODataModel taskDataModel;

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
     * @return the referralTaskItems
     */
    public TaskItemsVO getReferralTaskItems() {
        return referralTaskItems;
    }



    /**
     * @param referralTaskItems the referralTaskItems to set
     */
    public void setReferralTaskItems(TaskItemsVO referralTaskItems) {
        this.referralTaskItems = referralTaskItems;
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
            FacesContext.getCurrentInstance().addMessage("MESSAGE_FAILURE", new FacesMessage(FacesMessage.SEVERITY_INFO,"Username entered is Incorrect",""));
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
                return "enquiry";
            }
            else {
                FacesContext.getCurrentInstance().addMessage("MESSAGE_FAILURE", new FacesMessage(FacesMessage.SEVERITY_INFO,"Password entered is does not match",""));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("MESSAGE_FAILURE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
        }
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
        this.referralTaskItems =
            (TaskItemsVO) ServiceTaskExecutor.INSTANCE.executeSvc("referralTaskSvc", "getTasks",
                loginManageBean.getUserDetails());
        this.taskDataModel = new TaskItemVODataModel(this.referralTaskItems.getTaskVOs());
    }

    @Override
    protected void reinitializeBeanFields() {
        // TODO Auto-generated method stub
        
    }
}