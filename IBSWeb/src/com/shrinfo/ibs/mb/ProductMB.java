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
    
    private ProductVO test = new ProductVO();
    
    
    public String addAction(){
        ProductUWFieldVO productUWFieldVO = new ProductUWFieldVO();
        productUWFieldVO.setFieldOrder(this.productUWDetails.getFieldOrder());
        productUWFieldVO.setFieldName(this.productUWDetails.getFieldName());
        productUWFieldVO.setFieldType(this.productUWDetails.getFieldType());
        productUWFieldVO.setFieldLength(this.productUWDetails.getFieldLength());
        this.productDetails.getUwFieldsList().add(productUWFieldVO);
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
}
