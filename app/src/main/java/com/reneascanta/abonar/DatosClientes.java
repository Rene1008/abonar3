package com.reneascanta.abonar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DatosClientes extends AppCompatActivity {

    private int ID_EXTRA;
    private TextView tv_aliasUsusario, tv_nombresUsusario, tv_detalleUsusario, tv_idSectorUsusario, tv_idOperadorMovilUsusario, tv_telefonoUsusario, tv_saldoUsusario, tv_estadoUsusario, tv_fechaUsusario;
    private String AllDatos;
    private Button b_datosDetalladoCuentaDetalle;
    Procesos procesos = new Procesos();


    @Override
    protected void onPostResume() {
        super.onPostResume();
        cargarDatos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_clientes);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID_EXTRA = b.getInt("ID");

        }

        //  tv_datosClientes = (TextView) findViewById(R.id.tv_datosCliente);
        tv_aliasUsusario = (TextView) findViewById(R.id.tv_aliasUsusario);
        tv_nombresUsusario = (TextView) findViewById(R.id.tv_nombresUsusario);
        tv_detalleUsusario = (TextView) findViewById(R.id.tv_detalleUsusario);
        tv_idSectorUsusario = (TextView) findViewById(R.id.tv_idSectorUsusario);
        tv_idOperadorMovilUsusario = (TextView) findViewById(R.id.tv_idOperadorMovilUsusario);
        tv_telefonoUsusario = (TextView) findViewById(R.id.tv_telefonoUsusario);
        tv_saldoUsusario = (TextView) findViewById(R.id.tv_saldoUsusario);
        tv_estadoUsusario = (TextView) findViewById(R.id.tv_estadoUsusario);
        tv_fechaUsusario = (TextView) findViewById(R.id.tv_fechaUsusario);
        b_datosDetalladoCuentaDetalle = (Button) findViewById(R.id.b_datosDetalladoCuentaDetalle);

        cargarDatos();
        b_datosDetalladoCuentaDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(DatosClientes.this,"mensaje ", Toast.LENGTH_SHORT).show();

                Intent inten = new Intent(DatosClientes.this, DetalleCuenta.class);
                inten.putExtra("ID", ID_EXTRA);
                startActivity(inten);

                /*
                Intent intent =  new Intent(DatosClientes.this, DetalleCuenta.class);
                intent.putExtra("ID", ID_EXTRA);
                startActivity(intent);
            */

            }
        });
    }

    private void cargarDatos() {
        // tv_datosClientes.setText(Sql_select(ID_EXTRA));

        String aux[] = Sql_select(ID_EXTRA).split(",");


        //  tv_datosClientes.setText( aux[0]);
        tv_aliasUsusario.setText(aux[1]);
        tv_nombresUsusario.setText(aux[0]);
        tv_detalleUsusario.setText(aux[2]);
        tv_idSectorUsusario.setText(aux[4]);
        tv_idOperadorMovilUsusario.setText(aux[5]);
        tv_telefonoUsusario.setText(aux[6]);
        tv_saldoUsusario.setText(procesos.valor(aux[8]));
        tv_estadoUsusario.setText(aux[7]);
        tv_fechaUsusario.setText(aux[9]);

    }

    private String Sql_select(int id_parametro) {
        String datos = "";
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT A.ID, A.NOMBRES, A.ALIAS, A.DETALLE, A.FOTO, A.ID_SECTOR, A.ID_OPERADORMOVIL, A.TELEFONO, A.ESTADO," +
                " B.SALDO, B.FECHA , OP.DETALLE, S.DETALLE FROM USUARIO A " +
                " LEFT JOIN CUENTA B ON A.ID = B.ID_USUARIO" +
                " LEFT JOIN SECTOR S ON A.ID_SECTOR = S.ID" +
                " LEFT JOIN OPERADORMOVIL OP ON A.ID_OPERADORMOVIL = OP.ID" +
                " WHERE A.ESTADO = 'ACTIVO' AND A.ID = " + id_parametro;

        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {

                datos = c.getString(1) + "," + c.getString(2) + "," + c.getString(3)
                        + "," + c.getString(4) + "," + c.getString(12) + "," + c.getString(11)
                        + "," + c.getString(7) + "," + c.getString(8) + "," + c.getFloat(9) + "," + c.getString(10);

            } while (c.moveToNext());
        }
        return datos;

    }


}
