package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil kumar
 * 
 */
public class DocumentVO extends BaseVO {

    private static final long serialVersionUID = -6850564858341322159L;

    private Long id;

    private EnquiryVO enquiry;

    // quote_slip_id, quotation_id, policy_id
    private Long docSlipId;

    private Integer docSlipVersion;

    private String docDescreption;

    private Byte[] document;

    private String docType;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the enquiry
     */
    public EnquiryVO getEnquiry() {
        return enquiry;
    }

    /**
     * @param enquiry the enquiry to set
     */
    public void setEnquiry(EnquiryVO enquiry) {
        this.enquiry = enquiry;
    }

    /**
     * @return the docSlipId
     */
    public Long getDocSlipId() {
        return docSlipId;
    }

    /**
     * @param docSlipId the docSlipId to set
     */
    public void setDocSlipId(Long docSlipId) {
        this.docSlipId = docSlipId;
    }

    /**
     * @return the docSlipVersion
     */
    public Integer getDocSlipVersion() {
        return docSlipVersion;
    }

    /**
     * @param docSlipVersion the docSlipVersion to set
     */
    public void setDocSlipVersion(Integer docSlipVersion) {
        this.docSlipVersion = docSlipVersion;
    }

    /**
     * @return the docDescreption
     */
    public String getDocDescreption() {
        return docDescreption;
    }

    /**
     * @param docDescreption the docDescreption to set
     */
    public void setDocDescreption(String docDescreption) {
        this.docDescreption = docDescreption;
    }

    /**
     * @return the document
     */
    public Byte[] getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Byte[] document) {
        this.document = document;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
