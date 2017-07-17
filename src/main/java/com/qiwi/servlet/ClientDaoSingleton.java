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

    Status addNewUser(AgentRequest agentRequest) {
        String login = agentRequest.getLogin();
        String selectUsers = "SELECT * FROM users;";
        String insertUser = "INSERT INTO users (telephone, pwd) VALUES ( ? , ? );";
        String insertBalance = "INSERT INTO user_balance (telephone, balance) VALUES ( ?, 0 );";
        try (
                Connection c = ds.getConnection();
                PreparedStatement selectUsersStmt = c.prepareStatement(selectUsers);
                PreparedStatement insertUserStmt = c.prepareStatement(insertUser);
                PreparedStatement insertBalanceStmt = c.prepareStatement(insertBalance)
        ) {

            c.setAutoCommit(false);
            ResultSet rs = selectUsersStmt.executeQuery();
            c.commit();
            while (rs.next()) {
                String currLogin = rs.getString("telephone");
                if (currLogin.equals(login)) {
                    return Status.EXISTS;
                }
            }
            insertUserStmt.setString(1, login);
            insertUserStmt.setString(2, HashCodeGenerator.getHashCode(agentRequest));
            insertUserStmt.executeUpdate();
            c.commit();
            insertBalanceStmt.setString(1, login);
            insertBalanceStmt.executeUpdate();
            c.commit();

        }
        catch (Exception e) {
            return Status.OTHER;
        }
        return Status.OK;
    }


    Response getUserBalance(AgentRequest agentRequest) {
        String login = agentRequest.getLogin();
        String selectUsers = "SELECT * FROM users;";
        String selectBalance = "SELECT balance FROM user_balance WHERE telephone = ? ;";
        double bal;
        try (
                Connection c = ds.getConnection();
                PreparedStatement selectUsersStmt = c.prepareStatement(selectUsers);
                PreparedStatement selectBalanceStmt = c.prepareStatement(selectBalance)
        ){
            c.setAutoCommit(false);
            ResultSet rs = selectUsersStmt.executeQuery();
            c.commit();
            boolean isUserExists = false;
            boolean isRightPassword = false;
            while (rs.next()) {
                String currLogin = rs.getString("telephone");
                if (currLogin.equals(login)) {
                    isUserExists = true;
                    String currPassword = rs.getString("pwd");
                    if (currPassword.equals(HashCodeGenerator.getHashCode(agentRequest))) {
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
            e.printStackTrace();
            return new Response(Status.OTHER);
        }
        return new Response(Status.OK, new BigDecimal(bal));
    }

}
