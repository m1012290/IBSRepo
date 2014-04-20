package com.shrinfo.ibs.test.services;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.company.svc.CompanyService;
import com.shrinfo.ibs.vo.business.CompanyVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;


public class TestCompanyService {

    public static void main(String[] args) {
        // TODO Auto-generated method stub


        Utils.setAppContext(Utils.loadSpringBeansFactory("config/applicationcontext.xml"));

        CompanyService companyService = (CompanyService) Utils.getBean("companySvc");

        CompanyVO companyVO = new InsCompanyVO();
        companyVO.setCode("1000");
        companyVO  = (CompanyVO) companyService.getCompany(companyVO);
        System.out.println(companyVO);

    }

}
