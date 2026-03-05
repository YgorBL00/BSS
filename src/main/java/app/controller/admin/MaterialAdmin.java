package app.controller.admin;

import app.model.Material;
import app.service.MaterialService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MaterialAdmin {

    @FXML
    private TableView<Material> tableMateriais;

    @FXML
    private TableColumn<Material, Number> colId;

    @FXML
    private TableColumn<Material, String> colNome;

    @FXML
    private TableColumn<Material, Number> colValor;

    @FXML
    private TableColumn<Material, String> colUnidade;

    @FXML
    private TableColumn<Material, String> colClasse;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnVoltar;

    private final MaterialService materialService = new MaterialService();

    private ObservableList<Material> materiais;

    @FXML
    public void initialize() {

        configurarTabela();
        carregarMateriais();
        configurarBotoes();

    }

    private void configurarTabela() {

        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colNome.setCellValueFactory(cell -> cell.getValue().nomeProperty());
        colValor.setCellValueFactory(cell -> cell.getValue().valorProperty());
        colUnidade.setCellValueFactory(cell -> cell.getValue().unidadeProperty());
        colClasse.setCellValueFactory(cell -> cell.getValue().classeProperty());

    }

    private void carregarMateriais() {

        materiais = FXCollections.observableArrayList(materialService.buscarTodos());
        tableMateriais.setItems(materiais);

    }

    private void configurarBotoes() {

        btnEditar.setOnAction(e -> {

            Material selecionado = tableMateriais.getSelectionModel().getSelectedItem();

            if (selecionado != null) {
                abrirPopupEditar(selecionado);
            } else {
                System.out.println("Selecione um material");
            }

        });

        btnVoltar.setOnAction(e -> {

            try {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/app/admin/painel-admin.fxml")
                );

                Parent root = loader.load();

                Stage stage = (Stage) btnVoltar.getScene().getWindow();
                stage.setScene(new Scene(root, 1150, 750));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
    }

    private void abrirPopupEditar(Material material) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/admin/material-editar-admin.fxml")
            );
            Parent root = loader.load();

            MaterialEditarAdmin controller = loader.getController();
            controller.setMaterial(material);

            Stage stage = new Stage();
            stage.setTitle("Editar Valor do Material");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarMateriais();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}