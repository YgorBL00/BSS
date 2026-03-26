package app.model;

import java.time.LocalDateTime;

public class Projeto {

    private int id;
    private String cliente;
    private String dimensoes;
    private double valorVenda;
    private LocalDateTime data;

    public Projeto(int id, String cliente, String dimensoes, double valorVenda, LocalDateTime data) {
        this.id = id;
        this.cliente = cliente;
        this.dimensoes = dimensoes;
        this.valorVenda = valorVenda;
        this.data = data;
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public String getDimensoes() { return dimensoes; }
    public double getValorVenda() { return valorVenda; }
    public LocalDateTime getData() { return data; }

}