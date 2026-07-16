package utng.gtid232.adla.reporte;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import utng.gtid232.adla.dao.EvaluacionDAO;
import utng.gtid232.adla.modelo.Evaluacion;

import org.apache.poi.xwpf.usermodel.*;

public class ReporteWord {

    public static void generarReporte(String rutaArchivo, int idAuditoria, String nombreEmpresa) throws IOException {
        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        List<Evaluacion> evaluaciones = evaluacionDAO.listarPorAuditoria(idAuditoria);

        XWPFDocument document = new XWPFDocument();

        XWPFParagraph titulo = document.createParagraph();
        titulo.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun tituloRun = titulo.createRun();
        tituloRun.setText("INFORME DE AUDITORÍA DE IGUALDAD LABORAL");
        tituloRun.setBold(true);
        tituloRun.setFontSize(18);
        tituloRun.setFontFamily("Arial");

        XWPFParagraph subtitulo = document.createParagraph();
        subtitulo.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun subRun = subtitulo.createRun();
        subRun.setText("Empresa Auditada: " + nombreEmpresa + "\n");
        subRun.setText("ID de Auditoría: " + idAuditoria + "\n");
        subRun.setFontSize(12);
        subRun.setItalic(true);

        XWPFTable table = document.createTable();
        
        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("Pregunta / Criterio");
        headerRow.addNewTableCell().setText("Resultado");
        headerRow.addNewTableCell().setText("Observaciones");

        for (Evaluacion eval : evaluaciones) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(eval.getDescripcionCriterio() != null ? eval.getDescripcionCriterio() : "");
            
            boolean cumple = "Cumple".equalsIgnoreCase(eval.getCumplimiento());
            row.getCell(1).setText(cumple ? "Cumple" : "No Cumple");
            row.getCell(2).setText(eval.getObservaciones() != null ? eval.getObservaciones() : "Sin observaciones");
        }

        try (FileOutputStream out = new FileOutputStream(rutaArchivo)) {
            document.write(out);
        }
        document.close();
    }
}