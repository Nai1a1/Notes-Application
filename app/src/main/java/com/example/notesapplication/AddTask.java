package com.example.notesapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notesapplication.Notification.NotificationHelper;
import com.example.notesapplication.Objects.TaskModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";
    private TextView setDueDate, setDueTime;
    private EditText taskEdt;
    private ImageButton saveBtn;
    private Context context;
    private String taskUpdate;
    private String dueDate = "";
    private String dueTime = "";

    private Boolean isEditMode = false;
    private Boolean isUpdate = false;
    private String taskId;


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
        setDueTime = view.findViewById(R.id.set_time_tv);
        taskEdt = view.findViewById(R.id.task_edittext);
        saveBtn = view.findViewById(R.id.save_task);


        final Bundle bundle = getArguments();
        if(bundle!=null){
            isEditMode = true;
            taskUpdate = bundle.getString("task");
            taskId = bundle.getString("taskId");
            dueDate = bundle.getString("dueDate");
            dueTime = bundle.getString("dueTime");
            taskEdt.setText(taskUpdate);
            setDueDate.setText(dueDate);
            setDueTime.setText(dueTime);

        }

        setDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                setDueTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                dueTime = String.format("%02d:%02d", hourOfDay, minute);
                            }
                        },
                        hour,
                        minute,
                        android.text.format.DateFormat.is24HourFormat(context)
                );

                timePickerDialog.show();
            }
        });

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
            }
        });


    }


    void saveTask() {
        String task = taskEdt.getText().toString();
// verification including both editMode and !editMode
        if(task.isEmpty()){
            taskEdt.setError("Task can not be empty");
        }else{
            TaskModel taskModel = new TaskModel(task , dueDate , dueTime , 0 , Timestamp.now());
            saveTaskToFirebase(taskModel);

        }
    }
    void saveTaskToFirebase(TaskModel taskModel){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("NOTES")
                .document(currentUser.getUid()).collection("USER_TO-DO_LIST");
        //long delayMillis = calculateDelayMillis(dueDate, dueTime);



        if(isEditMode){
            collectionReference.document(taskId).update("dueTime" , dueTime)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                isUpdate = true;
                            }
                        }
                    });
            collectionReference.document(taskId).update("task" , taskEdt.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                isUpdate = true;
                            }
                        }
                    });
            collectionReference.document(taskId).update("dueDate" , dueDate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                isUpdate = true;
                            }else {
                                isUpdate = false; 
                            }
                        }
                    });
            if(isUpdate){
                Toast.makeText(context, "Task edited successfully", Toast.LENGTH_SHORT).show();
            }
            long delayMillis = calculateDelayMillis();
            NotificationHelper.scheduleNotification(context, delayMillis, taskUpdate);

        }else {
            collectionReference.document().set(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            long delayMillis = calculateDelayMillis();
            NotificationHelper.scheduleNotification(context, delayMillis, taskUpdate);

        }


        dismiss();
        }
        private long calculateDelayMillis() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            // Get the current time
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();
            String dueDateTimeString = dueDate + " " + dueTime;

            // Parse the combined string to get a Date object
            Date dueDateTime = null;
            try {
                dueDateTime = dateFormat.parse(dueDateTimeString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            return dueDateTime.getTime() - currentTime.getTime();


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