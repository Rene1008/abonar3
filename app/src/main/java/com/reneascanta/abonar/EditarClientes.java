package com.reneascanta.abonar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditarClientes extends AppCompatActivity {

    private int ID_EXTRA_UPDATE, ID_SECTOR_EXTRA_UPDATE, ID_OPERADORMOVIL_EXTRA_UPDATE;
    private String NOMBRES_EXTRA_UPDATE, ALIAS_EXTRA_UPDATE, DETALLE_EXTRA_UPDATE, FOTO_EXTRA_UPDATE, TELEFONO_EXTRA_UPDATE;

    private EditText et_alias2, et_nombres2, et_numeroTelefono2, et_descripcion2;
    private Spinner sd_operador2, sd_sector2;
    private Button b_editarClientes, b_cancelarClientes;
    private Switch switch2;
    private TextView tv_saldo2;
    private EditText et_saldo2;

    private ArrayList<String> listado_update;
    private ArrayList<String> listadoSector_update;

    private double auxSwith[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_clientes);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA_UPDATE = b.getInt("ID");
            NOMBRES_EXTRA_UPDATE = b.getString("NOMBRES");
            ALIAS_EXTRA_UPDATE = b.getString("ALIAS");
            DETALLE_EXTRA_UPDATE = b.getString("DETALLE");
            FOTO_EXTRA_UPDATE = b.getString("FOTO");
            ID_SECTOR_EXTRA_UPDATE = b.getInt("ID_SECTOR");
            ID_OPERADORMOVIL_EXTRA_UPDATE = b.getInt("ID_OPERADORMOVIL");
            TELEFONO_EXTRA_UPDATE = b.getString("TELEFONO");

        }
        et_alias2 = (EditText) findViewById(R.id.et_alias2);
        et_nombres2 = (EditText) findViewById(R.id.et_nombres2);
        et_numeroTelefono2 = (EditText) findViewById(R.id.et_numeroTelefono2);
        et_descripcion2 = (EditText) findViewById(R.id.et_descripcion2);
        sd_operador2 = (Spinner) findViewById(R.id.sd_operador2);
        sd_sector2 = (Spinner) findViewById(R.id.sd_sector2);
        b_editarClientes = (Button) findViewById(R.id.b_editarClientes);
        b_cancelarClientes = (Button) findViewById(R.id.b_cancelarClientes);
        switch2 = (Switch) findViewById(R.id.switch2);
        et_saldo2 = (EditText) findViewById(R.id.et_saldo2);
        tv_saldo2 = (TextView) findViewById(R.id.tv_saldo2);


        et_alias2.setText(ALIAS_EXTRA_UPDATE);
        et_nombres2.setText(NOMBRES_EXTRA_UPDATE);
        et_numeroTelefono2.setText(TELEFONO_EXTRA_UPDATE);
        et_descripcion2.setText(DETALLE_EXTRA_UPDATE);

        cargarDatos_update();


        // ---------------  codigo para cargar los spinner con cada item guardado en a bd -----------------------
        String item = itemSeleccionado("SELECT * FROM OPERADORMOVIL WHERE ID = " + ID_OPERADORMOVIL_EXTRA_UPDATE);
        sd_operador2.setSelection(getIndex(sd_operador2, item));
        String item2 = itemSeleccionado("SELECT * FROM SECTOR WHERE ID = " + ID_SECTOR_EXTRA_UPDATE);
        sd_sector2.setSelection(getIndex(sd_sector2, item2));
        // ---------------------  fin ---------------------------------------


        auxSwith = saldoInicialUpdate(ID_EXTRA_UPDATE);
        if (auxSwith[0] != -1) {

            switch2.setChecked(true);
            switch2.setVisibility(View.VISIBLE);
            tv_saldo2.setVisibility(View.VISIBLE);
            et_saldo2.setVisibility(View.VISIBLE);

            et_saldo2.setText(String.valueOf(auxSwith[1]));
            et_descripcion2.getLayoutParams().height = 130;

        } else {
            // switch2.setChecked(false);
            switch2.setVisibility(View.GONE);
            tv_saldo2.setVisibility(View.GONE);
            // et_alias2.setVisibility(View.GONE);
            et_descripcion2.getLayoutParams().height = 200;
            //  tv_saldo2.setText("0");
        }

        b_editarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sec[] = sd_sector2.getSelectedItem().toString().split(" ");
                String ope[] = sd_operador2.getSelectedItem().toString().split(" ");

                if (noRepet(et_nombres2.getText().toString().toUpperCase(), Integer.parseInt(sec[0]))) {
                    if (auxSwith[0] != -1) {

                        float s = Float.parseFloat(et_saldo2.getText().toString());
                        editarCliente2(ID_EXTRA_UPDATE, et_nombres2.getText().toString().toUpperCase(),
                                et_alias2.getText().toString().toUpperCase(),
                                et_descripcion2.getText().toString().toUpperCase(),
                                "foto",
                                Integer.parseInt(sec[0]),
                                Integer.parseInt(ope[0]),
                                et_numeroTelefono2.getText().toString(), s);
                        finish();
                    } else {

                        editarCliente(ID_EXTRA_UPDATE, et_nombres2.getText().toString().toUpperCase(),
                                et_alias2.getText().toString().toUpperCase(),
                                et_descripcion2.getText().toString().toUpperCase(),
                                "foto",
                                Integer.parseInt(sec[0]),
                                Integer.parseInt(ope[0]),
                                et_numeroTelefono2.getText().toString());
                        finish();
                    }

                    Intent intent = new Intent(EditarClientes.this, Clientes.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditarClientes.this,"YA EXISTE ALGUIEN CON EL MISMO NOMBRE.",Toast.LENGTH_SHORT).show();
                }




            }
        });
        b_cancelarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void editarCliente(int id, String nombres, String alias, String detalle, String foto, int id_sector, int id_operadorMovil, String telefono) {

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
            db.update("USUARIO", values, "ID = " + id, null);
            db.close();

            Toast.makeText(this, "SE GUARDO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editarCliente2(int id, String nombres, String alias, String detalle, String foto, int id_sector, int id_operadorMovil, String telefono, double saldoInicial) {

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
            db.update("USUARIO", values, "ID = " + id, null);


            Date d = new Date();
            SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = fecc.format(d);

            ContentValues valorCuenta = new ContentValues();
            valorCuenta.put("SALDO", saldoInicial);
            valorCuenta.put("FECHA", fecha);
            db.update("CUENTA", valorCuenta, "ID = " + id, null);

            ContentValues valorCuenta2 = new ContentValues();
            valorCuenta2.put("SALDO", saldoInicial);
            valorCuenta2.put("FECHA", fecha);
            db.update("SALDOCUENTA", valorCuenta2, "ID_USUARIO = " + id, null);


            Toast.makeText(this, "SE EDITO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();
        }
    }


    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private String itemSeleccionado(String sql) {
        String item = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                item = c.getInt(0) + " " + c.getString(1);
            } while (c.moveToNext());
        }
        db.close();
        return item;
    }

    private void cargarDatos_update() {
        // cargar en el drop todos los operadores existentes en la bdd
        listado_update = listadoOperadores_update();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado_update);
        sd_operador2.setAdapter(adapter);

        // cargar todos los sectores donde tiene clientes
        listadoSector_update = listaSectores_update();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listadoSector_update);
        sd_sector2.setAdapter(adapter1);
    }

    private ArrayList<String> listadoOperadores_update() {
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

    private ArrayList<String> listaSectores_update() {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM SECTOR";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String linea = c.getInt(0) + " " + c.getString(1);
                datos.add(linea);
            } while (c.moveToNext());
        }
        return datos;
    }

    private double[] saldoInicialUpdate(int id_usuario) {
        double datos[] = new double[3];
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM CUENTA WHERE ID_USUARIO = " + id_usuario;

        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                if (c.getInt(3) == 1) {
                    datos[0] = c.getInt(0);
                    datos[1] = c.getDouble(2);
                    datos[2] = c.getDouble(4);
                } else if (c.getInt(3) > 1) {
                    datos[0] = -1;
                    datos[1] = -1;
                    datos[2] = -1;
                    break;
                }
            } while (c.moveToNext());
        }
        db.close();
        return datos;

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
