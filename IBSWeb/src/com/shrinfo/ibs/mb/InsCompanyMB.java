package com.shrinfo.ibs.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.ProductVO;

@ManagedBean(name = "insCompanyMB")
@SessionScoped
public class InsCompanyMB extends BaseManagedBean {

    private InsCompanyVO insCompanyVO = new InsCompanyVO();

    private ContactVO contactDetails = new ContactVO();

    private ProductVO product = new ProductVO();

    private Boolean copyCompanyContact = Boolean.FALSE;


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
     * @return the contactDetails
     */
    public ContactVO getContactDetails() {
        return contactDetails;
    }


    /**
     * @param contactDetails the contactDetails to set
     */
    public void setContactDetails(ContactVO contactDetails) {
        this.contactDetails = contactDetails;
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
     * @return the copyCompanyContact
     */
    public Boolean getCopyCompanyContact() {
        return copyCompanyContact;
    }


    /**
     * @param copyCompanyContact the copyCompanyContact to set
     */
    public void setCopyCompanyContact(Boolean copyCompanyContact) {
        this.copyCompanyContact = copyCompanyContact;
    }

    @Override
    protected void reinitializeBeanFields() {
        // TODO Auto-generated method stub

    }

    public String save() {

        return null;
    }

    public String add() {

        return null;
    }


}
