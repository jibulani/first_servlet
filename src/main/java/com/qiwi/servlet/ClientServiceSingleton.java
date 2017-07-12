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

    Status registerNewAgent(String login, String password) {
        if (!isRightLogin(login)) {
            return Status.WRONG_FORMAT;
        }

        if ((password.length() < 8) || (password.length() > 20)) {
            return Status.BAD_PASSWORD;
        }

        return ClientDaoSingleton.getInstance().addNewUser(login, password);
    }

    Response getBalance(String login, String password) {
        if (!isRightLogin(login)) {
            return new Response(Status.WRONG_FORMAT);
        }

        return ClientDaoSingleton.getInstance().getUserBalance(login, password);
    }
}
