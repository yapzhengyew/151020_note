package com.example.a141020note;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_NOTE = "extra_note_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    private NoteViewModel noteViewModel;

    private NoteListAdapter adapter;

    private SearchView searchView;
    //private ListView listView;
    //private NoteRepository noteRepository = new NoteRepository();

    private ColorDrawable swipeBackground = new ColorDrawable(Color.parseColor("#FF5656"));
    private Drawable deleteIcon;
//    public MainActivity() {
//
//    }
//    public MainActivity(NoteListAdapter adapter) {
//        this.adapter = adapter;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //listView = findViewById(R.id.search_results_list);

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
        adapter = new NoteListAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
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
        deleteIcon = ContextCompat.getDrawable(this,R.drawable.ic_delete);

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
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){

                        new AlertDialog.Builder(viewHolder.itemView.getContext())
                                .setMessage("Are you sure to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int id){

                                        int position = viewHolder.getAdapterPosition();
                                        Note myNote = adapter.getNoteAtPosition(position);
                                        Toast.makeText(MainActivity.this,"Deleting "+
                                                myNote.getNote(), Toast.LENGTH_LONG).show();

                                        noteViewModel.deleteNote(myNote);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //cancel to delete
                                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    }
                                })
                                .create()
                                .show();


                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        View itemView = viewHolder.itemView;

                        int iconMargin = (itemView.getHeight()-deleteIcon.getIntrinsicHeight()) / 2 - 30;

                        if (dX > 0){
                            swipeBackground.setBounds(itemView.getLeft(), itemView.getTop()+30, (int) dX, itemView.getBottom());
                            deleteIcon.setBounds(itemView.getLeft() + iconMargin -10,itemView.getTop()+iconMargin+30,
                                    itemView.getLeft()+iconMargin+deleteIcon.getIntrinsicWidth()+10,itemView.getBottom()-iconMargin);
                        } else {
                            swipeBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop()+30, itemView.getRight(), itemView.getBottom());
                            deleteIcon.setBounds(itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth() -10 ,itemView.getTop()+iconMargin+30,
                                    itemView.getRight()-iconMargin+10,itemView.getBottom()-iconMargin);

                        }

                        swipeBackground.draw(c);

                        c.save();

                        if (dX > 0){
                            c.clipRect(itemView.getLeft(), itemView.getTop()+30, (int) dX, itemView.getBottom());
                        } else {
                            c.clipRect(itemView.getRight() + (int) dX, itemView.getTop()+30, itemView.getRight(), itemView.getBottom());
                        }
                        deleteIcon.draw(c);
                        c.restore();

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                });

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
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        //Search
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(onQueryTextListener);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                noteViewModel.getSearchNotes(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                //adapter.getFilter().filter(s);


                //noteViewModel.getSearchNotes(s);


                if(s.length() <= 0){
                    noteViewModel = ViewModelProviders.of(MainActivity.this).get(NoteViewModel.class);

                    noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
                        @Override
                        public void onChanged(@Nullable List<Note> notes) {
                            adapter.setNotes(notes);
                        }
                    });
                } else {
                    noteViewModel = ViewModelProviders.of(MainActivity.this).get(NoteViewModel.class);

                    noteViewModel.getSearchNotes(s).observe(MainActivity.this, new Observer<List<Note>>() {
                        @Override
                        public void onChanged(@Nullable List<Note> notes) {
                            adapter.setNotes(notes);
                        }
                    });
                }


                //if (s == NULL) 


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        NoteRoomDatabase.getDatabase(getApplicationContext())
//                                .noteDao()
//                                .getSearchNote(s);
//
//                        //Log(info+ searchList.toString());
//
//                    }
//                }).start();

//                noteViewModel.getSearchNotes(s).observe(MainActivity.this, new Observer<List<Note>>() {
//                    @Override
//                    public void onChanged(@Nullable List<Note> notes) {
//                        adapter.setNotes(notes);
//                    }
//                });

                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);

    }

//    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String s) {
//            getNotesFromDb(s);
//            return true;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String s) {
//            getNotesFromDb(s);
//            return true;
//        }
//
//        private void getNotesFromDb(String searchText){
//            searchText = "%"+searchText+"%";
//            noteRepository.getSearchNoteInfo(MainActivity.this,searchText)
//                    .observe(MainActivity.this, new Observer<List<Note>>(){
//                        @Override
//                        public void onChanged(@Nullable List<Note> notes) {
//                            if (notes == null){
//                                return;
//                            }
//                            NoteListAdapter adapter = new NoteListAdapter(MainActivity.this);
//                            listView.setAdapter((ListAdapter) adapter);
//                        }
//                    });
//        }
//    };

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