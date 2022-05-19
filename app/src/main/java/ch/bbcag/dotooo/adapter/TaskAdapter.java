package ch.bbcag.dotooo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import ch.bbcag.dotooo.R;
import ch.bbcag.dotooo.entity.Task;

public class TaskAdapter extends ArrayAdapter<Task> implements View.OnClickListener {

    private ArrayList<Task> tasks;
    Context mContext;

    public TaskAdapter(ArrayList<Task> tasks, Context context) {
        super(context, R.layout.task_row_item, tasks);
        this.tasks = tasks;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        // onClick is set outside
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        Task currentTaskPosition = getItem(position);


        // set isTask

        int isTask = 1;
        if (currentTaskPosition.getTitle().charAt(0) == '?') {
            if (currentTaskPosition.getTitle().startsWith("?notTask!No")) {
                isTask = 3;
            } else {
                isTask = 2;
            }
        }






        // inflate
        if (currentItemView == null || convertView.getTag() != null) {
            if (isTask == 1) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_row_item, parent, false);
            } else if (isTask == 2) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.day_row_item, parent, false);
                currentItemView.setEnabled(false);
            } else {
                System.out.println(isTask);
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.no_task_row_item, parent, false);
                currentItemView.setEnabled(false);
            }

            currentItemView.setTag(isTask);
        }


        // set values
        if (isTask == 1) {
            TextView taskName = currentItemView.findViewById(R.id.task_name);
            if (taskName != null) taskName.setText(currentTaskPosition.getTitle());

            CardView cardView = currentItemView.findViewById(R.id.color_card);
            if (cardView != null) cardView.setCardBackgroundColor(Color.parseColor(currentTaskPosition.getColorHex()));
        } else if (isTask == 2) {
            // get args
            String title = currentTaskPosition.getTitle().substring(1);
            String[] args = title.split("!");
            String headerTitle = args[1];
            String headerSubtitle = "";
            if (args.length > 2) {
                headerSubtitle = args[2];
            }

            TextView headerTitleTextView = currentItemView.findViewById(R.id.header_title);
            if (headerTitleTextView != null) headerTitleTextView.setText(headerTitle);

            TextView headerSubtitleTextView = currentItemView.findViewById(R.id.header_subtitle);
            if (headerSubtitleTextView != null) headerSubtitleTextView.setText((headerSubtitle));
        }


        return currentItemView;
    }
}