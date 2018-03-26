package com.reneascanta.abonar;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Clientes extends AppCompatActivity {

    ListView lv_listaClientes;
    ArrayList<NodoClientes> lista;
    ArrayList<String> lista2;
   // SearchView sv_buscarCliente;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        cargarDatos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        lv_listaClientes = (ListView) findViewById(R.id.lv_listaClientes);
       // sv_buscarCliente = (SearchView) findViewById(R.id.sv_buscarCliente);

        lv_listaClientes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mensaje("Que desea realizar con " + lista.get(position).getNOMBRES() + "?", position);

                return true;
            }
        });



        lv_listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int ID = lista.get(position).getID();

                Intent intent = new Intent(Clientes.this, DatosClientes.class);
                intent.putExtra("ID", ID);
                startActivity(intent);

            }
        });


    }

    private void cargarDatos() {

        lista = lista();
        ArrayAdapter<NodoClientes> adapter = new ArrayAdapter<NodoClientes>(this, android.R.layout.simple_expandable_list_item_1, lista);
        lv_listaClientes.setAdapter(adapter);

        lista2 = listaString();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, lista2);
        lv_listaClientes.setAdapter(adapter2);

    }


    private ArrayList<NodoClientes> lista() {
        ArrayList<NodoClientes> datos = new ArrayList<NodoClientes>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM USUARIO  WHERE ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                NodoClientes linea = new NodoClientes(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6), c.getString(7));
                datos.add(linea);
            } while (c.moveToNext());
        }
        return datos;
    }



    private ArrayList<String> listaString() {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM USUARIO WHERE ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                String linea = c.getInt(0) + " " + c.getString(1) + " " + c.getString(2);
                datos.add(linea);
            } while (c.moveToNext());
        }
        return datos;
    }

    private void mensaje(final String obj, final int position) {
        //Toast.makeText(this,obj.toString(),Toast.LENGTH_SHORT).show();


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(obj);
        dialogo.setPositiveButton("EDITAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int ID = lista.get(position).getID();
                String NOMBRES = lista.get(position).getNOMBRES();
                String ALIAS = lista.get(position).getALIAS();
                String DETALLE = lista.get(position).getDETALLE();
                String FOTO = lista.get(position).getFOTO();
                int ID_SECTOR = lista.get(position).getID_SECTOR();
                int ID_OPERADORMOVIL = lista.get(position).getID_OPERADORMOVIL();
                String TELEFONO = lista.get(position).getTELEFONO();

                Intent intent = new Intent(Clientes.this, EditarClientes.class);
                intent.putExtra("ID", ID);
                intent.putExtra("NOMBRES", NOMBRES);
                intent.putExtra("ALIAS", ALIAS);
                intent.putExtra("DETALLE", DETALLE);
                intent.putExtra("FOTO", FOTO);
                intent.putExtra("ID_SECTOR", ID_SECTOR);
                intent.putExtra("ID_OPERADORMOVIL", ID_OPERADORMOVIL);
                intent.putExtra("TELEFONO", TELEFONO);

                startActivity(intent);

            }
        });
        dialogo.setNegativeButton("ELIMINAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                final AlertDialog.Builder dialogo = new AlertDialog.Builder(Clientes.this);
                dialogo.setMessage("¿ESTA SEGURO DE ELIMINAR?");
                dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        int ID = lista.get(position).getID();
                        BaseHelper usuario = new BaseHelper(Clientes.this, "abonar", null, 1);
                        SQLiteDatabase db = usuario.getWritableDatabase();

                        ContentValues update = new ContentValues();
                        update.put("ESTADO", "DESACTIVO");
                        db.update("USUARIO", update, "ID = " + ID, null);
                        db.close();
                        //   db.delete("USUARIO", "ID = "+ ID,null)
                        // db.close();
                        Toast.makeText(Clientes.this, "SE ELIMINO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();
                        cargarDatos();

                    }
                });

                dialogo.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                      // codigo para cancelar
                        dialog.dismiss();


                    }
                });



                Dialog dial = dialogo.create();
                dial.show();



            }
        });
        Dialog dial = dialogo.create();
        dial.show();
    }
}
