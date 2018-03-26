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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DevolucionMercancia extends AppCompatActivity {

    private Procesos procesos = new Procesos();
    private TextView tv_datosUsuarioDevolucion, tv_detalladoDevolucion, tv_valorPrendaDevolucion, tv_saldoTotalArtificialDevolucion, tv_SaldoClienteAbonar;
    private int ID_EXTRA, posicionLista;
    private String NOMBRES_EXTRA;
    private ArrayList<String> listaFechas, listaProductosFechas;
    private Spinner sd_devolucionFechas, sd_devolucionProductFecha;

    private ListView lv_mercanciaSeleccionada;
    private Button b_guardarDevolucion, b_agregarListaDevolucion, b_cantidadMas, b_cantidadMenos, b_editarListaDevolucion;
    private EditText et_detalleDevolucion, et_cantidadDevolucion;
    private ArrayList<String> listaMercanciaDevolucion = new ArrayList<>();
    private ArrayList<String> listaMercanciaMostrarDevolucion = new ArrayList<>();

    private int cantidad = 0;
    private double val, valAntiguo;
    private  String tempDatosParentesis ;

    private String[] detalleProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion_mercancia);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA = Integer.parseInt(b.getString("ID"));
            NOMBRES_EXTRA = b.getString("NOMBRES");

        }

        tv_datosUsuarioDevolucion = (TextView) findViewById(R.id.tv_datosUsuarioDevolucion);
        tv_detalladoDevolucion = (TextView) findViewById(R.id.tv_detalladoDevolucion);
        sd_devolucionFechas = (Spinner) findViewById(R.id.sd_devolucionFechas);
        sd_devolucionProductFecha = (Spinner) findViewById(R.id.sd_devolucionProductFecha);
        lv_mercanciaSeleccionada = (ListView) findViewById(R.id.lv_mercanciaSeleccionada);
        b_agregarListaDevolucion = (Button) findViewById(R.id.b_agregarListaDevolucion);
        b_guardarDevolucion = (Button) findViewById(R.id.b_guardarDevolucion);
        et_cantidadDevolucion = (EditText) findViewById(R.id.et_cantidadDevolucion);
        et_detalleDevolucion = (EditText) findViewById(R.id.et_detalleDevolucion);
        b_cantidadMas = (Button) findViewById(R.id.b_cantidadMas);
        b_cantidadMenos = (Button) findViewById(R.id.b_cantidadMenos);
        tv_valorPrendaDevolucion = (TextView) findViewById(R.id.tv_valorPrendaDevolucion);
        tv_saldoTotalArtificialDevolucion = (TextView) findViewById(R.id.tv_saldoTotalArtificialDevolucion);
        tv_SaldoClienteAbonar = (TextView) findViewById(R.id.tv_SaldoClienteAbonar);
        b_editarListaDevolucion = (Button) findViewById(R.id.b_editarListaDevolucion);


        tv_datosUsuarioDevolucion.setText("CLIENTE: " + NOMBRES_EXTRA);
        // tv_prueba.setText(productosEscogidoClienteFechas(ID_EXTRA));
        // ------------- inicio del nombre de la cuenta y el saldo actual ------------
        String datosCuentaDelUsuario[] = datosCuentaUsuario(ID_EXTRA).split(",");
        valAntiguo = Double.parseDouble(String.valueOf(datosCuentaDelUsuario[2]));
        tv_SaldoClienteAbonar.setText(" SALDO ACTUAL: " + procesos.valor(String.valueOf(datosCuentaDelUsuario[2])));
        // ------------- fin del nombre de la cuenta y el saldo actual ------------
        cargarDatosfechas(ID_EXTRA);
        val = 0.0;


        // -------------------- primer drop -----------------------------------------
        sd_devolucionFechas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(DevolucionMercancia.this, listaFechas.get(position),Toast.LENGTH_SHORT).show();
                cargarDatosProductosFecha(ID_EXTRA, listaFechas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // ------------------------- segundo drop de productos ---------------------
        sd_devolucionProductFecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cantidad = 1;
                et_cantidadDevolucion.setText(String.valueOf(cantidad));
                tv_detalladoDevolucion.setText(listaProductosFechas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // ------------- aumentara cantiad por botones mas y menos---------------------
        b_cantidadMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidad++;
                et_cantidadDevolucion.setText(String.valueOf(cantidad));

            }
        });
        b_cantidadMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad > 1) {
                    cantidad--;
                    et_cantidadDevolucion.setText(String.valueOf(cantidad));
                }
            }
        });

        //------------------------- goton agregar a lista para poder realizar la devolucion --------------------
        b_agregarListaDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                b_editarListaDevolucion.setVisibility(View.GONE);
                //   b_agregarListaDevolucion.setVisibility(View.VISIBLE);
                detalleProducto = tv_detalladoDevolucion.getText().toString().split(" ");
                String detalleProductoDevolucion = detalleProducto(detalleProducto);

                val = Double.parseDouble(et_cantidadDevolucion.getText().toString()) * Double.parseDouble(detalleProducto[detalleProducto.length - 1]);

                cargarLista(Integer.parseInt(et_cantidadDevolucion.getText().toString()), Double.parseDouble(detalleProducto[detalleProducto.length - 1]), detalleProductoDevolucion.toUpperCase(), et_detalleDevolucion.getText().toString());
                double valorArtificialdeLista = recorrerTotalLista();
                tv_valorPrendaDevolucion.setText("Subtotal:\n   " + procesos.valor(String.valueOf(valorArtificialdeLista)));
                tv_saldoTotalArtificialDevolucion.setText("Total:\n   " + procesos.valor(String.valueOf(valAntiguo - valorArtificialdeLista)));

                Toast.makeText(DevolucionMercancia.this, "SE AGREGO A LA LISTA.", Toast.LENGTH_SHORT).show();
                et_detalleDevolucion.setText("");
                et_cantidadDevolucion.setText("1");
                //  et_cantidad.setText("");
                // et_valorPrenda.setText("");
                // et_descripcion.setText("");


            }
        });
        // -------------------- inicio funcion cuando realiza un clic en item de la lista cargar los editText para ser modificado --------------
        // ------------------ y realiza cambios en los botones de agregar y editar oculta y muestra cada boton -----------------

        lv_mercanciaSeleccionada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                b_editarListaDevolucion.setVisibility(View.VISIBLE);
                // b_guardarDevolucion.setVisibility(View.GONE);
                posicionLista = position;


                char temp []= listaMercanciaMostrarDevolucion.get(position).toCharArray();
                String tempDatos="";
                tempDatosParentesis = "";
                boolean estado= true;
                for (int j =0 ; j < temp.length ;j++){
                    if(temp[j] != '(' && estado){
                        tempDatos = tempDatos + temp[j];
                    }else
                    {
                        estado=false;
                        tempDatosParentesis = tempDatosParentesis + temp[j];
                    }
                }

                String itemLista[] = tempDatos.split(" ");
                et_cantidadDevolucion.setText(itemLista[0]);

                String auxDescipcion = "";
                for (int i = 4; i < itemLista.length - 2; i++) {
                    auxDescipcion = auxDescipcion + " " + itemLista[i];

                }

                tv_detalladoDevolucion.setText("");
               //  double valAux = Double.parseDouble(itemLista[itemLista.length - 1]) / Double.parseDouble(itemLista[0]);
                tv_detalladoDevolucion.setText(auxDescipcion + " " + String.valueOf(itemLista[3]));


                char detalleFalla [] = tempDatosParentesis.toCharArray();
                String auxDetalleFalla = "";

                for(int i = 1; i < detalleFalla.length-1; i++ ){
                    auxDetalleFalla = auxDetalleFalla+detalleFalla[i];
                }
                et_detalleDevolucion.setText("");
                et_detalleDevolucion.setText(auxDetalleFalla);

            }
        });
        //   --------------------------------- fin ----------------------
        // ------------------ quitar product de a lista --------
        lv_mercanciaSeleccionada.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mensaje("DESEA QUITAR DE LA LISTA?.", position);
                return true;
            }
        });
        // ------------------- fin quitar producto e la lista -------------

        b_editarListaDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                char temp []= listaMercanciaMostrarDevolucion.get(posicionLista).toCharArray();


                //----------------------------------
                String prueb [] =  procesos.areglo(listaMercanciaMostrarDevolucion.get(posicionLista));




                 double vvv = procesos.valorDecimal( prueb[1]);
                //------------------end


                double valorArtificialdeLista = recorrerTotalLista();
                cargarListaEditado(posicionLista,
                        Integer.parseInt(et_cantidadDevolucion.getText().toString()),
                        vvv,
                        prueb[2].toUpperCase(), et_detalleDevolucion.getText().toString());

                val = Double.parseDouble(et_cantidadDevolucion.getText().toString()) * Double.parseDouble(detalleProducto[detalleProducto.length - 1]);

                tv_valorPrendaDevolucion.setText("Subtotal:\n   " + procesos.valor(String.valueOf(valorArtificialdeLista)));
                tv_saldoTotalArtificialDevolucion.setText("Total:\n   " + procesos.valor(String.valueOf(valAntiguo - valorArtificialdeLista)));


                et_detalleDevolucion.setText("");
                et_cantidadDevolucion.setText("1");

                b_editarListaDevolucion.setVisibility(View.GONE);
                //   b_agregarListaDevolucion.setVisibility(View.VISIBLE);
            }
        });

        // --------------- funcion guardar los datos de la lista en BDD ------------------

        b_guardarDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listaMercanciaMostrarDevolucion.size() != 0) {

                    Date d = new Date();
                    SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String fecha = fecc.format(d);


                    String aux[] = tv_saldoTotalArtificialDevolucion.getText().toString().split(" ");

                    double valorDevolucion = recorrerTotalLista();
                    double prue = procesos.valorDecimal(aux[3]);
                    agregarCuenta_CuantaSaldos(ID_EXTRA, prue, valorDevolucion);

                    String[] datosCliente = consulstaCuenta(ID_EXTRA);
                    agregarListaMercanciaBDD(ID_EXTRA, "DEVOLUCION", fecha, "ACTIVO", listaMercanciaMostrarDevolucion, Integer.parseInt(datosCliente[3]));
                    Toast.makeText(DevolucionMercancia.this, "SE GUARDO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DevolucionMercancia.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(DevolucionMercancia.this, "NO EXISTE NINGUN PRODUCTO EN DEVOLUCIÒN.", Toast.LENGTH_SHORT).show();

                }


            }
        });
        // -------------------------- FIN ------------------------

    }


    private void cargarLista(int cantidad, double valor, String detalle, String motivoDevolucion) {
        listaMercanciaMostrarDevolucion = listaMercanciaSeleccionado(cantidad, valor, detalle, motivoDevolucion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaMercanciaMostrarDevolucion);
        lv_mercanciaSeleccionada.setAdapter(adapter);
    }

    private ArrayList<String> listaMercanciaSeleccionado(int cantidad, double valor, String detalle, String motivoDevolucion) {


        String val = procesos.valor(String.valueOf(valor));
        String vall = procesos.valor(String.valueOf((cantidad * valor)));
        listaMercanciaDevolucion.add(cantidad + "   " + val + " " + detalle + " " + vall + " (" + motivoDevolucion + ")");
        return listaMercanciaDevolucion;

    }

    private void cargarListaEditado(int posicion, int cantidad, double valor, String detalle, String motivoDevolucion) {
        listaMercanciaMostrarDevolucion = EditarlistaMercanciaSeleccionado(posicion, cantidad, valor, detalle, motivoDevolucion);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaMercanciaMostrarDevolucion);
        lv_mercanciaSeleccionada.setAdapter(adapter);
    }

    private ArrayList<String> EditarlistaMercanciaSeleccionado(int posicion, int cantidad, double valor, String detalle, String motivoDevolucion) {

        String val = procesos.valor(String.valueOf(valor));
        String vall = procesos.valor(String.valueOf((cantidad * valor)));
        //  listaMercancia.add(cantidad+"   "+val+" "+ detalle+" "+ vall);

        listaMercanciaDevolucion.set(posicion, cantidad + "   " + val + " " + detalle + " " + vall + " (" + motivoDevolucion + ")");
        return listaMercanciaDevolucion;

    }

    private double recorrerTotalLista() {
        double valorSuma = 0;
        String aux[];
        for (int i = 0; i < listaMercanciaMostrarDevolucion.size(); i++) {

            char temp []= listaMercanciaMostrarDevolucion.get(i).toCharArray();
            String tempDatos="";
            for (int j =0 ; j < temp.length-1 ;j++){
               if(temp[j] != '('){
                   tempDatos = tempDatos + temp[j];
               }else
               {
                   break;
               }
            }

           // aux = listaMercanciaMostrarDevolucion.get(i).split(" ");
                aux =tempDatos.split(" ");
            //  String a = String.valueOf(aux[aux.length-1]);
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


    // ------------------------ cargar los producto de tal fecha seleccionado --------------------------
    private void cargarDatosProductosFecha(int id_usuario, String fecha) {
        listaProductosFechas = productosEscogidoClienteProducto(id_usuario, fecha);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaProductosFechas);
        sd_devolucionProductFecha.setAdapter(adapter);
    }

    private ArrayList<String> productosEscogidoClienteProducto(int id_usuario, String fecha) {
        ArrayList<String> datos = new ArrayList<String>();

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        //  String sql =  "SELECT * FROM CUENTADETALLE WHERE ID_CUENTA = "+id_usuario +" GROUP BY DETALLE";
        String sql = "SELECT * FROM CUENTADETALLE WHERE ID_CUENTA = " + id_usuario + " AND FECHA = '" + fecha + "'";
        ;

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                //               dato=  dato+c.getInt(1)+" "+c.getString(2)+" "+c.getFloat(3)+" "+c.getDouble(4)+" "+c.getString(5)+" "+c.getString(6)+" "+c.getString(7)+" "+c.getInt(8)+"\n";

                datos.add(c.getString(7) + " " + c.getFloat(4));

            } while (c.moveToNext());
        }
        return datos;
    }

    // ------------------------ fin cargar los producto de tal fecha seleccionado --------------------------
    // ------------------ cargar las fechas que cogio prendas el cliente -----------------------------
    private void cargarDatosfechas(int id_usuario) {
        listaFechas = productosEscogidoClienteFechas(id_usuario);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaFechas);
        sd_devolucionFechas.setAdapter(adapter);
    }

    private ArrayList<String> productosEscogidoClienteFechas(int id_usuario) {
        ArrayList<String> datos = new ArrayList<String>();

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        //  String sql =  "SELECT * FROM CUENTADETALLE WHERE ID_CUENTA = "+id_usuario +" GROUP BY DETALLE";
        String sql = "SELECT * FROM CUENTADETALLE WHERE ID_CUENTA = " + id_usuario + " GROUP BY FECHA";
        ;

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                //               dato=  dato+c.getInt(1)+" "+c.getString(2)+" "+c.getFloat(3)+" "+c.getDouble(4)+" "+c.getString(5)+" "+c.getString(6)+" "+c.getString(7)+" "+c.getInt(8)+"\n";

                datos.add(c.getString(5));

            } while (c.moveToNext());
        }
        return datos;
    }
    // ------------------  fin cargar las fechas que cogio prendas el cliente -----------------------------


    private String detalleProducto(String[] areglo) {
        String dato = "";
        for (int i = 0; i < areglo.length - 1; i++) {
            if (i == areglo.length - 1) {
                dato = dato + areglo[i];
            } else {

                dato = dato + areglo[i] + " ";
            }
        }
        return dato;
    }


    private void agregarCuenta_CuantaSaldos(int id_usuario, double saldoNuevo, double abono) {
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
        value.put("TIPO_PROCESO", "DEVOLUCION");
        value.put("SALDO_ABONO", abono);
        db.insert("SALDOCUENTA", null, value);

        // --------------------- editar la tabla cuenta de cada usuario ----------------------
        editarCuentaUsuario(id_usuario, saldoNuevo, numero_proceso, fecha);

    }

    private void editarCuentaUsuario(int id_usuario, double saldoNuevo, int numero_proceso, String fecha) {

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put("SALDO", saldoNuevo);
        c.put("NUMERO_PROCESO", numero_proceso);
        c.put("TIPO_PROCESO", "DEVOLUCION");
        c.put("FECHA", fecha);
        //  db.insert("CUENTA",null, c);

        db.update("CUENTA", c, "ID_USUARIO = " + id_usuario, null);
        db.close();
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


    private void agregarListaMercanciaBDD(int id_usuario, String agregar_merca, String fecha, String estado, ArrayList<String> listaDisplay, int numero_proceso) {

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();
        //  String [] datosCuenta= consulstaCuenta(id_usuario);

        for (int i = 0; i < listaDisplay.size(); i++) {

            String itemLista[] = listaDisplay.get(i).split(" ");


            String auxDescipcion = "";
            for (int j = 4; j < itemLista.length - 2; j++) {
                if (j == 4) {
                    auxDescipcion = itemLista[j];
                } else {

                    auxDescipcion = auxDescipcion + " " + itemLista[j];
                }

            }
            auxDescipcion = auxDescipcion + " " + itemLista[itemLista.length - 1];

            ContentValues values = new ContentValues();
            values.put("ID_CUENTA", id_usuario);
            values.put("PROCESO", agregar_merca);
            values.put("CANTIDAD", Integer.parseInt(itemLista[0]));
            values.put("VALOR", procesos.valorDecimal(itemLista[3]));
            values.put("FECHA", fecha);
            values.put("ESTADO", estado);
            values.put("DETALLE", auxDescipcion);
            values.put("NUMERO_PROCESO", numero_proceso);

            db.insert("CUENTADETALLE", null, values);


        }
        db.close();
    }

    private void mensaje(final String obj, final int position) {
        //Toast.makeText(this,obj.toString(),Toast.LENGTH_SHORT).show();


        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setMessage(obj);
        dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                listaMercanciaDevolucion.remove(position);
                cargarListaBien();
                double valorArtificialdeLista = recorrerTotalLista();
                tv_valorPrendaDevolucion.setText("Subtotal:\n   " + procesos.valor(String.valueOf(valorArtificialdeLista)));
                tv_saldoTotalArtificialDevolucion.setText("Total:\n   " + procesos.valor(String.valueOf(valAntiguo - valorArtificialdeLista)));


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

    private void cargarListaBien() {
        listaMercanciaMostrarDevolucion = itemEliminad();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaMercanciaMostrarDevolucion);
        lv_mercanciaSeleccionada.setAdapter(adapter);
    }

    private ArrayList<String> itemEliminad() {
        return listaMercanciaDevolucion;
    }
}
