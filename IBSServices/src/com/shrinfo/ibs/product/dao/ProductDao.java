package com.shrinfo.ibs.product.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface ProductDao {
    
    public BaseVO getProductDetails(BaseVO baseVO);
    
    public BaseVO saveProductDetails(BaseVO baseVO);

}
