package utng.gtid232.adla.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utng.gtid232.adla.conexion.ConexionBD; 
import utng.gtid232.adla.modelo.Empresa;

public class EmpresasDAO {

    public List<Empresa> listar() {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT id_empresa, razon_social, rfc, sector, num_empleados, contacto_email FROM empresas ORDER BY razon_social";        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Empresa e = new Empresa(
                    rs.getInt("id_empresa"),
                    rs.getString("razon_social"),
                    rs.getString("rfc"),
                    rs.getString("sector"),
                    rs.getString("num_empleados"), 
                    rs.getString("contacto_email")
                );
                lista.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertar(Empresa e) {
        String sql = "INSERT INTO empresas (razon_social, rfc, sector, num_empleados, contacto_email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, e.getRazonsocial());
            ps.setString(2, e.getRfc());
            ps.setString(3, e.getSector());
            
            int numEmpleados = 0;
            if (e.getNombreEmpleados() != null && !e.getNombreEmpleados().trim().isEmpty()) {
                numEmpleados = Integer.parseInt(e.getNombreEmpleados().trim());
            }
            ps.setInt(4, numEmpleados); 
            
            ps.setString(5, e.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } catch (NumberFormatException nfe) {
            System.err.println("Error: El número de empleados '" + e.getNombreEmpleados() + "' no es un número válido.");
            nfe.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Empresa e) {
        String sql = "UPDATE empresas SET razon_social=?, rfc=?, sector=?, num_empleados=?, contacto_email=? WHERE id_empresa=?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, e.getRazonsocial());
            ps.setString(2, e.getRfc());
            ps.setString(3, e.getSector());
            
            int numEmpleados = 0;
            if (e.getNombreEmpleados() != null && !e.getNombreEmpleados().trim().isEmpty()) {
                numEmpleados = Integer.parseInt(e.getNombreEmpleados().trim());
            }
            ps.setInt(4, numEmpleados);
            
            ps.setString(5, e.getEmail());
            ps.setInt(6, e.getIdEmpresa());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } catch (NumberFormatException nfe) {
            System.err.println("Error: El número de empleados '" + e.getNombreEmpleados() + "' no es un número válido.");
            nfe.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM empresas WHERE id_empresa = ?";
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