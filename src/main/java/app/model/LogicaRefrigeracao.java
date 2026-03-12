package app.model;

import java.util.List;

public class LogicaRefrigeracao {

    // =====================
    // Classe interna Produto (opcional se já tiver na cache)
    // =====================
    public static class Produto {
        private String nome;
        private String categoria;
        private double massa; // kg
        private double calorEspecifico; // kcal/kg°C
        private double tempEntrada; // °C

        public Produto(String nome, String categoria, double massa, double calorEspecifico, double tempEntrada) {
            this.nome = nome;
            this.categoria = categoria;
            this.massa = massa;
            this.calorEspecifico = calorEspecifico;
            this.tempEntrada = tempEntrada;
        }

        public double getCargaTermica(double tempInterna, double tempoProcessoHoras){
            // carga do produto (kcal/h)
            return (massa * calorEspecifico * (tempEntrada - tempInterna)) / tempoProcessoHoras;
        }
    }

    // =====================
    // Método principal de cálculo
    // =====================
    public static double calcularCargaTermica(
            double areaPainel,       // m²
            double volumeCamara,     // m³
            double tempInterna,      // °C
            double tempAmbiente,     // °C
            List<Produto> produtos,  // produtos dentro da câmara
            int espessuraPainel,     // mm
            double tempoProcessoHoras, // h
            int trocasAr,            // trocas/h por porta
            int numeroPessoas,       // dentro da câmara
            boolean iluminacao,      // considerar iluminação?
            double equipamentos      // kcal/h equipamentos
    ){

        double deltaT = tempAmbiente - tempInterna;

        // -----------------
        // 1️⃣ Transmissão paredes/piso/teto
        // -----------------
        double U;
        switch(espessuraPainel){
            case 50: U = 0.45; break;
            case 75: U = 0.30; break;
            case 100: U = 0.22; break;
            case 150: U = 0.15; break;
            default: U = 0.25;
        }
        double qTransmissao = U * areaPainel * deltaT;

        // -----------------
        // 2️⃣ Carga do produto
        // -----------------
        double qProduto = 0;
        if(produtos != null){
            for(Produto p : produtos){
                qProduto += p.getCargaTermica(tempInterna, tempoProcessoHoras);
            }
        }

        // -----------------
        // 3️⃣ Infiltração de ar (porta)
        // -----------------
        double qInfiltracao = 0.33 * volumeCamara * deltaT * trocasAr;

        // -----------------
        // 4️⃣ Pessoas
        // -----------------
        double qPessoas = numeroPessoas * 400; // kcal/h por pessoa

        // -----------------
        // 5️⃣ Iluminação
        // -----------------
        double qIluminacao = iluminacao ? areaPainel * 10 : 0;

        // -----------------
        // 6️⃣ Equipamentos
        // -----------------
        double qEquipamentos = equipamentos;

        // -----------------
        // Carga térmica total
        // -----------------
        double qTotal = qTransmissao + qProduto + qInfiltracao + qPessoas + qIluminacao + qEquipamentos;

        // -----------------
        // Fator de segurança estilo Danfoss
        // -----------------
        double fatorSeguranca = 1.1;
        return qTotal * fatorSeguranca;
    }

    // ==================
    // Conversões úteis
    // ==================
    public static double kcalhToKW(double kcalh){
        return kcalh / 860.0;
    }

    public static double kcalhToTR(double kcalh){
        return kcalh / 3024.0;
    }
}