package utng.gtid232.adla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.Criterio;

public class CriterioDAO {

    public List<Criterio> listarPorCategoria(int categoriaId) {
        List<Criterio> lista = new ArrayList<>();
        String sql = "SELECT cr.id_criterio, cr.id_categoria, cr.descripcion, cr.peso, ca.nombre AS nombre_categoria "
                   + "FROM criterio cr "
                   + "INNER JOIN categoria ca ON cr.id_categoria = ca.id_categoria "
                   + "WHERE cr.id_categoria = ? ORDER BY cr.id_criterio";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, categoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Criterio c = new Criterio(
                        rs.getInt("id_criterio"),
                        rs.getInt("id_categoria"),
                        rs.getString("descripcion"),
                        rs.getDouble("peso")
                    );
                    c.setNombreCategoria(rs.getString("nombre_categoria"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Criterio> listarTodos() {
        List<Criterio> lista = new ArrayList<>();
        String sql = "SELECT cr.id_criterio, cr.id_categoria, cr.descripcion, cr.peso, ca.nombre AS nombre_categoria "
                   + "FROM criterio cr "
                   + "INNER JOIN categoria ca ON cr.id_categoria = ca.id_categoria "
                   + "ORDER BY cr.id_categoria, cr.id_criterio";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Criterio c = new Criterio(
                    rs.getInt("id_criterio"),
                    rs.getInt("id_categoria"),
                    rs.getString("descripcion"),
                    rs.getDouble("peso")
                );
                c.setNombreCategoria(rs.getString("nombre_categoria"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}