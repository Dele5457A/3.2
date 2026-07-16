package utng.gtid232.adla.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.PlanesAccion;

public class PlanAccionDAO {

    
    public List<PlanesAccion> listarTodos() {
        List<PlanesAccion> lista = new ArrayList<>();
        String sql = "SELECT p.id_plan, p.id_hallazgo, p.responsable, p.fecha_compromiso, p.estatus, "
                   + "h.descripcion AS descripcion_hallazgo "
                   + "FROM planes_accion p "
                   + "INNER JOIN hallazgos h ON p.id_hallazgo = h.id_hallazgo "
                   + "ORDER BY p.fecha_compromiso ASC";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                PlanesAccion p = new PlanesAccion(
                    rs.getInt("id_hallazgo"),
                    rs.getString("responsable"),
                    rs.getDate("fecha_compromiso").toLocalDate(),
                    rs.getString("estatus")
                );
                p.setIdPlan(rs.getInt("id_plan"));
                p.setDescripcionHallazgo(rs.getString("descripcion_hallazgo"));
                
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    public List<PlanesAccion> listarPorHallazgo(int idHallazgo) {
        List<PlanesAccion> lista = new ArrayList<>();
        String sql = "SELECT p.id_plan, p.id_hallazgo, p.responsable, p.fecha_compromiso, p.estatus, "
                   + "h.descripcion AS descripcion_hallazgo "
                   + "FROM planes_accion p "
                   + "INNER JOIN hallazgos h ON p.id_hallazgo = h.id_hallazgo "
                   + "WHERE p.id_hallazgo = ? "
                   + "ORDER BY p.id_plan";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idHallazgo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PlanesAccion p = new PlanesAccion(
                        rs.getInt("id_hallazgo"),
                        rs.getString("responsable"),
                        rs.getDate("fecha_compromiso").toLocalDate(),
                        rs.getString("estatus")
                    );
                    p.setIdPlan(rs.getInt("id_plan"));
                    p.setDescripcionHallazgo(rs.getString("descripcion_hallazgo"));
                    
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    
    public int insertar(PlanesAccion p) {
        String sql = "INSERT INTO planes_accion (id_hallazgo, responsable, fecha_compromiso, estatus) "
                   + "VALUES (?, ?, ?, ?) RETURNING id_plan";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, p.getIdHallazgo());
            ps.setString(2, p.getResponsable());
            ps.setDate(3, Date.valueOf(p.getFechaCompromiso()));
            ps.setString(4, p.getEstatus());
            
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

    
    public boolean actualizar(PlanesAccion p) {
        String sql = "UPDATE planes_accion SET id_hallazgo = ?, responsable = ?, fecha_compromiso = ?, estatus = ? "
                   + "WHERE id_plan = ?";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, p.getIdHallazgo());
            ps.setString(2, p.getResponsable());
            ps.setDate(3, Date.valueOf(p.getFechaCompromiso()));
            ps.setString(4, p.getEstatus());
            ps.setInt(5, p.getIdPlan());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    
    public boolean eliminar(int idPlan) {
        String sql = "DELETE FROM planes_accion WHERE id_plan = ?";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPlan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}