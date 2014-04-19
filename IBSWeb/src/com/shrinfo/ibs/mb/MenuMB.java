/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.util.AppConstants;


/**
 * @author Sunil kumar
 *
 */
@ManagedBean(name="menuMB")
@RequestScoped
public class MenuMB {
    public MenuMB(){
        //default constructor
    }
    
    /**
     * Redirection to home page after reinitialising all the beans which are in session
     * @return
     */
    public String redirectToHomePage(){
        try {
            //reinitialise managed beans of all the screens
            for( String beanName : Utils.getMultiValueAppConfig(AppConstants.APP_PAGES_IN_ORDER)){
                resetBeanInstanceFields(beanName);
            }
            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String redirectURL = Utils.concat(request.getContextPath(), AppConstants.HOME_PAGE_URI);
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirectURL);
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage("MENU_ERROR_REDIRECTION",new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error redirecting to home page", null));
        }
        return null;
    }
    
    // This method reinitialises instance fields on the passed bean
    private void resetBeanInstanceFields(String beanName){
       BaseManagedBean baseManagedBean = (BaseManagedBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(beanName);
       if(!Utils.isEmpty(baseManagedBean)){
           baseManagedBean.reinitializeBeanFields();
       }
    }
}