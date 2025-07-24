import { UserManager } from '../controllers/UserManager';
import { UserView } from '../views/UserView';

// Simulação do uso do app sem HTTP
const userManager = new UserManager();
const userView = new UserView(userManager);

// Testes simples com console.log
console.log(userView.enviarInfoUser('guilherme', 'senha123'));
console.log(userView.enviarInfoUser('ana', '1234'));
console.log(userView.mostrarListaUser());
