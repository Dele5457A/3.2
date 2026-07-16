package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.AuditoriaDAO;
import utng.gtid232.adla.dao.EvaluacionDAO;
import utng.gtid232.adla.dao.HallazgosDAO;
import utng.gtid232.adla.dao.PlanAccionDAO;
import utng.gtid232.adla.modelo.Auditoria;
import utng.gtid232.adla.modelo.Evaluacion;
import utng.gtid232.adla.modelo.Hallazgos;
import utng.gtid232.adla.modelo.PlanesAccion;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PlanesAccionController {

    @FXML private ComboBox<Auditoria> cbAuditorias;
    @FXML private TableView<Evaluacion> tvNoCumplimientos;
    @FXML private TableColumn<Evaluacion, String> colCriterio;
    @FXML private TableColumn<Evaluacion, String> colObservaciones;

    @FXML private ComboBox<String> cbTipoHallazgo;
    @FXML private ComboBox<String> cbRiesgo;
    @FXML private TextArea txtDescHallazgo;

    @FXML private TextField txtResponsable;
    @FXML private DatePicker dpCompromiso;
    @FXML private ComboBox<String> cbEstatusPlan;

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();
    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
    private final HallazgosDAO hallazgosDAO = new HallazgosDAO();
    private final PlanAccionDAO planDAO = new PlanAccionDAO();

    private Evaluacion evaluacionSeleccionada;

    @FXML
    public void initialize() {
        colCriterio.setCellValueFactory(new PropertyValueFactory<>("descripcionCriterio"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        cbTipoHallazgo.setItems(FXCollections.observableArrayList("No Conformidad Mayor", "No Conformidad Menor", "Oportunidad de Mejora"));
        cbRiesgo.setItems(FXCollections.observableArrayList("Alto", "Medio", "Bajo"));
        cbEstatusPlan.setItems(FXCollections.observableArrayList("Abierto", "En Proceso", "Cerrado"));

        cbAuditorias.setItems(FXCollections.observableArrayList(auditoriaDAO.listar()));

        cbAuditorias.setOnAction(e -> cargarNoCumplimientos());

        tvNoCumplimientos.getSelectionModel().selectedItemProperty().addListener((obs, o, newSelection) -> {
            if (newSelection != null) {
                evaluacionSeleccionada = newSelection;
                cargarHallazgoPrevio(newSelection.getIdEvaluacion());
            }
        });
    }

    private void cargarNoCumplimientos() {
        Auditoria aud = cbAuditorias.getValue();
        if (aud != null) {
            List<Evaluacion> listado = evaluacionDAO.listarPorAuditoria(aud.getIdAuditoria());
            List<Evaluacion> noCumple = listado.stream()
                .filter(ev -> "No Cumple".equalsIgnoreCase(ev.getCumplimiento()))
                .collect(Collectors.toList());
            tvNoCumplimientos.setItems(FXCollections.observableArrayList(noCumple));
        }
    }

    private void cargarHallazgoPrevio(int idEvaluacion) {
        List<Hallazgos> hallazgos = hallazgosDAO.listarPorEvaluacion(idEvaluacion);
        if (!hallazgos.isEmpty()) {
            Hallazgos h = hallazgos.get(0);
            cbTipoHallazgo.setValue(h.getTipo());
            cbRiesgo.setValue(h.getNivelRiesgo());
            txtDescHallazgo.setText(h.getDescripcion());
            
            List<PlanesAccion> planes = planDAO.listarPorHallazgo(h.getIdHallazgo());
            if (!planes.isEmpty()) {
                PlanesAccion p = planes.get(0);
                txtResponsable.setText(p.getResponsable());
                dpCompromiso.setValue(p.getFechaCompromiso());
                cbEstatusPlan.setValue(p.getEstatus());
            } else {
                limpiarPlanForm();
            }
        } else {
            cbTipoHallazgo.setValue(null);
            cbRiesgo.setValue(null);
            txtDescHallazgo.clear();
            limpiarPlanForm();
        }
    }

    private void limpiarPlanForm() {
        txtResponsable.clear();
        dpCompromiso.setValue(null);
        cbEstatusPlan.setValue(null);
    }

    @FXML
    private void handleGuardarPlan() {
        if (evaluacionSeleccionada == null) {
            mostrarAlerta("Error", "Selecciona una evaluación con 'No Cumple' de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        String tipoH = cbTipoHallazgo.getValue();
        String riesgo = cbRiesgo.getValue();
        String descH = txtDescHallazgo.getText().trim();

        String resp = txtResponsable.getText().trim();
        LocalDate compromiso = dpCompromiso.getValue();
        String estatusP = cbEstatusPlan.getValue();

        if (tipoH == null || riesgo == null || descH.isEmpty() || resp.isEmpty() || compromiso == null || estatusP == null) {
            mostrarAlerta("Campos vacíos", "Llena completamente el Hallazgo y su Plan de Acción.", Alert.AlertType.WARNING);
            return;
        }

        List<Hallazgos> existentes = hallazgosDAO.listarPorEvaluacion(evaluacionSeleccionada.getIdEvaluacion());
        int idHallazgo;
        if (existentes.isEmpty()) {
            Hallazgos h = new Hallazgos(evaluacionSeleccionada.getIdEvaluacion(), tipoH, riesgo, descH);
            idHallazgo = hallazgosDAO.insertar(h);
        } else {
            Hallazgos h = existentes.get(0);
            h.setTipo(tipoH);
            h.setNivelRiesgo(riesgo);
            h.setDescripcion(descH);
            hallazgosDAO.actualizar(h);
            idHallazgo = h.getIdHallazgo();
        }

        if (idHallazgo > 0) {
            List<PlanesAccion> planesExistentes = planDAO.listarPorHallazgo(idHallazgo);
            if (planesExistentes.isEmpty()) {
                PlanesAccion p = new PlanesAccion(idHallazgo, resp, compromiso, estatusP);
                planDAO.insertar(p);
            } else {
                PlanesAccion p = planesExistentes.get(0);
                p.setResponsable(resp);
                p.setFechaCompromiso(compromiso);
                p.setEstatus(estatusP);
                planDAO.actualizar(p);
            }
            mostrarAlerta("Éxito", "Hallazgo y Plan de Acción guardados con éxito.", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarAlerta(String t, String m, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(t);
        a.setContentText(m);
        a.showAndWait();
    }
}