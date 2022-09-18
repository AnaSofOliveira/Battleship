package com.isel;

import com.isel.utils.XMLUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@WebServlet("/server")
public class Server extends HttpServlet {

    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 5025;

    static Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;

    public Server(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tipo = req.getParameter("tipo");

        Document mensagem = null;

        switch (tipo) {
            case "login":
                String username = req.getParameter("loginNickname");
                String password = req.getParameter("loginPassword");
                mensagem = XMLUtil.login(username, password, "C:\\Users\\anaso\\Desktop\\galaxy.jpg");
                break;
        }
        
        if(mensagem != null){
            Document resposta = enviaMensagem(mensagem);
            processaResposta(resposta, req, resp);
        }
    }

    private Document enviaMensagem(Document mensagem) throws IOException {

        Document resposta = null;

        try{
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("Ligação estabelecida com o servidor no socket " + socket);
            outputStream.writeObject(mensagem);

            boolean esperaResposta = true;
            while(esperaResposta && socket.isConnected()){
                Object resp = inputStream.readObject();

                if(resp instanceof Document){
                    resposta = (Document) resp;
                    esperaResposta = false;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro na ligação com o servidor");
        }finally {
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null) socket.close();
            } catch (IOException e) {

            }
        }
        return resposta;
    }

    private void processaResposta(Document document, HttpServletRequest request, HttpServletResponse response) {
        String tipo = XMLUtil.obtemTipodaResposta(document);

        HttpSession session = request.getSession();

        switch (tipo){
            case "login":
                if(XMLUtil.loginValido(document)){
                    try {
                        session.setAttribute("userIsLogged", "true");
                        response.sendRedirect(request.getContextPath()  + "/jsp/index.jsp");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        response.sendRedirect(request.getContextPath()  + "/jsp/login.jsp");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            /*case "listaUtilizadores":
                apresentaUtilizadores(document, response, request);
                break;

            case "convite":
                convite(document);
                break;

            case "setup_game":
                onGame = true;
                this.tabuleiro = geraTabuleiro();
                this.tabuleiroTiros = geraTabuleiro();
                setup_game(document);
                break;

            case "joga":
                atira(document);
                break;

            case "atualiza":
                atualiza(document);
                break;

            case "termina":
                terminaJogo(document);
                break;*/

            default:
                System.out.println("Resposta inválida");
        }

    }

}
