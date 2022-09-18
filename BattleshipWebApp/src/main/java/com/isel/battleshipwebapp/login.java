package com.isel.battleshipwebapp;

import com.isel.Server;
import com.isel.utils.XMLUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;


import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "login", value = "/login")
public class login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("loginNickname");
        String password = req.getParameter("loginPassword");

        String tipo = req.getParameter("tipo");

        Document pedido = XMLUtil.login(username, password, "C:\\Users\\anaso\\Desktop\\galaxy.jpg");




        PrintWriter out = resp.getWriter();

        out.println(
        "<!DOCTYPE html>" +
                "<html>" +
                    "<head>" +
                        "<title>Battleship</title>" +
                        "<link rel='stylesheet' type='text/css' href='../css/login.css'>" +
                "</head>" +
                "<body>" +
                "<div id='background'>" +
                "<h1>Battleship Login Servlet</h1>" +
                "</div>" +
                "</body>" +
                "</html>");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
