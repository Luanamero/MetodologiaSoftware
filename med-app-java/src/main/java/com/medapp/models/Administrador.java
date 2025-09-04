package com.medapp.models;

public class Administrador extends User {
    private static final long serialVersionUID = 1L;
    private String nivelPermissao;

    public Administrador() {
        super();
    }

    public Administrador(String username, String password, String email, String nivelPermissao) {
        super(username, password, email);
        this.nivelPermissao = nivelPermissao;
    }

    public String getNivelPermissao() {
        return nivelPermissao;
    }

    public void setNivelPermissao(String nivelPermissao) {
        this.nivelPermissao = nivelPermissao;
    }

    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }
}
