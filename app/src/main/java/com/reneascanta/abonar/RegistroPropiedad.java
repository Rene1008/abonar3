package com.reneascanta.abonar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class RegistroPropiedad extends AppCompatActivity {

    private NumeroSerial numeroSerial = new NumeroSerial();
    EditText et_serial;
    Button b_registrarSerial;
    String[] dato;

    public static String carpeta = "Abonar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_propiedad);

        et_serial = (EditText) findViewById(R.id.et_serial);
        b_registrarSerial = (Button) findViewById(R.id.b_registrarSerial);
        cargarRegistro();
        dato = datos();
        if (dato[5].equals("TRUE")) {
            if (dato[2].equals("")) {
                Intent intent = new Intent(RegistroPropiedad.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(RegistroPropiedad.this, Password.class);
                startActivity(intent);
            }
        } else {
            et_serial.setVisibility(View.VISIBLE);
            b_registrarSerial.setVisibility(View.VISIBLE);


            b_registrarSerial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dato[4].equals(et_serial.getText().toString())) {


                        registrarSerial();
                        Intent intent = new Intent(RegistroPropiedad.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegistroPropiedad.this, "EL CÒDIGO DEL SERIAL ES INCORRECTO.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



       // File f = new File(Environment.getExternalStorageDirectory()+carpeta);

        File f = new File(Environment.getExternalStorageDirectory() + carpeta);
// Comprobamos si la carpeta está ya creada

// Si la carpeta no está creada, la creamos.

        if(!f.isDirectory()) {
            String newFolder = carpeta; //cualquierCarpeta es el nombre de la Carpeta que vamos a crear
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File myNewFolder = new File(extStorageDirectory + newFolder);
            myNewFolder.mkdir(); //creamos la carpeta
        }else{

           // Log.d(Tag,"La carpeta ya estaba creada");
        }

    }

    private void cargarRegistro() {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        ContentValues c = new ContentValues();
        c.put("ID", 1000);
        c.put("NOMBRE", "");
        c.put("PASS", "");
        c.put("EMAIL", "");
        c.put("SERIAL", numeroSerial.getSerial());
        c.put("ESTADO", "FALSE");
        db.insert("PROPIETARIO", null, c);

        db.close();
    }

    private String[] datos() {
        String[] dato = new String[6];
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getReadableDatabase();

        String sql = "SELECT * FROM PROPIETARIO  ";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                dato[0] = String.valueOf(c.getInt(0));
                dato[1] = c.getString(1);
                dato[2] = c.getString(2);
                dato[3] = c.getString(3);
                dato[4] = c.getString(4);
                dato[5] = c.getString(5);
                break;

            } while (c.moveToNext());
        }

        return dato;
    }

    private void registrarSerial() {
        BaseHelper usuario = new BaseHelper(this, "abonar", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put("ESTADO", "TRUE");
            db.update("PROPIETARIO", values, "ID = " + 1000, null);
            db.close();

            Toast.makeText(this, "SE REGISTRO CORRECTAMENTE.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show();
        }
    }
}
