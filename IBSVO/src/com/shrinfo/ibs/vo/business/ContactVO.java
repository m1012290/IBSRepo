package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil kumar This class represents Contact record for various entities like User,
 *         Customer, Broking Company , Broking Company branch and finally insurance company contact.
 */
public class ContactVO extends BaseVO {

    private static final long serialVersionUID = -1199380266871786516L;

    private Long contactId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String title;

    private String mobileNo;

    private String alternateMobileNo1;

    private String alternateMobileNo2;

    private String landlineNo;

    private String alternateLandlineNo1;

    private String alternateLandLineNo2;

    private String emailId;

    private String alternateEmailId1;

    private String alternateEmailId2;

    private String faxNo;

    private AddressVO addressVO=new AddressVO();

    // Record type here would be record type codes from IBS_RECORD_TYPES which are
    // basically to signify type of the contact record current entity represents.
    // We can contact record for User, Customer, Broking Company, Broking Company Branch
    // and Insurance company. Hence record types will exactly tell us whom does the contact
    // details belong to.
    private int recordType;

    private String recordTypeDesc;

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAlternateMobileNo1() {
		return alternateMobileNo1;
	}

	public void setAlternateMobileNo1(String alternateMobileNo1) {
		this.alternateMobileNo1 = alternateMobileNo1;
	}

	public String getAlternateMobileNo2() {
		return alternateMobileNo2;
	}

	public void setAlternateMobileNo2(String alternateMobileNo2) {
		this.alternateMobileNo2 = alternateMobileNo2;
	}

	public String getLandlineNo() {
		return landlineNo;
	}

	public void setLandlineNo(String landlineNo) {
		this.landlineNo = landlineNo;
	}

	public String getAlternateLandlineNo1() {
		return alternateLandlineNo1;
	}

	public void setAlternateLandlineNo1(String alternateLandlineNo1) {
		this.alternateLandlineNo1 = alternateLandlineNo1;
	}

	public String getAlternateLandLineNo2() {
		return alternateLandLineNo2;
	}

	public void setAlternateLandLineNo2(String alternateLandLineNo2) {
		this.alternateLandLineNo2 = alternateLandLineNo2;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAlternateEmailId1() {
		return alternateEmailId1;
	}

	public void setAlternateEmailId1(String alternateEmailId1) {
		this.alternateEmailId1 = alternateEmailId1;
	}

	public String getAlternateEmailId2() {
		return alternateEmailId2;
	}

	public void setAlternateEmailId2(String alternateEmailId2) {
		this.alternateEmailId2 = alternateEmailId2;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public AddressVO getAddressVO() {
		return addressVO;
	}

	public void setAddressVO(AddressVO addressVO) {
		this.addressVO = addressVO;
	}

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	public String getRecordTypeDesc() {
		return recordTypeDesc;
	}

	public void setRecordTypeDesc(String recordTypeDesc) {
		this.recordTypeDesc = recordTypeDesc;
	}    
}