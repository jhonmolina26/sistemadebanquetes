package models;

public class Anfitrion {
    private int id;
    private String nombre;
    private String empresa;
    private String documento;
    private String correo;
    private String telefono;
    private String segmento;
    private boolean vip;
    private String proximoEvento;

    public Anfitrion() {}

    public Anfitrion(int id, String nombre, String empresa, String documento, String correo,
                     String telefono, String segmento, boolean vip, String proximoEvento) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.documento = documento;
        this.correo = correo;
        this.telefono = telefono;
        this.segmento = segmento;
        this.vip = vip;
        this.proximoEvento = proximoEvento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getSegmento() { return segmento; }
    public void setSegmento(String segmento) { this.segmento = segmento; }

    public boolean isVip() { return vip; }
    public void setVip(boolean vip) { this.vip = vip; }

    public String getProximoEvento() { return proximoEvento; }
    public void setProximoEvento(String proximoEvento) { this.proximoEvento = proximoEvento; }
}