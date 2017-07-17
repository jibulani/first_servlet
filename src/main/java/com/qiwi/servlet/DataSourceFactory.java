package com.qiwi.servlet;

/**
 * Created by etrofimov on 13.07.17.
 */

import javax.sql.DataSource;

import org.apache.commons.dbcp.*;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DataSourceFactory {

    public static DataSource getPostgreDataSource() {
        GenericObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory("jdbc:postgresql://localhost:5432/etrofimov", "etrofimov", "1234");
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        PoolingDriver driver = new PoolingDriver();
        driver.registerPool("newPool", connectionPool);
        PoolingDataSource ds = new PoolingDataSource(connectionPool);
        return ds;
    }
}
