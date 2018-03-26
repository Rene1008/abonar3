package com.reneascanta.abonar;

/**
 * Created by Rene on 28/09/2017.
 */

public class Nodo_SaldoCuenta {


    private int id_usuario;
    private double saldo;
    private int numero_proceso;
    private String tipo_proceso;
    private double saldo_abono;
    private String fecha;

    public Nodo_SaldoCuenta() {
     }

    public Nodo_SaldoCuenta(int id_usuario, double saldo, int numero_proceso, String tipo_proceso, double saldo_abono, String fecha) {
        this.id_usuario = id_usuario;
        this.saldo = saldo;
        this.numero_proceso = numero_proceso;
        this.tipo_proceso = tipo_proceso;
        this.saldo_abono = saldo_abono;
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

    public double getSaldo_abono() {
        return saldo_abono;
    }

    public void setSaldo_abono(double saldo_abono) {
        this.saldo_abono = saldo_abono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
