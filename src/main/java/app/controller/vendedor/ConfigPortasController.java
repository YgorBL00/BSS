package app.controller.vendedor;

import app.model.Porta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ConfigPortasController {

    @FXML private TableView<Porta> tablePortas;

    @FXML private TableColumn<Porta, String> colTipo;
    @FXML private TableColumn<Porta, String> colBatente;
    @FXML private TableColumn<Porta, String> colAbertura;
    @FXML private TableColumn<Porta, String> colTamanho;

    @FXML private ComboBox<String> cbTipo;
    @FXML private ComboBox<String> cbBatente;
    @FXML private ComboBox<String> cbAplicacao;
    @FXML private ComboBox<String> cbAbertura;
    @FXML private ComboBox<String> cbTamanho;

    private ObservableList<Porta> lista = FXCollections.observableArrayList();

    public ObservableList<Porta> getLista() {
        return lista;
    }

    @FXML
    public void initialize() {

        // preencher combos
        cbTipo.valueProperty().addListener((obs, oldVal, newVal) -> {

            if ("Expositora".equals(newVal)) {

                cbBatente.setDisable(true);
                cbAplicacao.setDisable(true);
                cbTamanho.setDisable(true);

                cbTamanho.setValue("0,60 x 1,60");

            } else {

                cbBatente.setDisable(false);
                cbAplicacao.setDisable(false);
                cbTamanho.setDisable(false);

                cbTamanho.setValue(null);
            }
        });

        cbTipo.getItems().addAll(
                "Giratória",
                "Correr",
                "Expositora"
        );

        cbBatente.getItems().addAll(
                "3B",
                "4B"
        );

        cbAplicacao.getItems().addAll(
                "Resfriados",
                "Congelados"
        );

        cbAbertura.getItems().addAll(
                "Direita",
                "Esquerda",
                "Reversível"
        );

        cbTamanho.getItems().addAll(
                "0,80 x 1,80",
                "1,00 x 2,00",
                "1,20 x 2,00",
                "1,40 x 2,20",
                "1,60 x 2,40",
                "0,60 x 1,60"
        );

        // tabela

        colTipo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo()));

        colBatente.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getBatente()));

        colAbertura.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getAbertura()));

        colTamanho.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTamanho()));

        tablePortas.setItems(lista);
    }

    @FXML
    private void adicionarPorta() {

        if (cbTipo.getValue() == null
                || cbAbertura.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Campos incompletos");
            alert.setContentText("Selecione o tipo e a abertura da porta.");
            alert.showAndWait();
            return;
        }

        String tipo;
        String batente = cbBatente.getValue();
        String tamanho = cbTamanho.getValue();

        // =============================
        // PORTA EXPOSITORA
        // =============================

        if ("Expositora".equals(cbTipo.getValue())) {

            tipo = "Expositora";

            batente = ""; // não usa batente
            tamanho = "0,60 x 1,60"; // tamanho fixo
        }

        // =============================
        // PORTAS NORMAIS
        // =============================

        else {

            if (cbBatente.getValue() == null
                    || cbAplicacao.getValue() == null
                    || cbTamanho.getValue() == null) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Campos incompletos");
                alert.setContentText("Preencha todas as opções da porta.");
                alert.showAndWait();
                return;
            }

            tipo = cbTipo.getValue() + " - " + cbAplicacao.getValue();
        }

        lista.add(new Porta(
                tipo,
                batente,
                cbAbertura.getValue(),
                tamanho
        ));
    }

    @FXML
    private void removerPorta() {

        Porta selecionada = tablePortas.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            lista.remove(selecionada);
        }
    }

    @FXML
    private void salvar() {
        tablePortas.getScene().getWindow().hide();
    }

    public void setLista(ObservableList<Porta> lista) {
        this.lista = lista;
        tablePortas.setItems(lista);
    }
}