package com.reneascanta.abonar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    EditText et_claveLogin;
    Button b_claveLogin;
    private String[] dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        et_claveLogin = (EditText) findViewById(R.id.et_claveLogin);
        b_claveLogin = (Button) findViewById(R.id.b_claveLogin);

        dato = datos();

        b_claveLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dato[2].equals(et_claveLogin.getText().toString())) {
                    Intent intent = new Intent(Password.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Password.this, "CLAVE INCORRECTA.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}
