package com.isel.battleship;

public class Utilizador {
    private String nome_utilizador;
    private String password;
    private String foto_path;

    public Utilizador(String nome, String pass, String src) {
        this.nome_utilizador = nome;
        this.password = pass;
        this.foto_path = src;
    }

    public String getNomeUtilizador() {
        return this.nome_utilizador;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFotoPath() {
        return this.foto_path;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "nome_utilizador='" + nome_utilizador + '\'' +
                ", password='" + password + '\'' +
                ", foto_path='" + foto_path + '\'' +
                '}';
    }
}
