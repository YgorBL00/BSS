package app.service;

import app.database.DatabaseConnection;
import app.model.Equipamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UCService {

    public void imprimirTodasUnidades() {

        String sql = """
                SELECT 'R22' AS tipo_gas, u.modelo, u.tanque_de_liquido, u.linha_de_liquido, u.linha_de_succao_gas,
                       c.temp_evaporacao, c.temp_ambiente, c.carga_kcal
                FROM UC_R22 u
                LEFT JOIN carga_termica_r22 c ON u.modelo = c.modelo
                UNION ALL
                SELECT 'R404A' AS tipo_gas, u.modelo, u.tanque_de_liquido, u.linha_de_liquido, u.linha_de_succao_gas,
                       c.temp_evaporacao, c.temp_ambiente, c.carga_kcal
                FROM UC_R404A u
                LEFT JOIN carga_termica_r404a c ON u.modelo = c.modelo
                ORDER BY tipo_gas, modelo, temp_evaporacao, temp_ambiente;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            String currentModel = "";
            String currentGas = "";

            while (rs.next()) {
                String tipoGas = rs.getString("tipo_gas");
                String modelo = rs.getString("modelo");
                double tanque = rs.getDouble("tanque_de_liquido");
                String linhaLiquido = rs.getString("linha_de_liquido");
                String linhaSucao = rs.getString("linha_de_succao_gas");
                int evap = rs.getInt("temp_evaporacao");
                int amb = rs.getInt("temp_ambiente");
                double carga = rs.getDouble("carga_kcal");

                if (!tipoGas.equals(currentGas) || !modelo.equals(currentModel)) {
                    currentGas = tipoGas;
                    currentModel = modelo;
                    System.out.println("\n===== " + tipoGas + " - Modelo: " + modelo + " =====");
                    System.out.println("Tanque: " + tanque + " | Linha de Líquido: " + linhaLiquido + " | Linha de Sucção: " + linhaSucao);
                }

                if (rs.getObject("temp_evaporacao") != null) {
                    System.out.println("Evap: " + evap + "°C | Ambiente: " + amb + "°C | Carga: " + carga + " kcal");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Equipamento buscarEquipamento(double cargaNecessaria,
                                         int tempAmbiente,
                                         int tempEvap,
                                         String gas) {

        String tabelaUC = gas.equals("R404A") ? "UC_R404A" : "UC_R22";
        String tabelaCarga = gas.equals("R404A") ? "carga_termica_r404a" : "carga_termica_r22";

        String sql = String.format("""
        SELECT u.modelo, c.carga_kcal, c.temp_evaporacao
        FROM %s u
        JOIN %s c ON u.modelo = c.modelo
        WHERE c.temp_ambiente = ?
        ORDER BY 
            ABS(c.temp_evaporacao - ?) ASC,
            CASE 
                WHEN c.carga_kcal >= ? THEN 0 
                ELSE 1 
            END,
            ABS(c.carga_kcal - ?) ASC
        LIMIT 1
    """, tabelaUC, tabelaCarga);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tempAmbiente);
            stmt.setInt(2, tempEvap);
            stmt.setDouble(3, cargaNecessaria);
            stmt.setDouble(4, cargaNecessaria);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Equipamento(
                        rs.getString("modelo"),
                        gas,
                        rs.getDouble("carga_kcal")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // MAIN para teste
    public static void main(String[] args) {
        UCService service = new UCService();
        service.imprimirTodasUnidades();
    }
}