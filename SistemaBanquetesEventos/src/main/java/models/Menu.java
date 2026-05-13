package models;

import java.math.BigDecimal;

public class Menu {
    private int id;
    private String categoria;
    private String plato;
    private String paquete;
    private String restriccion;
    private BigDecimal precioPax;

    public Menu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPlato() {
        return plato;
    }

    public void setPlato(String plato) {
        this.plato = plato;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public String getRestriccion() {
        return restriccion;
    }

    public void setRestriccion(String restriccion) {
        this.restriccion = restriccion;
    }

    public BigDecimal getPrecioPax() {
        return precioPax;
    }

    public void setPrecioPax(BigDecimal precioPax) {
        this.precioPax = precioPax;
    }
}
