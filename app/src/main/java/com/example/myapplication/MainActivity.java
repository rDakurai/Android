package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText txtid;
    private TextInputEditText txtnombre;
    private TextInputEditText txtapellido;

    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new FeedReaderDbHelper(this);

        txtid = findViewById(R.id.txtId);
        txtnombre = findViewById(R.id.txtNombre);
        txtapellido = findViewById(R.id.txtApellido);
    }

    // --- MÉTODOS onClick ---
    // Estos métodos ahora usan los nombres correctos de tu FeedReaderContract

    public void Listar(View vista) {
        Intent listar = new Intent(this, Listado.class);
        startActivity(listar);
    }

    public void Guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String nombre = txtnombre.getText().toString().trim();
        String apellido = txtapellido.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Nombre y Apellido no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        // CORREGIDO: Usando column1 y column2
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        // CORREGIDO: Usando nameTable
        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);

        Toast.makeText(getApplicationContext(), "Se guardó el registro con clave: " + newRowId, Toast.LENGTH_LONG).show();
        limpiarCampos();
        db.close();
    }

    public void Buscar(View vista) {
        String idBusqueda = txtid.getText().toString().trim();
        if (idBusqueda.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un ID para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                // CORREGIDO: Usando column1 y column2
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idBusqueda };

        Cursor cursor = db.query(
                // CORREGIDO: Usando nameTable
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // CORREGIDO: Usando column1 y column2
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
        } else {
            Toast.makeText(this, "No se encontró el registro con ID: " + idBusqueda, Toast.LENGTH_SHORT).show();
            limpiarCampos();
        }
        cursor.close();
        db.close();
    }

    public void Eliminar(View vista) {
        String idEliminar = txtid.getText().toString().trim();
        if (idEliminar.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un ID para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idEliminar };
        // CORREGIDO: Usando nameTable
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);

        if (deletedRows > 0) {
            Toast.makeText(getApplicationContext(), "Se eliminó " + deletedRows + " registro(s)", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No se encontró el registro para eliminar", Toast.LENGTH_SHORT).show();
        }
        limpiarCampos();
        db.close();
    }

    public void Actualizar(View vista) {
        String idActualizar = txtid.getText().toString().trim();
        if (idActualizar.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un ID para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String nombre = txtnombre.getText().toString().trim();
        String apellido = txtapellido.getText().toString().trim();
        ContentValues values = new ContentValues();
        // CORREGIDO: Usando column1 y column2
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idActualizar };

        // CORREGIDO: Usando nameTable
        int count = db.update(FeedReaderContract.FeedEntry.nameTable, values, selection, selectionArgs);

        if (count > 0) {
            Toast.makeText(getApplicationContext(), "Se actualizó " + count + " registro(s)", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No se encontró el registro para actualizar", Toast.LENGTH_SHORT).show();
        }
        limpiarCampos();
        db.close();
    }

    private void limpiarCampos() {
        txtid.setText("");
        txtnombre.setText("");
        txtapellido.setText("");
    }
}