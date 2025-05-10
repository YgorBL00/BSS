
package com.example.bss.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecomendacaoEvaporadora {

    public static List<Evaporadoras> recomendarEvaporadoras(double temperaturaInterna, double cargaTermica) {
        // Primeiro, tenta pegar a ideal
        List<Evaporadoras> candidatos = Evaporadoras.getCatalogo().stream()
                .filter(evap ->
                        temperaturaInterna >= evap.getTemperaturaMin() &&
                                temperaturaInterna <= evap.getTemperaturaMax() &&
                                evap.getCapacidadeKcal() >= cargaTermica
                )
                .sorted(Comparator.comparingDouble(Evaporadoras::getCapacidadeKcal))
                .limit(1)
                .collect(Collectors.toList());

        if (!candidatos.isEmpty()) {
            return candidatos;
        }

        // Se não achar, retorna a evaporadora mais potente cadastrada
        Optional<Evaporadoras> fallback = Evaporadoras.getCatalogo().stream()
                .max(Comparator.comparingDouble(Evaporadoras::getCapacidadeKcal));
        return fallback.map(List::of).orElse(List.of());
    }
}