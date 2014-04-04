/**
 * 
 */
package com.shrinfo.ibs.cmn.exception;

import java.util.List;

import com.shrinfo.ibs.cmn.utils.Utils;


/**
 * @author Sunil Kumar
 */
public class BusinessException extends BaseException{
    
    private static final long serialVersionUID = 3025604381937322929L;

    /**
     * This is the most recommended constructor if you have only one error key. If you don't have a <code>cause</code>
     * to pass, pass it as <code>null</code>.
     * 
     * @param errorKey
     * @param cause 
     * @param messageParts The exception message passed as parts to avoid concatenation operator
     */
    
    public BusinessException( String errorKey, Throwable cause, String... messageParts ){
        super( Utils.concat( messageParts ), errorKey, cause );
    }
    
    /**
     * This constructor is expected to be used after validation. All the error keys can be set using this constructor.
     * 
     * @param errorKeys Error keys after validation
     * @param messageParts The exception message passed as parts to avoid concatenation operator
     */
    public BusinessException( List<String> errorKeys, String... messageParts ){
        super( Utils.concat( messageParts ), errorKeys  );
    }

}
