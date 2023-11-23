package app;

import bdconexao.AdicionarAmigo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;



public class RedeSocialGUI extends JFrame {
    private JPanel painelLogin = new JPanel();
    private JButton login = new JButton("Login");
    private JButton cadastro = new JButton("Cadastrar");
    private JTextField campoTexto1;
    private JTextField campoTexto2;
    private JLabel labelTexto1;
    private JLabel labelTexto2;
    private JPanel painelLogado = new JPanel();
    private JButton adicionar = new JButton("Adicionar amigos");
    private JButton listar = new JButton("Listar amigos");
    private JButton mensagem = new JButton("Enviar Mensagens");
    private JButton listarMensagem = new JButton("Mensagens");
    private JButton excluir = new JButton("Excluir amigos");
    private JButton sair = new JButton("Logout");
    private Usuario user = new Usuario();
    private String vNome = "";
    private String vSenha = "";
    private AdicionarAmigo.LogarUsuario lu = new AdicionarAmigo.LogarUsuario();


    public RedeSocialGUI() {
        configurarPainel();
        painelLogado();
        painelLogin();
    }
    private void configurarPainel() {
        AdicionarAmigo.LogarUsuario logarUsuario = new AdicionarAmigo.LogarUsuario();
        this.setTitle("Bem vindo a rede social");
        //this.setBounds(1000, 1000, 1000, 1000);
        this.setResizable(false);

        painelLogin.setLayout(null);
        painelLogin.setBackground(new Color(71, 119, 113));

        labelTexto1 = new JLabel("Email:  ");
        Font fonte = labelTexto1.getFont();
        labelTexto1.setFont(new Font(fonte.getName(), Font.ITALIC, 15));
        labelTexto1.setBounds(120, 50, 350, 25);

        labelTexto2 = new JLabel("Senha: ");
        Font fonte2 = labelTexto2.getFont();
        labelTexto2.setFont(new Font(fonte2.getName(), Font.ITALIC, 15));
        labelTexto2.setBounds(120, 100, 350, 25);


        campoTexto1 = new JTextField();
        campoTexto1.setBounds(200, 50, 150, 25);

        campoTexto2 = new JTextField();
        campoTexto2.setBounds(200, 100, 150, 25);


        login.setBounds(165, 160, 150, 25);
        cadastro.setBounds(165, 200, 150, 25);


        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    vNome = campoTexto1.getText();
                    vSenha = campoTexto2.getText();
                    if(!campoTexto1.getText().isBlank() || !campoTexto2.getText().isBlank() || !campoTexto1.getText().isEmpty() || !campoTexto2.getText().isEmpty()) {
                        boolean resultado = logarUsuario.verificarUsuario(vNome,vSenha);
                        if(resultado) {
                            user.logarUsuario(vNome, vSenha);
                            exibirPainelLogado();
                            getContentPane().add(painelLogado);
                            setVisible(true);
                            getContentPane().remove(painelLogin);
                            painelLogin.setVisible(false);
                            painelLogado.setVisible(true);
                            revalidate();
                            repaint();
                        }else{
                            JOptionPane.showMessageDialog(null, "Acesso negado! \nUsuário ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Acesso negado! \nUtulize algum usuário e senha para continuar.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch(SQLException exception){
                    exception.printStackTrace();
                } catch (Usuario.DomainException ex) {
                    throw new RuntimeException(ex);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user.cadastrar();
                }catch(SQLException exception){
                    exception.getMessage();
                }
            }
        });

        painelLogin.add(labelTexto1);
        painelLogin.add(labelTexto2);
        painelLogin.add(campoTexto1);
        painelLogin.add(campoTexto2);
        painelLogin.add(login);
        painelLogin.add(cadastro);
        this.setLocationRelativeTo(null);
    }
    private void painelLogado() {
        this.setTitle(" Mini Rede Social");
        this.setSize(800, 400);
        this.setResizable(false);

        painelLogado.setLayout(null);
        painelLogado.setBackground(new Color(71, 119, 113));

        adicionar.setBounds(80, 50, 150, 25);
        excluir.setBounds(80, 100, 150, 25);
        listar.setBounds(80, 150, 150, 25);
        mensagem.setBounds(260, 50, 150, 25);
        listarMensagem.setBounds(260, 100, 150, 25);
        sair.setBounds(360, 200, 100, 25);

        adicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user.adicionarAmigo(vNome);
                } catch (SQLException exception){
                    exception.getMessage();
                }
            }
        });
        excluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user.excluirAmigo(vNome);
                } catch (SQLException exception){
                    exception.getMessage();
                }
            }
        });
        listar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user.consultarAmigos(vNome);
                } catch (SQLException exception){
                    exception.getMessage();
                }
            }
        });
        mensagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    user.enviarMensagens(vNome);
                } catch (SQLException exception){
                    exception.getMessage();
                }
            }
        });
        listarMensagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    user.listarMensagens(vNome);
                } catch (SQLException exception){
                    exception.getMessage();
                }
            }
        });
        sair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                painelLogin.setVisible(true);
                painelLogado.setVisible(false);

                getContentPane().remove(painelLogado);
                getContentPane().add(painelLogin);

                campoTexto1.setText("");
                campoTexto2.setText("");

                revalidate();
                repaint();
            }
        });

        painelLogado.add(adicionar);
        painelLogado.add(excluir);
        painelLogado.add(listar);
        painelLogado.add(mensagem);
        painelLogado.add(listarMensagem);
        painelLogado.add(sair);
        this.setLocationRelativeTo(null);
    }

    private void painelLogin() {
        getContentPane().removeAll();
        getContentPane().add(painelLogin);
        revalidate();
        repaint();
    }
    private void exibirPainelLogado() {
        getContentPane().removeAll();
        getContentPane().add(painelLogado);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RedeSocialGUI gui = new RedeSocialGUI();
            gui.setSize(500, 300);
            gui.setLocationRelativeTo(null);
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setVisible(true);
        });
    }
}
