package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // URL JDBC do Supabase
    private static final String URL =
            "jdbc:postgresql://db.ddbmgpubuvhsialvgpen.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Bssrefrigeracao"; // substitua pela senha real

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conectado ao Supabase com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}