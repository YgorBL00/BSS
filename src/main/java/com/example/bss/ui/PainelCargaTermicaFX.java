package com.example.bss.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import com.example.bss.model.*;

import java.util.*;

public class PainelCargaTermicaFX extends HBox {

    // --- Labels de dimensões
    private final Label lbComprimento = new Label();
    private final Label lbLargura = new Label();
    private final Label lbAltura = new Label();
    private final Label lbEspessura = new Label();

    // --- Isolamento
    private final ComboBox<String> cbIsolamento = new ComboBox<>();
    private final TextField tfCondutividade = new TextField();

    // --- Produto, Temperaturas e Porta
    private final TextField tfTempAmbiente = new TextField();
    private final TextField tfTempDesejada = new TextField();
    private final ComboBox<String> cbTipoProduto = new ComboBox<>();
    private final ComboBox<String> cbProduto = new ComboBox<>();
    private final TextField tfTempEntradaProduto = new TextField();
    private final TextField tfQtdProdutoDia = new TextField();
    private final TextField tfTempoProcessamento = new TextField();
    private final ComboBox<String> cbTipoPorta = new ComboBox<>();
    private final ComboBox<Integer> cbBatentesPorta = new ComboBox<>();
    private final Button btnRecomendar = new Button("Recomendar Equipamentos");

    // --- Carga térmica
    private final TextField tfCargaTermica = new TextField();

    // --- Produtos e condutividades
    private final Map<String, List<String>> produtosPorTipo = new HashMap<>();
    private final Map<String, String> condutividades = Map.of(
            "PIR", "0,021 W/m·K",
            "PUR", "0,023 W/m·K",
            "EPS", "0,036 W/m·K"
    );

    // --- Referência ao painel de materiais
    private PainelMaterialFX painelMaterialFX;

    // --- Construtor
    public PainelCargaTermicaFX() {
        setSpacing(18);
        setPadding(new Insets(18));
        setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #e3f4ff, white);");

        inicializarProdutosPorTipo();

        VBox blocoEsquerdo = criarBlocoEsquerdo();
        VBox blocoMeio = criarBlocoMeio();
        VBox blocoDireito = criarBlocoDireito();

        getChildren().addAll(blocoEsquerdo, blocoMeio, blocoDireito);
        atualizarDados();
    }

    // --- Métodos de inicialização ---
    private void inicializarProdutosPorTipo() {
        produtosPorTipo.put("Frutas", Arrays.asList("Maçã", "Banana", "Laranja", "Uva", "Pera", "Melancia", "Manga", "Abacaxi", "Morango", "Melão"));
        produtosPorTipo.put("Carnes", Arrays.asList("Bovina", "Suína", "Frango", "Peixe", "Cordeiro", "Carneiro", "Peru", "Pato", "Coelho", "Vitela"));
        produtosPorTipo.put("Laticínios", Arrays.asList("Leite", "Queijo", "Iogurte", "Manteiga", "Requeijão", "Leite condensado", "Creme de leite", "Soro de leite", "Ricota", "Doce de leite"));
        produtosPorTipo.put("Grãos", Arrays.asList("Arroz", "Feijão", "Trigo", "Soja", "Milho", "Cevada", "Aveia", "Quinoa", "Lentilha", "Grão-de-bico"));
        produtosPorTipo.put("Vegetais", Arrays.asList("Batata", "Cenoura", "Tomate", "Cebola", "Alface", "Brócolis", "Espinafre", "Pepino", "Abobrinha", "Berinjela"));
    }

    private VBox criarBlocoEsquerdo() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(16));
        box.setPrefWidth(310);
        box.setStyle("-fx-background-color: #f9fafd; -fx-border-color: #e3e6ef; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7;");

        // Painel de Dimensões
        VBox painelDimensao = new VBox(7,
                titulo("Dimensões da Câmara"),
                linha("Comprimento (m):", lbComprimento),
                linha("Largura (m):", lbLargura),
                linha("Altura (m):", lbAltura),
                linha("Espessura (mm):", lbEspessura)
        );

        // Define largura igual para ComboBox e TextField
        double larguraCampo = 160;
        cbIsolamento.setPrefWidth(larguraCampo);
        tfCondutividade.setPrefWidth(larguraCampo);
        tfCondutividade.setEditable(false);
        tfCondutividade.setFocusTraversable(false);

        // Configuração do ComboBox
        cbIsolamento.getItems().setAll("PIR", "PUR", "EPS");
        cbIsolamento.setValue("PIR");
        cbIsolamento.valueProperty().addListener((o, a, novo) -> atualizarCondutividade());

        // Linha do ComboBox com label à esquerda, campo à direita
        Label labelTipo = new Label("Tipo:");
        Region spacerTipo = new Region();
        HBox.setHgrow(spacerTipo, Priority.ALWAYS);
        HBox linhaTipo = new HBox(10, labelTipo, spacerTipo, cbIsolamento);
        linhaTipo.setAlignment(Pos.CENTER_LEFT);

        // Linha da condutividade com label à esquerda, campo à direita
        Label labelCond = new Label("Condutividade (λ):");
        Region spacerCond = new Region();
        HBox.setHgrow(spacerCond, Priority.ALWAYS);
        HBox linhaCondutividade = new HBox(10, labelCond, spacerCond, tfCondutividade);
        linhaCondutividade.setAlignment(Pos.CENTER_LEFT);

        // Painel de Isolamento alinhado
        VBox painelIsolamento = new VBox(7,
                titulo("Isolamento"),
                linhaTipo,
                linhaCondutividade
        );

        // Espaçador para expandir o painel inferiormente
        Region espaco = new Region();
        VBox.setVgrow(espaco, Priority.ALWAYS);

        // Monta a VBox principal
        box.getChildren().setAll(painelDimensao, painelIsolamento, espaco);
        box.setAlignment(Pos.TOP_LEFT);

        atualizarCondutividade();
        return box;
    }

    // Exemplo de função utilitária para montar linhas:


    private VBox criarBlocoMeio() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(16));
        box.setPrefWidth(400);
        box.setStyle("-fx-background-color: #f9fafd; -fx-border-color: #e3e6ef; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7;");

        Label titulo = titulo("Condições e Produto");

        cbTipoProduto.getItems().addAll(produtosPorTipo.keySet());
        cbTipoProduto.setPromptText("Escolha o tipo");
        cbTipoProduto.valueProperty().addListener((o, a, novo) -> atualizarProdutos());
        cbTipoProduto.setValue("Frutas");

        cbProduto.setPromptText("Escolha o produto");

        cbTipoPorta.getItems().addAll("Congelado", "Resfriado");
        cbTipoPorta.setPromptText("Tipo de porta");

        cbBatentesPorta.getItems().addAll(3, 4);
        cbBatentesPorta.setPromptText("Batentes da porta");

        atualizarProdutos();

        // Novo ComboBox para temperatura ambiente
        ComboBox<Integer> cbTempAmbiente = new ComboBox<>();
        cbTempAmbiente.getItems().addAll(32, 35, 38, 43);
        cbTempAmbiente.setPromptText("Escolha a temperatura");
        cbTempAmbiente.setValue(32); // Valor padrão, se desejar

        GridPane painel = new GridPane();
        painel.setHgap(8);
        painel.setVgap(8);
        int row = 0;
        painel.add(new Label("Temperatura ambiente (°C):"), 0, row);
        painel.add(cbTempAmbiente, 1, row++);
        painel.add(new Label("Temperatura desejada (°C):"), 0, row);
        painel.add(tfTempDesejada, 1, row++);
        painel.add(new Label("Tipo do produto:"), 0, row);
        painel.add(cbTipoProduto, 1, row++);
        painel.add(new Label("Produto:"), 0, row);
        painel.add(cbProduto, 1, row++);
        painel.add(new Label("Temp. entrada do produto (°C):"), 0, row);
        painel.add(tfTempEntradaProduto, 1, row++);
        painel.add(new Label("Qtd produto/dia (kg):"), 0, row);
        painel.add(tfQtdProdutoDia, 1, row++);
        painel.add(new Label("Tempo process. (h):"), 0, row);
        painel.add(tfTempoProcessamento, 1, row++);
        painel.add(new Label("Tipo de porta:"), 0, row);
        painel.add(cbTipoPorta, 1, row++);
        painel.add(new Label("Batentes da porta:"), 0, row);
        painel.add(cbBatentesPorta, 1, row++);

        box.getChildren().addAll(titulo, painel);
        box.setAlignment(Pos.TOP_LEFT);
        return box;
    }

    private VBox criarBlocoDireito() {
        VBox box = new VBox(22);
        box.setPadding(new Insets(16));
        box.setPrefWidth(260);
        box.setStyle("-fx-background-color: #f9fafd; -fx-border-color: #e3e6ef; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7;");

        Label titulo = titulo("Carga Térmica");
        Label sub = new Label("Carga térmica necessária (kcal/h):");
        HBox botaoBox = new HBox(btnRecomendar);
        botaoBox.setAlignment(Pos.BOTTOM_LEFT);

        btnRecomendar.setOnAction(event -> realizarRecomendacao());

        box.getChildren().addAll(titulo, sub, tfCargaTermica, botaoBox);
        return box;
    }

    // --- Eventos e lógica de negócios ---

    private void realizarRecomendacao() {
        try {
            double tempDesejada = Double.parseDouble(tfTempDesejada.getText());
            double cargaTermica = Double.parseDouble(tfCargaTermica.getText());

            // PRIMEIRO - salva nos dados globais
            DadosCâmara.setTemperaturaInterna(tempDesejada);
            DadosCâmara.setCargaTermica(cargaTermica);

            // DEPOIS - recomenda com os dados atualizados
            UnidadeCondensadoras motor = RecomendacaoMotor.recomendarMotor(tempDesejada, cargaTermica).get();
            Evaporadoras evap = RecomendacaoEvaporadora.recomendarEvaporadoras(tempDesejada, cargaTermica).get(0);

            // Salva também recomendados para uso posterior
            DadosCâmara.setMotorRecomendado(motor);
            DadosCâmara.setEvaporadoraRecomendada(evap);

            mostrarResultado(motor, evap);

            if (painelMaterialFX != null) {
                // Adicionar porta, recomendados, complementares
                if (cbTipoPorta.getValue() != null && cbBatentesPorta.getValue() != null) {
                    String modeloPorta = cbTipoPorta.getValue() + " (" + cbBatentesPorta.getValue() + " batentes)";
                    int quantidade = 1;
                    painelMaterialFX.adicionarItemPorta(modeloPorta, quantidade);
                }
                painelMaterialFX.adicionarRecomendadosNaTabela();


            }
        } catch (Exception e) {
            mostrarErro("Preencha corretamente os campos necessários.");
        }
    }


    // Exibe mensagem de erro em um popup
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Exibe a recomendação técnica em um Dialog
    private void mostrarResultado(UnidadeCondensadoras motor, Evaporadoras evap) {
        // Unidade Condensadora
        Text ucTitulo = new Text("🔧 Unidade Condensadora:\n");
        ucTitulo.setStyle("-fx-font-weight: bold; -fx-fill: #1e1e1e; -fx-font-size: 14;");
        Text ucDados = new Text(String.format(
                "Modelo: %s (%s HP)\nCapacidade: %.0f kcal/h\nTemperatura: %.0f a %.0f ºC\nTensão: %s\n\n",
                motor.getModelo(), motor.getHP(), motor.getCapacidadeKcal(),
                motor.getTemperaturaMin(), motor.getTemperaturaMax(), motor.getTensao()
        ));

        // Evaporadora
        Text evapTitulo = new Text("❄️ Evaporadora:\n");
        evapTitulo.setStyle("-fx-font-weight: bold; -fx-fill: #1e1e1e; -fx-font-size: 14;");
        Text evapDados = new Text(String.format(
                "Modelo: %s (%s HP)\nCapacidade: %.0f kcal/h\nTemperatura: %.0f a %.0f ºC\nTensão: %s",
                evap.getModelo(), evap.getHp(), evap.getCapacidadeKcal(),
                evap.getTemperaturaMin(), evap.getTemperaturaMax(), evap.getTensao()
        ));

        TextFlow textFlow = new TextFlow(ucTitulo, ucDados, evapTitulo, evapDados);
        textFlow.setPadding(new Insets(10));

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Recomendação Técnica");
        dialog.getDialogPane().setContent(textFlow);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    // --- Utilitários (helpers de UI) ---

    private Label titulo(String txt) {
        Label lb = new Label(txt);
        lb.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #205081;");
        return lb;
    }

    private HBox linha(String nome, Label valor) {
        HBox h = new HBox(8, new Label(nome), valor);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    // Atualiza os valores das labels de dimensões de acordo com DadosCâmara
    public void atualizarDados() {
        lbComprimento.setText(String.format("%.2f", DadosCâmara.getComprimento()));
        lbLargura.setText(String.format("%.2f", DadosCâmara.getLargura()));
        lbAltura.setText(String.format("%.2f", DadosCâmara.getAltura()));
        lbEspessura.setText(String.format("%.0f", DadosCâmara.getEspessura()));
    }

    private void atualizarCondutividade() {
        String tipo = cbIsolamento.getValue();
        tfCondutividade.setText(condutividades.getOrDefault(tipo, ""));
    }

    private void atualizarProdutos() {
        String tipo = cbTipoProduto.getValue();
        cbProduto.getItems().clear();
        if (tipo != null && produtosPorTipo.containsKey(tipo)) {
            cbProduto.getItems().addAll(produtosPorTipo.get(tipo));
            cbProduto.setValue(produtosPorTipo.get(tipo).get(0));
        }
    }

    // --- Ligação com PainelMaterialFX ---

    public void setPainelMaterialFX(PainelMaterialFX painel) {
        this.painelMaterialFX = painel;
    }
}