package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.modelo.Empresa;
import utng.gtid232.adla.dao.EmpresasDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpresasController implements Initializable {

    @FXML private TableView<Empresa> tblEmpresas;
    @FXML private TableColumn<Empresa, String> colRazonSocial;
    @FXML private TableColumn<Empresa, String> colRfc;
    @FXML private TableColumn<Empresa, String> colSector;
    @FXML private TableColumn<Empresa, String> colEmpleados;
    @FXML private TableColumn<Empresa, String> colEmail;

    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtRfc;
    @FXML private TextField txtSector;
    @FXML private Spinner<Integer> spnEmpleados;
    @FXML private TextField txtEmail;

    private ObservableList<Empresa> listaEmpresas;
    private final EmpresasDAO empresaDAO = new EmpresasDAO();
    private Empresa empresaSeleccionada = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 1);
        spnEmpleados.setValueFactory(valueFactory);

        configurarColumnas();
        cargarEmpresas();

        tblEmpresas.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            if (nuevo != null) {
                empresaSeleccionada = nuevo;
                mostrarEmpresa(nuevo);
            }
        });
    }

    private void configurarColumnas() {
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("Razonsocial")); 
        colRfc.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        colSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
        colEmpleados.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleados")); 
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void cargarEmpresas() {
        listaEmpresas = FXCollections.observableArrayList(empresaDAO.listar());
        tblEmpresas.setItems(listaEmpresas);
    }

    private void mostrarEmpresa(Empresa e) {
        txtRazonSocial.setText(e.getRazonsocial()); 
        txtRfc.setText(e.getRfc());
        txtSector.setText(e.getSector());

        int numEmpleados = 0;
        try {
            if (e.getNombreEmpleados() != null && !e.getNombreEmpleados().trim().isEmpty()) { 
                numEmpleados = Integer.parseInt(e.getNombreEmpleados().trim());
            }
        } catch (NumberFormatException ex) {
            numEmpleados = 0;
        }
        spnEmpleados.getValueFactory().setValue(numEmpleados);

        txtEmail.setText(e.getEmail());
    }

    @FXML
    private void guardarEmpresa() {
        String razon = txtRazonSocial.getText();
        String rfc = txtRfc.getText();
        String sector = txtSector.getText();
        String empleados = String.valueOf(spnEmpleados.getValue()); 
        String email = txtEmail.getText();

        if (razon.isEmpty() || rfc.isEmpty()) {
            mostrarAlerta("Campos Requeridos", "Razón Social y RFC son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        if (empresaSeleccionada == null) {
            Empresa nueva = new Empresa(0, razon, rfc, sector, empleados, email); 
            if (empresaDAO.insertar(nueva)) {
                mostrarAlerta("Éxito", "Empresa registrada con éxito.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                cargarEmpresas();
            }
        } else {
            empresaSeleccionada.setRazonsocial(razon);
            empresaSeleccionada.setRfc(rfc);
            empresaSeleccionada.setSector(sector);
            empresaSeleccionada.setNombreEmpleados(empleados);
            empresaSeleccionada.setEmail(email);

            if (empresaDAO.actualizar(empresaSeleccionada)) {
                mostrarAlerta("Éxito", "Empresa actualizada con éxito.", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                cargarEmpresas();
            }
        }
    }

    @FXML
    private void eliminarEmpresa() {
        if (empresaSeleccionada == null) {
            mostrarAlerta("Selección requerida", "Por favor, selecciona una empresa de la lista.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de eliminar esta empresa?", ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (empresaDAO.eliminar(empresaSeleccionada.getIdEmpresa())) { 
                    mostrarAlerta("Éxito", "Empresa eliminada.", Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                    cargarEmpresas();
                }
            }
        });
    }

    @FXML
    private void limpiarFormulario() {
        txtRazonSocial.clear();
        txtRfc.clear();
        txtSector.clear();
        spnEmpleados.getValueFactory().setValue(0);
        txtEmail.clear();
        empresaSeleccionada = null;
        tblEmpresas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}