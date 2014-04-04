/**
 * 
 */
package com.shrinfo.ibs.vo.business;

import java.util.HashMap;
import java.util.Map;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;


/**
 * @author Sunil Kumar
 */
public class LookupVO extends BaseVO{

    private static final long serialVersionUID = 4728921222695414405L;
    private String category;
    private String level1;
    private String level2;
    private Map<String, String> codeDescMap;
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getLevel1() {
        return level1;
    }
    
    public void setLevel1(String level1) {
        this.level1 = level1;
    }
    
    public String getLevel2() {
        return level2;
    }
    
    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    
    public Map<String, String> getCodeDescMap() {
        if( Utils.isEmpty(codeDescMap)){
            codeDescMap = new HashMap<String, String>();
        }
        return codeDescMap;
    }

    
    public void setCodeDescMap(Map<String, String> codeDescMap) {
        this.codeDescMap = codeDescMap;
    }

   
}
