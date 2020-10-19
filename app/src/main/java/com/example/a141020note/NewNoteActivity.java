package com.example.a141020note;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.a141020note.MainActivity.EXTRA_DATA_ID;
import static com.example.a141020note.MainActivity.EXTRA_DATA_UPDATE_NOTE;

public class NewNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.a141020note.REPLY";
    public static final String EXTRA_REPLY_ID = "com.example.a141020note.REPLY_ID";

    private EditText editNote;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editNote = findViewById(R.id.edit_note);
        int id = -1;

        final Bundle extras = getIntent().getExtras();

        if (extras != null){
            String note = extras.getString(EXTRA_DATA_UPDATE_NOTE,"");
            if (!note.isEmpty()){
                editNote.setText(note);
                editNote.setSelection(note.length());
                editNote.requestFocus();
            }
        }

        //set title
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_DATA_UPDATE_NOTE)){
            setTitle("Edit Note");
        } else {
            setTitle("Add Note");
        }


        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editNote.getText())){
                    setResult(RESULT_CANCELED,replyIntent);
                }else{
                    String note = editNote.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, note);

                    if (extras != null && extras.containsKey(EXTRA_DATA_ID)){
                        int id = extras.getInt(EXTRA_DATA_ID,-1);
                        if (id != -1){
                            replyIntent.putExtra(EXTRA_REPLY_ID,id);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();

            }
        });
    }
}
