package com.reneascanta.abonar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SeleccionarAbonar extends AppCompatActivity {

    private Spinner s_sectoresMercanciaAbonar;
    private ListView lv_clientesSectoresAbonar;
    private ArrayList<String> listaSectores, listaClientesSectores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_abonar);

        s_sectoresMercanciaAbonar = (Spinner) findViewById(R.id.s_sectoresMercanciaAbonar);
        lv_clientesSectoresAbonar = (ListView) findViewById(R.id.lv_clientesSectoresAbonar);

        cargarDatosSectoresMercancia();

        // ------------------- inicio eveto para el spinner -----------------------
        s_sectoresMercanciaAbonar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item[] = listaSectores.get(position).split(" ");
                cargarDatosClientesSectores(Integer.parseInt(item[0]));
                //  Toast.makeText(parent.getContext(),item[0],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ------------------- fin eveto del spinner ------------------------------

        // ------------------- inicio evento del listview o lista -----------------

        lv_clientesSectoresAbonar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String identificador[] = listaClientesSectores.get(position).split(" ");
                String nombres = "";
                for (int i = 1; i < identificador.length; i++) {
                    nombres = nombres + " " + identificador[i];
                }

                //   Toast.makeText(Mercancia.this, identificador[0]+"  ->   "+ nombres,Toast.LENGTH_SHORT).show();

                Intent inten = new Intent(SeleccionarAbonar.this, AbonarSaldo.class);
                inten.putExtra("ID", identificador[0]);
                inten.putExtra("NOMBRES", nombres);
                startActivity(inten);


            }
        });


    }

    private void cargarDatosSectoresMercancia() {
        listaSectores = lista();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaSectores);
        s_sectoresMercanciaAbonar.setAdapter(adapter);
    }

    private void cargarDatosClientesSectores(int sector) {
        listaClientesSectores = clientesSector(sector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaClientesSectores);
        lv_clientesSectoresAbonar.setAdapter(adapter);
    }

    private ArrayList<String> lista() {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM SECTOR WHERE ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos.add(c.getInt(0) + " " + c.getString(1));
            } while (c.moveToNext());
        }
        return datos;


    }

    private ArrayList<String> clientesSector(int id_sector) {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO WHERE ID_SECTOR = " + id_sector + " AND ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos.add(c.getInt(0) + " " + c.getString(1));
            } while (c.moveToNext());
        }
        return datos;
    }


}
