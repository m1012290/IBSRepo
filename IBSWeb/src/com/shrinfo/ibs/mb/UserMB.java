package com.shrinfo.ibs.mb;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.vo.business.IBSUserVO;

@ManagedBean(name = "userMB")
@SessionScoped
public class UserMB extends BaseManagedBean implements java.io.Serializable {

    private IBSUserVO userDetails = new IBSUserVO();

    private Long selectedRole;

    private Long selectedBranch;

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


    /**
     * @return the selectedBranch
     */
    public Long getSelectedBranch() {
        return selectedBranch;
    }


    /**
     * @param selectedBranch the selectedBranch to set
     */
    public void setSelectedBranch(Long selectedBranch) {
        this.selectedBranch = selectedBranch;
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

        FacesContext.getCurrentInstance().addMessage(
            "SUCCESS_USER_SAVE",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "User details Saved Successfully",
                "User details Saved Successfully"));

        return "usermaster.xhtml";
    }

}
