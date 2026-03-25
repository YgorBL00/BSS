package app.service;

import app.database.DatabaseConnection;
import app.model.Evaporadora;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EvaporadoraService {

    /**
     * Busca a evaporadora mais próxima da carga necessária e da temperatura de evaporação.
     * Sempre retorna a evaporadora mais adequada, mesmo que não exista exato.
     */
    public Evaporadora buscarEvaporadora(double cargaNecessaria, int tempEvap) {

        String sql = """
            SELECT 
                e.id,
                e.codigo,
                e.quantidade_ventiladores,
                e.vazao_m3h,
                c.capacidade_kcal,
                e.valor,
                c.temp_evaporacao
            FROM evaporadoras e
            JOIN carga_termica_evap c 
                ON e.id = c.evaporadora_id
            ORDER BY 
                ABS(c.temp_evaporacao - ?) ASC,     -- aproxima mais da temperatura
                CASE WHEN c.capacidade_kcal >= ? THEN 0 ELSE 1 END, -- prioriza capacidade >= necessária
                ABS(c.capacidade_kcal - ?) ASC      -- aproxima mais da carga
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // parâmetros do SQL
            stmt.setInt(1, tempEvap);
            stmt.setDouble(2, cargaNecessaria);
            stmt.setDouble(3, cargaNecessaria);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Evaporadora evap = new Evaporadora();
                evap.setId(rs.getInt("id"));
                evap.setCodigo(rs.getString("codigo"));
                evap.setVentiladores(rs.getInt("quantidade_ventiladores"));
                evap.setVazao(rs.getDouble("vazao_m3h"));
                evap.setCapacidade(rs.getDouble("capacidade_kcal"));
                evap.setValor(rs.getDouble("valor"));

                System.out.println("Evaporadora selecionada: " + evap.getCodigo() +
                        " | Capacidade: " + evap.getCapacidade() +
                        " | Temp Evap: " + rs.getInt("temp_evaporacao") + "°C");

                return evap;
            } else {
                System.out.println("Nenhuma evaporadora encontrada próxima da carga/temp especificada.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar evaporadora: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // MAIN de teste
    public static void main(String[] args) {
        EvaporadoraService service = new EvaporadoraService();

        // Teste: imprime todas
        service.imprimirTodasEvaporadoras();

        // Teste: buscar evaporadora aproximada
        double cargaNecessaria = 1800; // kcal
        int tempEvap = -5;             // °C
        Evaporadora evap = service.buscarEvaporadora(cargaNecessaria, tempEvap);

        if (evap != null) {
            System.out.println("Evaporadora selecionada no teste: " + evap.getCodigo());
        }
    }

    // Mantém seu método existente de imprimir todas evaporadoras
    public void imprimirTodasEvaporadoras() {
        String sql = """
            SELECT 
                e.id,
                e.codigo,
                e.quantidade_ventiladores,
                e.vazao_m3h,
                c.temp_evaporacao,
                c.capacidade_kcal,
                c.capacidade_watts
            FROM evaporadoras e
            LEFT JOIN carga_termica_evap c 
                ON e.id = c.evaporadora_id
            ORDER BY e.codigo, c.temp_evaporacao
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int currentId = -1;

            while (rs.next()) {
                int id = rs.getInt("id");
                String codigo = rs.getString("codigo");
                int ventiladores = rs.getInt("quantidade_ventiladores");
                double vazao = rs.getDouble("vazao_m3h");

                Integer tempEvap = rs.getObject("temp_evaporacao", Integer.class);
                Double capacidadeKcal = rs.getObject("capacidade_kcal", Double.class);
                Double capacidadeWatts = rs.getObject("capacidade_watts", Double.class);

                if (id != currentId) {
                    currentId = id;
                    System.out.println("\n===== Evaporadora: " + codigo + " =====");
                    System.out.println(
                            "ID: " + id +
                                    " | Ventiladores: " + ventiladores +
                                    " | Vazão: " + vazao + " m3/h"
                    );
                }

                if (tempEvap != null) {
                    System.out.println(
                            "Temp Evap: " + tempEvap + "°C" +
                                    " | Capacidade: " + capacidadeKcal + " kcal" +
                                    " | " + capacidadeWatts + " W"
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}