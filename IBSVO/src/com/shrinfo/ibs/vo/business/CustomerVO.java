package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil kumar This class represents the Customer who is going to pay for the policy. Hence
 *         an accounting entity. We would have details such as if the customer is a retail/corporate
 *         customer and so on.
 */
public class CustomerVO extends BaseVO {

    private static final long serialVersionUID = -7346107748917877189L;

    private Long customerId;

    private String salutation;

    private String name;

    // Individual / Corporate
    private String category;

    // Customer if is a corporate then there would be a group name
    // that would be the group name.
    private String group;

    // Any specific classification reqd. Eg/- Government,Private,Public Limited
    // etc
    private String classification;

    // Source of Business
    private String sourceOfBusiness;

    private ContactVO contactAndAddrDets=new ContactVO();
    
    private String salesExecutive;


    public Long getCustomerId() {
        return customerId;
    }


    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public String getSalutation() {
        return salutation;
    }


    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getGroup() {
        return group;
    }


    public void setGroup(String group) {
        this.group = group;
    }


    public String getClassification() {
        return classification;
    }


    public void setClassification(String classification) {
        this.classification = classification;
    }


    public String getSourceOfBusiness() {
        return sourceOfBusiness;
    }


    public void setSourceOfBusiness(String sourceOfBusiness) {
        this.sourceOfBusiness = sourceOfBusiness;
    }


    public ContactVO getContactAndAddrDets() {
        return contactAndAddrDets;
    }


    public void setContactAndAddrDets(ContactVO contactAndAddrDets) {
        this.contactAndAddrDets = contactAndAddrDets;
    }



    public String getSalesExecutive() {
        return salesExecutive;
    }



    public void setSalesExecutive(String salesExecutive) {
        this.salesExecutive = salesExecutive;
    }



    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}