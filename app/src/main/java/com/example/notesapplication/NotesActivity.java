package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {

    RecyclerView noteRecycler;
    NoteAdapter noteAdapter;


    FloatingActionButton addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        addNote = findViewById(R.id.addNoteBtn);
        noteRecycler = findViewById(R.id.recyclerView);

        addNote.setOnClickListener(V -> startActivity(new Intent(NotesActivity.this , NoteDetailsActivity.class)));
        setupNoteRecycler();
    }



    void setupNoteRecycler(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_NOTES");
        //QUERY THE DATABASE
        Query query = collectionReference.orderBy("timestamp" , Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteClass> options = new FirestoreRecyclerOptions.Builder<NoteClass>()
                .setQuery(query , NoteClass.class).build();
        noteRecycler.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options , this);
        noteRecycler.setAdapter(noteAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}