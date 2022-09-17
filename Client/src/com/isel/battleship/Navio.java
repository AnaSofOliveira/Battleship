package com.isel.battleship;

public class Navio {

    private String nome;
    private char tipo;
    private Coordenada localizacao_inicial;
    private Coordenada localizacao_final;
    private int tamanho;

    public Navio(char sigla, Coordenada ini_loc, Coordenada fin_loc){
        /**
         * P - Porta-Aviões (1 x 5 quadriculas)
         * T - Tanques (2 x 4 quadriculas)
         * C - Contratorpedeiros (3 x 3 quadriculas)
         * S - Submarino (4 x 2 quadriculas)
         * **/

        this.tipo = sigla;
        this.localizacao_inicial = ini_loc;
        this.localizacao_final = fin_loc;

        switch (this.tipo){
            case 'P':
                this.tamanho = 5;
                this.nome = "PortaAvioes";
                break;

            case 'T':
                this.tamanho = 4;
                this.nome = "Tanque";
                break;

            case 'C':
                this.tamanho = 3;
                this.nome = "Contratorpedeiro";
                break;

            case 'S':
                this.tamanho = 2;
                this.nome = "Submarino";
                break;
        }
    }

    public Coordenada getLocalizacao_inicial() {
        return localizacao_inicial;
    }

    public Coordenada getLocalizacao_final() {
        return localizacao_final;
    }

    public char getTipo() {
        return tipo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getNome(){
        return this.nome;
    }

    @Override
    public String toString() {
        return "Navio{" +
                "tipo=" + tipo +
                ", localizacao_inicial=" + localizacao_inicial +
                ", localizacao_final=" + localizacao_final +
                ", tamanho=" + tamanho +
                '}';
    }
}
