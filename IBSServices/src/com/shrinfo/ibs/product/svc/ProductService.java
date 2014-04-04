package com.shrinfo.ibs.product.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface ProductService {

    public BaseVO getProductDetails(BaseVO baseVO);

    public BaseVO saveProductDetails(BaseVO baseVO);

}
