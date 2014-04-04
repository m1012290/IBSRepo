/**
 * 
 */
package com.shrinfo.ibs.cmn.constants;


/**
 * @author Sunil Kumar
 * The constants class for common error keys. Layer specific error keys are expected to be in classes
 * present in the respective packages.
 */
public class CommonErrorKeys {
    
    private CommonErrorKeys(){} /* To avoid "instantiability". */
    /* DB related constants */
    public static final String COULD_NOT_DELETE_RECORD = "cmn.couldNotDeleteRecord";
    public static final String COULD_NOT_INSERT_RECORD = "cmn.couldNotInsertRecord";
    public static final String COULD_NOT_SAVE_RECORD = "cmn.couldNotSaveRecord";
    public static final String COULD_NOT_UPDATE_RECORD = "cmn.couldNotUpdateRecord";
    public static final String INPUT_IS_NULL = "cmn.inputIsNull";
    public static final String STORED_PROC_ERROR = "cmn.SPError";
    public static final String INSUFFICIENT_ARGS_TO_SP = "cmn.insufficientArgsToSP";
    
    
    public static final String UNKNOWN_ERROR = "cmn.systemError";
    public static final String NO_CACHE_CONFIGURED = "cmn.noCacheConfigured";
    public static final String CLASS_NOT_FOUND = "cmn.classNotFound";
    public static final String CANNOT_INSTANTIATE_CLASS = "cmn.cannotInstantiate";
    public static final String CANNOT_SERIALIZE_OBJECT = "cmn.cannotSerialize";
    public static final String METHOD_NOT_ACCESSIBLE = "cmn.methodNotAccessible";
    public static final String NUMBERFORMAT_EXCEPTION = "cmn.couldNotConvertToInteger";
    public static final String CONFIG_FILE_NOT_FOUND = "cmn.configFileNotFound";
    public static final String REDIRECTION_CONFIG_INVALID = "cmn.redirectionConfigInvalid";
    public static final String INVALID_CONFIGURATION = "cmn.invalidConfiguration";
}
