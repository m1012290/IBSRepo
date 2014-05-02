package com.shrinfo.ibs.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.docgen.QuoteSlipPDFGenerator;
import com.shrinfo.ibs.helper.ReferralHelper;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.AppFlow;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.StatusVO;
import com.shrinfo.ibs.vo.business.TaskVO;

@ManagedBean(name="quoteSlipMB")
@SessionScoped
public class QuoteSlipMB  extends BaseManagedBean implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2101774372606912675L;
    private static final Logger logger = Logger.getLogger(QuoteSlipMB.class);

    public QuoteSlipMB(){
        super();
        logger.info("In QuoteSlipMB Constructor");

        //invoke load quoteslip details method in order to retrieve existing quote slip
        //details if required (existing quote record)
        this.retrieveInsuredQuoteDetails();
    }

    private List<String> selectedInsCompanies = new ArrayList<String>();
    private Map<String,String> insCompanies = new HashMap<String, String>();
    private QuoteDetailVO quoteDetailVO = new QuoteDetailVO();
    private InsuredVO insuredDetails=new InsuredVO();
    private PolicyVO policyVO = new PolicyVO();
    private Boolean renderCustomUWComponent = Boolean.FALSE;
    private Boolean screenFreeze = Boolean.FALSE;

    //This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    @Override
    protected void reinitializeBeanFields(){
        this.selectedInsCompanies = new ArrayList<String>();
        this.insCompanies = new HashMap<String, String>();
        this.quoteDetailVO = new QuoteDetailVO();
        this.insuredDetails=new InsuredVO();
        this.policyVO = new PolicyVO();
        this.renderCustomUWComponent = Boolean.FALSE;
        this.setSaveFromReferralDialog("false");
        this.screenFreeze = Boolean.FALSE;
        this.setAppFlow(null);
        this.setEditApproved(Boolean.FALSE);
        this.setNavigationDisbled(Boolean.FALSE);
    }

    public Map<String, String> getInsCompanies() {
        return this.insCompanies;
    }
    public void setInsCompanies(Map<String, String> insCompanies) {
        this.insCompanies = insCompanies;
    }
    public List<String> getSelectedInsCompanies() {
        return this.selectedInsCompanies;
    }
    public void setSelectedInsCompanies(List<String> selectedInsCompanies) {
        this.selectedInsCompanies = selectedInsCompanies;
    }

    public QuoteDetailVO getQuoteDetailVO() {
        return this.quoteDetailVO;
    }
    public void setQuoteDetailVO(QuoteDetailVO quoteDetailVO) {
        this.quoteDetailVO = quoteDetailVO;
    }
    public InsuredVO getInsuredDetails() {
        return this.insuredDetails;
    }
    public void setInsuredDetails(InsuredVO insuredDetails) {
        this.insuredDetails = insuredDetails;
    }


    public PolicyVO getPolicyVO() {
        return this.policyVO;
    }
    public void setPolicyVO(PolicyVO policyVO) {
        this.policyVO = policyVO;
    }

    public boolean isRenderCustomUWComponent() {
        return this.renderCustomUWComponent;
    }


    public void setRenderCustomUWComponent(boolean renderCustomUWComponent) {
        this.renderCustomUWComponent = renderCustomUWComponent;
    }

    
    public Boolean getScreenFreeze() {
        return screenFreeze;
    }

    
    public void setScreenFreeze(Boolean screenFreeze) {
        this.screenFreeze = screenFreeze;
    }

    public String save(){
        PolicyVO policyVO=new PolicyVO();

        try {
            FacesContext fc = FacesContext.getCurrentInstance();

            Map<String, String> requestMap = fc.getExternalContext().getRequestParameterMap();

            if(!Utils.isEmpty(requestMap)){
                ProductVO productDetails = this.quoteDetailVO.getProductDetails();
                boolean allUWFieldValid = true;
                for(ProductUWFieldVO uwField : productDetails.getUwFieldsList()){
                    String response = requestMap.get(this.getComponentClientId(uwField));
                    uwField.setResponse(response);
                    //validate underwriting answers in order to check if answer formats are in 
                    //accordance to values defined in the table
                    if(validateUWFieldResponseIsEmpty(uwField)){
                        allUWFieldValid = false;
                        
                        continue;//continue with next field value is empty though the field is defined as mandatory
                    }
                    
                    if(uwField.getFieldValueType().equalsIgnoreCase(AppConstants.UW_FIELD_VALUE_TYPE_NUMERIC)){
                        if(!validateUWFieldIsNumeric(uwField)){
                            allUWFieldValid = false;
                            FacesContext.getCurrentInstance().addMessage(
                                "",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, Utils.concat("Please enter valid numeric value for ", uwField.getFieldName()),
                                    Utils.concat("Please enter value for ", uwField.getFieldName())));
                            continue;//invalid value type entered for the field
                        }
                    }
                    
                    if(uwField.getFieldValueType().equalsIgnoreCase(AppConstants.UW_FIELD_VALUE_TYPE_DATE)){
                        if(!validateUWFieldIsDate(uwField)){
                            allUWFieldValid = false;
                            FacesContext.getCurrentInstance().addMessage(
                                "",
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, Utils.concat("Please enter valid date for ", uwField.getFieldName()),
                                    Utils.concat("Please enter value for ", uwField.getFieldName())));
                            continue;//invalid date format for the field
                        }
                    }
                }
                //recheck again if uw fields validations are through
                if(!allUWFieldValid){
                    return null;
                }
            }

            Map map=fc.getExternalContext().getSessionMap();
            EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
            EnquiryVO enquiryDetails=editCustEnqDetailsMB.getEnquiryVO();
            // Before proceeding validate if this is referral approval flow
            if(!Utils.isEmpty(editCustEnqDetailsMB.getAppFlow())){
                if(editCustEnqDetailsMB.getAppFlow().equals(AppFlow.REFERRAL_APPROVAL)){
                    /*
                    TaskVO taskVO = new TaskVO();
                    taskVO.setEnquiry(enquiryDetails);
                    TaskVO svcResponse = executeTaskSvcForTaskDetails(taskVO);
                    StatusVO statusVO = new StatusVO();
                    statusVO.setCode(Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_APPROVED")));
                    svcResponse.setStatusVO(statusVO);
                    */
                    policyVO.setAppFlow(AppFlow.REFERRAL_APPROVAL);
                }
            }
            
            policyVO.setPolicyNo(editCustEnqDetailsMB.getPolicyNum());//added
            this.insuredDetails.setCustomerDetails(enquiryDetails.getCustomerDetails());
            List<String> companiesList=null;
            policyVO.setInsuredDetails(this.insuredDetails);
            policyVO.setEnquiryDetails(enquiryDetails);
            companiesList=this.selectedInsCompanies;

            Map<InsCompanyVO, QuoteDetailVO> quoteDetMap=new HashMap<InsCompanyVO, QuoteDetailVO>();
            //later remove it
            this.quoteDetailVO.setStatusCode(1);
            this.quoteDetailVO.setCustomerId(enquiryDetails.getCustomerDetails().getCustomerId());
            //this.quoteDetailVO.setIsQuoteSlipEmailed("N");
            Iterator<String> compItr=companiesList.iterator();
            String compCodes;
            InsCompanyVO insComp;
            
            // Based on the flow set the status flag.
            if(!Utils.isEmpty(editCustEnqDetailsMB.getAppFlow())){
                if(editCustEnqDetailsMB.getAppFlow().equals(AppFlow.REFERRAL_APPROVAL)){
                    this.quoteDetailVO.setStatusCode(4);
                } else if(AppFlow.REFERRAL_APPROVED.equals(editCustEnqDetailsMB.getAppFlow())) {
                    // task is already approved. Hence set the status to active.
                    this.quoteDetailVO.setStatusCode(1);
                } else if(!Utils.isEmpty(this.getSaveFromReferralDialog()) && "true".endsWith(this.getSaveFromReferralDialog())) {
                    this.quoteDetailVO.setStatusCode(3);
                }
            }
            
            while(compItr.hasNext()){
                compCodes=compItr.next();
                insComp=new InsCompanyVO();
                insComp.setCode(compCodes);
                insComp.setName(this.insCompanies.get(compCodes));
                quoteDetMap.put(insComp, this.quoteDetailVO);
            }
            policyVO.setQuoteDetails(quoteDetMap);
            // Before performing save operation let's check if there are any referrals
            //if(!Utils.isEmpty(this.getSaveFromReferralDialog()) && "true".equalsIgnoreCase(this.getSaveFromReferralDialog())){
            if((Utils.isEmpty(this.getSaveFromReferralDialog()) || "false".equalsIgnoreCase(this.getSaveFromReferralDialog())) 
                    && (!AppFlow.REFERRAL_APPROVED.equals(this.getAppFlow()) || this.getEditApproved())){
                TaskVO taskVO = ReferralHelper.checkForReferrals(policyVO, SectionId.QUOTESLIP);
                if(!Utils.isEmpty(taskVO)){
                    this.setReferralDesc(taskVO.getDesc());
                    RequestContext context = RequestContext.getCurrentInstance();
                    if( context.isAjaxRequest() ){
                        context.addCallbackParam("referral", Boolean.TRUE);
                        return null;
                    }
                }
            }
            policyVO=(PolicyVO) ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","createQuoteSlip",policyVO);
            this.policyVO = policyVO;
            Set<Entry<InsCompanyVO, QuoteDetailVO>> quouEntries = policyVO.getQuoteDetails().entrySet();
            Iterator<Entry<InsCompanyVO, QuoteDetailVO>> it = quouEntries.iterator();
            this.quoteDetailVO = it.next().getValue();
        }catch(BusinessException be){
            logger.error(be, "Exception ["+ be +"] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unexpected error encountered", null));
            return null;
        }catch(SystemException se){
            logger.error(se, "Exception ["+ se +"] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
        }catch(Exception ex){
            logger.error(ex, "Exception ["+ ex +"] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
        }
        
        FacesContext.getCurrentInstance().addMessage("MESSAGE_SUCCESS", new FacesMessage(FacesMessage.SEVERITY_INFO,"Insured Details: Data saved successfully. Quote slip No:"+this.quoteDetailVO.getQuoteSlipId(),"Data saved successfully. Quote slip NO:"+this.quoteDetailVO.getQuoteSlipId()));

        return "SUCCESS";
    }

    /**
     * 
     * @return
     */
    public String next(){

        //perform save operation first on click of next button
        String response = this.save();
        if(Utils.isEmpty(response)){//some issue with save hence breaking it here
            return null;
        }
        //next check if quote slip mb is already available in session if so then invoke retrieveInsuredQuoteDetails method
        //on the bean
        QuotationMB quoteMB = (QuotationMB)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AppConstants.BEAN_NAME_CLOSING_SLIP_PAGE);
        if(!Utils.isEmpty(quoteMB)){
            quoteMB.loadQuotationsDetail();
        }
        return "closeslip";
    }

    /**
     * 
     * @param event
     * @return
     */
    public String retrieveProductUWFields(javax.faces.event.AjaxBehaviorEvent event){
        ProductVO productVO = new ProductVO();
        productVO.setProductClass(this.quoteDetailVO.getProductDetails().getProductClass());
        //Product UW Fields retrieval through service call
        ProductVO svcResponseVO = MasterDataRetrievalUtil.getProductUWDetails(productVO);
        this.quoteDetailVO.setProductDetails(svcResponseVO);

        LookupVO lookupVO = new LookupVO();
        lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMPPRODUCTLINK);
        lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getProductClass()));
        LookupVO responseVO = MasterDataRetrievalUtil.getInsCompanies(lookupVO);
        this.insCompanies = responseVO.getCodeDescMap();
        //enable rendering of custom underwriting component
        this.renderCustomUWComponent = Boolean.TRUE;

        return null;
    }

    /**
     * This method loads insured plus quote details while loading quote slip details
     * screen
     * @return
     */
    public String retrieveInsuredQuoteDetails(){

        // Fetch editcustenqdetailsMB from Session in order to retain search results required
        // for loading screens
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            Map beansMap=fc.getExternalContext().getSessionMap();
            EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) beansMap.get("editCustEnqDetailsMB");
            LoginMB loginManageBean = (LoginMB) beansMap.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
            //populate logged in user id to assigner user field which will be used within referral window
            this.setAssignerUser(loginManageBean.getUserDetails().getUserName());
            if(!Utils.isEmpty(editCustEnqDetailsMB.getQuoteSlipId())){
                this.renderCustomUWComponent = true;
                this.insuredDetails.setId(editCustEnqDetailsMB.getInsuredDetails().getId());

                //Retrieve quote slip details basis quote slip id retained on enquiry form through search results on home page
                this.quoteDetailVO.setQuoteSlipId(editCustEnqDetailsMB.getQuoteSlipId());
                this.quoteDetailVO.setQuoteSlipVersion(Long.valueOf(1));
                PolicyVO policyVO  =  (PolicyVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getQuoteSlipDetails",this.quoteDetailVO);

                this.insuredDetails.setId(policyVO.getInsuredDetails().getId());
                //Retrieving insured details based on insured id
                this.insuredDetails =  (InsuredVO)ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","getInsuredDetails",this.insuredDetails);

                policyVO.setPolicyNo(editCustEnqDetailsMB.getPolicyNum());//added
                Map<InsCompanyVO, QuoteDetailVO>  mapOfQuoteDets = policyVO.getQuoteDetails();

                Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
                Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
                InsCompanyVO insCompanyVO = null;
                while(iterator.hasNext()){
                    insCompanyVO = iterator.next();
                    this.selectedInsCompanies.add(insCompanyVO.getCode());
                }
                this.quoteDetailVO = mapOfQuoteDets.get(insCompanyVO);
                
                int quoteSlipSectionCode = Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_QUOTESLIP"));
                // Referral
                this.setTaskVO(new TaskVO());       

                this.getTaskVO().setEnquiry(policyVO.getEnquiryDetails());
                this.getTaskVO().setTaskSectionType(quoteSlipSectionCode);
                this.getTaskVO().setTaskType(Integer.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL")));
                this.setTaskVO(this.checkReferral(this.getTaskVO()));
                UserVO loggedInUser = loginManageBean.getUserDetails();
                this.setNavigationDisbled(Boolean.FALSE);
                this.screenFreeze = Boolean.FALSE;
                this.setEditVisible(Boolean.FALSE);
                this.setAppFlow(null);
                
                if(!Utils.isEmpty(this.getTaskVO()) /*&& 2 == this.getTaskVO().getTaskSectionType()*/) {
                    // Screen will be freezed if there is a pending referral not assigned to logged-in user . 
                    // This referral may be for current screen or the next screens. 
                    if (quoteSlipSectionCode <= this.getTaskVO().getTaskSectionType() && 3 == this.getTaskVO().getStatusVO().getCode()
                        && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                        this.screenFreeze = Boolean.TRUE;
                    }
                    // Screen will be freezed if there is a approved referral not assigned to logged-in user for current screen. 
                    if (quoteSlipSectionCode == this.getTaskVO().getTaskSectionType()
                        && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {


                        this.screenFreeze = Boolean.TRUE;                        
                    }
                    // navigation will be disabled only if referral is pending for the current screen and is not assigned to logged in user
                    if(3 == this.getTaskVO().getStatusVO().getCode() && quoteSlipSectionCode == this.getTaskVO().getTaskSectionType()
                       && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                        this.setNavigationDisbled(Boolean.TRUE);
                    }
                    // Edit button will be visible only if current screen task is approved since screen will be freezed.
                    if(3 != this.getTaskVO().getStatusVO().getCode() && quoteSlipSectionCode == this.getTaskVO().getTaskSectionType()
                       && loggedInUser.getUserId().longValue() != this.getTaskVO().getAssigneeUser().getUserId()) {
                        this.setEditVisible(Boolean.TRUE);
                    }
                    
                    if(3 == this.getTaskVO().getStatusVO().getCode() && quoteSlipSectionCode == this.getTaskVO().getTaskSectionType()) {
                        this.setAppFlow(AppFlow.REFERRAL_APPROVAL);
                    }
                    
                    if(3 != this.getTaskVO().getStatusVO().getCode() && quoteSlipSectionCode == this.getTaskVO().getTaskSectionType()) {
                        this.setAppFlow(AppFlow.REFERRAL_APPROVED);
                    }
                }

                
                // Populate insurane companies drop down also based on product class selected
                LookupVO lookupVO = new LookupVO();
                lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMPPRODUCTLINK);
                lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getUwFieldsList().get(0).getProductClass()));
                LookupVO responseVO = MasterDataRetrievalUtil.getInsCompanies(lookupVO);
                this.insCompanies = responseVO.getCodeDescMap();
                this.policyVO = policyVO;

            }
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error loading quote details, please try again after sometime"));
        }
        return "quoteslip";
    }
    @Override
    public String saveReferralTask() {
        //validate the referral window fields
        if(!validateReferralFields()){
            return null; //return as some field values are invalid on referral window.
        }
        this.setSaveFromReferralDialog("true");//highlight that save is getting invoked from referral dialog window
        this.save();//perform save operation first and then save the referral data
        
        Map map=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        
        EditCustEnqDetailsMB editCustEnqDetailsMB=(EditCustEnqDetailsMB) map.get(AppConstants.BEAN_NAME_ENQUIRY_PAGE);
        LoginMB loginMB = (LoginMB)map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
        //construct TaskVO to save referral desc
        TaskVO taskVO = new TaskVO();
        taskVO.setDesc(this.getReferralDesc());
        StatusVO statusVO = new StatusVO();
        statusVO.setCode(Integer.valueOf(Utils.getSingleValueAppConfig("STATUS_REFERRED")));//referred status
        statusVO.setDesc("Referred");
        taskVO.setStatusVO(statusVO);
        taskVO.setEnquiry(editCustEnqDetailsMB.getEnquiryVO());
        taskVO.setDocumentId(String.valueOf(getQuoteDetailVO().getQuoteSlipId()));
        taskVO.setAssignerUser(loginMB.getUserDetails());
        UserVO assigneeUser = new IBSUserVO();
        assigneeUser.setUserId(this.getAssigneeUser());
        taskVO.setAssigneeUser(assigneeUser);
        taskVO.setTaskType(Integer.valueOf(Utils.getSingleValueAppConfig("TASK_TYPE_REFERRAL")));
        taskVO.setTaskSectionType(Integer.valueOf(Utils.getSingleValueAppConfig("SECTION_ID_QUOTESLIP")));
        this.saveReferralTask(taskVO);//perform referral save task
        return super.saveReferralTask();
    }
    
    
    public String generatePDFForQuoteSlip(){

        //perform save operation first on click of next button
        String response = this.save();
        if(Utils.isEmpty(response)){//some issue with save hence breaking it here
            return null;
        }
        try {
            QuoteSlipPDFGenerator quoteSlipPDFGenerator=new QuoteSlipPDFGenerator();
            Map<InsCompanyVO, QuoteDetailVO>  mapOfQuoteDets = this.policyVO.getQuoteDetails();

            Set<InsCompanyVO> setOfInsCompanies = mapOfQuoteDets.keySet();
            Iterator<InsCompanyVO> iterator = setOfInsCompanies.iterator();
            InsCompanyVO insCompanyVO = null;
            while(iterator.hasNext()){
                insCompanyVO = iterator.next();
                //this.selectedInsCompanies.add(insCompanyVO.getCode());
                insCompanyVO =  (InsCompanyVO)ServiceTaskExecutor.INSTANCE.executeSvc("companySvc","getPolicy",insCompanyVO);
                quoteSlipPDFGenerator.generatePDF(this.quoteDetailVO, this.insuredDetails, insCompanyVO.getContactAndAddrDetails(),insCompanyVO.getName(), Utils.getSingleValueAppConfig("quoteSlipfilePath")+"_"+new Date().getTime(), Utils.getSingleValueAppConfig("imagePath"));
            }
            
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Error generating quote details document, see the error log"));
        }
        try{
            //update quote_slip_emailed to be updated within ibs_quote_slip_header
            Set<Entry<InsCompanyVO, QuoteDetailVO>> quouEntries = this.policyVO.getQuoteDetails().entrySet();
            Iterator<Entry<InsCompanyVO, QuoteDetailVO>> it = quouEntries.iterator();
            QuoteDetailVO quoteDetailVO = new QuoteDetailVO();
            quoteDetailVO.setQuoteSlipId(it.next().getValue().getQuoteSlipId());
            quoteDetailVO.setQuoteSlipVersion(1l);
            quoteDetailVO.setIsQuoteSlipEmailed("Y");
            ServiceTaskExecutor.INSTANCE.executeSvc("quoteSlipSvc","updateEmailedQuoteSlipFlag",quoteDetailVO);
            
        }catch(BusinessException be){
            logger.error(be, "Exception ["+ be +"] encountered while updating email flag for quote slip");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unexpected error encountered", null));
            return null;
        }catch(SystemException se){
            logger.error(se, "Exception ["+ se +"] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
        }catch(Exception ex){
            logger.error(ex, "Exception ["+ ex +"] encountered while saving customer/enquiry details");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSURED_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage("SUCCESS_EMAIL_MSG", new FacesMessage(FacesMessage.SEVERITY_INFO,"Quoteslipdoc is successfully emailed to the selected insurance companies",null));
        return null;
    }
    
    public String back() {
        this.setEditApproved(Boolean.FALSE);
        return "editenquiry";
    }

}