package com.qiwi.servlet;

import java.math.BigDecimal;
import java.sql.*;
import javax.sql.DataSource;

/**
 * Created by etrofimov on 12.07.17.
 */
public class ClientDaoSingleton {
    private static ClientDaoSingleton ourInstance = new ClientDaoSingleton();

    public static ClientDaoSingleton getInstance() {
        return ourInstance;
    }

    public DataSource ds = DataSourceFactory.getPostgreDataSource();

    private ClientDaoSingleton() {
    }

    Status addNewUser(String login, String password) {
//        DataSource ds = DataSourceFactory.getPostgreDataSource();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            c = ds.getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users;");
            while (rs.next()) {
                String currLogin = rs.getString("telephone");
                if (currLogin.equals(login)) {
                    return Status.EXISTS;
                }
            }
            stmt = c.createStatement();
            stmt.execute("INSERT INTO users (telephone, pwd) VALUES ( '" + login + "', '" + password + "');");
        } catch (Exception e) {
            return Status.OTHER;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                return Status.OTHER;
            }
        }
        return Status.OK;
    }


//        Connection c = null;
//        Statement stmt = null;
//        try {
//            Class.forName("org.postgresql.Driver");
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/etrofimov", "etrofimov", "1234");
//            c.setAutoCommit(false);
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM users;");
//            while (rs.next()) {
//                String currLogin = rs.getString("telephone");
//                if (currLogin.equals(login)) {
//                    return Status.EXISTS;
//                }
//            }
//
//            stmt = c.createStatement();
//            stmt.execute("INSERT INTO users (telephone, pwd) VALUES ( '" + login + "', '" + password + "');");
//            stmt.close();
//            c.commit();
//            c.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return Status.OTHER;
//        }
//        return Status.OK;
//    }

    Response getUserBalance(String login, String password) {
//        DataSource ds = DataSourceFactory.getPostgreDataSource();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        double bal;
        try {
            c = ds.getConnection();
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users;");
            boolean isUserExists = false;
            boolean isRightPassword = false;
            while (rs.next()) {
                String currLogin = rs.getString("telephone");
                if (currLogin.equals(login)) {
                    isUserExists = true;
                    String currPassword = rs.getString("pwd");
                    if (currPassword.equals(password)) {
                        isRightPassword = true;
                        break;
                    }
                }
            }
            if (!isUserExists) {
                return new Response(Status.NOT_EXISTS);
            }
            if (!isRightPassword) {
                return new Response(Status.WRONG_PASSWORD);
            }
            rs = stmt.executeQuery("SELECT balance FROM user_balance WHERE telephone='" + login + "';");
            if (rs.next()) {
                bal = rs.getDouble("balance");
            }
            else {
                return new Response(Status.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new Response(Status.OTHER);
        }
        finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (c != null) c.close();
            }
            catch (SQLException e) {
                return new Response(Status.OTHER);
            }
        }
        return new Response(Status.OK, new BigDecimal(bal));
    }


//        try {
//            Class.forName("org.postgresql.Driver");
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/etrofimov", "etrofimov", "1234");
//            c.setAutoCommit(false);
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM users;");
//            boolean isUserExists = false;
//            boolean isRightPassword = false;
//            while (rs.next()) {
//                String currLogin = rs.getString("telephone");
//                if (currLogin.equals(login)) {
//                    isUserExists = true;
//                    String currPassword = rs.getString("pwd");
//                    if (currPassword.equals(password)) {
//                        isRightPassword = true;
//                        break;
//                    }
//                }
//            }
//            if (!isUserExists) {
//                return new Response(Status.NOT_EXISTS);
//            }
//            if (!isRightPassword) {
//                return new Response(Status.WRONG_PASSWORD);
//            }
//            ResultSet balance = stmt.executeQuery("SELECT balance FROM user_balance WHERE telephone='" + login + "';");
//            if (balance.next()) {
//                bal = balance.getDouble("balance");
//            }
//            else {
//                return new Response(Status.NOT_FOUND);
//            }
//        }
//
//        catch (Exception e) {
//            e.printStackTrace();
//            return new Response(Status.OTHER);
//        }
//        return new Response(Status.OK, new BigDecimal(bal));
//    }
}
