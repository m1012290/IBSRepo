package com.shrinfo.ibs.test.services;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.customerenquiry.svc.CustomerEnquiryService;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.PolicyVO;

public class TestCustomerEnquiryServices {

    public static void main(String args[]) {
        Utils.setAppContext(Utils.loadSpringBeansFactory("config/applicationcontext.xml"));

        CustomerEnquiryService customerService =
            (CustomerEnquiryService) Utils.getBean("customerEnquirySvc");

        CustomerVO customerVO = new CustomerVO();
        customerVO.setCategory("Retail");
        customerVO.setClassification("");
        customerVO.setGroup("");
        customerVO.setIsStatusActive("Y");
        customerVO.setName("cumstomer save");
        customerVO.setSalutation("Mr");
        customerVO.setSourceOfBusiness("source");
        ContactVO contactVO = new ContactVO();
        contactVO.setEmailId("customer_save@ibs.com");
        contactVO.setMobileNo("888888888");
        contactVO.getAddressVO().setLattitude("1");
        contactVO.getAddressVO().setLongitude("1");
        
        ContactVO contactVO1 = new ContactVO();
        contactVO1.setEmailId("customer_save@ibs.com");
        contactVO1.setMobileNo("99999999");
        contactVO1.getAddressVO().setLattitude("1");
        contactVO1.getAddressVO().setLongitude("1");

        EnquiryVO enquiryVO = new EnquiryVO();
        //enquiryVO.setEnquiryNo(27l);
        customerVO.setContactAndAddrDets(contactVO);
        enquiryVO.setEnquiryContact(contactVO1);
        enquiryVO.setCustomerDetails(customerVO);

        System.out.println(customerService.createCustomerEnquiry(enquiryVO));

        //System.out.println(customerService.getCustomerEnquiry(enquiryVO));
    }

}
