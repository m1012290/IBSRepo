package com.shrinfo.ibs.vo.business;

import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public class DocumentListVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = -1246334688633191198L;

    List<DocumentVO> documentVOs;


    public List<DocumentVO> getDocumentVOs() {
        return documentVOs;
    }


    public void setDocumentVOs(List<DocumentVO> documentVOs) {
        this.documentVOs = documentVOs;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
