/**
 * 
 */
package com.shrinfo.ibs.util;


import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.delegator.ServiceTaskExecutor;
import com.shrinfo.ibs.vo.business.LookupVO;
import com.shrinfo.ibs.vo.business.ProductVO;

/**
 * @author Sunil Kumar
 *
 */
public class MasterDataRetrievalUtil {
	private static final Logger logger = Logger.getLogger(MasterDataRetrievalUtil.class);
	private MasterDataRetrievalUtil(){
		//private constructor to supress instance creation
	}
	
	public static Map<String,String> getProductDetails(){
		LookupVO lookUpVO = new LookupVO();
		lookUpVO.setCategory(AppConstants.LOOKUP_CATEGORY_PRODUCTS);
		LookupVO svcResponseVO = null;
		try{
			svcResponseVO = (LookupVO) ServiceTaskExecutor.INSTANCE.executeSvc("masterDataLookupSvc", "getMasterData", lookUpVO);
		}catch(BusinessException be){
        	logger.error(be, "Exception ["+ be +"] encountered while retrieving available products ");
        	FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTS_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Products couldn't be loaded , please try after sometime", null));
        	return lookUpVO.getCodeDescMap();
        }catch(SystemException se){
        	logger.error(se, "Exception ["+ se +"] encountered while saving customer/enquiry details");
        	FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTS_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Products couldn't be loaded , please try after sometime"));
        	return lookUpVO.getCodeDescMap();
        }
		return svcResponseVO.getCodeDescMap();
	}
	
	public static ProductVO getProductUWDetails(ProductVO productVO){
		ProductVO svcResponseVO = null;
		try{	
			svcResponseVO = (ProductVO)ServiceTaskExecutor.INSTANCE.executeSvc("productSvc", "getProductDetails", productVO);
		}catch(BusinessException be){
	    	logger.error(be, "Exception ["+ be +"] encountered while retrieving available products ");
	    	FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTUW_FIELDS_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Product underwriting fields couldn't be loaded , please try after sometime", null));
	    	return null;
	    }catch(SystemException se){
	    	logger.error(se, "Exception ["+ se +"] encountered while saving customer/enquiry details");
	    	FacesContext.getCurrentInstance().addMessage("ERROR_PRODUCTUW_FIELDS_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Product underwriting fields couldn't be loaded , please try after sometime"));
	    	return null;
	    }
		return svcResponseVO;
	}
	
	public static LookupVO getInsCompanies(LookupVO lookupVO){
		LookupVO svcResponseVO = null;
		try{	
			svcResponseVO = (LookupVO)ServiceTaskExecutor.INSTANCE.executeSvc("masterDataLookupSvc", "getMasterDataWithLevel1Criteria", lookupVO);
		}catch(BusinessException be){
	    	logger.error(be, "Exception ["+ be +"] encountered while retrieving ins companies list ");
	    	FacesContext.getCurrentInstance().addMessage("ERROR_INSCOMPANIES_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Insurance Companies list couldn't be loaded , please try after sometime", null));
	    	return null;
	    }catch(SystemException se){
	    	logger.error(se, "Exception ["+ se +"] encountered while retrieving ins companies list");
	    	FacesContext.getCurrentInstance().addMessage("ERROR_INSCOMPANIES_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Insurance Companies list couldn't be loaded , please try after sometime"));
	    	return null;
	    }
		return svcResponseVO;
	}
	
	public static LookupVO getAllInsCompanies(LookupVO lookupVO){
        LookupVO svcResponseVO = null;
        try{    
            svcResponseVO = (LookupVO)ServiceTaskExecutor.INSTANCE.executeSvc("masterDataLookupSvc", "getMasterData", lookupVO);
        }catch(BusinessException be){
            logger.error(be, "Exception ["+ be +"] encountered while retrieving ins companies list ");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSCOMPANIES_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Insurance Companies list couldn't be loaded , please try after sometime", null));
            return null;
        }catch(SystemException se){
            logger.error(se, "Exception ["+ se +"] encountered while retrieving ins companies list");
            FacesContext.getCurrentInstance().addMessage("ERROR_INSCOMPANIES_RETRIEVAL", new FacesMessage(FacesMessage.SEVERITY_ERROR,null, "Insurance Companies list couldn't be loaded , please try after sometime"));
            return null;
        }
        return svcResponseVO;
    }
	
}