package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.docgen.SendEmail;
import com.shrinfo.ibs.vo.business.ClaimsVO;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.DocumentListVO;
import com.shrinfo.ibs.vo.business.DocumentVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.ProductVO;

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

            this.claimsVO =
                (ClaimsVO) ServiceTaskExecutor.INSTANCE.executeSvc("claimsSvc", "getclaim",
                    this.policyVO);

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
            if (Utils.isEmpty(this.policyVO) || Utils.isEmpty(this.policyVO.getPolicyNo())
                || Utils.isEmpty(this.policyVO.getPremiumDetails().getTotalPremium())
                || Utils.isEmpty(this.policyVO.getPolicyEffectiveDate())
                || Utils.isEmpty(this.policyVO.getPolicyExpiryDate())) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_CLAIMS_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a valid policy",
                        null));
                return null;
            }
            if (Utils.isEmpty(this.claimsVO.getLossAmountEstimate())
                || Utils.isEmpty(this.claimsVO.getLossDateTime())
                || Utils.isEmpty(this.claimsVO.getLossDescription())) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_CLAIMS_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Loss date, Loss Amount, Loss description are mandatory", null));
                return null;
            }
            this.claimsVO.setPolicyDetails(this.policyVO);
            this.claimsVO.setInsuredDetails(this.policyVO.getInsuredDetails());
            CustomerVO customerVO = new CustomerVO();
            customerVO.setCustomerId(this.policyVO.getQuoteDetails().entrySet().iterator().next()
                    .getValue().getCustomerId());
            this.claimsVO.setCustomerDetails(customerVO);
            this.claimsVO =
                (ClaimsVO) ServiceTaskExecutor.INSTANCE.executeSvc("claimsSvc", "saveClaim",
                    this.claimsVO);

            ProductVO productVO =
                this.policyVO.getQuoteDetails().entrySet().iterator().next().getValue()
                        .getProductDetails();
            DocumentListVO documentListVO =
                (DocumentListVO) ServiceTaskExecutor.INSTANCE.executeSvc("productSvc",
                    "getProductDocuList", productVO);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Dear sir/mam, \n");
            for (DocumentVO documentVO : documentListVO.getDocumentVOs()) {
                stringBuffer.append("Document name:" + documentVO.getName());
                stringBuffer.append("Document format type:" + documentVO.getFormat());
                stringBuffer.append("\n");
            }

            stringBuffer.append("Regards, \n Broking company");

            String subject = "Documents to be submitted for claims";
            String body = stringBuffer.toString();
            List<String> recipientList = new ArrayList<String>();
            customerVO =
                (CustomerVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerSvc", "getCustomer",
                    customerVO);
            recipientList.add(customerVO.getContactAndAddrDets().getEmailId());

            SendEmail.sendEmail(subject, body, null, recipientList);

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
