/**
 * 
 */
package com.shrinfo.ibs.lookup.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


/**
 * @author Sunil Kumar
 */
public interface MasterDataLookupDAO {
    
    public BaseVO getMasterData(BaseVO baseVO);
    
    public BaseVO getMasterDataWithLevel1Criteria(BaseVO baseVO); 
}
