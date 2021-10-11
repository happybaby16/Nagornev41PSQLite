package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.view.View;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear;
    EditText etName, etCountry, etTaste, etPrice;


    DBHelper dbHelper;

    SQLiteDatabase database;

    ContentValues contentValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);



        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etTaste = (EditText) findViewById(R.id.etTaste);
        etPrice = (EditText) findViewById(R.id.etPrice);


        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();

    }



    public  void  UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_TEA, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int countryIndex = cursor.getColumnIndex(DBHelper.KEY_COUNTRY);
            int tasteIndex = cursor.getColumnIndex(DBHelper.KEY_TASTE);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);

            TableLayout dbOutput = findViewById(R.id.dbtable);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight =1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight =1.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputCountry = new TextView(this);
                params.weight =1.0f;
                outputCountry.setLayoutParams(params);
                outputCountry.setText(cursor.getString(countryIndex));
                dbOutputRow.addView(outputCountry);

                TextView outputTaste = new TextView(this);
                params.weight =1.0f;
                outputTaste.setLayoutParams(params);
                outputTaste.setText(cursor.getString(tasteIndex));
                dbOutputRow.addView(outputTaste);

                TextView outputPrice = new TextView(this);
                params.weight =1.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputPrice);

                Button btnDelete = new Button(this);
                btnDelete.setOnClickListener(this);
                params.weight = 1.0f;
                btnDelete.setLayoutParams(params);
                btnDelete.setText("Удалить запись");
                btnDelete.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btnDelete);

                dbOutput.addView(dbOutputRow);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }




    @Override
    public void onClick(View v) {

        database = dbHelper.getWritableDatabase();
        switch (v.getId()) {

            case R.id.btnAdd:

                String name = etName.getText().toString();
                String country = etCountry.getText().toString();
                String taste = etTaste.getText().toString();
                String price = etPrice.getText().toString();


                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_COUNTRY, country);
                contentValues.put(DBHelper.KEY_TASTE, taste);
                contentValues.put(DBHelper.KEY_PRICE, price);

                database.insert(DBHelper.TABLE_TEA, null, contentValues);

                etName.setText("");
                etCountry.setText("");
                etTaste.setText("");
                etPrice.setText("");
                UpdateTable();
                break;



            case R.id.btnClear:
                database.delete(DBHelper.TABLE_TEA, null, null);
                UpdateTable();
                break;

            default:
                database.delete(DBHelper.TABLE_TEA,DBHelper.KEY_ID +" = ?", new String[]{String.valueOf(v.getId())});
                UpdateTable();

                contentValues = new ContentValues();

                Cursor cursorUdater = database.query(DBHelper.TABLE_TEA, null, null, null, null, null, null);
                if (cursorUdater.moveToFirst()) {
                    int idIndex = cursorUdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUdater.getColumnIndex(DBHelper.KEY_NAME);
                    int countryIndex = cursorUdater.getColumnIndex(DBHelper.KEY_COUNTRY);
                    int tasteIndex = cursorUdater.getColumnIndex(DBHelper.KEY_TASTE);
                    int priceIndex = cursorUdater.getColumnIndex(DBHelper.KEY_PRICE);
                    int realId = 1;
                    do{
                        if(cursorUdater.getInt(idIndex)>realId);
                        {
                            contentValues.put(DBHelper.KEY_ID, realId);
                            contentValues.put(DBHelper.KEY_NAME, cursorUdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_COUNTRY, cursorUdater.getString(countryIndex));
                            contentValues.put(DBHelper.KEY_TASTE, cursorUdater.getString(tasteIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUdater.getString(priceIndex));
                            database.replace(DBHelper.TABLE_TEA, null, contentValues);

                        }
                        realId++;
                    }while (cursorUdater.moveToNext());
                    if(cursorUdater.moveToLast())
                    {
                        database.delete(DBHelper.TABLE_TEA, DBHelper.KEY_ID + " = ?", new String[]{cursorUdater.getString(idIndex)});
                    }
                }
                cursorUdater.close();
                UpdateTable();
                break;
        }
        dbHelper.close();
    }
}