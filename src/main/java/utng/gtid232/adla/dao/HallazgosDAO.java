package utng.gtid232.adla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.Hallazgos;

public class HallazgosDAO {

   
    public List<Hallazgos> listarPorEvaluacion(int idEvaluacion) {
        List<Hallazgos> lista = new ArrayList<>();
        String sql = "SELECT id_hallazgo, id_evaluacion, tipo, nivel_riesgo, descripcion "
                   + "FROM hallazgos WHERE id_evaluacion = ? ORDER BY id_hallazgo";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idEvaluacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Hallazgos h = new Hallazgos(
                        rs.getInt("id_evaluacion"),
                        rs.getString("tipo"),
                        rs.getString("nivel_riesgo"),
                        rs.getString("descripcion")
                    );
                    h.setIdHallazgo(rs.getInt("id_hallazgo"));
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    public int insertar(Hallazgos h) {
        String sql = "INSERT INTO hallazgos (id_evaluacion, tipo, nivel_riesgo, descripcion) "
                   + "VALUES (?, ?, ?, ?) RETURNING id_hallazgo";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, h.getIdEvaluacion());
            ps.setString(2, h.getTipo());
            ps.setString(3, h.getNivelRiesgo());
            ps.setString(4, h.getDescripcion());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    
    public boolean actualizar(Hallazgos h) {
        String sql = "UPDATE hallazgos SET tipo = ?, nivel_riesgo = ?, descripcion = ? WHERE id_hallazgo = ?";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, h.getTipo());
            ps.setString(2, h.getNivelRiesgo());
            ps.setString(3, h.getDescripcion());
            ps.setInt(4, h.getIdHallazgo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    
    public boolean eliminar(int idHallazgo) {
        String sql = "DELETE FROM hallazgos WHERE id_hallazgo = ?";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idHallazgo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}