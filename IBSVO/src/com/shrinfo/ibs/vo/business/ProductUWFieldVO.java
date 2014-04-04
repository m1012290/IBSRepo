package com.shrinfo.ibs.vo.business;

import com.shrinfo.ibs.cmn.vo.BaseVO;

/**
 * @author Sunil Kumar This class represents Underwriting field details corresponding to a
 *         particular product class.
 */
public class ProductUWFieldVO extends BaseVO implements Comparable<ProductUWFieldVO> {

    private static final long serialVersionUID = -8547918153558242262L;

    // this holds the primary key of the child(detail) tables
    // e.g. qoute_slip_detail, quote_comparison_detail, uw_transaction_detail
    private Long tableId;

    private Long uwFieldId;

    private int productClass;

    private int fieldOrder;

    private String fieldName;

    private String fieldValue;

    private String fieldType;

    private String isMandatory;

    private int fieldLength;

    // private String srcOfData;

    private String response;



    public Long getTableId() {
        return tableId;
    }


    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getUwFieldId() {
        return uwFieldId;
    }

    public void setUwFieldId(Long uwFieldId) {
        this.uwFieldId = uwFieldId;
    }

    public int getProductClass() {
        return productClass;
    }

    public void setProductClass(int productClass) {
        this.productClass = productClass;
    }

    public int getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(int fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public int getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int compareTo(ProductUWFieldVO arg0) {
        if (this.fieldOrder > arg0.fieldOrder)
            return 1;
        if (this.fieldOrder < arg0.fieldOrder)
            return -1;
        return 0;
    }
}
