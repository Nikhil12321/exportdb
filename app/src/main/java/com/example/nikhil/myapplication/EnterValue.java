package com.example.nikhil.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class EnterValue extends AppCompatActivity {

    LinearLayout linearLayout;
    String table_name;
    String database = "database";
    int columnCount = 0;
    int textViewId = 1232;
    int editTextID = 3212;
    int textSize = 20;
    Vector<Integer> list = new Vector<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_value);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_enter_value);
        Intent intent = getIntent();
        table_name = intent.getStringExtra(TableInfo.shareable_table_name);
        db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        Cursor c = db.query(table_name, null, null, null, null, null, null);
        final String[] column_names = c.getColumnNames();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmpty()){
                    insertValues(column_names.length);
                    Snackbar.make(view, "Done!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    Snackbar.make(view, "No entries should be empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });

        //get the columns of the table

        createFields(column_names);
    }

    //function to create text areas and editText fields
    public void createFields(String[] columns){

        for (String column : columns) {

            createTextField(column);
            createEditText(column);
        }

    }

    public void createTextField(String name_of_column){


        final TextView textView = new TextView(this);
        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_enter_value);
        linearLayout.addView(textView);
        textView.setText(name_of_column);
        textView.setGravity(Gravity.START);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        textView.setLayoutParams(layoutParams);
        //textView.setId(textViewId + columnCount);
        textView.setTextSize(textSize);
        textView.setPadding(0, 20, 0, 0);
        //columnCount++;
    }

    public void createEditText(String name_of_column){

        final EditText editText = new EditText(this);
        linearLayout = (LinearLayout)findViewById(R.id.linear_layout_enter_value);
        int id = editTextID+columnCount;
        linearLayout.addView(editText);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)editText.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        editText.setLayoutParams(layoutParams);
        list.add(id);
        editText.setId(id);
        columnCount++;
    }

    public boolean checkEmpty(){
        EditText et;
        for(int i=0; i<columnCount; i++){
            et = (EditText)findViewById(editTextID+i);
            if(et.getText().toString().trim().length() == 0){
                return false;

            }
        }
        return true;
    }

    public void insertValues(int length){
        String q = "INSERT INTO "+table_name+" VALUES(";
        EditText et;
        for(int i=0; i<length; i++){
            et = (EditText)findViewById(editTextID+i);
            q+="'"+et.getText().toString()+"',";
        }
        q = q.substring(0, q.length()-1);
        q+=");";
        db.execSQL(q);
    }
}
