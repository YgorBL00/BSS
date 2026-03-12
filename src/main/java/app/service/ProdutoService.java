package app.service;

import app.database.DatabaseConnection;
import app.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    public List<Produto> buscarTodos() {

        List<Produto> lista = new ArrayList<>();

        String sql = "SELECT id, codigo, nome, categoria FROM produtos ORDER BY categoria, nome";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Produto p = new Produto();

                p.setId(rs.getLong("id"));
                p.setCodigo(rs.getString("codigo"));
                p.setNome(rs.getString("nome"));
                p.setCategoria(rs.getString("categoria"));

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Produto> buscarPorCategoria(String categoria) {

        List<Produto> lista = new ArrayList<>();

        String sql = "SELECT id, codigo, nome, categoria FROM produtos WHERE categoria = ? ORDER BY nome";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setId(rs.getLong("id"));
                produto.setCodigo(rs.getString("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCategoria(rs.getString("categoria"));

                lista.add(produto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // MAIN PARA TESTE
    public static void main(String[] args) {

        ProdutoService service = new ProdutoService();

        List<Produto> produtos = service.buscarTodos();

        System.out.println("===== LISTA DE PRODUTOS =====");

        for (Produto p : produtos) {

            System.out.println(
                    "ID: " + p.getId() +
                            " | Código: " + p.getCodigo() +
                            " | Nome: " + p.getNome() +
                            " | Categoria: " + p.getCategoria()
            );

        }

        System.out.println("Total de produtos: " + produtos.size());
    }
}