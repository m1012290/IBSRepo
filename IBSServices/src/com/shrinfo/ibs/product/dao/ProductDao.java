package com.shrinfo.ibs.product.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.vo.business.DocumentListVO;


public interface ProductDao {
    
    public BaseVO getProductDetails(BaseVO baseVO);
    
    public BaseVO saveProductDetails(BaseVO baseVO);
    
    public DocumentListVO getProductDocuList(BaseVO baseVO);

}
