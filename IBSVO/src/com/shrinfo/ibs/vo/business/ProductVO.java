package com.shrinfo.ibs.vo.business;

import java.util.ArrayList;
import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil Kumar This class represents product details for a particular product class. This
 *         class includes details such as product name, product class, Underwriting fields
 *         applicable etc..
 */
public class ProductVO extends BaseVO {

    private static final long serialVersionUID = -1081127236877284077L;

    private String name;

    private Integer productClass;

    private String shortName;

    private Integer subClass;

    // Categorization like Retail / corporate product
    private String category;

    private List<ProductUWFieldVO> uwFieldsList = new ArrayList<ProductUWFieldVO>();

    private List<String> applicableInsCompanies =  new ArrayList<String>();
    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Integer getProductClass() {
        return productClass;
    }


    public void setProductClass(Integer productClass) {
        this.productClass = productClass;
    }


    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public Integer getSubClass() {
        return subClass;
    }


    public void setSubClass(Integer subClass) {
        this.subClass = subClass;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public List<ProductUWFieldVO> getUwFieldsList() {
        return uwFieldsList;
    }


    public void setUwFieldsList(List<ProductUWFieldVO> uwFieldsList) {
        this.uwFieldsList = uwFieldsList;
    }


    
    public List<String> getApplicableInsCompanies() {
        return applicableInsCompanies;
    }


    
    public void setApplicableInsCompanies(List<String> applicableInsCompanies) {
        this.applicableInsCompanies = applicableInsCompanies;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((productClass == null) ? 0 : productClass.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductVO other = (ProductVO) obj;
        if (productClass == null) {
            if (other.productClass != null)
                return false;
        } else if (!productClass.equals(other.productClass))
            return false;
        return true;
    }

}