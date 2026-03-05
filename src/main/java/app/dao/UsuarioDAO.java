package app.dao;




import app.database.DatabaseConnection;
import app.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Buscar usuário pelo email
    public Usuario buscarPorEmail(String email) {
        System.out.println("Buscando usuário com email: '" + email + "'");
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Usuário encontrado: " + rs.getString("nome"));
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setSenhaHash(rs.getString("senha_hash"));
                    u.setCargo(rs.getString("cargo"));
                    return u;
                } else {
                    System.out.println("Nenhum usuário encontrado!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar usuário", e);
        }

        return null;
    }

    // Criar novo usuário
    public void criarUsuario(Usuario u) {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, cargo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenhaHash());
            ps.setString(4, u.getCargo());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar usuário", e);
        }
    }
}