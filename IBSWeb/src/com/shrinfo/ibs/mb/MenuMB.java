/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.util.AppConstants;


/**
 * @author Sunil kumar
 *
 */
@ManagedBean(name="menuMB")
@RequestScoped
public class MenuMB {
    private MenuModel simpleMenuModel;
    public MenuMB(){
        //default constructor
        simpleMenuModel = new DefaultMenuModel(); 
        simpleMenuModel.addElement(getItem("Home", "#{menuMB.redirectToHomePage}", 1, null));
        simpleMenuModel.addElement(getItem("Enquiry Details", null, 2, "editenquiry.xhtml"));
        simpleMenuModel.addElement(getItem("Quote Slip Details", null, 3, "quoteslip.xhtml"));
        simpleMenuModel.addElement(getItem("Closing Slip Details", null, 4, "closeslip.xhtml"));
        simpleMenuModel.addElement(getItem("Policy Details", null, 5, "policy.xhtml"));
        simpleMenuModel.addElement(getItem("Product Master", null, 6, "productmaster.xhtml"));
    }   
    
    
    public MenuModel getSimpleMenuModel() {
        return simpleMenuModel;
    }
    
    public void setSimpleMenuModel(MenuModel simpleMenuModel) {
        this.simpleMenuModel = simpleMenuModel;
    }
    
    public DefaultMenuItem getItem(String value, String cmd, int id, String url){
        DefaultMenuItem menuItem = new DefaultMenuItem();
        menuItem.setValue(value);
        menuItem.setCommand(cmd);
        menuItem.setParam("id", id);
        menuItem.setStyleClass("selected-menu-style-background");
        menuItem.setUrl(url);
        return menuItem;
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