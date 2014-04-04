package com.shrinfo.ibs.search.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.vo.business.SearchItemVO;
import com.shrinfo.ibs.vo.business.SearchVO;


public class SearchDaoImpl extends BaseDBDAO implements SearchDao {

    private static final Logger LOGGER = Logger.getLogger(SearchDaoImpl.class);

    @Override
    public BaseVO getSearchResult(BaseVO baseVO) {


        SearchVO searchVO = new SearchVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Search critaria cannot be null");
        }
        if (!(baseVO instanceof SearchItemVO)) {
            throw new BusinessException("cmn.unknownError", null, "Search critaria is invalid");
        }
        SearchItemVO searchItemVO = (SearchItemVO) baseVO;

        searchVO.setSearchItems(fetchInsuredSearchResults(searchItemVO));

        LOGGER.info("search results: " + searchVO.toString());

        return searchVO;
    }


    @SuppressWarnings("unchecked")
    private List<SearchItemVO> fetchInsuredSearchResults(SearchItemVO searchItem) {

        List<SearchItemVO> searchResults = new ArrayList<SearchItemVO>();
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query = session.createSQLQuery(fetchInsuredSearchQuery(searchItem));
        List<Object[]> result = null;
        try {
            result = (List<Object[]>) query.list();
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetCustDetails", hibernateException,
                "Error while insured search");
        }

        SearchItemVO itemVO = null;
        Iterator<Object[]> it = result.iterator();
        Object[] object = null;
        while (it.hasNext()) {
            object = it.next();
            itemVO = new SearchItemVO();
            populateSearchItemVO(itemVO, object);
            searchResults.add(itemVO);

        }
        return searchResults;
    }

    private String fetchInsuredSearchQuery(SearchItemVO searchItem) {

        StringBuffer buffer = new StringBuffer();
        String query =
            "SELECT CUST_NAME, EMAIL, MOB_NO, INSURED_NAME, ENQUIRY_NO, QUOTATION_NO, POLICY_NO, "
                + " CUST_CONTACT_ID,INSURED_ID, CUST_ID FROM IBS_V_INSURED_SEARCH IVIS ";

        if (!Utils.isEmpty(searchItem.getCustomerName())) {
            buffer.append(" AND IVIS.CUST_NAME LIKE '%" + searchItem.getCustomerName() + "%'");
        }
        if (!Utils.isEmpty(searchItem.getCustomerMob())) {
            buffer.append(" AND IVIS.MOB_NO = '" + searchItem.getCustomerMob()+ "'");
        }
        if (!Utils.isEmpty(searchItem.getCustomerEmail())) {
            buffer.append(" AND IVIS.EMAIL = '" + searchItem.getCustomerEmail()+ "'");
        }
        if (!Utils.isEmpty(searchItem.getInsuredName())) {
            buffer.append(" AND IVIS.INSURED_NAME = '" + searchItem.getInsuredName()+ "'");
        }
        if (0 < searchItem.getEnquiryNum()) {
            buffer.append(" AND IVIS.ENQUIRY_NO = " + searchItem.getEnquiryNum());
        }
        if (0 < searchItem.getQuotationNum()) {
            buffer.append(" AND IVIS.QUOTATION_NO = " + searchItem.getQuotationNum());
        }
        if (!Utils.isEmpty(searchItem.getPolicyNum())) {
            buffer.append(" AND IVIS.POLICY_NO = '" + searchItem.getPolicyNum()+ "'");
        }

        if (3 < buffer.length()) {
            return query + " WHERE " + buffer.substring(4, buffer.length());
        }

        return query;
    }

    private void populateSearchItemVO(SearchItemVO itemVO, Object[] ibsVInsuredSearch) {

        if (Utils.isEmpty(ibsVInsuredSearch)) {
            return;
        }

        if (Utils.isEmpty(itemVO)) {
            return;
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[0])) {
            itemVO.setCustomerName((String) ibsVInsuredSearch[0]);
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[1])) {
            itemVO.setCustomerEmail((String) ibsVInsuredSearch[1]);
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[2])) {
            itemVO.setCustomerMob((String) ibsVInsuredSearch[2]);
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[3])) {
            itemVO.setInsuredName((String) ibsVInsuredSearch[3]);
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[4])) {
            itemVO.setEnquiryNum(((BigDecimal) ibsVInsuredSearch[4]).longValue());
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[5])) {
            itemVO.setQuotationNum(((BigDecimal) ibsVInsuredSearch[5]).longValue());
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[6])) {
            itemVO.setPolicyNum(""+ibsVInsuredSearch[6]);
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[8])) {
            itemVO.setInsuredId(((BigDecimal) ibsVInsuredSearch[8]).longValue());
        }
        if (!Utils.isEmpty(ibsVInsuredSearch[9])) {
            itemVO.setCustomerId(((BigDecimal) ibsVInsuredSearch[9]).longValue());
        }
    }
}
