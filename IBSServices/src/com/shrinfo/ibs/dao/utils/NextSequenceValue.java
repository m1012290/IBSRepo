package com.shrinfo.ibs.dao.utils;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.logger.Logger;
import com.shrinfo.ibs.cmn.utils.Utils;

public class NextSequenceValue extends BaseDBDAO {

    private final static Logger logger = Logger.getLogger(NextSequenceValue.class);

    private final static String sqlBuilderSel = "select ";

    private final static String sqlBuilderFrm = ".NEXTVAL as id  from dual";

    public final static Long getNextSequence(String sequenceName,
            HibernateTemplate hibernateTemplate) {
        Long data = null;
        if (logger.isInfo())
            logger.info("Enteing getNextSequence() to get next sequence for " + sequenceName);

        if (!Utils.isEmpty(sequenceName)) {
            SessionFactory factory = hibernateTemplate.getSessionFactory();
            Session session = factory.openSession();
            data =
                (Long) session.createSQLQuery(sqlBuilderSel + sequenceName + sqlBuilderFrm)
                        .addScalar("id", Hibernate.LONG).uniqueResult();
        }

        if (logger.isDebug())
            logger.debug("Sequence generated for " + sequenceName + " is " + data);

        if (logger.isInfo())
            logger.info("Exiting getNextSequence()");

        return data;

    }

}
