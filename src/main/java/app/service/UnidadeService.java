package app.service;

import app.database.DatabaseConnection;
import app.model.Capacidade;
import app.model.Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadeService {

    public List<Capacidade> buscarTodas() {

        List<Capacidade> lista = new ArrayList<>();

        String sql = """
            SELECT 
                m.id as modelo_id,
                m.modelo,
                m.linha,
                m.fluido,
                m.ventiladores,

                c.temperatura_evap,
                c.temperatura_ambiente,
                c.capacidade_kcal

            FROM capacidades c
            JOIN modelos m ON m.id = c.modelo_id
            ORDER BY m.modelo, c.temperatura_evap, c.temperatura_ambiente
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Modelo modelo = new Modelo(
                        rs.getInt("modelo_id"),
                        rs.getString("modelo"),
                        rs.getString("linha"),
                        rs.getString("fluido"),
                        rs.getInt("ventiladores")
                );

                Capacidade cap = new Capacidade(
                        modelo,
                        rs.getInt("temperatura_evap"),
                        rs.getInt("temperatura_ambiente"),
                        rs.getDouble("capacidade_kcal")
                );

                lista.add(cap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void main(String[] args) {

        UnidadeService service = new UnidadeService();

        List<Capacidade> lista = service.buscarTodas();

        System.out.println("=== LISTA DE UNIDADES ===");

        for (Capacidade c : lista) {

            System.out.println(
                    "Modelo: " + c.getModelo().getModelo() +
                            " | Evap: " + c.getTemperaturaEvap() + "°C" +
                            " | Amb: " + c.getTemperaturaAmbiente() + "°C" +
                            " | Capacidade: " + c.getCapacidadeKcal() + " kcal/h"
            );
        }
    }
}