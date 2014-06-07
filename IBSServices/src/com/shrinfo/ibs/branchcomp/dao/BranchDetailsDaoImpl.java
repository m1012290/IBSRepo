package com.shrinfo.ibs.branchcomp.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsCompanyBranch;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsRoles;
import com.shrinfo.ibs.gen.pojo.IbsRolesProductPrivileges;
import com.shrinfo.ibs.gen.pojo.IbsUser;
import com.shrinfo.ibs.gen.pojo.IbsUserRoleLink;
import com.shrinfo.ibs.vo.app.RecordType;
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
public class BranchDetailsDaoImpl extends BaseDBDAO implements BranchDetailsDao {

   
    @Override
    public BaseVO saveBranchDetails(BaseVO baseVO) {
        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Branch details cannot be null");
        }

        /*if (!(baseVO instanceof IBSUserVO)) {
            throw new BusinessException("cmn.unknownError", null, "Branch details are invalid");
        }*/
        BranchVO branchVO = (BranchVO) baseVO;
        IbsContact ibsContact = null;
        try {
            ibsContact = DAOUtils.constructIbsContactForRecType(branchVO, RecordType.COMPANY_BRANCH);
            saveOrUpdate(ibsContact);
            IbsCompanyBranch ibsCompanyBranch=new IbsCompanyBranch();
            ibsCompanyBranch.setCode(branchVO.getCode());
            ibsCompanyBranch.setName(branchVO.getName());
            ibsCompanyBranch.setShortname(branchVO.getShortName());
            ibsCompanyBranch.setIbsContact(ibsContact);
            ibsCompanyBranch.setStatus(branchVO.getIsStatusActive());
            saveOrUpdate(ibsCompanyBranch);
         /*   IbsUser ibsUser = (IbsUser) ((ibsContact.getIbsUsers().toArray())[0]);
            ibsUserVO.setUserId(ibsUser.getId());
            ibsUserVO.getContactDetails().setContactId(ibsContact.getId());
            
            Set<IbsUserRoleLink> roleLinks = DAOUtils.constructIbsUserRoleLinks(ibsUserVO);
            saveOrUpdate(roleLinks.iterator().next());*/

        } catch (HibernateException hibernateException) {
            throw new BusinessException("ibs.gi.couldNotSaveBranchDetails", hibernateException,
                "Error while saving User data");
        } catch (Exception exception) {
            throw new SystemException("ins.gi.couldNotSaveBranchDetails", exception,
                "Error while saving User data");
        }
        return branchVO;
    }
}