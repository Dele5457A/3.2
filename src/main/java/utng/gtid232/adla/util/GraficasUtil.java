package utng.gtid232.adla.util;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import utng.gtid232.adla.modelo.Evaluacion;

public class GraficasUtil {

    public static ObservableList<PieChart.Data> generarGraficaPastel(List<Evaluacion> evaluaciones) {
        int cumple = 0;
        int noCumple = 0;

        for (Evaluacion eval : evaluaciones) {
            if ("Cumple".equalsIgnoreCase(eval.getCumplimiento())) {
                cumple++;
            } else {
                noCumple++;
            }
        }

        ObservableList<PieChart.Data> datosGrafica = FXCollections.observableArrayList();
        
        if (cumple > 0 || noCumple > 0) {
            datosGrafica.add(new PieChart.Data("Cumple (" + cumple + ")", cumple));
            datosGrafica.add(new PieChart.Data("No Cumple (" + noCumple + ")", noCumple));
        } else {
            datosGrafica.add(new PieChart.Data("Sin Datos", 0));
        }

        return datosGrafica;
    }
}