package app.controller.admin;

import app.model.Material;
import app.service.MaterialService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class MaterialEditarAdmin {

    @FXML
    private Label nomeLabel;

    @FXML
    private TextField valorField;

    private Material material;

    private MaterialService materialService = new MaterialService();

    public void setMaterial(Material material) {

        this.material = material;

        nomeLabel.setText(material.getNome());
        valorField.setText(String.valueOf(material.getValor()));
    }

    @FXML
    private void salvar() {

        try {

            double novoValor = Double.parseDouble(valorField.getText());

            material.setValor(novoValor);
            materialService.atualizar(material);

            Stage stage = (Stage) valorField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {

            System.out.println("Valor inválido");

        }
    }
}
