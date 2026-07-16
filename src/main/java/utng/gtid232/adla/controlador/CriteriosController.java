package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.CategoriaDAO;
import utng.gtid232.adla.dao.CriterioDAO;
import utng.gtid232.adla.modelo.Categoria;
import utng.gtid232.adla.modelo.Criterio;

import java.util.List;

public class CriteriosController {

    @FXML private ComboBox<Categoria> cbCategoriaFiltro;
    @FXML private TableView<Criterio> tvCriterios;
    @FXML private TableColumn<Criterio, Integer> colId;
    @FXML private TableColumn<Criterio, String> colCategoria;
    @FXML private TableColumn<Criterio, String> colDescripcion;
    @FXML private TableColumn<Criterio, Double> colPeso;

    private final CriterioDAO criterioDAO = new CriterioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCriterio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        cargarCategorias();
        cargarCriteriosCompletos();

        // Filtrar automáticamente al seleccionar una categoría del ComboBox
        cbCategoriaFiltro.setOnAction(event -> {
            Categoria seleccionada = cbCategoriaFiltro.getValue();
            if (seleccionada != null) {
                List<Criterio> filtrados = criterioDAO.listarPorCategoria(seleccionada.getIdCategoria());
                tvCriterios.setItems(FXCollections.observableArrayList(filtrados));
            }
        });
    }

    private void cargarCategorias() {
        List<Categoria> categorias = categoriaDAO.listar();
        cbCategoriaFiltro.setItems(FXCollections.observableArrayList(categorias));
    }

    private void cargarCriteriosCompletos() {
        List<Criterio> todos = criterioDAO.listarTodos();
        tvCriterios.setItems(FXCollections.observableArrayList(todos));
    }

    @FXML
    private void handleMostrarTodos() {
        cbCategoriaFiltro.setValue(null);
        cargarCriteriosCompletos();
    }
}