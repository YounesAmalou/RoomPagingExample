package com.yousentech.roompagingexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yousentech.roompagingexample.R;
import com.yousentech.roompagingexample.db.entity.TaskEntity;

public class TaskListAdapter extends PagingDataAdapter<TaskEntity, TaskListAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public TaskListAdapter() {
        super(new EntityDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskEntity currentItem = getItem(position);
        if (currentItem != null) {
            holder.bind(currentItem);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final CheckBox statusCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.titleTextView);
            statusCheckBox = itemView.findViewById(R.id.statusCheckBox);

            statusCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    TaskEntity task = getItem(position);
                    if (task != null) {
                        task.setTaskStatus(isChecked);
                        listener.checkBoxClicked(task);
                    }
                }
            });
        }

        public void bind(TaskEntity entity) {
            textViewName.setText(entity.getTaskName().substring(0, Math.min(50, entity.getTaskName().length())));
            statusCheckBox.setChecked(entity.getTaskStatus());
        }

    }

    private static class EntityDiffCallback extends DiffUtil.ItemCallback<TaskEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    }

    public TaskEntity getTaskEntity(int position) {
        return getItem(position);
    }

    public interface OnItemClickListener {
        void checkBoxClicked(TaskEntity task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
