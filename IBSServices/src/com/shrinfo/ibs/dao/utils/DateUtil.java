package com.shrinfo.ibs.dao.utils;

import java.sql.Date;
import java.sql.Timestamp;

import com.shrinfo.ibs.cmn.utils.Utils;


public class DateUtil {

    /**
     * 
     * @param date
     * @return
     */
    public static Date constructSqlDate(java.util.Date date) {

        if (Utils.isEmpty(date)) {
            date = new java.util.Date();
        }
        Date sqlDate = new Date(date.getTime());

        return sqlDate;
    }

    /**
     * 
     * @param date
     * @return
     */
    public static Date constructSqlDate(Timestamp date) {

        if (Utils.isEmpty(date)) {
            date = new Timestamp(System.currentTimeMillis());
        }
        Date sqlDate = new Date(date.getTime());

        return sqlDate;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static Timestamp constructTimestamp(Object obj) {
        if(Utils.isEmpty(obj)) {
            return null;
        }
        Timestamp timestamp = null;
        if(obj instanceof java.sql.Date) {
            timestamp = new Timestamp(((java.sql.Date)obj).getTime());
        } else if (obj instanceof java.util.Date) {
            timestamp = new Timestamp(((java.util.Date)obj).getTime());
        }
        
        return timestamp;
    }

}
