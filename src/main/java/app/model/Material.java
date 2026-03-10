package app.model;

import javafx.beans.property.*;

public class Material {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();
    private StringProperty unidade = new SimpleStringProperty();
    private StringProperty classe = new SimpleStringProperty();
    private StringProperty codigo = new SimpleStringProperty();

    public Material() {}

    public Material(int id, String codigo, String nome, double valor, String unidade, String classe) {
        this.id.set(id);
        this.codigo.set(codigo);
        this.nome.set(nome);
        this.valor.set(valor);
        this.unidade.set(unidade);
        this.classe.set(classe);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public double getValor() { return valor.get(); }
    public void setValor(double valor) { this.valor.set(valor); }
    public DoubleProperty valorProperty() { return valor; }

    public String getUnidade() { return unidade.get(); }
    public void setUnidade(String unidade) { this.unidade.set(unidade); }
    public StringProperty unidadeProperty() { return unidade; }

    public String getClasse() { return classe.get(); }
    public void setClasse(String classe) { this.classe.set(classe); }
    public StringProperty classeProperty() { return classe; }

    public String getCodigo() {
        return codigo.get();
    }
    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }
    public StringProperty codigoProperty() {
        return codigo;
    }

    public class Espessura {

        private int id;
        private int valorMm;

        public Espessura(int id, int valorMm) {
            this.id = id;
            this.valorMm = valorMm;
        }

        public int getId() {
            return id;
        }

        public int getValorMm() {
            return valorMm;
        }

        @Override
        public String toString() {
            return valorMm + " mm";
        }
    }
}
