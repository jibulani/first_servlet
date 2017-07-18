package com.qiwi.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class App extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String respMsg = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response><result-code>";

        try {
            AgentRequest agentRequest = (AgentRequest) JaxbParser.getObject(request.getInputStream(), AgentRequest.class);
            int resultCode;

            if (agentRequest.getRequestType().equals("new-agt")) {
                resultCode = ClientServiceSingleton.getInstance().registerNewAgent(agentRequest).getStatusCode();
                respMsg += resultCode + "</result-code></response>";
            }

            else if (agentRequest.getRequestType().equals("agt-bal")) {
                Response result = ClientServiceSingleton.getInstance().getBalance(agentRequest);
                respMsg += result.getCode().getStatusCode() + "</result-code><bal>" + result.getBalance() + "</bal></response>";
            }

            response.setStatus(200);
            response.getWriter().write(respMsg);

        } catch (Exception e) {
            respMsg += "5</result-code></response>";
            response.setStatus(200);
            response.getWriter().write(respMsg);
            e.printStackTrace();
        }
    }


    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(200);
    }

}
