package app.service;

import app.database.DatabaseConnection;
import app.model.Projeto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjetoService {

    public void salvarProjeto(
            String cliente,
            String tipoCamara,
            String dimensoes,
            double custo,
            double venda,
            File pdf
    ) {

        String sql = """
                INSERT INTO projetos
                (cliente, tipo_camara, dimensoes, custo_material, valor_venda, pdf)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try {

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, cliente);
            ps.setString(2, tipoCamara);
            ps.setString(3, dimensoes);
            ps.setDouble(4, custo);
            ps.setDouble(5, venda);

            FileInputStream fis = new FileInputStream(pdf);
            ps.setBinaryStream(6, fis, (int) pdf.length());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Projeto> listarProjetos() {

        List<Projeto> lista = new ArrayList<>();

        String sql = """
            SELECT id, cliente, dimensoes, valor_venda, data
            FROM projetos
            ORDER BY data DESC
            """;

        try {

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                lista.add(new Projeto(
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getString("dimensoes"),
                        rs.getDouble("valor_venda"),
                        rs.getTimestamp("data").toLocalDateTime()
                ));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public File baixarPDF(int projetoId) {

        String sql = "SELECT pdf FROM projetos WHERE id = ?";

        try {

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, projetoId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                byte[] bytes = rs.getBytes("pdf");

                File arquivo = File.createTempFile("projeto_", ".pdf");

                FileOutputStream fos = new FileOutputStream(arquivo);
                fos.write(bytes);
                fos.close();

                return arquivo;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}