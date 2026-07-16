package utng.gtid232.adla.reporte;

import java.io.FileNotFoundException;
import java.util.List;

import utng.gtid232.adla.dao.EvaluacionDAO;
import utng.gtid232.adla.modelo.Evaluacion;

import com.itextpdf.kernel.pdf.PdfDocument;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;

public class ReportePDF {

    public static void generarReporte(String rutaArchivo, int idAuditoria, String nombreEmpresa) throws FileNotFoundException {
        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        List<Evaluacion> evaluaciones = evaluacionDAO.listarPorAuditoria(idAuditoria);

        PdfWriter writer = new PdfWriter(rutaArchivo);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph titulo = new Paragraph("SISTEMA DE AUDITORÍA DE IGUALDAD LABORAL Y NO DISCRIMINACIÓN")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(titulo);

        Paragraph sub = new Paragraph("Reporte Ejecutivo de Evaluación - " + nombreEmpresa)
                .setFontSize(12)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(sub);

        float[] columnWidths = {300f, 80f, 150f};
        Table table = new Table(columnWidths);

        table.addHeaderCell(new Cell().add(new Paragraph("Pregunta Evaluada").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Estatus").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Observaciones").setBold()));

        for (Evaluacion eval : evaluaciones) {
            table.addCell(new Cell().add(new Paragraph(eval.getDescripcionCriterio() != null ? eval.getDescripcionCriterio() : "")));
            
            boolean cumple = "Cumple".equalsIgnoreCase(eval.getCumplimiento());
            String estatus = cumple ? "CUMPLE" : "NO CUMPLE";
            table.addCell(new Cell().add(new Paragraph(estatus)));
            
            String obs = eval.getObservaciones() != null ? eval.getObservaciones() : "-";
            table.addCell(new Cell().add(new Paragraph(obs)));
        }

        document.add(table);
        document.close();
    }
}