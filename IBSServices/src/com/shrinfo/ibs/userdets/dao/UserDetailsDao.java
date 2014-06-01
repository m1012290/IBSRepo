/**
 * 
 */
package com.shrinfo.ibs.userdets.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil kumar
 * 
 */
public interface UserDetailsDao {

    public BaseVO getUserDetails(BaseVO baseVO);

    public BaseVO saveUserDetails(BaseVO baseVO);
}
