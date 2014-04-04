package com.shrinfo.ibs.mb;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.shrinfo.ibs.vo.business.SearchItemVO;

public class SearchItemVODataModel extends ListDataModel<SearchItemVO> implements SelectableDataModel<SearchItemVO>{

	
	 public SearchItemVODataModel() {  
	    }  
	  
	    public SearchItemVODataModel(List<SearchItemVO> itemsList) {  
	        super(itemsList);  
	    }  
	      
	@Override
	public SearchItemVO getRowData(String rowKey) {
		// TODO Auto-generated method stub
		
		List<SearchItemVO> wrappedData = (List<SearchItemVO>) getWrappedData();
		List<SearchItemVO> itemsList = wrappedData;  
        
        for(SearchItemVO searchItemVO : itemsList) {  
            if(String.valueOf(searchItemVO.getEnquiryNum()).equals(rowKey))
                return searchItemVO;  
        }  
          
		return null;
	}

	@Override
	public Object getRowKey(SearchItemVO searchItemVO) {
		// TODO Auto-generated method stub
		return searchItemVO.getEnquiryNum();
	}
	
	

}
