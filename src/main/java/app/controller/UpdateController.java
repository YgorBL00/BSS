package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class UpdateController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label labelStatus;

    public void atualizarProgresso(double valor) {
        progressBar.setProgress(valor);
    }

    public void setStatus(String texto) {
        labelStatus.setText(texto);
    }

}