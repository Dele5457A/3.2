package utng.gtid232.adla.modelo;

import java.time.LocalDate; 


public class Auditoria {
    private int idAuditoria;
    private int idempresa;
    private int idauditor;
    private LocalDate fechaInicio; 
    private LocalDate fechaFin;
    private String estatus;

    private String nombreEmpresa;
    private String nombreAuditor;

    public Auditoria(int idAuditoria, int idempresa, int idauditor, LocalDate fechaInicio, LocalDate fechaFin, String estatus) {
        this.idAuditoria = idAuditoria;
        this.idempresa = idempresa;
        this.idauditor = idauditor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estatus = estatus;
    }
    public int getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(int idAuditoria) { this.idAuditoria = idAuditoria; }

    public int getIdempresa() { return idempresa; }
    public void setIdempresa(int idempresa) { this.idempresa = idempresa; }

    public int getIdauditor() { return idauditor; }
    public void setIdauditor(int idauditor) { this.idauditor = idauditor; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getNombreAuditor() { return nombreAuditor; }
    public void setNombreAuditor(String nombreAuditor) { this.nombreAuditor = nombreAuditor; }

    @Override
    public String toString() {
        return "Auditoria #" + idAuditoria + 
               " - Empresa: " + nombreEmpresa + 
               " - Auditor: " + nombreAuditor + 
               " - Fecha Inicio: " + fechaInicio + 
               " - Fecha Fin: " + fechaFin + 
               " - Estatus: " + estatus;
    }
}