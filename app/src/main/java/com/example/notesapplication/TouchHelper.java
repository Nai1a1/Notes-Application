package com.example.notesapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.Adapters.TaskAdapter;
import com.example.notesapplication.Objects.TaskModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    //private final FirestoreRecyclerAdapter<TaskModel, RecyclerView.ViewHolder> adapter;
    TaskAdapter taskAdapter;

    public TouchHelper(@NonNull TaskAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.taskAdapter = adapter;

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(taskAdapter.getContext());
            builder.setMessage("Are You Sure?")
                    .setTitle("Delete Task")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            taskAdapter.deleteTask(position);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            taskAdapter.notifyItemChanged(position);

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
        }
    }
}
