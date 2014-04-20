package com.shrinfo.ibs.company.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface CompanyDao {

    public BaseVO getCompany(BaseVO baseVO);

    public BaseVO createCompany(BaseVO baseVO);
}
