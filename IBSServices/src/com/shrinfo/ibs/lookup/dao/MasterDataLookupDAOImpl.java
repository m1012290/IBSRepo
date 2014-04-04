/**
 * 
 */
package com.shrinfo.ibs.lookup.dao;

import java.util.List;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.gen.pojo.IbsMasLookup;
import com.shrinfo.ibs.vo.business.LookupVO;


/**
 * @author Sunil Kumar
 */
public class MasterDataLookupDAOImpl extends BaseDBDAO implements MasterDataLookupDAO{
    private static final Logger logger = Logger.getLogger(MasterDataLookupDAOImpl.class);
    @Override
    public BaseVO getMasterData(BaseVO baseVO) {
       
        if( Utils.isEmpty(baseVO)){
            throw new BusinessException("masterdatalookup.nullinput", null, "Input recieved for master data lookup is ["+baseVO+"]");
        }
        LookupVO inputVO = (LookupVO)baseVO;
        if(Utils.isEmpty(inputVO.getCategory())){
            throw new BusinessException("masterdatalookup.nullinput", null, "Input recieved for master data lookup is ["+baseVO+"]");
        }
        
        //check if level1 and level2 criterias are passed if not default them to 'ALL' and 'ALL'
        sanitizeInputVO(inputVO);
        
        //Query and retrieve master data from materialized view, corresponding to category name passed
        List<IbsMasLookup> lookUpDataList = null;
        try{
            lookUpDataList = queryLookUpViewForCategory(inputVO.getCategory());
        }catch(Exception cause){
            throw new SystemException("masterdatalookup.queryfailed", cause, "LookUp query failed ");
        }
        logger.info("MasterDataLookUp successful with resultset retrieved ["+ lookUpDataList +"]");
        
        return constructResponseVO(lookUpDataList);
    }
    
	@Override
	public BaseVO getMasterDataWithLevel1Criteria(BaseVO baseVO) {
		if( Utils.isEmpty(baseVO)){
            throw new BusinessException("masterdatalookup.nullinput", null, "Input recieved for master data lookup is ["+baseVO+"]");
        }
        LookupVO inputVO = (LookupVO)baseVO;
        if(Utils.isEmpty(inputVO.getCategory()) || Utils.isEmpty(inputVO.getLevel1())){
            throw new BusinessException("masterdatalookup.nullinput", null, "Input recieved for master data lookup is ["+baseVO+"]");
        }
        
      //Query and retrieve master data from materialized view, corresponding to category name passed
        List<IbsMasLookup> lookUpDataList = null;
        try{
            lookUpDataList = queryLookUpViewWithLevel1Criteria(inputVO.getCategory(), inputVO.getLevel1());
        }catch(Exception cause){
            throw new SystemException("masterdatalookup.queryfailed", cause, "LookUp query failed ");
        }
        logger.info("MasterDataLookUp successful with resultset retrieved ["+ lookUpDataList +"]");
		return constructResponseVO(lookUpDataList);
	}
	
    //
    private void sanitizeInputVO(LookupVO lookUpVO){
        if(Utils.isEmpty(lookUpVO.getLevel1())){
            lookUpVO.setLevel1("ALL");
        }
        if(Utils.isEmpty(lookUpVO.getLevel2())){
            lookUpVO.setLevel2("ALL");
        }
    }
    //
    @SuppressWarnings("unchecked")
    private List<IbsMasLookup> queryLookUpViewForCategory(String categoryName){
        return getHibernateTemplate().findByNamedParam("from IbsMasLookup ibsMasLookUp where ibsMasLookUp.id.category=:identifier", "identifier", categoryName);
    }
    @SuppressWarnings("unchecked")
    private List<IbsMasLookup> queryLookUpViewWithLevel1Criteria(String categoryName, String level1){
    	String[] paramNames = new String[2];
    	paramNames[0] = "identifier";
    	paramNames[1] = "level1";
    	Object[] values = new Object[2];
    	values[0] = categoryName;
    	values[1] = level1;
    	return getHibernateTemplate().findByNamedParam("from IbsMasLookup ibsMasLookUp where ibsMasLookUp.id.category=:identifier and ibsMasLookUp.id.level1=:level1", paramNames, values);
    }
    //
    private LookupVO constructResponseVO(List<IbsMasLookup> lookUpDataListRetrieved){
        if(Utils.isEmpty(lookUpDataListRetrieved)){
            throw new SystemException("masterdatalookup.queryfailed", null, "Master Data Lookup query failed to yield results");
        }
        LookupVO responseVO = new LookupVO();
        for(IbsMasLookup ibsMasLookupObj : lookUpDataListRetrieved){
            responseVO.getCodeDescMap().put(ibsMasLookupObj.getId().getDescription(), ibsMasLookupObj.getId().getCode());
        }
        //This is done assuming that currently we don't have level1 and level2 filtering on master data 
        responseVO.setCategory(lookUpDataListRetrieved.get(0).getId().getCategory());
        responseVO.setLevel1(lookUpDataListRetrieved.get(0).getId().getLevel1());
        responseVO.setLevel2(lookUpDataListRetrieved.get(0).getId().getLevel2());
        return  responseVO;
    }

}