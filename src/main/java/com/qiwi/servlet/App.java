package com.qiwi.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class App extends HttpServlet
{

    private String responseTemplate =
            "<html>\n" +
                    "<body>\n" +
                    "<h2>Hello from Simple Servlet</h2>\n" +
                    "</body>\n" +
                    "</html>";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String respMsg = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response><result-code>";

        try {
            Agent agent = (Agent) JaxbParser.getObject(request.getInputStream(), Agent.class);
            int resultCode;

            if (agent.requestType.equals("new-agt")) {
                resultCode = ClientServiceSingleton.getInstance().registerNewAgent(agent.login, agent.password).getStatusCode();
                respMsg += resultCode + "</result-code></response>";
            }

            else if (agent.requestType.equals("agt-bal")) {
                Response result = ClientServiceSingleton.getInstance().getBalance(agent.login, agent.password);
                respMsg += result.code.getStatusCode() + "</result-code><bal>" + result.balance + "</bal></response>";
            }

            response.setStatus(200);
            response.getWriter().write(respMsg);

        } catch (Exception e) {
            respMsg += "5</result-code></response>";
            response.setStatus(200);
            response.getWriter().write(respMsg);
            e.printStackTrace();
            return;
        }
    }

//        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//        String xml = "";
//        String line = null;
//        while ((line = in.readLine()) != null) {
//            xml += line;
//        }
//
//        String requestTypeStartTag = "<request-type>";
//        String requestTypeEndTag = "</request-type>";
//        int start = xml.indexOf(requestTypeStartTag);
//        int end = xml.indexOf(requestTypeEndTag);
//        String requestType = xml.substring(start + 14, end);
//
//        String requestLoginStartTag = "<login>";
//        String requestLoginEndTag = "</login>";
//        String requestPasswordStartTag = "<password>";
//        String requestPasswordEndTag = "</password>";
//
//        int startLogin = xml.indexOf(requestLoginStartTag);
//        int endLogin = xml.indexOf(requestLoginEndTag);
//
//        int startPassword = xml.indexOf(requestPasswordStartTag);
//        int endPassword = xml.indexOf(requestPasswordEndTag);
//
//        String login = xml.substring(startLogin + 7, endLogin);
//        String password = xml.substring(startPassword + 10, endPassword);

//        int resultCode;
//        if (requestType.equals("new-agt")) {
//            resultCode = ClientServiceSingleton.getInstance().registerNewAgent(login, password).getStatusCode();
//            respMsg += resultCode + "</result-code></response>";
//        }
//
//        else if (requestType.equals("agt-bal")) {
//            Response result = ClientServiceSingleton.getInstance().getBalance(login, password);
//            respMsg += result.code.getStatusCode() + "</result-code><bal>" + result.balance + "</bal></response>";
//        }
//
//        response.setStatus(200);
//        response.getWriter().write(respMsg);
//    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.getWriter().write(responseTemplate);
    }

}
