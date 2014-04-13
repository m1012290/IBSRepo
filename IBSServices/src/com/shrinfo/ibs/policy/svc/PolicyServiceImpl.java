package com.shrinfo.ibs.policy.svc;

import java.util.List;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.document.dao.DocumentDao;
import com.shrinfo.ibs.policy.dao.PolicyDao;
import com.shrinfo.ibs.vo.business.DocumentListVO;
import com.shrinfo.ibs.vo.business.PolicyVO;

public class PolicyServiceImpl extends BaseService implements PolicyService {

    PolicyDao policyDao;
    
    DocumentDao documentDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getPolicy".equals(methodName)) {
            returnValue = getPolicy((BaseVO) args[0]);
        }
        if ("getPloicies".equals(methodName)) {
            returnValue = getPolicies((BaseVO) args[0]);
        }
        if ("createPolicy".equals(methodName)) {
            returnValue = createPolicy((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO getPolicy(BaseVO baseVO) {
        return policyDao.getPolicy(baseVO);
    }

    @Override
    public List<BaseVO> getPolicies(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseVO createPolicy(BaseVO baseVO) {
        PolicyVO policyVO = (PolicyVO) policyDao.createPolicy(baseVO);
        DocumentListVO documentListVO = policyVO.getDocListUploaded();

        if(!Utils.isEmpty(documentListVO) && !Utils.isEmpty(documentListVO.getDocumentVOs())) {
            documentListVO = (DocumentListVO) documentDao.saveDocument(documentListVO);
        }
        policyVO.setDocListUploaded(documentListVO);
        return policyVO;
    }

    @Override
    public BaseVO uploadPolicyDoc(BaseVO baseVO) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setPolicyDao(PolicyDao policyDao) {
        this.policyDao = policyDao;
    }

}
