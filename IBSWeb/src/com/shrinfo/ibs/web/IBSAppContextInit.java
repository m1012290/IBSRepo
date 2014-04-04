package com.shrinfo.ibs.web;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.mvc.WebInitExecutor;

/**
 * @author Sunil Kumar
 * Loads the spring application context for the IBS web application
 */
public class IBSAppContextInit implements ServletContextListener  {

	private ServletConfig config ;
	private static final Logger logger = Logger.getLogger(IBSAppContextInit.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(final ServletContextEvent arg0) {
		
		if(logger.isInfo())
			logger.debug("Entering contextInitialized method");
		/* 
		 * This initializing is performed, coz the web init method is being used as a listener 
		 * Web init excepts the Servlet Config
		 * Servlet Config is out of scope in aListener  
		 */
		config = new ServletConfig(){
			 public String 	getInitParameter(String name){ return null; }
			 public Enumeration getInitParameterNames(){ return null; }
	         public ServletContext 	getServletContext(){ return arg0.getServletContext(); }
	         public String 	getServletName(){ return null; }
		};
		
		if(logger.isDebug())
			logger.debug("Intializating application context:");
		WebInitExecutor.execute(config);
		
		if(logger.isInfo())
			logger.debug("Exiting contextInitialized method");
	}

}
