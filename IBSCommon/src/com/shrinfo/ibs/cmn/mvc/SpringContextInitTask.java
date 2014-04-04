/**
 * 
 */
package com.shrinfo.ibs.cmn.mvc;

import javax.servlet.ServletConfig;

import com.shrinfo.ibs.cmn.constants.CommonConstants;
import com.shrinfo.ibs.cmn.utils.Utils;

/**
 * @author Sunil Kumar
 */
public class SpringContextInitTask implements IWebInitTask{

    @Override
    public void initialize(ServletConfig servletConfig) {
        initializeUtils(servletConfig);
    }
    
    /**
     * The Utils class needs to be initialized with some information. This is being done here. Eg, the application's
     * context file for Spring is loaded here and Utils made aware of it. Any more initialization can be done in an
     * overridden method in the concrete implementation of this class.
     * 
     * @param config The ServletConfig instance from the web context
     */
    private void initializeUtils( ServletConfig config ){
        /* Load the Spring context for the application here and set it to Utils. applicationContext.xml should be directly under
         * a CLASSPATH directory and not deep within one. */
        String springConfigPath = Utils.getSingleValueAppConfig( CommonConstants.APP_CONFIG_SPRING_APP_CONTEXT_PATH, CommonConstants.APP_CONTEXT_PATH );
        
        //if( !Boolean.TRUE.equals( config.getServletContext().getAttribute( CommonConstants.SESSION_APP_CONTEXT_LOADED ) ) ){
            Utils.setAppContext( Utils.loadSpringBeansFactory( springConfigPath ) );
        //}
    }
}