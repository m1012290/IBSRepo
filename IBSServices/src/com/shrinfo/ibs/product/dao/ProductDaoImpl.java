package com.shrinfo.ibs.product.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsInsuranceCompProdLink;
import com.shrinfo.ibs.gen.pojo.IbsProductMaster;
import com.shrinfo.ibs.gen.pojo.IbsProductUwFields;
import com.shrinfo.ibs.vo.business.ProductUWFieldVO;
import com.shrinfo.ibs.vo.business.ProductVO;


public class ProductDaoImpl extends BaseDBDAO implements ProductDao {

    @Override
    public BaseVO getProductDetails(BaseVO baseVO) {
        ProductVO productVO = new ProductVO();

        if (null == baseVO) {
            throw new BusinessException("cmn.unknownError", null, "Product details cannot be null");
        }
        if (!(baseVO instanceof ProductVO)) {
            throw new BusinessException("cmn.unknownError", null, "Product details are invalid");
        }

        IbsProductMaster ibsProductMaster = null;
        try {
            ibsProductMaster =
                (IbsProductMaster) getHibernateTemplate().find(
                    " from IbsProductMaster ibsProductMaster where ibsProductMaster.class_ = ? ",
                    Long.valueOf(((ProductVO) baseVO).getProductClass())).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("ibs.gi.couldNotGetProductDetails", hibernateException,
                "Error while retreiving product details");
        }
        MapperUtil.populateProductVO(productVO, ibsProductMaster);
        return productVO;
    }

    @Override
    public BaseVO saveProductDetails(BaseVO baseVO) {
        if(Utils.isEmpty(baseVO)){
            throw new BusinessException("SAVE_PROD_DETAILS_INVALID_INPUT",null,"Product details recieved is empty");
        }
        if (!(baseVO instanceof ProductVO)) {
            throw new BusinessException("SAVE_PROD_DETAILS_INVALID_INPUT", null, "Product details are invalid");
        }
        ProductVO productDetails = (ProductVO)baseVO;
        IbsProductMaster ibsProductMaster = DAOUtils.constructIbsProduct(productDetails);
        //below logic to update Set<IbsProductUwFields> to IbsProductMaster
        List<IbsProductUwFields> productUwFieldColl = new ArrayList<IbsProductUwFields>();
        for(ProductUWFieldVO productUWFieldVO : productDetails.getUwFieldsList()){
            IbsProductUwFields ibsProductUwFields = DAOUtils.constructIbsProductUwField(productUWFieldVO);
            ibsProductUwFields.setIsMandatory("Y");
            ibsProductUwFields.setIbsProductMaster(ibsProductMaster);
            productUwFieldColl.add(ibsProductUwFields);
        }
        Set<IbsProductUwFields> productUWFields = new HashSet<IbsProductUwFields>();
        productUWFields.addAll(productUwFieldColl);
        ibsProductMaster.setIbsProductUwFieldses(productUWFields);
        
        //below logic to update Set<IbsInsuranceCompProdLinks> to IbsProductMaster
        List<IbsInsuranceCompProdLink> ibsInsuranceCompProdLinksColl = new ArrayList<IbsInsuranceCompProdLink>();
        for(String insCompanyCode : productDetails.getApplicableInsCompanies()){
            IbsInsuranceCompProdLink ibsInsuranceCompProdLink = new IbsInsuranceCompProdLink();
            ibsInsuranceCompProdLink.setCompanyCode(insCompanyCode);
            ibsInsuranceCompProdLink.setIbsProductMaster(ibsProductMaster);
            ibsInsuranceCompProdLink.setStatus("Y");
            ibsInsuranceCompProdLinksColl.add(ibsInsuranceCompProdLink);
        }
        ibsProductMaster.setIbsInsuranceCompProdLinks(new HashSet<IbsInsuranceCompProdLink>(ibsInsuranceCompProdLinksColl));
        
        //persist IbsProductMaster so that IbsInsuranceCompProdLinks and IbsProductUwFields also gets persisted
        try{
            saveOrUpdate(ibsProductMaster);
        }catch(HibernateException he){
            he.printStackTrace();
            throw new SystemException(he, "Hibernate Exception ["+ he +"] encountered while performing product details save operation");
        }
        return baseVO;
    }

}