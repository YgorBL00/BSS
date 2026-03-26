package app.service;

import app.database.DatabaseConnection;
import java.sql.*;

public class VersaoService {

    public String buscarConfigUpdate() {

        String sql = "SELECT url_config FROM versao_app ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("url_config");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}