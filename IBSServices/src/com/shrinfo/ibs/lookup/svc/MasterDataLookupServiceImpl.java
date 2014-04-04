/**
 * 
 */
package com.shrinfo.ibs.lookup.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.lookup.dao.MasterDataLookupDAO;


/**
 * @author Sunil Kumar
 */
public class MasterDataLookupServiceImpl extends BaseService implements MasterDataLookUpService{
    MasterDataLookupDAO masterDataLookupDAO;
    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;
        if("getMasterData".equals(methodName)){
            returnValue = getMasterData((BaseVO)args[0]);
        }
        if("getMasterDataWithLevel1Criteria".equals(methodName)){
            returnValue = getMasterDataWithLevel1Criteria((BaseVO)args[0]);
        } 
        return returnValue;
    }

    @Override
    public BaseVO getMasterData(BaseVO baseVO) {
        return masterDataLookupDAO.getMasterData(baseVO);
    }
    
	@Override
	public BaseVO getMasterDataWithLevel1Criteria(BaseVO baseVO) {
		return masterDataLookupDAO.getMasterDataWithLevel1Criteria(baseVO);
	}

    
    public MasterDataLookupDAO getMasterDataLookupDAO() {
        return masterDataLookupDAO;
    }

    
    public void setMasterDataLookupDAO(MasterDataLookupDAO masterDataLookupDAO) {
        this.masterDataLookupDAO = masterDataLookupDAO;
    }



}
