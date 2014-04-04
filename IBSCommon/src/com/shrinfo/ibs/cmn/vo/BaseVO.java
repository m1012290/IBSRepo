package com.shrinfo.ibs.cmn.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Sunil Kumar
 * This class will act as base for all the backing beans in the application. Hence it has been
 * intentionally made as abstract in order to restrict any direct object creation of this class
 */
public abstract class BaseVO implements Serializable {

    /**
     * Generated serial version id for java 1.6 windows 8 64 bit build
     */
    private static final long serialVersionUID = -780370559913529771L;

    //Logged in user details would be available through below field at any point of time
    private UserVO loggedInUser;
    
    private long createdByUserId;

    private Timestamp createdDate;

    private long updatedByUserId;

    private Timestamp updatedDate;

    private String isStatusActive;
    
    
    /**
     * 
     * @return
     */
    public long getCreatedByUserId() {
        return createdByUserId;
    }

    /**
     * 
     * @param createdByUserId
     */
    public void setCreatedByUserId(long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    /**
     * 
     * @return
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * 
     * @param createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return
     */
    public long getUpdatedByUserId() {
        return updatedByUserId;
    }

    /**
     * 
     * @param updatedByUserId
     */
    public void setUpdatedByUserId(long updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    /**
     * 
     * @return
     */
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    /**
     * 
     * @param updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    
    public String getIsStatusActive() {
        return isStatusActive;
    }

    
    public void setIsStatusActive(String isStatusActive) {
        this.isStatusActive = isStatusActive;
    }

    
    public UserVO getLoggedInUser() {
        return loggedInUser;
    }

    
    public void setLoggedInUser(UserVO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
