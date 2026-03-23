package app.model;

public class Eletrica {

    private String modeloUC;
    private boolean trifasico;

    public Eletrica(String modeloUC, boolean trifasico) {
        this.modeloUC = modeloUC;
        this.trifasico = trifasico;
    }

    // =========================
    // BITOLA DO COMPRESSOR
    // =========================
    public double getBitolaCompressor() {

        switch (modeloUC) {

            case "OP-HJM(Z)019":
            case "OP-HJM(Z)022":
            case "OP-HJM(Z)028":
            case "OP-HJM(Z)036":
            case "OP-HJM(Z)044":
            case "OP-HJM(Z)032":
            case "OP-HJM(Z)040":
            case "OP-HJM(Z)050":
            case "OP-HJM(Z)056":
                return 4;

            case "OP-HJM(Z)064":
                return 6;

            case "OP-HGM(Z)072":
            case "OP-HGM(Z)080":
            case "OP-HGM(Z)100":
                return 10;

            case "OP-HGM(Z)125":
                return 16;

            case "OP-HGM(Z)144":
            case "OP-HGM(Z)160":
                return 25;

            default:
                return 4;
        }
    }

    // =========================
    // PERNAS DO COMPRESSOR
    // =========================
    public int getPernasCompressor() {

        if (trifasico) {
            return 4; // 3 fases + terra
        } else {
            return 3; // 2 fases + terra
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