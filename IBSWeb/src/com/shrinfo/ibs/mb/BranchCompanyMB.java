package com.shrinfo.ibs.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.EncryptionUtil;
import com.shrinfo.ibs.vo.business.BranchVO;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.UserRoleVO;

@ManagedBean(name = "branchCompanyMB")
@SessionScoped
public class BranchCompanyMB extends BaseManagedBean {

    private BranchVO branchVO = new BranchVO();

    private ContactVO contactDetails = new ContactVO();




    public BranchVO getBranchVO() {
		return branchVO;
	}


	public void setBranchVO(BranchVO branchVO) {
		this.branchVO = branchVO;
	}


	/**
     * @return the contactDetails
     */
    public ContactVO getContactDetails() {
        return contactDetails;
    }


    /**
     * @param contactDetails the contactDetails to set
     */
    public void setContactDetails(ContactVO contactDetails) {
        this.contactDetails = contactDetails;
    }


    @Override
    protected void reinitializeBeanFields() {
        // TODO Auto-generated method stub

    }

    public String save() {
            
    	try {
       
    		this.branchVO=(BranchVO) ServiceTaskExecutor.INSTANCE.executeSvc("branchSvcBean",
                    "saveBranchDetails", this.branchVO);
        } catch (Exception ex) {
            logger.error("Exception [" + ex + "] encountered while performing save operation");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_USER_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Unexpected error encountered, please try again after sometime",
                    "Unexpected error encountered, please try again after sometime"));
            return "branchcompanymaster";
        }

        FacesContext.getCurrentInstance().addMessage(
            "SUCCESS_USER_SAVE",
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Branch Company details Saved Successfully",
                "Branch Company details Saved Successfully"));

        return "branchcompanymaster";
        
    }

    public String add() {

        return null;
    }


}
