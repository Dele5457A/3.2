package utng.gtid232.adla.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {

    /**
     * Este método se ejecuta automáticamente cuando JavaFX termina de cargar el FXML.
     * Es ideal para inicializar datos, gráficas o conectar con la base de datos.
     */
    @FXML
    public void initialize() {
        System.out.println("Controlador de Dashboard inicializado con éxito.");
    }

    // =========================================================================
    // --- MÉTODOS DE ACCIÓN REQUERIDOS POR EL FXML (onAction) ---
    // =========================================================================

    /**
     * Actualiza las estadísticas, métricas y datos generales que se muestran 
     * en el panel principal del Dashboard.
     */
    @FXML
    private void actualizarDashboard(ActionEvent event) {
        System.out.println("Ejecutando: actualizarDashboard -> Cargando métricas actualizadas desde la base de datos.");
        // TODO: Agregar lógica de consulta y renderizado de datos reales
    }

    /**
     * Genera y exporta el reporte de hallazgos y auditorías en formato PDF.
     */
    @FXML
    private void generarReportePDF(ActionEvent event) {
        System.out.println("Ejecutando: generarReportePDF -> Generando documento .pdf con iText.");
        // TODO: Enlazar con el generador de PDF de la aplicación
    }

    /**
     * Genera y exporta el reporte de hallazgos y auditorías en formato de Word.
     */
    @FXML
    private void generarReporteWord(ActionEvent event) {
        System.out.println("Ejecutando: generarReporteWord -> Generando documento .docx con Apache POI.");
        // TODO: Enlazar con el exportador de MS Word
    }

    /**
     * Genera y exporta la matriz de hallazgos y auditorías a una hoja de cálculo Excel.
     */
    @FXML
    private void generarReporteExcel(ActionEvent event) {
        System.out.println("Ejecutando: generarReporteExcel -> Generando libro .xlsx con Apache POI.");
        // TODO: Enlazar con la exportación estructurada a Excel
    }

    // =========================================================================
    // --- MÉTODOS AUXILIARES Y DE RESPALDO (Por si la vista los implementa) ---
    // =========================================================================

    /**
     * Método alternativo para limpiar filtros o reestablecer la vista del Dashboard.
     */
    @FXML
    private void refrescarDatos(ActionEvent event) {
        System.out.println("Ejecutando: refrescarDatos -> Limpiando caché y refrescando paneles.");
        actualizarDashboard(event);
    }

    /**
     * Envía la vista actual del reporte directamente a la cola de impresión del sistema.
     */
    @FXML
    private void imprimirReporte(ActionEvent event) {
        System.out.println("Ejecutando: imprimirReporte -> Preparando vista de impresión.");
    }
}