package com.shrinfo.ibs.test.services;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.enquiry.svc.EnquiryService;
import com.shrinfo.ibs.vo.business.EnquiryVO;


public class TestEnquiryServices {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Utils.setAppContext(Utils.loadSpringBeansFactory("config/applicationcontext.xml"));

        EnquiryService enquiryService = (EnquiryService) Utils.getBean("enquirySvcBean");

        EnquiryVO enquiryVO = new EnquiryVO();
        enquiryVO.setEnquiryNo(32l);
        System.out.println(enquiryService.getEnquiryDetail(enquiryVO));
    }

}
