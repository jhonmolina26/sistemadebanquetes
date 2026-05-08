package modules;

public class ClienteTuristico extends Cliente {
    private String tipoTurista;

    public ClienteTuristico() {
        super();
    }

    public ClienteTuristico(String nombre, String apellido, String cedula, int edad, String correo, String telefono,
            String tipoTurista) {
                super(nombre, apellido, cedula, edad, correo, telefono);
                this.tipoTurista = tipoTurista;
    }

    public String getTipoTurista() {
        return tipoTurista;
    }

    public void setTipoTurista(String tipoTurista) {
        this.tipoTurista = tipoTurista;
    }

    @Override
    public String toString() {
        return super.toString() + "," + (tipoTurista == null ? "" : tipoTurista);
    }

    public static ClienteTuristico fromString(String line) {
        try {
            String[] p = line.split(",", -1);
            ClienteTuristico c = new ClienteTuristico();
            c.setNombre(p[0]);
            c.setApellido(p[1]);
            c.setCedula(p[2]);
            c.setEdad(Integer.parseInt(p[3]));
            c.setCorreo(p[4]);
            c.setTelefono(p[5]);
            if (p.length > 6)
                c.setTipoTurista(p[6]);
            return c;
        } catch (Exception e) {
            return null;
        }
    }
}
