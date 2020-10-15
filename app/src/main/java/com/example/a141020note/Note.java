package com.example.a141020note;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "note")
    private String note;

    public Note(@NonNull String note){
        this.note = note;
    }

    @Ignore
    public Note(int id, @NonNull String note) {
        this.id = id;
        this.note = note;
    }

    public String getNote(){
        return this.note;
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }
}
