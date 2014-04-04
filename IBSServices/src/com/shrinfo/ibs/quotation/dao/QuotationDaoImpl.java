package com.shrinfo.ibs.quotation.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsQuoteComparisionHeader;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

public class QuotationDaoImpl extends BaseDBDAO implements QuotationDao {

    @Override
    public List<BaseVO> getQuotations(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO getQuotation(BaseVO baseVO) {
        PolicyVO policyVO = new PolicyVO();

        if (null == baseVO || !(baseVO instanceof QuoteDetailVO)
            || Utils.isEmpty(((QuoteDetailVO) baseVO).getQuoteId())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid Quotation details. No data passed to fetch details");
        }
        IbsQuoteComparisionHeader ibsQuoteComparisionHeader = null;

        try {
            ibsQuoteComparisionHeader =
                (IbsQuoteComparisionHeader) getHibernateTemplate().find(
                    " from IbsQuoteComparisionHeader ibsQuoteComparisionHeader "
                        + "where ibsQuoteComparisionHeader.id = ? ",
                    ((QuoteDetailVO) baseVO).getQuoteId()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetQuotationDetails", hibernateException,
                "Error while insured search");
        }

        MapperUtil.populatePolicyVO(policyVO, ibsQuoteComparisionHeader);

        return policyVO;
    }

    @Override
    public BaseVO createQuotation(BaseVO baseVO) {
        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null,
                "Quotation details cannot be null");
        }
        if (!(baseVO instanceof PolicyVO)) {
            throw new BusinessException("cmn.unknownError", null, "Quotation details are invalid");
        }
        PolicyVO policyVO = (PolicyVO) baseVO;

        IbsQuoteComparisionHeader ibsQuoteComparisionHeader = null;

        try {
            ibsQuoteComparisionHeader = DAOUtils.constructIbsQuoteComparisionHeader(policyVO);
            saveOrUpdate(ibsQuoteComparisionHeader);

            // populate quotation IDs
            Map<InsCompanyVO, QuoteDetailVO> map = policyVO.getQuoteDetails();
            for (Entry<InsCompanyVO, QuoteDetailVO> e : map.entrySet()) {
                e.getValue().setQuoteId(ibsQuoteComparisionHeader.getId());
            }

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveQuotationDetails", hibernateException,
                "Error while saving Quotation data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveQuotationDetails", exception,
                "Error while saving Quotation data");
        }
        return policyVO;
    }

    @Override
    public BaseVO closeQuotation(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

}
