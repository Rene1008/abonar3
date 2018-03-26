package com.reneascanta.abonar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Operador extends AppCompatActivity {

    EditText et_operador;
    Button b_guardarOperador;
    ListView lv_listaOperadores;
    ArrayList<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operador);

        et_operador = (EditText) findViewById(R.id.et_operador);
        b_guardarOperador = (Button) findViewById(R.id.b_guardarOperador);
        lv_listaOperadores = (ListView) findViewById(R.id.lv_listaOperadores);


        b_guardarOperador.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (et_operador.getText().toString().equals("")) {
                    mensajeError();
                } else {

                    if (noRepet(et_operador.getText().toString().toUpperCase())) {

                        guardarOperador(et_operador.getText().toString().toUpperCase(), "ACTIVO");
                        finish();

                        et_operador.setText("");
                    } else {
                        Toast.makeText(Operador.this, "YA EXISTE ESTE OPERADOR.", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarOperador(String detalle, String estado) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put("DETALLE", detalle);
            contentValues.put("ESTADO", estado);
            db.insert("OPERADORMOVIL", null, contentValues);
            db.close();
            Toast.makeText(this, "SE GUARDO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {

            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();
        }


    }

    private void mensajeError() {
        Toast.makeText(this, "INGRESE TODOS LOS DATOS.", Toast.LENGTH_SHORT).show();
    }

    private boolean noRepet(String operadorNuevo) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM OPERADORMOVIL WHERE DETALLE= '" + operadorNuevo + "'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                return false; // retorna falso si el operador existe con el mismo nombre
            } while (c.moveToNext());
        }
        return true; // retorna true cuando puedo ingresar nuevo operador;s

    }

}
