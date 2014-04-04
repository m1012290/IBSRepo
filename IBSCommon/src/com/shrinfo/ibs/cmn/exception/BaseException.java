package com.shrinfo.ibs.cmn.exception;

import java.util.ArrayList;
import java.util.List;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;

/**
 * 
 * @author Sunil Kumar
 */
public abstract class BaseException extends RuntimeException {
    
    private static final long serialVersionUID = -8447536661250618513L;
    private List<String> errorKeysList;
    private Object exceptionData;
    
    Logger log  = Logger.getLogger(this.getClass());

    /**
     * @see SystemException, BusinessException
     * @param message
     * @param cause
     */
    protected BaseException( String message, Throwable cause ){
        super( message, cause );
    }

    /**
     * The message for the consumer of the exception
     * 
     * @param message - The message for the consumer of the exception
     * @param errorKey - The errorKey that can be used to decide the error message to be
     * shown (on UI, for example, or for logging). Typically, this will configured in
     * messageBundle.properties (Struts).
     */
    protected BaseException( String message, String errorKey, Throwable cause ){
        super( message, cause );
        this.addErrorKey( errorKey );
    }    
    
    /**
     * The message for the consumer of the exception
     * 
     * @param message - The message for the consumer of the exception
     * @param errorKeys - The errorKeys that can be used to decide the error messages to be
     * shown (on UI, for example, or for logging). Typically, this will configured in
     * messageBundle.properties (Struts).
     */
    protected BaseException( String message, List<String> errorKeys ){
        super( message );
        if(errorKeys != null && !errorKeys.isEmpty()){
            this.errorKeysList = new ArrayList<String>(errorKeys.size());
            for( int index = 0; index < errorKeys.size() ; index++){
                this.errorKeysList.add(errorKeys.get( index ));
            }
        }
    }
    
    /**
     * Adds an error key into exceptions error key list.
     * 
     * @param errorKey
     */
    public void addErrorKey( String errorKey ){
        if( Utils.isEmpty( this.errorKeysList ) ){
            this.errorKeysList = new ArrayList<String>( 1 ); /* There won't be more than 1 error key usually. 
                                                              * Hence, initialising with 1. */
        }
        
        this.errorKeysList.add( errorKey );
    }

    /**
     * Returns the list of error keys set in this exception.
     *
     * @return List of error keys associated with the exception.
     */
    public List<String> getErrorKeysList(){
        return errorKeysList;
    }

    /**
     * Returns an object that is associated with the exception.
     * @return
     */
    public Object getExceptionData(){
        return exceptionData;
    }

    /**
     * Sets an object associated with the exception.
     * @param exceptionData
     */
    public void setExceptionData( Object exceptionData ){
        this.exceptionData = exceptionData;
    }
}