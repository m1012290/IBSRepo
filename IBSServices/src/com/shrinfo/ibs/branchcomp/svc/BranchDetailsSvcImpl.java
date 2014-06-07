package com.shrinfo.ibs.branchcomp.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.branchcomp.dao.BranchDetailsDao;
import com.shrinfo.ibs.cmn.vo.BaseVO;

public class BranchDetailsSvcImpl extends BaseService implements BranchDetailsSvc{
	
	BranchDetailsDao branchDetailsDao;

	public BranchDetailsDao getBranchDetailsDao() {
		return branchDetailsDao;
	}

	public void setBranchDetailsDao(BranchDetailsDao branchDetailsDao) {
		this.branchDetailsDao = branchDetailsDao;
	}

	@Override
	public BaseVO saveBranchDetails(BaseVO baseVO) {
		// TODO Auto-generated method stub
        return branchDetailsDao.saveBranchDetails(baseVO);
	}

	@Override
	public Object invokeMethod(String methodName, Object... args) {
		// TODO Auto-generated method stub
		 Object returnValue = null;
	        if ("saveBranchDetails".equals(methodName)) {
	            returnValue = saveBranchDetails((BaseVO) args[0]);
	        }
	        return returnValue;
	}

}	       

