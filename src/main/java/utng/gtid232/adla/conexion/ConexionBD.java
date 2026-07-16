package utng.gtid232.adla.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Punto único de acceso a la conexión de base de datos
 * JDBC hacia PostgreSQL usando el patrón Singleton.
 */
public class ConexionBD {
    private static ConexionBD instancia;

    // CORREGIDO: "postgresql" bien escrito
    private static final String URL = "jdbc:postgresql://localhost:5432/auditoriasdb";
    private static final String USUARIO = "admin";
    private static final String PASSWORD = "Dele5457";

    private Connection conexion;

    // Constructor privado para evitar instanciación externa
    private ConexionBD() throws SQLException {
        conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // CORREGIDO: Llave de cierre agregada al final del método
    public static synchronized ConexionBD getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia; // El return debe ir fuera del IF para que siempre devuelva la instancia
    }

    public Connection getConexion() {
        return conexion;
    }

    // CORREGIDO: Ahora sí cierra la conexión físicamente
    public static void cerrar() {
        try {
            if (instancia != null && instancia.conexion != null && !instancia.conexion.isClosed()) {
                instancia.conexion.close();
                System.out.println("Conexión cerrada exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}