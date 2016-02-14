package com.example.nikhil.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    public final static String send_columns = "columns";
    int columnCount = 0;
    int editTextId = 12321;
    int textViewId = 32123;
    int textSize = 20;
    //int tableNumber = 0;
    String databse = "database";
    Vector<Integer> list = new Vector<>();
    LinearLayout layout;
    String table_name;
    String shareable_intent_string = "shareable_table_name";
    String add_table_to_index_query = "CREATE TABLE IF NOT EXISTS TABLEINDEX(NAME VARCHAR UNIQUE, DATE VARCHAR);";
    String insert_table_into_index = "INSERT INTO TABLEINDEX(NAME, DATE) VALUES(";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewEditText();
            }
        });

        Intent intent = getIntent();
        table_name = intent.getStringExtra(shareable_intent_string);

    }

    public void createNewEditText(){

        final int id_editText = editTextId+columnCount;
        layout = (LinearLayout)findViewById(R.id.linearLayout);

        //Make new EditText
        final EditText editText = new EditText(this);
        layout.addView(editText);
        editText.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)editText.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        editText.setLayoutParams(layoutParams);
        editText.setId(id_editText);
        editText.setTextSize(textSize);

        //add to ArrayList
        list.add(id_editText);
        columnCount++;

        //Make new Remove Button
        final Button button = new Button(this);
        layout.addView(button);
        button.setText("Remove");
        button.setGravity(Gravity.LEFT);

        //Each button having its own listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //USING VIEW.GONE since INVISIBLE just makes it disappear, Gone also rearranges
                editText.setVisibility(View.GONE);
                //only good way to do it
                list.remove(list.indexOf(id_editText));
                button.setVisibility(View.GONE);
            }
        });
    }

    public String getDate(){

        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.createTable){

            //Create a snackbar
            layout = (LinearLayout)findViewById(R.id.linearLayout);
            EditText editText;

            //check whether there is atleast one field
            if(list.size()==0)
                return false;

            //Check whether none of the entries are null
            for(int i=0; i<list.size(); i++){
                editText = (EditText)findViewById(list.elementAt(i));
                if(editText.getText().toString().trim().length() == 0){

                    Snackbar.make(layout, "ERROR! Please enter all UNIQUE column names", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return false;

                }
                else if(!editText.getText().toString().matches("[a-zA-Z ]+")){
                    Snackbar.make(layout, "ERROR! No special characters!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return false;
                }
            }
            //if no error. Proceed to create Table
            String[] all_columns = new String[list.size()];
            SQLiteDatabase db = openOrCreateDatabase(databse, MODE_PRIVATE, null);
            String command = "CREATE TABLE IF NOT EXISTS "+table_name+"(";
            for(int i=0; i<list.size(); i++){
                editText = (EditText)findViewById(list.elementAt(i));
                all_columns[i] = editText.getText().toString();
                command+=editText.getText().toString();
                command+=" VARCHAR,";

            }

            //To remove the last comma
            command = command.substring(0, command.length()-1);
            command+=");";
            db.execSQL(command);
            Snackbar.make(layout, "Creating table. Please Wait", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            /*
                Create the index table
             */
            db.execSQL(add_table_to_index_query);
            /*
                Insert into the index table.
                You see i just realized. You need to insert values like VALUES('name', 'date');
                So the shitty '' are important, enclosing the values.
                WEEEEEEEEEIIIIIIRRDDD.
             */
            String second_command = insert_table_into_index+"'"+table_name+"'"+", "+"'"+getDate()+"'"+");";
            db.execSQL(second_command);
            //Start new activity
            Intent intent = new Intent(this, Home.class);
            intent.putExtra(send_columns, all_columns);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}


   /*

   //Create a new Text view over the editTexts. Just in case
    public void createNewTextView(){
        TextView textView = new TextView(this);
        textView.setText("Column "+columnCount);
        layout = (LinearLayout)findViewById(R.id.linearLayout);
        layout.addView(textView);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)textView.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        textView.setLayoutParams(layoutParams);
        textView.setId(textViewId + columnCount);
        textView.setTextSize(textSize);
        columnCount++;
    }
    */