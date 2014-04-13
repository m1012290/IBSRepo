/**
 * 
 */
package com.shrinfo.ibs.vo.app;


/**
 * @author Sunil kumar
 *
 */
public enum SectionId {
    ENQUIRY(1),
    QUOTESLIP(2),
    CLOSINGSLIP(3),
    POLICY(4),
    ENDORSEMENT(5);
    
    private int sectionId;
    private SectionId(int sectionId){
        this.sectionId = sectionId;
    }
    
    public int getSectionId(){
        return this.sectionId;
    }
}
