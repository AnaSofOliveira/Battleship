package com.isel.network;

import com.isel.battleship.Utilizador;
import com.isel.battleship.XMLUtil;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 5025;

    static Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    Thread t;

    private GestorPedidos gestorPedidos = null;
    //private GestorConvite gestorConvites = null;

    boolean logado = false;

    Utilizador utilizador;

    public Cliente(){
        try{
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("Ligação: " + socket);
            menu();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu(){
        int option;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Login");
            System.out.println("2. Exit");

            option = scanner.nextInt();

            switch (option){
                case 1:
                    Scanner input = new Scanner(System.in);
                    System.out.println("Introduza o nome de utilizador: ");
                    String user = input.nextLine();
                    System.out.println("Introduza a password: ");
                    String pass = input.nextLine();
                    System.out.println("Introduza o caminho da imagem(foto): ");
                    String foto = input.nextLine();

                    Document pedido = XMLUtil.login(user, pass, foto);
                    System.out.println("<-- Vou enviar o pedido de Login: " + XMLUtil.documentToString(pedido));
                    enviaMensagem(pedido);

                    Document resposta = esperaMensagemLogin();

                    System.out.println("Resposta: " + resposta);

                    if(XMLUtil.loginValido(resposta)){
                        System.out.println("Login Válido");
                        logado = true;
                        this.utilizador = new Utilizador(user, pass, foto);
                        gestorPedidos = new GestorPedidos(this);
                        gestorPedidos.start();
                    }
                    //System.out.println("Login Inválido. Tente novamente. ");
                    break;
                case 0:
                    fechaLigacao();
                    break;
                default:
                    System.out.println("Opção errada. Escolha novamente!");
            }
        }while (!logado);
    }


    synchronized public void enviaMensagem(Document mensagem) {
        try{
            outputStream.writeObject(mensagem);
        } catch (Exception e) {
            System.err.println("Não foi possível enviar a mensagem" + mensagem + ".");
        }
    }

    synchronized public void esperaMensagem() {

            t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(socket.isConnected()){
                        try {
                            Object reply = null;

                            reply = inputStream.readObject();

                            if (reply instanceof Document) {
                                System.out.println("--> Recebi a mensagem: " + XMLUtil.documentToString((Document) reply));
                                gestorPedidos.trataResposta((Document) reply);
                            }
                        }catch (IOException e) {
                            System.out.println("IOException no método esperaMensagem() do Cliente2");
                            e.printStackTrace();
                            fechaLigacao();
                        } catch (ClassNotFoundException e) {
                            System.out.println("ClassNotFoundException no método esperaMensagem() do Cliente2");
                            fechaLigacao();
                            break;
                        }
                    }
                }
            });
            t.start();
    }

    public Document esperaMensagemLogin() {
        Document mensagem = null;
        try{
            mensagem = (Document) inputStream.readObject();
            System.out.println("--> Recebi a mensagem de Login: " + XMLUtil.documentToString((Document) mensagem));

        } catch (Exception e) {
            System.err.println("Não foi possível receber mensagem.");
        }
        return mensagem;
    }

    private  void fechaLigacao(){
        t.stop();
        try {
            if( outputStream != null) outputStream.close();
            if( inputStream != null) inputStream.close();
            if( socket != null) socket.close();
            //System.exit(0);
        }catch (IOException e) {
            System.err.println("Não foi possível fechar devidamente a licação ao Servidor. \n " + e.getMessage());
        }
    }

    public void fechaGestorPedidos(){
        gestorPedidos.stop(); // TODO PERGUNTAR ENGENHEIRO SE PÁRA A THREAD GESTORPEDIDOS2 E THREAD PRINCIPAL
        logado = false;
        menu();
    }

//    public void fechaGestorConvites(){
//        this.gestorConvites.stop(); // TODO PERGUNTAR ENGENHEIRO SE PÁRA A THREAD GESTORPEDIDOS2 E THREAD PRINCIPAL
//    }

//    public void trataConvite(Document pedido) {
//        return gestorPedidos.convite(pedido);
//    }

    public static void main(String[] args) {
        new Cliente();
    }
}
