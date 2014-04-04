package com.shrinfo.ibs.cmn.mvc;

import javax.servlet.ServletConfig;

import com.shrinfo.ibs.cmn.constants.CommonConstants;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;

/**
 * This API executes all the configured "web-init" tasks. Web-init tasks are expected to be executed when the application is starting up.
 * A filter may need to be written to call this API.
 * 
 * @author Sunil Kumar
 */
public class WebInitExecutor{
	private static final Logger logger = Logger.getLogger( WebInitExecutor.class );

	/**
	 * Executes all the configured web-init tasks against the application configuration entry <code>WEB_INITIALIZERS</code>, which is
	 * expected to be a comma-separated multi-value list with fully qualified names of classes that implement 
	 * <code>com.mindtree.ruc.mvc.init.IWebInitTask</code>.
	 * 
	 * @param config The <code>ServletConfig</code> instance that may be used in the task classes.
	 */
	public static void execute( ServletConfig config ){
		/* Get the list of initialisers. These are expected to be listed as comma-separated fully qualified class names. */
		String[] initializers = Utils.getMultiValueAppConfig( CommonConstants.APP_CONFIG_WEB_INITIALIZERS );
		
		if( !Utils.isEmpty( initializers ) ){
			for( String initializer : initializers ){
				/* Create a new instance of the initialiser. */
				IWebInitTask task = (IWebInitTask) Utils.newInstance( initializer );

				if( Utils.isEmpty( task ) ){
					if( logger.isError() ){
						logger.error( "RequestInitTask implementation class for [", initializer, "] not found" );
					}
				}
				else{
					task.initialize( config );
				}
			}
		}
	}
}
