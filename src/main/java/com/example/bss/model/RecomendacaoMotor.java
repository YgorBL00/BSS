package com.example.bss.model;

import java.util.Comparator;
import java.util.Optional;

public class RecomendacaoMotor {

    public static Optional<UnidadeCondensadoras> recomendarMotor(double temperaturaInterna, double cargaTermica) {
        // Primeira tentativa: filtro ideal
        Optional<UnidadeCondensadoras> ideal = UnidadeCondensadoras.getCatalogo().stream()
                .filter(motor ->
                        motor.getCapacidadeKcal() >= cargaTermica &&
                                temperaturaInterna >= motor.getTemperaturaMin() &&
                                temperaturaInterna <= motor.getTemperaturaMax()
                )
                .min(Comparator.comparingDouble(UnidadeCondensadoras::getCapacidadeKcal));

        if (ideal.isPresent()) {
            return ideal;
        } else {
            // Retorna o motor mais potente disponível, mesmo fora dos limites
            return UnidadeCondensadoras.getCatalogo().stream()
                    .max(Comparator.comparingDouble(UnidadeCondensadoras::getCapacidadeKcal));
        }
    }
}