package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public class UserRoleVO extends BaseVO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6083379196556678522L;

    private Integer id;

    private String name;

    private String desc;

    private String status;

    private UserRolePrivilege rolePrivilege;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the rolePrivilege
     */
    public UserRolePrivilege getRolePrivilege() {
        return rolePrivilege;
    }

    /**
     * @param rolePrivilege the rolePrivilege to set
     */
    public void setRolePrivilege(UserRolePrivilege rolePrivilege) {
        this.rolePrivilege = rolePrivilege;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
