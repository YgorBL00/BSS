package app.controller.vendedor;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class OrcamentoController {

    @FXML private Label lblCliente;
    @FXML private Label lblCusto;
    @FXML private Label lblVenda;
    @FXML private Label lblResumo; // 🔥 opcional (se adicionar no FXML)

    @FXML private TextField txtMargem;
    @FXML private ComboBox<Integer> cbDias;
    @FXML private ComboBox<Integer> cbPessoas;
    @FXML private TextField txtKm;
    @FXML private TextField txtImposto;

    @FXML private CheckBox chkHospedagem;
    @FXML private CheckBox chkAlimentacao;
    @FXML private CheckBox chkAndaime;
    @FXML private CheckBox chkPTA;

    private double custo;
    private ResultadoUsuario resultadoController;

    @FXML
    public void initialize() {

        for (int i = 1; i <= 30; i++) {
            cbDias.getItems().add(i);
        }

        for (int i = 1; i <= 6; i++) {
            cbPessoas.getItems().add(i);
        }

        cbDias.setValue(1);
        cbPessoas.setValue(1);
    }

    public void setDados(String cliente, String custoTexto, ResultadoUsuario controller) {

        this.resultadoController = controller;

        lblCliente.setText(cliente);
        lblCusto.setText(custoTexto);

        this.custo = Double.parseDouble(
                custoTexto.replace("R$", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .trim()
        );
    }

    @FXML
    private void calcularVenda() {

        try {

            double margem = Double.parseDouble(txtMargem.getText());
            double imposto = Double.parseDouble(txtImposto.getText());

            int dias = cbDias.getValue();
            int pessoas = cbPessoas.getValue();
            double km = Double.parseDouble(txtKm.getText());

            double valorDia = 208;

            // =========================
            // MÃO DE OBRA
            // =========================
            double maoDeObra = dias * pessoas * valorDia;

            // =========================
            // DESLOCAMENTO
            // =========================
            double deslocamento = km;

            // =========================
            // EXTRAS
            // =========================
            double extras = 0;

            if (chkHospedagem.isSelected()) extras += dias * pessoas * 80;
            if (chkAlimentacao.isSelected()) extras += dias * pessoas * 40;
            if (chkAndaime.isSelected()) extras += 500;
            if (chkPTA.isSelected()) extras += 1200;

            // =========================
            // CUSTO TOTAL
            // =========================
            double custoTotal = custo + maoDeObra + deslocamento + extras;

            // =========================
            // VENDA
            // =========================
            double venda = custoTotal * (1 + margem / 100);

            // =========================
            // IMPOSTO
            // =========================
            double valorImposto = venda * (imposto / 100);

            // 🔥 valor final (líquido)
            double vendaLiquida = venda - valorImposto;

            // =========================
            // FORMATAÇÃO BR
            // =========================
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

            // 🔥 EXIBE VENDA
            lblVenda.setText("R$ " + df.format(vendaLiquida));

            // =========================
            // RESUMO (OPCIONAL)
            // =========================
            if (lblResumo != null) {

                String resumo =
                        "Material: R$ " + df.format(custo) + "\n" +
                                "Mão de obra: R$ " + df.format(maoDeObra) + "\n" +
                                "Deslocamento: R$ " + df.format(deslocamento) + "\n" +
                                "Extras: R$ " + df.format(extras) + "\n" +
                                "Imposto: R$ " + df.format(valorImposto);

                lblResumo.setText(resumo);
            }

        } catch (Exception e) {
            lblVenda.setText("Erro");
        }
    }

    @FXML
    private void confirmarVenda() {

        try {
            String vendaTexto = lblVenda.getText()
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            // aqui você pode futuramente salvar ou mandar pro ResultadoUsuario

            Stage stage = (Stage) lblVenda.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}