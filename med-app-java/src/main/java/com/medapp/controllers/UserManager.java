package com.medapp.controllers;

import com.medapp.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> userList = new ArrayList<>();

    public String cadastraUsuario(User user) {
        userList.add(user);
        return String.format("Usuário '%s' cadastrado com sucesso.", user.getUsuario());
    }

    public String cadastraUsuarioPorCredenciais(String usuario, String senha) {
        User novoUser = new User(usuario, senha);
        return cadastraUsuario(novoUser);
    }

    public String listarUsuarios() {
        if (userList.isEmpty()) {
            return "Nenhum usuário cadastrado.";
        }

        StringBuilder nomes = new StringBuilder();
        for (int i = 0; i < userList.size(); i++) {
            if (i > 0) {
                nomes.append("\n");
            }
            nomes.append(userList.get(i).getUsuario());
        }
        
        return "Usuários cadastrados:\n" + nomes.toString();
    }
}
