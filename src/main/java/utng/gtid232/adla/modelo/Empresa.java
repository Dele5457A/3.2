package utng.gtid232.adla.modelo;

public class Empresa {
    private int idEmpresa;
    private String Razonsocial;
    private String rfc;
    private String sector;
    private String nombreEmpleados;
    private String email;

    public Empresa() {
    }

    public Empresa(int idEmpresa, String razonsocial, String rfc, String sector, String nombreEmpleados, String email) {
        this.idEmpresa = idEmpresa;
        this.Razonsocial = razonsocial;
        this.rfc = rfc;
        this.sector = sector;
        this.nombreEmpleados = nombreEmpleados;
        this.email = email;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    public String getRazonsocial() {
        return Razonsocial;
    }
    public void setRazonsocial(String razonsocial) {
        this.Razonsocial = razonsocial;
    }
    public String getRfc() {
        return rfc;
    }
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public String getNombreEmpleados() {
        return nombreEmpleados;
    }
    public void setNombreEmpleados(String nombreEmpleados) {
        this.nombreEmpleados = nombreEmpleados;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return Razonsocial;
    }
}