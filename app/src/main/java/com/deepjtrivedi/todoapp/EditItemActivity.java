package com.deepjtrivedi.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText  etMultiLineEditText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String selectedFromList = getIntent().getStringExtra("senttext");
        final int receivedposition = getIntent().getExtras().getInt("position", 0); //gets position of list item to be edited
        etMultiLineEditText = (EditText) findViewById(R.id.etMultiLineEditText);
        etMultiLineEditText.setText(selectedFromList);
        etMultiLineEditText.setSelection(etMultiLineEditText.length());  //sends the cursor to end of text
        etMultiLineEditText.requestFocus(); //sets the focus to this edit text

        //Button Save On Click Listener
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent save = new Intent();
                save.putExtra("savedText", etMultiLineEditText.getText().toString());
                save.putExtra("position", receivedposition);
                setResult(RESULT_OK, save);
                finish();
            }
        });
    }

}
