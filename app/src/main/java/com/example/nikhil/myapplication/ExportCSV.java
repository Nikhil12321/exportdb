package com.example.nikhil.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;

public class ExportCSV extends AppCompatActivity {

    String table_name;
    String database = "database";
    File exportDir;
    File file;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_csv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        table_name = intent.getStringExtra(TableInfo.shareable_table_name);
        File dbFile = getDatabasePath("database.db");
        exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if(!exportDir.exists()){
            exportDir.mkdir();
        }
        file = new File(exportDir, table_name+".csv");
        try{
            file.createNewFile();
            CSVWriter csvWriter = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = openOrCreateDatabase(database, MODE_PRIVATE, null);
            String query = "SELECT * FROM "+table_name;
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            csvWriter.writeNext(c.getColumnNames());
            int number_of_columns = c.getColumnNames().length;
            while(!c.isAfterLast()){
                String[] arrStr = new String[number_of_columns];
                for(int i=0; i<number_of_columns; i++){
                    arrStr[i] = c.getString(i);
                }
                csvWriter.writeNext(arrStr);
                c.moveToNext();
            }
            //makeToast("Done!");
            tv = (TextView)findViewById(R.id.done_text_view);
            tv.setText("Done");
            csvWriter.close();
            c.close();
            startExport();

        }
        catch(Exception e){
            makeToast("Error in exporting");
        }
    }
    public void makeToast(String message){
        Toast.makeText(getApplicationContext(),message,
                Toast.LENGTH_SHORT).show();
    }
    /*
        Apparently for no reason at all Environment thing doesn't work here
        meeeh
        What the hell

     */
    public void startExport(){
        String fileLocation = "/mnt/sdcard/"+table_name+".csv";
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileLocation));
        startActivity(Intent.createChooser(emailIntent, "Send Email"));

    }

    public void export_again(View v){
        startExport();
    }
    /*
        String filelocation="/mnt/sdcard/contacts_sid.vcf";
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"asd@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, filelocation);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
     */

}
