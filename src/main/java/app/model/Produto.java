package app.model;

public class Produto {

    private Long id;
    private String codigo;
    private String nome;
    private String categoria;

    public Produto() {}

    public Produto(Long id, String codigo, String nome, String categoria) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}