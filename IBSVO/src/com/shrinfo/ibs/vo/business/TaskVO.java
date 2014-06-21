package com.shrinfo.ibs.vo.business;

import java.util.Date;

import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;

public class TaskVO extends BaseVO {

    private static final long serialVersionUID = -3912956915649056656L;

    private Long id;

    private EnquiryVO enquiry;

    private DocumentVO document;
    // this value could be quoteslip id, closing slip id or even policy id depending 
    // on the screen where in referral was triggered.
    private String documentId;

    private String desc;

    private UserVO assignerUser = new IBSUserVO();

    private UserVO assigneeUser = new IBSUserVO();

    private StatusVO statusVO = new StatusVO();

    private Integer taskType;
    
    private Integer taskSectionType;
    
    private Date dueDate;
    
    private String priority;
    
    private String documentName;
    
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


    public StatusVO getStatusVO() {
        return statusVO;
    }


    public void setStatusVO(StatusVO statusVO) {
        this.statusVO = statusVO;
    }

    
    public String getDocumentId() {
        return documentId;
    }

    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    
    
    public Integer getTaskType() {
        return taskType;
    }

    
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    
    public Integer getTaskSectionType() {
        return taskSectionType;
    }

    
    public void setTaskSectionType(Integer taskSectionType) {
        this.taskSectionType = taskSectionType;
    }    

    
    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    
    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    
    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    
    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }    

    
    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    
    /**
     * @param documentName the documentName to set
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    

    @Override
    public String toString() {
        return "TaskVO: assigneeUser=" + this.assigneeUser + ", assignerUser=" + this.assignerUser
            + ", id=" + this.id + ", enquiry=" + this.enquiry + ", status=" + this.statusVO;
    }

}
