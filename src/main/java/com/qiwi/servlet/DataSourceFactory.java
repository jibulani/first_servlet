package com.qiwi.servlet;

/**
 * Created by etrofimov on 13.07.17.
 */

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

public class DataSourceFactory {

    public static DataSource getPostgreDataSource() {
        PGSimpleDataSource pgDS = new PGSimpleDataSource();
        pgDS.setUrl("jdbc:postgresql://localhost:5432/etrofimov");
        pgDS.setUser("etrofimov");
        pgDS.setPassword("1234");
        return pgDS;
    }
}
