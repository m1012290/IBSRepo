package com.shrinfo.ibs.gen.pojo;

// Generated Mar 25, 2014 2:46:55 AM by Hibernate Tools 3.4.0.CR1


import java.sql.Date;

/**
 * IbsInsuranceCompanyContact generated by hbm2java
 */
public class IbsInsuranceCompanyContact implements java.io.Serializable {


    private Long id;

    private IbsProductMaster ibsProductMaster;

    private IbsContact ibsContact;

    private IbsInsuranceCompany ibsInsuranceCompany;

    private String companyName;

    private String status;

    private Long recCreUserId;

    private Date recCreDate;

    private Long recUpdUserId;

    private Date recUpdDate;

    public IbsInsuranceCompanyContact() {}


    public IbsInsuranceCompanyContact(Long id) {
        this.id = id;
    }

    public IbsInsuranceCompanyContact(Long id, IbsProductMaster ibsProductMaster,
            IbsContact ibsContact, IbsInsuranceCompany ibsInsuranceCompany, String companyName,
            String status, Long recCreUserId, Date recCreDate, Long recUpdUserId, Date recUpdDate) {
        this.id = id;
        this.ibsProductMaster = ibsProductMaster;
        this.ibsContact = ibsContact;
        this.ibsInsuranceCompany = ibsInsuranceCompany;
        this.companyName = companyName;
        this.status = status;
        this.recCreUserId = recCreUserId;
        this.recCreDate = recCreDate;
        this.recUpdUserId = recUpdUserId;
        this.recUpdDate = recUpdDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IbsProductMaster getIbsProductMaster() {
        return this.ibsProductMaster;
    }

    public void setIbsProductMaster(IbsProductMaster ibsProductMaster) {
        this.ibsProductMaster = ibsProductMaster;
    }

    public IbsContact getIbsContact() {
        return this.ibsContact;
    }

    public void setIbsContact(IbsContact ibsContact) {
        this.ibsContact = ibsContact;
    }

    public IbsInsuranceCompany getIbsInsuranceCompany() {
        return this.ibsInsuranceCompany;
    }

    public void setIbsInsuranceCompany(IbsInsuranceCompany ibsInsuranceCompany) {
        this.ibsInsuranceCompany = ibsInsuranceCompany;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRecCreUserId() {
        return this.recCreUserId;
    }

    public void setRecCreUserId(Long recCreUserId) {
        this.recCreUserId = recCreUserId;
    }

    public Date getRecCreDate() {
        return this.recCreDate;
    }

    public void setRecCreDate(Date recCreDate) {
        this.recCreDate = recCreDate;
    }

    public Long getRecUpdUserId() {
        return this.recUpdUserId;
    }

    public void setRecUpdUserId(Long recUpdUserId) {
        this.recUpdUserId = recUpdUserId;
    }

    public Date getRecUpdDate() {
        return this.recUpdDate;
    }

    public void setRecUpdDate(Date recUpdDate) {
        this.recUpdDate = recUpdDate;
    }




}
