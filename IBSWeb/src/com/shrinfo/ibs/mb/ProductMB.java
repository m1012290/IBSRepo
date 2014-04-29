/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.util.MasterDataRetrievalUtil;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;


/**
 * @author Sunil kumar
 *
 */
@ManagedBean(name="productMB")
@SessionScoped
public class ProductMB extends BaseManagedBean implements java.io.Serializable{
    private ProductVO productDetails = new ProductVO();
    private ProductUWFieldVO productUWDetails = new ProductUWFieldVO();
    private Map<String,String> insCompanies = new HashMap<String, String>();
    private List<String> selectedInsCompanies = new ArrayList<String>();
    private Integer fieldSrlNo;
    private Integer fieldLength;
    
    // This is an important method which is overriden from parent managed bean
    // this is an reinitializer block which includes all the instance fields which are bound to form
    // this method is necessary as managed beans are defined as sessionscoped beans
    protected void reinitializeBeanFields() {
        this.productDetails = new ProductVO();
        this.productUWDetails = new ProductUWFieldVO();
        this.insCompanies = new HashMap<String, String>();
        this.selectedInsCompanies = new ArrayList<String>();
        this.fieldSrlNo = null;
        this.fieldLength = null;
    }
    
    public String addAction(){
        ProductUWFieldVO productUWFieldVO = new ProductUWFieldVO();
        //productUWFieldVO.setFieldOrder(this.productUWDetails.getFieldOrder());
        productUWFieldVO.setFieldOrder(this.fieldSrlNo);
        productUWFieldVO.setFieldName(this.productUWDetails.getFieldName());
        productUWFieldVO.setFieldType(this.productUWDetails.getFieldType());
        //productUWFieldVO.setFieldLength(this.productUWDetails.getFieldLength());
        productUWFieldVO.setFieldLength(this.fieldLength);
        productUWFieldVO.setFieldValueType(this.productUWDetails.getFieldValueType());
        if( this.productDetails.getUwFieldsList().contains(productUWFieldVO) ){
            FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCT_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Underwriting field with same srl no has already been added"));
            return null;
        }
        this.productDetails.getUwFieldsList().add(productUWFieldVO);
        //reinitialize product underwriting details
        this.productUWDetails = new ProductUWFieldVO();
        this.fieldLength = null;
        this.fieldSrlNo = null;
        return null;
    }
    
    
    
    public String save(){
        try{
            this.productDetails.setIsStatusActive("Y");
            ServiceTaskExecutor.INSTANCE.executeSvc("productSvc", "saveProductDetails", this.productDetails);
        }catch(Exception ex){
            System.out.println("Exception ["+ex+"] encountered while performing save operation");
            FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCT_SAVE", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Unexpected error encountered, please try again after sometime"));
            return null;
        }
        FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCT_SAVE", new FacesMessage(FacesMessage.SEVERITY_INFO,null, "Data Saved Successfully"));
        FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();
        MenuMB menuMB = new MenuMB();
        menuMB.redirectToHomePage();
        return null;
    }
    
    public ProductVO getProductDetails() {
        return productDetails;
    }
    
    public void setProductDetails(ProductVO productDetails) {
        this.productDetails = productDetails;
    }
    
    public ProductUWFieldVO getProductUWDetails() {
        return productUWDetails;
    }
    
    public void setProductUWDetails(ProductUWFieldVO productUWDetails) {
        this.productUWDetails = productUWDetails;
    }

    
    public Map<String, String> getInsCompanies() {
        LookupVO lookupVO = new LookupVO();
        lookupVO.setCategory(AppConstants.LOOKUP_CATEGORY_INSCOMP);
        //lookupVO.setLevel1(String.valueOf(this.quoteDetailVO.getProductDetails().getProductClass()));
        LookupVO responseVO = MasterDataRetrievalUtil.getAllInsCompanies(lookupVO);
        this.insCompanies = responseVO.getCodeDescMap();
        return insCompanies;
    }

    
    public void setInsCompanies(Map<String, String> insCompanies) {
        this.insCompanies = insCompanies;
    }

    
    public List<String> getSelectedInsCompanies() {
        return selectedInsCompanies;
    }

    
    public void setSelectedInsCompanies(List<String> selectedInsCompanies) {
        this.selectedInsCompanies = selectedInsCompanies;
    }

    
    public Integer getFieldSrlNo() {
        return fieldSrlNo;
    }

    
    public void setFieldSrlNo(Integer fieldSrlNo) {
        this.fieldSrlNo = fieldSrlNo;
    }

    
    public Integer getFieldLength() {
        return fieldLength;
    }

    
    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

}
