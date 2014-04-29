/**
 * 
 */
package com.shrinfo.ibs.helper;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.mb.LoginMB;
import com.shrinfo.ibs.vo.app.SectionId;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;
import com.shrinfo.ibs.vo.business.TaskVO;
import com.shrinfo.ibs.vo.business.UserRolePrivilege;
import com.shrinfo.ibs.vo.business.UserRoleVO;


/**
 * @author Sunil kumar
 */
public class ReferralHelper {

    private static StringBuilder referralDesc = null;
    private ReferralHelper(){
       //suppressing instantiation of ReferralHelper
    }
    
    public static TaskVO checkForReferrals(BaseVO baseVO, SectionId sectionId){
        LoginMB loginMB = (LoginMB)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
        return checkForReferrals((PolicyVO)baseVO, loginMB.getUserDetails(), sectionId);
    }
    
    private static TaskVO checkForReferrals(PolicyVO policyVO, UserVO userDetails, SectionId sectionId){
        referralDesc = new StringBuilder();
        TaskVO taskVO = null;
        if(Utils.isEmpty(userDetails)){
            throw new BusinessException("referralhandler.invalidinput", null, "User details are null");
        }
        IBSUserVO ibsUser = (IBSUserVO) userDetails;
        Iterator<Entry<InsCompanyVO, QuoteDetailVO>> iterator = policyVO.getQuoteDetails().entrySet().iterator();
        for( UserRoleVO userRoleVO : ibsUser.getRoles() ){
            boolean checkForQuoteSlip = false;
            while(iterator.hasNext()){
                Entry<InsCompanyVO, QuoteDetailVO> entryObj = iterator.next();
                for( UserRolePrivilege userRolePrivilege : userRoleVO.getRoleProductPrivileges() ){
                    if( entryObj.getValue().getProductDetails().equals(userRolePrivilege.getProduct())){
                        switch(sectionId){
                            case CLOSINGSLIP:
                                taskVO = closingSlipScreenReferrals(entryObj.getValue(), userRolePrivilege);
                                break;
                            case ENQUIRY:
                                break;
                            case POLICY:
                                break;
                            case QUOTESLIP:
                                taskVO = quoteSlipScreenReferrals(entryObj.getValue(), userRolePrivilege);
                                //referral is being checked for quote slip screen hence break away once
                                //details are tested for one of the company records (replicated across other company records)
                                checkForQuoteSlip = true;
                                break;
                            default:
                                throw new BusinessException("referralhandler.invalidsectionid", null, "Invalid section id passed, please check the same");
                        }
                        break;
                    }
                }
                if(checkForQuoteSlip) break; //breakaway if true
            }
        }
        if(!Utils.isEmpty(taskVO) && !Utils.isEmpty(policyVO.getEnquiryDetails())){
            taskVO.setEnquiry(policyVO.getEnquiryDetails());
        }
        return taskVO;
    }
    
    private static TaskVO enquiryScreenReferrals(EnquiryVO enquiryDetails, UserRolePrivilege userRolePrivilege){
        return null;
    }
    
    private static TaskVO quoteSlipScreenReferrals(QuoteDetailVO quoteDetailVO, UserRolePrivilege userRolePrivilege){
        TaskVO taskVO = null;
        if(!Utils.isEmpty(userRolePrivilege.getGenerateQuoteSlip())){
            if(userRolePrivilege.getGenerateQuoteSlip().equalsIgnoreCase("N")){
                referralDesc.append("Your role doesn't allow you to generate quote slip for this product").append("||");
            }
        }
        if(!Utils.isEmpty( userRolePrivilege.getEmailQuoteSlip())){
            if(userRolePrivilege.getEmailQuoteSlip().equalsIgnoreCase("N")){
                referralDesc.append("Your role doesn't allow you to email quote slip for this product").append("||");
            }
        }
        if(!Utils.isEmpty(userRolePrivilege.getMaxSiLimit()) && !Utils.isEmpty(quoteDetailVO.getSumInsured())){
            if( userRolePrivilege.getMaxSiLimit().compareTo(quoteDetailVO.getSumInsured()) == -1) {
                referralDesc.append("Your role has a limit of "+userRolePrivilege.getMaxSiLimit()+ "on maximum sum insured for this product").append("||");
            }
        }
        if(!Utils.isEmpty(userRolePrivilege.getMinSiLimit()) && !Utils.isEmpty(quoteDetailVO.getSumInsured())){
            if( userRolePrivilege.getMinSiLimit().compareTo(quoteDetailVO.getSumInsured()) == 1){
                referralDesc.append("Your role has a limit of "+userRolePrivilege.getMinSiLimit()+ " on minimum sum insured for this product").append("||");
            }
        }
        if(!Utils.isEmpty(referralDesc.toString())){
            taskVO = new TaskVO();
            taskVO.setDesc(referralDesc.toString());
            taskVO.setDocumentId(String.valueOf(quoteDetailVO.getQuoteId()));
        }
        return taskVO;
    }
    
    private static TaskVO closingSlipScreenReferrals(QuoteDetailVO quoteDetailVO, UserRolePrivilege userRolePrivilege){
        TaskVO taskVO = null;
        if(!Utils.isEmpty(userRolePrivilege.getGenerateClosingSlip())){
            if(userRolePrivilege.getGenerateClosingSlip().equalsIgnoreCase("N")){
                referralDesc.append("Your role doesn't allow you to generate closing slip for this product").append("||");
            }
        }
        if(!Utils.isEmpty(userRolePrivilege.getEmailClosingSlip())){
            if(userRolePrivilege.getEmailClosingSlip().equalsIgnoreCase("N")){
                referralDesc.append("Your role doesn't allow you to email closing slip for this product").append("||");
            }
        }
        if(Utils.isEmpty(userRolePrivilege.getMaxSiLimit()) && !Utils.isEmpty(quoteDetailVO.getSumInsured())){
            if( userRolePrivilege.getMaxSiLimit().compareTo(quoteDetailVO.getSumInsured()) == -1) {
                referralDesc.append("Your role has a limit of "+userRolePrivilege.getMaxSiLimit()+ "on maximum sum insured for this product").append("||");
            }
        }
        if(!Utils.isEmpty(userRolePrivilege.getMinSiLimit()) && !Utils.isEmpty(quoteDetailVO.getSumInsured())){
            if( userRolePrivilege.getMinSiLimit().compareTo(quoteDetailVO.getSumInsured()) == 1){
                referralDesc.append("Your role has a limit of "+userRolePrivilege.getMinSiLimit()+ " on minimum sum insured for this product").append("||");
            }
        }
        if(!Utils.isEmpty(referralDesc.toString())){
            taskVO = new TaskVO();
            taskVO.setDesc(referralDesc.toString());
            taskVO.setDocumentId(String.valueOf(quoteDetailVO.getQuoteSlipId()));
        }
        return taskVO;
    }
    
    private boolean policyScreenReferrals(PolicyVO policyVO, UserRolePrivilege userRolePrivilege){
        return false;
    }
}