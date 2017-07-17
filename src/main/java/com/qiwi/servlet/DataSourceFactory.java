package com.qiwi.servlet;

/**
 * Created by etrofimov on 13.07.17.
 */

import javax.naming.*;
import javax.sql.DataSource;

import org.apache.commons.dbcp.*;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.postgresql.ds.PGSimpleDataSource;

public class DataSourceFactory {

    public static DataSource getPostgreDataSource() {
        GenericObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:postgresql://localhost:5432/etrofimov", "etrofimov", "1234");
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        PoolingDriver driver = new PoolingDriver();
        driver.registerPool("newPool", connectionPool);
        PoolingDataSource ds = new PoolingDataSource(connectionPool);
        return ds;

//        DataSource ds = null;
//        try {
//            InitialContext ic = new InitialContext();
//            ds = (DataSource) ic.lookup("jdbc:apache:commons:dbcp:newPool");
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//        return ds;

//        PGSimpleDataSource pgDS = new PGSimpleDataSource();
//        pgDS.setUrl("jdbc:postgresql://localhost:5432/etrofimov");
//        pgDS.setUser("etrofimov");
//        pgDS.setPassword("1234");
//        return pgDS;
    }
}
