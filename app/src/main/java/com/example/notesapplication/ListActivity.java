package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListActivity extends AppCompatActivity implements OnDialogCloseListener {

    RecyclerView listRecycler;
    TaskAdapter taskAdapter;
    FloatingActionButton addTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        addTask = findViewById(R.id.addTaskBtn);
        listRecycler = findViewById(R.id.recyclerView);
        listRecycler.setHasFixedSize(true);

        addTask.setOnClickListener(V -> AddTask.newInstance().show(getSupportFragmentManager() , AddTask.TAG));
        setUpRecyclerView();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(listRecycler);
    }

    void setUpRecyclerView(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_TO-DO_LIST");
        //QUERY THE DATABASE
        Query query = collectionReference.orderBy("timestamp" , Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TaskModel> options = new FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(query , TaskModel.class).build();
        taskAdapter.notifyDataSetChanged();
        listRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(options , this);
        listRecycler.setAdapter(taskAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        taskAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        setUpRecyclerView();
        taskAdapter.notifyDataSetChanged();

    }
}