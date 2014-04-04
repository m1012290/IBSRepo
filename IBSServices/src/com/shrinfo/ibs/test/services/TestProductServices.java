package com.shrinfo.ibs.test.services;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.product.svc.ProductService;
import com.shrinfo.ibs.vo.business.ProductVO;


public class TestProductServices {

    public static void main(String args[]) {
        Utils.setAppContext(Utils.loadSpringBeansFactory("config/applicationcontext.xml"));

        ProductService productService = (ProductService) Utils.getBean("productSvc");

        ProductVO productVO = new ProductVO();
        productVO.setProductClass(1);

        System.out.println(((ProductVO) productService.getProductDetails(productVO))
                .getUwFieldsList());
    }

}
