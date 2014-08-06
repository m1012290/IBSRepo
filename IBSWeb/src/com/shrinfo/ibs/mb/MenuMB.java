/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.util.AppConstants;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.UserRoleVO;


/**
 * @author Sunil kumar
 *
 */
@ManagedBean(name="menuMB")
@RequestScoped
public class MenuMB {
    
    //For main menu
    private MenuModel mainMenuModel;
    
    // For breadcrumb
    private MenuModel simpleMenuModel;
    public MenuMB(){
        //default constructor
        simpleMenuModel = new DefaultMenuModel(); 
        simpleMenuModel.addElement(getItem("Home", "#{menuMB.redirectToHomePage}", 1, null));
        simpleMenuModel.addElement(getItem("Enquiry Details", null, 2, "#"));
        simpleMenuModel.addElement(getItem("Quote Slip Details", null, 3, "#"));
        simpleMenuModel.addElement(getItem("Closing Slip Details", null, 4, "#"));
        simpleMenuModel.addElement(getItem("Policy Details", null, 5, "#"));
        //simpleMenuModel.addElement(getItem("Product Master", null, 6, "productmaster.xhtml"));
        
        
        mainMenuModel = new DefaultMenuModel();
        
        Map map=FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        LoginMB loginMB = (LoginMB) map.get(AppConstants.BEAN_NAME_LOGIN_PAGE);
        IBSUserVO userVO = (IBSUserVO) loginMB.getUserDetails();
        List<UserRoleVO> roleList = userVO.getRoles();
        List<String> masterTranRoles =
            Arrays.asList(Utils.getMultiValueAppConfig(AppConstants.MASTER_TRAN_ROLES));
        Boolean addMasterEntry = Boolean.FALSE;
        for (UserRoleVO role : roleList) {
            if (masterTranRoles.contains(role.getName())) {
                addMasterEntry = Boolean.TRUE;
                break;
            }
        }
        if (addMasterEntry) {
            // Add masters menu items
            DefaultSubMenu submenuMaster = new DefaultSubMenu("Masters entry");
            List<DefaultMenuItem> productMenuList = new ArrayList<DefaultMenuItem>();
            productMenuList.add(getItem("Customer Master", null, 1, "customermaster.xhtml"));
            productMenuList.add(getItem("Insured Master", null, 2, "insuredmaster.xhtml"));
            productMenuList.add(getItem("Product Master", null, 3, "productmaster.xhtml"));
            productMenuList.add(getItem("User Master", null, 4, "usermaster.xhtml"));
            productMenuList.add(getItem("Insurance Company Master", null, 5,
                "inscompanymaster.xhtml"));
            productMenuList.add(getItem("Branch Master", null, 6, "branchcompanymaster.xhtml"));

            for (DefaultMenuItem item : productMenuList) {
                submenuMaster.addElement(item);
            }

            mainMenuModel.addElement(submenuMaster);
        }        
        
        // Add claims menu item
        //mainMenuModel.addElement(getItem("Claims Entry", null, 2, "claims.xhtml"));
        // Add renewal notification menu item
        mainMenuModel.addElement(getItem("Renewal Notification", null, 3, "renewalnoticegeneration.xhtml"));
    }   
    
    
    public MenuModel getSimpleMenuModel() {
        return simpleMenuModel;
    }
    
    public void setSimpleMenuModel(MenuModel simpleMenuModel) {
        this.simpleMenuModel = simpleMenuModel;
    }
    
    
    public MenuModel getMainMenuModel() {
        return mainMenuModel;
    }

    
    public void setMainMenuModel(MenuModel mainMenuModel) {
        this.mainMenuModel = mainMenuModel;
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
        	resetBeanInstanceFields();
            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String redirectURL = Utils.concat(request.getContextPath(), AppConstants.HOME_PAGE_URI);
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirectURL);
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage("MENU_ERROR_REDIRECTION",new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error redirecting to home page", null));
        }
        return null;
    }
    
    public void resetBeanInstanceFields(){
    	//reinitialise managed beans of all the screens
        for( String beanName : Utils.getMultiValueAppConfig(AppConstants.APP_PAGES_IN_ORDER)){
            resetBeanInstanceFields(beanName);
        }
    }
    // This method reinitialises instance fields on the passed bean
    private void resetBeanInstanceFields(String beanName){
    	FacesContext fc = FacesContext.getCurrentInstance();
        Map map=fc.getExternalContext().getSessionMap();
         
        BaseManagedBean baseManagedBean = (BaseManagedBean) map.get(beanName);
        //BaseManagedBean baseManagedBean = (BaseManagedBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(beanName);
        if(!Utils.isEmpty(baseManagedBean)){
        	baseManagedBean.reinitializeBeanFields();
       }
    }
}