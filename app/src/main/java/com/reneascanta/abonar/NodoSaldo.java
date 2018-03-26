package com.reneascanta.abonar;

import java.util.Date;

/**
 * Created by Rene on 05/09/2017.
 */

public class NodoSaldo {
    private int id;
    private int id_usuario;
    private float saldo;
    private int numero_proceso;
    private String tipo_proceso;
    private float saldo_abono;
    private String fecha;

    public NodoSaldo(int id, int id_usuario, float saldo, int numero_proceso, String tipo_proceso, float saldo_abono, String fecha) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.saldo = saldo;
        this.numero_proceso = numero_proceso;
        this.tipo_proceso = tipo_proceso;
        this.saldo_abono = saldo_abono;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public float getNumero_proceso() {
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

    public float getSaldo_abono() {
        return saldo_abono;
    }

    public void setSaldo_abono(float saldo_abono) {
        this.saldo_abono = saldo_abono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
