package app.controller.vendedor;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OrcamentoController {

    @FXML private Label lblCliente;
    @FXML private Label lblCusto;
    @FXML private Label lblVenda;
    @FXML private Label lblResumo;

    @FXML private ComboBox<Integer> cbDias;
    @FXML private ComboBox<Integer> cbPessoas;

    @FXML private TextField txtKm;
    @FXML private TextField txtPedagio;
    @FXML private TextField txtDiasHospedagem;
    @FXML private TextField txtMargem;
    @FXML private TextField txtImposto;

    @FXML private CheckBox chkHospedagem;
    @FXML private CheckBox chkPTA;

    private double custo;

    // 🔥 BASE
    private final double VALOR_DIA = 208;
    private final double VALOR_KM = 1.0;
    private final double HOSPEDAGEM_DIA = 250;
    private final double ALIMENTACAO_DIA = 60;
    private final double PTA_MENSAL = 5000;
    private final double IMPOSTO_FIXO = 5.93;

    @FXML
    public void initialize() {

        for (int i = 1; i <= 30; i++) cbDias.getItems().add(i);
        for (int i = 1; i <= 6; i++) cbPessoas.getItems().add(i);

        cbDias.setValue(1);
        cbPessoas.setValue(1);

        // valores padrão (editáveis)
        txtMargem.setText("30");
        txtImposto.setText("23,93");

        chkHospedagem.setOnAction(e -> {
            txtDiasHospedagem.setDisable(!chkHospedagem.isSelected());
        });
    }

    public void setDados(String cliente, String custoTexto) {
        lblCliente.setText("Cliente: " + cliente);
        lblCusto.setText(custoTexto);
        this.custo = parseBR(custoTexto);
    }

    private double parseBR(String valor) {
        return Double.parseDouble(
                valor.replace("R$", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .trim()
        );
    }

    @FXML
    private void calcularVenda() {

        try {
            int dias = cbDias.getValue();
            int pessoas = cbPessoas.getValue();

            double margem = parseBR(txtMargem.getText());
            double imposto = parseBR(txtImposto.getText());

            double km = txtKm.getText().isEmpty() ? 0 : parseBR(txtKm.getText());
            double pedagio = txtPedagio.getText().isEmpty() ? 0 : parseBR(txtPedagio.getText());

            // =========================
            // MÃO DE OBRA
            // =========================
            double maoDeObra = dias * pessoas * VALOR_DIA;

            // =========================
            // KM
            // =========================
            double deslocamento = km * VALOR_KM;

            // =========================
            // HOSPEDAGEM
            // =========================
            double hospedagem = 0;
            double alimentacao = 0;

            if (chkHospedagem.isSelected()) {

                int diasHosp = txtDiasHospedagem.getText().isEmpty()
                        ? dias
                        : (int) parseBR(txtDiasHospedagem.getText());

                hospedagem = diasHosp * HOSPEDAGEM_DIA;

                // 🔥 alimentação só se tiver hospedagem
                alimentacao = diasHosp * pessoas * ALIMENTACAO_DIA;
            }

            // =========================
            // PTA
            // =========================
            double pta = 0;

            if (chkPTA.isSelected()) {
                pta = (PTA_MENSAL / 30.0) * dias;
            }

            // =========================
            // CUSTO TOTAL
            // =========================
            double custoTotal = custo + maoDeObra + deslocamento + pedagio + hospedagem + alimentacao + pta;

            // =========================
            // VENDA
            // =========================
            double venda = custoTotal * (1 + margem / 100);

            // =========================
            // IMPOSTO
            // =========================
            double impostoValor = venda * (imposto / 100);

            double creditoPercentual = imposto - IMPOSTO_FIXO;
            double creditoMaterial = custo * (creditoPercentual / 100);

            double impostoReal = impostoValor - creditoMaterial;

            double vendaLiquida = venda - impostoReal;

            // =========================
            // FORMATAÇÃO
            // =========================
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

            lblVenda.setText("R$ " + df.format(vendaLiquida));

            // =========================
            // RESUMO DINÂMICO
            // =========================
            StringBuilder resumo = new StringBuilder();

            resumo.append("Material: R$ ").append(df.format(custo)).append("\n");
            resumo.append("Mão de obra: R$ ").append(df.format(maoDeObra)).append("\n");
            resumo.append("KM: R$ ").append(df.format(deslocamento)).append("\n");
            resumo.append("Pedágio: R$ ").append(df.format(pedagio)).append("\n");

            if (hospedagem > 0) {
                resumo.append("Hospedagem: R$ ").append(df.format(hospedagem)).append("\n");
                resumo.append("Alimentação: R$ ").append(df.format(alimentacao)).append("\n");
            }

            if (pta > 0) {
                resumo.append("PTA: R$ ").append(df.format(pta)).append("\n");
            }

            resumo.append("\nVenda Bruta: R$ ").append(df.format(venda)).append("\n");
            resumo.append("Imposto: R$ ").append(df.format(impostoValor)).append("\n");
            resumo.append("Crédito: R$ ").append(df.format(creditoMaterial)).append("\n");
            resumo.append("Imposto Real: R$ ").append(df.format(impostoReal));

            lblResumo.setText(resumo.toString());

        } catch (Exception e) {
            lblVenda.setText("Erro");
        }
    }

    @FXML
    private void confirmarVenda() {
        System.out.println("Gerar relatório (em breve)");
    }
}