package com.yousentech.roompagingexample.ui.allTasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yousentech.roompagingexample.R;
import com.yousentech.roompagingexample.adapter.TaskListAdapter;
import com.yousentech.roompagingexample.databinding.FragmentAllTasksBinding;
import com.yousentech.roompagingexample.db.entity.TaskEntity;

public class AllTasksFragment extends Fragment {

    private FragmentAllTasksBinding binding;
    private TaskListAdapter taskListAdapter;
    private AllTasksViewModel allTasksViewModel;
    private Button addTaskButton;
    private EditText taskEditText;
    private EditText searchTaskEditText;
    private RecyclerView taskListRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allTasksViewModel = new ViewModelProvider(this).get(AllTasksViewModel.class);

        binding = FragmentAllTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Binding Views
        taskEditText = binding.addTaskInput;
        searchTaskEditText = binding.searchTaskInput;
        addTaskButton = binding.addTaskButton;
        taskListRecyclerView = binding.taskList;

        //Setting up the Adapter
        taskListAdapter = new TaskListAdapter();
        allTasksViewModel.getTaskList().observe(getViewLifecycleOwner(), taskEntityPagingData ->
                taskListAdapter.submitData(getLifecycle(), taskEntityPagingData));


        //Setting up the RecyclerView
        taskListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskListRecyclerView.setAdapter(taskListAdapter);

        //Setting up the Add Task Button
        addTaskButton.setOnClickListener(v -> {
            String taskName = taskEditText.getText().toString();
            if (!taskName.equals("") && !allTasksViewModel.taskExists(taskName)) {
                allTasksViewModel.insert(new TaskEntity(taskName, false));
                taskEditText.setText("");
            } else {
                Toast.makeText(getContext(), "Task already exists", Toast.LENGTH_SHORT).show();
            }
        });

        //Setting up the Swipe to Delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    allTasksViewModel.deleteTask(taskListAdapter.getTaskEntity(viewHolder.getBindingAdapterPosition()));
                }
            }
        }).attachToRecyclerView(taskListRecyclerView);

        //Setting up the check status Update
        taskListAdapter.setOnItemClickListener(taskEntity -> allTasksViewModel.updateTask(taskEntity));

        //Setting up the Menu
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.frag_menu_options, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_all_tasks) {
                    allTasksViewModel.deleteAllTasks();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        //Setting up the Search
        searchTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allTasksViewModel.getTaskList(charSequence.toString()).observe(getViewLifecycleOwner(), taskEntityPagingData ->
                        taskListAdapter.submitData(getLifecycle(), taskEntityPagingData));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}