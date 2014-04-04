package com.shrinfo.ibs.search.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.search.dao.SearchDao;

/**
 * 
 * @author Rahul
 *
 */

public class SearchServiceImpl extends BaseService implements SearchService {

    SearchDao searchDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        BaseVO returnValue = null;

        if ("getSearchResult".equals(methodName)) {
            returnValue = getSearchResult((BaseVO) args[0]);
        }
        return returnValue;
    }


    @Override
    public BaseVO getSearchResult(BaseVO baseVO) {
        return searchDao.getSearchResult(baseVO);
    }



    public void setSearchDao(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

}
