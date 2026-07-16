package utng.gtid232.adla.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.Auditoria;

public class AuditoriaDAO {

    public List<Auditoria> listar() {
        List<Auditoria> lista = new ArrayList<>();
        // CORREGIDO: "auditorias" y "empresas" en plural para coincidir con PostgreSQL
        String sql = "SELECT a.id_auditoria, a.id_empresa, a.id_auditor, a.fecha_inicio, a.fecha_fin, a.estatus, "
                   + "e.razon_social AS nombre_empresa, au.nombre_completo AS nombre_auditor "
                   + "FROM auditorias a " 
                   + "INNER JOIN empresas e ON a.id_empresa = e.id_empresa " 
                   + "INNER JOIN auditores au ON a.id_auditor = au.id_auditor "
                   + "ORDER BY a.fecha_inicio DESC";
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Date fin = rs.getDate("fecha_fin");
                Auditoria a = new Auditoria(
                    rs.getInt("id_auditoria"),
                    rs.getInt("id_empresa"),
                    rs.getInt("id_auditor"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    fin != null ? fin.toLocalDate() : null,
                    rs.getString("estatus")
                );
                a.setNombreEmpresa(rs.getString("nombre_empresa"));
                a.setNombreAuditor(rs.getString("nombre_auditor"));
                
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int insertar(Auditoria a) {
        // CORREGIDO: "auditorias" en plural
        String sql = "INSERT INTO auditorias (id_empresa, id_auditor, fecha_inicio, fecha_fin, estatus) VALUES (?, ?, ?, ?, ?) RETURNING id_auditoria";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, a.getIdempresa());
            ps.setInt(2, a.getIdauditor());
            ps.setDate(3, Date.valueOf(a.getFechaInicio()));
            if (a.getFechaFin() != null) {
                ps.setDate(4, Date.valueOf(a.getFechaFin()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, a.getEstatus());
            
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

    public boolean actualizar(Auditoria a) {
        // CORREGIDO: "auditorias" en plural
        String sql = "UPDATE auditorias SET id_empresa=?, id_auditor=?, fecha_inicio=?, fecha_fin=?, estatus=? WHERE id_auditoria=?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, a.getIdempresa());
            ps.setInt(2, a.getIdauditor());
            ps.setDate(3, Date.valueOf(a.getFechaInicio()));
            if (a.getFechaFin() != null) {
                ps.setDate(4, Date.valueOf(a.getFechaFin()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, a.getEstatus());
            ps.setInt(6, a.getIdAuditoria());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        // CORREGIDO: "auditorias" en plural
        String sql = "DELETE FROM auditorias WHERE id_auditoria = ?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}