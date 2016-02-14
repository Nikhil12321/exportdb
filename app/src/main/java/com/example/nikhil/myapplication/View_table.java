package com.example.nikhil.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

public class View_table extends AppCompatActivity {

    String database = "database";
    String table_name;
    TableRow tr;
    TableLayout tl;
    int header_text_size = 20;
    int header_left_padding = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
            Necessary crap like initializing tableLayout, getting table name,
            getting all column names for headers
            and finally calling two function to set the headers & values to the table
         */
        tl = (TableLayout)findViewById(R.id.display_table_layout);
        Intent intent = getIntent();
        table_name = intent.getStringExtra(TableInfo.shareable_table_name);
        SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        Cursor dbCursor = db.query(table_name, null, null, null, null, null, null);
        String[] column_names = dbCursor.getColumnNames();
        setHeaders(column_names);
        setData(column_names.length);
    }

    public void setHeaders(String[] column_names){
        /*
            Make a row for headers
         */
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        for(int i=0; i<column_names.length; i++) {
            TextView tv = new TextView(this);
            tv.setText(column_names[i]);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(header_text_size);
            tv.setPadding(header_left_padding, 0, 0, 0);
            tr.addView(tv);
        }
        tl.addView(tr);

        /*
            Make a row for dividers
         */
        tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        for(int i=0; i<column_names.length; i++){
            TextView divider = new TextView(this);
            divider.setText("________________");
            divider.setTextColor(Color.BLACK);
            divider.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            divider.setPadding(5, 0, 0, 0);
            tr.addView(divider);
        }
        tl.addView(tr);
    }

    public void setData(int length){
        //String q = "INSERT INTO "+table_name+" VALUES('1', '2', '3');";
        SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        //db.execSQL(q);
        String query = "SELECT * FROM "+table_name;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            for(int i=0; i<length; i++){
                TextView tv = new TextView(this);
                tv.setText(c.getString(i));
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(header_text_size-5);
                tv.setPadding(header_left_padding, 0, 0, 0);
                tr.addView(tv);
            }
            tl.addView(tr);
            c.moveToNext();
        }
    }

}
