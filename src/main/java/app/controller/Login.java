package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import app.dao.UsuarioDAO;
import app.model.Usuario;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Label mensagemErro;

    private static final String ARQUIVO_EMAIL = "login.txt";

    // Chamado automaticamente quando a tela é carregada
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarEmail();
    }

    @FXML
    private void fazerLogin() {

        String email = emailField.getText().trim();  // remove espaços
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mensagemErro.setText("Preencha todos os campos!");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.buscarPorEmail(email);

        if (usuario == null) {
            mensagemErro.setText("Email ou senha inválidos.");
            return;
        }

        // Comparar a senha com o hash armazenado
        if (senhaValida(senha, usuario.getSenhaHash())) {
            mensagemErro.setText("");
            System.out.println("Login realizado com sucesso! Usuário: " + usuario.getNome());

            // Salvar email para próxima vez
            salvarEmail(usuario.getEmail());

            // Aqui você pode abrir a próxima tela
        } else {
            mensagemErro.setText("Email ou senha inválidos.");
        }
    }

    // Método para validar senha com SHA-256
    private boolean senhaValida(String senha, String hashArmazenado) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().equals(hashArmazenado);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Salva o email em arquivo
    private void salvarEmail(String email) {
        try (FileWriter writer = new FileWriter(ARQUIVO_EMAIL)) {
            writer.write(email);
            System.out.println("Email salvo com sucesso: " + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carrega o email do último login, se existir
    private void carregarEmail() {
        File arquivo = new File(ARQUIVO_EMAIL);
        if (arquivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String email = br.readLine();
                if (email != null && !email.isEmpty()) {
                    emailField.setText(email);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}