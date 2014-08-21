package com.shrinfo.ibs.mb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.AddressVO;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.ProductVO;

@ManagedBean(name = "insCompanyMB")
@SessionScoped
public class InsCompanyMB extends BaseManagedBean {

    private InsCompanyVO insCompanyVO = new InsCompanyVO();

    private ProductVO product = new ProductVO();

    private List<Entry<ProductVO, ContactVO>> productList =
        new ArrayList<Map.Entry<ProductVO, ContactVO>>(insCompanyVO.getContactForProduct()
                .entrySet());

    private ContactVO productContact = new ContactVO();

    private Boolean isCpyCompContact = Boolean.FALSE;


    /**
     * @return the insCompanyVO
     */
    public InsCompanyVO getInsCompanyVO() {
        return insCompanyVO;
    }

    /**
     * @param insCompanyVO the insCompanyVO to set
     */
    public void setInsCompanyVO(InsCompanyVO insCompanyVO) {
        this.insCompanyVO = insCompanyVO;
    }


    /**
     * @return the productContact
     */
    public ContactVO getProductContact() {
        return productContact;
    }


    /**
     * @param productContact the productContact to set
     */
    public void setProductContact(ContactVO productContact) {
        this.productContact = productContact;
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
     * @return the isCpyCompContact
     */
    public Boolean getIsCpyCompContact() {
        return isCpyCompContact;
    }


    /**
     * @param isCpyCompContact the isCpyCompContact to set
     */
    public void setIsCpyCompContact(Boolean isCpyCompContact) {
        this.isCpyCompContact = isCpyCompContact;
    }


    /**
     * @return the productList
     */
    public List<Entry<ProductVO, ContactVO>> getProductList() {
        return productList;
    }


    /**
     * @param productList the productList to set
     */
    public void setProductList(List<Entry<ProductVO, ContactVO>> productList) {
        this.productList = productList;
    }


    /**
     * @param copyCompanyContact the copyCompanyContact to set
     */
    public void copyCompanyContact() {

        if (isCpyCompContact) {
            this.productContact.setAlternateEmailId1(this.insCompanyVO.getContactAndAddrDetails()
                    .getAlternateEmailId1());
            this.productContact.setAlternateEmailId2(this.insCompanyVO.getContactAndAddrDetails()
                    .getAlternateEmailId2());
            this.productContact.setAlternateLandlineNo1(this.insCompanyVO
                    .getContactAndAddrDetails().getAlternateLandlineNo1());
            this.productContact.setAlternateLandLineNo2(this.insCompanyVO
                    .getContactAndAddrDetails().getAlternateLandLineNo2());
            this.productContact.setAlternateMobileNo1(this.insCompanyVO.getContactAndAddrDetails()
                    .getAlternateMobileNo1());
            this.productContact.setAlternateMobileNo2(this.insCompanyVO.getContactAndAddrDetails()
                    .getAlternateMobileNo2());
            this.productContact.setEmailId(this.insCompanyVO.getContactAndAddrDetails()
                    .getEmailId());

            this.productContact.setFaxNo(this.insCompanyVO.getContactAndAddrDetails().getFaxNo());
            this.productContact.setFirstName(this.insCompanyVO.getContactAndAddrDetails()
                    .getFirstName());
            this.productContact.setIsStatusActive(this.insCompanyVO.getContactAndAddrDetails()
                    .getIsStatusActive());
            this.productContact.setLandlineNo(this.insCompanyVO.getContactAndAddrDetails()
                    .getLandlineNo());
            this.productContact.setLastName(this.insCompanyVO.getContactAndAddrDetails()
                    .getLastName());

            this.productContact.setMiddleName(this.insCompanyVO.getContactAndAddrDetails()
                    .getMiddleName());
            this.productContact.setMobileNo(this.insCompanyVO.getContactAndAddrDetails()
                    .getMobileNo());
            this.productContact.setRecordType(RecordType.INS_COMPANY_CONTACT.getRecType());
            this.productContact.setTitle(this.insCompanyVO.getContactAndAddrDetails().getTitle());
            AddressVO addressVO = new AddressVO();
            AddressVO compAddress = insCompanyVO.getContactAndAddrDetails().getAddressVO();
            addressVO.setAddress(compAddress.getAddress());
            this.productContact.setAddressVO(addressVO);
        }

    }

    @Override
    protected void reinitializeBeanFields() {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @return
     */
    public String save() {

        try {
            
            this.insCompanyVO =
                    (InsCompanyVO) ServiceTaskExecutor.INSTANCE.executeSvc("companySvc", "createCompany",
                        this.insCompanyVO);  
                
                FacesContext.getCurrentInstance().addMessage(
                    "SUCCESS_USER_SAVE",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Insurance company details Saved Successfully",
                        "User details Saved Successfully"));
            
        } catch (Exception e) {
            logger.error("Exception [" + e + "] encountered while saving Ins company");
            FacesContext.getCurrentInstance().addMessage(
                "ERROR_USER_SAVE",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error while savin Insurance company",
                    "Error while savin Insurance company"));
        }
        
        return null;
    }

    /**
     * 
     * @return
     */
    public String add() {

        for (Entry<String, String> e : this.getProducts().entrySet()) {
            if (e.getValue().equals(this.product.getProductClass().toString())) {
                this.product.setName(e.getKey());
            }
        }

        insCompanyVO.getContactForProduct().put(getProductVO(this.product),
            getContactClone(this.productContact));

        productList =
            new ArrayList<Map.Entry<ProductVO, ContactVO>>(insCompanyVO.getContactForProduct()
                    .entrySet());

        return null;
    }


    /**
     * 
     * @param productVO
     * @return
     */
    private ProductVO getProductVO(ProductVO productVO) {

        ProductVO clone = new ProductVO();
        clone.setName(productVO.getName());
        clone.setProductClass(productVO.getProductClass());

        return clone;
    }

    /**
     * 
     * @param contactVO
     * @return
     */
    private ContactVO getContactClone(ContactVO contactVO) {
        ContactVO clone = new ContactVO();

        clone.setAlternateEmailId1(contactVO.getAlternateEmailId1());
        clone.setAlternateEmailId2(contactVO.getAlternateEmailId2());
        clone.setAlternateLandlineNo1(this.insCompanyVO.getContactAndAddrDetails()
                .getAlternateLandlineNo1());
        clone.setAlternateLandLineNo2(this.insCompanyVO.getContactAndAddrDetails()
                .getAlternateLandLineNo2());
        clone.setAlternateMobileNo1(contactVO.getAlternateMobileNo1());
        clone.setAlternateMobileNo2(contactVO.getAlternateMobileNo2());
        clone.setEmailId(contactVO.getEmailId());
        clone.setFaxNo(contactVO.getFaxNo());
        clone.setFirstName(contactVO.getFirstName());
        clone.setIsStatusActive(contactVO.getIsStatusActive());
        clone.setLandlineNo(contactVO.getLandlineNo());
        clone.setLastName(contactVO.getLastName());
        clone.setMiddleName(contactVO.getMiddleName());
        clone.setMobileNo(contactVO.getMobileNo());
        clone.setRecordType(RecordType.INS_COMPANY_CONTACT.getRecType());
        clone.setTitle(contactVO.getTitle());
        AddressVO addressVO = new AddressVO();
        AddressVO compAddress = insCompanyVO.getContactAndAddrDetails().getAddressVO();
        addressVO.setAddress(compAddress.getAddress());
        clone.setAddressVO(addressVO);

        return clone;
    }


}
