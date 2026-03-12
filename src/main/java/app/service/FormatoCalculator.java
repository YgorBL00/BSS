package app.service;

import app.model.Porta;
import java.util.ArrayList;
import java.util.List;

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
        public void calcularAreas() {
            // paredes
            areaParedesM2 = 2*(comprimento*alturaParedeReal) + 2*(largura*alturaParedeReal);

            // teto
            areaTetoM2 = comprimento * largura;

            // piso
            areaPisoM2 = requerPiso ? (comprimento * largura) : 0;

            // desperdício
            desperdicioM2 = 0; // se não precisar calcular recortes separados, ou calcular apenas recortes reais

            // aproveitamento
            double areaBruta = areaParedesM2 + areaTetoM2 + areaPisoM2;
            aproveitamento = 100.0; // agora é 100% pois estamos calculando direto por m²
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

        double C = C_ext - 2 * E;
        double L = L_ext - 2 * E;
        double alturaParede = A_ext - E - (temPiso ? E : 0);
        r.alturaParedeReal = alturaParede;
        r.alturaTetoReal = L_ext;
        r.alturaPisoReal = L;

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
        int inteirosTeto = (int) (C_ext / LARGURA_PAINEL);
        double recorteTeto = C_ext - (inteirosTeto * LARGURA_PAINEL);
        r.paineisTeto = inteirosTeto;
        if (recorteTeto > 0) {
            r.paineisTeto++;
            r.recortesTeto.add(new Recorte(recorteTeto, L_ext));
        }

        // =============================
        // PISO
        // =============================
        if (temPiso) {
            r.requerPiso = true;
            int inteirosPiso = (int) (C / LARGURA_PAINEL);
            double recortePiso = C - (inteirosPiso * LARGURA_PAINEL);
            r.paineisPiso = inteirosPiso;
            if (recortePiso > 0) {
                r.paineisPiso++;
                r.recortesPiso.add(new Recorte(recortePiso, L));
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
        if (!temPiso) {
            double totalPerfil = 2 * C_ext + 2 * L_ext + (A_ext + L_ext);
            r.perfilU = (int) Math.ceil(totalPerfil / 3.0);
        }

        if (portas != null) {
            for (Porta p : portas) {
                r.perfilU += calcularPerfilPorta(p);
            }
        }

        // =============================
        // FIXAÇÃO
        // =============================
        int totalAcabamentos = r.cantoneiraInterna + r.cantoneiraExterna + r.perfilU;
        r.rebites = totalAcabamentos * 14;
        r.parafusos = r.perfilU * 3;

        // =============================
        // CALCULA ÁREAS E APROVEITAMENTO
        // =============================
        r.calcularAreas();

        return r;
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