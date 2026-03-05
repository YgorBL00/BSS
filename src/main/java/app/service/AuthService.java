package app.service;

import app.dao.UsuarioDAO;
import app.model.Usuario;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Login normal
    public Usuario login(String email, String senha) {

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null && senhaValida(senha, usuario.getSenhaHash())) {
            return usuario;
        }

        return null;
    }

    // Criar usuário (ex: vendedor ou admin)
    public void criarUsuario(String nome, String email, String senha, String cargo) {

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenhaHash(gerarHash(senha));
        u.setCargo(cargo);

        usuarioDAO.criarUsuario(u);
    }

    // Login automático (quando o email está salvo)
    public Usuario loginAutomatico(String email) {
        return usuarioDAO.buscarPorEmail(email);
    }

    // Gerar hash da senha (SHA-256)
    private String gerarHash(String senha) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(senha.getBytes());

            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }

    // Verificar senha
    private boolean senhaValida(String senha, String hashArmazenado) {
        return gerarHash(senha).equals(hashArmazenado);
    }
}