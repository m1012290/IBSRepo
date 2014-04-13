package com.shrinfo.ibs.vo.business;

import java.math.BigDecimal;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public class UserRolePrivilege extends BaseVO {

    private static final long serialVersionUID = -3280000395236550395L;

    private Integer id;

    private ProductVO product;

    // sum insured
    private BigDecimal minSiLimit;

    private BigDecimal maxSiLimit;

    private String emailRequisition;

    private String emailQuoteSlip;

    private String generateQuoteSlip;
    
    private String emailClosingSlip;
    
    private String generateClosingSlip;
    
    private String freeText1;

    private String freeText2;

    private String status;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the product
     */
    public ProductVO getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(ProductVO product) {
        this.product = product;
    }

    /**
     * @return the minSiLimit
     */
    public BigDecimal getMinSiLimit() {
        return minSiLimit;
    }

    /**
     * @param minSiLimit the minSiLimit to set
     */
    public void setMinSiLimit(BigDecimal minSiLimit) {
        this.minSiLimit = minSiLimit;
    }

    /**
     * @return the maxSiLimit
     */
    public BigDecimal getMaxSiLimit() {
        return maxSiLimit;
    }

    /**
     * @param maxSiLimit the maxSiLimit to set
     */
    public void setMaxSiLimit(BigDecimal maxSiLimit) {
        this.maxSiLimit = maxSiLimit;
    }

    /**
     * @return the emailRequisition
     */
    public String getEmailRequisition() {
        return emailRequisition;
    }

    /**
     * @param emailRequisition the emailRequisition to set
     */
    public void setEmailRequisition(String emailRequisition) {
        this.emailRequisition = emailRequisition;
    }

    /**
     * @return the generateClosignSlip
     */
    public String getGenerateClosingSlip() {
        return generateClosingSlip;
    }

    /**
     * @param generateClosignSlip the generateClosignSlip to set
     */
    public void setGenerateClosingSlip(String generateClosingSlip) {
        this.generateClosingSlip = generateClosingSlip;
    }

    /**
     * @return the emailQuoteSlip
     */
    public String getEmailQuoteSlip() {
        return emailQuoteSlip;
    }

    /**
     * @param emailQuoteSlip the emailQuoteSlip to set
     */
    public void setEmailQuoteSlip(String emailQuoteSlip) {
        this.emailQuoteSlip = emailQuoteSlip;
    }

    /**
     * @return the freeText1
     */
    public String getFreeText1() {
        return freeText1;
    }

    /**
     * @param freeText1 the freeText1 to set
     */
    public void setFreeText1(String freeText1) {
        this.freeText1 = freeText1;
    }

    /**
     * @return the freeText2
     */
    public String getFreeText2() {
        return freeText2;
    }

    /**
     * @param freeText2 the freeText2 to set
     */
    public void setFreeText2(String freeText2) {
        this.freeText2 = freeText2;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getGenerateQuoteSlip() {
        return generateQuoteSlip;
    }

    
    public void setGenerateQuoteSlip(String generateQuoteSlip) {
        this.generateQuoteSlip = generateQuoteSlip;
    }

    
    public String getEmailClosingSlip() {
        return emailClosingSlip;
    }

    
    public void setEmailClosingSlip(String emailClosingSlip) {
        this.emailClosingSlip = emailClosingSlip;
    }

}
