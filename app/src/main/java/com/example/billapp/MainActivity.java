package com.example.billapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Spinner spMonth;
    EditText etUnit, etRebate;
    Button btnCalculate, btnSave, btnView, btnAbout;
    TextView tvResult;

    DataHelper dbHelper;

    double totalCharge = 0;
    double finalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spMonth = findViewById(R.id.spMonth);
        etUnit = findViewById(R.id.etUnit);
        etRebate = findViewById(R.id.etRebate);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);
        btnView = findViewById(R.id.btnView);
        btnAbout = findViewById(R.id.btnAbout);

        tvResult = findViewById(R.id.tvResult);

        dbHelper = new DataHelper(this);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.month_array,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spMonth.setAdapter(adapter);

        btnCalculate.setOnClickListener(v -> calculateBill());

        btnSave.setOnClickListener(v -> saveRecord());

        btnView.setOnClickListener(v -> {

            Intent i =
                    new Intent(
                            MainActivity.this,
                            ViewRecordActivity.class);

            startActivity(i);
        });

        btnAbout.setOnClickListener(v -> {

            Intent i =
                    new Intent(
                            MainActivity.this,
                            AboutActivity.class);

            startActivity(i);
        });
    }

    private void calculateBill() {

        if(etUnit.getText().toString().isEmpty()){

            etUnit.setError("Enter Unit");
            return;
        }

        if(etRebate.getText().toString().isEmpty()){

            etRebate.setError("Enter Rebate");
            return;
        }

        int unit =
                Integer.parseInt(
                        etUnit.getText().toString());

        double rebate =
                Double.parseDouble(
                        etRebate.getText().toString());

        if(unit < 1 || unit > 1000){

            etUnit.setError("Unit must be 1-1000");
            return;
        }

        if(rebate < 0 || rebate > 5){

            etRebate.setError("Rebate must be 0-5%");
            return;
        }

        totalCharge = calculateCharge(unit);

        finalCost =
                totalCharge -
                        (totalCharge * rebate / 100);

        tvResult.setText(
                "Total Charges : RM "
                        + String.format("%.2f", totalCharge)
                        + "\nFinal Cost : RM "
                        + String.format("%.2f", finalCost));
    }

    private double calculateCharge(int unit){

        double charge;

        if(unit <= 200){

            charge = unit * 0.218;
        }

        else if(unit <= 300){

            charge =
                    (200 * 0.218)
                            +
                            ((unit-200) * 0.334);
        }

        else if(unit <= 600){

            charge =
                    (200 * 0.218)
                            +
                            (100 * 0.334)
                            +
                            ((unit-300) * 0.516);
        }

        else{

            charge =
                    (200 * 0.218)
                            +
                            (100 * 0.334)
                            +
                            (300 * 0.516)
                            +
                            ((unit-600) * 0.546);
        }

        return charge;
    }

    private void saveRecord(){

        String month =
                spMonth.getSelectedItem().toString();

        int unit =
                Integer.parseInt(
                        etUnit.getText().toString());

        double rebate =
                Double.parseDouble(
                        etRebate.getText().toString());

        SQLiteDatabase db =
                dbHelper.getWritableDatabase();

        ContentValues cv =
                new ContentValues();

        cv.put("month", month);
        cv.put("unit", unit);
        cv.put("totalCharge", totalCharge);
        cv.put("rebate", rebate);
        cv.put("finalCost", finalCost);

        db.insert("bill",
                null,
                cv);

        Toast.makeText(
                this,
                "Record Saved",
                Toast.LENGTH_SHORT).show();
    }
}