package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.BaseColumns;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtid;

    private EditText txtnombre;

    private EditText txtapellido;

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtid=findViewById(R.id.txtId);
        txtnombre=findViewById(R.id.txtNombre);
        txtapellido=findViewById(R.id.txtApellido);
    }

    public void Listar(View vista)
    {
        Intent listar = new Intent(this,Listado.class);
        startActivity(listar);
    }

    public void Guardar(View vista)
    {
        // Gers the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);

        Toast.makeText(getApplicationContext(), "se guardo el registro con clave: "+
                newRowId, Toast.LENGTH_LONG).show();
        db.close();
    }

    public void Buscar(View vista) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2 };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";;
        String[] selectionArgs = {txtid.getText().toString()};
        String sortOrder = FeedReaderContract.FeedEntry.column2 + " ASC";
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()) {
            String nombre=cursor.getString(cursor.getColumnIndexOrThrow((FeedReaderContract.FeedEntry.column1)));
            txtnombre.setText(nombre+"");
            String apellido=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            txtapellido.setText(apellido+"");

        }
        db.close();
    }

    public void Eliminar(View vista)  {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);
        db.close();
        Toast.makeText(getApplicationContext(), "Se eliminó " + deletedRows+" registro(s)",Toast.LENGTH_LONG).show();


    }

    public void Actualizar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};

        int count = db.update((FeedReaderContract.FeedEntry.nameTable), values, selection, selectionArgs);
        Toast.makeText(getApplicationContext(), "Se actualizó " + count+" registro(s)",Toast.LENGTH_LONG).show();
        db.close();

    }

}


