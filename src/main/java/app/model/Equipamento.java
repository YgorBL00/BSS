package app.model;

public class Equipamento {
    private String modelo;
    private String gas;
    private double carga;

    public Equipamento(String modelo, String gas, double carga) {
        this.modelo = modelo;
        this.gas = gas;
        this.carga = carga;
    }

    public String getModelo() { return modelo; }
    public String getGas() { return gas; }
    public double getCarga() { return carga; }
}
