package com.shrinfo.ibs.vo.business;

import java.util.Map;

/**
 * @author Sunil kumar This class represents Insurance Company entity in the application. This class
 *         inherits company details from CompanyVO, however additionally contact details product
 *         wise would also be available.
 */
public class InsCompanyVO extends CompanyVO {

    private static final long serialVersionUID = 2860672426234918139L;

    private Map<ProductVO, ContactVO> contactForProduct;

    /**
     * @return Map consisting of contacts for each of the products portfolio
     */
    public Map<ProductVO, ContactVO> getContactForProduct() {
        return contactForProduct;
    }

    /**
     * 
     * @param contactForProduct
     */
    public void setContactForProduct(Map<ProductVO, ContactVO> contactForProduct) {
        this.contactForProduct = contactForProduct;
    }
}