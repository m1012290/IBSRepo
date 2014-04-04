package com.shrinfo.ibs.cmn.exception;

import java.util.List;

import com.shrinfo.ibs.cmn.utils.Utils;

/**
 * @author Sunil Kumar
 */
public class SystemException extends BaseException{

    private static final long serialVersionUID = -4008595735445636072L;

    /**
     * This constructor can be used if the error key is the default, the one for "Unknown error". 
     * 
     * @param cause The root cause 3rd party exception
     * @param messageParts The custom, scenario-specific message passed as parts (to avoid concatenation)
     */
    public SystemException( Throwable cause, String... messageParts ){
        super( Utils.concat( messageParts ), cause );
    }

    /**
     * This is the most recommended constructor if you have only one error key. If you don't have a <code>cause</code>
     * to pass, pass it as <code>null</code>.
     * 
     * @param errorKey
     * @param cause 
     * @param messageParts The exception message passed as parts to avoid concatenation operator
     */
    public SystemException( String errorKey, Throwable cause, String... messageParts ){
        super( Utils.concat( messageParts ), errorKey, cause );
    }
    
    /**
     * This constructor is expected to be used after validation. All the error keys can be set using this constructor.
     * 
     * @param errorKeys Error keys after validation
     * @param messageParts The exception message passed as parts to avoid concatenation operator
     */
    public SystemException( List<String> errorKeys, String... messageParts ){
        super( Utils.concat( messageParts ), errorKeys  );
    }
}
