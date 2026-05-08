package modules;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String apellido;
    private String cedula;
    private int edad;
    private String correo;
    private String telefono;

    public Cliente() {
    }

    // Constructor base (el que necesita ClienteTuristico)
    public Cliente(String nombre, String apellido, String cedula, int edad, String correo, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.edad = edad;
        this.correo = correo;
        this.telefono = telefono;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return String.join(",",
                safe(nombre), safe(apellido), safe(cedula), String.valueOf(edad),
                safe(correo), safe(telefono));
    }

    public static Cliente fromString(String s) {
        try {
            String[] p = s.split(",", -1);
            Cliente c = new Cliente();
            c.setNombre(p.length > 0 ? p[0] : "");
            c.setApellido(p.length > 1 ? p[1] : "");
            c.setCedula(p.length > 2 ? p[2] : "");
            c.setEdad(p.length > 3 && !p[3].isEmpty() ? Integer.parseInt(p[3]) : 0);
            c.setCorreo(p.length > 4 ? p[4] : "");
            c.setTelefono(p.length > 5 ? p[5] : "");
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    private static String safe(String v) {
        return v == null ? "" : v;
    }
}
