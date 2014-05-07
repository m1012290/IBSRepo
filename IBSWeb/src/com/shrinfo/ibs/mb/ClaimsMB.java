package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.ClaimsVO;
import com.shrinfo.ibs.vo.business.PolicyVO;

@ManagedBean(name = "claimsMB")
@SessionScoped
public class ClaimsMB extends BaseManagedBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2909580737203256643L;

    private String policyNo;

    private PolicyVO policyVO = new PolicyVO();

    private ClaimsVO claimsVO = new ClaimsVO();

    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.policyVO = new PolicyVO();
        this.claimsVO = new ClaimsVO();
        this.policyNo = "";
    }

    public ClaimsMB() {
        super();
        this.policyNo = "";
        this.policyVO = new PolicyVO();
        this.claimsVO = new ClaimsVO();
    }


    public String getPolicyNo() {
        return policyNo;
    }


    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public PolicyVO getPolicyVO() {
        return policyVO;
    }


    public void setPolicyVO(PolicyVO policyVO) {
        this.policyVO = policyVO;
    }


    public ClaimsVO getClaimsVO() {
        return claimsVO;
    }


    public void setClaimsVO(ClaimsVO claimsVO) {
        this.claimsVO = claimsVO;
    }

    public void policySearch() {

        try {
            this.policyVO = new PolicyVO();
            this.policyVO.setPolicyNo(this.policyNo);
            this.policyVO =
                (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "getPolicy",
                    this.policyVO);
            if (Utils.isEmpty(this.policyVO)) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_POLICY_SEARCH",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Policy number doen not exist",
                        null));
            }

        } catch (BusinessException be) {
            logger.error(be, "Exception [" + be + "] encountered retreiving policy details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_POLICY_SEARCH",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error encountered retreiving policy details", null));

        } catch (SystemException se) {
            logger.error(se, "Exception [" + se + "] encountered retreiving policy details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_POLICY_SEARCH",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "encountered retreiving policy details, please try again after sometime"));
        }

    }


    public String submit() {

        try {
            this.claimsVO.setPolicyDetails(this.policyVO);
            this.claimsVO =
                    (ClaimsVO) ServiceTaskExecutor.INSTANCE.executeSvc("claimsSvc", "saveClaim",
                        this.claimsVO);

        } catch (BusinessException be) {
            logger.error(be, "Exception [" + be + "] encountered retreiving policy details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_POLICY_SEARCH",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error encountered retreiving policy details", null));

        } catch (SystemException se) {
            logger.error(se, "Exception [" + se + "] encountered retreiving policy details");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_POLICY_SEARCH",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "encountered retreiving policy details, please try again after sometime"));
        }

        return null;
    }

    public void back() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map map = fc.getExternalContext().getSessionMap();
        MenuMB menuMB = (MenuMB) map.get("menuMB");
        menuMB.redirectToHomePage();
    }
}
