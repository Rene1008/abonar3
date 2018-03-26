package com.reneascanta.abonar;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import harmony.java.awt.Color;

//public class Crear_PDF extends AppCompatActivity {
public class Crear_PDF extends Activity {

    ListView lv_listaClientesPDF;
    ArrayList<NodoClientes> lista;
    ArrayList<String> lista2;
    ArrayList<Integer> lista_ids_pdf;

    TextView prueba;

     List<UserModel> users;


    // ------------- variables para crear pdf --------------



    Procesos p =  new Procesos();
    InvoiceObject invoiceObject = new InvoiceObject();

    //InvoiceObject invoiceObject = new InvoiceObject();
    private String INVOICES_FOLDER = "Abonar";
    private String FILENAME = "Abonar-Reporte.pdf";
    //Declaramos la clase PdfManager
    private PdfManager pdfManager = null;
   // private ArrayList<NodoSaldo> saldoCuenta;
    //--------------- fin varables pdf --------------------


// ----------------- crear pdf --------------------
    private static final String nombre_carpeta = "com.abonarApp.rg";
    private static final String generados = "MisArchivos";
    List<Dato_Personal_Cliente> datosUsuario;
    List<Nodo_SaldoCuenta> saldoCuenta;
    List<Nodo_CuentaDetalle> cuentaDetails;

    Button b_generarPDF;

    @Override
    protected void onPostResume() {
        super.onPostResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear__pdf);
        lista_ids_pdf = new ArrayList<>();
        lv_listaClientesPDF = (ListView) findViewById(R.id.lv_listaClientesPDF);
        prueba = (TextView) findViewById(R.id.prueba);


        b_generarPDF = (Button) findViewById(R.id.b_generarPDF);

       // ------------------- crear los check box en la lista -*----------------------------------


        users = new ArrayList<>();
        lista = lista();
        users.add(new UserModel(false , ""));
        for(int i = 0 ; i < lista.size() ; i++){
            users.add(new UserModel(false , lista.get(i).getID()+" "+ lista.get(i).getNOMBRES()));

        }

        final CustomAdapter adapter =  new CustomAdapter(this, users);
        lv_listaClientesPDF.setAdapter(adapter);

        lv_listaClientesPDF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel model = users.get(position);
                String []dato = model.getUserName().toString().split(" ");

                   if(model.isSelected){
                       model.setSelected(false); // no selecciona
                       if(position != 0){
                           // quita la seleccion uno por uno
                           for (int i = 0; i < lista_ids_pdf.size(); i++) {   // buscamos el id que vamos a eliminar  para generar reportes
                                if (lista_ids_pdf.get(i) == Integer.parseInt(dato[0])) {
                                   lista_ids_pdf.remove(i);
                               }
                           }

                       }else{
                           // quita seleccion todo
                            for(int i = 1 ; i < users.size() ; i++){
                                model = users.get(i);
                                model.setSelected(false);

                            }

                           for (int j = 0; j < lista_ids_pdf.size(); j++) {   // buscamos el id que vamos a eliminar  para generar reportes
                               lista_ids_pdf.clear();
                           }
                           b_generarPDF.setVisibility(View.GONE);

                       }

                   }else{

                       b_generarPDF.setVisibility(View.VISIBLE);
                       model.setSelected(true); // selecciona

                       if(position != 0){
                           // seleciona unpo por uno
                              lista_ids_pdf.add(Integer.parseInt(dato[0]));


                       }else{
                           // selecciona todo
                           lista_ids_pdf.clear();
                           for(int i = 1 ; i < users.size() ; i++){

                               model = users.get(i);
                               model.setSelected(true); // selecciona

                               String []d =  users.get(i).getUserName().toString().split(" ");
                               lista_ids_pdf.add(Integer.parseInt(d[0]));
                              }

                          }
                   }
                   if(position != 0){
                       users.set(position,model);

                   }
                   // ahora actualizar
                   adapter.updateRecords(users);


                /*
                    String a="";
                   for(int i = 0 ; i< lista_ids_pdf.size();i++){
                       if(lista_ids_pdf.isEmpty()){
                           break;
                       }else{

                           a =a+ lista_ids_pdf.get(i).toString();
                       }
                   }
                   prueba.setText(a);
                */
               }

        });

        // ------------------- fin crear check box en lista

        //---------------- crear pdf -------------------

        /*
        b_generarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create PDF document
                assert pdfManager != null;
                pdfManager.createPdfDocument(invoiceObject);

            }
        });
*/

        b_generarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    generarPdfOnClick(v);

                pdfManager =  new PdfManager();
                        assert pdfManager != null;


               if(!lista_ids_pdf.isEmpty()){
                   datosUsuario =  new ArrayList<Dato_Personal_Cliente>();
                   saldoCuenta =  new ArrayList<Nodo_SaldoCuenta>();
                   cuentaDetails =  new ArrayList<Nodo_CuentaDetalle>();

                   for(int i = 0 ; i < lista_ids_pdf.size(); i++){
                       int id= Integer.parseInt(lista_ids_pdf.get(i).toString());

                       datosUsuario.add(datoPersonalCliente(id)); ;

                   }
                   for(int i = 0 ; i < lista_ids_pdf.size(); i++){

                       int id= Integer.parseInt(lista_ids_pdf.get(i).toString());

                        saldoCuenta(id) ;
                        cuentaDetale(id);
                    }


                   // generarPdfOnClick(v,datosUsuario,saldoCuenta,cuentaDetails);

                   generarPDF2();


               }else{
                   Toast.makeText(Crear_PDF.this,"Seleccione un cliente.", Toast.LENGTH_SHORT).show();
               }


            }
        });


        // --------------- fin crear pdf ---------------------------


    }

    //Código generado por Android Studio
    /**
     * A placeholder fragment containing a simple view.
     */
    /*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    */



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


    // ----------------------------- CREAR PDF ------------------


    /*
    public void generarPdfOnClick(View v, List<Dato_Personal_Cliente> dpc, List<Nodo_SaldoCuenta> sc, List<Nodo_CuentaDetalle> cd) throws DocumentException, IOException {

        Document document = new Document(PageSize.LETTER);
        String nombre_archivo = "reporte-abonar-PDF.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();

        File pdfDir = new File(tarjetaSD+ File.separator+nombre_carpeta);
        if(!pdfDir.exists()){
            pdfDir.mkdir(); // creamos la carpeta done guardar los datos
        }

        File pdfSubDir = new File(pdfDir.getPath()+File.separator+generados);
        if(!pdfSubDir.exists()){
            pdfSubDir.mkdir();
        }

        String nombre_completo_ruta = Environment.getExternalStorageDirectory()+File.separator+nombre_carpeta+File.separator+generados+
                File.separator+nombre_archivo;


        File outputFile =  new File(nombre_completo_ruta);
        if(outputFile.exists()){
            outputFile.delete();
        }


             PdfWriter.getInstance(document, new FileOutputStream(nombre_completo_ruta));

        Font saldo_texto= new Font();
        saldo_texto.setColor(242,4,0);
        saldo_texto.setStyle(Font.BOLD );

        Font abono_texto= new Font();
        abono_texto.setColor(252,164,96);
        abono_texto.setStyle(Font.BOLD );

        Font suma_texto= new Font();
        suma_texto.setColor(78,252,114);
        suma_texto.setStyle(Font.BOLD );

        Font devol_texto= new Font();
        devol_texto.setColor(15,17,208);
        devol_texto.setStyle(Font.BOLD );

        Font titulo_texto= new Font();
        titulo_texto.setColor(242,4,0);
        titulo_texto.setStyle(Font.BOLD );

        Font te_texto= new Font();
        te_texto.setColor(0,0,0);







            document.open();
            document.addAuthor("Autor: Rene Ascanta");
            document.addCreator("Creador: RG");
            document.addSubject("cotendido de subject :)");
            document.addCreationDate();
            document.addTitle("TITULO DE PRUEBA DE MI PRIMER PDF");


        document.add(new Paragraph("                                                         REPORTE DE DATOS",titulo_texto));

        document.add(new Paragraph("Fecha:   "+ new Date()));
        document.add(new Paragraph("Ascanta.com"));






        for(int i = 0 ; i <  dpc.size() ; i++){
                if(i != 0 ){
                    document.newPage();
                }
                document.add(new Paragraph(" "));
                document.add(new Paragraph("_____________________________________________________________________________",saldo_texto));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Còdigo:   00000"+ dpc.get(i).getID()));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Informaciòn Cliente"));
                document.add(new Paragraph("Nombre:   "+ dpc.get(i).getNOMBRES()));
                document.add(new Paragraph(dpc.get(i).getID_OPERADORMOVIL()+"  "+ dpc.get(i).getTELEFONO()));
                document.add(new Paragraph("De:   " +dpc.get(i).getID_SECTOR()));
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));


                for (int j = 0 ; j < sc.size() ; j++){
                    if(sc.get(j).getId_usuario() == dpc.get(i).getID()){
                        double total=0.0;

                        if(sc.get(j).getTipo_proceso().equals("MAS")){
                            document.add(new Paragraph(sc.get(j).getFecha() +"   SE AGREGO", te_texto));
                            total=0.0;
                            for (int k = 0 ; k < cd.size(); k++){
                                 if(cd.get(k).getNumero_proceso() == sc.get(j).getNumero_proceso() && cd.get(k).getId_cuenta_usuario() == dpc.get(i).getID()){
                                     double temp = cd.get(k).getCantidad()*cd.get(k).getValor();

                                     document.add(new Paragraph("                                                                "
                                             +cd.get(k).getCantidad()+"  "+cd.get(k).getValor()+"  "+
                                     cd.get(k).getDetalle()+"   "+ temp, te_texto));

                                     total = total + temp;

                                 }
                             }
                            document.add(new Paragraph("                                                           -------------------------------------- " ));
                            document.add(new Paragraph("                                                                            SUMA:  " + total, suma_texto));
                            document.add(new Paragraph(" " ));

                           // Font font = FontFactory.getFont(FontFactory.HELVETICA, Font.BOLD, Color.RED);
                            document.add(new Paragraph("                                                                            SALDO:  " + sc.get(j).getSaldo(),saldo_texto));

                        }


                        if(sc.get(j).getTipo_proceso().equals("MENOS")){
                            document.add(new Paragraph(sc.get(j).getFecha() +"      ABONO", te_texto));
                            document.add(new Paragraph("                                                                ABONO:  " +sc.get(j).getSaldo_abono(),abono_texto));
                            document.add(new Paragraph("                                                           -------------------------------------- " ));
                            document.add(new Paragraph(" " ));
                            double temp = sc.get(j).getSaldo()- sc.get(j).getSaldo_abono();
                            document.add(new Paragraph("                                                                            SALDO:  " + temp,saldo_texto));
                        }

                        if(sc.get(j).getTipo_proceso().equals("DEVOLUCION")){
                            document.add(new Paragraph(sc.get(j).getFecha() +"   DEVOLUCION", te_texto));
                            total=0.0;
                            for (int k = 0 ; k < cd.size(); k++){
                                if(cd.get(k).getNumero_proceso() == sc.get(j).getNumero_proceso() && cd.get(k).getId_cuenta_usuario() == dpc.get(i).getID()){
                                    double temp = cd.get(k).getCantidad()*cd.get(k).getValor();

                                    document.add(new Paragraph("                                                                "
                                            +cd.get(k).getCantidad()+"  "+cd.get(k).getValor()+"  "+
                                            cd.get(k).getDetalle()+"   "+ temp, te_texto));

                                    total = total + temp;

                                }
                            }
                            document.add(new Paragraph("                                                           -------------------------------------- " ));
                            document.add(new Paragraph("                                                                            SUMA:  " + total,suma_texto));
                            document.add(new Paragraph(" " ));
                            document.add(new Paragraph("                                                                            SALDO:  " + sc.get(j).getSaldo(),saldo_texto));
                        }

                    }
                }


            }



            document.close();
            Toast.makeText(this,"Documento PDF generado.",Toast.LENGTH_SHORT).show();
            muestraPdf(nombre_completo_ruta,this);




    }
*/






    public void muestraPdf(String archivo, Context context){

        Toast.makeText(this,"Muestra el archivo",Toast.LENGTH_SHORT).show();
        File file =  new File(archivo);
        Intent intent =  new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{

            context.startActivity(intent);

        }catch (ActivityNotFoundException e){
            Toast.makeText(this,"No tiene una aplicacion para abrir este archivo.",Toast.LENGTH_SHORT).show();

        }

    }
    // -------------------------- FIN CREAR PDF --------------------

    private void saldoCuenta(int id_usuario){
       // List<Nodo_SaldoCuenta> dato =  new ArrayList<>();
        BaseHelper usuario =  new BaseHelper(this,"abonar", null,1);
        SQLiteDatabase db =  usuario.getReadableDatabase();

        String sql = "SELECT * FROM SALDOCUENTA WHERE ID_USUARIO = "+id_usuario;
        Cursor c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            do{
              saldoCuenta.add(new Nodo_SaldoCuenta(c.getInt(1), c.getDouble(2),c.getInt(3),c.getString(4),c.getDouble(5),c.getString(6)));
            }while(c.moveToNext());
        }
      //  return  dato;
    }

    private void cuentaDetale(int id_usuario){
     //   List<Nodo_CuentaDetalle> dato =  new ArrayList<>();
        BaseHelper usuario =  new BaseHelper(this,"abonar", null,1);
        SQLiteDatabase db =  usuario.getReadableDatabase();

        String sql = "SELECT * FROM CUENTADETALLE WHERE ID_CUENTA= "+id_usuario;
        Cursor c = db.rawQuery(sql,null);
        if(c.moveToFirst()){
            do{

             cuentaDetails.add(new Nodo_CuentaDetalle(c.getInt(1),c.getString(2),c.getFloat(3),c.getDouble(4),
                        c.getString(5),c.getString(6),c.getString(7),c.getInt(8)));
            }while(c.moveToNext());
        }
      //  return  dato;
    }
    private Dato_Personal_Cliente datoPersonalCliente(int id_usuario){
        Dato_Personal_Cliente dato =  new Dato_Personal_Cliente();
        BaseHelper usuario =  new BaseHelper(this,"abonar", null,1);
        SQLiteDatabase db =  usuario.getReadableDatabase();

        String sql = "SELECT A.ID, A.NOMBRES, A.ALIAS, A.DETALLE, A.FOTO, A.ID_SECTOR, A.ID_OPERADORMOVIL, A.TELEFONO, A.ESTADO," +
                " B.SALDO, B.FECHA , OP.DETALLE, S.DETALLE FROM USUARIO A " +
                " LEFT JOIN CUENTA B ON A.ID = B.ID_USUARIO" +
                " LEFT JOIN SECTOR S ON A.ID_SECTOR = S.ID" +
                " LEFT JOIN OPERADORMOVIL OP ON A.ID_OPERADORMOVIL = OP.ID" +
                " WHERE A.ESTADO = 'ACTIVO' AND A.ID = " + id_usuario;

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {

                dato.setID(id_usuario);
                dato.setNOMBRES(c.getString(1));
                dato.setALIAS(c.getString(2));
                dato.setDETALLE(c.getString(3));
                dato.setFOTO(c.getString(4));
                dato.setID_SECTOR(c.getString(12));
                dato.setID_OPERADORMOVIL(c.getString(11));
                dato.setTELEFONO(c.getString(7));
                dato.setEstado(c.getString(8));
                /*
                datos = c.getString(1) + "," + c.getString(2) + "," + c.getString(3)
                        + "," + c.getString(4) + "," + c.getString(12) + "," + c.getString(11)
                        + "," + c.getString(7) + "," + c.getString(8) + "," + c.getFloat(9) + "," + c.getString(10);
*/
            } while (c.moveToNext());
        }
        return dato;



    }


    private final static String NOMBRE_DIRETORIO = "MiPdf";
    private final static String NOMBRE_DOCUMENTO= "PRUEBA_RENE_PDF.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";


    //----------------------------- crear docmento pdf -----------
    public File getRuta(){
        File ruta =  null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            ruta= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),NOMBRE_DIRETORIO);
            if(ruta != null){
                if(!ruta.mkdir()){
                    if(!ruta.exists()){
                        return null;
                    }
                }
            }
        }else{
        }
        return ruta;
    }
    public File crearFichero(String nombreFichero){
        File ruta = getRuta();
        File fichero =  null;
        if(ruta != null){
            fichero =  new File(ruta, nombreFichero);
        }
        return fichero;
    }

    public void generarPDF2(){

         Document documento = new Document();
         try{

             File f = crearFichero(NOMBRE_DOCUMENTO);
             FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

             PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);
             HeaderFooter cabecera = new HeaderFooter(new Phrase("Ascanta.com"),false);
             HeaderFooter pie = new HeaderFooter(new Phrase("Este es mi pie de pagin"),false);
             documento.setHeader(cabecera);
             documento.setFooter(pie);

             documento.open();
             documento.add(new Paragraph("Titulo 1"));
             Font font = FontFactory.getFont(FontFactory.HELVETICA, 28, Font.BOLD, Color.RED);
             documento.add(new Paragraph("Titulo Personalizado", font));

             Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.rglogos);
             ByteArrayOutputStream stream = new ByteArrayOutputStream();
             bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
             Image image = Image.getInstance(stream.toByteArray());
             documento.add(image);




             font = FontFactory.getFont(FontFactory.HELVETICA,150,Font.BOLD,Color.lightGray);
             ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Paragraph("R.G.",font),297.5f,421,
                     writer.getPageNumber()%2==1 ? 45 : -45);


         }catch (DocumentException e){
             Log.e(ETIQUETA_ERROR, e.getMessage());
         }catch (IOException e){
             Log.e(ETIQUETA_ERROR, e.getMessage());

         }finally {
             documento.close();
             String nombre_completo_ruta = Environment.getExternalStorageDirectory()+File.separator+
                     Environment.DIRECTORY_DOWNLOADS+File.separator+NOMBRE_DIRETORIO+File.separator+NOMBRE_DOCUMENTO;

           //  Toast.makeText(this,nombre_completo_ruta,Toast.LENGTH_LONG).show();
             muestraPdf(nombre_completo_ruta,this);

         }

     }




}
