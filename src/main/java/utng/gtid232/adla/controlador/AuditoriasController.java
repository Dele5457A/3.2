package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.AuditoriaDAO;
import utng.gtid232.adla.dao.AuditorDAO;
import utng.gtid232.adla.dao.EmpresasDAO;
import utng.gtid232.adla.modelo.Auditoria;
import utng.gtid232.adla.modelo.Auditor;
import utng.gtid232.adla.modelo.Empresa;

import javafx.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AuditoriasController {

    // --- Componentes de la vista de Auditorías ---
    @FXML private TableView<Auditoria> tblAuditorias;
    @FXML private TableColumn<Auditoria, Integer> colId;
    @FXML private TableColumn<Auditoria, String> colEmpresa;
    @FXML private TableColumn<Auditoria, String> colAuditor;
    @FXML private TableColumn<Auditoria, LocalDate> colFechaInicio;
    @FXML private TableColumn<Auditoria, LocalDate> colFechaFin;
    @FXML private TableColumn<Auditoria, String> colEstatus;

    @FXML private ComboBox<Empresa> cmbEmpresa;
    @FXML private ComboBox<Auditor> cmbAuditor;
    @FXML private DatePicker dpFechaInicio;

    @FXML private ComboBox<Auditoria> cmbAuditoria;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TableView<Object> tblCriterios; 
    @FXML private TableColumn<Object, String> colDescripcion;
    @FXML private TableColumn<Object, Double> colPeso;
    @FXML private TableColumn<Object, String> colCumplimiento;
    @FXML private TableColumn<Object, String> colObservaciones;

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final EmpresasDAO empresaDAO = new EmpresasDAO();
    private final AuditorDAO auditorDAO = new AuditorDAO();

    private ObservableList<Auditoria> listaAuditorias;
    private Auditoria auditoriaSeleccionada;

    @FXML
    public void initialize() {
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("idAuditoria"));
        if (colEmpresa != null) colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        if (colAuditor != null) colAuditor.setCellValueFactory(new PropertyValueFactory<>("nombreAuditor"));
        if (colFechaInicio != null) colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        if (colFechaFin != null) colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        if (colEstatus != null) colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        cargarCatalogos();
        cargarDatos();

        if (tblAuditorias != null) {
            tblAuditorias.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    auditoriaSeleccionada = newSelection;
                    seleccionarEmpresaEnCombo(auditoriaSeleccionada.getIdempresa());
                    seleccionarAuditorEnCombo(auditoriaSeleccionada.getIdauditor());
                    if (dpFechaInicio != null) {
                        dpFechaInicio.setValue(auditoriaSeleccionada.getFechaInicio());
                    }
                }
            });
        }
    }

    private void cargarCatalogos() {
        if (cmbEmpresa != null) {
            cmbEmpresa.setItems(FXCollections.observableArrayList(empresaDAO.listar()));
        }
        try {
            if (cmbAuditor != null) {
                cmbAuditor.setItems(FXCollections.observableArrayList(auditorDAO.listarTodos()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatos() {
        List<Auditoria> lista = auditoriaDAO.listar();
        listaAuditorias = FXCollections.observableArrayList(lista);
        if (tblAuditorias != null) {
            tblAuditorias.setItems(listaAuditorias);
        }
    }


    @FXML
    private void cargarCriterios(ActionEvent event) {
        System.out.println("Botón 'Cargar criterios' presionado.");
    }

    @FXML
    private void guardarEvaluacion(ActionEvent event) {
        System.out.println("Botón 'Guardar evaluación' presionado.");
    }


    @FXML
    private void crearAuditoria(ActionEvent event) {
        Empresa emp = cmbEmpresa.getValue();
        Auditor aud = cmbAuditor.getValue();
        LocalDate inicio = dpFechaInicio.getValue();

        if (emp == null || aud == null || inicio == null) {
            mostrarAlerta("Campos vacíos", "Completa los campos obligatorios (Empresa, Auditor y Fecha de inicio).", Alert.AlertType.WARNING);
            return;
        }

        Auditoria nueva = new Auditoria(0, emp.getIdEmpresa(), aud.getIdAuditor(), inicio, null, "Programada");
        int id = auditoriaDAO.insertar(nueva);
        if (id > 0) {
            cargarDatos();
            mostrarAlerta("Éxito", "Auditoría programada correctamente.", Alert.AlertType.INFORMATION);
            limpiar();
        }
    }

    @FXML
    private void concluirAuditoria(ActionEvent event) {
        if (auditoriaSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una auditoría de la tabla primero.", Alert.AlertType.WARNING);
            return;
        }

        auditoriaSeleccionada.setFechaFin(LocalDate.now());
        auditoriaSeleccionada.setEstatus("Completada");

        if (audiaActualizar(auditoriaSeleccionada)) {
            cargarDatos();
            mostrarAlerta("Éxito", "La auditoría ha sido concluida.", Alert.AlertType.INFORMATION);
            limpiar();
        }
    }

    @FXML
    private void cancelarAuditoria(ActionEvent event) {
        if (auditoriaSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una auditoría de la tabla primero.", Alert.AlertType.WARNING);
            return;
        }

        auditoriaSeleccionada.setEstatus("Cancelada");

        if (audiaActualizar(auditoriaSeleccionada)) {
            cargarDatos();
            mostrarAlerta("Éxito", "La auditoría ha sido cancelada.", Alert.AlertType.INFORMATION);
            limpiar();
        }
    }

    @FXML
    private void eliminarAuditoria(ActionEvent event) {
        if (auditoriaSeleccionada == null) {
            mostrarAlerta("Atención", "Selecciona una auditoría de la tabla primero.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de eliminar esta auditoría?", ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (auditoriaDAO.eliminar(auditoriaSeleccionada.getIdAuditoria())) {
                    listaAuditorias.remove(auditoriaSeleccionada);
                    mostrarAlerta("Éxito", "Auditoría eliminada de la base de datos.", Alert.AlertType.INFORMATION);
                    limpiar();
                }
            }
        });
    }

    private boolean audiaActualizar(Auditoria aud) {
        return auditoriaDAO.actualizar(aud);
    }

    private void limpiar() {
        if (cmbEmpresa != null) cmbEmpresa.setValue(null);
        if (cmbAuditor != null) cmbAuditor.setValue(null);
        if (dpFechaInicio != null) dpFechaInicio.setValue(null);
        auditoriaSeleccionada = null;
        if (tblAuditorias != null) tblAuditorias.getSelectionModel().clearSelection();
    }

    private void seleccionarEmpresaEnCombo(int idEmpresa) {
        if (cmbEmpresa == null) return;
        for (Empresa e : cmbEmpresa.getItems()) {
            if (e.getIdEmpresa() == idEmpresa) {
                cmbEmpresa.setValue(e);
                break;
            }
        }
    }

    private void seleccionarAuditorEnCombo(int idAuditor) {
        if (cmbAuditor == null) return;
        for (Auditor a : cmbAuditor.getItems()) {
            if (a.getIdAuditor() == idAuditor) {
                cmbAuditor.setValue(a);
                break;
            }
        }
    }

    private void mostrarAlerta(String t, String m, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(t);
        alert.setHeaderText(null);
        alert.setContentText(m);
        alert.showAndWait();
    }
}