package com.isel.network;

import com.isel.battleship.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class GestorPedidos extends Thread{

    private static final String XSD_FILE_LOCATION = "../src/xsd/protocolo.xsd";
    Socket socket;
    Servidor server;
    Boolean connectionEstablished = false;
    ObjectInputStream inputStream = null;
    ObjectOutputStream outputStream = null;
    Object pedido = "";

    Utilizador user;
    BattleshipGame game;


    GestorPedidos(Socket socket, Servidor server){
        super("ServerHandlerThread");
        this.socket = socket;
        this.server = server;
        this.connectionEstablished = true;
    }

    @Override
    public void run() {

        System.out.println("Thread " + this.getId() + ", " + socket.getRemoteSocketAddress());

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            while (connectionEstablished){
                pedido = esperaMensagem();
                System.out.println("--> Recebi a mensagem: " + XMLUtil.documentToString((Document) pedido));

                Document resposta = trataPedidos((Document) pedido);
                System.out.println("<-- Vou responder: " + XMLUtil.documentToString((Document) resposta));

                enviaMensagem(resposta);

                System.out.println("REGISTO DO SERVIDOR:");
                System.out.println("All Users:");
                for( Utilizador utilizador: server.all_users){
                    System.out.println("Utilizador: " + utilizador.getNomeUtilizador());
                }
                System.out.println("Hash User - Conections:");
                server.online.forEach((user, os) -> {
                    System.out.println("* User: " + user.getNomeUtilizador());
                    //System.out.println("* Connection ID: " + socket.getInetAddress() + " | " + socket.getPort());
                });
            }

        } catch (IOException | NullPointerException e) {

            if(this.user != null && this.game != null) {
                Utilizador adversario = (this.user == this.game.getAnfitriao() ? this.game.getConvidado() : this.game.getAnfitriao());
                Document docTerminaJogo = XMLUtil.terminaJogo(adversario);

                System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(docTerminaJogo) + " ao adversário " + adversario.getNomeUtilizador());
                try {
                    ObjectOutputStream outputStream = server.online.get(adversario).outputStream;
                    outputStream.writeObject(docTerminaJogo);
                    outputStream.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                server.online.remove(this.user);
                server.disponiveis.remove(this.user);
                server.games.remove(this.game);
            }
        }

    }

    synchronized private Document esperaMensagem() {
        Document mensagem = null;
        try {
            mensagem = (Document) inputStream.readObject();

        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            System.err.println("Não foi possível receber o pedido do cliente. ");
            server.online.remove(this.user);
            server.disponiveis.remove(this.user);
        }

        return mensagem;
    }

    synchronized private void enviaMensagem(Document mensagem) {
        try{
            outputStream.writeObject(mensagem);
        } catch (Exception e) {
            System.err.println("Não foi possível enviar a mensagem" + mensagem + ".");
            server.online.remove(this.user);
            server.disponiveis.remove(this.user);
        }
    }

    private Document trataPedidos(Document document) throws NullPointerException{

       // if(XMLUtil.validar(document, XSD_FILE_LOCATION)) {
            String tipo = XMLUtil.obtemTipoDoPedido(document);
            Document resposta = null;

            switch (tipo) {
                case "login":
                    resposta = login(document);
                    System.out.println("Foi pedido " + tipo + ".");
                    break;

                case "listaUtilizadores":
                    Set<Utilizador> utilizadores_disponiveis = server.disponiveis.keySet();

                    resposta = XMLUtil.listaUtilizadores(document, utilizadores_disponiveis);
                    System.out.println("Vou enviar a resposta: \n " + XMLUtil.documentToString(resposta));
                    System.out.println("Foi pedido " + tipo + ".");
                    break;

                case "convite":
                    resposta = convite(document);

                    System.out.println("Foi pedido " + tipo + ".");
                    break;

                case "setup_game":
                    resposta = preparaTabuleiro(document);
                    break;

                case "joga":
                    resposta = validaEstadoJogo(document);
                    // validaJogada(document);
                    break;

                case "terminar":
                    terminaJogo(document);

                default:
                    System.out.println("Pedido inválido");
            }
        /*}else {
            System.out.println("O pedido não é válido.");
        }*/

        return resposta;

    }

    private void terminaJogo(Document document) {

        this.server.games.remove(this.game);
        this.server.disponiveis.put(this.user, this);
        System.out.println("Jogo " + this.game + " terminado.");

        /*for (BattleshipGame game : this.server.games){
            if(game.estaNoJogo(this.user)){
                this.server.disponiveis.put(this.user, this);
                try {
                    this.server.games.remove(this.game);
                } catch (Exception e) {
                    System.out.println("O jogo já foi terminado.");
                }
            }
        }*/
    }

    private Document validaEstadoJogo(Document document) {
        boolean jogoTerminado = false;
        boolean resultadoTiro = false;

        Utilizador adversario = (this.user == this.game.getAnfitriao() ? this.game.getConvidado() : this.game.getAnfitriao());

        coordinate tiro = XMLUtil.obtemCoordenasTiro(document);
        resultadoTiro = this.game.validaTiro(adversario, tiro);
        jogoTerminado = this.game.jogoTerminado();

        Utilizador atacante = this.user;
        Document docAtualizaTab = atualizaTiroTabuleiros(document, resultadoTiro, atacante, adversario);

        if(jogoTerminado){

            Utilizador vencedor = this.game.obtemVencedor();

            Document docTerminaJogo = XMLUtil.terminaJogo(vencedor);
            try {
                System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(docTerminaJogo) + " ao adversário " + adversario.getNomeUtilizador());
                ObjectOutputStream outputStream = server.online.get(adversario).outputStream;
                outputStream.writeObject(docTerminaJogo);
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return docTerminaJogo;
        }else{
            //manda nova jogada para o outro jogador
            try {
                Document nova_jogada = jogar();

                System.out.println("Vou enviar a mensagem " + nova_jogada + " ao adversário " + adversario.getNomeUtilizador());
                ObjectOutputStream outputStream = server.online.get(adversario).outputStream;
                outputStream.writeObject(nova_jogada);
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return docAtualizaTab;
        }
    }

    private Document atualizaTiroTabuleiros(Document document, boolean resultadoTiro, Utilizador atacante, Utilizador adversario) {
        Document docResultado = XMLUtil.atualizaResultadoTiro(document, resultadoTiro, atacante);

        try {
            System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(docResultado) + " ao adversário " + adversario.getNomeUtilizador());
            ObjectOutputStream outputStream = server.online.get(adversario).outputStream;
            outputStream.writeObject(docResultado);

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return docResultado;
    }

    private Document validaJogada(Document document) {
        boolean resultado = false;
        coordinate tiro = XMLUtil.obtemCoordenasTiro(document);

        Utilizador adversario = (this.user == this.game.getAnfitriao() ? this.game.getConvidado() : this.game.getAnfitriao());

        // Valida o tiro no tabuleiro do adversário
        resultado = this.game.validaTiro(adversario, tiro);

        Utilizador atacante = this.user;

        Document docResultado = XMLUtil.atualizaResultadoTiro(document, resultado, atacante);

        try {
            System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(docResultado) + " ao adversário " + adversario.getNomeUtilizador());
            System.out.println("Vou enviar a mensagem " + jogar() + " ao adversário " + adversario.getNomeUtilizador());

            Document nova_jogada = jogar();

            ObjectOutputStream outputStream = server.online.get(adversario).outputStream;
            outputStream.writeObject(docResultado);
            outputStream.writeObject(nova_jogada);

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return docResultado;
    }

    private Document jogar() {
        return XMLUtil.jogar();
    }

    private Document preparaTabuleiro(Document document) {
        String nome_utilizador = XMLUtil.obtemUtilizadorDaResposta(document);
        ArrayList<Navio> navios = XMLUtil.obtemNavios(document);

        Utilizador user = obtemUtilizador(nome_utilizador);

        for(BattleshipGame game: server.games){
            if(game.estaNoJogo(user)){
                this.game = game;
                System.out.println("Vou configurar o tabuleiro do " + user.getNomeUtilizador());
                this.game.insereNaviosNoTabuleiro(user, navios);

                if(this.game.tabuleirosPreparados()){
                    return jogar();
                }
            }


        }
        return null;
    }

    private Document convite(Document document){
        NodeList tag_estado = document.getElementsByTagName("estado");
        Set<Utilizador> utilizadores = server.online.keySet();

        String nome_anfitriao = XMLUtil.getNomeAnfitriao(document);
        String nome_convidado = XMLUtil.getNomeConvidado(document);

        if (tag_estado.getLength() > 0) {
            String estado = tag_estado.item(0).getTextContent();

            if(estado.equals("Recusado")){

                for(Utilizador utilizador: utilizadores){
                    if(nome_anfitriao.equals(utilizador.getNomeUtilizador())){
                        try {
                            System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(document) + " ao anfitriao " + utilizador.toString());
                            ObjectOutputStream outputStream = server.online.get(utilizador).outputStream;
                            outputStream.writeObject(document);
                            outputStream.flush();
                        } catch (IOException e) {
                            System.err.println("Não foi possível enviar convite ao convidado.");
                            return XMLUtil.estado_convite(document, "Erro");
                        }
                    }
                }
                return document;
            }else if(estado.equals("Aceite")){

                for(Utilizador utilizador: utilizadores){
                    if(nome_anfitriao.equals(utilizador.getNomeUtilizador())){
                        try {
                            Document setup_game_message = XMLUtil.setup_game(nome_anfitriao);
                            System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(setup_game_message) + " ao anfitriao " + utilizador.toString());
                            ObjectOutputStream outputStream = server.online.get(utilizador).outputStream;
                            outputStream.writeObject(setup_game_message);
                            outputStream.flush();
                        } catch (IOException e) {
                            System.err.println("Não foi possível enviar convite ao convidado.");
                            return XMLUtil.estado_convite(document, "Erro");
                        }
                    }
                }

                iniciaNovoJogo(nome_anfitriao, nome_convidado);

                Document setup_game_message = XMLUtil.setup_game(nome_convidado);
                return setup_game_message;
            }
        } else {

            for (Utilizador utilizador: utilizadores) {
                if (nome_convidado.equals(utilizador.getNomeUtilizador())) {
                    try {
                        System.out.println("Vou enviar a mensagem " + XMLUtil.documentToString(document) + " para o convidado " + utilizador.toString());
                        ObjectOutputStream outputStream = server.online.get(utilizador).outputStream;
                        outputStream.writeObject(document);
                        outputStream.flush();
                        return XMLUtil.estado_convite(document, "Enviado");
                    }catch (IOException e) {
                        System.err.println("Não foi possível enviar convite ao convidado.");
                        return XMLUtil.estado_convite(document, "Erro");
                    }
                }
            }
        }
        return XMLUtil.estado_convite(document, "Erro");
    }

    private void iniciaNovoJogo(String nome_anfitriao, String nome_convidado) {

        Utilizador anfitriao = obtemUtilizador(nome_anfitriao);
        Utilizador convidado = obtemUtilizador(nome_convidado);

        System.out.println("Vou começar a jogar com " + anfitriao + " e com " + convidado);

        this.game = new BattleshipGame(anfitriao, convidado);

        System.out.println("A instancia do jogo é: " + game);

        server.games.add(game);

        server.disponiveis.remove(anfitriao);
        server.disponiveis.remove(convidado);

    }

    private Utilizador obtemUtilizador(String nome_utilizador){

        Set<Utilizador> utilizadores = server.online.keySet();

        for(Utilizador user: utilizadores){
            if(user.getNomeUtilizador().equals(nome_utilizador)){
                return user;
            }
        }

        return null;
    }

    private Document login(Document document) {
        Document resposta = null;
        ArrayList<Utilizador> utilizadores = server.all_users;
        Set<Utilizador> utilizadores_online = server.online.keySet();

        boolean invalid_password = false;
        boolean isLogged = false;
        boolean autorizado = false;

        Element root = document.getDocumentElement();
        String nome = root.getElementsByTagName("nome_utilizador").item(0).getTextContent();
        nome = nome.replaceAll("\\s", "");
        String pass = root.getElementsByTagName("password").item(0).getTextContent();
        pass = pass.replaceAll("\\s", "");
        Element foto = (Element)root.getElementsByTagName("foto").item(0);
        String foto_src = foto.getAttribute("src");


        for(int user_idx = 0; user_idx < utilizadores.size(); ++user_idx) {
            Utilizador user = (Utilizador) utilizadores.get(user_idx);
            if (nome.equals(user.getNomeUtilizador())) {
                if (pass.equals(user.getPassword())) {
                    for (Utilizador utilizador: utilizadores_online){
                        if(utilizador.getNomeUtilizador().equals(user.getNomeUtilizador())){
                            isLogged = true;
                        }
                    }
                    if(!isLogged){
                        autorizado = true;
                    }
                } else {
                    invalid_password = true;
                }
                break;
            }
        }


        if (autorizado) {
            // Autorizado login mas utilizador já estava registado na plataforma
            this.user = new Utilizador(nome, pass, foto_src);
            server.online.put(this.user, this);
            server.disponiveis.put(this.user, this);
            System.out.println("Autorizado - Login");
            return XMLUtil.resposta_login(document, "Autorizado", "");
        } else if (!autorizado && invalid_password) {
            // Login Não Autorizado - password incorreta
            System.out.println("Não Autorizado");
            return XMLUtil.resposta_login(document, "Não Autorizado", "Password incorreta");
        } else if (!autorizado && isLogged) {
            // Login Não Autorizado - utilizador já logado
            System.out.println("Não Autorizado");
            return XMLUtil.resposta_login(document, "Não Autorizado", "Utilizador já logado");
        } else {
            // Novo registo na plataforma
            // TODO ADICIONAR UTILIZADOR À VARIÁVEL ALL_USERS E AO FICHEIRO XML UTILIZADORES. OBRIGAR SERVER A CARREGAR NOVAMENTE XML DOS UTILIZADORES
            this.user = new Utilizador(nome, pass, foto_src);
            server.all_users.add(user);
            server.online.put(this.user, this);
            server.disponiveis.put(this.user, this);
            System.out.println("Autorizado - Novo registo");
            return XMLUtil.resposta_login(document, "Autorizado", "Novo registo");
        }
    }
}
