package com.reneascanta.abonar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Sector_Editar extends AppCompatActivity {

    Button b_guardarSector,b_editarSector;
    EditText et_sector;
    ListView lv_sector;
    ArrayList<Nodo_Catalogo> lista;
    ArrayList<String> lista2;
    private int id_sector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector__editar);
        b_guardarSector = (Button) findViewById(R.id.b_guardarSector);
        b_editarSector = (Button) findViewById(R.id.b_editarSector);
        et_sector = (EditText) findViewById(R.id.et_sector);
        lv_sector = (ListView) findViewById(R.id.lv_sector);


        cargarDatos();

        b_guardarSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_sector.getText().toString().equals("")) {

                    mensajeError();
                } else {
                    if (noRepet(et_sector.getText().toString().toUpperCase())) {

                        guardar(et_sector.getText().toString().toUpperCase(), "ACTIVO");

                        cargarDatos();
                        et_sector.setText("");

                    } else {
                        Toast.makeText(Sector_Editar.this, "YA EXISTE ESTE SECTOR.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        lv_sector.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_sector = lista.get(position).getId();
                et_sector.setText(lista.get(position).getDetalle());
               // Toast.makeText(Sector_Editar.this, "ENTRO "+ ID+" ->" + lista.get(position).getDetalle(), Toast.LENGTH_SHORT).show();

                b_editarSector.setVisibility(View.VISIBLE);
                b_guardarSector.setVisibility(View.GONE);
            }
        });

        lv_sector.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mensaje("¿Desea eliminar " + lista.get(position).getDetalle() + "?", position);

                return true;
            }
        });

        b_editarSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editar(et_sector.getText().toString().toUpperCase(),id_sector);
                Toast.makeText(Sector_Editar.this, "GUARDADO.", Toast.LENGTH_SHORT).show();

                cargarDatos();

                et_sector.setText("");

                b_editarSector.setVisibility(View.GONE);
                b_guardarSector.setVisibility(View.VISIBLE);

            }
        });



    }

    private void editar(String detalle, int id){

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put("DETALLE", detalle);
            db.update("SECTOR", values, "ID = " + id, null);

        }catch (Exception e) {
        }

    }

    private void cargarDatos() {

        lista = lista();
        ArrayAdapter<Nodo_Catalogo> adapter = new ArrayAdapter<Nodo_Catalogo>(this, android.R.layout.simple_expandable_list_item_1, lista);
        lv_sector.setAdapter(adapter);

        lista2 = listaString();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, lista2);
        lv_sector.setAdapter(adapter2);

    }

    private ArrayList<Nodo_Catalogo> lista() {
        ArrayList<Nodo_Catalogo> datos = new ArrayList<Nodo_Catalogo>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM SECTOR WHERE ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {

                Nodo_Catalogo linea = new Nodo_Catalogo(c.getInt(0), c.getString(1), c.getString(2));
                datos.add(linea);
            } while (c.moveToNext());
        }
        return datos;
    }

    private ArrayList<String> listaString() {
        ArrayList<String> datos = new ArrayList<String>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM SECTOR WHERE ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                String linea = c.getInt(0) + " " + c.getString(1) + " " + c.getString(2);
                datos.add(linea);
            } while (c.moveToNext());
        }
        return datos;
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
           // finish();
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

    private boolean noDelete(int  id_sector) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO WHERE ID_SECTOR = " + id_sector +" AND ESTADO = 'ACTIVO'";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                return false; // retorna falso si no se puede eliminar el sector
            } while (c.moveToNext());
        }
        return true; // retorna true cuando se puede eliminar e sector
    }

    private void mensaje(final String obj, final int position) {
        //Toast.makeText(this,obj.toString(),Toast.LENGTH_SHORT).show();


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(obj);
        dialogo.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int ID = lista.get(position).getId();

                BaseHelper usuario = new BaseHelper(Sector_Editar.this, "abonar", null, 1);
              final  SQLiteDatabase db = usuario.getWritableDatabase();




                        if(noDelete(ID) ){ //  se puede borrar el sector porque no est siendo usado por alguienn

                            final AlertDialog.Builder dialogo = new AlertDialog.Builder(Sector_Editar.this);
                            dialogo.setMessage("¿ESTA SEGURO DE ELIMINAR?");
                            dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ContentValues update = new ContentValues();
                                    update.put("ESTADO", "DESACTIVO");
                                    db.update("SECTOR", update, "ID = " + ID, null);
                                    db.close();
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


                        }else{
                            Toast.makeText(Sector_Editar.this, "NO SE PUEDE ELIMINAR, EXISTE UN CLIENTE EN EL SECTOR CON SALDO PENDIENTE.", Toast.LENGTH_LONG).show();
                        }


            }
        });
        dialogo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dial = dialogo.create();
        dial.show();
    }

}
