package com.shrinfo.ibs.product.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.product.dao.ProductDao;


public class ProductServiceImpl extends BaseService implements ProductService {
    
    ProductDao productDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
    	 Object returnValue = null;
    	 if ("getProductDetails".equals(methodName)) {
             returnValue = getProductDetails((BaseVO) args[0]);
         }
    	 if("saveProductDetails".equals(methodName)){
    	     returnValue = saveProductDetails((BaseVO) args[0]);
    	 }
         return returnValue;
    }

    @Override
    public BaseVO getProductDetails(BaseVO baseVO) {
        return productDao.getProductDetails(baseVO);
    }
   
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

	@Override
	public BaseVO saveProductDetails(BaseVO baseVO) {
		return productDao.saveProductDetails(baseVO);
	}

}