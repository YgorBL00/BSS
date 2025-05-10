package com.example.bss.model;

public class DadosCâmara {

    private static double temperaturaInterna;
    private static double cargaTermica;

    private static UnidadeCondensadoras motorRecomendado;
    private static Evaporadoras evaporadoraRecomendada;

    private static double comprimento = 0, largura = 0, altura = 0, espessura = 0;

    // Getters e setters para medidas
    public static double getComprimento() { return comprimento; }
    public static double getLargura() { return largura; }
    public static double getAltura() { return altura; }
    public static double getEspessura() { return espessura; }

    public static void setMedidas(double novoComprimento, double novaLargura, double novaAltura, double novaEspessura) {
        comprimento = novoComprimento;
        largura = novaLargura;
        altura = novaAltura;
        espessura = novaEspessura;
    }

    // Getters e setters para carga térmica e temperatura
    public static double getTemperaturaInterna() { return temperaturaInterna; }
    public static void setTemperaturaInterna(double temperatura) { temperaturaInterna = temperatura; }

    public static double getCargaTermica() { return cargaTermica; }
    public static void setCargaTermica(double carga) { cargaTermica = carga; }

    // Getters e setters para recomendações
    public static UnidadeCondensadoras getMotorRecomendado() { return motorRecomendado; }
    public static void setMotorRecomendado(UnidadeCondensadoras motor) { motorRecomendado = motor; }

    public static Evaporadoras getEvaporadoraRecomendada() { return evaporadoraRecomendada; }
    public static void setEvaporadoraRecomendada(Evaporadoras evap) { evaporadoraRecomendada = evap; }

    private static double custoObra = 0.0;

    public static void setCustoObra(double valor) { custoObra = valor; }
    public static double getCustoObra() { return custoObra; }
}