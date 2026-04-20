package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // ✅ URL com POOLER (IPv4) + SSL
    private static final String URL =
            "jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:6543/postgres?sslmode=require";

    // ⚠️ usuário muda no pooler
    private static final String USER = "postgres.ddbmgpubuvhsialvgpen";

    private static final String PASSWORD = "Bssrefrigeracao";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Teste
    public static void main(String[] args) {

        System.out.println("Testando conexão com o banco...");

        try (Connection conn = getConnection()) {

            if (conn != null) {
                System.out.println("✅ Conectado ao Supabase com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar ao banco:");
            e.printStackTrace();
        }
    }
}