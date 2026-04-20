package app.model;

public class Eletrica {

    private String modeloUC;
    private String tensao; // NOVO

    public Eletrica(String modeloUC, String tensao) {
        this.modeloUC = modeloUC;
        this.tensao = tensao;
    }

    // =========================
    // IDENTIFICAÇÃO
    // =========================

    public boolean isTrifasico() {
        return "220V_TRI".equals(tensao) || "380V_TRI".equals(tensao);
    }

    public boolean is380V() {
        return "380V_TRI".equals(tensao);
    }

    // =========================
    // BITOLA DO COMPRESSOR
    // =========================
    public double getBitolaCompressor() {

        double base;

        switch (modeloUC) {

            case "OP-HJM(Z)019":
            case "OP-HJM(Z)022":
            case "OP-HJM(Z)028":
            case "OP-HJM(Z)032":
            case "OP-HJM(Z)036":
            case "OP-HJM(Z)040":
            case "OP-HJM(Z)044":
            case "OP-HJM(Z)050":
            case "OP-HJM(Z)056":
                base = 4;
                break;

            case "OP-HJM(Z)064":
                base = 6;
                break;

            case "OP-HGM(Z)072":
            case "OP-HGM(Z)080":
            case "OP-HGM(Z)100":
                base = 10;
                break;

            case "OP-HGM(Z)125":
                base = 16;
                break;

            case "OP-HGM(Z)144":
            case "OP-HGM(Z)160":
                base = 25;
                break;

            default:
                base = 4;
        }

        // 🔥 AJUSTE POR TENSÃO
        if (is380V()) {
            return reduzirBitola(base);
        }

        return base;
    }

    // =========================
    // REDUÇÃO PARA 380V
    // =========================
    private double reduzirBitola(double bitola) {

        // regra prática (corrente menor em 380V)
        if (bitola == 25) return 16;
        if (bitola == 16) return 10;
        if (bitola == 10) return 6;
        if (bitola == 6) return 4;

        return bitola; // 4 e menores mantém
    }

    // =========================
    // PERNAS DO COMPRESSOR
    // =========================
    public int getPernasCompressor() {

        if (isTrifasico()) {
            return 4; // 3F + terra
        } else {
            return 3; // fase + neutro + terra
        }
    }

    // =========================
    // CABOS FIXOS
    // =========================

    public double getBitolaPressostato() {
        return 1.5;
    }

    public int getPernasPressostato() {
        return 2;
    }

    public double getBitolaSensor() {
        return 0.75;
    }

    public int getPernasSensor() {
        return 2;
    }

    public double getBitolaMotoventilador() {
        return 2.5;
    }

    public int getPernasMotoventilador() {
        return 2;
    }

    public double getBitolaSolenoide() {
        return 1.5;
    }

    public int getPernasSolenoide() {
        return 2;
    }
}