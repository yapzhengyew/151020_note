package com.example.a141020note;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel (Application application){
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public void insert(Note note){
        repository.insert(note);
    }

    public void deleteAll(){ repository.deleteAll(); }

    public void deleteNote(Note note){ repository.deleteNote(note); }

    public void update(Note note){ repository.update(note);}
}
