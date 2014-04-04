/**
 * 
 */
package com.shrinfo.ibs.delegator;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;


/**
 * @author Sunil Kumar
 */
public enum ServiceTaskExecutor {
    INSTANCE;
    
    public BaseVO executeSvc(String svcName, String svcMethodName, BaseVO... inputArgs){
          if(Utils.isEmpty(svcName) || Utils.isEmpty(svcMethodName) || Utils.isEmpty(inputArgs)){
              throw new SystemException("serviceexecutor.insufficientinputs",null,"Insufficients input args recieved, please check values passed to ServiceTaskExecutor");  
          }
          BaseService service = (BaseService)Utils.getBean(svcName);
          return (BaseVO) service.invokeMethod(svcMethodName, (Object[])inputArgs);
    }
}