package com.example.a141020note;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Note.class}, version = 2, exportSchema = false)
public abstract class NoteRoomDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();
    private static NoteRoomDatabase INSTANCE;

    static NoteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "note_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final NoteDao dao;
        String[] notes = {"dolphin","crocodile","cobra"};

        PopulateDbAsync(NoteRoomDatabase db){
            dao = db.noteDao();
        }

        @Override
        protected  Void doInBackground(final Void... params){
            if (dao.getAnyNote().length < 1){
                for (int i = 0; i <= notes.length -1; i++){
                    Note note = new Note(notes[i]);
                    dao.insert(note);
                }
            }

            return null;
        }
    }
}