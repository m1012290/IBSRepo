/**
 * 
 */
package com.shrinfo.ibs.vo.app;

import com.shrinfo.ibs.gen.pojo.IbsCompany;
import com.shrinfo.ibs.gen.pojo.IbsCompanyBranch;
import com.shrinfo.ibs.gen.pojo.IbsCustomer;
import com.shrinfo.ibs.gen.pojo.IbsCustomerEnquiry;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompany;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompanyContact;
import com.shrinfo.ibs.gen.pojo.IbsInsuredMaster;



/**
 * @author Sunil Kumar This enum represents various available record types defined in the system for
 *         ex - Company User record, Customer record etc
 * 
 */
public enum RecordType {
    USER(1, IbsInsuredMaster.class), CUSTOMER(2, IbsCustomer.class), COMPANY(3, IbsCompany.class), INSURANCE_COMPANY(
            4, IbsInsuranceCompany.class), COMPANY_BRANCH(5, IbsCompanyBranch.class), PORTFOLIO(6,
            IbsInsuranceCompanyContact.class), INSURED(7, IbsInsuredMaster.class), ENQUIRY_CONTACT(
            8, IbsCustomerEnquiry.class), INS_COMPANY_CONTACT(9, IbsInsuranceCompanyContact.class);

    private int recordType;

    @SuppressWarnings("rawtypes")
    private Class pojoClass;

    private <T> RecordType(int recordType, Class<T> pojoClass) {
        this.recordType = recordType;
        this.pojoClass = pojoClass;
    }

    /**
     * 
     * @return record type int
     */
    public int getRecType() {
        return recordType;
    }

    @SuppressWarnings("rawtypes")
    public Class getPojoClass() {
        return this.pojoClass;
    }
}
