package com.reneascanta.abonar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Sector extends AppCompatActivity {

    Button b_guardar;
    EditText et_sector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);
        b_guardar = (Button) findViewById(R.id.b_guardarSector);
        et_sector = (EditText) findViewById(R.id.et_sector);

        b_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_sector.getText().toString().equals("")) {

                    mensajeError();
                } else {
                    if (noRepet(et_sector.getText().toString().toUpperCase())) {

                        guardar(et_sector.getText().toString().toUpperCase(), "ACTIVO");

                        finish();
                        et_sector.setText("");

                    } else {
                        Toast.makeText(Sector.this, "YA EXISTE ESTE SECTOR.", Toast.LENGTH_SHORT).show();
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

    private void guardar(String detalle, String estado) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("DETALLE", detalle);
            contentValues.put("ESTADO", estado);
            db.insert("SECTOR", null, contentValues);
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
        String sql = "SELECT * FROM SECTOR WHERE DETALLE= '" + operadorNuevo + "'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                return false; // retorna falso si el operador existe con el mismo nombre
            } while (c.moveToNext());
        }
        return true; // retorna true cuando puedo ingresar nuevo operador;s

    }
}
