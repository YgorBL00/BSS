package app.model;

public class Modelo {

    private int id;
    private String modelo;
    private String linha;
    private String fluido;
    private int ventiladores;

    public Modelo(int id, String modelo, String linha, String fluido, int ventiladores) {
        this.id = id;
        this.modelo = modelo;
        this.linha = linha;
        this.fluido = fluido;
        this.ventiladores = ventiladores;
    }

    public String getModelo() {
        return modelo;
    }
}
