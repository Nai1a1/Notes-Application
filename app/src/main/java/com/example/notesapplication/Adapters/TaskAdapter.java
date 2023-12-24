package com.example.notesapplication.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.AddTask;
import com.example.notesapplication.ListActivity;
import com.example.notesapplication.Objects.TaskModel;
import com.example.notesapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TaskAdapter extends FirestoreRecyclerAdapter<TaskModel, TaskAdapter.TaskViewHolder> {
    //private List<TaskModel> taskList;
    Context context;
    public FirestoreRecyclerOptions<TaskModel> options;
    public ListActivity activity;



    public TaskAdapter(@NonNull FirestoreRecyclerOptions<TaskModel> options , ListActivity context) {
        super(options);
        this.context = context;
        activity = context;


    }

    public void updateOptions(FirestoreRecyclerOptions<TaskModel> options) {
        this.options = options;
    }


    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull TaskModel model) {
        holder.taskTxt.setText(model.getTask());
        if(!model.getDueDate().isEmpty()){
            holder.dueDateTxt.setText("Due on " + model.getDueDate());
        }else{
            holder.dueDateTxt.setText("");
        }
        holder.taskTxt.setChecked(toBoolean(model.getStatus()));

        String taskId = this.getSnapshots().getSnapshot(position).getId();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("NOTES").document(currentUser.getUid()).
                collection("USER_TO-DO_LIST");

        holder.taskTxt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    collectionReference.document(taskId).update("status" , 1);
                }else{
                    collectionReference.document(taskId).update("status" , 0);
                }
            }
        });
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


    public void deleteTask(int position){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_TO-DO_LIST");

        String taskId = this.getSnapshots().getSnapshot(position).getId();

        DocumentReference documentReference = collectionReference.document(taskId);
        documentReference.delete();
        notifyItemRemoved(position);
    }

    //OPEN FRAGMENT TO "EDIT" TASK
    public void editTask(int position){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_TO-DO_LIST");

        String taskId = this.getSnapshots().getSnapshot(position).getId();


        collectionReference.document(taskId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Document exists, handle the data
                            String task = documentSnapshot.getString("task");
                            String dueDate = documentSnapshot.getString("dueDate");
                            String dueTime = documentSnapshot.getString("dueTime");
                            //int status = documentSnapshot.getLong("status").intValue();

                            AddTask addNewTask = new AddTask();

                            // Do something with the data
                            Bundle bundle = new Bundle();
                            bundle.putString("task",task);
                            bundle.putString("dueDate",dueDate);
                            bundle.putString("dueTime",dueTime);
                            bundle.putString("taskId",taskId);
                            addNewTask.setArguments(bundle);
                            addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
                            // For example, update UI or process the data
                        } else {
                            // Document does not exist
                            // Handle the case accordingly
                        }
                    }
                });


    }

    public Context getContext(){
        return context;
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
