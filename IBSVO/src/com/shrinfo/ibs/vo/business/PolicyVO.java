package com.shrinfo.ibs.vo.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public class PolicyVO extends BaseVO {

    private static final long serialVersionUID = 8127574257289256602L;

    // policy table transaction id is captured in below field
    private Long uwTransactionId;

    private Long policyId;

    private Integer policyVersion;

    private String policyNo;

    private String quoteNo;

    private Long quoteId;

    private Integer quoteNoVersion;

    private Map<InsCompanyVO, QuoteDetailVO> quoteDetails;

    private InsuredVO insuredDetails;

    private EnquiryVO enquiryDetails;

    // private EnquiryType enquiryType;// enquiry type enum

    private Integer statusCode;

    private PremiumVO premiumDetails = new PremiumVO();

    private BigDecimal sumInsured;

    private Integer policyTerm;

    private String coverDescription;

    private Long policyApprovedByUserId;

    private Boolean isQuote;// will tell us if its a policy or quote

    private Date policyEffectiveDate;

    private Date policyExpiryDate;

    // Endorsement dates
    private Date endtEffectiveDate;

    private Date endtExpiryDate;

    private Integer newQuoteVersion;

    private String isQuoteConvertedToPolicy;

    private DocumentListVO docListUploaded;
    
    private AppFlow appFlow;



    public DocumentListVO getDocListUploaded() {
        return docListUploaded;
    }



    public void setDocListUploaded(DocumentListVO docListUploaded) {
        this.docListUploaded = docListUploaded;
    }


    public Long getUwTransactionId() {
        return uwTransactionId;
    }


    public void setUwTransactionId(Long uwTransactionId) {
        this.uwTransactionId = uwTransactionId;
    }


    public Long getPolicyId() {
        return policyId;
    }


    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }




    public Integer getPolicyVersion() {
        return policyVersion;
    }



    public void setPolicyVersion(Integer policyVersion) {
        this.policyVersion = policyVersion;
    }


    public String getPolicyNo() {
        return policyNo;
    }


    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }


    public String getQuoteNo() {
        return quoteNo;
    }


    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }


    public Long getQuoteId() {
        return quoteId;
    }



    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }


    public Integer getQuoteNoVersion() {
        return quoteNoVersion;
    }


    public void setQuoteNoVersion(Integer quoteNoVersion) {
        this.quoteNoVersion = quoteNoVersion;
    }


    public Map<InsCompanyVO, QuoteDetailVO> getQuoteDetails() {
        return quoteDetails;
    }


    public void setQuoteDetails(Map<InsCompanyVO, QuoteDetailVO> quoteDetails) {
        this.quoteDetails = quoteDetails;
    }


    public InsuredVO getInsuredDetails() {
        return insuredDetails;
    }


    public void setInsuredDetails(InsuredVO insuredDetails) {
        this.insuredDetails = insuredDetails;
    }


    public EnquiryVO getEnquiryDetails() {
        return enquiryDetails;
    }


    public void setEnquiryDetails(EnquiryVO enquiryDetails) {
        this.enquiryDetails = enquiryDetails;
    }


    public Integer getStatusCode() {
        return statusCode;
    }


    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }


    public PremiumVO getPremiumDetails() {
        return premiumDetails;
    }


    public void setPremiumDetails(PremiumVO premiumDetails) {
        this.premiumDetails = premiumDetails;
    }


    public BigDecimal getSumInsured() {
        return sumInsured;
    }


    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }


    public Integer getPolicyTerm() {
        return policyTerm;
    }


    public void setPolicyTerm(Integer policyTerm) {
        this.policyTerm = policyTerm;
    }


    public String getCoverDescription() {
        return coverDescription;
    }


    public void setCoverDescription(String coverDescription) {
        this.coverDescription = coverDescription;
    }


    public Long getPolicyApprovedByUserId() {
        return policyApprovedByUserId;
    }


    public void setPolicyApprovedByUserId(Long policyApprovedByUserId) {
        this.policyApprovedByUserId = policyApprovedByUserId;
    }


    public Boolean getIsQuote() {
        return isQuote;
    }


    public void setIsQuote(Boolean isQuote) {
        this.isQuote = isQuote;
    }


    public Date getPolicyEffectiveDate() {
        return policyEffectiveDate;
    }


    public void setPolicyEffectiveDate(Date policyEffectiveDate) {
        this.policyEffectiveDate = policyEffectiveDate;
    }


    public Date getPolicyExpiryDate() {
        return policyExpiryDate;
    }


    public void setPolicyExpiryDate(Date policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }


    public Date getEndtEffectiveDate() {
        return endtEffectiveDate;
    }


    public void setEndtEffectiveDate(Date endtEffectiveDate) {
        this.endtEffectiveDate = endtEffectiveDate;
    }


    public Date getEndtExpiryDate() {
        return endtExpiryDate;
    }


    public void setEndtExpiryDate(Date endtExpiryDate) {
        this.endtExpiryDate = endtExpiryDate;
    }


    public Integer getNewQuoteVersion() {
        return newQuoteVersion;
    }


    public void setNewQuoteVersion(Integer newQuoteVersion) {
        this.newQuoteVersion = newQuoteVersion;
    }


    public String getIsQuoteConvertedToPolicy() {
        return isQuoteConvertedToPolicy;
    }


    public void setIsQuoteConvertedToPolicy(String isQuoteConvertedToPolicy) {
        this.isQuoteConvertedToPolicy = isQuoteConvertedToPolicy;
    }


    public AppFlow getAppFlow() {
		return appFlow;
	}



	public void setAppFlow(AppFlow appFlow) {
		this.appFlow = appFlow;
	}



	public static long getSerialversionuid() {
        return serialVersionUID;
    }



}