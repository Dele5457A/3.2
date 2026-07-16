package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.AuditoriaDAO;
import utng.gtid232.adla.dao.CriterioDAO;
import utng.gtid232.adla.dao.EvaluacionDAO;
import utng.gtid232.adla.modelo.Auditoria;
import utng.gtid232.adla.modelo.Criterio;
import utng.gtid232.adla.modelo.Evaluacion;

import java.util.List;

public class EvaluacionesController {

    @FXML private ComboBox<Auditoria> cbAuditoria;
    @FXML private TableView<Criterio> tvChecklist;
    @FXML private TableColumn<Criterio, String> colCategoria;
    @FXML private TableColumn<Criterio, String> colCriterio;
    @FXML private TableColumn<Criterio, Double> colPeso;

    @FXML private ComboBox<String> cbCumplimiento;
    @FXML private TextArea txtObservaciones;
    @FXML private TextField txtEvidencia;
    @FXML private Button btnGuardarEvaluacion;
    

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final CriterioDAO criterioDAO = new CriterioDAO();
    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAO();

    private Criterio criterioSeleccionado;

    @FXML
    public void initialize() {
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colCriterio.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        cbCumplimiento.setItems(FXCollections.observableArrayList("Cumple", "No Cumple", "No Aplica"));

        cargarAuditorias();

        cbAuditoria.setOnAction(e -> cargarChecklist());

        tvChecklist.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                criterioSeleccionado = newSel;
                cargarDatosDeEvaluacionPrevia(newSel.getIdCriterio());
            }
        });
    }

    private void cargarAuditorias() {
        cbAuditoria.setItems(FXCollections.observableArrayList(auditoriaDAO.listar()));
    }

    private void cargarChecklist() {
        if (cbAuditoria.getValue() != null) {
            List<Criterio> criterios = criterioDAO.listarTodos();
            tvChecklist.setItems(FXCollections.observableArrayList(criterios));
        }
    }

    private void cargarDatosDeEvaluacionPrevia(int idCriterio) {
        Auditoria aud = cbAuditoria.getValue();
        if (aud != null) {
            List<Evaluacion> previas = evaluacionDAO.listarPorAuditoria(aud.getIdAuditoria());
            for (Evaluacion ev : previas) {
                if (ev.getIdCriterio() == idCriterio) {
                    cbCumplimiento.setValue(ev.getCumplimiento());
                    txtObservaciones.setText(ev.getObservaciones());
                    txtEvidencia.setText(ev.getEvidenciaRef());
                    return;
                }
            }
            cbCumplimiento.setValue("Cumple");
            txtObservaciones.clear();
            txtEvidencia.clear();
        }
    }

    @FXML
    private void handleGuardarEvaluacion() {
        Auditoria aud = cbAuditoria.getValue();
        if (aud == null || criterioSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona una Auditoría y un Criterio de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        Evaluacion ev = new Evaluacion(
            aud.getIdAuditoria(),
            criterioSeleccionado.getIdCriterio(),
            cbCumplimiento.getValue(),
            txtObservaciones.getText().trim(),
            txtEvidencia.getText().trim()
        );

        if (evaluacionDAO.guardarOActualizar(ev)) {
            mostrarAlerta("Éxito", "Evaluación guardada correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo guardar la evaluación.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String t, String m, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
    public class CapturaEvaluacionController {
    @FXML
    public void initialize() {
    }
}
}