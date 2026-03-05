package app.model;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senhaHash;
    private String cargo;

    // Construtor vazio
    public Usuario() {
    }

    // Construtor com campos (opcional)
    public Usuario(String nome, String email, String senhaHash, String cargo) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.cargo = cargo;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // Opcional: para debug
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
