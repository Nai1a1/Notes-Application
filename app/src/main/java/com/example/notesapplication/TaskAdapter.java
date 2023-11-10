package com.example.notesapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskAdapter extends FirestoreRecyclerAdapter<TaskModel , TaskAdapter.TaskViewHolder> {
    Context context;
    String taskId;
    FirebaseUser currentUser;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private  ListActivity activity;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<TaskModel> options , ListActivity context) {
        super(options);
        this.activity = context;

    }
    Context getContext(){
        return this.context;
    }


    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull TaskModel model) {
        holder.taskTxt.setText(model.getTask());
        holder.dueDateTxt.setText("Due on" + model.getDueDate());
        holder.taskTxt.setChecked(toBoolean(model.getStatus()));
        //En cliquent sur le task
        taskId = this.getSnapshots().getSnapshot(position).getId();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = firestore.collection("NOTES").document(currentUser.getUid()).
                collection("USER_TO-DO_LIST");

        /*holder.taskTxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    collectionReference.document(model.taskID).update("status" , 1);
                }else{
                    collectionReference.document(model.taskID).update("status" , 0);
                }
            }
        });
            Bundle bundle = new Bundle();
            bundle.putString("task",model.getTask());
            bundle.putString("dueDate",model.getDueDate());
            //Returns:
            //the backing snapshot array
            String taskId = this.getSnapshots().getSnapshot(position).getId();
            bundle.putString("taskId",taskId);
            AddTask addNewTask = new AddTask();
            addNewTask.setArguments(bundle);*/
    }
    boolean toBoolean(int b){
        return b != 0;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_recycler_view,parent,false);
        return new TaskAdapter.TaskViewHolder(view);

    }
    void deleteTask(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_TO-DO_LIST");

        DocumentReference documentReference = collectionReference.document(taskId);
        documentReference.delete();
    }

    void editTask(){
        AddTask addTask = new AddTask();
        addTask.show(activity.getSupportFragmentManager() , addTask.getTag());
    }


    static class TaskViewHolder extends RecyclerView.ViewHolder{
        CheckBox taskTxt;
        TextView dueDateTxt;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTxt = itemView.findViewById(R.id.checkbox);
            dueDateTxt = itemView.findViewById(R.id.task_time);



        }

    }
}
