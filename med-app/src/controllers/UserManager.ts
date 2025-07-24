import { User } from '../models/User';

export class UserManager {
  private userList: User[] = [];

  cadastraUsuario(user: User): string {
    this.userList.push(user);
    return `Usuário '${user.getUsuario()}' cadastrado com sucesso.`;
  }

  cadastraUsuarioPorCredenciais(usuario: string, senha: string): string {
    const novoUser = new User(usuario, senha);
    return this.cadastraUsuario(novoUser);
  }

  listarUsuarios(): string {
    if (this.userList.length === 0) {
      return 'Nenhum usuário cadastrado.';
    }

    const nomes = this.userList.map((u) => u.getUsuario()).join('\n');
    return 'Usuários cadastrados:\n' + nomes;
  }
}
