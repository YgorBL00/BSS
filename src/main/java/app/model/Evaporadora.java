package app.model;

public class Evaporadora {

    private int id;
    private String codigo;
    private int ventiladores;
    private double vazao;
    private double capacidade;
    private double valor;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public int getVentiladores() { return ventiladores; }
    public void setVentiladores(int ventiladores) { this.ventiladores = ventiladores; }

    public double getVazao() { return vazao; }
    public void setVazao(double vazao) { this.vazao = vazao; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}