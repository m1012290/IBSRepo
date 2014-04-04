package com.shrinfo.ibs.vo.app;

import com.shrinfo.ibs.cmn.constants.CommonConstants;

/**
 * @author Sunil Kumar This Enum represents enquiry types available in the system.
 */
public enum EnquiryType {

    POLICY(CommonConstants.ENQUIRY_TYPE_POLICY),

    ENDORSEMENT(CommonConstants.ENQUIRY_TYPE_ENDORSEMENT),

    RENEWAL(CommonConstants.ENQUIRY_TYPE_RENEWAL),

    CLAIMS(CommonConstants.ENQUIRY_TYPE_CLAIMS);

    private String enquiryType;

    private EnquiryType(String enquiryType) {
        this.enquiryType = enquiryType;
    }

    /**
     * @return String which is referred in DB using this string returned by this method
     */
    public String getEnquiryType() {
        return this.enquiryType;
    }
}