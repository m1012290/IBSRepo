package com.shrinfo.ibs.company.dao;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompany;
import com.shrinfo.ibs.vo.business.BrokingCompany;
import com.shrinfo.ibs.vo.business.CompanyVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;


public class CompanyDaoImpl extends BaseDBDAO implements CompanyDao {

    @Override
    public BaseVO getCompany(BaseVO baseVO) {
        CompanyVO companyVO = null;

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Company details cannot be null");
        }
        if (!(baseVO instanceof InsCompanyVO) && !(baseVO instanceof BrokingCompany)) {
            throw new BusinessException("cmn.unknownError", null, "Company details are invalid");
        }

        if (baseVO instanceof InsCompanyVO) {
            companyVO = getInsCompanyDetails((InsCompanyVO) baseVO);
        }

        return companyVO;
    }

    @Override
    public BaseVO createCompany(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    private InsCompanyVO getInsCompanyDetails(InsCompanyVO insCompanyVO) {

        IbsInsuranceCompany ibsInsuranceCompany = null;

        try {
            ibsInsuranceCompany =
                (IbsInsuranceCompany) getHibernateTemplate()
                        .find(
                            " from IbsInsuranceCompany ibsInsuranceCompany where ibsInsuranceCompany.code = ?",
                            insCompanyVO.getCode()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCompanyDetails", hibernateException,
                "Error while getting insurance company details");
        }

        MapperUtil.populateInsCompanyVO(insCompanyVO, ibsInsuranceCompany);

        return insCompanyVO;
    }

}
