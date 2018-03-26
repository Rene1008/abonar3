package com.reneascanta.abonar;

import java.util.Date;

/**
 * Created by Rene on 28/09/2017.
 */

public class Nodo_Cuenta {


    private int id_usuario;
    private double saldo;
    private int numero_proceso;
    private String tipo_proceso;
    private String fecha;

    public Nodo_Cuenta(int id_usuario, double saldo, int numero_proceso, String tipo_proceso, String fecha) {

        this.id_usuario = id_usuario;
        this.saldo = saldo;
        this.numero_proceso = numero_proceso;
        this.tipo_proceso = tipo_proceso;
        this.fecha = fecha;
    }


    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getNumero_proceso() {
        return numero_proceso;
    }

    public void setNumero_proceso(int numero_proceso) {
        this.numero_proceso = numero_proceso;
    }

    public String getTipo_proceso() {
        return tipo_proceso;
    }

    public void setTipo_proceso(String tipo_proceso) {
        this.tipo_proceso = tipo_proceso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
