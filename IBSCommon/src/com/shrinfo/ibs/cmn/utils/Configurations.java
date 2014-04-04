package com.shrinfo.ibs.cmn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.shrinfo.ibs.cmn.constants.CommonErrorKeys;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;

/**
 * A utility to manage configuration files in read-only mode. The configuration files should be 
 * .properties files.
 */
public enum Configurations{
    INSTANCE;
    
    private ConcurrentMap<String, Properties> resourceBundles = new ConcurrentHashMap<String, Properties>();
    private static Logger logger = Logger.getLogger( Configurations.class );
    
    /**
     * Reloads the specified bundle.
     * 
     * @param bundle The bundle file name with path relative to the classpath. Eg., "config/cmvcommon.properties"
     */
    public void reload( String bundle ){
        load( bundle, true );
    }

    /**
     * Gets the property from the specified resource bundle. If the resource bundle has already
     * been loaded, gets the property from it. If it has not been loaded, then first loads the bundle
     * and then gets the property.
     *  
     * @param key The key for which the value is required
     * @param bundle The bundle in which to look for
     * @return
     */
    public String getProperty( String key, String bundle ){
        return getProperty0( key, bundle );
    }
    
    private String getProperty0( String key, String bundle ){
        Properties props = load( bundle, false );
        
        return props.getProperty( key );
    }

    /**
     * Gets multi-valued properties from the specified resource bundle, the values separated at comma. This is equivalent
     * to calling <code>getMultiValuedProperty( String key, String bundle, String separator )</code> passing comma as the
     * separator. This convenience method has been added to encourage the use of comma as the standard separator as far
     * as possible.
     * 
     * @param key The key for which the value is required
     * @param bundle The bundle in which to look for
     * 
     * @return The configured value split at comma (',')
     */
    public String[] getMultiValuedProperty( String key, String bundle ){
        return getMultiValuedProperty( key, bundle, "," );
    }
    
    /**
     * Gets multi-valued properties from the specified resource bundle. A multi-valued property can be configured
     * comma-separated in a properties file and this method will split the configured value at <code>separator</code> 
     * and return the values.
     * 
     * @param key The key for which the value is required
     * @param bundle The bundle in which to look for
     * @param separator The separator character at which to split the configured value
     * 
     * @return The configured value split at <code>separator</code>
     */
    public String[] getMultiValuedProperty( String key, String bundle, String separator ){
        String[] multiValues = null;
        String value = getProperty0( key, bundle );
        
        if( !Utils.isEmpty( value ) ){
            multiValues = value.split( separator );
        }
        return multiValues;
    }
    
    private Properties load( String bundle, boolean reload ){
        Properties props = resourceBundles.get( bundle );

        /* No need to load if the properties have been loaded and it is not a case of
         * reload. */
        if( !reload && !Utils.isEmpty( props ) ){
            return props;
        }
        
        /* Open an input stream to the file. */
        java.io.FileInputStream fin = null;
        try{
            java.net.URL urlToFile = this.getClass().getClassLoader().getResource( bundle );
            
            fin = new FileInputStream( urlToFile.getFile() );
        }
        catch( FileNotFoundException e ){
            throw new SystemException( CommonErrorKeys.CONFIG_FILE_NOT_FOUND, e, "Bundle not found [" + bundle + "]" );
        }/*
        catch( URISyntaxException e ){
            throw new SystemException( CommonErrorKeys.CONFIG_FILE_NOT_FOUND, e, "URI syntax exception" );
        }*/
        
        /* Shun the current instance and create a new one. */
        props = new Properties();
        try{        
            props.load( fin );
            resourceBundles.put( bundle, props );           
            fin.close();
        }
        catch( IOException e ){
            if( logger.isError() ) logger.error( "Couldn't load properties file [" + bundle + "]" );
            throw new SystemException( CommonErrorKeys.CONFIG_FILE_NOT_FOUND, e, "Exception while trying to load properties file" );
        }
        
        return props;
    }

    /**
     * Returns an unmodifiable Set containing all keys in the specified bundle.
     * 
     * @param bundle The bundle from which all keys are required
     * @return An unmodifiable <code>java.util.Set&lt;Object&gt;</code> of all keys in the bundle
     */
    public Set<Object> allKeys( String bundle ){
        Properties props = load( bundle, false );
        
        return Utils.isEmpty( props ) ? null : java.util.Collections.unmodifiableSet( props.keySet() );
    }
    
    /**
     * Returns an unmodifiable Map containing all entries in the specified bundle.
     * 
     * @param bundle The bundle from which all keys are required
     * @return An unmodifiable <code>java.util.Map&lt;String, String&gt;</code> of all entries in the bundle
     */
    public Map<String, String> allEntries( String bundle ){
        Properties props = load( bundle, false );
        Map<String, String> allEntries = null;
        
        formMap: {
            if( Utils.isEmpty( props ) ){
                break formMap;
            }

            Set<Object> keys = props.keySet();
            if( Utils.isEmpty( keys ) ){
                break formMap;
            }

            /* Get the values one-by-one and create a map with all the entries. */
            allEntries = new HashMap<String, String>();
            for( Object keyObj : keys ){
                String key = (String) keyObj;
                allEntries.put( key, props.getProperty( key ) );
            }
            
            /* Create an unmodifiable view of the map. */
            allEntries = Collections.unmodifiableMap( allEntries );
        }
        
        return allEntries;
    }
}