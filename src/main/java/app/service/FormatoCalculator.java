package app.service;

import java.util.ArrayList;
import java.util.List;

public class FormatoCalculator {

    public static final double LARGURA_PAINEL = 1.15;

    // =============================
    // CLASSE RECORTE
    // =============================

    public static class Recorte {

        public double largura;
        public double altura;

        public Recorte(double largura, double altura) {
            this.largura = largura;
            this.altura = altura;
        }
    }

    // =============================
    // RESULTADO
    // =============================

    public static class ResultadoFormato {

        public int paineisParede;
        public int paineisTeto;
        public int paineisPiso;

        public List<Recorte> recortesParede = new ArrayList<>();
        public List<Recorte> recortesTeto = new ArrayList<>();
        public List<Recorte> recortesPiso = new ArrayList<>();

        public int totalPaineis;

        public double desperdicioM2;
        public double aproveitamento;

        public boolean requerPiso;

        public double alturaParedeReal;
        public double alturaTetoReal;
        public double alturaPisoReal;

        public double metrosJuntaPU;
        public int sachePU;
        public double rendimentoPU = 10.0;// metros por sache

        public int cantoneiraInterna;
        public int cantoneiraExterna;
        public int perfilU;

        public int rebites;
        public int parafusos;
    }

    // =============================
    // MÉTODO PRINCIPAL
    // =============================

    public static ResultadoFormato calcular(
            double C_ext,
            double L_ext,
            double A_ext,
            double E,
            boolean temPiso
    ) {

        ResultadoFormato r = new ResultadoFormato();

        // =============================
        // SEMPRE POR DENTRO
        // =============================

        double C = C_ext - (2 * E);
        double L = L_ext - (2 * E);

        // =============================
        // ALTURA REAL DA PAREDE
        // =============================

        double alturaParede = A_ext - E - (temPiso ? E : 0);
        r.alturaParedeReal = alturaParede;

        // =============================
        // PAREDES
        // =============================

        double[] lados = {C, C, L, L};

        for (double lado : lados) {

            int inteiros = (int) (lado / LARGURA_PAINEL);
            double sobra = lado % LARGURA_PAINEL;

            r.paineisParede += inteiros;

            if (sobra > 0) {

                r.paineisParede++;

                r.recortesParede.add(
                        new Recorte(sobra, alturaParede)
                );

                double larguraDesperdicio = LARGURA_PAINEL - sobra;
                r.desperdicioM2 += larguraDesperdicio * alturaParede;
            }
        }

        // =============================
        // TETO
        // =============================

        r.alturaTetoReal = L_ext;

        int inteirosTeto = (int) (C_ext / LARGURA_PAINEL);
        double sobraTeto = C_ext % LARGURA_PAINEL;

        r.paineisTeto = inteirosTeto;

        if (sobraTeto > 0) {

            r.paineisTeto++;

            r.recortesTeto.add(
                    new Recorte(sobraTeto, L_ext)
            );

            double larguraDesperdicio = LARGURA_PAINEL - sobraTeto;
            r.desperdicioM2 += larguraDesperdicio * L;
        }

        // =============================
        // PISO
        // =============================

        if (temPiso) {

            r.requerPiso = true;
            r.alturaPisoReal = L;

            int inteirosPiso = (int) (C / LARGURA_PAINEL);
            double sobraPiso = C % LARGURA_PAINEL;

            r.paineisPiso = inteirosPiso;

            if (sobraPiso > 0) {

                r.paineisPiso++;

                r.recortesPiso.add(
                        new Recorte(sobraPiso, L)
                );

                double larguraDesperdicio = LARGURA_PAINEL - sobraPiso;
                r.desperdicioM2 += larguraDesperdicio * L;
            }
        }

        // =============================
        // TOTAL
        // =============================

        r.totalPaineis =
                r.paineisParede
                        + r.paineisTeto
                        + r.paineisPiso;

        double areaComprada =
                r.totalPaineis * LARGURA_PAINEL * alturaParede;

        double areaUtil = areaComprada - r.desperdicioM2;

        r.aproveitamento =
                areaComprada == 0
                        ? 0
                        : (areaUtil / areaComprada) * 100;

        // =============================
        // CALCULO DO PU
        // =============================

        // perimetro interno
        double perimetro = (2 * C) + (2 * L);

        // juntas verticais entre paineis
        int juntasVerticais = r.paineisParede - 4;
        double metrosVerticais = juntasVerticais * alturaParede;

        // junção parede teto
        double juntaTeto = perimetro;

        // junção parede piso
        double juntaPiso = temPiso ? perimetro : 0;

        r.metrosJuntaPU =
                metrosVerticais
                        + juntaTeto
                        + juntaPiso;

        // quantidade de sachês
        r.sachePU = (int) Math.ceil(r.metrosJuntaPU / r.rendimentoPU) + 1;

        double colunasInternas = 4 * alturaParede;

        double topoInterno = (2 * C) + (2 * L);

        double baseInterna = temPiso ? ((2 * C) + (2 * L)) : 0;

        double totalCantInterna = colunasInternas + topoInterno + baseInterna;

        r.cantoneiraInterna = (int) Math.ceil(totalCantInterna / 3.0);

        double colunasExternas = 4 * A_ext;

        double topoExterno = (2 * C_ext) + (2 * L_ext);

        double baseExterna = temPiso ? ((2 * C_ext) + (2 * L_ext)) : 0;

        double totalCantExterna = colunasExternas + topoExterno + baseExterna;

        r.cantoneiraExterna = (int) Math.ceil(totalCantExterna / 3.0);

        if (!temPiso) {

            double totalPerfil =
                    (2 * C_ext)
                            + (2 * L_ext)
                            + (A_ext + L_ext);

            r.perfilU = (int) Math.ceil(totalPerfil / 3.0);
        }

        int totalAcabamentos =
                r.cantoneiraInterna
                        + r.cantoneiraExterna
                        + r.perfilU;

        r.rebites = totalAcabamentos * 14;

        r.parafusos = r.perfilU * 3;

        return r;
    }
}