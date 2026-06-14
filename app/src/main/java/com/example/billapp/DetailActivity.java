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

    private void updateRecord() {

        String month = etMonth.getText().toString();

        int unit = Integer.parseInt(
                etUnit.getText().toString());

        double rebate = Double.parseDouble(
                etRebate.getText().toString());

        // Recalculate Total Charge
        double totalCharge = calculateCharge(unit);

        // Recalculate Final Cost
        double finalCost =
                totalCharge - (totalCharge * rebate / 100);

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("month", month);
        cv.put("unit", unit);
        cv.put("totalCharge", totalCharge);
        cv.put("rebate", rebate);
        cv.put("finalCost", finalCost);

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

    private double calculateCharge(int unit) {

        double total = 0;

        if (unit <= 200) {

            total = unit * 0.218;

        } else if (unit <= 300) {

            total = (200 * 0.218)
                    + ((unit - 200) * 0.334);

        } else if (unit <= 600) {

            total = (200 * 0.218)
                    + (100 * 0.334)
                    + ((unit - 300) * 0.516);

        } else {

            total = (200 * 0.218)
                    + (100 * 0.334)
                    + (300 * 0.516)
                    + ((unit - 600) * 0.546);
        }

        return total;
    }
}