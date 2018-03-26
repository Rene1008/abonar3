package com.reneascanta.abonar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Rene on 09/08/2017.
 */

public class NodoClientes {


    int ID;
    String NOMBRES;
    String ALIAS;
    String DETALLE;
    String FOTO;
    int ID_SECTOR;
    int ID_OPERADORMOVIL;
    String TELEFONO;


    public NodoClientes(int ID, String NOMBRES, String ALIAS, String DETALLE, String FOTO, int ID_SECTOR, int ID_OPERADORMOVIL, String TELEFONO) {
        this.ID = ID;
        this.NOMBRES = NOMBRES;
        this.ALIAS = ALIAS;
        this.DETALLE = DETALLE;
        this.FOTO = FOTO;
        this.ID_SECTOR = ID_SECTOR;
        this.ID_OPERADORMOVIL = ID_OPERADORMOVIL;
        this.TELEFONO = TELEFONO;
    }

    public int getID_SECTOR() {
        return ID_SECTOR;
    }

    public void setID_SECTOR(int ID_SECTOR) {
        this.ID_SECTOR = ID_SECTOR;
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

}
