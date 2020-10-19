package com.example.a141020note;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.method.HideReturnsTransformationMethod;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> searchNotes;

    NoteRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public LiveData<List<Note>> getSearchNote(String search){
        searchNotes = noteDao.getSearchNote(search);

        return searchNotes;
    }

//    private static NoteRoomDatabase noteRoomDatabase;
//    private static final Object LOCK = new Object();
//    private synchronized static NoteRoomDatabase getNotesDatabase(Context context) {
//        if (noteRoomDatabase == null){
//            synchronized (LOCK){
//                if (noteRoomDatabase == null){
//                    noteRoomDatabase = Room.databaseBuilder(context, NoteRoomDatabase.class, "note_database")
//                            .fallbackToDestructiveMigration()
//                            .addCallback(dbCallback).build();
//                }
//            }
//        }
//        return noteRoomDatabase;
//    }
//
//    public NoteDao getNotesDao(Context context){
//        return getNotesDatabase(context).noteDao();
//    }
//
//
//
//    public LiveData<List<Note>> getSearchNoteInfo(Context context, String query){
//        return getNotesDao(context).getSearchNote(query);
//    }
//
//    public Cursor getNotesCursor(Context context, String query){
//        return getNotesDao(context).getNotesCursor(query);
//    }
//
//    private static RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//        }
//
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//        }
//    };

    public void insert (Note note){
        new insertAsyncTask(noteDao).execute(note);
    }

    public void deleteAll(){ new deleteAllNotesAsyncTask(noteDao).execute();}

    public void deleteNote(Note note){ new deleteWordAsyncTask(noteDao).execute(note);}

    public void update(Note note){ new updateNoteAsyncTask(noteDao).execute(note);}

    //Insert a note into database
    private static class insertAsyncTask extends AsyncTask<Note, Void,Void>{
        private NoteDao asyncTaskDao;

        insertAsyncTask(NoteDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params){
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //delete all notes from the database
    private static class deleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao asyncTaskDao;

        deleteAllNotesAsyncTask(NoteDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids){
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    //delete a single note from database
    private static class deleteWordAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao asyncTaskDao;

        deleteWordAsyncTask(NoteDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params){
            asyncTaskDao.deleteNote(params[0]);
            return null;
        }
    }

    //Updates a word in the database
    private static class updateNoteAsyncTask extends AsyncTask<Note, Void, Void>{
        private NoteDao asyncTaskDao;

        updateNoteAsyncTask(NoteDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params){
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

}
