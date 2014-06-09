/**
 * 
 */
package com.shrinfo.ibs.vo.business;

import java.util.List;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil kumar
 * This will be used for fetching list of available customers in the system
 */
public class CustomersListVO extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1901644199652212784L;
	private List<CustomerVO> customersList;

	public List<CustomerVO> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<CustomerVO> customersList) {
		this.customersList = customersList;
	}
	
}
