package com.reneascanta.abonar;

/**
 * Created by Rene on 28/09/2017.
 */

public class Nodo_Usuario {
    private int ID;
    private String NOMBRES;
    private String ALIAS;
    private String DETALLE;
    private String FOTO;
    private int ID_SECTOR;
    private int ID_OPERADORMOVIL;
    private String TELEFONO;
    private String estado;

    public Nodo_Usuario(int ID, String NOMBRES, String ALIAS, String DETALLE, String FOTO, int ID_SECTOR, int ID_OPERADORMOVIL, String TELEFONO, String estado) {
        this.ID = ID;
        this.NOMBRES = NOMBRES;
        this.ALIAS = ALIAS;
        this.DETALLE = DETALLE;
        this.FOTO = FOTO;
        this.ID_SECTOR = ID_SECTOR;
        this.ID_OPERADORMOVIL = ID_OPERADORMOVIL;
        this.TELEFONO = TELEFONO;
        this.estado = estado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOMBRES() {
        return NOMBRES;
    }

    public void setNOMBRES(String NOMBRES) {
        this.NOMBRES = NOMBRES;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public String getDETALLE() {
        return DETALLE;
    }

    public void setDETALLE(String DETALLE) {
        this.DETALLE = DETALLE;
    }

    public String getFOTO() {
        return FOTO;
    }

    public void setFOTO(String FOTO) {
        this.FOTO = FOTO;
    }

    public int getID_SECTOR() {
        return ID_SECTOR;
    }

    public void setID_SECTOR(int ID_SECTOR) {
        this.ID_SECTOR = ID_SECTOR;
    }

    public int getID_OPERADORMOVIL() {
        return ID_OPERADORMOVIL;
    }

    public void setID_OPERADORMOVIL(int ID_OPERADORMOVIL) {
        this.ID_OPERADORMOVIL = ID_OPERADORMOVIL;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
