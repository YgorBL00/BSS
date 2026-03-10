package app.service;

import java.util.ArrayList;
import java.util.List;
import app.model.Porta;

public class FormatoCalculator {

    public static final double LARGURA_PAINEL = 1.15;

    public static class Recorte {

        public double largura;
        public double altura;

        public Recorte(double largura, double altura) {
            this.largura = largura;
            this.altura = altura;
        }
    }

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
        public double rendimentoPU = 10.0;

        public int cantoneiraInterna;
        public int cantoneiraExterna;
        public int perfilU;

        public int rebites;
        public int parafusos;

        public List<Integer> framesExpositor = new ArrayList<>();
    }

    public static ResultadoFormato calcular(
            double C_ext,
            double L_ext,
            double A_ext,
            double E,
            boolean temPiso,
            List<Porta> portas
    ) {

        ResultadoFormato r = new ResultadoFormato();

        double C = C_ext - (2 * E);
        double L = L_ext - (2 * E);

        double alturaParede = A_ext - E - (temPiso ? E : 0);

        r.alturaParedeReal = alturaParede;

        // =============================
        // PAREDES
        // =============================

        double[] lados = {C, C, L, L};

        double areaUtilParede = 0;

        for (double lado : lados) {

            int inteiros = (int) (lado / LARGURA_PAINEL);

            double recorte = lado - (inteiros * LARGURA_PAINEL);

            if (recorte < 0.0001) recorte = 0;

            r.paineisParede += inteiros;

            if (recorte > 0) {

                r.paineisParede++;

                r.recortesParede.add(
                        new Recorte(recorte, alturaParede)
                );

                double desperdicio = LARGURA_PAINEL - recorte;

                r.desperdicioM2 += desperdicio * alturaParede;
            }

            areaUtilParede += lado * alturaParede;
        }

        // =============================
        // TETO
        // =============================

        r.alturaTetoReal = L_ext;

        int inteirosTeto = (int) (C_ext / LARGURA_PAINEL);

        double recorteTeto = C_ext - (inteirosTeto * LARGURA_PAINEL);

        r.paineisTeto = inteirosTeto;

        double areaUtilTeto = C_ext * L_ext;

        if (recorteTeto > 0) {

            r.paineisTeto++;

            r.recortesTeto.add(
                    new Recorte(recorteTeto, L_ext)
            );

            double desperdicio = LARGURA_PAINEL - recorteTeto;

            r.desperdicioM2 += desperdicio * L_ext;
        }

        // =============================
        // PISO
        // =============================

        double areaUtilPiso = 0;

        if (temPiso) {

            r.requerPiso = true;

            r.alturaPisoReal = L;

            int inteirosPiso = (int) (C / LARGURA_PAINEL);

            double recortePiso = C - (inteirosPiso * LARGURA_PAINEL);

            r.paineisPiso = inteirosPiso;

            areaUtilPiso = C * L;

            if (recortePiso > 0) {

                r.paineisPiso++;

                r.recortesPiso.add(
                        new Recorte(recortePiso, L)
                );

                double desperdicio = LARGURA_PAINEL - recortePiso;

                r.desperdicioM2 += desperdicio * L;
            }
        }

        // =============================
        // TOTAL
        // =============================

        r.totalPaineis =
                r.paineisParede +
                        r.paineisTeto +
                        r.paineisPiso;

        double areaCompradaParede =
                r.paineisParede * LARGURA_PAINEL * alturaParede;

        double areaCompradaTeto =
                r.paineisTeto * LARGURA_PAINEL * L_ext;

        double areaCompradaPiso =
                r.paineisPiso * LARGURA_PAINEL * L;

        double areaComprada =
                areaCompradaParede +
                        areaCompradaTeto +
                        areaCompradaPiso;

        double areaUtil =
                areaUtilParede +
                        areaUtilTeto +
                        areaUtilPiso;

        r.desperdicioM2 = areaComprada - areaUtil;

        if (areaComprada > 0) {
            r.aproveitamento = (areaUtil / areaComprada) * 100;
        }

        // =============================
        // PU
        // =============================

        double perimetro = (2 * C) + (2 * L);

        int juntasVerticais = r.paineisParede - 4;

        double metrosVerticais = juntasVerticais * alturaParede;

        double juntaTeto = perimetro;

        double juntaPiso = temPiso ? perimetro : 0;

        r.metrosJuntaPU =
                metrosVerticais +
                        juntaTeto +
                        juntaPiso;

        r.sachePU = (int) Math.ceil(r.metrosJuntaPU / r.rendimentoPU) + 1;

        // =============================
        // CANTONEIRAS
        // =============================

        double colunasInternas = 4 * alturaParede;

        double topoInterno = (2 * C) + (2 * L);

        double baseInterna = temPiso ? ((2 * C) + (2 * L)) : 0;

        double totalCantInterna =
                colunasInternas +
                        topoInterno +
                        baseInterna;

        r.cantoneiraInterna =
                (int) Math.ceil(totalCantInterna / 3.0);

        double colunasExternas = 4 * A_ext;

        double topoExterno = (2 * C_ext) + (2 * L_ext);

        double baseExterna = temPiso ? ((2 * C_ext) + (2 * L_ext)) : 0;

        double totalCantExterna =
                colunasExternas +
                        topoExterno +
                        baseExterna;

        r.cantoneiraExterna =
                (int) Math.ceil(totalCantExterna / 3.0);

        // =============================
        // PERFIL U
        // =============================

        if (!temPiso) {

            double totalPerfil =
                    (2 * C_ext) +
                            (2 * L_ext) +
                            (A_ext + L_ext);

            r.perfilU =
                    (int) Math.ceil(totalPerfil / 3.0);
        }

        // =============================
        // PORTAS
        // =============================

        if (portas != null) {

            for (Porta p : portas) {
                r.perfilU += calcularPerfilPorta(p);
            }
        }

        // =============================
        // FIXAÇÃO
        // =============================

        int totalAcabamentos =
                r.cantoneiraInterna +
                        r.cantoneiraExterna +
                        r.perfilU;

        r.rebites = totalAcabamentos * 14;

        r.parafusos = r.perfilU * 3;

        return r;
    }

    private static int calcularPerfilPorta(Porta porta) {

        String tamanho = porta.getTamanho();

        String[] partes = tamanho.split("x");

        double largura =
                Double.parseDouble(partes[0].trim().replace(",", "."));

        double altura =
                Double.parseDouble(partes[1].trim().replace(",", "."));

        double metros = (2 * altura) + largura;

        return (int) Math.ceil(metros / 3.0);
    }
}