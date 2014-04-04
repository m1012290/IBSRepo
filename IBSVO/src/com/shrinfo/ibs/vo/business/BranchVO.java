package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public class BranchVO extends BaseVO {

    private static final long serialVersionUID = 7657694427397868180L;

    private String code;

    private String name;

    private String abbrevation;

    private CompanyVO company;

    private ContactVO inchargeContactAndAddrDets;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the abbrevation
     */
    public String getAbbrevation() {
        return abbrevation;
    }

    /**
     * @param abbrevation the abbrevation to set
     */
    public void setAbbrevation(String abbrevation) {
        this.abbrevation = abbrevation;
    }

    /**
     * @return the company
     */
    public CompanyVO getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(CompanyVO company) {
        this.company = company;
    }


    public ContactVO getInchargeContactAndAddrDets() {
        return inchargeContactAndAddrDets;
    }


    public void setInchargeContactAndAddrDets(ContactVO inchargeContactAndAddrDets) {
        this.inchargeContactAndAddrDets = inchargeContactAndAddrDets;
    }

}