package com.qiwi.servlet;

/**
 * Created by etrofimov on 12.07.17.
 */
public class ClientServiceSingleton {
    private static ClientServiceSingleton ourInstance = new ClientServiceSingleton();

    public static ClientServiceSingleton getInstance() {
        return ourInstance;
    }

    private ClientServiceSingleton() {
    }

    static boolean isRightLogin(String login) {
        if (login.length() > 11) {
            return false;
        }
        for (int i = 0; i < login.length(); i++) {
            if (!Character.isDigit(login.charAt(i))) return false;
        }
        return true;
    }

    static boolean isLoginExists(String login) {
        if (login != null) return true;
        return false;
    }

    static boolean isPasswordExists(String password) {
        if (password != null) return true;
        return false;
    }

    Status registerNewAgent(AgentRequest agentRequest) {
        String login = agentRequest.getLogin();
        if (!isLoginExists(login) || !isRightLogin(login)) {
            return Status.WRONG_FORMAT;
        }

        String password = agentRequest.getPassword();
        if (!isPasswordExists(password) || (password.length() < 8) || (password.length() > 20)) {
            return Status.BAD_PASSWORD;
        }

        return ClientDaoSingleton.getInstance().addNewUser(agentRequest);
    }

    Response getBalance(AgentRequest agentRequest) {
        String login = agentRequest.getLogin();
        if (!isLoginExists(login) || !isRightLogin(login)) {
            return new Response(Status.WRONG_FORMAT);
        }

        String password = agentRequest.getPassword();
        if (!isPasswordExists(password)) {
            return new Response(Status.BAD_PASSWORD);
        }

        return ClientDaoSingleton.getInstance().getUserBalance(agentRequest);
    }
}
