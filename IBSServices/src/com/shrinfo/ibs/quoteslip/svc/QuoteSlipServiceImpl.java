package com.shrinfo.ibs.quoteslip.svc;

import java.util.List;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.document.dao.DocumentDao;
import com.shrinfo.ibs.enquiry.dao.EnquiryDao;
import com.shrinfo.ibs.insured.dao.InsuredDao;
import com.shrinfo.ibs.quoteslip.dao.QuoteSlipDao;
import com.shrinfo.ibs.vo.business.ContactVO;
import com.shrinfo.ibs.vo.business.DocumentListVO;
import com.shrinfo.ibs.vo.business.DocumentVO;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.InsuredVO;
import com.shrinfo.ibs.vo.business.PolicyVO;
import com.shrinfo.ibs.vo.business.QuoteDetailVO;



public class QuoteSlipServiceImpl extends BaseService implements QuoteSlipService {

    QuoteSlipDao quoteSlipDao;

    InsuredDao insuredDao;
    
    DocumentDao documentDao;
    
    EnquiryDao enquiryDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getQuoteSlipDetails".equals(methodName)) {
            returnValue = getQuoteSlipDetails((BaseVO) args[0]);
        }
        if ("getInsuredDetails".equals(methodName)) {
            returnValue = getInsuredDetails((BaseVO) args[0]);
        }
        if ("createQuoteSlip".equals(methodName)) {
            returnValue = createQuoteSlip((BaseVO) args[0]);
        }
        if ("emailQuoteSlip".equals(methodName)) {
            // returnValue = emailQuoteSlip( (BaseVO) args[ 0 ], args );
        }
        if("updateEmailedQuoteSlipFlag".equals(methodName)){
            returnValue = updateEmailedQuoteSlipFlag((BaseVO) args[0]);
        }
        if("createInsuredDetails".equals(methodName)){
        	returnValue = createInsuredDetails((BaseVO) args[0]);
        }

        return returnValue;
    }



    @Override
    public BaseVO getQuoteSlipDetails(BaseVO baseVO) {
        return quoteSlipDao.getQuoteSlipDetails(baseVO);
    }

    @Override
    public BaseVO getInsuredDetails(BaseVO baseVO) {
        return insuredDao.getInsuredetails(baseVO);
    }

    @Override
    public BaseVO createQuoteSlip(BaseVO baseVO) {
        PolicyVO policyVO = (PolicyVO) baseVO;
        EnquiryVO enquiryDetails = policyVO.getEnquiryDetails();
        InsuredVO insuredVO =
            (InsuredVO) insuredDao.createInsuredDetails(policyVO.getInsuredDetails());
        policyVO.setInsuredDetails(insuredVO);
        policyVO = (PolicyVO) quoteSlipDao.createQuoteSlip(baseVO);
        policyVO.setEnquiryDetails(enquiryDetails);
        policyVO.getEnquiryDetails().setInsuredDetails(insuredVO);
        policyVO.getEnquiryDetails().setCustomerDetails(insuredVO.getCustomerDetails());
        EnquiryVO enquiryVO = (EnquiryVO) enquiryDao.createEnquiry(policyVO.getEnquiryDetails());
        policyVO.setEnquiryDetails(enquiryVO);
        DocumentListVO documentListVO = policyVO.getDocListUploaded();
        
        // populate policy related details in document list.
        if(!Utils.isEmpty(documentListVO) && !Utils.isEmpty(documentListVO.getDocumentVOs())) {
            for(DocumentVO document : documentListVO.getDocumentVOs()){
                document.setDocSlipId(policyVO.getPolicyId());
                document.setDocSlipVersion(policyVO.getPolicyVersion());
                document.setDocType("QUOTRSLIPRISK");
                document.setEnquiry(policyVO.getEnquiryDetails());                
            }
            documentListVO = (DocumentListVO) documentDao.saveDocument(documentListVO);
        }
        policyVO.setDocListUploaded(documentListVO);
        return policyVO;
    }


    @Override
    public BaseVO emailQuoteSlip(BaseVO baseVO, List<BaseVO> companies) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public BaseVO createInsuredDetails(BaseVO baseVO) {
    	return insuredDao.createInsuredDetails(baseVO);
    }

    public void setQuoteSlipDao(QuoteSlipDao quoteSlipDao) {
        this.quoteSlipDao = quoteSlipDao;
    }



    public void setInsuredDao(InsuredDao insuredDao) {
        this.insuredDao = insuredDao;
    }



    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    public void setEnquiryDao(EnquiryDao enquiryDao){
    	this.enquiryDao = enquiryDao;
    }

    @Override
    public BaseVO createQuoteSlipFile(QuoteDetailVO quoteDetails, InsuredVO insuredDetails,
            ContactVO contacts, String companyName, String filePath, String imagePath) {
       /*
    	QuoteSlipPDFGenerator quoteSlipPDFGenerator = new QuoteSlipPDFGenerator();
        quoteSlipPDFGenerator.generatePDF(quoteDetails, insuredDetails, contacts, companyName,
            filePath, imagePath);*/
        return null;
    }
    
    @Override
    public BaseVO updateEmailedQuoteSlipFlag(BaseVO baseVO) {
        return quoteSlipDao.updateEmailedQuoteSlipFlag(baseVO);
    }


}
