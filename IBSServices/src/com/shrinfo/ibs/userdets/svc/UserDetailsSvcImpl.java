/**
 * 
 */
package com.shrinfo.ibs.userdets.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.userdets.dao.UserDetailsDao;

/**
 * @author Sunil kumar
 * 
 */
public class UserDetailsSvcImpl extends BaseService implements UserDetailsSvc {

    UserDetailsDao userDetailsDao;

    public UserDetailsDao getUserDetailsDao() {
        return userDetailsDao;
    }

    public void setUserDetailsDao(UserDetailsDao userDetailsDao) {
        this.userDetailsDao = userDetailsDao;
    }

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;
        if ("getUserDetails".equals(methodName)) {
            returnValue = getUserDetails((BaseVO) args[0]);
        } else if ("saveUserDetails".equals(methodName)) {
            returnValue = saveUserDetails((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO saveUserDetails(BaseVO baseVO) {
        return userDetailsDao.saveUserDetails(baseVO);
    }

    @Override
    public BaseVO getUserDetails(BaseVO baseVO) {
        return userDetailsDao.getUserDetails(baseVO);
    }


}
