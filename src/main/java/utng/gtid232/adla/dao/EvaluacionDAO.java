package utng.gtid232.adla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.Evaluacion;

public class EvaluacionDAO {

    public List<Evaluacion> listarPorAuditoria(int auditoriaId) {
        List<Evaluacion> lista = new ArrayList<>();
        // Consulta optimizada con JOIN para traer los datos descriptivos adicionales
        String sql = "SELECT ev.id_evaluacion, ev.id_auditoria, ev.id_criterio, ev.cumplimiento, "
                   + "ev.observaciones, ev.evidencia_ref, ev.fecha_captura, "
                   + "cr.descripcion AS descripcion_criterio, ca.nombre AS nombre_categoria "
                   + "FROM evaluacion ev "
                   + "INNER JOIN criterio cr ON ev.id_criterio = cr.id_criterio "
                   + "INNER JOIN categoria ca ON cr.id_categoria = ca.id_categoria "
                   + "WHERE ev.id_auditoria = ? ORDER BY ca.id_categoria, cr.id_criterio";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, auditoriaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evaluacion e = new Evaluacion(
                        rs.getInt("id_auditoria"),
                        rs.getInt("id_criterio"),
                        rs.getString("cumplimiento"),
                        rs.getString("observaciones"),
                        rs.getString("evidencia_ref")
                    );
                    e.setIdEvaluacion(rs.getInt("id_evaluacion"));
                    
                    // Manejo del campo LocalDateTime de fechaCaptura
                    Timestamp ts = rs.getTimestamp("fecha_captura");
                    if (ts != null) {
                        e.setFechaCaptura(ts.toLocalDateTime());
                    }
                    
                    // Asignación de propiedades adicionales
                    e.setDescripcionCriterio(rs.getString("descripcion_criterio"));
                    e.setNombreCategoria(rs.getString("nombre_categoria"));
                    
                    lista.add(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean guardarOActualizar(Evaluacion ev) {
        String existeSql = "SELECT id_evaluacion FROM evaluacion WHERE id_auditoria = ? AND id_criterio = ?";
        String insertSql = "INSERT INTO evaluacion (id_auditoria, id_criterio, cumplimiento, observaciones, evidencia_ref) VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE evaluacion SET cumplimiento = ?, observaciones = ?, evidencia_ref = ? WHERE id_auditoria = ? AND id_criterio = ?";
        
        try (Connection con = ConexionBD.getInstancia().getConexion()) {
            boolean existe = false;
            try (PreparedStatement ps = con.prepareStatement(existeSql)) {
                ps.setInt(1, ev.getIdAuditoria());
                ps.setInt(2, ev.getIdCriterio());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existe = true;
                    }
                }
            }
            
            if (existe) {
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setString(1, ev.getCumplimiento());
                    ps.setString(2, ev.getObservaciones());
                    ps.setString(3, ev.getEvidenciaRef());
                    ps.setInt(4, ev.getIdAuditoria());
                    ps.setInt(5, ev.getIdCriterio());
                    return ps.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setInt(1, ev.getIdAuditoria());
                    ps.setInt(2, ev.getIdCriterio());
                    ps.setString(3, ev.getCumplimiento());
                    ps.setString(4, ev.getObservaciones());
                    ps.setString(5, ev.getEvidenciaRef());
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}