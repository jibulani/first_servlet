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

    private DataSource ds = DataSourceFactory.getPostgreDataSource();

    private ClientDaoSingleton() {
    }

    Status addNewUser(AgentRequest agentRequest) {//String login, String password) {
        String login = agentRequest.getLogin();
        Connection c = null;
        PreparedStatement selectUsersStmt = null;
        PreparedStatement insertUserStmt = null;
        PreparedStatement insertBalanceStmt = null;
        ResultSet rs = null;
        String selectUsers = "SELECT * FROM users;";
        String insertUser = "INSERT INTO users (telephone, pwd) VALUES ( ? , ? );";
        String insertBalance = "INSERT INTO user_balance (telephone, balance) VALUES ( ?, 0 );";
        try {
            c = ds.getConnection();
//            c = DriverManager.getConnection("jdbc:apache:commons:dbcp:newPool");
            c.setAutoCommit(false);
            selectUsersStmt = c.prepareStatement(selectUsers);
            rs = selectUsersStmt.executeQuery();
            c.commit();
            while (rs.next()) {
                String currLogin = rs.getString("telephone");
                if (currLogin.equals(login)) {
                    return Status.EXISTS;
                }
            }
            insertUserStmt = c.prepareStatement(insertUser);
            insertUserStmt.setString(1, login);
            insertUserStmt.setString(2, agentRequest.getHashCode());
            insertUserStmt.executeUpdate();
            c.commit();
            insertBalanceStmt = c.prepareStatement(insertBalance);
            insertBalanceStmt.setString(1, login);
            insertBalanceStmt.executeUpdate();
            c.commit();

        } catch (Exception e) {
            return Status.OTHER;
        } finally {
            try {
                if (rs != null) rs.close();
                if (selectUsersStmt != null) selectUsersStmt.close();
                if (insertUserStmt != null) insertUserStmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                return Status.OTHER;
            }
        }
        return Status.OK;
    }


    Response getUserBalance(AgentRequest agentRequest) { // String login, String password) {
        String login = agentRequest.getLogin();
        Connection c = null;
        ResultSet rs = null;
        PreparedStatement selectUsersStmt = null;
        PreparedStatement selectBalanceStmt = null;
        String selectUsers = "SELECT * FROM users;";
        String selectBalance = "SELECT balance FROM user_balance WHERE telephone = ? ;";
        double bal;
        try {
            c = ds.getConnection();
//            c = DriverManager.getConnection("jdbc:apache:commons:dbcp:newPool");
            c.setAutoCommit(false);
            selectUsersStmt = c.prepareStatement(selectUsers);
            rs = selectUsersStmt.executeQuery();
            c.commit();
            boolean isUserExists = false;
            boolean isRightPassword = false;
            while (rs.next()) {
                String currLogin = rs.getString("telephone");
                if (currLogin.equals(login)) {
                    isUserExists = true;
                    String currPassword = rs.getString("pwd");
                    if (currPassword.equals(agentRequest.getHashCode())) {
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
            selectBalanceStmt = c.prepareStatement(selectBalance);
            selectBalanceStmt.setString(1, login);
            rs = selectBalanceStmt.executeQuery();
            c.commit();
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
                if (selectUsersStmt != null) selectUsersStmt.close();
                if (selectBalanceStmt != null) selectBalanceStmt.close();
                if (c != null) c.close();
            }
            catch (SQLException e) {
                return new Response(Status.OTHER);
            }
        }
        return new Response(Status.OK, new BigDecimal(bal));
    }

}
