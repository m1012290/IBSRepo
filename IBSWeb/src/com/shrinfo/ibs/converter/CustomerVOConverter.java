package com.shrinfo.ibs.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.mb.CustomerMasterMB;
import com.shrinfo.ibs.vo.business.CustomerVO;
	 
    @FacesConverter("customerConverter")
	public class CustomerVOConverter  implements Converter {
	 
	    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
	        if(value != null && value.trim().length() > 0) {
	        	CustomerMasterMB service = (CustomerMasterMB) fc.getExternalContext().getSessionMap().get("customerMasterMB");
	            if(!Utils.isEmpty(service)) {
	            	try{
		            	List<CustomerVO> customersList = service.getCustomersList();
		            	for(CustomerVO customerVO : customersList){
		            		if( customerVO.getCustomerId().longValue() == Long.valueOf(value).longValue()){
		            			return customerVO;
		            		}
		            	}
	            	}catch(NumberFormatException nfe){
	            		//since we are converting the value which was submitted to a number we would like to catch this 
	            		//exception because we might get a string value here in case we have forceSelection set as "false"
	            		//for autocomplete tag and user enters value of his/her choice
	            		CustomerVO customerVO = new CustomerVO();
	            		customerVO.setName(value);
	            		return customerVO;
	            	}
	            }
	        }
			return null;
	    }
	 
	    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	        if(object != null) {
	            return String.valueOf(((CustomerVO) object).getCustomerId());
	        }
	        return null;
	    }   
	}
