package com.shrinfo.ibs.gen.pojo;

// Generated Mar 25, 2014 2:46:55 AM by Hibernate Tools 3.4.0.CR1


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * IbsQuoteComparisionHeader generated by hbm2java
 */
public class IbsQuoteComparisionHeader implements java.io.Serializable {


    private Long id;

    private IbsStatusMaster ibsStatusMaster;

    private IbsQuoteSlipHeader ibsQuoteSlipHeader;

    private IbsProductMaster ibsProductMaster;

    private Long enquiryNo;

    private String subClass;

    private Long customerId;

    private Long insuredId;

    private String insuredName;

    private Date policyStartDate;

    private Date policyExpiryDate;

    private String recommendationSummary;

    private String quoteRecommended;

    private Long recCreUserId;

    private Date recCreDate;

    private Long recUpdUserId;

    private Date recUpdDate;

    private Set<IbsUwTransactionHeader> ibsUwTransactionHeaders =
        new HashSet<IbsUwTransactionHeader>(0);

    private Set<IbsUwTransactionDetail> ibsUwTransactionDetails =
        new HashSet<IbsUwTransactionDetail>(0);

    private Set<IbsQuoteComparisionDetail> ibsQuoteComparisionDetails =
        new HashSet<IbsQuoteComparisionDetail>(0);

    public IbsQuoteComparisionHeader() {}


    public IbsQuoteComparisionHeader(Long id) {
        this.id = id;
    }

    public IbsQuoteComparisionHeader(Long id, IbsStatusMaster ibsStatusMaster,
            IbsQuoteSlipHeader ibsQuoteSlipHeader, IbsProductMaster ibsProductMaster,
            Long enquiryNo, String subClass, Long customerId, Long insuredId, String insuredName,
            Date policyStartDate, Date policyExpiryDate, String recommendationSummary,
            String quoteRecommended, Long recCreUserId, Date recCreDate, Long recUpdUserId,
            Date recUpdDate, Set<IbsUwTransactionHeader> ibsUwTransactionHeaders,
            Set<IbsUwTransactionDetail> ibsUwTransactionDetails,
            Set<IbsQuoteComparisionDetail> ibsQuoteComparisionDetails) {
        this.id = id;
        this.ibsStatusMaster = ibsStatusMaster;
        this.ibsQuoteSlipHeader = ibsQuoteSlipHeader;
        this.ibsProductMaster = ibsProductMaster;
        this.enquiryNo = enquiryNo;
        this.subClass = subClass;
        this.customerId = customerId;
        this.insuredId = insuredId;
        this.insuredName = insuredName;
        this.policyStartDate = policyStartDate;
        this.policyExpiryDate = policyExpiryDate;
        this.recommendationSummary = recommendationSummary;
        this.quoteRecommended = quoteRecommended;
        this.recCreUserId = recCreUserId;
        this.recCreDate = recCreDate;
        this.recUpdUserId = recUpdUserId;
        this.recUpdDate = recUpdDate;
        this.ibsUwTransactionHeaders = ibsUwTransactionHeaders;
        this.ibsUwTransactionDetails = ibsUwTransactionDetails;
        this.ibsQuoteComparisionDetails = ibsQuoteComparisionDetails;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IbsStatusMaster getIbsStatusMaster() {
        return this.ibsStatusMaster;
    }

    public void setIbsStatusMaster(IbsStatusMaster ibsStatusMaster) {
        this.ibsStatusMaster = ibsStatusMaster;
    }

    public IbsQuoteSlipHeader getIbsQuoteSlipHeader() {
        return this.ibsQuoteSlipHeader;
    }

    public void setIbsQuoteSlipHeader(IbsQuoteSlipHeader ibsQuoteSlipHeader) {
        this.ibsQuoteSlipHeader = ibsQuoteSlipHeader;
    }

    public IbsProductMaster getIbsProductMaster() {
        return this.ibsProductMaster;
    }

    public void setIbsProductMaster(IbsProductMaster ibsProductMaster) {
        this.ibsProductMaster = ibsProductMaster;
    }

    public Long getEnquiryNo() {
        return this.enquiryNo;
    }

    public void setEnquiryNo(Long enquiryNo) {
        this.enquiryNo = enquiryNo;
    }

    public String getSubClass() {
        return this.subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getInsuredId() {
        return this.insuredId;
    }

    public void setInsuredId(Long insuredId) {
        this.insuredId = insuredId;
    }

    public String getInsuredName() {
        return this.insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public Date getPolicyStartDate() {
        return this.policyStartDate;
    }

    public void setPolicyStartDate(Date policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public Date getPolicyExpiryDate() {
        return this.policyExpiryDate;
    }

    public void setPolicyExpiryDate(Date policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public String getRecommendationSummary() {
        return this.recommendationSummary;
    }

    public void setRecommendationSummary(String recommendationSummary) {
        this.recommendationSummary = recommendationSummary;
    }

    public String getQuoteRecommended() {
        return this.quoteRecommended;
    }

    public void setQuoteRecommended(String quoteRecommended) {
        this.quoteRecommended = quoteRecommended;
    }

    public Long getRecCreUserId() {
        return this.recCreUserId;
    }

    public void setRecCreUserId(Long recCreUserId) {
        this.recCreUserId = recCreUserId;
    }

    public Date getRecCreDate() {
        return this.recCreDate;
    }

    public void setRecCreDate(Date recCreDate) {
        this.recCreDate = recCreDate;
    }

    public Long getRecUpdUserId() {
        return this.recUpdUserId;
    }

    public void setRecUpdUserId(Long recUpdUserId) {
        this.recUpdUserId = recUpdUserId;
    }

    public Date getRecUpdDate() {
        return this.recUpdDate;
    }

    public void setRecUpdDate(Date recUpdDate) {
        this.recUpdDate = recUpdDate;
    }

    public Set<IbsUwTransactionHeader> getIbsUwTransactionHeaders() {
        return this.ibsUwTransactionHeaders;
    }

    public void setIbsUwTransactionHeaders(Set<IbsUwTransactionHeader> ibsUwTransactionHeaders) {
        this.ibsUwTransactionHeaders = ibsUwTransactionHeaders;
    }

    public Set<IbsUwTransactionDetail> getIbsUwTransactionDetails() {
        return this.ibsUwTransactionDetails;
    }

    public void setIbsUwTransactionDetails(Set<IbsUwTransactionDetail> ibsUwTransactionDetails) {
        this.ibsUwTransactionDetails = ibsUwTransactionDetails;
    }

    public Set<IbsQuoteComparisionDetail> getIbsQuoteComparisionDetails() {
        return this.ibsQuoteComparisionDetails;
    }

    public void setIbsQuoteComparisionDetails(
            Set<IbsQuoteComparisionDetail> ibsQuoteComparisionDetails) {
        this.ibsQuoteComparisionDetails = ibsQuoteComparisionDetails;
    }




}