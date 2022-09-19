package com.isel.network;


import com.isel.battleship.BattleshipGame;
import com.isel.battleship.Utilizador;
import com.isel.battleship.XMLUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Servidor {
    private final String UTILIZADORES_XML_FILEPATH = "src\\WebContent\\xml\\utilizadores.xml";
    private final String UTILIZADORES_XSD_FILEPATH = "src\\WebContent\\xsd\\utilizadores.xsd";

        public final static int DEFAULT_PORT = 5025;
    public ServerSocket serverSocket = null;

    public ArrayList<Utilizador> all_users = new ArrayList<>();

    public HashMap<Utilizador, GestorPedidos> online = new HashMap<>();
    public HashMap<Utilizador, GestorPedidos> disponiveis = new HashMap<>();

    public ArrayList<BattleshipGame> games;

    Servidor(int port){
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            all_users = XMLUtil.carregaUtilizadores(UTILIZADORES_XML_FILEPATH, UTILIZADORES_XSD_FILEPATH);
            games = new ArrayList<>();

            Socket newSock = null;

            for (;;) {

                System.out
                        .println("Servidor TCP concorrente aguarda ligacao no porto "
                                + DEFAULT_PORT + "...");

                printClientes();

                // Espera connect do cliente
                newSock = serverSocket.accept();

                GestorPedidos th = new GestorPedidos(newSock, this);
                th.start();
            }
        } catch (IOException e) {
            System.err.println("Excepção no servidor: " + e);

        }

    }


    private void printClientes() {

        System.out.println(" ############ SERVER STATE BEGIN ############ ");

        System.out.println("Users:");
        for( Utilizador utilizador: all_users){
            System.out.println("Utilizador: " + utilizador.getNomeUtilizador());
        }

        System.out.println(" ############ SERVER STATE END ############ ");
    }

    public static void main(String[] args) {
        new Servidor(DEFAULT_PORT);
    }
}
