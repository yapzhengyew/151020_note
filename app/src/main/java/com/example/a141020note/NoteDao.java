package com.example.a141020note;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table LIMIT 1")
    Note[] getAnyNote();

    @Update
    void update(Note... note);

    @Query("SELECT * FROM note_table WHERE note LIKE '%search%'")
    List<Note> getSearchNote();

}