package com.shrinfo.ibs.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.EncryptionUtil;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.UserRoleVO;

@ManagedBean(name = "userMB")
@SessionScoped
public class UserMB extends BaseManagedBean implements java.io.Serializable {

    private static final Logger logger = Logger.getLogger(UserMB.class);

    private IBSUserVO userDetails = new IBSUserVO();

    private Long selectedRole;

    /**
     * @return the userDetails
     */
    public IBSUserVO getUserDetails() {
        return userDetails;
    }


    /**
     * @param userDetails the userDetails to set
     */
    public void setUserDetails(IBSUserVO userDetails) {
        this.userDetails = userDetails;
    }


    /**
     * @return the selectedRole
     */
    public Long getSelectedRole() {
        return selectedRole;
    }


    /**
     * @param selectedRole the selectedRole to set
     */
    public void setSelectedRole(Long selectedRole) {
        this.selectedRole = selectedRole;
    }


    @Override
    protected void reinitializeBeanFields() {
        // TODO Auto-generated method stub

    }


    public UserMB() {
        // TODO Auto-generated constructor stub
    }


    public String save() {

        /*
         * MenuMB menuMB = new MenuMB(); menuMB.redirectToHomePage();
         */

        try {
            List<UserRoleVO> roles = new ArrayList<UserRoleVO>();
            UserRoleVO userRoleVO = new UserRoleVO();
            userRoleVO.setId(this.selectedRole);
            roles.add(userRoleVO);
            this.userDetails.setRoles(roles);
            this.userDetails.setPassword(EncryptionUtil.encrypt(this.userDetails.getPassword()));
            this.userDetails =
                (IBSUserVO) ServiceTaskExecutor.INSTANCE.executeSvc("loginDetsSvc",
                    "saveUserDetails", this.userDetails);
        } catch (Exception ex) {
            logger.error("Exception [" + ex + "] encountered while performing save operation");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_USER_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Unexpected error encountered, please try again after sometime",
                    "Unexpected error encountered, please try again after sometime"));
            return "usermaster.xhtml";
        }

        FacesContext.getCurrentInstance().addMessage(
            "SUCCESS_USER_SAVE",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "User details Saved Successfully",
                "User details Saved Successfully"));

        return "usermaster.xhtml";
    }

}
