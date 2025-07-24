import { UserManager } from '../controllers/UserManager';

export class UserView {
  constructor(private gerenciador: UserManager) {}

  enviarInfoUser(usuario: string, senha: string): string {
    return this.gerenciador.cadastraUsuarioPorCredenciais(usuario, senha);
  }

  mostrarListaUser(): string {
    return this.gerenciador.listarUsuarios();
  }
}
