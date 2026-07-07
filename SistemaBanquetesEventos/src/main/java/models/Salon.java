package model;

public class Salon {
    private int    idSalon;
    private String nombre;
    private int    capacidad;
    private String ubicacion;        // Interior | Jardin | Terraza | VIP
    private String tipoMontajePrincipal;
    private String estado;           // Disponible | Reservado | Mantenimiento
    private String descripcion;

    public Salon() {}

    public Salon(int idSalon, String nombre, int capacidad, String ubicacion,
                 String tipoMontajePrincipal, String estado, String descripcion) {
        this.idSalon             = idSalon;
        this.nombre              = nombre;
        this.capacidad           = capacidad;
        this.ubicacion           = ubicacion;
        this.tipoMontajePrincipal = tipoMontajePrincipal;
        this.estado              = estado;
        this.descripcion         = descripcion;
    }

    public int    getIdSalon()                    { return idSalon; }
    public void   setIdSalon(int v)               { this.idSalon = v; }
    public String getNombre()                     { return nombre; }
    public void   setNombre(String v)             { this.nombre = v; }
    public int    getCapacidad()                  { return capacidad; }
    public void   setCapacidad(int v)             { this.capacidad = v; }
    public String getUbicacion()                  { return ubicacion; }
    public void   setUbicacion(String v)          { this.ubicacion = v; }
    public String getTipoMontajePrincipal()       { return tipoMontajePrincipal; }
    public void   setTipoMontajePrincipal(String v){ this.tipoMontajePrincipal = v; }
    public String getEstado()                     { return estado; }
    public void   setEstado(String v)             { this.estado = v; }
    public String getDescripcion()                { return descripcion; }
    public void   setDescripcion(String v)        { this.descripcion = v; }

    @Override public String toString() { return nombre; }
}
