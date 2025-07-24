# MetodologiaSoftware

Repositório para disciplina de Metodologia de Software

# 📘 Diagrama de Classes - Sistema de Gerenciamento de Usuário

Este sistema foi modelado com base no padrão **MVC (Model-View-Controller)**, onde:

- **Model** → representa os dados (`Usuario`)
- **Controller** → gerencia a lógica de negócio (`UsuarioGerenciador`)
- **View** → representa a interface gráfica do sistema (`UsuarioInterfaceGrafica`)

##  Estrutura de Classes

###  Classe: `Usuario`
Representa o único tipo de usuário existente no sistema. Contém informações básicas de login.

#### Atributos:
- `usuario: String` → Nome de usuário.
- `senha: String` → Senha do usuário.

#### Métodos:
- `getUsuario(): String` → Retorna o nome de usuário.
- `setUsuario(String)` → Define um novo nome de usuário.
- `getSenha(): String` → Retorna a senha do usuário.
- `setSenha(String)` → Define uma nova senha.

---

###  Classe: `UsuarioGerenciador`
É o responsável por controlar a lógica do sistema. Armazena a lista de usuários (mesmo que apenas um, no momento) e realiza ações como cadastrar ou listar usuários.

#### Atributos:
- `usuarioLista: List<Usuario>` → Lista de usuários do sistema.

#### Métodos:
- `cadastraUsuario(Usuario): void` → Cadastra um novo usuário na lista.
- `listarUsuarios(): Usuario` → Retorna o(s) usuário(s) cadastrado(s).

---

###  Classe: `UsuarioInterfaceGrafica`
É a interface com o usuário (a "View" do sistema). A interface gráfica utiliza os métodos do gerenciador (`UsuarioGerenciador`) para realizar as ações e exibir informações.

#### Atributos:
- `gerenciador: UsuarioGerenciador` → Instância do gerenciador usada para interagir com os dados.

#### Métodos:
- `mostrarListaUsuario(Usuario): void` → Exibe as informações de um usuário.
- `enviarInfoUsuario(): Usuario` → Coleta dados do usuário (por exemplo, via formulário) e os retorna.

---

##  Resumo da Arquitetura

```plaintext
UsuarioInterfaceGrafica  -->  UsuarioGerenciador  -->  Usuario
         View                     Controller             Model

