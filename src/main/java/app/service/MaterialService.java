package app.service;

import app.database.DatabaseConnection;
import app.model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialService {

    public List<Material> buscarTodos() {

        List<Material> lista = new ArrayList<>();
        String sql = "SELECT * FROM materiais ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Material(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("valor"),
                        rs.getString("unidade"),
                        rs.getString("classe")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Material material) {

        String sql = "UPDATE materiais SET valor=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, material.getValor());
            stmt.setInt(2, material.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}