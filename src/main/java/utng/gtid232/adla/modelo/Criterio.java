package utng.gtid232.adla.modelo;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Criterio {
    private int idCriterio;
    private int idCategoria;
    private String descripcion;
    private double peso; 
    private String nombreCategoria;
    
    private final StringProperty cumplimientoSeleccionado = new SimpleStringProperty("Cumple");
    protected final StringProperty ObservacionesCapturadas = new SimpleStringProperty("");

    public Criterio(int idCriterio, int idCategoria, String descripcion, double peso) {
        this.idCriterio = idCriterio;
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.peso = peso;
    }

    public int getIdCriterio() {
        return idCriterio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public String getCumplimientoSeleccionado() {
        return cumplimientoSeleccionado.get();
    }

    public void setCumplimientoSeleccionado(String cumplimiento) {
        this.cumplimientoSeleccionado.set(cumplimiento);
    }

    public StringProperty cumplimientoSeleccionadoProperty() {
        return cumplimientoSeleccionado;
    }

    public String getObservacionesCapturadas() {
        return ObservacionesCapturadas.get();
    }

    public void setObservacionesCapturadas(String observaciones) {
        this.ObservacionesCapturadas.set(observaciones);
    }

    public StringProperty observacionesCapturadasProperty() {
        return ObservacionesCapturadas;
    }

    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}