package com.reneascanta.abonar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AbonarSaldo extends AppCompatActivity {

    private TextView tv_nombresClientesAbonar, tv_SaldoClienteAbonar;
    private int ID_EXTRA;
    private String NOMBRES_EXTRA;
    private Button b_abonarSaldo;
    private EditText et_valorAbonar;
    private String datosCuentaDelUsuario[];
    Procesos procesos = new Procesos();

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //  cargarDatosSectoresMercancia();

        datosCuentaDelUsuario = datosCuentaUsuario(ID_EXTRA).split(",");
        tv_SaldoClienteAbonar.setText(" SALDO : " + procesos.valor(String.valueOf(datosCuentaDelUsuario[2])));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonar_saldo);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA = Integer.parseInt(b.getString("ID"));
            NOMBRES_EXTRA = b.getString("NOMBRES");

        }

        tv_nombresClientesAbonar = (TextView) findViewById(R.id.tv_nombresClientesAbonar);
        tv_SaldoClienteAbonar = (TextView) findViewById(R.id.tv_SaldoClienteAbonar);
        b_abonarSaldo = (Button) findViewById(R.id.b_abonarSaldo);
        et_valorAbonar = (EditText) findViewById(R.id.et_valorAbonar);


        // b_editarMercancia.setVisibility(View.INVISIBLE);
        tv_nombresClientesAbonar.setText("CLIENTE: " + NOMBRES_EXTRA);

        // ------------- inicio del nombre de la cuenta y el saldo actual ------------
        datosCuentaDelUsuario = datosCuentaUsuario(ID_EXTRA).split(",");
        // valAntiguo= Float.parseFloat(String.valueOf(datosCuentaDelUsuario[2]));
        tv_SaldoClienteAbonar.setText(" SALDO : " + procesos.valor(String.valueOf(datosCuentaDelUsuario[2])));

        b_abonarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_valorAbonar.getText().toString().equals("")) {
                    Toast.makeText(AbonarSaldo.this, "INGRESE UN VALOR A ABONAR.", Toast.LENGTH_SHORT).show();
                } else {

                    if (Double.parseDouble(et_valorAbonar.getText().toString()) <= Double.parseDouble(datosCuentaDelUsuario[2].toString())) {

                        guardarSaldoCuentaAbonado(datosCuentaDelUsuario, Double.parseDouble(et_valorAbonar.getText().toString()));
                        double nuevoSaldo = Double.parseDouble(datosCuentaDelUsuario[2]) - Double.parseDouble(et_valorAbonar.getText().toString());
                        editarCuentaUsuario(ID_EXTRA, nuevoSaldo, Integer.parseInt(datosCuentaDelUsuario[3]));

                        Toast.makeText(AbonarSaldo.this, "SE GUARDO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();

                        datosCuentaDelUsuario = datosCuentaUsuario(ID_EXTRA).split(",");
                        tv_SaldoClienteAbonar.setText(" SALDO : " + procesos.valor(datosCuentaDelUsuario[2]));

                        et_valorAbonar.setText("");

                    } else {
                        Toast.makeText(AbonarSaldo.this, "EL VALOR ABONAR NO PUEDE SER MAYOR AL SALDO.", Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });


    }

    private void editarCuentaUsuario(int id_usuario, double saldoNuevo, int numero_proceso) {

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        Date d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = fecc.format(d);

        int n = numero_proceso + 1;
        ContentValues c = new ContentValues();
        c.put("SALDO", saldoNuevo);
        c.put("TIPO_PROCESO", "MENOS");
        c.put("NUMERO_PROCESO", n);
        c.put("FECHA", fecha);
        //  db.insert("CUENTA",null, c);

        db.update("CUENTA", c, "ID_USUARIO = " + id_usuario, null);
        db.close();
    }

    private void guardarSaldoCuentaAbonado(String datosCuentaDelUsuario[], double valorAbono) {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        int n = Integer.parseInt(datosCuentaDelUsuario[3]) + 1;
        Date d = new Date();
        SimpleDateFormat fecc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = fecc.format(d);
        ContentValues value = new ContentValues();
        value.put("ID_USUARIO", datosCuentaDelUsuario[1]);
        value.put("SALDO", datosCuentaDelUsuario[2]);
        value.put("NUMERO_PROCESO", n);
        value.put("TIPO_PROCESO", "MENOS");
        value.put("SALDO_ABONO", valorAbono);
        value.put("FECHA", fecha);

        db.insert("SALDOCUENTA", null, value);
        db.close();


    }

    private String datosCuentaUsuario(int id_parametro) {
        String datos = "";

        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();


        String sql = "SELECT * FROM CUENTA  WHERE  ID_USUARIO = " + id_parametro;

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                datos = c.getInt(0) + "," + c.getInt(1) + "," + c.getFloat(2) + "," + c.getInt(3) + "," + c.getString(4) + "," + c.getString(5);
            } while (c.moveToNext());
        }
        return datos;

    }
}
