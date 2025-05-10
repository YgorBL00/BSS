package com.example.bss.model;

import com.example.bss.model.DadosCâmara;
import com.example.bss.model.Material;
import com.example.bss.model.MateriaisBase;

import java.util.List;

public class CalculadoraMateriais {

    public static List<Material> calcularMateriais() {
        double comprimento = DadosCâmara.getComprimento();
        double largura = DadosCâmara.getLargura();
        double altura = DadosCâmara.getAltura();

        double areaParedes = 2 * altura * (comprimento + largura);
        double areaTetoPiso = 2 * (comprimento * largura);
        double volume = comprimento * largura * altura;
        double perimetro = 2 * (comprimento + largura);

        List<Material> materiais = MateriaisBase.getMateriaisDisponiveis();

        for (Material m : materiais) {
            switch (m.getNome()) {
                case "Painel Isotérmico":
                    m.setQuantidade(areaParedes + areaTetoPiso);
                    break;
                case "Cantoneira de PVC":
                    m.setQuantidade(perimetro); // exemplo: 1 volta completa
                    break;
                case "Silicone PU":
                    m.setQuantidade((areaParedes + areaTetoPiso) / 4); // 1 tubo a cada 4 m², por exemplo
                    break;
                case "Parafuso Autobrocante":
                    m.setQuantidade((areaParedes + areaTetoPiso) * 10); // 10 parafusos por m²
                    break;
                case "Motor Condensador":
                    m.setQuantidade(1); // já definido no DadosCâmara, mas fixo aqui como 1
                    break;
                case "Evaporadora":
                    m.setQuantidade(1); // idem
                    break;
                case "Perfil H":
                    m.setQuantidade(perimetro); // exemplo
                    break;
                case "Chapa Alumínio Piso":
                    m.setQuantidade(comprimento * largura);
                    break;
                case "Dreno PVC":
                    m.setQuantidade(largura); // exemplo: um dreno na parede do fundo
                    break;
                default:
                    m.setQuantidade(0);
            }
        }

        return materiais;
    }
}