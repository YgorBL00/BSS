package com.example.bss.model;

import java.util.Arrays;
import java.util.List;

public class Evaporadoras {
    private String marca;
    private String modelo;
    private double capacidadeKcal;
    private double temperaturaMin;
    private double temperaturaMax;
    private String gas;
    private String tensao;
    private int hp; // se quiser usar também
    private double Preco;

    public Evaporadoras(String marca, String modelo, double capacidadeKcal,
                        double temperaturaMin, double temperaturaMax, String gas, String tensao, int hp, double preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.capacidadeKcal = capacidadeKcal;
        this.temperaturaMin = temperaturaMin;
        this.temperaturaMax = temperaturaMax;
        this.gas = gas;
        this.tensao = tensao;
        this.hp = hp;
        this.Preco = preco;
    }

    public String getModelo() { return modelo; }
    public double getCapacidadeKcal() { return capacidadeKcal; }
    public double getTemperaturaMin() { return temperaturaMin; }
    public double getTemperaturaMax() { return temperaturaMax; }
    public String getGas() { return gas; }
    public String getTensao() { return tensao; }
    public int getHp() { return hp; }
    public double getPreco() { return Preco; }

    private static final List<Evaporadoras> catalogo = Arrays.asList(

            new Evaporadoras("MIPAL", "HD0042", 4305, 5, 20, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 4129, 0, 4, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 3938, -5, -1, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 3730, -10, -6, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 3511, -15, -11, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 3256, -20, -16, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 2975, -25, -21, "R22", "220V", 1,2500),
            new Evaporadoras("MIPAL", "HD0042", 2670, -30, -26, "R22", "220V", 1,2500),

            new Evaporadoras("MIPAL", "HD0050", 5147, 5, 20, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 4938, 0, 4, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 4708, -5, -1, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 4460, -10, -6, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 4199, -15, -11, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 3893, -20, -16, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 3558, -25, -21, "R22", "220V", 1,3000),
            new Evaporadoras("MIPAL", "HD0050", 3193, -30, -26, "R22", "220V", 1,3000),

            new Evaporadoras("MIPAL", "HD0064", 6590, 5, 20, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 6322, 0, 4, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 6028, -5, -1, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 5710, -10, -6, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 5375, -15, -11, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 4985, -20, -16, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 4555, -25, -21, "R22", "220V", 1,3500),
            new Evaporadoras("MIPAL", "HD0064", 4088, -30, -26, "R22", "220V", 1,3500),

            new Evaporadoras("MIPAL", "HD0077", 7894, 5, 20, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 7572, 0, 4, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 7221, -5, -1, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 6840, -10, -6, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 6439, -15, -11, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 5971, -20, -16, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 5456, -25, -21, "R22", "220V", 1,4000),
            new Evaporadoras("MIPAL", "HD0077", 4897, -30, -26, "R22", "220V", 1,4000),

            new Evaporadoras("MIPAL", "HD0086", 8863, 5, 20, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 8503, 0, 4, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 8108, -5, -1, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 7680, -10, -6, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 7230, -15, -11, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 6704, -20, -16, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 6126, -25, -21, "R22", "220V", 1,4500),
            new Evaporadoras("MIPAL", "HD0086", 5498, -30, -26, "R22", "220V", 1,4500),

            new Evaporadoras("MIPAL", "HD0103", 10617, -30, -26, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 10185, 5, 20, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 9712, 0, 4, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 9200, -5, -1, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 8661, -10, -6, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 8031, -15, -11, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 7339, -20, -16, "R22", "220V", 1,5000),
            new Evaporadoras("MIPAL", "HD0103", 6586, -25, -21, "R22", "220V", 1,5000),

            new Evaporadoras("MIPAL", "HD0115", 11742, 5, 20, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 10742, 0, 4, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 10175, -5, -1, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 9579, -10, -6, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 8882, -15, -11, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 8117, -20, -16, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 7284, -25, -21, "R22", "220V", 1,5500),
            new Evaporadoras("MIPAL", "HD0115", 6516, -30, -26, "R22", "220V", 1,5500),

            new Evaporadoras("MIPAL", "HD0138", 14062, 5, 20, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 13490, 0, 4, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 12864, -5, -1, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 12185, -10, -6, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 11471, -15, -11, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 10637, -20, -16, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 9720, -25, -21, "R22", "220V", 1,6000),
            new Evaporadoras("MIPAL", "HD0138", 8723, -30, -26, "R22", "220V", 1,6000),

            new Evaporadoras("MIPAL", "HD00173", 17451, 5, 20, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 16742, 0, 4, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 15964, -5, -1, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 15122, -10, -6, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 14236, -15, -11, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 13201, -20, -16, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 12063, -25, -21, "R22", "220V", 1,6500),
            new Evaporadoras("MIPAL", "HD00173", 10826, -30, -26, "R22", "220V", 1,6500),

            new Evaporadoras("MIPAL", "HD0208", 20960, 5, 20, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 20107, 0, 4, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 19174, -5, -1, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 18162, -10, -6, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 17098, -15, -11, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 15855, -20, -16, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 14488, -25, -21, "R22", "220V", 1,7000),
            new Evaporadoras("MIPAL", "HD0208", 13003, -30, -26, "R22", "220V", 1,7000),

            new Evaporadoras("MIPAL", "HD0265", 26705, 5, 20, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 25618, 0, 4, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 24429, -5, -1, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 23140, -10, -6, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 21784, -15, -11, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 20201, -20, -16, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 18459, -25, -21, "R22", "220V", 1,7500),
            new Evaporadoras("MIPAL", "HD0265", 16566, -30, -26, "R22", "220V", 1,7500),

            new Evaporadoras("MIPAL", "HD0318", 32160, 5, 20, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 30852, 0, 4, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 29419, -5, -1, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 27867, -10, -6, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 26234, -15, -11, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 24327, -20, -16, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 22230, -25, -21, "R22", "220V", 1,8000),
            new Evaporadoras("MIPAL", "HD0318", 19950, -30, -26, "R22", "220V", 1,8000)


            // adicione o restante
    );

    public static List<Evaporadoras> getCatalogo() {
        return catalogo;
    }
}