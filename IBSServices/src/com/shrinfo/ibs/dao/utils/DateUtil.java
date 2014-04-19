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
     * @return
     */
    public static Timestamp constructTimeStamp(Date date) {

        if (Utils.isEmpty(date)) {
            date = new Date(System.currentTimeMillis());
        }
        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp;
    }

}
