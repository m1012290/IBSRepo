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

    private byte[] document;

    private String docType;
    
    private String name;
    
    private String format;
    
    private Long size;

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

    
    public byte[] getDocument() {
        return document;
    }

    
    public void setDocument(byte[] document) {
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    
    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    
    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    
    /**
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
