package app.service;

import app.model.Material;
import app.model.Produto;
import app.service.MaterialService;
import app.service.ProdutoService;

import java.util.List;

public class CacheSistema {

    private static List<Material> materiais;
    private static List<Produto> produtos;

    public static void carregar() {

        System.out.println("Carregando dados do banco...");

        MaterialService materialService = new MaterialService();
        ProdutoService produtoService = new ProdutoService();

        materiais = materialService.buscarTodos();
        produtos = produtoService.buscarTodos();

        System.out.println("Materiais carregados: " + materiais.size());
        System.out.println("Produtos carregados: " + produtos.size());
    }

    public static List<Material> getMateriais() {
        return materiais;
    }

    public static List<Produto> getProdutos() {
        return produtos;
    }
}