package com.shrinfo.ibs.web;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Configurations;
import com.shrinfo.ibs.cmn.utils.Utils;

public class TomcatDataSourceInitContextListener implements ServletContextListener{

    private static final Logger logger = Logger.getLogger( TomcatDataSourceInitContextListener.class );
    
    @Override
    public void contextDestroyed( ServletContextEvent arg0 ){

    }

    @Override
    public void contextInitialized( ServletContextEvent arg0 ){
        try{
            /* Identify deployment on AppServer in order to use data source and connection pooling
             * from the container itself */
            /*
            String appServerDeployment = (String) Utils.getSingleValueAppConfig( AppConstants.APP_SERVER_DEPLOYMENT, AppConstants.NON_APP_SERVER_DEPLOYMENT );
            if( !Utils.isEmpty( appServerDeployment ) && !appServerDeployment.equalsIgnoreCase( AppConstants.NON_APP_SERVER_DEPLOYMENT ) ){
                bindDataSourceFromAppServer();
            }
            else{*/
                bindDataSource();
            /*}*/
        }
        catch( NamingException e ){
            if( logger.isDebug() ) logger.debug( "Error in data source name" );
            e.printStackTrace();
        }
        catch( SQLException e ){
            if( logger.isDebug() ) logger.debug( "Error in connecting to database" );
            e.printStackTrace();
        }
    }

    private static final String APP_CONFIG_PROPERTIES = "config/appconfig.properties";

    private void bindDataSource() throws NamingException, SQLException{
        final String[] dss = Configurations.INSTANCE.getMultiValuedProperty( "TOMCAT_DATA_SOURCES", APP_CONFIG_PROPERTIES );
        //encrypter = new DesEncrypter("Dbpasskey");
        if( !Utils.isEmpty( dss ) ){
            final Context ctx = new InitialContext();
            for( String ds : dss ){
                createDataSource( ctx, ds );
            }
        }
        else{
            if( logger.isInfo() ) logger.info( "No data source configured" );
        }

    }

    private void createDataSource( Context ctx, String dsName ) throws NamingException{
        final String dataSourceName = dsName.trim();
        if( Utils.isEmpty( dataSourceName ) ) return;

        String url = getDSProperty( dataSourceName, "_URL" );
        String user = getDSProperty( dataSourceName, "_USER" );
        String pwd = getDSProperty( dataSourceName, "_PWD" );
        
        DataSource    ds = createDataSourceInstance( dataSourceName, url, user, pwd );
            //ds = createDataSourceInstance( dataSourceName, dbDetails[0], dbDetails[1], dbDetails[2] );
        if( !Utils.isEmpty( ds ) ){
            ctx.rebind( dataSourceName, ds );
        }
        
    }

    private DataSource createDataSourceInstance( String dsName, String url, String user, String pwd ){
        String dataSourceClassName = Utils.getSingleValueAppConfig( Utils.concat( dsName, "_DSCLASS" ), "oracle.jdbc.pool.OracleDataSource" );

        DataSource ds = null;
        try{
            ds = (DataSource) Utils.newInstance( dataSourceClassName );

            if( "oracle.jdbc.pool.OracleDataSource".equals( dataSourceClassName ) ){
                OracleDataSource ods = (OracleDataSource) ds;
                ods.setDriverType( "oci" );
                ods.setNetworkProtocol( "tcp" );
                ods.setURL( url );
                ods.setUser( user );
                ods.setPassword( pwd );
            }
            else{
                String dsConfigClass = Utils.getSingleValueAppConfig( "DSCONFIGCLASS_" + dataSourceClassName );
                
                if( !Utils.isEmpty( dsConfigClass ) ){
                    IDSConfig dsConfig = (IDSConfig) Utils.newInstance( dsConfigClass );
                    dsConfig.configureDS( ds, url, user, pwd );
                }
            }
        }
        /* The expected exception is SQLException. However, since we are not instantiating the data source classes
         * directly, the compiler will not allow catching SQLException. */
        catch( Exception e ){
            if( logger.isDebug() ) logger.debug( "Error in creating the datasource" );
            e.printStackTrace();
        }

        return ds;
    }

    private String getDSProperty( String dataSource, String suffix ){
        return Configurations.INSTANCE.getProperty( Utils.concat( dataSource, suffix ), APP_CONFIG_PROPERTIES );
    }
    
    /**
     * If we are using some other database than Oracle, then this interface can be implemented to set the URL,
     * user and password in the data source class's specific style.<br/><br/>
     * 
     * The main purpose of this interface is to avoid dependencies of JARs that we don't need on actual product-
     * ion environment.
     */
    public static interface IDSConfig{
        public void configureDS( DataSource ds, String url, String user, String password );
    }
    /**
     * Rebinds context with datasource object via lookup when deployed on APP_SERVER
     * @throws NamingException
     */
    private void bindDataSourceFromAppServer() throws NamingException{
        final String[] dss = Configurations.INSTANCE.getMultiValuedProperty( "TOMCAT_DATA_SOURCES", APP_CONFIG_PROPERTIES );
        final Context ctx = new InitialContext();
        for( String ds : dss ){
            ctx.rebind( ds, ctx.lookup( ds ) );
        }
    }
}