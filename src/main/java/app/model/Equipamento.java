package app.model;

public class Equipamento {
    private String modelo;
    private String gas;
    private double carga;
    private double tanqueLiquido;
    private String linhaLiquido;
    private String linhaSucção;

    public Equipamento(String modelo, String gas, double carga,
                       double tanqueLiquido,
                       String linhaLiquido,
                       String linhaSucção) {

        this.modelo = modelo;
        this.gas = gas;
        this.carga = carga;
        this.tanqueLiquido = tanqueLiquido;
        this.linhaLiquido = linhaLiquido;
        this.linhaSucção = linhaSucção;
    }

    public String getModelo() { return modelo; }
    public String getGas() { return gas; }
    public double getCarga() { return carga; }
    public double getTanqueLiquido() { return tanqueLiquido; }
    public String getLinhaLiquido() { return linhaLiquido; }
    public String getLinhaSucção() { return linhaSucção; }
}
