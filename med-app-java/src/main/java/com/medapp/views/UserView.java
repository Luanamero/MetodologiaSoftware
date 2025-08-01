package com.medapp.views;

import com.medapp.controllers.UserManager;

public class UserView {
    private UserManager gerenciador;

    public UserView(UserManager gerenciador) {
        this.gerenciador = gerenciador;
    }

    public String enviarInfoUser(String usuario, String senha) {
        return gerenciador.cadastraUsuarioPorCredenciais(usuario, senha);
    }

    public String mostrarListaUser() {
        return gerenciador.listarUsuarios();
    }
}
