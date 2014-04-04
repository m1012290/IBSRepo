package com.shrinfo.ibs.cmn.logger;

import org.apache.log4j.Level;

import com.shrinfo.ibs.cmn.constants.CommonConstants;
import com.shrinfo.ibs.cmn.utils.Utils;

/**
 * This is a wrapper over the underlying logging utility. Works only with Log4J 1.2.12 and higher.
 * @author Sunil Kumar
 */
public class Logger{
    private org.apache.log4j.Logger logger;
    private Level configuredLevel;

    /* The default constructor has been made "private" to discourage direct instantiation */
    private Logger(){
    }

    /**
     * The factory method to get a Logger instance.
     * @param clazz
     * @return
     */
    public static Logger getLogger( Class<?> clazz ){
        Logger thisLogger = new Logger();
        thisLogger.setLogger( org.apache.log4j.Logger.getLogger( clazz ) );
        
        /* Set the configured level for the logger. Default is ERROR. */
        if( Utils.isEmpty( thisLogger.logger.getParent() ) || Utils.isEmpty( thisLogger.logger.getParent().getLevel() ) ){
            /* Get the default log level from app config. If not set, consider default as "ERROR". */
            String level = Utils.getSingleValueAppConfig( CommonConstants.APP_CONFIG_DEFAULT_LOG_LEVEL, "ERROR" );
            thisLogger.configuredLevel = Level.toLevel( level );
        }
        else{
            thisLogger.configuredLevel = thisLogger.logger.getParent().getLevel();
        }
        return thisLogger;
    }

    public org.apache.log4j.Logger getLogger(){
        return logger;
    }

    public void setLogger( org.apache.log4j.Logger logger ){
        this.logger = logger;
    }

    /* Logging methods for FATAL. */
    public void fatal( Throwable t, String... messageParts ){
        logger.fatal( getConcatenatedMessage( messageParts ), t );
    }

    public void fatal( String... messageParts ){
        logger.fatal( getConcatenatedMessage( messageParts ) );
    }

    /* Logging methods for ERROR. */
    public void error( Throwable t, String... messageParts ){
        logger.error( getConcatenatedMessage( messageParts ), t );
    }

    public void error( String... messageParts ){
        logger.error( getConcatenatedMessage( messageParts ) );
    }

    /* Logging methods for WARN. */
    public void warn( Throwable t, String... messageParts ){
        logger.warn( getConcatenatedMessage( messageParts ), t );
    }

    public void warn( String... messageParts ){
        logger.warn( getConcatenatedMessage( messageParts ) );
    }

    /* Logging methods for INFO. */
    public void info( Throwable t, String... messageParts ){
        logger.info( getConcatenatedMessage( messageParts ), t );
    }

    public void info( String... messageParts ){
        logger.info( getConcatenatedMessage( messageParts ) );
    }

    /* Logging methods for DEBUG. */
    public void debug( Throwable t, String... messageParts ){
        logger.debug( getConcatenatedMessage( messageParts ), t );
    }

    public void debug( String... messageParts ){
        logger.debug( getConcatenatedMessage( messageParts ) );
    }

    /* Logging methods for TRACE. */
    public void trace( Throwable t, String... messageParts ){
        logger.trace( getConcatenatedMessage( messageParts ), t );
    }

    public void trace( String... messageParts ){
        logger.trace( getConcatenatedMessage( messageParts ) );
    }

    /* Methods to check logging level. */
    public boolean isFatal(){
        return configuredLevel.toInt() <= Level.FATAL.toInt();
    }

    public boolean isError(){
        return configuredLevel.toInt() <= Level.ERROR.toInt();
    }

    public boolean isWarn(){
        return configuredLevel.toInt() <= Level.WARN.toInt();
    }

    public boolean isInfo(){
        return configuredLevel.toInt() <= Level.INFO.toInt();
    }

    public boolean isDebug(){
        return configuredLevel.toInt() <= Level.DEBUG.toInt();
    }

    public boolean isTrace(){
        return configuredLevel.toInt() <= Level.TRACE.toInt();
    }

    /* Concatenates parts of strings sent by the caller. */
    private String getConcatenatedMessage( String... messageParts ){
        String concatenatedString = null;

        if( Utils.isEmpty( messageParts ) ){
            return concatenatedString;
        }

        if( messageParts.length > 1 ){
            StringBuilder sb = new StringBuilder();
            for( Object message : messageParts ){
                if( !Utils.isEmpty( message ) ){
                    sb.append( message.toString() );
                }
            }

            concatenatedString = sb.toString();
        }
        else{
            String message = messageParts[ 0 ];
            
            if( !Utils.isEmpty( message ) ){
                concatenatedString = message.toString();
            }
        }

        return concatenatedString;
    }
    
    
   
}