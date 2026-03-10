package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteSupabase {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://db.ddbmgpubuvhsialvgpen.supabase.co:5432/postgres?sslmode=require";
        String user = "postgres";
        String password = "Bssrefrigeracao";
        Connection conn = null;

        try {
            System.out.println("Tentando conectar ao banco...");

            conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("Conexão realizada com sucesso!");
                System.out.println("Banco: " + conn.getCatalog());
            }

        } catch (SQLException e) {
            System.out.println("Erro ao conectar:");
            e.printStackTrace();
        } finally {

            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("Conexão encerrada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}