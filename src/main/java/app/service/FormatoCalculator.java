package app.service;

import app.model.Porta;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FormatoCalculator {

    public static final double LARGURA_PAINEL = 1.15;
    private static boolean congelado;

    // =============================
    // CLASSES INTERNAS
    // =============================

    public static class Recorte {
        public double largura;
        public double altura;

        public Recorte(double largura, double altura) {
            this.largura = largura;
            this.altura = altura;
        }
    }

    public static class ResultadoFormato {

        // =============================
        // PAINÉIS E RECORTES
        // =============================
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


        // =============================
        // ÁREAS
        // =============================
        public double areaParedesM2;
        public double areaTetoM2;
        public double areaPisoM2;

        // =============================
        // PU
        // =============================
        public double metrosJuntaPU;
        public int sachePU;
        public double rendimentoPU = 10.0;
        public double metrosEspumaExpansiva;

        // =============================
        // CANTONEIRAS E PERFIL
        // =============================
        public int cantoneiraInterna;
        public int cantoneiraExterna;
        public int perfilU;

        // =============================
        // FIXAÇÃO
        // =============================
        public int rebites;
        public int parafusos;

        // =============================
        // FRAMES EXPOSITOR
        // =============================
        public List<Integer> framesExpositor = new ArrayList<>();

        // =============================
        // DIMENSÕES GERAIS
        // =============================
        private double comprimento;
        private double largura;
        private double altura;

        public void setDimensoes(double C, double L, double A) {
            this.comprimento = C;
            this.largura = L;
            this.altura = A;
        }

        public double getAreaTotal() {
            return areaParedesM2 + areaTetoM2 + areaPisoM2;
        }

        public double getVolumeTotal() {
            return comprimento * largura * altura;
        }

        // =============================
        // CALCULA ÁREAS E APROVEITAMENTO
        // =============================
        public void calcularAreas(boolean requerPiso) {

            // paredes
            areaParedesM2 = 2*(comprimento*alturaParedeReal) + 2*(largura*alturaParedeReal);

            // teto
            areaTetoM2 = comprimento * largura;

            // piso (agora correto)
            areaPisoM2 = requerPiso ? (comprimento * largura) : 0;

            // desperdício
            desperdicioM2 = 0;

            // aproveitamento
            double areaBruta = areaParedesM2 + areaTetoM2 + areaPisoM2;
            aproveitamento = 100.0;
        }
    }

    // =============================
    // MÉTODO CALCULAR PRINCIPAL
    // =============================
    public static ResultadoFormato calcular(
            double C_ext,
            double L_ext,
            double A_ext,
            double E,
            boolean temPiso,
            List<Porta> portas,
            boolean congelado
    ) {
        FormatoCalculator.congelado = congelado;

        ResultadoFormato r = new ResultadoFormato();
        r.setDimensoes(C_ext, L_ext, A_ext);
        r.requerPiso = temPiso;


        double C = C_ext;
        double L = L_ext - 2 * E;

        double C_interno = C_ext - 2 * E;
        double L_interno = L_ext - 2 * E;

        double alturaParede = A_ext - E - (temPiso ? E : 0);

        r.alturaParedeReal = alturaParede;
        r.alturaTetoReal = L_interno;
        r.alturaPisoReal = L_interno;

        // =============================
        // PAREDES
        // =============================

        double[] lados = {C, C, L, L};
        for (double lado : lados) {
            int inteiros = (int) (lado / LARGURA_PAINEL);
            double recorte = lado - (inteiros * LARGURA_PAINEL);
            if (recorte < 0.0001) recorte = 0;

            r.paineisParede += inteiros;
            if (recorte > 0) {
                r.paineisParede++;
                r.recortesParede.add(new Recorte(recorte, alturaParede));
            }
        }

        // =============================
        // TETO
        // =============================

        double ladoTeto = C_ext;

        int inteirosTeto = (int) (ladoTeto / LARGURA_PAINEL);
        double recorteTeto = ladoTeto - (inteirosTeto * LARGURA_PAINEL);

        r.paineisTeto += inteirosTeto;

        if (recorteTeto > 0.001) {
            r.paineisTeto++;
            r.recortesTeto.add(new Recorte(recorteTeto, L_ext));
        }

        // =============================
        // PISO
        // =============================

        if (temPiso) {

            double ladoPiso = C_ext;

            int inteirosPiso = (int) (ladoPiso / LARGURA_PAINEL);
            double recortePiso = ladoPiso - (inteirosPiso * LARGURA_PAINEL);

            r.paineisPiso += inteirosPiso;

            if (recortePiso > 0.001) {
                r.paineisPiso++;
                r.recortesPiso.add(new Recorte(recortePiso, L_ext));
            }
        }

        // =============================
        // TOTAL PAINÉIS
        // =============================
        r.totalPaineis = r.paineisParede + r.paineisTeto + r.paineisPiso;

        // =============================
        // PU
        // =============================
        double perimetro = 2 * C + 2 * L;
        int juntasVerticais = r.paineisParede - 4;
        double metrosVerticais = juntasVerticais * alturaParede;
        double juntaTeto = perimetro;
        double juntaPiso = temPiso ? perimetro : 0;

        r.metrosJuntaPU = metrosVerticais + juntaTeto + juntaPiso;
        r.sachePU = (int) Math.ceil(r.metrosJuntaPU / r.rendimentoPU) + 1;

        if(portas != null && portas.size() > 0) {
            // exemplo: tipo congelado — aqui você pode passar uma flag ou verificar pelo tipo
            boolean tipoCongelado = true; // você pode passar como parâmetro do método
            if(tipoCongelado){
                r.metrosEspumaExpansiva = metrosVerticais + juntaTeto + juntaPiso; // mesma lógica do PU
                // sem +1 e sem arredondamento por enquanto
            }
        }

        // =============================
        // CANTONEIRAS
        // =============================
        double colunasInternas = 4 * alturaParede;
        double topoInterno = 2 * C + 2 * L;
        double baseInterna = temPiso ? (2 * C + 2 * L) : 0;
        r.cantoneiraInterna = (int) Math.ceil((colunasInternas + topoInterno + baseInterna) / 3.0);

        double colunasExternas = 4 * A_ext;
        double topoExterno = 2 * C_ext + 2 * L_ext;
        double baseExterna = temPiso ? (2 * C_ext + 2 * L_ext) : 0;
        r.cantoneiraExterna = (int) Math.ceil((colunasExternas + topoExterno + baseExterna) / 3.0);

        // =============================
        // PERFIL U
        // =============================
        double totalPerfil = 0;

        // Perfil base da câmara
        if (temPiso) {
            // com piso → normalmente não usa base, só partes superiores/laterais
            totalPerfil = (A_ext + L_ext);
        } else {
            // sem piso → precisa de base completa
            totalPerfil = 2 * C_ext + 2 * L_ext + (A_ext + L_ext);
        }

        // converte para barras de 3m
        r.perfilU = (int) Math.ceil(totalPerfil / 3.0);

        // SEMPRE soma portas
        if (portas != null) {
            for (Porta p : portas) {
                r.perfilU += calcularPerfilPorta(p);
            }
        }

        r.perfilU += 1;
        // =============================
        // FIXAÇÃO
        // =============================
        int totalAcabamentos = r.cantoneiraInterna + r.cantoneiraExterna + r.perfilU;
        r.rebites = totalAcabamentos * 14;
        r.parafusos = r.perfilU * 3;

        // =============================
        // CALCULA ÁREAS E APROVEITAMENTO
        // =============================
        r.calcularAreas(r.requerPiso);

        return r;
    }

    public static String gerarMemorial(
            double C,
            double L,
            double A,
            int espessura,
            ResultadoFormato r
    ) {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%.2f x %.2f x %.2f\n", C, L, A));
        sb.append("\n");

        // =========================
        // PAREDES
        // =========================
        Map<String, Integer> paineisParede = new LinkedHashMap<>();

        // painéis inteiros
        String chaveInteiro = String.format("1,15x%.2f", r.alturaParedeReal);
        paineisParede.put(chaveInteiro, r.paineisParede - r.recortesParede.size());

        // recortes
        for (Recorte rec : r.recortesParede) {

            String chave = String.format("%.2fx%.2f", rec.largura, rec.altura);

            paineisParede.merge(chave, 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> e : paineisParede.entrySet()) {

            int qtd = e.getValue();

            sb.append(qtd)
                    .append(qtd > 1 ? " Painéis PIR " : " Painel PIR ")
                    .append(espessura)
                    .append("mm ")
                    .append(e.getKey())
                    .append(" - Paredes\n");
        }

        // =========================
        // TETO
        // =========================
        Map<String, Integer> paineisTeto = new LinkedHashMap<>();

        int inteirosTeto = r.paineisTeto - r.recortesTeto.size();

        if (inteirosTeto > 0) {

            String chave = String.format("1,15x%.2f", r.alturaTetoReal);
            paineisTeto.put(chave, inteirosTeto);
        }

        for (Recorte rec : r.recortesTeto) {

            String chave = String.format("%.2fx%.2f", rec.largura, rec.altura);

            paineisTeto.merge(chave, 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> e : paineisTeto.entrySet()) {

            int qtd = e.getValue();

            sb.append(qtd)
                    .append(qtd > 1 ? " Painéis PIR " : " Painel PIR ")
                    .append(espessura)
                    .append("mm ")
                    .append(e.getKey())
                    .append(" - Teto\n");
        }

        // =========================
        // PISO
        // =========================
        if (r.requerPiso) {

            Map<String, Integer> paineisPiso = new LinkedHashMap<>();

            int inteirosPiso = r.paineisPiso - r.recortesPiso.size();

            if (inteirosPiso > 0) {

                String chave = String.format("1,15x%.2f", r.alturaPisoReal);
                paineisPiso.put(chave, inteirosPiso);
            }

            for (Recorte rec : r.recortesPiso) {

                String chave = String.format("%.2fx%.2f", rec.largura, rec.altura);

                paineisPiso.merge(chave, 1, Integer::sum);
            }

            for (Map.Entry<String, Integer> e : paineisPiso.entrySet()) {

                int qtd = e.getValue();

                sb.append(qtd)
                        .append(qtd > 1 ? " Painéis PIR " : " Painel PIR ")
                        .append(espessura)
                        .append("mm ")
                        .append(e.getKey())
                        .append(" - Piso\n");
            }
        }

        return sb.toString();
    }
    // =============================
    // AUXILIAR: PERFIL PORTA
    // =============================
    private static int calcularPerfilPorta(Porta porta) {
        String[] partes = porta.getTamanho().trim().replace(",", ".").split("x");
        double largura = Double.parseDouble(partes[0]);
        double altura = Double.parseDouble(partes[1]);
        double metros = 2 * altura + largura;
        return (int) Math.ceil(metros / 3.0);
    }

}