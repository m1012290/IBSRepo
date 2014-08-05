package com.shrinfo.ibs.mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.dao.utils.IOUtil;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.docgen.SendEmail;
import com.shrinfo.ibs.util.AppConstants;
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

    private DocumentListVO documentListVO = new DocumentListVO();

    private List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();

    private UploadedFile uploadedFile = new DefaultUploadedFile();

    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.policyVO = new PolicyVO();
        this.claimsVO = new ClaimsVO();
        this.policyNo = "";
        documentListVO = new DocumentListVO();
        uploadedFiles = new ArrayList<UploadedFile>();
        uploadedFile = new DefaultUploadedFile();
    }

    public ClaimsMB() {
        super();
        this.policyNo = "";
        this.policyVO = new PolicyVO();
        this.claimsVO = new ClaimsVO();
        documentListVO = new DocumentListVO();
        uploadedFiles = new ArrayList<UploadedFile>();
        uploadedFile = new DefaultUploadedFile();
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


    /**
     * @return the documentListVO
     */
    public DocumentListVO getDocumentListVO() {
        return documentListVO;
    }


    /**
     * @param documentListVO the documentListVO to set
     */
    public void setDocumentListVO(DocumentListVO documentListVO) {
        this.documentListVO = documentListVO;
    }


    /**
     * @return the uploadedFiles
     */
    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }


    /**
     * @param uploadedFiles the uploadedFiles to set
     */
    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }



    /**
     * @return the uploadedFile
     */
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }


    /**
     * @param uploadedFile the uploadedFile to set
     */
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * Search policy and claims entry based on policy number entered (not the policy ID).
     */
    public void policySearch() {

        try {
            this.policyVO = new PolicyVO();
            Map map=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
            this.policyVO = editCustEnqDetailsMB.getPolicyVO();
            this.policyNo = this.policyVO.getPolicyNo();
            this.policyVO =
                (PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("policySvc", "getPolicy",
                    this.policyVO);
            if (Utils.isEmpty(this.policyVO)) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_POLICY_SEARCH",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Policy number doen not exist in the system",
                        null));
            }

            if (!Utils.isEmpty(this.policyVO) && !Utils.isEmpty(this.policyVO.getPolicyId())) {
                this.claimsVO =
                    (ClaimsVO) ServiceTaskExecutor.INSTANCE.executeSvc("claimsSvc", "getclaim",
                        this.policyVO);
                ProductVO productVO =
                    this.policyVO.getQuoteDetails().entrySet().iterator().next().getValue()
                            .getProductDetails();
                this.documentListVO =
                    (DocumentListVO) ServiceTaskExecutor.INSTANCE.executeSvc("productSvc",
                        "getProductDocuList", productVO);
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


    /**
     * Save claim, send documents email, upload documents
     * 
     * @return
     */
    public String submit() {

        try {
            if (Utils.isEmpty(this.policyVO) || Utils.isEmpty(this.policyVO.getPolicyNo())
                /*|| Utils.isEmpty(this.policyVO.getPremiumDetails().getTotalPremium())
                || Utils.isEmpty(this.policyVO.getPolicyEffectiveDate())
                || Utils.isEmpty(this.policyVO.getPolicyExpiryDate())*/) {
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

            // If claims entry for the first time, then send email. Else do not send email.
            if (Utils.isEmpty(this.claimsVO.getId())) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Dear sir/mam, \n");
                stringBuffer
                        .append(" Claim is successfully submitted for your policy. For further processing please submit the below mentiond documents.");
                if (!Utils.isEmpty(this.documentListVO)
                    && !Utils.isEmpty(this.documentListVO.getDocumentVOs())) {
                    for (DocumentVO documentVO : this.documentListVO.getDocumentVOs()) {
                        stringBuffer.append("Document name:" + documentVO.getName());
                        stringBuffer.append("| Document format type:" + documentVO.getFormat());
                        stringBuffer.append("\n");
                    }
                }

                stringBuffer.append("Regards, \n Broking company");

                String subject = "Documents to be submitted for claims";
                String body = stringBuffer.toString();
                List<String> recipientList = new ArrayList<String>();
                customerVO =
                    (CustomerVO) ServiceTaskExecutor.INSTANCE.executeSvc("customerSvc",
                        "getCustomer", customerVO);
                recipientList.add(customerVO.getContactAndAddrDets().getEmailId());

                SendEmail.sendEmail(subject, body, null, recipientList);
            }

            // Save the claims
            this.claimsVO =
                (ClaimsVO) ServiceTaskExecutor.INSTANCE.executeSvc("claimsSvc", "saveClaim",
                    this.claimsVO);

            FacesContext.getCurrentInstance().addMessage(
                "ERROR_POLICY_SEARCH",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Claims saved successfully",
                    "Claims saved successfully"));

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
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "encountered retreiving policy details, please try again after sometime",
                    "encountered retreiving policy details, please try again after sometime"));
        }

        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {

        try {
            
            if(Utils.isEmpty(this.claimsVO) || Utils.isEmpty(this.claimsVO.getId())) {
                FacesContext.getCurrentInstance().addMessage(
                    "ERROR_CLAIMS_UPLOAD",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Please add claims before uploading documents",
                        "Please add claims before uploading documents"));
                return;
            }
            
            DocumentListVO documentListVO = new DocumentListVO();
            List<DocumentVO> docVOList = new ArrayList<DocumentVO>();

            // Document upload
            if (!Utils.isEmpty(event.getFile())) {
                DocumentVO document = new DocumentVO();
                // document.setEnquiry(this.policyDetails.getEnquiryDetails());
                document.setDocType("CLAIMS");
                document.setDocSlipId(this.claimsVO.getId());
                document.setDocument(IOUtil.getFilaDataAsArray(event.getFile().getInputstream()));
                docVOList.add(document);
            }
            documentListVO.setDocumentVOs(docVOList);
            
            ServiceTaskExecutor.INSTANCE.executeSvc("documentSvc", "saveDocumentList",
                documentListVO);
            FacesMessage message =
                    new FacesMessage("Succesful. ", event.getFile().getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            
        } catch (IOException e) {
            logger.error(e, "Exception [" + e + "] encountered uploading file");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_CLAIMS_UPLOAD",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Encountered error uploading file, please try again after sometime",
                    "Encountered error uploading file, please try again after sometime"));
        } catch (Exception e) {
            logger.error(e, "Exception [" + e + "] encountered uploading file");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_CLAIMS_UPLOAD",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Encountered error uploading file, please try again after sometime",
                    "Encountered error uploading file, please try again after sometime"));
        }

    }

    public void back() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map map = fc.getExternalContext().getSessionMap();
        MenuMB menuMB = (MenuMB) map.get("menuMB");
        menuMB.redirectToHomePage();
    }
}
