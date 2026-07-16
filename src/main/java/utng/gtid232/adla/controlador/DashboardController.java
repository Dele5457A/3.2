package utng.gtid232.adla.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {

    
    @FXML
    public void initialize() {
        System.out.println("Controlador de Dashboard inicializado con éxito.");
    }

    
    @FXML
    private void actualizarDashboard(ActionEvent event) {
        System.out.println("Ejecutando: actualizarDashboard -> Cargando métricas actualizadas desde la base de datos.");
    }

    
    @FXML
    private void generarReportePDF(ActionEvent event) {
        System.out.println("Ejecutando: generarReportePDF -> Generando documento .pdf con iText.");
    }

   
    @FXML
    private void generarReporteWord(ActionEvent event) {
        System.out.println("Ejecutando: generarReporteWord -> Generando documento .docx con Apache POI.");
    }

    
    @FXML
    private void generarReporteExcel(ActionEvent event) {
        System.out.println("Ejecutando: generarReporteExcel -> Generando libro .xlsx con Apache POI.");
    }

   
    @FXML
    private void refrescarDatos(ActionEvent event) {
        System.out.println("Ejecutando: refrescarDatos -> Limpiando caché y refrescando paneles.");
        actualizarDashboard(event);
    }

    
    @FXML
    private void imprimirReporte(ActionEvent event) {
        System.out.println("Ejecutando: imprimirReporte -> Preparando vista de impresión.");
    }
}