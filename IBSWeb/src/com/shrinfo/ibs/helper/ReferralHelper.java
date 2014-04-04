/*package com.shrinfo.ibs.helper;

import java.math.BigDecimal;

import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.vo.business.ReferalTaskVO;

*//**
 * 
 * @author Rahul Reddy
 * 
 *//*

public class ReferralHelper {

    *//**
     * 
     * @param referralType :Values- QUOTESLIP, POLICY
     * @param flow : values- EDIT, NORMAL
     * @param assignedBy :
     * @param assignedTo
     * @param amount : amount entered during quote-slip generation or policy generation
     * @return
     *//*
    public ReferalTaskVO applyReferral(String referralType, String flow, UserVO assignedBy,
            UserVO assignedTo, BigDecimal amount) {

        ReferalTaskVO referalTask = null;

        if (!isReferralApplicable(referralType, flow, assignedBy, amount)) {
            referalTask = new ReferalTaskVO();
            referalTask.setReferralApplicale(false);

        }

        referalTask = refer(referralType, flow, assignedBy, assignedTo, amount);


        return referalTask;
    }

    *//**
     * 
     * @param referralType :Values- QUOTESLIP, POLICY
     * @param flow : values- EDIT, NORMAL
     * @param assignedBy :
     * @param amount : amount entered during quote-slip generation or policy generation
     * @return
     *//*
    private Boolean isReferralApplicable(String referralType, String flow, UserVO assignedBy,
            BigDecimal amount) {

        return false;
    }

    *//**
     * 
     * @param referralType
     * @param flow
     * @param assignedBy
     * @param assignedTo
     * @param amount
     * @return
     *//*
    private ReferalTaskVO refer(String referralType, String flow, UserVO assignedBy,
            UserVO assignedTo, BigDecimal amount) {

        return null;
    }

}
*/