package com.example.notesapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class NoteAdapter extends FirestoreRecyclerAdapter<NoteClass,NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<NoteClass> options , Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteClass model) {
        holder.titleTxt.setText(model.noteTitle);
        holder.contentTxt.setText(model.noteContent);
        holder.timestampTxt.setText(new SimpleDateFormat("dd/MM/yyyy").format(model.timestamp.toDate()));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",model.noteTitle);
            intent.putExtra("content",model.noteContent);
            //Returns:
            //the backing snapshot array
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    //Passer la vue cree en-dessous
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_recycler_view,parent,false);
        return new NoteViewHolder(view);
    }

    //Creer une classe qui fait un lien entre l'adaptateur de vue et le recyclerView
    class  NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt,contentTxt,timestampTxt;
//Constructor
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.note_title);
            contentTxt = itemView.findViewById(R.id.note_content);
            timestampTxt = itemView.findViewById(R.id.note_time);
        }
    }
}
