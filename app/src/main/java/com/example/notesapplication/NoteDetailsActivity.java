package com.example.notesapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapplication.Objects.NoteClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class NoteDetailsActivity extends AppCompatActivity {

    private EditText noteTitleEdt,noteContentEdt;
    private TextView noteTitleTxt;
    private String title, content, docId;
    boolean isEditMode = false;
    private ImageButton saveNoteBtn;
    private TextView deleteNoteTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        noteTitleEdt = findViewById(R.id.noteTitle);
        noteContentEdt  = findViewById(R.id.noteContent);
        saveNoteBtn = findViewById(R.id.save_note);
        deleteNoteTxt = findViewById(R.id.delete_note);
        noteTitleTxt = findViewById(R.id.page_title);

        //Recuperer les donnees a partir de l'adaptateur de vue
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        //Afficher les donnees
        noteTitleEdt.setText(title);
        noteContentEdt.setText(content);

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }
        if(isEditMode){
            deleteNoteTxt.setVisibility(View.VISIBLE);
            noteTitleTxt.setText("Edit your Note");
        }


        saveNoteBtn.setOnClickListener(V -> saveNote());
        deleteNoteTxt.setOnClickListener(V -> deleteNote());

    }
    void saveNote(){
        String noteTitle = noteTitleEdt.getText().toString();
        String noteContent = noteContentEdt.getText().toString();

        if(noteTitle.isEmpty()){
            noteTitleEdt.setError("Note Title is required");
        }else if(noteTitle.equals(title) && noteContent.equals(content)){
            finish();
            Toast.makeText(NoteDetailsActivity.this, "No changes affected", Toast.LENGTH_SHORT).show();
        }else {
            NoteClass note = new NoteClass(noteTitle, noteContent, Timestamp.now());
            SaveNoteToFirebase(note);
        }

    }

    void SaveNoteToFirebase(NoteClass note){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_NOTES");
        DocumentReference documentReference;
        if(isEditMode){
            documentReference = collectionReference.document(docId);
        }else {
            documentReference = collectionReference.document();//ID genere automatiquement
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    if(isEditMode){
                        Toast.makeText(NoteDetailsActivity.this, "Note edited successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(NoteDetailsActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else {
                    Toast.makeText(NoteDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void deleteNote(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_NOTES");

        DocumentReference documentReference = collectionReference.document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Toast.makeText(NoteDetailsActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(NoteDetailsActivity.this, "Failure while deleting Note", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}