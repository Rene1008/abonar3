package com.reneascanta.abonar;

/**
 * Created by Rene on 01/08/2017.
 */

public class Catalogo {
    private int id;
    private String detalle;
    private String estado;

    public Catalogo(int id, String detalle, String estado) {
        this.id = id;
        this.detalle = detalle;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
