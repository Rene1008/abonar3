package com.reneascanta.abonar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Configuracion extends AppCompatActivity {

    EditText et_pass, et_pass2;
    TextView tv_pass, tv_pass2;
    Button b_guardarPass,b_sector,b_crearPDF;
    Switch sw_pass;

    Switch sw_email;
    EditText et_email;
    Button b_email;

    Button b_enviarArchivo;
    TextView tv_enviarArchivo;


    Button b_devolucionMercancia;

    private String dato[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        dato = datos();
        // -------------  para guardar pass -------------------
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass2 = (EditText) findViewById(R.id.et_pass2);
        tv_pass = (TextView) findViewById(R.id.tv_pass);
        tv_pass2 = (TextView) findViewById(R.id.tv_pass2);
        b_guardarPass = (Button) findViewById(R.id.b_guardarPass);
        sw_pass = (Switch) findViewById(R.id.sw_pass);
        // -----------------    fin guardar pass   ---------------------

        // ----------------- para guardar email y enviar archivos -------------
        sw_email = (Switch) findViewById(R.id.sw_email);
        et_email = (EditText) findViewById(R.id.et_email);
        b_email = (Button) findViewById(R.id.b_email);
        b_enviarArchivo = (Button) findViewById(R.id.b_enviarArchivo);
        tv_enviarArchivo = (TextView) findViewById(R.id.tv_enviarArchivo);
        // ----------------------- fin guardar email -----------------------

        //------------------------- devolucion mercancia ---------------------
        b_devolucionMercancia = (Button) findViewById(R.id.b_devolucionMercancia);
        // -------------------------- fin devolucion mercancia------------------
    /// ------------------- agregar o editar sectores ---------------------

        b_sector = (Button)findViewById(R.id.b_sector);
        // ------------------- fin agregar o editar sectes

        // ---------------- CREAR PDF --------------
        b_crearPDF = (Button) findViewById(R.id.b_crearPDF);
        // ----------------- FIN CREAR PDF -----------

        if (dato[2].equals("")) {

            sw_pass.setChecked(false);
        } else {
            sw_pass.setChecked(true);
            et_pass.setVisibility(View.VISIBLE);
            et_pass2.setVisibility(View.VISIBLE);
            tv_pass.setVisibility(View.VISIBLE);
            tv_pass2.setVisibility(View.VISIBLE);
            b_guardarPass.setVisibility(View.VISIBLE);

            et_pass.setText(dato[2]);
            et_pass2.setText(dato[2]);
        }

        if (dato[3].equals("")) {

            sw_email.setChecked(false);
        } else {
            sw_email.setChecked(true);
            et_email.setVisibility(View.VISIBLE);
            b_email.setVisibility(View.VISIBLE);
            b_enviarArchivo.setVisibility(View.VISIBLE);
            tv_enviarArchivo.setVisibility(View.VISIBLE);

            et_email.setText(dato[3]);
        }

        sw_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sw_pass.isChecked()) {
                    et_pass.setVisibility(View.VISIBLE);
                    et_pass2.setVisibility(View.VISIBLE);
                    tv_pass.setVisibility(View.VISIBLE);
                    tv_pass2.setVisibility(View.VISIBLE);
                    b_guardarPass.setVisibility(View.VISIBLE);
                } else {
                    et_pass.setVisibility(View.GONE);
                    et_pass2.setVisibility(View.GONE);
                    tv_pass.setVisibility(View.GONE);
                    tv_pass2.setVisibility(View.GONE);
                    b_guardarPass.setVisibility(View.GONE);
                    registrarClave("");
                }
            }
        });
        b_sector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Configuracion.this,Sector_Editar.class);
                startActivity(intent);
            }
        });
        sw_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_email.isChecked()) {

                    et_email.setVisibility(View.VISIBLE);
                    b_email.setVisibility(View.VISIBLE);
                    b_enviarArchivo.setVisibility(View.VISIBLE);
                    tv_enviarArchivo.setVisibility(View.VISIBLE);
                } else {

                    et_email.setVisibility(View.GONE);
                    b_email.setVisibility(View.GONE);
                    registrarEmail("");
                    et_email.setText("");
                    b_enviarArchivo.setVisibility(View.GONE);
                    tv_enviarArchivo.setVisibility(View.GONE);
                    Toast.makeText(Configuracion.this, "EL EMAIL FUE BORRADO.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        b_guardarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pass.getText().toString().equals(et_pass2.getText().toString())) {

                    registrarClave(et_pass.getText().toString());
                    Toast.makeText(Configuracion.this, "CLAVE GUARDADA.", Toast.LENGTH_SHORT).show();
                    et_pass.setText("");
                    et_pass2.setText("");
                } else {
                    Toast.makeText(Configuracion.this, "LAS CLAVES NO COINCIDEN.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        b_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().equals("")) {

                    Toast.makeText(Configuracion.this, "EL CAMPO NO PUEDE ESTAR VACIO.", Toast.LENGTH_SHORT).show();
                } else {
                    registrarEmail(et_email.getText().toString());
                    Toast.makeText(Configuracion.this, "EMAIL GUARDADO.", Toast.LENGTH_SHORT).show();
                    // et_email.setText("");
                }
            }
        });

        b_enviarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recuperarSDCard(v);

                if (et_email.getText().toString().equals("")) {
                    Toast.makeText(Configuracion.this, "NECESITA UN EMAIL PARA ENVIAR.", Toast.LENGTH_SHORT).show();

                } else {
                    grabarSDCard(v, "datos.txt");
                    enviarEmail(et_email.getText().toString(), "datos.txt");

                }
            }
        });


        b_devolucionMercancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuracion.this, SeleccionarDevolucion.class);
                startActivity(intent);
            }
        });


        b_crearPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Configuracion.this, Crear_PDF.class);
                startActivity(intent);
            }
        });

    }


    private void registrarClave(String clave) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put("PASS", clave);
            db.update("PROPIETARIO", values, "ID = " + 1000, null);
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarEmail(String email) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put("EMAIL", email);
            db.update("PROPIETARIO", values, "ID = " + 1000, null);
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] datos() {
        String[] dato = new String[6];
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM PROPIETARIO  ";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato[0] = String.valueOf(c.getInt(0));
                dato[1] = c.getString(1);
                dato[2] = c.getString(2);
                dato[3] = c.getString(3);
                dato[4] = c.getString(4);
                dato[5] = c.getString(5);
                break;

            } while (c.moveToNext());
        }

        return dato;
    }

    private void enviarEmail(String email, String txt) {
        Date d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = fecc.format(d);


        String[] mailto = {email};
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), txt));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dats");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Respaldo de datos.\nFecha que se envio el archivo: " + fecha);
        //emailIntent.setType("message/rfc822");
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send email using:"));

    }

    private void grabarSDCard(View v, String nameArchivo) {
        String datos_t_sector = T_sector();
        String datos_t_operadorMovil = T_operadorMovil();
        String datos_t_usuario = T_usuario();
        String datos_t_saldoCuenta = T_saldosCuenta();
        String datos_t_cuenta = T_cuenta();
        String datos_t_cuentaDetalle = T_cuentaDetalle();
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), nameArchivo);

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

            osw.write(datos_t_operadorMovil + "/" + "\n" + datos_t_sector + "/" + "\n" + datos_t_usuario + "/" + "\n" + datos_t_saldoCuenta + "/" + "\n" + datos_t_cuenta + "/" + "\n" + datos_t_cuentaDetalle);
            osw.flush();
            osw.close();

        } catch (IOException e) {

        }
    }

    // -------------------------  SACAR TODOS LOS DATOS PARA SINCRONIZAR EN UN TXT ------------------------------
    private String T_usuario() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM USUARIO";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " +
                        c.getString(4) + " " + c.getInt(5) + " " + c.getInt(6) + " " + c.getString(7) + " " + c.getString(8) + "," + "\n";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_sector() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM SECTOR";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + " " + c.getString(1) + " " + c.getString(2) + "," + "\n";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_operadorMovil() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM OPERADORMOVIL";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + " " + c.getString(1) + " " + c.getString(2) + "," + "\n";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_saldosCuenta() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM SALDOCUENTA";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + " " + c.getInt(1) + " " + c.getDouble(2) + " " + c.getInt(3) + " " + c.getString(4) + " " + c.getDouble(5) + " " + c.getString(6) + "," + "\n";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_cuenta() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM CUENTA";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + " " + c.getInt(1) + " " + c.getDouble(2) + " " + c.getInt(3) + " " + c.getString(4) + " " + c.getString(5) + "," + "\n";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_cuentaDetalle() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM CUENTADETALLE";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + " " + c.getInt(1) + " " + c.getString(2) + " " + c.getFloat(3) + " " + c.getDouble(4) + " " + c.getString(5) + " " + c.getString(6)
                        + " " + c.getString(7) + " " + c.getInt(8) + "," + "\n";
            } while (c.moveToNext());
        }
        return dato;
    }
    // ----------------------- FIN SACAR TODOS LOS DATOS PARA SINCRONIZAR EN UN TXT ------------------------------


}
