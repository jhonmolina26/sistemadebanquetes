package models;

import java.sql.Date;

public class Evento {
    private int id;
    private String codigo;
    private String tipoEvento;
    private Date fecha;
    private int salonId;
    private String salonNombre;
    private int invitados;
    private String horario;
    private String paquete;
    private String contacto;
    private String servicios;
    private String estado;
    private int anfitrionId;
    private String anfitrionNombre;

    public Evento() {}

    // metodos getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getSalonId() { return salonId; }
    public void setSalonId(int salonId) { this.salonId = salonId; }

    public String getSalonNombre() { return salonNombre; }
    public void setSalonNombre(String salonNombre) { this.salonNombre = salonNombre; }

    public int getInvitados() { return invitados; }
    public void setInvitados(int invitados) { this.invitados = invitados; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getPaquete() { return paquete; }
    public void setPaquete(String paquete) { this.paquete = paquete; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public String getServicios() { return servicios; }
    public void setServicios(String servicios) { this.servicios = servicios; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getAnfitrionId() { return anfitrionId; }
    public void setAnfitrionId(int anfitrionId) { this.anfitrionId = anfitrionId; }

    public String getAnfitrionNombre() { return anfitrionNombre; }
    public void setAnfitrionNombre(String anfitrionNombre) { this.anfitrionNombre = anfitrionNombre; }
}