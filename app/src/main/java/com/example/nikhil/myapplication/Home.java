package com.example.nikhil.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Vector;

import it.gmariotti.cardslib.library.internal.Card;

public class Home extends AppCompatActivity {

    //SQL command to fetch all table names to a cursor
    String command = "select name from sqlite_master where type='table'";
    String create_table_command = "CREATE TABLE IF NOT EXISTS ";
    String database = "database";
    String dialog_message = "Create New Table";
    String new_table_name;
    public static final String shareable_table_name = "shareable_table_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.home_linear_layout);
        SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        Cursor c = db.rawQuery(command, null);
        Vector<String> table_names = new Vector<>();

        //Collect table names from database
        c.moveToNext();
        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                table_names.add(c.getString(0));
                //Toast.makeText(getApplicationContext(), c.getString(0), Toast.LENGTH_SHORT).show();
                c.moveToNext();
            }
        }

        //Create Table Buttons
        if(table_names.size() > 0){

            for(int i=1; i<table_names.size(); i++){
                createButtons(table_names.elementAt(i));
            }
        }

    }
    

    public void createButtons(String table_name){

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.home_linear_layout);
            final Button button = new Button(this);
            linearLayout.addView(button);
            button.setText(table_name);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)button.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            button.setLayoutParams(layoutParams);
            final Intent intent = new Intent(this, TableInfo.class);
            //intent.putExtra();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra(shareable_table_name, button.getText());
                    startActivity(intent);
                }
            });
        }


    public boolean doesTableExist(String table_name){

        SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+table_name+"'", null);
        if(c!=null) {
            if(c.getCount()>0) {
                c.close();
                return true;
            }
            c.close();
        }
        return false;
    }

    public void showSnackBar(String message){
        LinearLayout ll = (LinearLayout)findViewById(R.id.home_linear_layout);
        Snackbar.make(ll, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    public void showDialog(){

        final SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        /* add_table_dialog is the separate layout defined for the dialog

         */
        View promptView = layoutInflater.inflate(R.layout.add_table_dialog, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(promptView);
        alertBuilder.setMessage(dialog_message)
                .setTitle(dialog_message + "title");
        /* add_table_edit_text is the editText field of the dialog

         */
        final EditText input = (EditText)promptView.findViewById(R.id.add_table_edit_text);

        alertBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new_table_name = input.getText().toString();
                        if (doesTableExist(new_table_name)) {
                            dialog.cancel();
                            showSnackBar("Error! Table name already exists");
                            dialog.cancel();
                        } else {
                            //db.execSQL(command);
                            //showSnackBar("Creating table. Please Wait");
                            //Start new activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra(shareable_table_name, new_table_name);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menuhome, menu);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.add_table){

            //Toast.makeText(getApplicationContext(), "yeahh", Toast.LENGTH_SHORT).show();
            showDialog();
        }
        return true;
        }

    }




