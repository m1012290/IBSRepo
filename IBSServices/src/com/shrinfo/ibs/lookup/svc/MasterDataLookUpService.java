/**
 * 
 */
package com.shrinfo.ibs.lookup.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;


/**
 * @author Sunil Kumar
 */
public interface MasterDataLookUpService {
    
    public BaseVO getMasterData(BaseVO baseVO);
    
    public BaseVO getMasterDataWithLevel1Criteria(BaseVO baseVO);

}
