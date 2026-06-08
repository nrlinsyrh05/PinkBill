package com.example.billapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    EditText etMonth, etUnit, etRebate,
            etTotal, etFinal;

    Button btnUpdate, btnDelete;

    DataHelper dbHelper;

    int recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        etMonth = findViewById(R.id.etMonth);
        etUnit = findViewById(R.id.etUnit);
        etRebate = findViewById(R.id.etRebate);
        etTotal = findViewById(R.id.etTotal);
        etFinal = findViewById(R.id.etFinal);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DataHelper(this);

        recordID =
                getIntent().getIntExtra(
                        "recordID",0);

        displayRecord();

        btnUpdate.setOnClickListener(v -> updateRecord());

        btnDelete.setOnClickListener(v -> deleteRecord());
    }

    private void displayRecord(){

        SQLiteDatabase db =
                dbHelper.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM bill WHERE id=?",
                        new String[]{
                                String.valueOf(recordID)
                        });

        if(cursor.moveToFirst()){

            etMonth.setText(
                    cursor.getString(1));

            etUnit.setText(
                    cursor.getString(2));

            etTotal.setText(
                    cursor.getString(3));

            etRebate.setText(
                    cursor.getString(4));

            etFinal.setText(
                    cursor.getString(5));
        }
    }

    private void updateRecord(){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("month",
                etMonth.getText().toString());

        cv.put("unit",
                Integer.parseInt(
                        etUnit.getText().toString()));

        cv.put("totalCharge",
                Double.parseDouble(
                        etTotal.getText().toString()));

        cv.put("rebate",
                Double.parseDouble(
                        etRebate.getText().toString()));

        cv.put("finalCost",
                Double.parseDouble(
                        etFinal.getText().toString()));

        db.update(
                "bill",
                cv,
                "id=?",
                new String[]{
                        String.valueOf(recordID)
                });

        Toast.makeText(
                this,
                "Record Updated",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    private void deleteRecord(){

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        db.delete(
                "bill",
                "id=?",
                new String[]{
                        String.valueOf(recordID)
                });

        Toast.makeText(
                this,
                "Record Deleted",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}