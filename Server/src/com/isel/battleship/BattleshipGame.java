package com.isel.battleship;

import java.util.ArrayList;

public class BattleshipGame {

    private Utilizador anfitriao;
    private Utilizador convidado;
    private char[][] tabuleiro_anfitriao = null;
    private char[][] tabuleiro_convidado = null;
    private final int tamanhoTabuleiro = 10;


    public BattleshipGame(Utilizador anfitriao, Utilizador convidado){
        this.anfitriao = anfitriao;
        this.convidado = convidado;
    }

    public void insereNaviosNoTabuleiro(Utilizador player, ArrayList<Navio> navios){
        if(player.getNomeUtilizador() == anfitriao.getNomeUtilizador()){
            this.tabuleiro_anfitriao = new char[tamanhoTabuleiro][tamanhoTabuleiro];
        }else{
            this.tabuleiro_convidado = new char[tamanhoTabuleiro][tamanhoTabuleiro];
        }

        for(Navio navio: navios){

            coordinate pos_ini = navio.getLocalizacao_inicial();
            coordinate pos_fin = navio.getLocalizacao_final();
            int posX_ini = pos_ini.getX();
            int posX_fin = pos_fin.getX();
            int posY_ini = pos_ini.getY();
            int posY_fin = pos_fin.getY();

            if(player.getNomeUtilizador() == anfitriao.getNomeUtilizador()){
                this.tabuleiro_anfitriao = desenhaNavio(this.tabuleiro_anfitriao, posX_ini, posX_fin, posY_ini, posY_fin);
            }else{
                this.tabuleiro_convidado = desenhaNavio(this.tabuleiro_convidado, posX_ini, posX_fin, posY_ini, posY_fin);
            }
        }

    }

    private char[][] desenhaNavio(char[][] tabuleiro, int posX_ini, int posX_fin, int posY_ini, int posY_fin){
        for(int i = posY_ini; i <= posY_fin; i++){
            for(int j = posX_ini; j <= posX_fin; j++){
                tabuleiro[i][j] = 'N';
            }
        }
        return tabuleiro;
    }

    public boolean tabuleirosPreparados() {
        if(this.tabuleiro_anfitriao != null && this.tabuleiro_convidado != null){
            return true;
        }
        return false;
    }

    public Utilizador getAnfitriao() {
        return this.anfitriao;
    }

    public Utilizador getConvidado() {
        return this.convidado;
    }

    public boolean estaNoJogo(Utilizador user){
        if(user.equals(this.anfitriao) || user.equals(this.convidado)){
            return true;
        }
        return false;
    }

    public boolean validaTiro(Utilizador userDestino, coordinate posicao){
        if(userDestino == anfitriao){
            if (tabuleiro_anfitriao[posicao.getY()][posicao.getX()] == 'N'){
                tabuleiro_anfitriao[posicao.getY()][posicao.getX()] = '*';
                return true;
            }return false;
        }else if(userDestino == convidado){
            if (tabuleiro_convidado[posicao.getY()][posicao.getX()] == 'N'){
                tabuleiro_convidado[posicao.getY()][posicao.getX()] = '*';
                return true;
            }return false;
        }
        return  false;
    }

    public boolean jogoTerminado(){

        int numNaviosAnfitriao = 0;
        int numNaviosConvidado = 0;

        for(int y = 0; y < this.tamanhoTabuleiro; y++){
            for(int x = 0; x < this.tamanhoTabuleiro; x++){
                if(this.tabuleiro_anfitriao[y][x] == 'N'){
                    numNaviosAnfitriao++;
                }
                if (this.tabuleiro_convidado[y][x] == 'N'){
                    numNaviosConvidado++;
                }
            }
        }

        if(numNaviosAnfitriao == 0 || numNaviosConvidado == 0){
            return true;
        }return false;
    }

    public Utilizador obtemVencedor() {
        int numNaviosAnfitriao = 0;
        int numNaviosConvidado = 0;

        for(int y = 0; y < this.tamanhoTabuleiro; y++){
            for(int x = 0; x < this.tamanhoTabuleiro; x++){
                if(this.tabuleiro_anfitriao[y][x] == 'N'){
                    numNaviosAnfitriao++;
                }
                if (this.tabuleiro_convidado[y][x] == 'N'){
                    numNaviosConvidado++;
                }
            }
        }

        if(numNaviosAnfitriao < numNaviosConvidado){
            return this.convidado;
        }else if(numNaviosAnfitriao > numNaviosConvidado){
            return this.anfitriao;
        }else{
            return null;
        }
    }
}
