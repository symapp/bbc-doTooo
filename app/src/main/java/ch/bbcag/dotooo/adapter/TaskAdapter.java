package ch.bbcag.dotooo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ch.bbcag.dotooo.R;
import ch.bbcag.dotooo.TaskActivity;
import ch.bbcag.dotooo.entity.Task;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final ArrayList<Task> tasks;

    public class ViewHolderTask extends RecyclerView.ViewHolder {
        private final TextView title;
        private final CardView card;
        private final ConstraintLayout container;

        public ViewHolderTask(View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.task_name);
            card = itemView.findViewById(R.id.color_card);
            container = itemView.findViewById(R.id.task_container);

            itemView.setOnClickListener(view -> {
                int pos = getAbsoluteAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Task clickedTask = getTask(pos);

                    Intent intent = new Intent(context, TaskActivity.class);
                    intent.putExtra("taskId", clickedTask.getId());
                    intent.putExtra("taskTitle", clickedTask.getTitle());
                    intent.putExtra("taskDescription", clickedTask.getDescription());
                    @SuppressLint("SimpleDateFormat")
                    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    intent.putExtra("taskDate", dateFormatter.format(clickedTask.getDate()));
                    intent.putExtra("taskColorHex", clickedTask.getColorHex());
                    context.startActivity(intent);
                }
            });
        }
    }

    public static class ViewHolderDayHeader extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subtitle;

        public ViewHolderDayHeader(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.header_title);
            subtitle = itemView.findViewById(R.id.header_subtitle);
        }
    }

    public static class ViewHolderNoTasks extends RecyclerView.ViewHolder {
        public ViewHolderNoTasks(View itemView) {
            super(itemView);
        }
    }

    public TaskAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: return new ViewHolderTask(inflater.inflate(R.layout.task_row_item, parent, false), parent.getContext());
            case 2: return new ViewHolderDayHeader(inflater.inflate(R.layout.day_row_item, parent, false));
            default: return new ViewHolderNoTasks(inflater.inflate(R.layout.no_task_row_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 1 -> task
        // 2 -> day
        // 3 -> "No tasks"
        String title = getTask(position).getTitle();
        if (title.charAt(0) == '?') {
            if (title.startsWith("?notTask!No")) {
                return 3;
            } else {
                return 2;
            }
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Task task = getTask(position);
        switch (holder.getItemViewType()) {
            case 1:
                ViewHolderTask taskViewHolder = (ViewHolderTask) holder;
                taskViewHolder.title.setText(task.getTitle());
                taskViewHolder.card.setCardBackgroundColor(Color.parseColor(task.getColorHex()));

                if (task.getDone()) {
                    taskViewHolder.title.setTextColor(Color.parseColor("#999999"));
                    taskViewHolder.container.setBackgroundColor(Color.parseColor("#fafafa"));

                    String color = task.getColorHex();
                    color = color.charAt(0) + "50" + color.substring(1);
                    taskViewHolder.card.setCardBackgroundColor(Color.parseColor(color));
                    taskViewHolder.card.setElevation(0f);
                }
                return;
            case 2:
                ViewHolderDayHeader headerViewHolder = (ViewHolderDayHeader) holder;

                String title = task.getTitle().substring(1);
                String[] args = title.split("!");
                String headerTitle = args[1];
                String headerSubtitle = "";
                if (args.length > 2) {
                    headerSubtitle = args[2];
                }

                headerViewHolder.title.setText(headerTitle);
                headerViewHolder.subtitle.setText(headerSubtitle);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getTask(int i) {
        return tasks.get(i);
    }

    public void removeItem(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Task task, int position) {
        tasks.add(position, task);
        notifyItemInserted(position);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}