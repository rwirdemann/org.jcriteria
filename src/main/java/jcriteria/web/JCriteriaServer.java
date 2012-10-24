package jcriteria.web;

import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JCriteriaServer extends org.mortbay.jetty.handler.AbstractHandler {

    public void handle(String s, HttpServletRequest request, HttpServletResponse response, int i) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("<h1>Hello</h1>");
        ((Request) request).setHandled(true);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new JCriteriaServer());
        server.start();
        server.join();
    }
}