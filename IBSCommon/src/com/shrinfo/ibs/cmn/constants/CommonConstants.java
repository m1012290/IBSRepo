/**
 * 
 */
package com.shrinfo.ibs.cmn.constants;


/**
 * @author Sunil Kumar
 * Commonly used constants which would be used across the application would be added up 
 * here. For ex - APP_NAME, Enquiry types available etc
 */
public class CommonConstants {

    public static final String APP_NAME = "IBS";
    
    // Available enquiry types in the application
    public static final String ENQUIRY_TYPE_POLICY = "POLICY";
    public static final String ENQUIRY_TYPE_ENDORSEMENT = "ENDORSEMENT";
    public static final String ENQUIRY_TYPE_RENEWAL = "RENEWAL";
    public static final String ENQUIRY_TYPE_CLAIMS = "CLAIMS";
    
    //Default logging level configuration
    public static final String APP_CONFIG_DEFAULT_LOG_LEVEL = "DEFAULT_LOG_LEVEL";
    
    /* Session attributes */
    public static final String SESSION_USER_PROFILE = "userProfile";
    public static final String SESSION_APP_CONTEXT_LOADED = "appContextLoaded";
    public static final String SESSION_IS_USER_LOGGED_IN = "isUserLoggedIn";
    
    public static final String APP_CONFIG_SPRING_APP_CONTEXT_PATH = "SPRING_APP_CONTEXT_PATH";
    
    /* General constants */
    public static final String APP_CONTEXT_PATH = "config/applicationcontext.xml";
    public static final String WEB_XML_PARAM_APP_NAME = "appName";
    
    public static final String APP_CONFIG_WEB_INITIALIZERS = "WEB_INITIALIZERS";
}
