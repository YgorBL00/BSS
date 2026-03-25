package app.controller;

import app.service.AuthService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import app.model.Usuario;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public VBox conteudo;

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
        FadeTransition fadeLogo = new FadeTransition(Duration.seconds(1.0), conteudo);
        fadeLogo.setFromValue(0);
        fadeLogo.setToValue(1);
        fadeLogo.play();
    }

    @FXML
    private void fazerLogin() {

        String email = emailField.getText().trim();
        String senha = senhaField.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mensagemErro.setText("Preencha todos os campos!");
            return;
        }

        AuthService authService = new AuthService();
        Usuario usuario = authService.login(email, senha);

        if (usuario != null) {

            salvarEmail(usuario.getEmail());

            try {

                // verifica o cargo
                if (usuario.getCargo().equalsIgnoreCase("ADMIN")) {
                    abrirTela("/app/admin/painel-admin.fxml", usuario);
                } else {
                    abrirTela("/app/usuario/painel-usuario.fxml", usuario);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mensagemErro.setText("Email ou senha inválidos.");
        }
    }

    private void abrirTela(String fxml, Usuario usuario) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            // pegar controller da tela
            Object controller = loader.getController();

            // se for painel de vendedor
            if (controller instanceof app.controller.vendedor.PainelUsuario painel) {
                painel.setUsuario(usuario);
            }

            // se for painel de admin
            if (controller instanceof app.controller.admin.PainelAdmin painelAdmin) {
                painelAdmin.setUsuario(usuario);
            }

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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