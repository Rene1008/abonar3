package com.reneascanta.abonar;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v4.app.ActivityCompatApi21;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton ib_mas, ib_config, ib_compartir, ib_nuevo, ib_abonar;
    // Spinner sd_operadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ib_mas = (ImageButton) findViewById(R.id.ib_mas);
        ib_config = (ImageButton) findViewById(R.id.ib_config);
        ib_compartir = (ImageButton) findViewById(R.id.ib_compartir);
        ib_nuevo = (ImageButton) findViewById(R.id.ib_nuevo);
        ib_abonar = (ImageButton) findViewById(R.id.ib_abonar);
        //  sd_operadores = (Spinner)findViewById(R.id.sd_operador);

        // cargarCatalogoOperador();
        //  cargarRegistro();

        if (!noRepeat("CLARO")) {

            cargarOperadores();
        }
        ib_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, Nuevo.class);
                startActivity(intent);
                // mensajeAux();
            }


        });


        ib_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                startActivity(intent);
            }
        });

        ib_mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Mercancia.class);
                startActivity(intent);
            }
        });

        ib_abonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(MainActivity.this, SeleccionarAbonar.class);
                startActivity(inten);
            }
        });

        ib_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(MainActivity.this, Configuracion.class);
                startActivity(inten);
            }
        });

    }

    private void cargarOperadores() {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put("DETALLE", "CLARO");
        c.put("ESTADO", "ACTIVO");
        db.insert("OPERADORMOVIL", null, c);

        c = new ContentValues();
        c.put("DETALLE", "MOVISTAR");
        c.put("ESTADO", "ACTIVO");
        db.insert("OPERADORMOVIL", null, c);

        db.close();
    }

    private boolean noRepeat(String operador) {
        boolean aux = false;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT  * FROM OPERADORMOVIL WHERE DETALLE = '" + operador + "'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                aux = true;
                break;
            } while (c.moveToNext());
        }

        return aux;
    }


}
