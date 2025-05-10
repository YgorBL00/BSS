package com.example.bss.model;

import java.util.ArrayList;
import java.util.List;

public class RecomendacaoItensComplementares {
    public static List<Item> recomendar() {
        List<Item> lista = new ArrayList<>();
        double comprimento = DadosCâmara.getComprimento();
        double largura = DadosCâmara.getLargura();
        double altura = DadosCâmara.getAltura();

        double perimetro = 2 * (comprimento + largura);

        lista.add(new Item("Motoventilador", "Universal", "un", 1, 450));
        lista.add(new Item("Resistência do Evaporador", "Inox 250W", "un", 1, 100));
        lista.add(new Item("Quadro de Comando", "Painel automático", "un", 1, 650));
        lista.add(new Item("Válvula de Expansão", "Danfoss", "un", 1, 330));
        lista.add(new Item("Orifício", "Ajustável", "un", 1, 80));
        lista.add(new Item("Válvula Solenoide", "Danfoss 1/4", "un", 1, 400));
        lista.add(new Item("Bobina da Válvula", "Danfoss 220V", "un", 1, 60));
        lista.add(new Item("Sifão", "PVC 1", "un", 1, 15));
        lista.add(new Item("Filtro Secador", "Danfoss 1/4", "un", 1, 50));
        lista.add(new Item("Visor de Líquido", "Clip-on", "un", 1, 35));
        lista.add(new Item("Metros de Cobre", "Tubo 3/8", "m", (int) Math.ceil(perimetro), 100));
        lista.add(new Item("Curva (tubo de cobre)", "3/8", "un", 2, 10));
        lista.add(new Item("Joelho (tubo de cobre)", "3/8", "un", 2, 10));
        lista.add(new Item("Metros de Isolamento", "Tubo elastomérico 3/8", "m", (int) Math.ceil(perimetro), 8));
        lista.add(new Item("Cantoneira Interna", "40x40mm", "un", (int) Math.ceil(perimetro/2), 30));
        lista.add(new Item("Cantoneira Externa", "40x40mm", "un", (int) Math.ceil(perimetro/2), 30));
        lista.add(new Item("Perfil U", "40x40mm", "un", (int) Math.ceil(perimetro/3), 40));
        lista.add(new Item("Fluido (Gás)", "R404A", "kg", 3, 85));
        lista.add(new Item("Sikaflex (Silicone)", "Tubete", "un", (int) Math.ceil(perimetro/5), 80));
        lista.add(new Item("Rebite", "Alumínio 1/8", "pct", 1, 20));
        lista.add(new Item("Poliuretano Spray", "400ml", "un", (int) Math.ceil((comprimento*largura)/2), 45));
        lista.add(new Item("Fiação", "2,5mm²", "rolo 100m", 1, 250));
        lista.add(new Item("Luminária", "LED", "un", (comprimento*largura > 12 ? 2 : 1), 80));
        lista.add(new Item("Dreno", "PVC 1/2", "un", 2, 15));
        lista.add(new Item("Condulete/Eletroduto", "PVC 20mm", "un", (int) Math.ceil(perimetro/2), 18));
        lista.add(new Item("Barra Roscada", "3/8 1m","un", (int) Math.ceil(altura/2),16));
        lista.add(new Item("Curva", "PVC 20mm", "un", 3, 8));

        return lista;
    }
}