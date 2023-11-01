package com.example.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

public class ListActivity extends AppCompatActivity {

    RecyclerView listRecycler;
    FloatingActionButton addTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        addTask = findViewById(R.id.addTaskBtn);
        listRecycler = findViewById(R.id.recyclerView);
// TO CHANGE TO ADD NEW TASK
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask.newInstance().show(getSupportFragmentManager() , AddTask.TAG);
            }
        });
    }
    //void setupListRecycler(){
    //    listRecycler.setLayoutManager(new LinearLayoutManager(this));

    //}
}