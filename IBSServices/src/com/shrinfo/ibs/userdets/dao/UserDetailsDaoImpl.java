package com.shrinfo.ibs.userdets.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsRoles;
import com.shrinfo.ibs.gen.pojo.IbsRolesProductPrivileges;
import com.shrinfo.ibs.gen.pojo.IbsUser;
import com.shrinfo.ibs.gen.pojo.IbsUserRoleLink;
import com.shrinfo.ibs.vo.business.AddressVO;
import com.shrinfo.ibs.vo.business.BranchVO;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.UserRolePrivilege;
import com.shrinfo.ibs.vo.business.UserRoleVO;

/**
 * @author Sunil kumar
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
		try{
		    populateUserDetails(ibsUserList, userVO);
		}catch(Exception ex){
		    System.out.println("Exception encountered while populating user details");
		    throw new SystemException(ex, "Exception ["+ex+"] while populating user details");
		}
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
		//user contact details
		populateUserContact(ibsUserList.get(0), ibsUser);
		//user branch details
		populateUserBranch(ibsUserList.get(0), ibsUser);
		//user roles along with products applicable for each of the roles
		populateUserRoles(ibsUserList.get(0), ibsUser);
	}
	
	private void populateUserContact(IbsUser ibsUser, UserVO userVO){
	    IBSUserVO ibsUserVO = (IBSUserVO) userVO;
	    ContactVO contactDetails = new ContactVO();
	    contactDetails.setContactId(ibsUser.getIbsContact().getId());
	    contactDetails.setEmailId(ibsUser.getIbsContact().getPrimaryEmailId());
	    contactDetails.setLandlineNo(ibsUser.getIbsContact().getPrimaryLandlineNo());
	    contactDetails.setMobileNo(ibsUser.getIbsContact().getPrimaryMobileNo());
	    contactDetails.setFirstName(ibsUser.getIbsContact().getFirstName());
	    contactDetails.setMiddleName(ibsUser.getIbsContact().getMiddleName());
	    contactDetails.setLastName(ibsUser.getIbsContact().getLastName());
	    contactDetails.setTitle(ibsUser.getIbsContact().getTitle());
	    AddressVO addressVO = new AddressVO();
	    addressVO.setCity(ibsUser.getIbsContact().getCity());
	    addressVO.setCountry(ibsUser.getIbsContact().getCountry());
	    addressVO.setPoBox(ibsUser.getIbsContact().getPobox());
	    addressVO.setAddress(ibsUser.getIbsContact().getAddress());
	    addressVO.setLattitude(Utils.isEmpty(ibsUser.getIbsContact().getLocationLattitude())?null:String.valueOf(ibsUser.getIbsContact().getLocationLattitude()));
	    addressVO.setLongitude(Utils.isEmpty(ibsUser.getIbsContact().getLocationLongitude())?null:String.valueOf(ibsUser.getIbsContact().getLocationLongitude()));
	    contactDetails.setAddressVO(addressVO);
	    ibsUserVO.setContactDetails(contactDetails);
	}
	
	private void populateUserBranch(IbsUser ibsUser, UserVO userVO){
	    IBSUserVO ibsUserVO = (IBSUserVO) userVO;
	    BranchVO branchVO = new BranchVO();
	    branchVO.setCode(ibsUser.getIbsCompanyBranch().getCode());
	    branchVO.setName(ibsUser.getIbsCompanyBranch().getName());
	    branchVO.setAbbrevation(ibsUser.getIbsCompanyBranch().getAbbr());
	    ibsUserVO.setBranchDetails(branchVO);
	}
	
	private void populateUserRoles(IbsUser ibsUser, UserVO userVO){
	    IBSUserVO ibsUserVO = (IBSUserVO) userVO;
	    List<UserRoleVO> roles = new ArrayList<UserRoleVO>();
	    List<UserRolePrivilege> rolesProductPrivileges = new ArrayList<UserRolePrivilege>();
	    UserRoleVO userRoleVO =  null;
	    for(IbsUserRoleLink userRolesLink : ibsUser.getIbsUserRoleLinks()){
	        userRoleVO = new UserRoleVO();
	        userRoleVO.setId(userRolesLink.getIbsRoles().getId());
	        userRoleVO.setDesc(userRolesLink.getIbsRoles().getDescription());
	        userRoleVO.setName(userRolesLink.getIbsRoles().getName());
	        populateUserRolePrivilege(userRolesLink.getIbsRoles(), rolesProductPrivileges);
	        userRoleVO.setRoleProductPrivileges(rolesProductPrivileges);
	        roles.add(userRoleVO);
	    }
	    ibsUserVO.setRoles(roles);
	}
	
	private void populateUserRolePrivilege(IbsRoles roles, List<UserRolePrivilege> userRolePrivileges){
	    UserRolePrivilege userRolePrivilege = null;
	    ProductVO productVO = null;
	    Iterator<IbsRolesProductPrivileges> rolesProductPrivilege = roles.getIbsRolesProductPrivilegeses().iterator();
	    while(rolesProductPrivilege.hasNext()){
	        userRolePrivilege = new UserRolePrivilege();
	        IbsRolesProductPrivileges roleProductPrivileges = rolesProductPrivilege.next();
	        userRolePrivilege.setEmailQuoteSlip(roleProductPrivileges.getEmailQuoteslip());
	        userRolePrivilege.setEmailRequisition(roleProductPrivileges.getEmailRequisition());
	        userRolePrivilege.setGenerateQuoteSlip(roleProductPrivileges.getGenerateQuoteslip());
	        userRolePrivilege.setEmailClosingSlip(roleProductPrivileges.getEmailClosingslip());
	        userRolePrivilege.setGenerateClosingSlip(roleProductPrivileges.getGenerateClosingslip());
	        userRolePrivilege.setFreeText1(roleProductPrivileges.getFreetext1());
            userRolePrivilege.setFreeText2(roleProductPrivileges.getFreetext2());
            userRolePrivilege.setMaxSiLimit(roleProductPrivileges.getToSiLimit());
            userRolePrivilege.setMinSiLimit(roleProductPrivileges.getFromSiLimit());
            productVO = new ProductVO();
            MapperUtil.populateProductVO(productVO, roleProductPrivileges.getIbsProductMaster());
            userRolePrivilege.setProduct(productVO);
            userRolePrivileges.add(userRolePrivilege);
	    }
	}
}