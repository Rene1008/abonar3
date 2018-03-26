package com.reneascanta.abonar;

/**
 * Created by Rene on 28/09/2017.
 */

public class Nodo_CuentaDetalle {


    private int id_cuenta_usuario;
    private String proceso;
    private float cantidad;
    private double valor;
    private String fecha;
    private String estado;
    private String detalle;
    private int numero_proceso;

    public Nodo_CuentaDetalle() {
     }

    public Nodo_CuentaDetalle(int id_cuenta_usuario, String proceso, float cantidad, double valor, String fecha, String estado, String detalle, int numero_proceso) {

        this.id_cuenta_usuario = id_cuenta_usuario;
        this.proceso = proceso;
        this.cantidad = cantidad;
        this.valor = valor;
        this.fecha = fecha;
        this.estado = estado;
        this.detalle = detalle;
        this.numero_proceso = numero_proceso;
    }


    public int getId_cuenta_usuario() {
        return id_cuenta_usuario;
    }

    public void setId_cuenta_usuario(int id_cuenta_usuario) {
        this.id_cuenta_usuario = id_cuenta_usuario;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getNumero_proceso() {
        return numero_proceso;
    }

    public void setNumero_proceso(int numero_proceso) {
        this.numero_proceso = numero_proceso;
    }
}
