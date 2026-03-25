package app.model;

public class Equipamento {
    private String modelo;
    private String gas;
    private double carga;
    private double tanqueLiquido;
    private String linhaLiquido;
    private String linhaSucção;
    private String acumuladorSuccao;
    private String separadorOleo;
    private double hp;
    private double valor;


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

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    public String getModelo() { return modelo; }
    public String getGas() { return gas; }
    public double getCarga() { return carga; }
    public double getTanqueLiquido() { return tanqueLiquido; }
    public String getLinhaLiquido() { return linhaLiquido; }
    public String getLinhaSucção() { return linhaSucção; }
    public String getAcumuladorSuccao() {return acumuladorSuccao;}
    public void setAcumuladorSuccao(String acumuladorSuccao) {this.acumuladorSuccao = acumuladorSuccao;}
    public String getSeparadorOleo() {return separadorOleo;}
    public void setSeparadorOleo(String separadorOleo) {this.separadorOleo = separadorOleo;}
}
