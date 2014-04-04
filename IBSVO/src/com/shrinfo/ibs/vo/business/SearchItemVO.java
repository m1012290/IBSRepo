package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public class SearchItemVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = 733026355772901204L;

    private String customerName;

    private String customerMob;

    private String customerEmail;

    private Long customerId;

    private String insuredName;

    private Long insuredId;

    private Long enquiryNum;

    private Long quotationNum;

    private String policyNum;


    public String getCustomerName() {
        return customerName;
    }


    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getCustomerMob() {
        return customerMob;
    }


    public void setCustomerMob(String customerMob) {
        this.customerMob = customerMob;
    }


    public String getCustomerEmail() {
        return customerEmail;
    }


    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }


    public Long getCustomerId() {
        return customerId;
    }


    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public String getInsuredName() {
        return insuredName;
    }


    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }


    public Long getInsuredId() {
        return insuredId;
    }


    public void setInsuredId(Long insuredId) {
        this.insuredId = insuredId;
    }


    public Long getEnquiryNum() {
        return enquiryNum;
    }


    public void setEnquiryNum(Long enquiryNum) {
        this.enquiryNum = enquiryNum;
    }


    public Long getQuotationNum() {
        return quotationNum;
    }


    public void setQuotationNum(Long quotationNum) {
        this.quotationNum = quotationNum;
    }


    public String getPolicyNum() {
        return policyNum;
    }


    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
