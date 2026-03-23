package app.controller.vendedor;

import app.model.*;
import app.model.LogicaRefrigeracao;
import app.service.FormatoCalculator;
import app.service.ProdutoService;
import app.service.UCService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RefrigeracaoUsuario {

    @FXML
    public Button btnOrcamento;
    @FXML
    public TextField txtTempInterna;
    @FXML
    public TextField txtTempEntrada;
    @FXML
    public TextField txtCargaProduto;
    @FXML
    public TextField txtDistQuadroUC;
    @FXML
    public TextField txtDistQuadroEU;
    @FXML
    public TextField txtDistUEUC;
    @FXML
    public TextField txtAlturaInfra;
    public Label lblCargaTermica;
    public Label lblEquipamento;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblTipo;
    @FXML
    private Label lblDimensoes;
    @FXML
    private Label lblEspessura;
    @FXML
    private Label lblAreaPorta;
    @FXML
    private ComboBox<String> cbVariedade;
    @FXML
    private ComboBox<String> cbProduto;
    @FXML
    private ComboBox<Integer> cbTempAmbiente;
    @FXML
    private TextField txtTempoProcesso;
    @FXML
    private TextField txtCondutividade;

    private Usuario usuario;
    private FormatoCalculator.ResultadoFormato resultados;
    private int espessura;
    private List<Porta> portas;
    private List<Produto> todosProdutos;


    private double cargaCalculada;
    private int ambienteSelecionado;
    private double evapCalculada;
    private String gasSelecionado;
    private String tipoCamara;
    private Equipamento equipamentoSelecionado;

    public void setEspessura(int espessura) {
        this.espessura = espessura;
        if (lblEspessura != null) {
            lblEspessura.setText(espessura + " mm");
        }
    }

    public void setTipoCamara(String tipoCamara) {
        this.tipoCamara = tipoCamara;   // guarda o valor
        atualizarLabelTipo();
    }

    private void atualizarLabelTipo() {
        if (lblTipo != null && tipoCamara != null) {
            lblTipo.setText(tipoCamara);
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setResultados(FormatoCalculator.ResultadoFormato resultados) {
        this.resultados = resultados;
    }

    public void setCliente(String cliente) {
        lblCliente.setText(cliente);
    }

    public void setDimensoes(String dim) {
        lblDimensoes.setText(dim);
    }

    @FXML
    public void initialize() {

        // 1️⃣ Inicializa ComboBox de temperatura ambiente
        cbTempAmbiente.setItems(FXCollections.observableArrayList(32, 35, 38, 43));
        cbTempAmbiente.setValue(32); // valor padrão

        // 2️⃣ Inicializa ComboBox de variedades
        cbVariedade.setItems(FXCollections.observableArrayList(
                "Todos",
                "Carnes",
                "Frutas",
                "Verduras",
                "Bebidas",
                "Laticinios"
        ));

        // 3️⃣ Inicializa campo de tempo de processo
        txtTempoProcesso.setText("24");

        // 4️⃣ Inicializa a lista de produtos
        ProdutoService service = new ProdutoService();
        todosProdutos = service.buscarTodos(); // carrega todos os produtos do banco

        // 5️⃣ Configura ação do ComboBox de variedade
        cbVariedade.setOnAction(e -> carregarProdutos());
        // 6️⃣ Carrega produtos inicialmente
        carregarProdutos();

        // ⚠️ Não tenta atualizar lblTipo aqui, pois tipoCamara ainda não foi setado
    }

    private double getCalorMedio(String categoria) {

        switch (categoria) {

            case "carne":
                return 0.80;

            case "fruta":
                return 0.90;

            case "verdura":
                return 0.95;

            case "bebida":
                return 1.00;

            case "laticinio":
                return 0.85;

            default:
                return 0.90;
        }
    }

    private void carregarProdutos() {

        String variedade = cbVariedade.getValue();

        ObservableList<String> lista = FXCollections.observableArrayList();

        if (variedade == null) return;

        if (variedade.equals("Todos")) {

            for (Produto p : todosProdutos) {
                lista.add(p.getNome());
            }

        } else {

            String categoria = "";

            switch (variedade) {
                case "Carnes":
                    categoria = "carne";
                    break;

                case "Frutas":
                    categoria = "fruta";
                    break;

                case "Verduras":
                    categoria = "verdura";
                    break;

                case "Bebidas":
                    categoria = "bebida";
                    break;

                case "Laticinios":
                    categoria = "laticinio";
                    break;
            }

            for (Produto p : todosProdutos) {

                if (p.getCategoria().equalsIgnoreCase(categoria)) {
                    lista.add(p.getNome());
                }
            }
        }

        cbProduto.setOnAction(e -> {

            String nome = cbProduto.getValue();

            for (Produto p : todosProdutos) {

                if (p.getNome().equals(nome)) {

                    double calor = getCalorMedio(p.getCategoria());

                    txtCondutividade.setText(String.valueOf(calor));
                    break;
                }
            }
        });

        cbProduto.setItems(lista);
    }

    @FXML
    public void abrirResultado() {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/app/usuario/resultado.fxml")
            );


            Parent root = loader.load();

            ResultadoUsuario controller = loader.getController();

            controller.setDadosRefrigeracao(
                    cargaCalculada,
                    ambienteSelecionado,
                    evapCalculada,
                    gasSelecionado
            );

            controller.carregarDados(
                    usuario,
                    resultados,
                    espessura,
                    lblCliente.getText(),
                    lblTipo.getText(),
                    lblDimensoes.getText(),
                    portas
            );

            controller.setTipoCamara(tipoCamara);
            controller.setEquipamento(equipamentoSelecionado);

            Stage stage = (Stage) btnOrcamento.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltarCaixote() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/usuario/refrigeracao.fxml"));
            Parent root = loader.load();

            RefrigeracaoUsuario controller = loader.getController();
            controller.setTipoCamara("Frigorífico");
            controller.setUsuario(usuario);
            controller.setResultados(resultados);
            controller.setCliente("Cliente XYZ");
            controller.setDimensoes("2x3x2 m");
            controller.setPortas(portas);


            Stage stage = (Stage) btnOrcamento.getScene().getWindow();
            stage.setScene(new Scene(root, 1150, 750));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPortas(List<Porta> portas) {

        this.portas = portas;

        double area = 0;

        if (portas == null || portas.isEmpty()) {
            area = 1.44;
        } else {

            for (Porta p : portas) {

                String tamanho = p.getTamanho()
                        .replace(",", ".")
                        .toLowerCase();

                String[] partes = tamanho.split("x");

                double largura = Double.parseDouble(partes[0]);
                double altura = Double.parseDouble(partes[1]);

                area += largura * altura;
            }
        }



        if (lblAreaPorta != null) {
            lblAreaPorta.setText(String.format("%.2f m²", area));
        }
    }

    @FXML
    public void calcularRefrigeracao() {

        // 1️⃣ Verificação de campos obrigatórios
        if (txtTempInterna.getText().isEmpty() ||
                txtTempEntrada.getText().isEmpty() ||
                txtCargaProduto.getText().isEmpty() ||
                txtTempoProcesso.getText().isEmpty() ||
                cbProduto.getValue() == null ||
                cbVariedade.getValue() == null ||
                cbTempAmbiente.getValue() == null ||
                resultados == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dados incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, preencha todos os campos antes de calcular.");
            alert.showAndWait();


            return;
        }

        try {
            // 2️⃣ Extrair valores
            double tempInterna = Double.parseDouble(txtTempInterna.getText());
            double tempEntrada = Double.parseDouble(txtTempEntrada.getText());
            double cargaProduto = Double.parseDouble(txtCargaProduto.getText());
            double tempoProcesso = Double.parseDouble(txtTempoProcesso.getText());
            int tempAmbiente = cbTempAmbiente.getValue();

            // 3️⃣ Produto selecionado
            List<LogicaRefrigeracao.Produto> listaProdutos = List.of(
                    new LogicaRefrigeracao.Produto(
                            cbProduto.getValue(),
                            cbVariedade.getValue(),
                            cargaProduto,
                            Double.parseDouble(txtCondutividade.getText()),
                            tempEntrada
                    )
            );

            // 4️⃣ Calcular área e volume da câmara (usando Resultados do FormatoCalculator)
            double areaPainel = resultados.getAreaTotal();      // m²
            double volumeCamara = resultados.getVolumeTotal();  // m³

            // 5️⃣ Chamada do motor de cálculo
            double cargaKcal = LogicaRefrigeracao.calcularCargaTermica(
                    areaPainel,
                    volumeCamara,
                    tempInterna,
                    tempAmbiente,
                    listaProdutos,
                    espessura,      // painel
                    tempoProcesso,
                    1,              // trocas de ar/hora, fixo por enquanto
                    1,              // pessoas, fixo
                    true,           // iluminação
                    0               // equipamentos extras
            );

            // 6️⃣ Atualizar labels
            lblCargaTermica.setText(String.format("%.0f kcal/h | %.2f kW | %.2f TR",
                    cargaKcal,
                    LogicaRefrigeracao.kcalhToKW(cargaKcal),
                    LogicaRefrigeracao.kcalhToTR(cargaKcal)
            ));

            this.cargaCalculada = cargaKcal;
            this.ambienteSelecionado = tempAmbiente;

            String gas = definirGas(this.tipoCamara);

            UCService service = new UCService();

            double cargaComSeguranca = cargaKcal * 1.10; // +10%

            // 🔹 cálculo correto da evaporação
            int tempEvap = calcularTempEvap(tempInterna, this.tipoCamara);

            // 🔹 busca equipamento
            Equipamento eq = service.buscarEquipamento(
                    cargaComSeguranca,
                    tempAmbiente,
                    tempEvap,
                    gas
            );


            if (eq != null) {

                this.equipamentoSelecionado = eq; // ✅ AQUI

                lblEquipamento.setText(
                        "Modelo: " + eq.getModelo() +
                                " | Gás: " + eq.getGas() +
                                " | Capacidade: " + String.format("%.0f kcal/h", eq.getCarga())
                );

                this.gasSelecionado = eq.getGas();
                this.evapCalculada = tempEvap;

            } else {

                lblEquipamento.setText(
                        "⚠ Equipamento não encontrado\n" +
                                "Carga: " + String.format("%.0f kcal/h", cargaKcal) +
                                " | Evap: " + tempEvap + "°C"
                );
            }


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de formato");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, insira valores numéricos válidos.");
            alert.showAndWait();
        }
    }

    private int calcularTempEvap(double tempInterna, String tipoCamara) {

        double delta;

        if ("CONGELADOS".equals(tipoCamara)) {
            delta = 10; // pode ajustar pra 12 depois
        } else {
            delta = 7;
        }

        int evap = (int) Math.round(tempInterna - delta);

        int[] faixas = {-20, -15, -10, -5, 0, 5, 10};

        int maisProximo = faixas[0];

        for (int f : faixas) {
            if (Math.abs(evap - f) < Math.abs(maisProximo - evap)) {
                maisProximo = f;
            }
        }

        return maisProximo;
    }

    private String definirGas(String tipoCamara) {

        if ("CONGELADOS".equals(tipoCamara)) {
            return "R404A";
        } else {
            return "R22";
        }
    }

}