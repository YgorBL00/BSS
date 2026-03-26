package app.controller.vendedor;

import app.model.Projeto;
import app.service.ProjetoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.List;

public class ProjetoSalvoUsuario {

    @FXML private TableView<Projeto> tableProjetos;

    @FXML private TableColumn<Projeto, Integer> colId;
    @FXML private TableColumn<Projeto, String> colCliente;
    @FXML private TableColumn<Projeto, String> colDimensoes;
    @FXML private TableColumn<Projeto, Double> colValor;
    @FXML private TableColumn<Projeto, String> colData;

    private ProjetoService service = new ProjetoService();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colDimensoes.setCellValueFactory(new PropertyValueFactory<>("dimensoes"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorVenda"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        carregarProjetos();
    }

    @FXML
    public void carregarProjetos() {

        List<Projeto> projetos = service.listarProjetos();

        tableProjetos.setItems(
                FXCollections.observableArrayList(projetos)
        );
    }

    @FXML
    private void abrirPDF() {

        Projeto projeto = tableProjetos.getSelectionModel().getSelectedItem();

        if (projeto == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Selecione um projeto!");
            alert.show();

            return;
        }

        try {

            ProjetoService service = new ProjetoService();

            File pdf = service.baixarPDF(projeto.getId());

            if (pdf != null) {

                Desktop.getDesktop().open(pdf);

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("PDF não encontrado no banco.");
                alert.show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/painel-usuario.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) tableProjetos.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}