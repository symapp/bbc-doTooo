package ch.bbcag.dotooo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

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

        // get the position of the view from the ArrayAdapter
        Task currentTaskPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        assert currentTaskPosition != null;


        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            if (currentTaskPosition.getTitle().charAt(0) == '?') {
                // get args
                String title = currentTaskPosition.getTitle().substring(1);
                String[] args = title.split("!");
                String headerTitle = args[1];
                String headerSubtitle = "";
                if (args.length > 2) {
                        headerSubtitle = args[2];
                }

                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.day_row_item, parent, false);
                currentItemView.setEnabled(false);

                // set text, etc
                TextView headerTitleTextView = currentItemView.findViewById(R.id.header_title);
                headerTitleTextView.setText(headerTitle);

                TextView headerSubtitleTextView = currentItemView.findViewById(R.id.header_subtitle);
                headerSubtitleTextView.setText((headerSubtitle));

            } else {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_row_item, parent, false);

                // set text, etc
                TextView taskName = currentItemView.findViewById(R.id.task_name);
                taskName.setText(currentTaskPosition.getTitle());

                CardView cardView = currentItemView.findViewById(R.id.color_card);
                cardView.setCardBackgroundColor(Color.parseColor(currentTaskPosition.getColorHex()));

            }
        }


        // then return the recyclable view
        return currentItemView;
    }
}