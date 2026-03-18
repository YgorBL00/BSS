package app.service;

import app.database.DatabaseConnection;
import app.model.ModeloResultado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ModeloService {

    public void listarTodosModelos() {

        String sql = """
                SELECT 
                    v.id,
                    v.codigo_completo,
                    v.gas,
                    v.sufixo,
                    m.nome
                FROM variantes_modelo v
                JOIN modelos m ON m.id = v.modelo_id
                ORDER BY v.codigo_completo
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("=== LISTA DE MODELOS (32) ===");

            while (rs.next()) {

                System.out.println(
                        "ID: " + rs.getString("id") +
                                " | Código: " + rs.getString("codigo_completo") +
                                " | Gás: " + rs.getString("gas") +
                                " | Linha: " + rs.getString("sufixo") +
                                " | Base: " + rs.getString("nome")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detalharModelos() {

        String sql = """
        SELECT 
            v.codigo_completo,
            v.gas,
            v.sufixo,
            
            m.ventiladores,
            m.tanque_liquido,
            m.conexao_succao,
            m.conexao_liquido,

            f.temp_ambiente,
            f.temp_evaporacao,
            f.carga_termica

        FROM variantes_modelo v
        JOIN modelos m ON m.id = v.modelo_id
        LEFT JOIN faixas_operacao f ON f.variante_id = v.id

        ORDER BY v.codigo_completo, f.temp_ambiente, f.temp_evaporacao
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            String modeloAtual = "";
            Double tempAmbAtual = null;

            while (rs.next()) {

                String codigo = rs.getString("codigo_completo");

                // 🔥 NOVO MODELO
                if (!codigo.equals(modeloAtual)) {

                    modeloAtual = codigo;
                    tempAmbAtual = null; // reset ambiente

                    System.out.println("\n=================================");
                    System.out.println("🔧 MODELO: " + codigo);
                    System.out.println("GÁS: " + rs.getString("gas"));
                    System.out.println("LINHA: " + rs.getString("sufixo"));

                    System.out.println("Ventiladores: " + rs.getInt("ventiladores"));
                    System.out.println("Tanque: " + rs.getDouble("tanque_liquido") + " L");
                    System.out.println("Sucção: " + rs.getString("conexao_succao"));
                    System.out.println("Líquido: " + rs.getString("conexao_liquido"));
                }

                Double tempAmb = rs.getDouble("temp_ambiente");

                if (!rs.wasNull()) {

                    // 🔥 NOVA TEMPERATURA AMBIENTE
                    if (tempAmbAtual == null || !tempAmb.equals(tempAmbAtual)) {

                        tempAmbAtual = tempAmb;

                        System.out.println("\n🌡️ Temperatura ambiente: " + tempAmb + "°C\n");
                    }

                    // 🔥 LINHA DE CAPACIDADE
                    System.out.println(
                            rs.getDouble("temp_evaporacao") + "°C → " +
                                    rs.getDouble("carga_termica") + " kcal/h"
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModeloResultado buscarModeloIdeal(double cargaNecessaria, int tempAmbiente, double tempEvap, String gas) {

        String sql = """
        SELECT 
            v.codigo_completo,
            f.carga_termica
        FROM faixas_operacao f
        JOIN variantes_modelo v ON v.id = f.variante_id
        WHERE f.temp_ambiente = ?
        AND f.temp_evaporacao = ?
        AND v.gas = ?
        AND f.carga_termica >= ?
        ORDER BY f.carga_termica ASC
        LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tempAmbiente);
            stmt.setDouble(2, tempEvap);
            stmt.setString(3, gas);
            stmt.setDouble(4, cargaNecessaria);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ModeloResultado(
                        rs.getString("codigo_completo"),
                        rs.getDouble("capacidade_kcal"),
                        rs.getString("sucao"),
                        rs.getString("liquido"),
                        rs.getDouble("tanque_litros"),
                        rs.getString("gas")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        ModeloService service = new ModeloService();
        service.detalharModelos();
    }
}