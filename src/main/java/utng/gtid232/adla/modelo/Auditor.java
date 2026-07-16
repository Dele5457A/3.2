package utng.gtid232.adla.modelo;

public class Auditor {
    private int idAuditor;
    private String nombreCompleto; 
    private String celulaProfesional;
    private String email;

    public Auditor() {
    }

    public Auditor(int idAuditor, String nombreCompleto, String celulaProfesional, String email) {
        this.idAuditor = idAuditor;
        this.nombreCompleto = nombreCompleto;
        this.celulaProfesional = celulaProfesional;
        this.email = email;
    }

    public int getIdAuditor() { 
        return idAuditor; 
    }
    public void setIdAuditor(int idAuditor) { 
        this.idAuditor = idAuditor; 
    }

    public String getNombreCompleto() { 
        return nombreCompleto; 
    }
    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }

    public String getCelulaProfesional() { 
        return celulaProfesional; 
    }
    // Corregido a 'setCelulaProfesional' para que coincida con el atributo
    public void setCelulaProfesional(String celulaProfesional) { 
        this.celulaProfesional = celulaProfesional; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
}