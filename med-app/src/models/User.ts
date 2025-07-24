export class User {
  private usuario: string;
  private senha: string;

  constructor(usuario: string, senha: string) {
    this.usuario = usuario;
    this.senha = senha;
  }

  getUsuario(): string {
    return this.usuario;
  }

  setUsuario(usuario: string): void {
    this.usuario = usuario;
  }

  getSenha(): string {
    return this.senha;
  }

  setSenha(senha: string): void {
    this.senha = senha;
  }
}
