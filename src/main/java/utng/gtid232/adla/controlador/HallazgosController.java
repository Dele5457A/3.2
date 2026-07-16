package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.AuditoriaDAO;
import utng.gtid232.adla.modelo.Auditoria;

public class HallazgosController {

    @FXML private ComboBox<Auditoria> cmbAuditorias;
    @FXML private TableView<Object> tblHallazgos; 
    @FXML private TableColumn<Object, Integer> colId;
    @FXML private TableColumn<Object, String> colDetalle;
    @FXML private TableColumn<Object, String> colGravedad;
    @FXML private TableColumn<Object, String> colEstatus;

    @FXML private TextArea txtDetalle;
    @FXML private ComboBox<String> cmbGravedad;
    @FXML private ComboBox<String> cmbEstatus;

    @FXML private TextArea txtPlanAccion;
    @FXML private DatePicker dpFechaCompromiso;

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    @FXML
    public void initialize() {
        System.out.println("Inicializando HallazgosController...");

        if (cmbAuditorias != null) {
            cmbAuditorias.setItems(FXCollections.observableArrayList(auditoriaDAO.listar()));
        }

        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("idHallazgo"));
        if (colDetalle != null) colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        if (colGravedad != null) colGravedad.setCellValueFactory(new PropertyValueFactory<>("gravedad"));
        if (colEstatus != null) colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        if (cmbGravedad != null) {
            cmbGravedad.setItems(FXCollections.observableArrayList("Crítica", "Mayor", "Menor", "Observación"));
        }
        if (cmbEstatus != null) {
            cmbEstatus.setItems(FXCollections.observableArrayList("Abierto", "En Proceso", "Solucionado"));
        }
    }


    @FXML
    private void cargarDatosAuditoria(ActionEvent event) {
        System.out.println("Ejecutando cargarDatosAuditoria...");
        if (cmbAuditorias != null && cmbAuditorias.getValue() != null) {
            Auditoria seleccionada = cmbAuditorias.getValue();
            System.out.println("Auditoría seleccionada ID: " + seleccionada.getIdAuditoria());
        } else {
            System.out.println("No se ha seleccionado ninguna auditoría.");
        }
    }

    @FXML
    private void registrarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando registrarHallazgo...");
    }

    @FXML
    private void guardarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando guardarHallazgo...");
        registrarHallazgo(event);
    }

    @FXML
    private void guardarPlanAccion(ActionEvent event) {
        System.out.println("Ejecutando guardarPlanAccion...");
    }

    @FXML
    private void registrarPlanAccion(ActionEvent event) {
        System.out.println("Ejecutando registrarPlanAccion...");
        guardarPlanAccion(event);
    }

    @FXML
    private void modificarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando modificarHallazgo...");
    }

    @FXML
    private void editarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando editarHallazgo...");
    }

    @FXML
    private void actualizarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando actualizarHallazgo...");
    }

    @FXML
    private void eliminarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando eliminarHallazgo...");
    }

    @FXML
    private void borrarHallazgo(ActionEvent event) {
        System.out.println("Ejecutando borrarHallazgo...");
    }

    @FXML
    private void limpiarFormulario(ActionEvent event) {
        System.out.println("Limpiando formulario de hallazgos...");
        if (txtDetalle != null) txtDetalle.clear();
        if (cmbGravedad != null) cmbGravedad.setValue(null);
        if (cmbEstatus != null) cmbEstatus.setValue(null);
        if (txtPlanAccion != null) txtPlanAccion.clear();
        if (dpFechaCompromiso != null) dpFechaCompromiso.setValue(null);
        if (tblHallazgos != null) tblHallazgos.getSelectionModel().clearSelection();
    }
}