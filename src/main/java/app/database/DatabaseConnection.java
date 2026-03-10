package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // URL JDBC do Supabase (SSL obrigatório)
    private static final String URL =
            "jdbc:postgresql://db.ddbmgpubuvhsialvgpen.supabase.co:5432/postgres?sslmode=require";

    private static final String USER = "postgres";
    private static final String PASSWORD = "Bssrefrigeracao";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Método apenas para teste
    public static void main(String[] args) {

        System.out.println("Testando conexão com o banco...");

        try (Connection conn = getConnection()) {

            if (conn != null) {
                System.out.println("Conectado ao Supabase com sucesso!");
                System.out.println("Banco: " + conn.getCatalog());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco:");
            e.printStackTrace();
        }
    }
}