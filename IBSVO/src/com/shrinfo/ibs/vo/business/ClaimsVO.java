package com.shrinfo.ibs.vo.business;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public class ClaimsVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -458717498082990964L;
    
    private Long id;

    private PolicyVO policyDetails;

    private CustomerVO customerDetails;

    private InsuredVO insuredDetails;

    private Timestamp lossDateTime;

    private BigDecimal lossAmountEstimate;

    private String lossDescription;


    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }


    public PolicyVO getPolicyDetails() {
        return policyDetails;
    }


    public void setPolicyDetails(PolicyVO policyDetails) {
        this.policyDetails = policyDetails;
    }


    public CustomerVO getCustomerDetails() {
        return customerDetails;
    }


    public void setCustomerDetails(CustomerVO customerDetails) {
        this.customerDetails = customerDetails;
    }


    public InsuredVO getInsuredDetails() {
        return insuredDetails;
    }


    public void setInsuredDetails(InsuredVO insuredDetails) {
        this.insuredDetails = insuredDetails;
    }


    public Timestamp getLossDateTime() {
        return lossDateTime;
    }


    public void setLossDateTime(Timestamp lossDateTime) {
        this.lossDateTime = lossDateTime;
    }


    public BigDecimal getLossAmountEstimate() {
        return lossAmountEstimate;
    }


    public void setLossAmountEstimate(BigDecimal lossAmountEstimate) {
        this.lossAmountEstimate = lossAmountEstimate;
    }


    public String getLossDescription() {
        return lossDescription;
    }


    public void setLossDescription(String lossDescription) {
        this.lossDescription = lossDescription;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
