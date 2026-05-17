package app.controller.vendedor;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.List;

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
    private List<ResultadoUsuario.ItemTabela> materiais;

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

    public void setMateriais(List<ResultadoUsuario.ItemTabela> materiais) {
        this.materiais = materiais;
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

        try {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Salvar Relatório de Venda");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF", "*.pdf")
            );

            String nomeCliente = lblCliente.getText()
                    .replace("Cliente:", "")
                    .trim();

            fileChooser.setInitialFileName(
                    "relatorio_venda_" + nomeCliente + ".pdf"
            );

            File arquivo = fileChooser.showSaveDialog(
                    lblVenda.getScene().getWindow()
            );

            if (arquivo == null) return;

            // =========================
            // CALCULOS
            // =========================

            int dias = cbDias.getValue();
            int pessoas = cbPessoas.getValue();

            double margem = parseBR(txtMargem.getText());
            double imposto = parseBR(txtImposto.getText());

            double km = txtKm.getText().isEmpty()
                    ? 0
                    : parseBR(txtKm.getText());

            double pedagio = txtPedagio.getText().isEmpty()
                    ? 0
                    : parseBR(txtPedagio.getText());

            double maoDeObra = dias * pessoas * VALOR_DIA;

            double deslocamento = km * VALOR_KM;

            double hospedagem = 0;
            double alimentacao = 0;

            if (chkHospedagem.isSelected()) {

                int diasHosp = txtDiasHospedagem.getText().isEmpty()
                        ? dias
                        : (int) parseBR(txtDiasHospedagem.getText());

                hospedagem = diasHosp * HOSPEDAGEM_DIA;

                alimentacao = diasHosp * pessoas * ALIMENTACAO_DIA;
            }

            double pta = 0;

            if (chkPTA.isSelected()) {
                pta = (PTA_MENSAL / 30.0) * dias;
            }

            double custoTotal = custo
                    + maoDeObra
                    + deslocamento
                    + pedagio
                    + hospedagem
                    + alimentacao
                    + pta;

            double venda = custoTotal * (1 + margem / 100);

            double impostoValor = venda * (imposto / 100);

            double creditoPercentual = imposto - IMPOSTO_FIXO;

            double creditoMaterial =
                    custo * (creditoPercentual / 100);

            double impostoReal =
                    impostoValor - creditoMaterial;

            double vendaLiquida =
                    venda - impostoReal;

            // =========================
            // FORMATACAO
            // =========================

            DecimalFormatSymbols symbols =
                    new DecimalFormatSymbols(new Locale("pt", "BR"));

            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');

            DecimalFormat df =
                    new DecimalFormat("#,##0.00", symbols);

            // =========================
            // PDF
            // =========================

            Document doc = new Document();

            PdfWriter.getInstance(
                    doc,
                    new FileOutputStream(arquivo)
            );

            doc.open();

            // TITULO
            doc.add(new Paragraph(
                    "RELATÓRIO FINANCEIRO / VENDA\n\n"
            ));

            // CLIENTE
            doc.add(new Paragraph(
                    "Cliente: " + nomeCliente
            ));

            doc.add(new Paragraph(
                    "Data: " + LocalDateTime.now()
            ));

            doc.add(new Paragraph("\n"));

            // TABELA
            PdfPTable tabela = new PdfPTable(2);

            tabela.setWidthPercentage(100);

            tabela.setWidths(new float[]{5, 2});

            tabela.addCell("Descrição");
            tabela.addCell("Valor");

            tabela.addCell("Material");
            tabela.addCell("R$ " + df.format(custo));

            tabela.addCell("Mão de obra");
            tabela.addCell("R$ " + df.format(maoDeObra));

            tabela.addCell("KM");
            tabela.addCell("R$ " + df.format(deslocamento));

            tabela.addCell("Pedágio");
            tabela.addCell("R$ " + df.format(pedagio));

            if (hospedagem > 0) {

                tabela.addCell("Hospedagem");
                tabela.addCell("R$ " + df.format(hospedagem));

                tabela.addCell("Alimentação");
                tabela.addCell("R$ " + df.format(alimentacao));
            }

            if (pta > 0) {

                tabela.addCell("PTA");
                tabela.addCell("R$ " + df.format(pta));
            }

            tabela.addCell("Venda Bruta");
            tabela.addCell("R$ " + df.format(venda));

            tabela.addCell("Imposto");
            tabela.addCell("R$ " + df.format(impostoValor));

            tabela.addCell("Crédito");
            tabela.addCell("R$ " + df.format(creditoMaterial));

            tabela.addCell("Imposto Real");
            tabela.addCell("R$ " + df.format(impostoReal));

            tabela.addCell("VENDA LÍQUIDA");
            tabela.addCell("R$ " + df.format(vendaLiquida));

            doc.add(tabela);

            // ======================================
            // NOVA PÁGINA
            // ======================================

            doc.newPage();

            doc.add(new Paragraph(
                    "MATERIAIS DETALHADOS\n\n"
            ));

            // ======================================
            // TABELA DE MATERIAIS
            // ======================================

            PdfPTable tabelaMateriais = new PdfPTable(6);

            tabelaMateriais.setWidthPercentage(100);

            tabelaMateriais.setWidths(
                    new float[]{1f, 5f, 1f, 1f, 2f, 2f}
            );

            // CABEÇALHO
            tabelaMateriais.addCell("Item");
            tabelaMateriais.addCell("Descrição");
            tabelaMateriais.addCell("Qtd");
            tabelaMateriais.addCell("Un");
            tabelaMateriais.addCell("Valor");
            tabelaMateriais.addCell("Total");

            // =========================
            // LINHAS
            // =========================

            int contador = 1;

            for (ResultadoUsuario.ItemTabela item : materiais) {

                tabelaMateriais.addCell(
                        String.valueOf(contador++)
                );

                tabelaMateriais.addCell(
                        item.getDescricao()
                );

                tabelaMateriais.addCell(
                        String.valueOf(item.getQuantidade())
                );

                tabelaMateriais.addCell(
                        item.getUnidade()
                );

                tabelaMateriais.addCell(
                        "R$ " + df.format(item.getValor())
                );

                tabelaMateriais.addCell(
                        "R$ " + df.format(item.getTotal())
                );
            }

            // =========================
            // TOTAL GERAL
            // =========================

            double totalMateriais = materiais.stream()
                    .mapToDouble(ResultadoUsuario.ItemTabela::getTotal)
                    .sum();

            tabelaMateriais.addCell("");
            tabelaMateriais.addCell("");
            tabelaMateriais.addCell("");
            tabelaMateriais.addCell("");

            tabelaMateriais.addCell("TOTAL");

            tabelaMateriais.addCell(
                    "R$ " + df.format(totalMateriais)
            );

            doc.add(tabelaMateriais);

            doc.close();

            Desktop.getDesktop().open(arquivo);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setHeaderText(null);

            alert.setContentText(
                    "Relatório gerado com sucesso!"
            );

            alert.showAndWait();

        } catch (Exception e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setContentText(
                    "Erro ao gerar relatório."
            );

            alert.showAndWait();
        }
    }
}