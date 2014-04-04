/**
 * 
 */
package com.shrinfo.ibs.mb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.util.EncryptionUtil;
import com.shrinfo.ibs.vo.business.IBSUserVO;

/**
 * @author Sunil Kumar
 *
 */
@ManagedBean(name="loginBean")
@SessionScoped
public class LoginMB extends BaseManagedBean implements Serializable{
	private String userName;
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String submit(){
		IBSUserVO ibsUserVO = new IBSUserVO();
		ibsUserVO.setLoginName(userName);
		ibsUserVO.setPassword(password);
		
		try{	
			ServiceTaskExecutor.INSTANCE.executeSvc("loginDetsSvc", "getUserDetails", ibsUserVO);
		}catch(BusinessException be){
	    	System.out.println("BusinessException ["+be+"] encountered");
			//logger.error(be, "Exception ["+ be +"] encountered while retrieving available products ");
	    	//FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTUW_FIELDS_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unexpected error encountered", null));
	    	//return null;
	    }catch(SystemException se){
	    	System.out.println("SystemException ["+se+"] encountered");
	    	//logger.error(se, "Exception ["+ se +"] encountered while saving customer/enquiry details");
	    	//FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTUW_FIELDS_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Product underwriting fields couldn't be loaded , please try after sometime"));
	    	//return null;
	    }
		 
		try {
			if(password.equals(EncryptionUtil.decrypt(ibsUserVO.getPassword()))){
				System.out.println("Authentication successful");
				return "enquiry";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
