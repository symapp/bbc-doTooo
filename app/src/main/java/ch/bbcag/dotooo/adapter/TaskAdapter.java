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

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_row_item, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Task currentTaskPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        assert currentTaskPosition != null;

        // set names
        TextView taskName = currentItemView.findViewById(R.id.task_name);
        taskName.setText(currentTaskPosition.getTitle());

        CardView cardView = currentItemView.findViewById(R.id.color_card);
        cardView.setCardBackgroundColor(Color.parseColor(currentTaskPosition.getColorHex()));

        // then return the recyclable view
        return currentItemView;
    }
}
