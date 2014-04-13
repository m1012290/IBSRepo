/**
 * 
 */
package com.shrinfo.ibs.helper;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.vo.business.TaskVO;


/**
 * @author Sunil kumar
 */
public class ReferralDataManager {

    private ReferralDataManager(){
        //suppressing instantiation
    }
    /**
     * 
     * @param taskVO
     */
    public static void setReferralDataToContext(TaskVO taskVO){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute(AppConstants.SESSION_ATTRIBUTE_REFERRAL_DATA, taskVO);
    }

    /**
     *
     * @param context
     * @return
     */
    public static TaskVO getReferralDataFromContext(){
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (TaskVO)session.getAttribute(AppConstants.SESSION_ATTRIBUTE_REFERRAL_DATA);
    }
}