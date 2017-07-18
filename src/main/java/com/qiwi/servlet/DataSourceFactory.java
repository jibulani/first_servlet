package com.qiwi.servlet;

/**
 * Created by etrofimov on 13.07.17.
 */

import javax.sql.DataSource;

import org.apache.commons.dbcp.*;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DataSourceFactory {


    public static BasicDataSource getPostgreDataSource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/etrofimov");
        ds.setUsername("etrofimov");
        ds.setPassword("1234");

        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);

        return ds;
    }
}
