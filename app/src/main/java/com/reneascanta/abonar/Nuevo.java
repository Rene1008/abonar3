package com.reneascanta.abonar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;


public class Nuevo extends AppCompatActivity {

    ImageButton im_agregarOperador, ib_sector;
    Spinner s_lista, s_sector;
    ArrayList<String> listado;
    ArrayList<String> listadoSector;
    Button b_guardarNuevo, b_mostrar;
    EditText et_alias, et_nombres, et_numeroTelefonico, et_descripcion, et_saldo;
    Spinner sd_operador, sd_sector;
    Switch switch1;
    TextView tv_saldo;

    double valorInicial;


    int ID_EXTRA, ID_SECTOR_EXTRA, ID_OPERADORMOVIL_EXTRA;
    String NOMBRES_EXTRA, ALIAS_EXTRA, DETALLE_EXTRA, FOTO_EXTRA, TELEFONO_EXTRA;


    @Override
    protected void onPostResume() {
        super.onPostResume();
        cargarDatos();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        //  valorInicial=0;

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA = b.getInt("ID");
            NOMBRES_EXTRA = b.getString("NOMBRES");
            ALIAS_EXTRA = b.getString("ALIAS");
            DETALLE_EXTRA = b.getString("DETALLE");
            FOTO_EXTRA = b.getString("FOTO");
            ID_SECTOR_EXTRA = b.getInt("ID_SECTOR");
            ID_OPERADORMOVIL_EXTRA = b.getInt("ID_OPERADORMOVIL");
            TELEFONO_EXTRA = b.getString("TELEFONO");

        }


        im_agregarOperador = (ImageButton) findViewById(R.id.ib_agregarOperador);
        ib_sector = (ImageButton) findViewById(R.id.ib_sector);
        b_guardarNuevo = (Button) findViewById(R.id.b_guardarNuevo);
        et_alias = (EditText) findViewById(R.id.et_alias);
        et_nombres = (EditText) findViewById(R.id.et_nombres);
        sd_operador = (Spinner) findViewById(R.id.sd_operador);
        et_numeroTelefonico = (EditText) findViewById(R.id.et_numeroTelefono);
        sd_sector = (Spinner) findViewById(R.id.sd_sector);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        switch1 = (Switch) findViewById(R.id.switch1);
        et_saldo = (EditText) findViewById(R.id.et_saldo);
        tv_saldo = (TextView) findViewById(R.id.tv_saldo);


        et_alias.setText(ALIAS_EXTRA);
        et_nombres.setText(NOMBRES_EXTRA);
        et_numeroTelefonico.setText(TELEFONO_EXTRA);
        et_descripcion.setText(DETALLE_EXTRA);

        b_mostrar = (Button) findViewById(R.id.b_mostrar);

        et_saldo.setText("0");
        // switch1.setChecked(false);


        s_lista = (Spinner) findViewById(R.id.sd_operador);
        im_agregarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nuevo.this, Operador.class);

                startActivity(intent);
            }
        });

        s_sector = (Spinner) findViewById(R.id.sd_sector);
        ib_sector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botonSector = new Intent(Nuevo.this, Sector.class);
                startActivity(botonSector);
            }
        });

        b_guardarNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sec[] = sd_sector.getSelectedItem().toString().split(" ");
                String ope[] = sd_operador.getSelectedItem().toString().split(" ");

                if (et_nombres.getText().toString().equals("") || et_descripcion.getText().toString().equals("")
                        || et_numeroTelefonico.getText().toString().equals("")) {


                    mensajeError();
                } else {

                    if (noRepet(et_nombres.getText().toString().toUpperCase(), Integer.parseInt(sec[0]))) {

                        valorInicial = Double.parseDouble(et_saldo.getText().toString());
                        guardarNuevo(et_nombres.getText().toString().toUpperCase(),
                                et_alias.getText().toString().toUpperCase(),
                                et_descripcion.getText().toString().toUpperCase(),
                                "foto",
                                Integer.parseInt(sec[0]),
                                Integer.parseInt(ope[0]),
                                et_numeroTelefonico.getText().toString(), valorInicial);

                        et_nombres.setText("");
                        et_alias.setText("");
                        et_descripcion.setText("");
                        et_numeroTelefonico.setText("");
                        et_saldo.setText("0");
                        //Intent intent =  new Intent(Nuevo.this, this.getClass());
                        //startActivity(intent);

                        finish();
                     } else {
                        Toast.makeText(Nuevo.this, "EXISTE UN CLIETE CON EL MISMO NOMBRE!!.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        b_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Nuevo.this, Clientes.class);
                startActivity(intent);
            }
        });

        //     Toast.makeText(Nuevo.this,a+"    "+b, Toast.LENGTH_SHORT).show();


        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch1.isChecked()) {


                    et_saldo.setVisibility(View.VISIBLE);
                    tv_saldo.setVisibility(View.VISIBLE);

                    et_saldo.setText("0.0");
                    //   valorInicial = Float.parseFloat(et_saldo.getText().toString());


                    et_descripcion.getLayoutParams().height = 130;
                    // Toast.makeText(Nuevo.this,String.valueOf(valorInicial), Toast.LENGTH_SHORT).show();
                } else {
                    et_saldo.setText("0.0");
                    //    valorInicial = 0;
                    et_saldo.setVisibility(View.GONE);
                    tv_saldo.setVisibility(View.GONE);
                    et_descripcion.getLayoutParams().height = 200;
                }
            }
        });


    }

    private void guardarNuevo(String nombres, String alias, String detalle, String foto, int id_sector, int id_operadorMovil, String telefono, double valorInicio) {

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("NOMBRES", nombres);
            values.put("ALIAS", alias);
            values.put("DETALLE", detalle);
            values.put("FOTO", foto);
            values.put("ID_SECTOR", id_sector);
            values.put("ID_OPERADORMOVIL", id_operadorMovil);
            values.put("TELEFONO", telefono);
            values.put("ESTADO", "ACTIVO");
            db.insert("USUARIO", null, values);

            Calendar calendar = Calendar.getInstance();


            Date d = new Date();
            SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = fecc.format(d);
            int ultimoRegistro = ultimoRegistro();


            ContentValues valorCuenta = new ContentValues();
            valorCuenta.put("ID_USUARIO", ultimoRegistro);
            valorCuenta.put("SALDO", valorInicio);
            valorCuenta.put("TIPO_PROCESO", "MAS");
            valorCuenta.put("NUMERO_PROCESO", 1);
            valorCuenta.put("FECHA", fecha);
            db.insert("CUENTA", null, valorCuenta);

            ContentValues saldoDetelle = new ContentValues();
            saldoDetelle.put("ID_USUARIO", ultimoRegistro);
            saldoDetelle.put("SALDO", valorInicio);
            saldoDetelle.put("NUMERO_PROCESO", 1);
            saldoDetelle.put("TIPO_PROCESO", "MAS");
            saldoDetelle.put("SALDO_ABONO", 0);
            saldoDetelle.put("FECHA", fecha);
            db.insert("SALDOCUENTA", null, saldoDetelle);

            db.close();
            Toast.makeText(this, "SE GUARDO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();

        }


    }

    private int ultimoRegistro() {
        int registro = 0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO";

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                registro = c.getCount();
            } while (c.moveToNext());
        } else {
            registro = 1;
        }
        return registro;
    }

    private void cargarDatos() {
        // cargar en el drop todos los operadores existentes en la bdd
        listado = listadoOperadores();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado);
        s_lista.setAdapter(adapter);

        // cargar todos los sectores donde tiene clientes
        listadoSector = listaSectores();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listadoSector);
        s_sector.setAdapter(adapter1);
    }

    private ArrayList<String> listadoOperadores() {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM OPERADORMOVIL";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                //  Catalogo cat =  new Catalogo(c.getInt(0),c.getString(1),c.getString(2));
                String linea = c.getInt(0) + " " + c.getString(1);
                datos.add(linea);
            } while (c.moveToNext());
        }
        db.close();
        return datos;
    }

    private ArrayList<String> listaSectores() {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM SECTOR WHERE ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String linea = c.getInt(0) + " " + c.getString(1);
                datos.add(linea);
            } while (c.moveToNext());
        }
        return datos;
    }


    private void mensajeError() {
        Toast.makeText(this, "INGRESE TODOS LOS DATOS.", Toast.LENGTH_SHORT).show();
    }

    private boolean noRepet(String nombresNuevo, int id_sector) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO WHERE NOMBRES = '" + nombresNuevo + "' AND ID_SECTOR = " + id_sector;
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                return false; // retorna falso si el operador existe con el mismo nombre
            } while (c.moveToNext());
        }
        return true; // retorna true cuando puedo ingresar nuevo operador;s

    }
}
