package com.shrinfo.ibs.base.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.shrinfo.ibs.cmn.constants.CommonErrorKeys;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.logger.Logger;


/**
 * This class<code>BaseDBDAO</code> contains all common database related activities.
 * like save ,delete ,update
 */
public class BaseDBDAO{
    private HibernateTemplate hibernateTemplate;

    private Logger log = Logger.getLogger( this.getClass() );

    /**
     * This method will delete records from table
     * @param entity
     * @throws SystemException
     */

    public void delete( Object entity ) throws SystemException{
        try{
            hibernateTemplate.delete( entity );
        }
        catch( Exception e ){
            log.error( e, "BaseDAO.delete(): Error when trying to delete entity of type [" + entity.getClass().getName(), "]" );
            throw new SystemException( CommonErrorKeys.COULD_NOT_DELETE_RECORD, e, "BaseDAO.delete(): Error when trying to delete entity of type [", entity.getClass().getName(), "]" );
        }
    }

    /**
     * This method will delete records from table
     * @param entity
     * @throws SystemException
     */

    public void deleteAll( List<?> entityList ) throws SystemException{
        try{
            hibernateTemplate.deleteAll( entityList );
        }
        catch( Exception e ){
            log.error( e, "BaseDAO.deleteAll(): Error when trying to execute hibernateTemplate.deleteAll()" );
            throw new SystemException( CommonErrorKeys.COULD_NOT_DELETE_RECORD, e, "BaseDAO.deleteAll(): Error when trying to execute hibernateTemplate.deleteAll()" );
        }
    }

    /**
     * @return the hibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate(){
        return hibernateTemplate;
    }

    /**
     * Fetches the latest date from the database. This date will be used for
     * updating Created On / Updated On attributes for every Entity 
     * 
     * @return Timestamp
     */

    public Timestamp getSysDate(){
        HibernateCallback hibernateCallBack = new HibernateCallback(){
            public Object doInHibernate( Session session ) throws HibernateException, SQLException{
                Timestamp date = null;
                String querySystemDate = "select sysdate from dual";
                date = (Timestamp) session.createSQLQuery( querySystemDate ).addScalar( "sysdate", Hibernate.TIMESTAMP )
                        .uniqueResult();
                return date;
            }
        };
        return (Timestamp) hibernateTemplate.execute( hibernateCallBack );
    }

    /**
     * This method compares the input date with the system date. It returns true if the input date is after the system date. 
     * @param inputDate
     * @return Boolean
     */
    public Boolean isDateAfterSysdate( Date inputDate ){
        Date systemDate = new Date( this.getSysDate().getTime() );
        if( inputDate == null ){
            throw new BusinessException( CommonErrorKeys.INPUT_IS_NULL, null, "Input date to method isDateAfterSysdate() cannot be null " );
        }
        return inputDate.after( systemDate );
    }

    /**
     * This method compares the input date with the system date. It returns true if the input date is before the system date. 
     * @param inputDate
     * @return Boolean
     */
    public Boolean isDateBeforeSysdate( Date inputDate ){
        Date systemDate = new Date( this.getSysDate().getTime() );
        if( inputDate == null ){
            throw new BusinessException( CommonErrorKeys.INPUT_IS_NULL, null, "Input date to method isDateBeforeSysdate() cannot be null");
        }
        return inputDate.before( systemDate );
    }

    /**
     * This method will save the object
     * @param entity
     * @throws SystemException
     */
    @SuppressWarnings( "unchecked" )
    public void save( Object entity ) throws SystemException{
        try{
            hibernateTemplate.save( entity );
        }
        catch( Exception e ){
            log.error( e, "BaseDAO.save(): Error when trying to save entity of type [", entity.getClass().getName(), "]" );
            throw new SystemException( CommonErrorKeys.COULD_NOT_INSERT_RECORD, e, "BaseDAO.save(): Error when trying to save entity of type [", entity.getClass().getName(), "]" );
        }

    }

    /**
     * This method will save or update based on object exist or not.
     * @param entity
     * @throws SystemException
     */
    public void saveOrUpdate( Object entity ) throws SystemException{
        try{
            hibernateTemplate.saveOrUpdate( entity );
        }
        catch( Exception e ){
            log.error( e, "BaseDAO.saveOrUpdate(): Error when trying to save/update entity of type [", entity.getClass().getName(), "]" );
            throw new SystemException( CommonErrorKeys.COULD_NOT_SAVE_RECORD, e, "BaseDAO.saveOrUpdate(): Error when trying to save/update entity of type [", entity.getClass().getName(), "]" );
        }
    }

    /**
     * This method will save or update based on object exist or not.
     * @param entity
     * @throws SystemException
     */
    public void saveOrUpdateAll( List<?> entityList ) throws SystemException{
        try{
            hibernateTemplate.saveOrUpdateAll( entityList );
        }
        catch( Exception e ){
            log.error( e, "BaseDAO.saveOrUpdateAll(): Error when trying to save/update entity of type" );
            throw new SystemException( CommonErrorKeys.COULD_NOT_SAVE_RECORD, e, "BaseDAO.saveOrUpdateAll(): Error when trying to save/update entity list" );
        }
    }

    /**
     * @param hibernateTemplate the hibernateTemplate to set
     */
    public void setHibernateTemplate( HibernateTemplate hibernateTemplate ){
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * This method will update the persistent object
     * @param entity
     * @throws SystemException
     */
    public void update( Object entity ) throws SystemException{
        try{
            hibernateTemplate.merge( entity );
        }
        catch( Exception e ){
            log.error( e, "BaseDAO.update(): Error when trying to update (merge) entity of type [", entity.getClass().getName(), "]" );
            throw new SystemException( CommonErrorKeys.COULD_NOT_UPDATE_RECORD, e, "BaseDAO.update(): Error when trying to update (merge) entity of type [", entity.getClass().getName(), "]" );
        }
    }
}