package com.qiwi.servlet;

import java.security.MessageDigest;

/**
 * Created by etrofimov on 17.07.17.
 */
public class HashCodeGenerator {
    public static String getHashCode(AgentRequest agentRequest) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(agentRequest.getPassword().getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
