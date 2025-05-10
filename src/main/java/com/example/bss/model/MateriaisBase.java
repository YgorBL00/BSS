package com.example.bss.model;

import java.util.ArrayList;
import java.util.List;

public class MateriaisBase {

    private static final List<Material> materiaisDisponiveis = new ArrayList<>();

    static {
        // Adiciona todos os materiais padrão para câmara fria
        materiaisDisponiveis.add(new Material("Painel Isotérmico", 50.0, "m²"));
        materiaisDisponiveis.add(new Material("Cantoneira de PVC", 4.0, "m"));
        materiaisDisponiveis.add(new Material("Silicone PU", 20.0, "un"));
        materiaisDisponiveis.add(new Material("Parafuso Autobrocante", 0.5, "un"));
        materiaisDisponiveis.add(new Material("Motor Condensador", 1000.0, "un"));
        materiaisDisponiveis.add(new Material("Evaporadora", 800.0, "un"));
        materiaisDisponiveis.add(new Material("Perfil H", 6.0, "m"));
        materiaisDisponiveis.add(new Material("Chapa Alumínio Piso", 120.0, "m²"));
        materiaisDisponiveis.add(new Material("Dreno PVC", 3.5, "m"));
        // Adicione mais conforme necessário
    }

    // Retorna uma cópia da lista com novos objetos (para evitar alterar o original)
    public static List<Material> getMateriaisDisponiveis() {
        List<Material> copia = new ArrayList<>();
        for (Material m : materiaisDisponiveis) {
            copia.add(new Material(m.getNome(), m.getPrecoUnitario(), m.getUnidade()));
        }
        return copia;
    }
}