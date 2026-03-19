package app.service;

import app.model.Capacidade;
import app.model.Material;
import app.model.Produto;

import java.util.List;

public class CacheSistema {

    private static List<Material> materiais;
    private static List<Produto> produtos;
    private static List<Capacidade> capacidades; // 👈 FALTAVA ISSO

    public static void carregar() {

        System.out.println("Carregando dados do banco...");

        MaterialService materialService = new MaterialService();
        ProdutoService produtoService = new ProdutoService();
        UnidadeService unidadeService = new UnidadeService(); // 👈 NOVO

        materiais = materialService.buscarTodos();
        produtos = produtoService.buscarTodos();
        capacidades = unidadeService.buscarTodas(); // 👈 CARREGA MÁQUINAS

        System.out.println("Materiais carregados: " + materiais.size());
        System.out.println("Produtos carregados: " + produtos.size());
        System.out.println("Capacidades carregadas: " + capacidades.size()); // 👈 DEBUG TOP
    }

    public static List<Material> getMateriais() {
        return materiais;
    }

    public static List<Produto> getProdutos() {
        return produtos;
    }

    public static List<Capacidade> getCapacidades(){
        return capacidades;
    }
}