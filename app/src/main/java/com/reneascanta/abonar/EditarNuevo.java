package com.reneascanta.abonar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class EditarNuevo extends AppCompatActivity {

    ImageButton im_agregarOperador2, ib_sector2;
    Spinner s_lista2, s_sector2;
    ArrayList<String> listado2;
    ArrayList<String> listadoSector2;
    Button b_guardarNuevo2, b_mostrar2;
    EditText et_alias2, et_nombres2, et_numeroTelefonico2, et_descripcion2;
    Spinner sd_operador2, sd_sector2;


    int ID_EXTRA2, ID_SECTOR_EXTRA2, ID_OPERADORMOVIL_EXTRA2;
    String NOMBRES_EXTRA2, ALIAS_EXTRA2, DETALLE_EXTRA2, FOTO_EXTRA2, TELEFONO_EXTRA2;

    /*
        @Override

        protected void onPostResume() {
            super.onPostResume();
            cargarDatos2();
        }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nuevo);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA2 = b.getInt("ID");
            NOMBRES_EXTRA2 = b.getString("NOMBRES");
            ALIAS_EXTRA2 = b.getString("ALIAS");
            DETALLE_EXTRA2 = b.getString("DETALLE");
            FOTO_EXTRA2 = b.getString("FOTO");
            ID_SECTOR_EXTRA2 = b.getInt("ID_SECTOR");
            ID_OPERADORMOVIL_EXTRA2 = b.getInt("ID_OPERADORMOVIL");
            TELEFONO_EXTRA2 = b.getString("TELEFONO");

        }


        // im_agregarOperador2 = (ImageButton) findViewById(R.id.ib_agregarOperador);
        // ib_sector = (ImageButton) findViewById(R.id.ib_sector);
        b_guardarNuevo2 = (Button) findViewById(R.id.b_guardarNuevo2);


        et_alias2 = (EditText) findViewById(R.id.et_alias2);
        et_nombres2 = (EditText) findViewById(R.id.et_nombres2);
        sd_operador2 = (Spinner) findViewById(R.id.sd_operador2);
        et_numeroTelefonico2 = (EditText) findViewById(R.id.et_numeroTelefono2);
        sd_sector2 = (Spinner) findViewById(R.id.sd_sector2);
        et_descripcion2 = (EditText) findViewById(R.id.et_descripcion2);


        et_alias2.setText(ALIAS_EXTRA2);
        et_nombres2.setText(NOMBRES_EXTRA2);
        et_numeroTelefonico2.setText(TELEFONO_EXTRA2);
        et_descripcion2.setText(DETALLE_EXTRA2);

        b_mostrar2 = (Button) findViewById(R.id.b_mostrar2);


        b_guardarNuevo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditarNuevo.this, "bien el boton de editar", Toast.LENGTH_SHORT).show();
            }
        });

        s_lista2 = (Spinner) findViewById(R.id.sd_operador2);
    }

    private void cargarDatos2() {
        // cargar en el drop todos los operadores existentes en la bdd
        listado2 = listadoOperadores();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado2);
        s_lista2.setAdapter(adapter);

        // cargar todos los sectores donde tiene clientes
        listadoSector2 = listaSectores();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listadoSector2);
        s_sector2.setAdapter(adapter1);
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


    private void mensajeError() {
        Toast.makeText(this, "INGRESE TODOS LOS DATOS.", Toast.LENGTH_SHORT).show();
    }

}
