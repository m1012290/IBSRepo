package com.shrinfo.ibs.document.dao;


import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface DocumentDao {
    
    public BaseVO saveDocument(BaseVO baseVO);
    
    public BaseVO getDocument(BaseVO baseVO);

}
