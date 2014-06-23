package com.shrinfo.ibs.company.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsCompany;
import com.shrinfo.ibs.gen.pojo.IbsContact;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompProdLink;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompany;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompanyContact;
import com.shrinfo.ibs.vo.app.RecordType;
import com.shrinfo.ibs.vo.business.BrokingCompany;
import com.shrinfo.ibs.vo.business.CompanyVO;
import com.shrinfo.ibs.vo.business.InsCompanyListVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;


public class CompanyDaoImpl extends BaseDBDAO implements CompanyDao {

    @Override
    public BaseVO getCompany(BaseVO baseVO) {
        BaseVO companyVO = null;

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Company details cannot be null");
        }
        if (!(baseVO instanceof InsCompanyVO) && !(baseVO instanceof BrokingCompany)) {
            throw new BusinessException("cmn.unknownError", null, "Company details are invalid");
        }

        if (baseVO instanceof InsCompanyVO) {
            if (!Utils.isEmpty(((InsCompanyVO) baseVO).getCode())) {
                companyVO = getInsCompanyDetails((InsCompanyVO) baseVO);
            } else {
                companyVO = getAllInsCompanyDetails((InsCompanyVO) baseVO);
            }

        }

        return companyVO;
    }

    @Override
    public BaseVO createCompany(BaseVO baseVO) {

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Company details cannot be null");
        }
        if (!(baseVO instanceof InsCompanyVO) && !(baseVO instanceof BrokingCompany)) {
            throw new BusinessException("cmn.unknownError", null, "Company details are invalid");
        }

        CompanyVO companyVO = (CompanyVO) baseVO;
        IbsContact ibsContact = null;
        try {
            if (baseVO instanceof InsCompanyVO) {
                ibsContact =
                    DAOUtils.constructIbsContactForRecType(baseVO, RecordType.INSURANCE_COMPANY);
                saveOrUpdate(ibsContact);
                // persist company product contact details
                List<IbsInsuranceCompanyContact> contactList =
                    DAOUtils.constructIbsInsCompanyContact(baseVO);
                saveOrUpdateAll(contactList);
                // Persist company product details
                List<IbsInsuranceCompProdLink> compProdLinkList =
                    DAOUtils.constructIbsInsuranceCompProdLink(baseVO);
                saveOrUpdateAll(compProdLinkList);

            } else if (baseVO instanceof BrokingCompany) {
                ibsContact = DAOUtils.constructIbsContactForRecType(baseVO, RecordType.COMPANY);
                saveOrUpdate(ibsContact);
            }


            if (baseVO instanceof InsCompanyVO) {
                companyVO.setCode(((IbsInsuranceCompany) ibsContact.getIbsInsuranceCompanies()
                        .toArray()[0]).getCode());
            } else if (baseVO instanceof BrokingCompany) {
                companyVO.setCode(((IbsCompany) ibsContact.getIbsCompanies().toArray()[0])
                        .getCode());
            }

        } catch (HibernateException hibernateException) {
            throw new BusinessException("ibs.gi.couldNotSaveCompanyDetails", hibernateException,
                "Error while saving Company data");
        } catch (Exception exception) {
            throw new SystemException("ins.gi.couldNotSaveCompanyDetails", exception,
                "Error while saving Company data");
        }

        return companyVO;
    }

    private InsCompanyVO getInsCompanyDetails(InsCompanyVO insCompanyVO) {

        IbsInsuranceCompany ibsInsuranceCompany = null;

        try {
            ibsInsuranceCompany =
                (IbsInsuranceCompany) getHibernateTemplate()
                        .find(
                            " from IbsInsuranceCompany ibsInsuranceCompany where ibsInsuranceCompany.code = ?",
                            insCompanyVO.getCode()).get(0);

            MapperUtil.populateInsCompanyVO(insCompanyVO, ibsInsuranceCompany);

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCompanyDetails", hibernateException,
                "Error while getting insurance company details");
        }

        return insCompanyVO;
    }

    private BaseVO getAllInsCompanyDetails(InsCompanyVO insCompanyVO) {

        InsCompanyListVO insCompanyListVO = new InsCompanyListVO();
        List<InsCompanyVO> insuranceCompanies = new ArrayList<InsCompanyVO>();

        List compObjList = null;
        try {
            compObjList =
                getHibernateTemplate().find(" from IbsInsuranceCompany ibsInsuranceCompany");
            if (!Utils.isEmpty(compObjList)) {
                InsCompanyVO companyVO = null;
                for (Object obj : compObjList) {
                    companyVO = new InsCompanyVO();
                    MapperUtil.populateInsCompanyVO(companyVO, (IbsInsuranceCompany) obj);
                    insuranceCompanies.add(companyVO);
                }
            }
            insCompanyListVO.setCompaniList(insuranceCompanies);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCompanyDetails", hibernateException,
                "Error while getting insurance company details");
        }

        return insCompanyListVO;
    }


}
