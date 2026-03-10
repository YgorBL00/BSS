package app.model;

public class Porta {

    private String tipo;
    private String batente;
    private String abertura;
    private String tamanho;

    public Porta(String tipo, String batente, String abertura, String tamanho) {
        this.tipo = tipo;
        this.batente = batente;
        this.abertura = abertura;
        this.tamanho = tamanho;
    }

    public String getTipo() { return tipo; }
    public String getBatente() { return batente; }
    public String getAbertura() { return abertura; }
    public String getTamanho() { return tamanho; }

    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setBatente(String batente) { this.batente = batente; }
    public void setAbertura(String abertura) { this.abertura = abertura; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
}