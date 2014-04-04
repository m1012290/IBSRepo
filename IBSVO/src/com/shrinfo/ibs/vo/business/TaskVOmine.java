package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;

public abstract class TaskVOmine extends BaseVO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3912956915649056656L;

    private Long id;

    private EnquiryVO enquiry;

    private DocumentVO document;

    private String desc;

    private String statusCode;

    private UserVO assignerUser;

    private UserVO assigneeUser;

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
     * @return the document
     */
    public DocumentVO getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(DocumentVO document) {
        this.document = document;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the assignerUser
     */
    public UserVO getAssignerUser() {
        return assignerUser;
    }

    /**
     * @param assignerUser the assignerUser to set
     */
    public void setAssignerUser(UserVO assignerUser) {
        this.assignerUser = assignerUser;
    }

    /**
     * @return the assigneeUser
     */
    public UserVO getAssigneeUser() {
        return assigneeUser;
    }

    /**
     * @param assigneeUser the assigneeUser to set
     */
    public void setAssigneeUser(UserVO assigneeUser) {
        this.assigneeUser = assigneeUser;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
