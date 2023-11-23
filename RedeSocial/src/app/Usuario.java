package app;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import bdconexao.*;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private AdicionarAmigo.Cadastrar novoUsuario = null;
    private ConexaoPostgre conexao = new ConexaoPostgre();
    private AdicionarAmigo.LogarUsuario login = null;
    private AdicionarAmigo adicionar = null;
    private AdicionarAmigo.ExcluirAmigo excluir = null;
    private AdicionarAmigo.consultarAmigos consultar = new AdicionarAmigo.consultarAmigos();
    private AdicionarAmigo.EnviarMensagem enviar = new AdicionarAmigo.EnviarMensagem();
    private AdicionarAmigo.ListarMensagens listar = new AdicionarAmigo.ListarMensagens();

    ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario() {
        this.novoUsuario = new AdicionarAmigo.Cadastrar();
        this.login = new AdicionarAmigo.LogarUsuario();
        this.adicionar = new AdicionarAmigo();
        this.excluir = new AdicionarAmigo.ExcluirAmigo();
        this.consultar = new AdicionarAmigo.consultarAmigos();
    }

    public Usuario(ArrayList<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
            }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public void cadastrar() throws SQLException {
        try {
                       nome = JOptionPane.showInputDialog(null, "Digite seu nome:", "",
                               JOptionPane.PLAIN_MESSAGE);
            while (nome.length() < 1 || nome.isEmpty() || nome.isBlank()) {
                nome = JOptionPane.showInputDialog(null, "Inválido! \nDigite novamente:",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
            email = JOptionPane.showInputDialog(null, "Digite seu e-mail:", "",
                    JOptionPane.PLAIN_MESSAGE);
            while (email.length() < 1 || email.isEmpty() || email.isBlank()) {
                email = JOptionPane.showInputDialog(null, "Inválido! \nDigite novamente:",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
            senha = JOptionPane.showInputDialog(null, "Crie uma senha:", "",
                    JOptionPane.PLAIN_MESSAGE);
            while (senha.length() < 1 || senha.isEmpty() || senha.isBlank()) {
                senha = JOptionPane.showInputDialog(null, "Inválida! \nDigite novamente:",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

            if (conexao.autenticarUsuario(nome) == true) {
                JOptionPane.showMessageDialog(null, "Nome já está em uso! Por favor, " +
                        "escreva um novo nome.", "", JOptionPane.ERROR_MESSAGE);
            } else if (conexao.autenticarUsuario(email) == true) {
                JOptionPane.showMessageDialog(null, "O email já está em uso. Por favor, " +
                        "escreva outro email.", "", JOptionPane.ERROR_MESSAGE);
            } else {
                novoUsuario.inserirUsuario(nome, email, senha);
                Usuario user = new Usuario(nome, email, senha);
                usuarios.add(user);
            }
        } catch (NullPointerException e) {
            System.err.println("Operação cancelada");
        }

    }

    public boolean logarUsuario(String vNome, String vSenha) throws SQLException, Exception {

        boolean buscaBanco = login.verificarUsuario(vNome, vSenha);

        if (buscaBanco) {
            JOptionPane.showMessageDialog(null, "Bem vindo a mini Rede Social!",
                    "", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Acesso negado! Usuário ou senha inválidos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    public void adicionarAmigo(String loginUsuario) throws SQLException {
        try {
            String newA = JOptionPane.showInputDialog(null, "Digite o nome do amigo que deseja adicionar?",
                    "Escreva o nome de Usuário", JOptionPane.PLAIN_MESSAGE);
            if (!newA.isEmpty()) {// se não for vazio
                if (newA != null) {// se ele clicar em ok
                    if (!loginUsuario.equals(newA)) {// se ele tentar adicionar a sí mesmo
                        boolean buscaBanco = conexao.autenticarUsuario(newA);

                        if (buscaBanco) {
                            adicionar.adicionarAmigo(loginUsuario, newA);
                            JOptionPane.showMessageDialog(null, "Amigo adicionado com sucesso!",
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        } else {//se ele não for encontrado
                            JOptionPane.showMessageDialog(null, "Usuário inexistente!",
                                    "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Você não pode se adicioanar",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {// se ele clicou em cancelar
                    System.err.println("Operação cancelada");
                }
            } else {//se for vazio
                System.err.println("Digite algum usuário.");
                JOptionPane.showMessageDialog(null, "Digite um usuário", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }

    }

    public void excluirAmigo(String loginUsuario) throws SQLException {
        try {
            String amigoExcluir = JOptionPane.showInputDialog(null, "Qual amigo você quer excluir?",
                    "Escreva o nome do Amigo que deseja Excluir", JOptionPane.PLAIN_MESSAGE);
            if(!amigoExcluir.isEmpty()) {
                boolean buscaBanco = conexao.autenticarUsuario(amigoExcluir);
                if (buscaBanco) {
                    excluir.excluirAmigo(loginUsuario, amigoExcluir);
                    JOptionPane.showMessageDialog(null, "Amigo excluído com sucesso!",
                            "Deu certo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Amigo não foi encontrado.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                System.err.println("Digite o nome de algum usuário.");
                JOptionPane.showMessageDialog(null, "Digite o nome de algum usuário.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch(NullPointerException e){
            e.getMessage();
        }

    }

    public void consultarAmigos(String loginUsuario) throws SQLException {
        consultar.consultarAmigos(loginUsuario);

    }

    public void enviarMensagens(String loginUsuario) throws SQLException {
        String mensagemDoAmigo = JOptionPane.showInputDialog(null,
                "Para qual amigo deseja enviar mensagem?","Digite o nome do Amigo", JOptionPane.QUESTION_MESSAGE);
        boolean amigoEncontrado = conexao.autenticarUsuario(mensagemDoAmigo);
        try {
            if(mensagemDoAmigo != null) {
                if (amigoEncontrado) {
                    String conteudo = JOptionPane.showInputDialog(null, "Digite a mensagem:",
                            "", JOptionPane.PLAIN_MESSAGE);
                    if(!conteudo.isEmpty()) {
                        enviar.enviarMensagem(loginUsuario, mensagemDoAmigo, conteudo);
                        JOptionPane.showMessageDialog(null, "Mensagem enviada!",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        System.err.println("Conteúdo da mensagem não pode estar vazio.");
                        JOptionPane.showMessageDialog(null, "Digite alguma mensagem!",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Amigo não encontrado.",
                            "Erro!", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                System.err.println("Operação cancelada.");
            }
        } catch(NullPointerException e){
            e.getMessage();
        }
    }

    public void listarMensagens(String loginUsuario) throws SQLException {
        try {
            String conversa = JOptionPane.showInputDialog(null,
                    "Deseja ver a conversa com qual amigo?", "Digite o nome do Amigo",
                    JOptionPane.PLAIN_MESSAGE);
            if(!conversa.isEmpty()) {
                boolean buscaBanco = conexao.autenticarUsuario(conversa);

                if (buscaBanco) {
                    listar.listarMensagens(loginUsuario, conversa);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Não há registro de mensagens com esse usuário.",
                            "Conversa vazia", JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                System.err.println("Insira algum amigo");
                JOptionPane.showMessageDialog(null,
                        "Insira algum amigo", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch(NullPointerException e){
            e.getMessage();
        }

    }

    static class DomainException extends Exception {
    }
}