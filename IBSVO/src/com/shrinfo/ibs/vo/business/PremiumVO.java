/**
 * 
 */
package com.shrinfo.ibs.vo.business;

import java.math.BigDecimal;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil Kumar This class represents premium details entity in the application. This can be
 *         used to hold premium details for all the type of enquiries.
 */
public class PremiumVO extends BaseVO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5312351188275144183L;

    private BigDecimal premium;

    private BigDecimal totalPremium = BigDecimal.ZERO;

    private double discountPercentage;

    private double loadingPercentage;

    private BigDecimal commissionAmt = BigDecimal.ZERO;

    private String coverDescription;

    
	public BigDecimal getPremium() {
		return premium;
	}

	public void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	public BigDecimal getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(BigDecimal totalPremium) {
		this.totalPremium = totalPremium;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public double getLoadingPercentage() {
		return loadingPercentage;
	}

	public void setLoadingPercentage(double loadingPercentage) {
		this.loadingPercentage = loadingPercentage;
	}

	public BigDecimal getCommissionAmt() {
		return commissionAmt;
	}

	public void setCommissionAmt(BigDecimal commissionAmt) {
		this.commissionAmt = commissionAmt;
	}

	public String getCoverDescription() {
		return coverDescription;
	}

	public void setCoverDescription(String coverDescription) {
		this.coverDescription = coverDescription;
	}

   
}
