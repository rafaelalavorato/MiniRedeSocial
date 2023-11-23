package bdconexao;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class AdicionarAmigo extends ConexaoPostgre {

    public static class Cadastrar extends ConexaoPostgre {

        private static final String INSERT_USER = "INSERT INTO usuarios" +
                " (nome, email, senha) VALUES " +
                " (?, ?, ?);";

        public void inserirUsuario(String nome, String email, String senha) throws SQLException {
            System.out.println(INSERT_USER);
            // Estabelecendo conexão com o banco de dados
            try (Connection connection = DriverManager.getConnection(url, user, password);

                 //Cria uma instrução usando o objeto de conexão
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
                preparedStatement.setString(1, nome);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, senha);
                System.out.println(preparedStatement);
                //Executa a consulta ou atualiza a consulta
                preparedStatement.executeUpdate();
            } catch (SQLException e) {

                // imprime informações de exceção SQL
                printSQLException(e);
            }
        }

    }

    public static class LogarUsuario extends bdconexao.ConexaoPostgre {

        private static final String VALIDAR_USER ="SELECT COUNT(*) AS count FROM usuarios WHERE email = ? " +
                "AND senha = ?";

        public boolean verificarUsuario(String email, String senha) throws SQLException {
            boolean amigoEncontrado = false;

            try (Connection connection = DriverManager.getConnection(url, user, password);

                 PreparedStatement preparedStatement = connection.prepareStatement(VALIDAR_USER)) {

                preparedStatement.setString(1, email);
                preparedStatement.setString(2, senha);

                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.println(preparedStatement);

                // Se houver algum resultado na consulta, significa que o usuário foi encontrado
                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    amigoEncontrado = true;
                }
            } catch (SQLException e) {
                printSQLException(e);
            }

            return amigoEncontrado;
        }

    }

    private static final String INSERT_AMIGO = "INSERT INTO amigos" +
            " (usuario1, usuario2) VALUES " +
            " (?, ?);";

    public void adicionarAmigo(String loginUsuario, String novoAmigo) throws SQLException {
        System.out.println(INSERT_AMIGO);
        // Estabelecendo conexão com o banco de dados
        try (Connection connection = DriverManager.getConnection(url, user, password);

             //Cria uma instrução usando o objeto de conexão
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AMIGO)) {
            preparedStatement.setString(1, loginUsuario);
            preparedStatement.setString(2, novoAmigo);

            System.out.println(preparedStatement);
            //Executa a consulta ou atualiza a consulta
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            // imprime informações de exceção SQL
            printSQLException(e);
        }
    }


    public static class ExcluirAmigo extends bdconexao.ConexaoPostgre {

        private static final String DELETE_AMIGO = "DELETE FROM amigos WHERE (usuario1 = ? AND usuario2 = ?)";

        public void excluirAmigo(String loginUsuario, String amigoExcluir) throws SQLException {
            System.out.println(DELETE_AMIGO);
            // Estabelecendo conexão com o banco de dados
            try (Connection connection = DriverManager.getConnection(url, user, password);

                 //Cria uma instrução usando o objeto de conexão
                 PreparedStatement preparedStatement = connection.prepareStatement(DELETE_AMIGO)) {
                preparedStatement.setString(1, loginUsuario);
                preparedStatement.setString(2, amigoExcluir);

                System.out.println(preparedStatement);
                //Executa a consulta ou atualiza a consulta
                preparedStatement.executeUpdate();
            } catch (SQLException e) {

                // imprime informações de exceção SQL
                printSQLException(e);
            }
        }
    }

    public static class consultarAmigos extends bdconexao.ConexaoPostgre {

        private static final String QUERY_AMIGOS = "SELECT CASE " +
                "WHEN usuario1 = ? THEN usuario2 " +
                "ELSE usuario1 " +
                "END AS nome_amigo " +
                "FROM amigos " +
                "WHERE usuario1 = ? OR usuario2 = ?";

        public void consultarAmigos(String loginUsuario) throws SQLException {
            System.out.println(QUERY_AMIGOS);

            ArrayList<String> amigos = new ArrayList<>();
            // Estabelecendo conexão com o banco de dados
            try (Connection connection = DriverManager.getConnection(url, user, password);

                 //Cria uma instrução usando o objeto de conexão
                 PreparedStatement preparedStatement = connection.prepareStatement(QUERY_AMIGOS)) {
                preparedStatement.setString(1, loginUsuario);
                preparedStatement.setString(2, loginUsuario);
                preparedStatement.setString(3, loginUsuario);
                System.out.println(preparedStatement);
                //Executa a consulta ou atualiza a consulta
                ResultSet rs = preparedStatement.executeQuery();

                while(rs.next()) {
                    String nomeAmigo = rs.getString("nome_amigo");
                    amigos.add(nomeAmigo);

                }

                StringBuilder amigosText = new StringBuilder();
                int i = 1;
                for (String amigo : amigos) {
                    amigosText.append(i).append(" - ").append(amigo).append("\n"); // Adiciona o nome do amigo ao texto
                    i++;
                }

                // Exibe todos os nomes de amigos na interface gráfica
                JOptionPane.showMessageDialog(null, amigosText.toString(), "Lista de amigos",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (SQLException e) {

                // imprime informações de exceção SQL
                printSQLException(e);
            }
        }
    }

    public static class ListarMensagens extends bdconexao.ConexaoPostgre {

        private static final String QUERY_MENSAGENS = "SELECT * FROM enviarmensagem where (primeirousuario = ? " +
                "AND segundousuario = ?) OR (primeirousuario = ? AND segundousuario = ?)";

        public void listarMensagens(String loginUsuario, String conversa) {
            System.out.println(QUERY_MENSAGENS);

            ArrayList<String> mensagens = new ArrayList<>();

            // Estabelecendo conexão com o banco de dados
            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement preparedStatement = connection.prepareStatement(QUERY_MENSAGENS)) {

                preparedStatement.setString(1, loginUsuario);
                preparedStatement.setString(2, conversa);
                preparedStatement.setString(3, conversa);
                preparedStatement.setString(4, loginUsuario);

                System.out.println(preparedStatement);
                //Executa a consulta ou atualiza a consulta
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    String usuarioEnvio = rs.getString("primeirousuario");
                    String mensagem = rs.getString("mensagem");

                    if (loginUsuario.equals(usuarioEnvio)) {
                        // Se for o remetente, adiciona a mensagem como enviada por ele
                        mensagens.add("Você: " + mensagem);
                    } else {
                        // Se for o destinatário, adiciona a mensagem como recebida dele
                        mensagens.add(conversa + ": " + mensagem);
                    }
                }

                StringBuilder mensagensText = new StringBuilder();
                for (String mensagem : mensagens) {
                    mensagensText.append(mensagem).append("\n");
                }

                JOptionPane.showMessageDialog(null, mensagensText.toString(),
                        "Conversa com "+ conversa, JOptionPane.PLAIN_MESSAGE);
            } catch (SQLException e) {
                // imprime informações de exceção SQL
                printSQLException(e);
            }
        }

    }

    public static class EnviarMensagem extends bdconexao.ConexaoPostgre {

        private static final String INSERT_MENSAGEM = "INSERT INTO enviarmensagem (primeirousuario, segundousuario, " +
                "mensagem) VALUES (?, ?, ?)"
                + " ON CONFLICT (primeirousuario, segundousuario) DO UPDATE SET mensagem = EXCLUDED.mensagem";
        public void enviarMensagem(String loginUsuario, String amigo, String mensagem) throws SQLException {
            System.out.println(INSERT_MENSAGEM);
            // Estabelecendo conexão com o banco de dados
            try (Connection connection = DriverManager.getConnection(url, user, password);

                 //Cria uma instrução usando o objeto de conexão
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MENSAGEM)) {
                preparedStatement.setString(1, loginUsuario);
                preparedStatement.setString(2, amigo);
                preparedStatement.setString(3, mensagem);

                System.out.println(preparedStatement);
                //Executa a consulta ou atualiza a consulta
                preparedStatement.executeUpdate();
            } catch (SQLException e) {

                // imprime informações de exceção SQL
                printSQLException(e);
            }
        }
    }
}