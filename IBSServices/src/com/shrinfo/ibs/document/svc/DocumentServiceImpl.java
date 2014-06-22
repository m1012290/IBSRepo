package com.shrinfo.ibs.document.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.document.dao.DocumentDao;


public class DocumentServiceImpl extends BaseService implements DocumentService {
    
    DocumentDao documentDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("saveDocumentList".equals(methodName)) {
            returnValue = saveDocumentList((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO saveDocumentList(BaseVO baseVO) {        
        return documentDao.saveDocument(baseVO);
    }

    
    /**
     * @return the documentDao
     */
    public DocumentDao getDocumentDao() {
        return documentDao;
    }

    
    /**
     * @param documentDao the documentDao to set
     */
    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

}
