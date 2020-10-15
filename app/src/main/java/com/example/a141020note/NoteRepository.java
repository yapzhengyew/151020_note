package com.example.a141020note;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    NoteRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

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
