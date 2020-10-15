package com.example.a141020note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {
    private final LayoutInflater inflater;
    private List<Note> notes;

    private static ClickListener clickListener;

    NoteListAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = inflater.inflate(R.layout.recyclerview_item,parent,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position){
        if (notes!=null){
            Note current = notes.get(position);
            holder.noteItemView.setText(current.getNote());
        }else{
            holder.noteItemView.setText("No Word");
        }
    }

    void setNotes(List<Note> note){
        notes = note;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if (notes!=null)
            return notes.size();
        else
            return 0;
    }

    public Note getNoteAtPosition(int position){
        return notes.get(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        private final TextView noteItemView;

        private NoteViewHolder(View itemView){
            super(itemView);
            noteItemView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener){
        NoteListAdapter.clickListener = clickListener;
    }

    public interface ClickListener{
        void onItemClick(View v, int position);
    }

}
