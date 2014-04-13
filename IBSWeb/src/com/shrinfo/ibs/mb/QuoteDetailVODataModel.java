package com.shrinfo.ibs.mb;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.shrinfo.ibs.vo.business.QuoteDetailVO;


public class QuoteDetailVODataModel extends ListDataModel<QuoteDetailVO> implements SelectableDataModel<QuoteDetailVO>{
    public QuoteDetailVODataModel() {  
    }  
  
    public QuoteDetailVODataModel(List<QuoteDetailVO> itemsList) {  
        super(itemsList);  
    } 
    @Override
    public QuoteDetailVO getRowData(String rowKey) {
        List<QuoteDetailVO> wrappedData = (List<QuoteDetailVO>) getWrappedData();
        List<QuoteDetailVO> itemsList = wrappedData;  
        
        for(QuoteDetailVO quoteDetailVO : itemsList) {  
            if(String.valueOf(quoteDetailVO.getCompanyCode()).equals(rowKey))
                return quoteDetailVO;  
        }  
          
        return null;
    }

    @Override
    public Object getRowKey(QuoteDetailVO quoteDetailVO) {
        // TODO Auto-generated method stub
        return quoteDetailVO.getCompanyCode();
    }

}
