package com.reneascanta.abonar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rene on 29/07/2017.
 */

public class BaseHelper extends SQLiteOpenHelper {

    String tabla = "USUARIO";
    String tabla2 = "TELEFONO";
    String tabla3 = "OPERADORMOVIL";
    String tabla4 = "SECTOR";
    String tabla5 = "CUENTA";
    String tabla6 = "CUENTADETALLE";
    String tabla7 = "SALDOCUENTA";
    String tabla8 = "PROPIETARIO";

    String sql = " CREATE TABLE " + tabla + " (ID INTEGER PRIMARY KEY, NOMBRES TEXT, ALIAS TEXT, DETALLE TEXT, FOTO TEXT, ID_SECTOR INTEGER, ID_OPERADORMOVIL INTEGER, TELEFONO TEXT, ESTADO TEXT);";
    String sql2 = " CREATE TABLE " + tabla2 + " (ID INTEGER PRIMARY KEY, ID_OPERADOR INTEGER, DETALLE TEXT, ID_USUARIO INTEGER);";
    //String sql3 = " CREATE TABLE "+tabla3+" (ID INTEGER PRIMARY KEY, DETALLE TEXT, ESTADO TEXT);" ;
    String sql3 = " CREATE TABLE " + tabla3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DETALLE TEXT, ESTADO TEXT);";
    String sql4 = " CREATE TABLE " + tabla4 + " (ID INTEGER PRIMARY KEY, DETALLE TEXT, ESTADO TEXT);";
    String sql5 = " CREATE TABLE " + tabla5 + " (ID INTEGER PRIMARY KEY, ID_USUARIO INTEGER, SALDO DOUBLE, NUMERO_PROCESO INTEGER,TIPO_PROCESO TEXT, FECHA DATETIME);";
    String sql6 = " CREATE TABLE " + tabla6 + "(ID INTEGER PRIMARY KEY, ID_CUENTA INTEGER," +
            " PROCESO TEXT, CANTIDAD FLOAT, VALOR DOUBLE, FECHA DATETIME, ESTADO TEXT, DETALLE TEXT, NUMERO_PROCESO INTEGER);";

    String sql7 = " CREATE TABLE " + tabla7 + "(ID INTEGER PRIMARY KEY, ID_USUARIO INTEGER, SALDO DOUBLE, NUMERO_PROCESO INTEGER, TIPO_PROCESO TEXT, SALDO_ABONO DOUBLE, FECHA DATETIME);";
    String sql8 = " CREATE TABLE " + tabla8 + "(ID INTEGER , NOMBRE TEXT, PASS TEXT, EMAIL TEXT, SERIAL TEXT,  ESTADO TEXT);";


    public BaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dSql = " DROP TABLE " + tabla;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla2;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla3;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla4;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla5;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla6;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla7;
        db.execSQL(dSql);
        dSql = " DROP TABLE " + tabla8;
        db.execSQL(dSql);

        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);


    }
}
