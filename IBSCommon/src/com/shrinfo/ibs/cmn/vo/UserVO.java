/**
 * 
 */
package com.shrinfo.ibs.cmn.vo;


/**
 * @author Sunil Kumar 
 * Base User VO class which would be extended by the application to create
 * IBSUser who will have more details like roles assigned and branch user belongs to.
 */
public abstract class UserVO extends BaseVO implements java.io.Serializable {

    private static final long serialVersionUID = 8814906103217778844L;

    private String loginName; //user login name

    private String password;

    private Long userId; //unique id associated with the user (IBS_USER table)

    private String userName; // Logged in user name
    
    // User status which would be "Y" in case user is active and "N" in case user is inactive
    private String isActive; 
    

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    public String getIsActive() {
        return isActive;
    }

    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "UserVO [loginName=" + loginName + ", userId=" + userId + ", userName=" + userName
            + ", isActive=" + isActive + "]";
    }

}