package com.example.billapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewRecordActivity extends AppCompatActivity {

    ListView listView;

    DataHelper dbHelper;

    ArrayList<String> list;
    ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        listView = findViewById(R.id.listView);

        dbHelper = new DataHelper(this);

        displayData();
    }

    private void displayData() {

        SQLiteDatabase db =
                dbHelper.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM bill",
                        null);

        list = new ArrayList<>();
        idList = new ArrayList<>();

        while(cursor.moveToNext()) {

            idList.add(
                    cursor.getInt(0));

            list.add(
                    cursor.getString(1)
                            + " - RM "
                            + String.format("%.2f",
                            cursor.getDouble(5)));
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Intent i =
                            new Intent(
                                    ViewRecordActivity.this,
                                    DetailActivity.class);

                    i.putExtra(
                            "recordID",
                            idList.get(position));

                    startActivity(i);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayData();
    }
}