package utng.gtid232.adla.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.Auditor;

public class AuditorDAO {

    private Auditor mapear(ResultSet rs) throws SQLException {
        Auditor a = new Auditor();
        a.setIdAuditor(rs.getInt("id_auditor"));
        a.setNombreCompleto(rs.getString("nombre_completo"));
        a.setCelulaProfesional(rs.getString("cedula_profesional")); 
        a.setEmail(rs.getString("email"));                       
        return a;
    }

    public List<Auditor> listarTodos() throws SQLException {
        List<Auditor> lista = new ArrayList<>();
        String sql = "SELECT * FROM auditores ORDER BY nombre_completo";
        
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } 
        return lista; 
    } 

    public Auditor obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM auditores WHERE id_auditor = ?";
        
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    } 

    public int insertar(Auditor a) throws SQLException {
        String sql = "INSERT INTO auditores (nombre_completo, cedula_profesional, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getInstancia()
                .getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombreCompleto());
            ps.setString(2, a.getCelulaProfesional()); 
            ps.setString(3, a.getEmail());
            ps.executeUpdate();
            
            try (ResultSet key = ps.getGeneratedKeys()) {
                if (key.next()) {
                    return key.getInt(1);
                }
            }
        }
        return -1; 
    }

    public void actualizar(Auditor a) throws SQLException {
        String sql = "UPDATE auditores SET nombre_completo = ?, cedula_profesional = ?, email = ? WHERE id_auditor = ?";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, a.getNombreCompleto());
            ps.setString(2, a.getCelulaProfesional()); 
            ps.setString(3, a.getEmail());
            ps.setInt(4, a.getIdAuditor());
            ps.executeUpdate();
        } // Se corrigió el cierre de llaves aquí
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM auditores WHERE id_auditor = ?";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}