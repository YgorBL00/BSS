package app.model;

public class Capacidade {

    private Modelo modelo;
    private int temperaturaEvap;
    private int temperaturaAmbiente;
    private double capacidadeKcal;

    public Capacidade(Modelo modelo, int evap, int amb, double capacidade) {
        this.modelo = modelo;
        this.temperaturaEvap = evap;
        this.temperaturaAmbiente = amb;
        this.capacidadeKcal = capacidade;
    }

    public Modelo getModelo() { return modelo; }
    public int getTemperaturaEvap() { return temperaturaEvap; }
    public int getTemperaturaAmbiente() { return temperaturaAmbiente; }
    public double getCapacidadeKcal() { return capacidadeKcal; }
}