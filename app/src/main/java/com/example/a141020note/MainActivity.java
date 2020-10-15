package com.example.a141020note;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_NOTE = "extra_note_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        //Recycler View
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NoteListAdapter adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //NoteViewModel
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        //Swipe to delete
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target){
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){
                        int position = viewHolder.getAdapterPosition();
                        Note myNote = adapter.getNoteAtPosition(position);
                        Toast.makeText(MainActivity.this,"Deleting "+
                                myNote.getNote(), Toast.LENGTH_LONG).show();

                        noteViewModel.deleteNote(myNote);

                    }
                }
        );

        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Note note = adapter.getNoteAtPosition(position);
                launchUpdateNoteActivity(note);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_data) {
            Toast.makeText(this, "Clearing the data...",
                    Toast.LENGTH_SHORT).show();

            noteViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(NewNoteActivity.EXTRA_REPLY));
            noteViewModel.insert(note);
        } else if (requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String note_data = data.getStringExtra(NewNoteActivity.EXTRA_REPLY);
            int id = data.getIntExtra(NewNoteActivity.EXTRA_REPLY_ID,-1);

            if (id != -1){
                noteViewModel.update(new Note(id, note_data));
            } else {
                Toast.makeText(this, R.string.unable_to_update,
                        Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(
                    getApplicationContext(), R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void launchUpdateNoteActivity(Note note) {
        Intent intent = new Intent(this, NewNoteActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_NOTE, note.getNote());
        intent.putExtra(EXTRA_DATA_ID, note.getId());
        startActivityForResult(intent, UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
    }
}