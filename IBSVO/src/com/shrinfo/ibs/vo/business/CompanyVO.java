package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil Kumar - This class represents Company entity in the application. Company can
 *         further be broking company, insurance company. Hence this class will act as base abstract
 *         for basic company details.
 */
public abstract class CompanyVO extends BaseVO {

    private static final long serialVersionUID = 8674776028827953900L;

    private String code;

    private String name;

    private ContactVO contactAndAddrDetails;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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



    public ContactVO getContactAndAddrDetails() {
        return contactAndAddrDetails;
    }


    public void setContactAndAddrDetails(ContactVO contactAndAddrDetails) {
        this.contactAndAddrDetails = contactAndAddrDetails;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyVO other = (CompanyVO) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
