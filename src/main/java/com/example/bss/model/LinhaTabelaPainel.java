package com.example.bss.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LinhaTabelaPainel {
    private final StringProperty local;
    private final StringProperty qtd;
    private final StringProperty altura;
    private final StringProperty largura;
    private final StringProperty area;

    public LinhaTabelaPainel(String local, String qtd, String altura, String largura, String area) {
        this.local = new SimpleStringProperty(local);
        this.qtd = new SimpleStringProperty(qtd);
        this.altura = new SimpleStringProperty(altura);
        this.largura = new SimpleStringProperty(largura);
        this.area = new SimpleStringProperty(area);
    }

    public StringProperty localProperty() { return local; }
    public StringProperty qtdProperty() { return qtd; }
    public StringProperty alturaProperty() { return altura; }
    public StringProperty larguraProperty() { return largura; }
    public StringProperty areaProperty() { return area; }
}