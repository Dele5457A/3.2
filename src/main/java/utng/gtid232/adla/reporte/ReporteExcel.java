package utng.gtid232.adla.reporte;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import utng.gtid232.adla.dao.EvaluacionDAO;
import utng.gtid232.adla.modelo.Evaluacion;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReporteExcel {

    public static void generarReporte(String rutaArchivo, int idAuditoria) throws IOException {
        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
        List<Evaluacion> evaluaciones = evaluacionDAO.listarPorAuditoria(idAuditoria);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Resultados de Auditoría");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        String[] columnas = {"ID Evaluación", "Pregunta", "Cumple", "Observaciones"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Evaluacion eval : evaluaciones) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(eval.getIdEvaluacion());
            row.createCell(1).setCellValue(eval.getDescripcionCriterio() != null ? eval.getDescripcionCriterio() : "");
            
            boolean cumple = "Cumple".equalsIgnoreCase(eval.getCumplimiento());
            row.createCell(2).setCellValue(cumple ? "SÍ" : "NO");
            row.createCell(3).setCellValue(eval.getObservaciones() != null ? eval.getObservaciones() : "");

            Cell cumpleCell = row.getCell(2);
            CellStyle cumpleStyle = workbook.createCellStyle();
            Font cumpleFont = workbook.createFont();
            cumpleFont.setBold(true);
            
            if (cumple) {
                cumpleFont.setColor(IndexedColors.GREEN.getIndex());
            } else {
                cumpleFont.setColor(IndexedColors.RED.getIndex());
            }
            cumpleStyle.setFont(cumpleFont);
            cumpleCell.setCellStyle(cumpleStyle);
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}