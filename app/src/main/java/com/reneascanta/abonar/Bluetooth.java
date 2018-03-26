package com.reneascanta.abonar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {

    // -------------  varibles para el bluetooth --------------
    Button b_on, b_off, b_disc, b_listaBluetooth;
    ListView lv_listaBluetooth;
    private static final int REQUEST_ENABLE = 0;
    private static final int REQUEST_DISCONERABLE = 0;
    BluetoothAdapter adaptador;
    //---------- variables para generar txt ---------------------
    // ------------ fin variables para bluetooth ----------------
  //  EditText et_pruebaTxt;
    Button b_txt, b_save;

    // ------------ fin variables para generar txt --------------
    // -----------------  enviar txt al email -------------------
  //  Button b_enviarEmail;
    // ------------------ fin enviar txt al email -------------------

    private ArrayList<Nodo_Catalogo> operadoresMovil;
    private ArrayList<Nodo_Catalogo> sector;
    private ArrayList<Nodo_Usuario> usuario;
    private ArrayList<Nodo_SaldoCuenta> saldoCuenta;
    private ArrayList<Nodo_Cuenta> cuenta;
    private ArrayList<Nodo_CuentaDetalle> cuentaDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        b_on = (Button) findViewById(R.id.b_on);
        b_off = (Button) findViewById(R.id.b_off);
        b_disc = (Button) findViewById(R.id.b_disc);
        b_listaBluetooth = (Button) findViewById(R.id.b_listaBluetooth);
        lv_listaBluetooth = (ListView) findViewById(R.id.lv_listaBluetooth);


       // et_pruebaTxt = (EditText) findViewById(R.id.et_pruebaTxt);
        b_txt = (Button) findViewById(R.id.b_txt);
        b_save = (Button) findViewById(R.id.b_save);


      //  b_enviarEmail = (Button) findViewById(R.id.b_enviarEmail);

        // --------------- bluetooth ------------------------
        adaptador = BluetoothAdapter.getDefaultAdapter();
        if (adaptador == null) {
            Toast.makeText(Bluetooth.this, " Bluetooth no soportado.", Toast.LENGTH_SHORT).show();
            finish();
        }


        b_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE);

                // mostrar en lista los bluetooth activos
                Set<BluetoothDevice> pairesDevices = adaptador.getBondedDevices();
                ArrayList<String> devices = new ArrayList<>();
                for (BluetoothDevice bt : pairesDevices) {
                    devices.add(bt.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Bluetooth.this, android.R.layout.simple_expandable_list_item_1, devices);
                lv_listaBluetooth.setAdapter(adapter);

            }
        });

        b_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!adaptador.isDiscovering()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCONERABLE);
                }

                adaptador.disable();
            }
        });

        /*
        b_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!adaptador.isDiscovering()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCONERABLE);
                }
            }
        });

        b_listaBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> pairesDevices = adaptador.getBondedDevices();
                ArrayList<String> devices = new ArrayList<>();
                for (BluetoothDevice bt : pairesDevices) {
                    devices.add(bt.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Bluetooth.this, android.R.layout.simple_expandable_list_item_1, devices);
                lv_listaBluetooth.setAdapter(adapter);

            }
        });

        */
// ------------------- fin bluetooth ------------------


        b_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 recuperarSDCard(v, "datos.txt","Download");
              //  recuperarInternal(v,"datos.txt","Download");

            }
        });
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  grabar(v);

                if( grabarMemoriaInterna(v, "datos.txt") ){


                    if( grabarSDCard(v, "datos.txt","Download")  ){

                         Toast.makeText(Bluetooth.this, "LISTO, EL ARCHIVO ESTA EN LA CARPETA DOWNLOAD!!.", Toast.LENGTH_SHORT).show();

                    }else
                        {
                        Toast.makeText(Bluetooth.this, "ERROR EN MEMORIA SDCARD", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Bluetooth.this, "ERROR EN MEMORIA INTERNA", Toast.LENGTH_SHORT).show();
                }

              //  grabarSDCard(v, "datos.txt");
            }
        });


        /*
        b_enviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail("Wolf100889@gmail.com", "notas2.txt");
            }
        });

*/

    }

    private void enviarEmail(String email, String txt) {
        String[] mailto = {email};
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), txt));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Datos app.");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Archivo para poder actualizar los datos. ");
        //emailIntent.setType("message/rfc822");
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send email using:"));

    }


    private boolean existe(String[] archivos, String archivoBuscar) {
        for (int f = 0; f < archivos.length; f++) {
            if (archivoBuscar.equals(archivos[f])) {
                return true;
            }
        }
        return false;

    }



    private boolean grabarSDCard(View v, String nameArchivo, String ruta) {
        boolean estado = false;
        String datos_t_sector = T_sector();
        String datos_t_operadorMovil = T_operadorMovil();
        String datos_t_usuario = T_usuario();
        String datos_t_saldoCuenta = T_saldosCuenta();
        String datos_t_cuenta = T_cuenta();
        String datos_t_cuentaDetalle = T_cuentaDetalle();
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/"+ruta, nameArchivo);

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

            osw.write(datos_t_operadorMovil + "/" + datos_t_sector + "/" + datos_t_usuario + "/" + datos_t_saldoCuenta + "/" + datos_t_cuenta + "/" + datos_t_cuentaDetalle);
            osw.flush();
            osw.close();
            estado= true;

        } catch (IOException e) {

            estado = false;
        }

        return  estado;

     //   Toast.makeText(Bluetooth.this, "LISTO!!.", Toast.LENGTH_SHORT).show();
      //  et_pruebaTxt.setText("");
    }
    private boolean grabarMemoriaInterna(View v, String nameArchivo) {
        boolean estado =  false;
        String datos_t_sector = T_sector();
        String datos_t_operadorMovil = T_operadorMovil();
        String datos_t_usuario = T_usuario();
        String datos_t_saldoCuenta = T_saldosCuenta();
        String datos_t_cuenta = T_cuenta();
        String datos_t_cuentaDetalle = T_cuentaDetalle();
        try {
          //  File tarjeta = Environment.getExternalStorageDirectory();
          //  File file = new File(tarjeta.getAbsolutePath(), nameArchivo);



            OutputStreamWriter osw = new OutputStreamWriter(openFileOutput(nameArchivo, Activity.MODE_PRIVATE));

            osw.write(datos_t_operadorMovil + "/" + datos_t_sector + "/" + datos_t_usuario + "/" + datos_t_saldoCuenta + "/" + datos_t_cuenta + "/" + datos_t_cuentaDetalle);
            osw.flush();
            osw.close();
          //  Toast.makeText(Bluetooth.this,"LISTO.!!", Toast.LENGTH_SHORT).show();

            estado = true;
        } catch (IOException e) {

            estado = false;
        }

        return  estado;
      //  Toast.makeText(Bluetooth.this, "LISTO!!.", Toast.LENGTH_SHORT).show();
        //  et_pruebaTxt.setText("");
    }


    private void recuperarInternal(View v, String nameArchivo, String ruta) {

        operadoresMovil = new ArrayList<>();
        sector = new ArrayList<>();
        usuario = new ArrayList<>();
        saldoCuenta = new ArrayList<>();
        cuenta = new ArrayList<>();
        cuentaDetalle = new ArrayList<>();


        String[] archivos = fileList();
        if (existe(archivos, nameArchivo)){
            try {
              //  FileInputStream fIn = new FileInputStream(file);
              //  InputStreamReader archivo = new InputStreamReader(fIn);
              //  BufferedReader br = new BufferedReader(archivo);


                InputStreamReader archivo = new InputStreamReader(openFileInput(nameArchivo));

                BufferedReader br = new BufferedReader(archivo);

                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    todo = todo + linea + "\n";
                    linea = br.readLine();
                }
                String aregloDatos[] = todo.split("/");


                //   insert_operador(operador);
                operadoresMovil = cargarOperador(aregloDatos[0]);
                sector = cargarSector(aregloDatos[1]);
                usuario = cargarUsuario(aregloDatos[2]);
                saldoCuenta = cargarSaldoCuenta(aregloDatos[3]);
                cuenta = cargarCuenta(aregloDatos[4]);
                cuentaDetalle = cargarDetalleCuenta(aregloDatos[5]);

                insert_sector(sector);
                insert_OperadorMovil(operadoresMovil);
                insert_usuario(usuario);
                insert_saldoCuenta(saldoCuenta);
                //  insert_cuent(cuenta);
                insert_cuentDetalle(cuentaDetalle);

                //int a = 4;

                br.close();
                archivo.close();
                Toast.makeText(Bluetooth.this,"SINCRONIZACION EXITOSA.",Toast.LENGTH_SHORT).show();
                //  et_pruebaTxt.setText(todo);

            } catch (IOException e) {
            }
        }else{
            Toast.makeText(Bluetooth.this,"EL ARCHIVO NO SE ENCONTRO EN LA CARPETA DOWNLOAD.",Toast.LENGTH_SHORT).show();

        }

    }
    private void recuperarSDCard(View v, String nameArchivo, String ruta) {

        operadoresMovil = new ArrayList<>();
        sector = new ArrayList<>();
        usuario = new ArrayList<>();
        saldoCuenta = new ArrayList<>();
        cuenta = new ArrayList<>();
        cuentaDetalle = new ArrayList<>();

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath()+"/"+ruta, nameArchivo);

        if(file.exists()){
            try {
                FileInputStream fIn = new FileInputStream(file);
                InputStreamReader archivo = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    todo = todo + linea + "\n";
                    linea = br.readLine();
                }
                String aregloDatos[] = todo.split("/");


                //   insert_operador(operador);
                operadoresMovil = cargarOperador(aregloDatos[0]);
                sector = cargarSector(aregloDatos[1]);
                usuario = cargarUsuario(aregloDatos[2]);
                saldoCuenta = cargarSaldoCuenta(aregloDatos[3]);
                cuenta = cargarCuenta(aregloDatos[4]);
                cuentaDetalle = cargarDetalleCuenta(aregloDatos[5]);

                insert_sector(sector);
                insert_OperadorMovil(operadoresMovil);
                insert_usuario(usuario);
                insert_saldoCuenta(saldoCuenta);
                //  insert_cuent(cuenta);
                insert_cuentDetalle(cuentaDetalle);

                int a = 4;

                br.close();
                archivo.close();
                Toast.makeText(Bluetooth.this,"SINCRONIZACION EXITOSA.",Toast.LENGTH_SHORT).show();
                //  et_pruebaTxt.setText(todo);

            } catch (IOException e) {
            }
        }else{
            Toast.makeText(Bluetooth.this,"EL ARCHIVO NO SE ENCONTRO EN LA CARPETA DOWNLOAD.",Toast.LENGTH_SHORT).show();

        }

    }
    // ------------------------- BUSCAR DETALLE EN LAS TABLAS -----------------------
    private boolean exist_sector(String detalle){
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM SECTOR WHERE DETALLE='"+ detalle+"' ";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                return false; //  el sector existe y no es necesario cambiar de id en las listas
            }while (c.moveToNext());
        }

        return true; // el sector no existe y tocara cambiar los id_sector en las usuario
    }
    private int get_idSector(String detalle){
        int id=0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM SECTOR WHERE DETALLE='"+ detalle+"' ";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }

        return id; // el sector no existe y tocara cambiar los id_sector en las usuario
    }

    private boolean exist_operadorMovil(String detalle){
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM OPERADORMOVIL WHERE DETALLE='"+ detalle+"' ";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                return false; //  el sector existe y no es necesario cambiar de id en las listas
            }while (c.moveToNext());
        }

        return true; // el sector no existe y tocara cambiar los id_sector en las usuario
    }
    private int get_idOperadorMovil(String detalle){
        int id=0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM OPERADORMOVIL WHERE DETALLE='"+ detalle+"' ";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }

        return id; // el sector no existe y tocara cambiar los id_sector en las usuario
    }

    private boolean exist_usuario(String nombres, int id_sector){
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO WHERE NOMBRES='"+ nombres+"' AND ID_SECTOR = "+id_sector;
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                return false; //  el usuario existe y no es necesario cambiar de id en las listas
            }while (c.moveToNext());
        }

        return true; // el sector no existe y tocara cambiar los id_sector en las usuario
    }
    private int get_idUsuario(String nombres, int id_sector){
        int id=0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO WHERE NOMBRES='"+ nombres+"' AND ID_SECTOR = "+id_sector;
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }

        return id; // el sector no existe y tocara cambiar los id_sector en las usuario
    }

    private int get_idUsuario(ArrayList<Nodo_Usuario> usuarioBuscar, int posicion){
        int id=0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT ID FROM USUARIO WHERE NOMBRES='"+ usuarioBuscar.get(posicion).getNOMBRES()+"' AND " +
                "ID_SECTOR = "+usuarioBuscar.get(posicion).getID_SECTOR();
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
                break;
            }while (c.moveToNext());
        }

        return id; // el sector no existe y tocara cambiar los id_sector en las usuario
    }

    private int maxNumeroProcesoBDD(String nombreTabla,int id_usuario){
        int id=0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT MAX(NUMERO_PROCESO) FROM "+nombreTabla+" WHERE ID_USUARIO="+ id_usuario;
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }

        return id; // el sector no existe y tocara cambiar los id_sector en las usuario
    }
    private int maxNumeroProcesoBDD2(String nombreTabla,int id_usuario){
        int id=0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
       // String aux = auxBDD(nombreTabla, id_usuario);
        String sql = "SELECT MAX(NUMERO_PROCESO) FROM "+nombreTabla+" WHERE ID_CUENTA="+ id_usuario;
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }

        return id; // el sector no existe y tocara cambiar los id_sector en las usuario
    }

    private String auxBDD(String nombreTabla,int id_usuario){
        String dato="";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM "+nombreTabla+" WHERE ID_CUENTA="+ id_usuario;
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                dato= dato+ c.getInt(0)+" "+c.getInt(1)+" "+c.getString(2)+" "+c.getInt(3)+" "+c.getFloat(4)+" "+c.getString(5)+" "+c.getString(6)+" "+c.getString(7)+" "+c.getInt(8)+"\n";
            }while (c.moveToNext());
        }

        return dato; // el sector no existe y tocara cambiar los id_sector en las usuario
    }

    private boolean exist_cuenta(int id_usuario){
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM CUENTA WHERE ID_USUARIO= "+ id_usuario+" ";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToFirst()){
            do{
                return true; //  el sector existe y no es necesario cambiar de id en las listas
            }while (c.moveToNext());
        }

        return false; // el sector no existe y tocara cambiar los id_sector en las usuario
    }
    // ------------------------- FIN BUSCAR DETALLE EN LAS TABLAS ---------------------
    // -------------------------- INSERTAR DATOS DE ARCHIVO TXT --------------------------------
    private void insert_sector(ArrayList<Nodo_Catalogo> sector) {
        BaseHelper base = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = base.getWritableDatabase();

        for(int i=0 ; i<sector.size(); i++){
            if(exist_sector(sector.get(i).getDetalle())){ // para evitar la duplicacion del mismo sector
                guardarSector(sector.get(i).getDetalle(),sector.get(i).getEstado()); // guardamos nuevo sector y tenemos nuevo id
            }
            int idNuevoSector = get_idSector(sector.get(i).getDetalle()); // obtenemos el nuevo id del sector guardado
            for(int j = 0 ; j < usuario.size() ; j++){
                if(usuario.get(j).getID_SECTOR()== sector.get(i).getId()){
                    usuario.get(j).setID_SECTOR(idNuevoSector+100000);
                }
            }
        }

    }
    private void guardarSector(String detalle, String estado) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("DETALLE", detalle);
            contentValues.put("ESTADO", estado);
            db.insert("SECTOR", null, contentValues);
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR EN SECTOR.", Toast.LENGTH_SHORT).show();
        }


    }


    private void insert_OperadorMovil(ArrayList<Nodo_Catalogo> operador) {
        BaseHelper base = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = base.getWritableDatabase();

        for(int i=0 ; i<operador.size(); i++){
            if(exist_operadorMovil(operador.get(i).getDetalle())){ // para evitar la duplicacion del mismo sector
                guardarOperador(operador.get(i).getDetalle(),operador.get(i).getEstado()); // guardamos nuevo sector y tenemos nuevo id
            }
            int idNuevoSector = get_idOperadorMovil(operador.get(i).getDetalle()); // obtenemos el nuevo id del sector guardado
            for(int j = 0 ; j < usuario.size() ; j++){
                if(usuario.get(j).getID_OPERADORMOVIL()== operador.get(i).getId()){
                    usuario.get(j).setID_OPERADORMOVIL(idNuevoSector+100000);
                }
            }
        }

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
        } catch (Exception e) {

            Toast.makeText(this, "ERROR EN OPERADOR.", Toast.LENGTH_SHORT).show();
        }


    }


    private void insert_usuario(ArrayList<Nodo_Usuario> nuevoUsuario) {
        BaseHelper base = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = base.getWritableDatabase();

        eliminarMiles(usuario); // ELIMINAR LOS MILES QUE AGREGAR EN ID SECTOR E ID OPERADOR MOVIL
        for(int i=0 ; i<nuevoUsuario.size(); i++){
             if(exist_usuario(nuevoUsuario.get(i).getNOMBRES(),(nuevoUsuario.get(i).getID_SECTOR()))){ // para evitar la duplicacion del mismo sector
                guardarNuevo(nuevoUsuario.get(i).getNOMBRES(),nuevoUsuario.get(i).getALIAS(),
                        nuevoUsuario.get(i).getDETALLE(),nuevoUsuario.get(i).getFOTO(),
                        (nuevoUsuario.get(i).getID_SECTOR()),
                        (nuevoUsuario.get(i).getID_OPERADORMOVIL()),
                        nuevoUsuario.get(i).getTELEFONO(),nuevoUsuario.get(i).getEstado());// guardamos nuevo sector y tenemos nuevo id
            }

          //  aqui va la otra parte de agregar usuario reptido

            int idNuevoSector = get_idUsuario(nuevoUsuario.get(i).getNOMBRES(),nuevoUsuario.get(i).getID_SECTOR()); // obtenemos el nuevo id del sector guardado


            // ------------------ RECORER A LISTA DE CUENTA
            for(int j = 0 ; j < cuenta.size() ; j++){
                if(cuenta.get(j).getId_usuario()== nuevoUsuario.get(i).getID()){
                    cuenta.get(j).setId_usuario(idNuevoSector+100000);
                }
            }
            // --------------- RECORER SALDO CUENTA ----------
            for(int j = 0 ; j < saldoCuenta.size() ; j++){
                if(saldoCuenta.get(j).getId_usuario()== nuevoUsuario.get(i).getID()){
                    saldoCuenta.get(j).setId_usuario(idNuevoSector+100000);
                }
            }
            // -------------------- RECORRER CUENTA DETALLE --------------
            for(int j = 0 ; j < cuentaDetalle.size() ; j++){
                if(cuentaDetalle.get(j).getId_cuenta_usuario()== nuevoUsuario.get(i).getID()){
                    cuentaDetalle.get(j).setId_cuenta_usuario(idNuevoSector+100000);
                }
            }
        }
        eliminarMiles(saldoCuenta,cuenta,cuentaDetalle); // ELIMINAR LOS MILES QUE AGREGAMOS EN ID USUARIO EN CADA UNA DE LAS LISTAS

    }
    private void guardarNuevo(String nombres, String alias, String detalle, String foto, int id_sector, int id_operadorMovil, String telefono, String estado) {

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
            values.put("ESTADO", estado);
            db.insert("USUARIO", null, values);

            db.close();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();

        }


    }






    private void insert_saldoCuenta(ArrayList<Nodo_SaldoCuenta> sldoCuenta){

        BaseHelper base = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = base.getWritableDatabase();

        for(int i = 0 ; i < usuario.size() ; i++){

            int numeroProcesoTemp = 1;
            int id_usuario = get_idUsuario(usuario,i); // obtenemos el id de los usuario que estan ingresando en sincronizacion
            int maxNumeroProceoBDD = maxNumeroProcesoBDD("SALDOCUENTA",id_usuario);
            int maxNumeroProcesoTXT = maximoNumeroProcesoTXT(sldoCuenta,id_usuario);



            if(maxNumeroProceoBDD <  maxNumeroProcesoTXT){
                //  if(maxNumeroProceoBDD == 1 ){
                // numeroProcesoTemp = 2;
                for(int k = (maxNumeroProceoBDD + 1); k <= maxNumeroProcesoTXT; k++){

                    for(int j = 0 ; j< sldoCuenta.size() ; j++){

                        if(id_usuario == sldoCuenta.get(j).getId_usuario() && k == sldoCuenta.get(j).getNumero_proceso()){

                            try{
                                    ContentValues value = new ContentValues();
                                    value.put("ID_USUARIO", id_usuario);
                                    value.put("SALDO", sldoCuenta.get(j).getSaldo());
                                    value.put("FECHA", sldoCuenta.get(j).getFecha());
                                    value.put("NUMERO_PROCESO", k);
                                    value.put("TIPO_PROCESO", sldoCuenta.get(j).getTipo_proceso());
                                    value.put("SALDO_ABONO", sldoCuenta.get(j).getSaldo_abono());
                                    db.insert("SALDOCUENTA", null, value);
                            }catch (Exception e){
                                Toast.makeText(this,"ERROR CUENTA DETALLE.", Toast.LENGTH_SHORT).show();

                            }
                             break;
                        }
                     //   temporal =auxBDD("CUENTADETALLE",id_usuario);
                    }

                }
            }


            }

        db.close();
    }


    private void insert_cuentDetalle(ArrayList<Nodo_CuentaDetalle> cuentDetall){

        BaseHelper base = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = base.getWritableDatabase();

        String dato="";
        for(int i = 0 ; i < usuario.size() ; i++){

            int id_usuario = get_idUsuario(usuario,i); // obtenemos el id de los usuario que estan ingresando en sincronizacion
            int maxNumeroProceoBDD = maxNumeroProcesoBDD2("CUENTADETALLE",id_usuario);
            int maxNumeroProcesoTXT = maximoNumeroProcesoTXT2(cuentDetall,id_usuario);
            int numeroProcesoTemp = 1;
            String temporal="";
            if(maxNumeroProceoBDD <  maxNumeroProcesoTXT){
                //  if(maxNumeroProceoBDD == 1 ){
                // numeroProcesoTemp = 2;
                for(int k = (maxNumeroProceoBDD + 1); k <= maxNumeroProcesoTXT; k++){

                    for(int j = 0 ; j< cuentDetall.size() ; j++){

                        if(id_usuario == cuentDetall.get(j).getId_cuenta_usuario() && k == cuentDetall.get(j).getNumero_proceso()){
                            try{


                                ContentValues values = new ContentValues();
                                values.put("ID_CUENTA", id_usuario);
                                values.put("PROCESO", cuentDetall.get(j).getProceso());
                                values.put("CANTIDAD", cuentDetall.get(j).getCantidad());
                                values.put("VALOR", cuentDetall.get(j).getValor());
                                values.put("FECHA", cuentDetall.get(j).getFecha());
                                values.put("ESTADO", cuentDetall.get(j).getEstado());
                                values.put("DETALLE", cuentDetall.get(j).getDetalle());
                                values.put("NUMERO_PROCESO", k);
                                db.insert("CUENTADETALLE", null, values);
                            }catch (Exception e){
                                Toast.makeText(this,"ERROR CUENTA DETALLE.", Toast.LENGTH_SHORT).show();

                            }
                        }
                        //temporal =auxBDD("CUENTADETALLE",id_usuario);
                    }

                }
             //   dato=auxBDD("CUENTADETALLE",id_usuario);

            }
        }


    //    String auxSaldoCuenta = auxSaldoCuenta();

    //    String auxCuetaDetalle = auxCuentaDetalle();
    //    int a = 0;
        db.close();

    }


    private void insert_cuent(ArrayList<Nodo_Cuenta> cuent){

        BaseHelper base = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = base.getWritableDatabase();

        for(int i = 0 ; i < usuario.size() ; i++){

            int id_usuario = get_idUsuario(usuario,i); // obtenemos el id de los usuario que estan ingresando en sincronizacion

                for(int j = 0 ; j < cuent.size() ; j ++){
                    if(cuent.get(j).getId_usuario() == id_usuario){

                        ContentValues c = new ContentValues();
                        c.put("SALDO", cuent.get(j).getSaldo());
                        c.put("NUMERO_PROCESO", cuent.get(j).getNumero_proceso());
                        c.put("TIPO_PROCESO", cuent.get(j).getTipo_proceso());
                        c.put("FECHA", cuent.get(j).getFecha());

                        db.update("CUENTA", c, "ID_USUARIO = " + id_usuario, null);

                    }
                }


        }
        db.close();

    }

    private int maximoNumeroProcesoTXT(ArrayList<Nodo_SaldoCuenta> nCuenta, int id_usuario){
        int max=0;
        for(int i = 0 ; i < nCuenta.size(); i++){
            if(nCuenta.get(i).getId_usuario()== id_usuario){
                if(nCuenta.get(i).getNumero_proceso()>max){
                    max= nCuenta.get(i).getNumero_proceso();
                }
            }

        }
        return  max;
    }
    private int maximoNumeroProcesoTXT2(ArrayList<Nodo_CuentaDetalle> nCuenta, int id_usuario){
        int max=0;
        for(int i = 0 ; i < nCuenta.size(); i++){
            if(nCuenta.get(i).getId_cuenta_usuario()== id_usuario){
                if(nCuenta.get(i).getNumero_proceso()>max){
                    max= nCuenta.get(i).getNumero_proceso();
                }
            }

        }
        return  max;
    }

    private int minimoNumeroProcesoTXT2(ArrayList<Nodo_CuentaDetalle> nCuenta, int id_usuario){
        int max=1000000;
        for(int i = 0 ; i < nCuenta.size(); i++){
            if(nCuenta.get(i).getId_cuenta_usuario()== id_usuario){
                if(nCuenta.get(i).getNumero_proceso() < max){
                    max= nCuenta.get(i).getNumero_proceso();
                }
            }

        }
        return  max;
    }

    // ------------------- FIN CARGAR DATOS DE ARCHIVO TXT -----------------------------


    // -------------------------  SACAR TODOS LOS DATOS PARA SINCRONIZAR EN UN TXT ------------------------------
    private String T_usuario() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM USUARIO";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2) + "-" + c.getString(3) + "-" +
                        c.getString(4) + "-" + c.getInt(5) + "-" + c.getInt(6) + "-" + c.getString(7) + "-" + c.getString(8) + ",";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_sector() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM SECTOR";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2) + ",";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_operadorMovil() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM OPERADORMOVIL";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2) + ",";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_saldosCuenta() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM SALDOCUENTA";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + c.getInt(1) + "↕" + c.getDouble(2) + "↕" + c.getInt(3) + "↕" + c.getString(4) + "↕" + c.getDouble(5) + "↕" + c.getString(6) + ",";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_cuenta() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM CUENTA";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + c.getInt(1) + "↕" + c.getDouble(2) + "↕" + c.getInt(3) + "↕" + c.getString(4) + "↕" + c.getString(5) + ",";
            } while (c.moveToNext());
        }
        return dato;
    }

    private String T_cuentaDetalle() {
        String dato = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM CUENTADETALLE";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato = dato + c.getInt(1) + "↕" + c.getString(2) + "↕" + c.getFloat(3) + "↕" + c.getDouble(4) + "↕" + c.getString(5) + "↕" + c.getString(6)
                        + "↕" + c.getString(7) + "↕" + c.getInt(8) + ",";
            } while (c.moveToNext());
        }
        return dato;
    }
    // ----------------------- FIN SACAR TODOS LOS DATOS PARA SINCRONIZAR EN UN TXT ------------------------------

    // --------------------------- CARGAR DATOS A LISTAS -----------------------------------

    private ArrayList<Nodo_Catalogo> cargarOperador(String operador) {
        ArrayList<Nodo_Catalogo> listaOperadores = new ArrayList<>();
        String[] datos = operador.split(",");

        for (int i = 0; i < datos.length; i++) {
            String[] auxDatos = datos[i].split("-");
            listaOperadores.add(new Nodo_Catalogo(Integer.parseInt(auxDatos[0]), auxDatos[1], auxDatos[2]));
        }
        return listaOperadores;
    }

    private ArrayList<Nodo_Catalogo> cargarSector(String sector) {
        ArrayList<Nodo_Catalogo> listaSectores = new ArrayList<>();
        if(!sector.equals("")){
            String[] datos = sector.split(",");

            for (int i = 0; i < datos.length; i++) {
                String[] auxDatos = datos[i].split("-");
                listaSectores.add(new Nodo_Catalogo(Integer.parseInt(auxDatos[0]), auxDatos[1], auxDatos[2]));
            }
        }
        return listaSectores;
    }

    private ArrayList<Nodo_Usuario> cargarUsuario(String sector) {
        ArrayList<Nodo_Usuario> lista = new ArrayList<>();
        if(!sector.equals("")){
            String[] datos = sector.split(",");

            for (int i = 0; i < datos.length; i++) {
                String[] auxDatos = datos[i].split("-");
                lista.add(new Nodo_Usuario(Integer.parseInt(auxDatos[0]), auxDatos[1], auxDatos[2], auxDatos[3],
                        auxDatos[4], Integer.parseInt(auxDatos[5]), Integer.parseInt(auxDatos[6]), auxDatos[7], auxDatos[8]));

            }
        }
        return lista;
    }

    private ArrayList<Nodo_SaldoCuenta> cargarSaldoCuenta(String sector) {
        ArrayList<Nodo_SaldoCuenta> lista = new ArrayList<>();
        if(!sector.equals("")){
            String[] datos = sector.split(",");

            for (int i = 0; i < datos.length; i++) {
                String[] auxDatos = datos[i].split("↕");
                lista.add(new Nodo_SaldoCuenta(Integer.parseInt(auxDatos[0]), Double.parseDouble(auxDatos[1])
                        , Integer.parseInt(auxDatos[2]), auxDatos[3], Double.parseDouble(auxDatos[4]), auxDatos[5]));

            }
        }
        return lista;
    }

    private ArrayList<Nodo_Cuenta> cargarCuenta(String sector) {
        ArrayList<Nodo_Cuenta> lista = new ArrayList<>();
       if(!sector.equals("")){
           String[] datos = sector.split(",");

           for (int i = 0; i < datos.length; i++) {
               String[] auxDatos = datos[i].split("↕");
               lista.add(new Nodo_Cuenta(Integer.parseInt(auxDatos[0]), Double.parseDouble(auxDatos[1]), Integer.parseInt(auxDatos[2]), auxDatos[3], auxDatos[4]));

           }
       }
        return lista;
    }

    private ArrayList<Nodo_CuentaDetalle> cargarDetalleCuenta(String sector) {
        ArrayList<Nodo_CuentaDetalle> lista = new ArrayList<>();
        if(!sector.equals("")){
            String[] datos = sector.split(",");

            for (int i = 0; i < datos.length - 1; i++) {
                String[] auxDatos = datos[i].split("↕");
                lista.add(new Nodo_CuentaDetalle(Integer.parseInt(auxDatos[0]), auxDatos[1],
                        Float.parseFloat(auxDatos[2]), Double.parseDouble(auxDatos[3]), auxDatos[4], auxDatos[5], auxDatos[6], Integer.parseInt(auxDatos[7])));

            }
        }
        return lista;
    }
    // --------------------------- FIN CARGAR DATOS A LISTAS -----------------------------------


    private void eliminarMiles(ArrayList<Nodo_Usuario> usuarioAux ){
        for(int i = 0 ; i < usuarioAux.size() ; i ++){
            if(usuarioAux.get(i).getID_SECTOR() > 100000){
                usuarioAux.get(i).setID_SECTOR(usuarioAux.get(i).getID_SECTOR()-100000);
            }
            if(usuarioAux.get(i).getID_OPERADORMOVIL() > 100000){
                usuarioAux.get(i).setID_OPERADORMOVIL(usuarioAux.get(i).getID_OPERADORMOVIL()-100000);
            }
        }

    }
    private void eliminarMiles( ArrayList<Nodo_SaldoCuenta> saldoCuentaAux, ArrayList<Nodo_Cuenta> cuentaAux, ArrayList<Nodo_CuentaDetalle> cdAux){

        BaseHelper base =  new BaseHelper(this,"abonar",null,1);
        SQLiteDatabase bd =  base.getWritableDatabase();

        for(int i = 0 ; i < saldoCuentaAux.size() ; i++){
            if(saldoCuentaAux.get(i).getId_usuario() > 100000){
                saldoCuentaAux.get(i).setId_usuario(saldoCuentaAux.get(i).getId_usuario()-100000);


            }
        }
        for(int i = 0 ; i < cuentaAux.size() ; i++){
            if(cuentaAux.get(i).getId_usuario() > 100000){
                cuentaAux.get(i).setId_usuario(cuentaAux.get(i).getId_usuario()-100000);
               }
            int existe= saldoCuenta_existe(cuentaAux.get(i).getId_usuario());

            if(existe != 0){
                //SI ES MENOR SE HACE UN UPDATE SI ES MAYOR NO SE HAACE NADA
                if(existe < cuentaAux.get(i).getNumero_proceso()){
                    //reaizar update
                    ContentValues value = new ContentValues();
                     value.put("SALDO", cuentaAux.get(i).getSaldo());
                    value.put("NUMERO_PROCESO", cuentaAux.get(i).getNumero_proceso());
                    value.put("TIPO_PROCESO",cuentaAux.get(i).getTipo_proceso());
                    value.put("FECHA", cuentaAux.get(i).getFecha());
                    bd.update("CUENTA", value, "ID_USUARIO = " + cuentaAux.get(i).getId_usuario(), null);
                }

            }else{
                // no existe asi que se ingresa a saldo cuenta
                ContentValues value = new ContentValues();
                value.put("SALDO", cuentaAux.get(i).getSaldo());
                value.put("NUMERO_PROCESO", cuentaAux.get(i).getNumero_proceso());
                value.put("TIPO_PROCESO",cuentaAux.get(i).getTipo_proceso());
                value.put("FECHA", cuentaAux.get(i).getFecha());
                value.put("ID_USUARIO",cuentaAux.get(i).getId_usuario());
                bd.insert("CUENTA",null,value);
            }

        }
        for(int i = 0 ; i < cdAux.size() ; i++){
            if(cdAux.get(i).getId_cuenta_usuario() > 100000){
                cdAux.get(i).setId_cuenta_usuario(cdAux.get(i).getId_cuenta_usuario()-100000);
            }
        }

        bd.close();

    }
    private int saldoCuenta_existe(int id_usuario){
        int dato=0;
        BaseHelper base =  new BaseHelper(this,"abonar",null,1);
        SQLiteDatabase bd =  base.getReadableDatabase();
        String sql = "SELECT NUMERO_PROCESO FROM CUENTA WHERE ID_USUARIO = "+id_usuario;

        Cursor c = bd.rawQuery(sql,null);
        if(c.moveToFirst()){
            do{
                dato= c.getInt(0);
            }while (c.moveToNext());
        }
        return dato;

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


    /*
    private String auxCuenta() {
        String datos="";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM CUENTA  "  ;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos = String.valueOf(c.getInt(0)) + " " + String.valueOf(c.getInt(1)) + " " + String.valueOf(c.getInt(2)) + " " + String.valueOf(c.getInt(3)) + " " + c.getString(4)+"\n";
            } while (c.moveToNext());
        }
        return datos;
    }

    private String auxUsuario() {
        String datos="";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM USUARIO  "  ;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos = String.valueOf(c.getInt(0)) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3) + " " + c.getString(4)+" "+ c.getInt(5)+" "+c.getInt(6)+" "+ c.getString(7)+" "+c.getString(8)+"\n";
            } while (c.moveToNext());
        }
        return datos;
    }

    private String auxSaldoCuenta() {
        String datos="";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM SALDOCUENTA  "  ;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos = String.valueOf(c.getInt(0)) + " " + c.getInt(1) + " " + c.getDouble(2) + " " + c.getInt(3) + " " + c.getString(4)+" "+ c.getDouble(5)+"\n";
            } while (c.moveToNext());
        }
        return datos;
    }
    private String auxCuentaDetalle() {
        String datos="";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM CUENTADETALLE  "  ;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {

                datos = c.getInt(0) + " " + c.getInt(1) + " " + c.getString(2) + " " + c.getFloat(3) + " " + c.getDouble(4)+" "+ c.getString(7)+" "+c.getInt(8)+"\n";
            } while (c.moveToNext());
        }
        return datos;
    }
    */

}
