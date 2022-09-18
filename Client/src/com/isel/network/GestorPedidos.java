package com.isel.network;

import com.isel.battleship.Coordenada;
import com.isel.battleship.Navio;
import com.isel.battleship.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;

public class GestorPedidos extends Thread{
    private Cliente cliente;
    private Scanner scanner;
    private boolean onGame = false;
    private char[][] tabuleiro;
    private char[][] tabuleiroTiros;

    GestorPedidos(Cliente cliente){
        this.cliente = cliente;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        menu();
    }

    public void menu(){
        int option = -1;


        // TODO VALIDAR SE É PRECISO ESPERA
        cliente.esperaMensagem();

        do {

            System.out.println("1. listarUtilizadores.");
            System.out.println("2. Convidar.");
            System.out.println("3. Validar se tenho convites.");

            System.out.println("99. Sair.");

            String input = scanner.nextLine();

            try {

                option = Integer.parseInt(input);

                Document request = null;

                switch (option) {
                    case 1:
                        request = listaUtilizadores();
                        cliente.enviaMensagem(request);
                        esperaAtiva();

                        break;
                    case 2:
                        request = listaUtilizadores();
                        cliente.enviaMensagem(request);
                        esperaAtiva();

                        System.out.println("Indique o nome do utilizador para quem quer enviar o convite:");

                        //Scanner scanner1 = new Scanner(System.in);

                        String nome_anfitriao = this.cliente.utilizador.getNomeUtilizador();
                        String nome_convidado = scanner.nextLine();

                        //System.out.println("Anfitriao: " + nome_anfitriao);
                        //System.out.println("Convidado: " + nome_convidado);

                        Document pedido = XMLUtil.convida(nome_anfitriao, nome_convidado);
                        //System.out.println("Vou enviar o pedido: \n " + XMLUtil.documentToString(pedido));
                        cliente.enviaMensagem(pedido);
                        break;

                    case 3:
                        break;

                    case 99:
                        cliente.fechaGestorPedidos();
                        break;
                    default:
                        System.out.println("Invalid Option.");
                }

                esperaAtiva();
            }catch (NumberFormatException e){
                System.out.println("Confirme a sua resposta.");
                //System.out.println("NumberFormatException from menu() in GestorPedidos2");
            }

        } while (!onGame);
    }

    private void esperaAtiva(){
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e) {
            System.out.println("Sleep error");
        }
    }

    private Document listaUtilizadores() {
        Document request = XMLUtil.listaUtilizadores();
        //System.out.println("Vou enviar o pedido: \n " + XMLUtil.documentToString(request));
        return request;
//        Document resposta = cliente.esperaMensagem();
//        System.out.println("Recebi a resposta: \n " + XMLUtil.documentToString(resposta));
//        trataResposta(resposta);
    }

    public void trataResposta(Document document) {
        String tipo = obtemTipodaResposta(document);

        switch (tipo){
            case "listaUtilizadores":
                apresentaUtilizadores(document);
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
                break;

            default:
                System.out.println("Resposta inválida");
        }

    }

    private void terminaJogo(Document document) {
        String estado = XMLUtil.obtemEstado(document);

        System.out.println("O Jogo terminou " + estado +
                (estado.equals("Empatado") ? "." : " pelo jogador " + XMLUtil.obtemVencedor(document) + "."));


        Document docTerminarJogo = XMLUtil.terminarJogo(document);
        this.cliente.enviaMensagem(docTerminarJogo);
        onGame = false;
        menu();
    }

    private void atualiza(Document document) {
        Coordenada tiro = XMLUtil.getCoordenadasTiro(document);
        boolean resultado = XMLUtil.getResultadoTiro(document);
        String atacante = XMLUtil.getNomeAtacante(document);


        if (atacante.equals(this.cliente.utilizador.getNomeUtilizador())) {
            // atualiza no registo do tabuleiro do adversário
            if(resultado){
                this.tabuleiroTiros[tiro.getY()][tiro.getX()] = '*';
            }else{
                this.tabuleiroTiros[tiro.getY()][tiro.getX()] = 'a';
            }

        } else {
            // atualiza no próprio tabuleiro
            if(resultado){
                this.tabuleiro[tiro.getY()][tiro.getX()] = '*';
            }else {
                this.tabuleiro[tiro.getY()][tiro.getX()] = 'a';
            }
        }
        System.out.println("O teu tabuleiro:");
        imprimeTabuleiro(this.tabuleiro);

        System.out.println("\n\n O tabuleiro de tiros auxiliar:");
        imprimeTabuleiro(this.tabuleiroTiros);

    }


    private void apresentaUtilizadores(Document document) {
        Element root = document.getDocumentElement();
        NodeList utilizadores = root.getElementsByTagName("utilizador");

        System.out.println("Utilizadores ativos:");
        for( int index_user = 0; index_user < utilizadores.getLength(); index_user++){
            String nome = utilizadores.item(index_user).getFirstChild().getTextContent();
            if(!nome.equals(cliente.utilizador.getNomeUtilizador())) {
                System.out.println("* " + nome);
            }
        }
    }

    private String obtemTipodaResposta(Document document) {
        Element root = document.getDocumentElement();
        Element tag_pedido = (Element) root.getElementsByTagName("pedido").item(0);
        String tipo = tag_pedido.getAttribute("tipo");

        return tipo;
    }

    public void convite(Document document) {
        Document resposta = null;
        NodeList tag_estado = document.getElementsByTagName("estado");
        if (tag_estado.getLength() > 0) {
            String estado = ((Element)tag_estado.item(0)).getTextContent();
            System.out.println("* Estado convite: " + estado);

            if(estado.equals("Erro")){
                String nome_anfitriao = XMLUtil.getNomeAnfitriao(document);
                String nome_convidado = XMLUtil.getNomeConvidado(document);

                if(nome_convidado.equals(cliente.utilizador.getNomeUtilizador())){
                    System.out.println("Não foi possível enviar mensagem ao anfitriao " + nome_anfitriao);
                }else if(nome_anfitriao.equals(cliente.utilizador.getNomeUtilizador())){
                    System.out.println("Não foi possível enviar mensagem ao convidado " + nome_convidado);
                }
            }
        } else {
            System.out.println("Foi convidado para uma partida de battleship. Deseja aceitar o convite (S/N) ?");
            onGame = true;
            //Scanner scanner = new Scanner(System.in);

            Scanner input = new Scanner(System.in);
            String decisao = input.nextLine();
            System.out.println("A sua decisão foi: " + decisao);
            if (decisao.equals("S")) {
                resposta = XMLUtil.respondeConvite(document, "Aceite");
            } else {
                resposta = XMLUtil.respondeConvite(document, "Recusado");
            }
        }

        if(resposta != null) {
            //System.out.println("<-- Vou responder: " + XMLUtil.documentToString(resposta));
            cliente.enviaMensagem(resposta);
        }
    }

    private void setup_game(Document document) {
        ArrayList<Navio> navios_a_jogo = new ArrayList<>();

        ArrayList<Map> navios = XMLUtil.obtemInfoNavios(document);
        Scanner scanner1 = new Scanner(System.in);

        for(Map navio: navios){

            String tipo = (String) navio.get("tipo");
            int tamanho = Integer.parseInt((String) navio.get("tamanho"));
            int quantidade = Integer.parseInt((String) navio.get("quantidade"));

            int navio_num = 0;
            while (navio_num != quantidade){

                System.out.println("Insira um navio " + tipo + " (Tamanho " + tamanho + " espaços). ");

                System.out.println("Indique a posição inicial do navio.");
                System.out.print("Linha: ");
                int linha_ini = scanner1.nextInt() - 1;
                System.out.print("Coluna: ");
                int coluna_ini = scanner1.nextInt() - 1;

                System.out.println("Indique a posição final do navio.");
                System.out.print("Linha: ");
                int linha_fin = scanner1.nextInt() - 1;
                System.out.print("Coluna: ");
                int coluna_fin = scanner1.nextInt() - 1;

                int distX = Math.abs(coluna_fin - coluna_ini)+1;
                int distY = Math.abs(linha_fin - linha_ini)+1;

                if(linha_ini >= 0 && linha_ini <= 9 &&
                linha_fin >= 0 && linha_fin <=9 &&
                coluna_ini >= 0 && coluna_ini <= 9 &&
                coluna_fin >= 0 && coluna_fin <= 9 ) {
                    if (linha_ini <= linha_fin && coluna_ini <= coluna_fin) {
                        if ((distX == tamanho || distY == tamanho) && (linha_fin - linha_ini == 0 || coluna_fin - coluna_ini == 0)) {

                            Coordenada ini_loc = new Coordenada(coluna_ini, linha_ini);
                            Coordenada fin_loc = new Coordenada(coluna_fin, linha_fin);

                            char sigla = tipo.toUpperCase().charAt(0);
                            Navio novo_navio = new Navio(sigla, ini_loc, fin_loc);

                            if (updateTabuleiro(novo_navio)) {
                                navios_a_jogo.add(novo_navio);
                                navio_num++;
                            }
                        }
                    }
                }
                System.out.println("############# TABULEIRO #############");
                imprimeTabuleiro(this.tabuleiro);
                System.out.println("########### TABULEIRO END ###########");
            }
        }

        Document resposta = XMLUtil.setup_game(document, navios_a_jogo);
        //System.out.println("<-- Vou responder: " + XMLUtil.documentToString(resposta));
        cliente.enviaMensagem(resposta);
    }

    private void atira(Document document) {
        System.out.println("Escolha uma posição onde atirar.");

        Coordenada coord = new Coordenada(20, 20);
        boolean posicaoValida = false;
        Scanner scanner = new Scanner(System.in);

        while(!posicaoValida) {
            System.out.println("Linha: ");
            int linha = scanner.nextInt();
            System.out.println("Coluna: ");
            int coluna = scanner.nextInt();

            coord = new Coordenada(coluna, linha);

            posicaoValida = posicaoValida(coord);
        }

        Document resposta = XMLUtil.tiro(document, coord);
        //System.out.println("<-- Vou responder: " + XMLUtil.documentToString(resposta));
        cliente.enviaMensagem(resposta);


    }

    private boolean posicaoValida(Coordenada coord) {
        int x = coord.getX();
        int y = coord.getY();

        int lim_esq_tab = 0;
        int lim_cim_tab = 0;
        int lim_dir_tab = this.tabuleiroTiros[0].length;
        int lim_bai_tab = this.tabuleiroTiros.length;

        x = x-1;
        y = y-1;

        if(y >= lim_esq_tab && y < lim_dir_tab && x >= lim_cim_tab && x < lim_bai_tab){
            return true;
        }else {
            return false;
        }
    }

    private boolean updateTabuleiro(Navio novo_navio) {

        boolean podeInserir = true;

        Coordenada pos_ini = novo_navio.getLocalizacao_inicial();
        Coordenada pos_fin = novo_navio.getLocalizacao_final();
        int posX_ini = pos_ini.getX();
        int posX_fin = pos_fin.getX();
        int posY_ini = pos_ini.getY();
        int posY_fin = pos_fin.getY();

        char pos, pos_cima, pos_baixo, pos_esq, pos_dir;

        for(int i = posY_ini; i <= posY_fin; i++){
            for(int j = posX_ini; j <= posX_fin; j++) {
                pos = tabuleiro[i][j];
                pos_cima = tabuleiro[i][j];
                pos_baixo = tabuleiro[i][j];
                pos_esq = tabuleiro[i][j];
                pos_dir = tabuleiro[i][j];

                if ((i == 0 || i == 9 || j == 0 || j == 9)) {
                    if (j > 0 && j < 9) {
                        pos_esq = tabuleiro[i][j - 1];
                        pos_dir = tabuleiro[i][j + 1];

                        if (i == 0) {
                            pos_baixo = tabuleiro[i + 1][j];
                        } else if (i == 9) {
                            pos_cima = tabuleiro[i - 1][j];
                        }
                    }
                    if (i > 0 && i < 9) {
                        pos_cima = tabuleiro[i - 1][j];
                        pos_baixo = tabuleiro[i + 1][j];

                        if (j == 0) {
                            pos_dir = tabuleiro[i][j + 1];
                        } else if (j == 9) {
                            pos_esq = tabuleiro[i][j - 1];
                        }
                    }
                } else {
                    pos_cima = tabuleiro[i - 1][j];
                    pos_baixo = tabuleiro[i + 1][j];
                    pos_esq = tabuleiro[i][j - 1];
                    pos_dir = tabuleiro[i][j + 1];

                }


                if (pos != '~' ||
                        pos_cima != '~' ||
                        pos_baixo != '~' ||
                        pos_esq != '~' ||
                        pos_dir != '~') {
                    podeInserir = false;
                    break;
                }
            }
        }

        if(podeInserir){
            for(int i = posY_ini; i <= posY_fin; i++){
                for(int j = posX_ini; j <= posX_fin; j++){
                    this.tabuleiro[i][j] = 'N';
                }
            }
        }

        return podeInserir;
    }

    private boolean updateTabuleiro(Coordenada coord){
        int x = coord.getX();
        int y = coord.getY();

        int lim_esq_tab = 0;
        int lim_cim_tab = 0;
        int lim_dir_tab = this.tabuleiroTiros[0].length;
        int lim_bai_tab = this.tabuleiroTiros.length;

        x = x-1;
        y = y-1;

        if(y > lim_esq_tab && y < lim_dir_tab && x > lim_cim_tab && x < lim_bai_tab){
            this.tabuleiroTiros[x][y] = 'X';
            return true;
        }else {
            return false;
        }
    }
    private char[][] geraTabuleiro() {
        char[][] tabuleiro = new char[10][10];
        String[] label_colunas = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9", "10"};

        /*System.out.print(" ");
        for(String col: label_colunas){
            System.out.print("  " + col);
        }
        System.out.println();*/

        int linha = 1;
        for(char[] row : tabuleiro){
            Arrays.fill(row, '~');
            /*System.out.print(linha + "  ");
            for(char pos: row){
                System.out.print(pos + "  ");
            }
            System.out.println();*/
            linha++;
        }

        return tabuleiro;
    }

    private void imprimeTabuleiro(char[][] tabuleiro){
        String[] label_colunas = new String[]{"1", "2", "3",
                "4", "5", "6",
                "7", "8", "9", "10"};

        System.out.print(" ");
        for(String col: label_colunas){
            System.out.print("  " + col);
        }
        System.out.println();

        int linha = 1;
        for(char[] row : tabuleiro){
            System.out.print(linha + "  ");
            for(char pos: row){
                System.out.print(pos + "  ");
            }
            System.out.println();
            linha++;
        }
    }

}
