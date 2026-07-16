package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.AuditorDAO;
import utng.gtid232.adla.modelo.Auditor;

import java.sql.SQLException;
import java.util.List;

public class AuditoresController {

    @FXML private TableView<Auditor> tvAuditores;
    @FXML private TableColumn<Auditor, Integer> colId;
    @FXML private TableColumn<Auditor, String> colNombre;
    @FXML private TableColumn<Auditor, String> colCedula;
    @FXML private TableColumn<Auditor, String> colEmail;

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;

    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    private final AuditorDAO auditorDAO = new AuditorDAO();
    private ObservableList<Auditor> listaAuditores;
    private Auditor auditorSeleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAuditor"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("celulaProfesional"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        cargarDatos();

        tvAuditores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                auditorSeleccionado = newSelection;
                txtNombre.setText(auditorSeleccionado.getNombreCompleto());
                txtCedula.setText(auditorSeleccionado.getCelulaProfesional());
                txtEmail.setText(auditorSeleccionado.getEmail());
                btnEliminar.setDisable(false);
            }
        });
        
        btnEliminar.setDisable(true);
    }

    private void cargarDatos() {
        try {
            List<Auditor> lista = auditorDAO.listarTodos();
            listaAuditores = FXCollections.observableArrayList(lista);
            tvAuditores.setItems(listaAuditores);
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los auditores.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Coincide con onAction="#guardarAuditor" en el FXML
    @FXML
    private void guardarAuditor() {
        String nombre = txtNombre.getText().trim();
        String cedula = txtCedula.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombre.isEmpty() || cedula.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Completa todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (auditorSeleccionado == null) {
                Auditor nuevo = new Auditor(0, nombre, cedula, email);
                int idGenerado = auditorDAO.insertar(nuevo);
                if (idGenerado > 0) {
                    nuevo.setIdAuditor(idGenerado);
                    listaAuditores.add(nuevo);
                    mostrarAlerta("Éxito", "Auditor registrado.", Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                }
            } else {
                auditorSeleccionado.setNombreCompleto(nombre);
                auditorSeleccionado.setCelulaProfesional(cedula);
                auditorSeleccionado.setEmail(email);

                auditorDAO.actualizar(auditorSeleccionado);
                tvAuditores.refresh();
                mostrarAlerta("Éxito", "Auditor actualizado.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error BD", "Error al guardar.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Coincide con onAction="#eliminarAuditor" en el FXML
    @FXML
    private void eliminarAuditor() {
        if (auditorSeleccionado != null) {
            try {
                auditorDAO.eliminar(auditorSeleccionado.getIdAuditor());
                listaAuditores.remove(auditorSeleccionado);
                mostrarAlerta("Éxito", "Auditor eliminado.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
            } catch (SQLException e) {
                mostrarAlerta("Error BD", "No se pudo eliminar el registro.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
        txtCedula.clear();
        txtEmail.clear();
        auditorSeleccionado = null;
        tvAuditores.getSelectionModel().clearSelection();
        btnEliminar.setDisable(true);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}