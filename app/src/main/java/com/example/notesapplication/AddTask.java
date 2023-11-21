package com.example.notesapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapplication.Objects.TaskModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    private TextView setDueDate;
    private EditText taskEdt;
    private ImageButton saveBtn;
    private Context context;
    private String dueDate = "";

    public static AddTask newInstance(){
        return new AddTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_task , container , false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_tv);
        taskEdt = view.findViewById(R.id.task_edittext);
        saveBtn = view.findViewById(R.id.save_task);

        /*final Bundle bundle = getArguments();
        if(bundle!=null){
            String task = bundle.getString("task");
            // private Calendar date;
            String id = bundle.getString("id");
            String dueDateUpdate = bundle.getString("due");
            taskEdt.setText(task);
            setDueDate.setText(dueDateUpdate);
            if (task.length() > 0){
                saveBtn.setEnabled(false);
                saveBtn.setBackgroundColor(Color.GRAY);
            }
        }*/

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        setDueDate.setText(dayOfMonth + "/" + month + "/" + year);
                        dueDate = dayOfMonth + "/" + month +"/"+year;
                        //calendar.set(year,month,dayOfMonth);

                    }
                } , YEAR , MONTH , DAY);

                datePickerDialog.show();
            }
        });
        setDueDate.setText(dueDate);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
                passData();
            }
        });


    }


    void saveTask(){
        String task = taskEdt.getText().toString();

        if(task.isEmpty()){
            taskEdt.setError("Task can not be empty");
        }else{
            TaskModel taskModel = new TaskModel(task , dueDate , 0 , Timestamp.now());
            saveTaskToFirebase(taskModel);

        }
    }
    void saveTaskToFirebase(TaskModel taskModel){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_TO-DO_LIST");
        DocumentReference documentReference = collectionReference.document();
        documentReference.set(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        dismiss();
    }

    public void passData(){
        String task = taskEdt.getText().toString();
        Map<String , Object> taskMap = new HashMap<>();
        taskMap.put("TASK" , task);
        taskMap.put("DUE DATE" , dueDate);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        onBottomSheetDismissed();



    }
    private void onBottomSheetDismissed() {
        Activity activity = getActivity();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("NOTES")
                .document(currentUser.getUid())
                .collection("USER_TO-DO_LIST");

        Query updatedQuery = collectionReference.orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<TaskModel> updatedOptions = new FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(updatedQuery, TaskModel.class)
                .build();

        if (activity instanceof  OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onBottomSheetDismissed();

        }
    }

}