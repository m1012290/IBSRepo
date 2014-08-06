/**
 * 
 */
package com.shrinfo.ibs.vo.business;

import java.math.BigDecimal;
import java.util.Date;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil Kumar
 * 
 */
public class QuoteDetailVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -5498039917625223122L;

    private String quoteNo;

    private Date quoteDate;

    private Long quoteId;

    private Long quoteSlipId;

    private Long quoteSlipVersion;

    private String quoteSlipDescription;

    private ProductVO productDetails=new ProductVO();

    private Long customerId;

    private Long quoteSlipApprovedByUserId;

    private Date quoteSlipApprovalDate;

    private Date quoteSlipDate;

    private PremiumVO quoteSlipPrmDetails=new PremiumVO();

    private Integer statusCode;

    private String isQuoteSlipEmailed;

    private boolean  isQuoteRecommended;

    private String recommendationSummary;

    private String isClosingSlipGenerated;

    private String isClosingSlipEmailed;

    private String remarks;
    
    private BigDecimal sumInsured; 
    
    private String companyCode;
    
    private Integer policyTerm;
    
    private Long enquiryNum;
    
    private String companyDesc;
    
    
    /**
     * @return the companyDesc
     */
    public String getCompanyDesc() {
        return companyDesc;
    }



    
    /**
     * @param companyDesc the companyDesc to set
     */
    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }



    public Long getEnquiryNum() {
        return enquiryNum;
    }


    
    public void setEnquiryNum(Long enquiryNum) {
        this.enquiryNum = enquiryNum;
    }


    public Integer getPolicyTerm() {
		return policyTerm;
	}


	public void setPolicyTerm(Integer policyTerm) {
		this.policyTerm = policyTerm;
	}


	public String getCompanyCode() {
		return companyCode;
	}


	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}


	public BigDecimal getSumInsured() {
		return sumInsured;
	}


	public void setSumInsured(BigDecimal sumInsured) {
		this.sumInsured = sumInsured;
	}


	public String getQuoteNo() {
        return quoteNo;
    }


    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }


    public Date getQuoteDate() {
        return quoteDate;
    }


    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
    }


    public Long getQuoteId() {
        return quoteId;
    }


    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }


    public Long getQuoteSlipId() {
        return quoteSlipId;
    }


    public void setQuoteSlipId(Long quoteSlipId) {
        this.quoteSlipId = quoteSlipId;
    }


    public Long getQuoteSlipVersion() {
        return quoteSlipVersion;
    }


    public void setQuoteSlipVersion(Long quoteSlipVersion) {
        this.quoteSlipVersion = quoteSlipVersion;
    }


    public String getQuoteSlipDescription() {
        return quoteSlipDescription;
    }


    public void setQuoteSlipDescription(String quoteSlipDescription) {
        this.quoteSlipDescription = quoteSlipDescription;
    }


    public ProductVO getProductDetails() {
        return productDetails;
    }


    public void setProductDetails(ProductVO productDetails) {
        this.productDetails = productDetails;
    }


    public Long getCustomerId() {
        return customerId;
    }


    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public Long getQuoteSlipApprovedByUserId() {
        return quoteSlipApprovedByUserId;
    }


    public void setQuoteSlipApprovedByUserId(Long quoteSlipApprovedByUserId) {
        this.quoteSlipApprovedByUserId = quoteSlipApprovedByUserId;
    }


    public Date getQuoteSlipApprovalDate() {
        return quoteSlipApprovalDate;
    }


    public void setQuoteSlipApprovalDate(Date quoteSlipApprovalDate) {
        this.quoteSlipApprovalDate = quoteSlipApprovalDate;
    }


    public Date getQuoteSlipDate() {
        return quoteSlipDate;
    }


    public void setQuoteSlipDate(Date quoteSlipDate) {
        this.quoteSlipDate = quoteSlipDate;
    }


    public PremiumVO getQuoteSlipPrmDetails() {
        return quoteSlipPrmDetails;
    }


    public void setQuoteSlipPrmDetails(PremiumVO quoteSlipPrmDetails) {
        this.quoteSlipPrmDetails = quoteSlipPrmDetails;
    }


    public Integer getStatusCode() {
        return statusCode;
    }


    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }


    public String getIsQuoteSlipEmailed() {
        return isQuoteSlipEmailed;
    }


    public void setIsQuoteSlipEmailed(String isQuoteSlipEmailed) {
        this.isQuoteSlipEmailed = isQuoteSlipEmailed;
    }


    public boolean getIsQuoteRecommended() {
        return isQuoteRecommended;
    }


    public void setIsQuoteRecommended(boolean isQuoteRecommended) {
        this.isQuoteRecommended = isQuoteRecommended;
    }


    public String getRecommendationSummary() {
        return recommendationSummary;
    }


    public void setRecommendationSummary(String recommendationSummary) {
        this.recommendationSummary = recommendationSummary;
    }


    public String getIsClosingSlipGenerated() {
        return isClosingSlipGenerated;
    }


    public void setIsClosingSlipGenerated(String isClosingSlipGenerated) {
        this.isClosingSlipGenerated = isClosingSlipGenerated;
    }


    public String getIsClosingSlipEmailed() {
        return isClosingSlipEmailed;
    }


    public void setIsClosingSlipEmailed(String isClosingSlipEmailed) {
        this.isClosingSlipEmailed = isClosingSlipEmailed;
    }


    public String getRemarks() {
        return remarks;
    }


    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
        return result;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof QuoteDetailVO)) {
            return false;
        }
        QuoteDetailVO other = (QuoteDetailVO) obj;
        if (companyCode == null) {
            if (other.companyCode != null) {
                return false;
            }
        } else if (!companyCode.equals(other.companyCode)) {
            return false;
        }
        return true;
    }


}
