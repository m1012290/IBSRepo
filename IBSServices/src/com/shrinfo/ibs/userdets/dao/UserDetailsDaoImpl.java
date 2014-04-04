/**
 * 
 */
package com.shrinfo.ibs.userdets.dao;

import java.util.List;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.gen.pojo.IbsUser;
import com.shrinfo.ibs.vo.business.IBSUserVO;

/**
 * @author Uday kumar
 *
 */
public class UserDetailsDaoImpl extends BaseDBDAO implements UserDetailsDao{

	@Override
	public BaseVO getUserDetails(BaseVO baseVO) {
		if( Utils.isEmpty(baseVO)){
            throw new BusinessException("userdetails.nullinput", null, "Input recieved for user details ["+baseVO+"] is null");
        }
		
		UserVO userVO = (UserVO)baseVO;
		if(Utils.isEmpty(userVO.getLoginName())){
            throw new BusinessException("userdetails.nullinput", null, "Input recieved for user details ["+baseVO+"] is null");
        }
		
		if(Utils.isEmpty(userVO.getLoginName())){
			throw new BusinessException("userdetails.nullinput", null, "Input recieved for user details ["+baseVO+"] is null");
		}
		List<IbsUser> ibsUserList = queryUserDetails(userVO);
		if(Utils.isEmpty(ibsUserList)){
			throw new BusinessException("userdetails.invalidinput", null, "Invalid user details recieved, please check the same");
		}
		populateUserDetails(ibsUserList, userVO);
		return baseVO;
	}
	
	@SuppressWarnings("unchecked")
	private List<IbsUser> queryUserDetails(UserVO userVO){
		//getHibernateTemplate().findByNamedParam("from IbsMasLookup ibsMasLookUp where ibsMasLookUp.id.category=:identifier", "identifier", categoryName);
		return getHibernateTemplate().findByNamedParam("from IbsUser ibsUser where ibsUser.loginName=:loginName", "loginName", userVO.getLoginName());
	}
	//Update userVO with loginname, password details
	private void populateUserDetails(List<IbsUser> ibsUserList, UserVO userVO){
		IBSUserVO ibsUser = (IBSUserVO) userVO;
		userVO.setIsActive(ibsUserList.get(0).getStatus());
		userVO.setPassword(ibsUserList.get(0).getPassword());
		userVO.setLoginName(ibsUserList.get(0).getLoginName());
	}

}
