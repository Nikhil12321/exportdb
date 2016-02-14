package com.example.nikhil.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TableInfo extends AppCompatActivity {

    String table_name;
    public final static String shareable_table_name = "shared";
    String database = "database";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        Intent intent = getIntent();
        table_name = intent.getStringExtra(Home.shareable_table_name);
        setValues(table_name);
    }

    public void setValues(String table_name){
        SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        /*
            Cursor c is for getting the date of the last edit
            Cursor dbCursor is to get the column names of all the columns
            of the table.
         */
        String query = "SELECT * FROM TABLEINDEX";
        Cursor c = db.rawQuery(query, null);
        Cursor dbCursor = db.query(table_name, null, null, null, null, null, null);
        /*
            Save all the column names in the String array names_of_columns
         */
        String[] names_of_columns = dbCursor.getColumnNames();
        /*
            append in the string names_to_be_displayed.
         */
        String names_to_be_displayed="";
        for(int i=0; i<names_of_columns.length; i++){
            names_to_be_displayed+=names_of_columns[i]+", ";
        }
        /*
            Set the values of all the TextViews
         */
        TextView table_name_view = (TextView)findViewById(R.id.display_table_name);
        TextView table_name_date = (TextView)findViewById(R.id.display_table_date);
        TextView column_names = (TextView)findViewById(R.id.column_names);
        c.moveToFirst();
        table_name_date.setText("Created On: " + c.getString(1));
        table_name_view.setText(table_name);
        column_names.setText("Columns: "+names_to_be_displayed);
        c.close();
        /*
            Shiteee yeah
         */
    }
    /*
        Listeners for the buttons
     */
    public void view_table(View v){
        Intent intent = new Intent(getApplicationContext(), View_table.class);
        intent.putExtra(shareable_table_name, table_name);
        startActivity(intent);
    }
    public void insert_values(View v){
        Intent intent = new Intent(getApplicationContext(), EnterValue.class);
        intent.putExtra(shareable_table_name, table_name);
        startActivity(intent);
    }
    public void export_table(View v){

    }

}
