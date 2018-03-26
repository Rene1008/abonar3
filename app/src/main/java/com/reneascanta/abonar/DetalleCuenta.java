package com.reneascanta.abonar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetalleCuenta extends AppCompatActivity {

    private TextView tv_detalleCuenta;
    private ArrayList<NodoSaldo> saldoCuenta;
    private int ID_EXTRA;
    Procesos procesos = new Procesos();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuenta);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA = b.getInt("ID");

        }

        tv_detalleCuenta = (TextView) findViewById(R.id.tv_detallecuenta);

        cargarDatos();
        // String prueba = prueba();
    }


    private void cargarDatos() {
        tv_detalleCuenta.setText(cuentaDetalladoMasMenos(saldoCuenta(ID_EXTRA)));
    }

    private ArrayList<NodoSaldo> saldoCuenta(int id_usuario) {
        saldoCuenta = new ArrayList<>();
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT  *  FROM SALDOCUENTA WHERE ID_USUARIO = " + id_usuario;

        Cursor c = db.rawQuery(sql, null);
        NodoSaldo nodo;
        if (c.moveToFirst()) {
            do {
                nodo = new NodoSaldo(c.getInt(0), c.getInt(1), c.getFloat(2), c.getInt(3), c.getString(4), c.getFloat(5), c.getString(6));
                saldoCuenta.add(nodo);
            } while (c.moveToNext());
        }


        return saldoCuenta;
    }

    private int idCuenta(int id_usuario) {
        int dato = 0;
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = " SELECT ID FROM CUENTA WHERE ID_USUARIO =" + id_usuario;
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                dato = c.getInt(0);
            } while (c.moveToNext());
        }
        return dato;
    }

    private String cuentaDetalladoMasMenos(ArrayList<NodoSaldo> detallado) {
        String datos = "";
        // int id_cuenta = idCuenta(ID_EXTRA);
        for (int i = 0; i < detallado.size(); i++) {

            //       BaseHelper usuario =  new BaseHelper(this,"abonar", null, 1);
            //  SQLiteDatabase db= usuario.getReadableDatabase();
            BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
            SQLiteDatabase db = usuario.getReadableDatabase();
            if (detallado.get(i).getTipo_proceso().equals("MAS")) {
                String sql = "SELECT * FROM CUENTADETALLE CD" +
                        " WHERE CD.NUMERO_PROCESO = " + ((int) detallado.get(i).getNumero_proceso()) + " AND CD.ID_CUENTA = " + ID_EXTRA;

                //  Cursor c =  db.rawQuery(sql,null);
                Cursor c = db.rawQuery(sql, null);
                double total_aux = 0;
                datos = datos + "FECHA: " + detallado.get(i).getFecha() + "\n";
                if (c.moveToFirst()) {
                    do {
                        total_aux = total_aux + (c.getInt(3) * c.getFloat(4));
                        datos = datos + "                           ";
                        datos = datos + "" + c.getInt(3) + "     " + procesos.valor(String.valueOf(c.getFloat(4))) + "     " + c.getString(7) + "     " + procesos.valor(String.valueOf((c.getInt(3) * c.getFloat(4)))) + "\n";
                    } while (c.moveToNext());
                }

                datos = datos + "                             --------------------------- \n";
                datos = datos + "                                                        SUMA: " + procesos.valor(String.valueOf(total_aux)) + "\n";

                datos = datos + " ---------------------------------------- \n";
                datos = datos + "                                                       SALDO: " + procesos.valor(String.valueOf(detallado.get(i).getSaldo())) + "\n\n";

            } else if (detallado.get(i).getTipo_proceso().equals("MENOS")) {

                datos = datos + "FECHA: " + detallado.get(i).getFecha() + "      ABONO " + procesos.valor(String.valueOf(detallado.get(i).getSaldo_abono())) + "\n";
                datos = datos + " ---------------------------------------- \n";
                datos = datos + "FECHA: " + detallado.get(i).getFecha() + "      SALDO: " + procesos.valor(String.valueOf((detallado.get(i).getSaldo() - detallado.get(i).getSaldo_abono()))) + "\n\n";


            }
            if (detallado.get(i).getTipo_proceso().equals("DEVOLUCION")) {
                String sql = "SELECT * FROM CUENTADETALLE CD" +
                        " WHERE CD.NUMERO_PROCESO = " + ((int) detallado.get(i).getNumero_proceso()) + " AND CD.ID_CUENTA = " + ID_EXTRA;

                //  Cursor c =  db.rawQuery(sql,null);
                Cursor c = db.rawQuery(sql, null);
                double total_aux = 0;
                datos = datos + "FECHA: " + detallado.get(i).getFecha() + "   DEVOLUCIÒN\n";
                if (c.moveToFirst()) {
                    do {
                        total_aux = total_aux + (c.getInt(3) * c.getFloat(4));
                        datos = datos + "                           ";
                        datos = datos + "" + c.getInt(3) + "     " + procesos.valor(String.valueOf(c.getFloat(4))) + "     " + c.getString(7) + "     " + procesos.valor(String.valueOf((c.getInt(3) * c.getFloat(4)))) + "\n";
                    } while (c.moveToNext());
                }

                datos = datos + "                           --------------------------- \n";
                datos = datos + "                                                        SUMA: " + procesos.valor(String.valueOf(total_aux)) + "\n";

                datos = datos + " ---------------------------------------- \n";
                datos = datos + "                                                       SALDO: " + procesos.valor(String.valueOf(detallado.get(i).getSaldo())) + "\n\n";

            }
            db.close();

        }


        return datos;
    }


    private String prueba() {

        String datos = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();
        String sql = "SELECT * FROM  CUENTADETALLE ";

        try {
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
                do {
                    datos = datos + " " + c.getInt(0) + "    " + c.getInt(8) + "\n";
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(DetalleCuenta.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return datos;
    }
}
