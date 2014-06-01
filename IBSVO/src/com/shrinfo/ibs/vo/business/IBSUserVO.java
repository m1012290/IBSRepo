package com.shrinfo.ibs.vo.business;

import java.util.ArrayList;
import java.util.List;

import com.shrinfo.ibs.cmn.vo.UserVO;

/**
 * 
 * @author Sunil Kumar This is application UserVO which will extend UserVO from IBSCommon. This VO
 *         has all details related to logged in user.
 */
public class IBSUserVO extends UserVO {

    private static final long serialVersionUID = -3203642603434696942L;

    private ContactVO contactDetails = new ContactVO();

    private BranchVO branchDetails = new BranchVO();

    private List<UserRoleVO> roles = new ArrayList<UserRoleVO>();

    private String isFreezed;


    public ContactVO getContactDetails() {
        return contactDetails;
    }


    public void setContactDetails(ContactVO contactDetails) {
        this.contactDetails = contactDetails;
    }


    public BranchVO getBranchDetails() {
        return branchDetails;
    }


    public void setBranchDetails(BranchVO branchDetails) {
        this.branchDetails = branchDetails;
    }


    public List<UserRoleVO> getRoles() {
        return roles;
    }


    public void setRoles(List<UserRoleVO> roles) {
        this.roles = roles;
    }

    /**
     * @return String such as "Y" or "N" for freezed and non freezed user respectively
     */
    public String getIsFreezed() {
        return isFreezed;
    }

    /**
     * Update user as freezed by using inputs either as "Y" or "N"
     * 
     * @param isFreezed
     */
    public void setIsFreezed(String isFreezed) {
        // TODO check for valid values which are only "Y" and "N"
        this.isFreezed = isFreezed;
    }

}
