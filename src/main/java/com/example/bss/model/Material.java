package com.example.bss.model;

public class Material {

    private String nome;
    private String unidade;          // Ex: "m²", "m", "un"
    private double precoUnitario;   // Valor por unidade
    private double quantidade;      // Calculado dinamicamente

    public Material(String nome, double precoUnitario, String unidade) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.unidade = unidade;
        this.quantidade = 0.0;
    }

    // Cálculo da quantidade com fator de multiplicação (pode ser área, perímetro, etc.)
    public void calcularQuantidade(double base, double fator) {
        this.quantidade = base * fator;
    }

    public double getCustoTotal() {
        return this.quantidade * this.precoUnitario;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}