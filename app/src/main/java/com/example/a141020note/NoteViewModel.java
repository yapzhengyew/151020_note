package com.example.a141020note;

import android.app.Application;
import android.icu.text.IDNA;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.zip.InflaterOutputStream;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> allSearch;


    public NoteViewModel (Application application){
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    LiveData<List<Note>> getSearchNotes(String search){
        allSearch = repository.getSearchNote(search);
        return allSearch;
    }

    public void insert(Note note){
        repository.insert(note);
    }

    public void deleteAll(){ repository.deleteAll(); }

    public void deleteNote(Note note){ repository.deleteNote(note); }

    public void update(Note note){ repository.update(note);}


}
