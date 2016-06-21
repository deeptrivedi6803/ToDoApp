package com.deepjtrivedi.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    Button btnAddItem;
    EditText etEditText;
    String editedText; //Edited Text from EditItem Activity
    int intentposition; //Position of the Edited Text from EditItem Activity

    private final int REQUEST_CODE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(MainActivity.this, "Item removed", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //List Item Click to invoke EditItem Activity
       lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

               String selectedFromList = (String) lvItems.getItemAtPosition(position);
               Intent i = new Intent(MainActivity.this, EditItemActivity.class);
               i.putExtra("senttext", selectedFromList);
               i.putExtra("position", position);
               startActivityForResult(i, REQUEST_CODE);
           }
       });

        //Edit Text Reference
        etEditText = (EditText) findViewById(R.id.etEditText);

        //Add Button onClick
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aToDoAdapter.add(etEditText.getText().toString());
                etEditText.setText("");
                Toast.makeText(MainActivity.this, "New item added", Toast.LENGTH_LONG).show();
                writeItems();
            }
        });
    }

    public void populateArrayItems(){
        todoItems = new ArrayList<String>();
        readItems();
        aToDoAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems );
    }

    //Read items from File
    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e){

        }
    }

    //Write items to File
    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(file, todoItems);
        }catch (IOException e){

        }
    }

    //Invoked after second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent save){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE)
        {
            editedText = save.getExtras().getString("savedText");
            intentposition = save.getExtras().getInt("position");
            todoItems.set(intentposition, editedText);
            writeItems();
            aToDoAdapter.notifyDataSetChanged();  //Notification to array adapter to populate changed text
            Toast.makeText(MainActivity.this, "Item edited and saved successfully", Toast.LENGTH_LONG).show();
        }

    }

}
