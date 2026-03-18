package app.model;

public class ModeloResultado {

    private String codigo;
    private double capacidade;
    private String sucao;
    private String liquido;
    private double tanque;
    private String gas;

    public ModeloResultado(String codigo, double capacidade,
                           String sucao, String liquido,
                           double tanque, String gas) {
        this.codigo = codigo;
        this.capacidade = capacidade;
        this.sucao = sucao;
        this.liquido = liquido;
        this.tanque = tanque;
        this.gas = gas;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidade() { return capacidade; }
    public String getSucao() { return sucao; }
    public String getLiquido() { return liquido; }
    public double getTanque() { return tanque; }
    public String getGas() { return gas; }
}