package com.example.bss.model;

import java.util.Arrays;
import java.util.List;


public class UnidadeCondensadoras {
    private String marca;
    private String modelo;
    private double HP;
    private double capacidadeKcal; // Capacidade em kcal/h
    private double temperaturaMin; // Temperatura mínima de operação (°C)
    private double temperaturaMax; // Temperatura máxima de operação (°C)
    private String gas;
    private String tensao;
    private double Preco;

    public UnidadeCondensadoras(String marca, String modelo,double HP, double capacidadeKcal, double temperaturaMin, double temperaturaMax,String gas, String tensao, double preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.HP = HP;
        this.capacidadeKcal = capacidadeKcal;
        this.temperaturaMin = temperaturaMin;
        this.temperaturaMax = temperaturaMax;
        this.gas = gas;
        this.tensao = tensao;
        this.Preco = preco;
    }

    public String getModelo() { return modelo; }
    public double getHP() {return HP;}
    public double getCapacidadeKcal() { return capacidadeKcal; }
    public double getTemperaturaMin() { return temperaturaMin; }
    public double getTemperaturaMax() { return temperaturaMax; }
    public String getTensao() { return tensao; }
    public double getPreco() { return Preco; }

    private static final List<UnidadeCondensadoras> catalogo = Arrays.asList(
            new UnidadeCondensadoras("Danfoss","OP-HJM019",1.5,2618,-5,20,"R22","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJM019",1.5,3324,0,20,"R22","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJM019",1.5,4130,5,20,"R22","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJM019",1.5,5034,10,20,"R22","220V",6000),

            new UnidadeCondensadoras("Danfoss","OP-HJM022",2.0,3586,-5,20,"R22","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJM022",2.0,4421,0,20,"R22","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJM022",2.0,5334,5,20,"R22","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJM022",2.0,6321,10,20,"R22","220V",6500),

            new UnidadeCondensadoras("Danfoss","OP-HJM028",2.50,5279,-5,20,"R22","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJM028",2.50,6395,0,20,"R22","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJM028",2.50,7616,5,20,"R22","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJM028",2.50,8943,10,20,"R22","220V",7000),

            new UnidadeCondensadoras("Danfoss","OP-HJM032",3.00,5584,-5,20,"R22","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJM032",3.00,6535,0,20,"R22","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJM032",3.00,7850,5,20,"R22","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJM032",3.00,9291,10,20,"R22","220V",7500),

            new UnidadeCondensadoras("Danfoss","OP-HJM036",3.50,6508,-5,20,"R22","220V",7600),
            new UnidadeCondensadoras("Danfoss","OP-HJM036",3.50,7796,0,20,"R22","220V",7600),
            new UnidadeCondensadoras("Danfoss","OP-HJM036",3.50,9185,5,20,"R22","220V",7600),
            new UnidadeCondensadoras("Danfoss","OP-HJM036",3.50,10670,10,20,"R22","220V",7600),

            new UnidadeCondensadoras("Danfoss","OP-HJM040",4.00,7421,-5,20,"R22","220V",7700),
            new UnidadeCondensadoras("Danfoss","OP-HJM040",4.00,8967,0,20,"R22","220V",7700),
            new UnidadeCondensadoras("Danfoss","OP-HJM040",4.00,10670,5,20,"R22","220V",7700),
            new UnidadeCondensadoras("Danfoss","OP-HJM040",4.00,12210,10,20,"R22","220V",7700),

            new UnidadeCondensadoras("Danfoss","OP-HJM044",4.50,7555,-5,20,"R22","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJM044",4.50,9348,0,20,"R22","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJM044",4.50,11330,5,20,"R22","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJM044",4.50,13510,10,20,"R22","220V",8000),

            new UnidadeCondensadoras("Danfoss","OP-HJM050",5.00,8541,-5,20,"R22","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJM050",5.00,10460,0,20,"R22","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJM050",5.00,12590,5,20,"R22","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJM050",5.00,14910,10,20,"R22","220V",8500),

            new UnidadeCondensadoras("Danfoss","OP-HJM056",5.50,9554,-5,20,"R22","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJM056",5.50,11620,0,20,"R22","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJM056",5.50,13880,5,20,"R22","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJM056",5.50,16320,10,20,"R22","220V",9000),

            new UnidadeCondensadoras("Danfoss","OP-HJM064",6.50,10580,-5,20,"R22","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJM064",6.50,12860,0,20,"R22","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJM064",6.50,15360,5,20,"R22","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJM064",6.50,18080,10,20,"R22","220V",9500),

            new UnidadeCondensadoras("Danfoss","OP-HJM072",7.00,12600,-5,20,"R22","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJM072",7.00,15370,0,20,"R22","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJM072",7.00,18460,5,20,"R22","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJM072",7.00,21840,10,20,"R22","220V",10000),

            new UnidadeCondensadoras("Danfoss","OP-HJM080",8.00,14110,-5,20,"R22","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJM080",8.00,17120,0,20,"R22","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJM080",8.00,20440,5,20,"R22","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJM080",8.00,24040,10,20,"R22","220V",10500),

            new UnidadeCondensadoras("Danfoss","OP-HJM0100",9.00,15480,-5,20,"R22","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0100",9.00,18850,0,20,"R22","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0100",9.00,22580,5,20,"R22","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0100",9.00,26660,10,20,"R22","220V",11000),

            new UnidadeCondensadoras("Danfoss","OP-HJM0125",10.00,21320,-5,20,"R22","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0125",10.00,25910,0,20,"R22","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0125",10.00,30980,5,20,"R22","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0125",10.00,36540,10,20,"R22","220V",12000),

            new UnidadeCondensadoras("Danfoss","OP-HJM0144",11.00,23680,-5,20,"R22","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0144",11.00,28670,0,20,"R22","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0144",11.00,34180,5,20,"R22","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0144",11.00,40180,10,20,"R22","220V",13000),

            new UnidadeCondensadoras("Danfoss","OP-HJM0160",12.00,25740,-5,20,"R22","220V",15000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0160",12.00,31100,0,20,"R22","220V",15000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0160",12.00,36990,5,20,"R22","220V",15000),
            new UnidadeCondensadoras("Danfoss","OP-HJM0160",12.00,43370,10,20,"R22","220V",15000),


            new UnidadeCondensadoras("Danfoss","OP-HJZ019",1.50,586,-30,-6,"R404A","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ019",1.50,952,-25,-6,"R404A","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ019",1.50,1385,-20,-6,"R404A","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ019",1.50,1888,-15,-6,"R404A","220V",6000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ019",1.50,2462,-10,-6,"R404A","220V",6000),

            new UnidadeCondensadoras("Danfoss","OP-HJZ022",2.00,1023,-30,-6,"R404A","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ022",2.00,1449,-25,-6,"R404A","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ022",2.00,1995,-20,-6,"R404A","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ022",2.00,2543,-15,-6,"R404A","220V",6500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ022",2.00,3211,-10,-6,"R404A","220V",6500),

            new UnidadeCondensadoras("Danfoss","OP-HJZ028",2.50,1531,-30,-6,"R404A","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ028",2.50,2098,-25,-6,"R404A","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ028",2.50,2761,-20,-6,"R404A","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ028",2.50,3525,-15,-6,"R404A","220V",7000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ028",2.50,4394,-10,-6,"R404A","220V",7000),

            new UnidadeCondensadoras("Danfoss","OP-HJZ032",3.00,1616,-30,-6,"R404A","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ032",3.00,2222,-25,-6,"R404A","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ032",3.00,2944,-20,-6,"R404A","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ032",3.00,3785,-15,-6,"R404A","220V",7500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ032",3.00,4746,-10,-6,"R404A","220V",7500),

            new UnidadeCondensadoras("Danfoss","OP-HJZ036",3.50,1779,-30,-6,"R404A","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ036",3.50,2490,-25,-6,"R404A","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ036",3.50,3325,-20,-6,"R404A","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ036",3.50,4286,-15,-6,"R404A","220V",8000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ036",3.50,5372,-10,-6,"R404A","220V",8000),

            new UnidadeCondensadoras("Danfoss","OP-HJZ040",4.00,2122,-30,-6,"R404A","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ040",4.00,2972,-25,-6,"R404A","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ040",4.00,3977,-20,-6,"R404A","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ040",4.00,5145,-15,-6,"R404A","220V",8500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ040",4.00,6479,-10,-6,"R404A","220V",8500),

            new UnidadeCondensadoras("Danfoss","OP-HJZ044",4.50,2168,-30,-6,"R404A","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ044",4.50,3101,-25,-6,"R404A","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ044",4.50,4199,-20,-6,"R404A","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ044",4.50,5471,-15,-6,"R404A","220V",9000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ044",4.50,6920,-10,-6,"R404A","220V",9000),


            new UnidadeCondensadoras("Danfoss","OP-HJZ050",5.00,2630,-30,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ050",5.00,3652,-25,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ050",5.00,4850,-20,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ050",5.00,6234,-15,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ050",5.00,7612,-10,-6,"R404A","220V",9500),

            new UnidadeCondensadoras("Danfoss","OP-HJZ056",5.50,2918,-30,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ056",5.50,4082,-25,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ056",5.50,5437,-20,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ056",5.50,6985,-15,-6,"R404A","220V",9500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ056",5.50,8723,-10,-6,"R404A","220V",9500),

            new UnidadeCondensadoras("Danfoss","OP-HJZ064",6.50,3316,-30,-6,"R404A","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ064",6.50,4548,-25,-6,"R404A","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ064",6.50,5980,-20,-6,"R404A","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ064",6.50,7615,-15,-6,"R404A","220V",10000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ064",6.50,9450,-10,-6,"R404A","220V",10000),

            new UnidadeCondensadoras("Danfoss","OP-HJZ072",7.00,3973,-30,-6,"R404A","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ072",7.00,5393,-25,-6,"R404A","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ072",7.00,7066,-20,-6,"R404A","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ072",7.00,9002,-15,-6,"R404A","220V",10500),
            new UnidadeCondensadoras("Danfoss","OP-HJZ072",7.00,11210,-10,-6,"R404A","220V",10500),

            new UnidadeCondensadoras("Danfoss","OP-HJZ080",8.00,4677,-30,-6,"R404A","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ080",8.00,6205,-25,-6,"R404A","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ080",8.00,7989,-20,-6,"R404A","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ080",8.00,10040,-15,-6,"R404A","220V",11000),
            new UnidadeCondensadoras("Danfoss","OP-HJZ080",8.00,12350,-10,-6,"R404A","220V",11000),

            new UnidadeCondensadoras("Danfoss","OP-HGZ100",9.00,4788,-30,-6,"R404A","220V",11500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ100",9.00,6603,-25,-6,"R404A","220V",11500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ100",9.00,8697,-20,-6,"R404A","220V",11500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ100",9.00,11070,-15,-6,"R404A","220V",11500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ100",9.00,13720,-10,-6,"R404A","220V",11500),

            new UnidadeCondensadoras("Danfoss","OP-HGZ125",10.00,7162,-30,-6,"R404A","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ125",10.00,9464,-25,-6,"R404A","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ125",10.00,12130,-20,-6,"R404A","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ125",10.00,15180,-15,-6,"R404A","220V",12000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ125",10.00,18630,-10,-6,"R404A","220V",12000),

            new UnidadeCondensadoras("Danfoss","OP-HGZ0144",11.00,8503,-30,-6,"R404A","220V",12500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0144",11.00,11080,-25,-6,"R404A","220V",12500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0144",11.00,14060,-20,-6,"R404A","220V",12500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0144",11.00,17460,-15,-6,"R404A","220V",12500),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0144",11.00,21290,-10,-6,"R404A","220V",12500),

            new UnidadeCondensadoras("Danfoss","OP-HGZ0160",12.00,9256,-30,-6,"R404A","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0160",12.00,11980,-25,-6,"R404A","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0160",12.00,15110,-20,-6,"R404A","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0160",12.00,18660,-15,-6,"R404A","220V",13000),
            new UnidadeCondensadoras("Danfoss","OP-HGZ0160",12.00,22640,-10,-6,"R404A","220V",13000)
    );

    public static List<UnidadeCondensadoras> getCatalogo() {
        return catalogo;
    }

}