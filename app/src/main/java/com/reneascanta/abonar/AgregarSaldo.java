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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgregarSaldo extends AppCompatActivity {

    Procesos procesos = new Procesos();
    private ArrayList<String> listaMercancia = new ArrayList<>();
    private ArrayList<String> listaMercanciaMostrar = new ArrayList<>();
    private boolean estadoAgregarEditar;

    private int ID_EXTRA, posicionLista;
    private String NOMBRES_EXTRA;
    private double val, valAntiguo;

    EditText et_cantidad, et_valorPrenda, et_descripcion;
    TextView tv_valorPrenda, tv_saldoTotalArtificial;
    Button b_agregarMercancia, b_editarMercancia, b_guardarListaBDD;
    ListView lv_mercanciaSeleccionada;

    TextView tv_nombresClientes, tv_saldoClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_saldo);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA = Integer.parseInt(b.getString("ID"));
            NOMBRES_EXTRA = b.getString("NOMBRES");

        }

        tv_nombresClientes = (TextView) findViewById(R.id.tv_nombresClientes);
        tv_saldoClientes = (TextView) findViewById(R.id.tv_SaldoCliente);
        et_cantidad = (EditText) findViewById(R.id.et_cantidad);
        et_valorPrenda = (EditText) findViewById(R.id.et_valorPrenda);
        et_descripcion = (EditText) findViewById(R.id.et_descripcionPrenda);
        b_agregarMercancia = (Button) findViewById(R.id.b_agregarMercancia);
        b_editarMercancia = (Button) findViewById(R.id.b_editarMercancia);
        b_guardarListaBDD = (Button) findViewById(R.id.b_guardarListaBDD);
        tv_valorPrenda = (TextView) findViewById(R.id.tv_valorPrenda);
        tv_saldoTotalArtificial = (TextView) findViewById(R.id.tv_saldoTotalArtificial);
        lv_mercanciaSeleccionada = (ListView) findViewById(R.id.lv_mercanciaSeleccionada);


        // b_editarMercancia.setVisibility(View.INVISIBLE);
        tv_nombresClientes.setText("CLIENTE: " + NOMBRES_EXTRA);

        // ------------- inicio del nombre de la cuenta y el saldo actual ------------
        String datosCuentaDelUsuario[] = datosCuentaUsuario(ID_EXTRA).split(",");
        valAntiguo = Double.parseDouble(String.valueOf(datosCuentaDelUsuario[2]));
        tv_saldoClientes.setText(" SALDO ACTUAL: " + procesos.valor(String.valueOf(datosCuentaDelUsuario[2])));
        // ------------- fin del nombre de la cuenta y el saldo actual ------------


        val = 0.0;


// ---------------------------  inicio funcion boton para agregar a la lista y mostrar en lista  ---------------------
        b_agregarMercancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b_editarMercancia.setVisibility(View.INVISIBLE);
                b_agregarMercancia.setVisibility(View.VISIBLE);

                if (et_cantidad.getText().toString().equals("") || et_valorPrenda.getText().toString().equals("") || et_descripcion.getText().toString().equals("")) {
                    Toast.makeText(AgregarSaldo.this, "INGRESE TODOS LOS DATOS.", Toast.LENGTH_SHORT).show();
                } else {

                    val = Double.parseDouble(et_cantidad.getText().toString()) * Float.parseFloat(et_valorPrenda.getText().toString());

                    cargarLista(Integer.parseInt(et_cantidad.getText().toString()), Float.parseFloat(et_valorPrenda.getText().toString()), et_descripcion.getText().toString().toUpperCase());
                    double valorArtificialdeLista = recorrerTotalLista();
                    tv_valorPrenda.setText("Subtotal:\n   " + procesos.valor(String.valueOf(valorArtificialdeLista)));
                    tv_saldoTotalArtificial.setText("Total:\n   " + procesos.valor(String.valueOf(valAntiguo + valorArtificialdeLista)));

                    Toast.makeText(AgregarSaldo.this, "SE AGREGO A LA LISTA.", Toast.LENGTH_SHORT).show();
                    et_cantidad.setText("");
                    et_valorPrenda.setText("");
                    et_descripcion.setText("");
                }


            }
        });

        // ---------------------------  fin funcion boton para agregar a la lista y mostrar en lista  ---------------------


// -------------------- inicio funcion cuando realiza un clic en item de la lista cargar los editText para ser modificado --------------
        // ------------------ y realiza cambios en los botones de agregar y editar oculta y muestra cada boton -----------------

        lv_mercanciaSeleccionada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                posicionLista = position;
                b_editarMercancia.setVisibility(View.VISIBLE);
                b_agregarMercancia.setVisibility(View.INVISIBLE);
                //  Toast.makeText(AgregarSaldo.this, listaMercanciaMostrar.get(position).toString(),Toast.LENGTH_SHORT).show();

                String itemLista[] = listaMercanciaMostrar.get(position).split(" ");
                et_cantidad.setText(itemLista[0]);
                et_valorPrenda.setText(String.valueOf(procesos.valorDecimal(String.valueOf(itemLista[3]))));

                String auxDescipcion = "";
                for (int i = 4; i < itemLista.length - 1; i++) {
                    auxDescipcion = auxDescipcion + " " + itemLista[i];

                }
                et_descripcion.setText(auxDescipcion.toUpperCase());


            }
        });
        //   --------------------------------- fin ----------------------
        lv_mercanciaSeleccionada.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mensaje("DESEA QUITAR DE LA LISTA?.", position);
                return true;
            }
        });
        //  --------------- funcion para editar la lista  con el item seleccionado -------------------------------

        b_editarMercancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_cantidad.getText().toString().equals("") || et_valorPrenda.getText().toString().equals("") || et_descripcion.getText().toString().equals("")) {
                    Toast.makeText(AgregarSaldo.this, "INGRESE TODOS LOS DATOS.", Toast.LENGTH_SHORT).show();
                } else {

                    cargarListaEditado(posicionLista, Integer.parseInt(et_cantidad.getText().toString()), Double.parseDouble(et_valorPrenda.getText().toString()), et_descripcion.getText().toString().toUpperCase());

                    val = Float.parseFloat(et_cantidad.getText().toString()) * Float.parseFloat(et_valorPrenda.getText().toString());

                    double valorArtificialdeLista = recorrerTotalLista();
                    tv_valorPrenda.setText("Subtotal:\n   " + procesos.valor(String.valueOf(valorArtificialdeLista)));
                    tv_saldoTotalArtificial.setText("Total:\n   " + procesos.valor(String.valueOf(valAntiguo + valorArtificialdeLista)));


                    et_cantidad.setText("");
                    et_valorPrenda.setText("");
                    et_descripcion.setText("");
                    b_agregarMercancia.setVisibility(View.VISIBLE);
                    b_editarMercancia.setVisibility(View.INVISIBLE);
                }


            }
        });
        // --------------------- fin -------------------------

        // --------------- funcion guardar los datos de la lista en BDD ------------------

        b_guardarListaBDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listaMercanciaMostrar.size() != 0) {

                    Date d = new Date();
                    SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String fecha = fecc.format(d);


                    String aux[] = tv_saldoTotalArtificial.getText().toString().split(" ");

                    double prue = procesos.valorDecimal(aux[3]);
                    agregarCuenta_CuantaSaldos(ID_EXTRA, prue);

                    String[] datosCliente = consulstaCuenta(ID_EXTRA);
                    agregarListaMercanciaBDD(ID_EXTRA, "MAS", fecha, "ACTIVO", listaMercanciaMostrar, Integer.parseInt(datosCliente[3]));
                    Toast.makeText(AgregarSaldo.this, "SE GUARDO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AgregarSaldo.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(AgregarSaldo.this, "NO A AGREGADO NINGUN PRODUCTO.", Toast.LENGTH_SHORT).show();

                }


            }
        });
        // -------------------------- FIN ------------------------

    }


    private void agregarListaMercanciaBDD(int id_usuario, String agregar_merca, String fecha, String estado, ArrayList<String> listaDisplay, int numero_proceso) {

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();
        //  String [] datosCuenta= consulstaCuenta(id_usuario);

        for (int i = 0; i < listaDisplay.size(); i++) {

            String itemLista[] = listaDisplay.get(i).split(" ");


            String auxDescipcion = "";
            for (int j = 4; j < itemLista.length - 1; j++) {
                if (j == 4) {
                    auxDescipcion = itemLista[j];
                } else {

                    auxDescipcion = auxDescipcion + " " + itemLista[j];
                }

            }

            ContentValues values = new ContentValues();
            values.put("ID_CUENTA", id_usuario);
            values.put("PROCESO", agregar_merca);
            values.put("CANTIDAD", Integer.parseInt(itemLista[0]));
            values.put("VALOR", procesos.valorDecimal(itemLista[3]));
            values.put("FECHA", fecha);
            values.put("ESTADO", estado);
            values.put("DETALLE", auxDescipcion);
            values.put("NUMERO_PROCESO", numero_proceso);

            // db.insert("CUENTADETALLE", null, values);
            db.insert("CUENTADETALLE", null, values);


        }
        db.close();
    }

    private void agregarCuenta_CuantaSaldos(int id_usuario, double saldoNuevo) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        //SQLiteDatabase db = abrirSQLWrite();

        // ----------  consulta de la tabla cuenta del usuario para agregar a la tabla saldosCuenta ------------------
        String[] datosCuenta = consulstaCuenta(id_usuario);

        Date d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = fecc.format(d);
        int numero_proceso = (Integer.parseInt(datosCuenta[3]) + 1);
        // ------------------ insertamos en la tabla saldoCuenta ------------------------------
        ContentValues value = new ContentValues();
        value.put("ID_USUARIO", Integer.parseInt(datosCuenta[1]));
        value.put("SALDO", saldoNuevo);
        value.put("FECHA", fecha);
        value.put("NUMERO_PROCESO", numero_proceso);
        value.put("TIPO_PROCESO", "MAS");
        value.put("SALDO_ABONO", 0);
        db.insert("SALDOCUENTA", null, value);

        // --------------------- editar la tabla cuenta de cada usuario ----------------------
        editarCuentaUsuario(id_usuario, saldoNuevo, numero_proceso, fecha);

    }

    private String[] consulstaCuenta(int id_usuario) {

        String[] datos = new String[6];

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        // SQLiteDatabase db = abrirSQLRead();
        String sql = "SELECT * FROM CUENTA WHERE ID_USUARIO = " + id_usuario;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {

                String d = String.valueOf(c.getInt(0)) + " " + String.valueOf(c.getInt(1)) + " " + String.valueOf(c.getInt(2)) + " " + String.valueOf(c.getInt(3)) + " " + c.getString(4);
                datos[0] = String.valueOf(c.getInt(0));
                datos[1] = String.valueOf(c.getInt(1));
                datos[2] = String.valueOf(c.getInt(2));
                datos[3] = String.valueOf(c.getInt(3));
                datos[4] = c.getString(4);
                datos[5] = c.getString(5);

            } while (c.moveToNext());
        }

        return datos;

    }

    private void editarCuentaUsuario(int id_usuario, double saldoNuevo, int numero_proceso, String fecha) {

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put("SALDO", saldoNuevo);
        c.put("NUMERO_PROCESO", numero_proceso);
        c.put("TIPO_PROCESO", "MAS");
        c.put("FECHA", fecha);
        //  db.insert("CUENTA",null, c);

        db.update("CUENTA", c, "ID_USUARIO = " + id_usuario, null);
        db.close();
    }


    private void cargarLista(int cantidad, double valor, String detalle) {
        listaMercanciaMostrar = listaMercanciaSeleccionado(cantidad, valor, detalle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaMercanciaMostrar);
        lv_mercanciaSeleccionada.setAdapter(adapter);
    }

    private void cargarListaEditado(int posicion, int cantidad, double valor, String detalle) {
        listaMercanciaMostrar = EditarlistaMercanciaSeleccionado(posicion, cantidad, valor, detalle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaMercanciaMostrar);
        lv_mercanciaSeleccionada.setAdapter(adapter);
    }

    private void cargarListaBien() {
        listaMercanciaMostrar = itemEliminad();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaMercanciaMostrar);
        lv_mercanciaSeleccionada.setAdapter(adapter);
    }

    private ArrayList<String> itemEliminad() {
        return listaMercancia;
    }

    private ArrayList<String> listaMercanciaSeleccionado(int cantidad, double valor, String detalle) {


        String val = procesos.valor(String.valueOf(valor));
        String vall = procesos.valor(String.valueOf((cantidad * valor)));
        listaMercancia.add(cantidad + "   " + val + " " + detalle + " " + vall);
        return listaMercancia;

    }

    private ArrayList<String> EditarlistaMercanciaSeleccionado(int posicion, int cantidad, double valor, String detalle) {

        String val = procesos.valor(String.valueOf(valor));
        String vall = procesos.valor(String.valueOf((cantidad * valor)));
        //  listaMercancia.add(cantidad+"   "+val+" "+ detalle+" "+ vall);

        listaMercancia.set(posicion, cantidad + "   " + val + " " + detalle + " " + vall);
        return listaMercancia;

    }

    private double recorrerTotalLista() {
        double valorSuma = 0;
        String aux[];
        for (int i = 0; i < listaMercanciaMostrar.size(); i++) {
            aux = listaMercanciaMostrar.get(i).split(" ");
            // float n =);
            String a = String.valueOf(aux[aux.length - 1]);
            valorSuma += procesos.valorDecimal(aux[aux.length - 1]);
        }
        return valorSuma;
    }

    private String datosCuentaUsuario(int id_parametro) {
        String datos = "";

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM CUENTA  WHERE  ID_USUARIO = " + id_parametro;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos = c.getInt(0) + "," + c.getInt(1) + "," + c.getFloat(2) + "," + c.getString(3);

            } while (c.moveToNext());
        }
        return datos;
    }

    private void mensaje(final String obj, final int position) {
        //Toast.makeText(this,obj.toString(),Toast.LENGTH_SHORT).show();


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(obj);
        dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                listaMercancia.remove(position);
                cargarListaBien();
                double valorArtificialdeLista = recorrerTotalLista();
                tv_valorPrenda.setText("Subtotal:\n   " + procesos.valor(String.valueOf(valorArtificialdeLista)));
                tv_saldoTotalArtificial.setText("Total:\n   " + procesos.valor(String.valueOf(valAntiguo + valorArtificialdeLista)));


            }
        });
        dialogo.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        Dialog dial = dialogo.create();
        dial.show();
    }
}
