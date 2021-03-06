package com.shrinfo.ibs.test.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.quotation.svc.QuotationService;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.CustomerVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.InsCompanyVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;

public class TestQuotationServices {

    public static void main(String[] args) {
        Utils.setAppContext(Utils.loadSpringBeansFactory("config/applicationcontext.xml"));

        QuotationService quotationService = (QuotationService) Utils.getBean("quotationSvc");

        QuoteDetailVO quoteDetailVO = new QuoteDetailVO();

        // quoteDetailVO.setEnquiryNum(89l);
        quoteDetailVO.setCustomerId(89l);
        quoteDetailVO.setQuoteSlipId(58l);
        quoteDetailVO.setQuoteSlipVersion(1l);

        InsuredVO insuredVO = new InsuredVO();
        CustomerVO customerVO = new CustomerVO();
        customerVO.setCustomerId(90l);
        customerVO.setCategory("Retail");
        customerVO.setClassification("");
        customerVO.setGroup("");
        customerVO.setIsStatusActive("Y");
        customerVO.setName("cumstomer saveupdated");
        customerVO.setSalutation("Mr");
        customerVO.setSourceOfBusiness("source");
        ContactVO contactVO = new ContactVO();
        contactVO.setEmailId("customer_save@ibs.com");
        contactVO.setMobileNo("888888888");
        contactVO.getAddressVO().setLattitude("1");
        contactVO.getAddressVO().setLongitude("1");
        customerVO.setContactAndAddrDets(contactVO);
        insuredVO.setId(43l);
        insuredVO.setCustomerDetails(customerVO);
        insuredVO.setContactAndAddrDetails(contactVO);
        insuredVO.setIsStatusActive("Y");
        insuredVO.setName("insured insert");
        insuredVO.setSalutation("Dir");
        insuredVO.setSourceOfBusiness("time pass");
        ProductVO productVO = new ProductVO();
        productVO.setProductClass(1);
        ProductUWFieldVO fieldVO = new ProductUWFieldVO();
        fieldVO.setUwFieldId(1l);
        fieldVO.setFieldName("name");
        fieldVO.setFieldType("text");
        fieldVO.setFieldValue("field value name");
        ProductUWFieldVO fieldVO1 = new ProductUWFieldVO();
        fieldVO1.setUwFieldId(2l);
        fieldVO1.setFieldName("name");
        fieldVO1.setFieldType("text");
        fieldVO1.setFieldValue("field value name");
        List<ProductUWFieldVO> fieldVOs = new ArrayList<ProductUWFieldVO>();
        fieldVOs.add(fieldVO);
        fieldVOs.add(fieldVO1);
        productVO.setUwFieldsList(fieldVOs);

        // quoteDetailVO.setInsuredDetails(insuredVO);
        quoteDetailVO.setProductDetails(productVO);
        quoteDetailVO.setStatusCode(1);
        InsCompanyVO quotedCompany = new InsCompanyVO();
        quotedCompany.setCode("1");
        // quoteDetailVO.setQuotedCompany(quotedCompany);

        PolicyVO policyVO = new PolicyVO();
        policyVO.setInsuredDetails(insuredVO);
        EnquiryVO enquiryVO = new EnquiryVO();
        enquiryVO.setEnquiryNo(23l);
        policyVO.setEnquiryDetails(enquiryVO);

        Map<InsCompanyVO, QuoteDetailVO> map = new HashMap<InsCompanyVO, QuoteDetailVO>();
        InsCompanyVO companyVO = new InsCompanyVO();
        companyVO.setCode("1000");
        map.put(companyVO, quoteDetailVO);
        InsCompanyVO companyVO1 = new InsCompanyVO();
        companyVO1.setCode("1001");
        map.put(companyVO1, quoteDetailVO);
        policyVO.setQuoteDetails(map);

        policyVO = (PolicyVO) quotationService.createQuotation(policyVO);

        quoteDetailVO.setQuoteSlipId(58l);
        quoteDetailVO.setQuoteSlipVersion(1l);
        quoteDetailVO.setQuoteId(10l);
        System.out.println(quotationService.getQuotation(quoteDetailVO));

        policyVO = (PolicyVO) quotationService.getQuotation(quoteDetailVO);
       
    }

}
