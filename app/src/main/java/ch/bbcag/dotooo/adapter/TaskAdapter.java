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

        // set isTask
        boolean isTask = true;
        Task currentTaskPosition = getItem(position);
        if (currentTaskPosition == null) {
            assert currentItemView != null;
            return currentItemView;
        }
        if (currentTaskPosition.getTitle().charAt(0) == '?') isTask = false;


        // inflate
        if (currentItemView == null || isTask != (boolean) convertView.getTag()) {
            if (isTask) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.task_row_item, parent, false);


            } else {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.day_row_item, parent, false);
                currentItemView.setEnabled(false);

                if (currentTaskPosition.getTitle().equals("?notTask!No Tasks")){
                    int pixels = (int) (48 * mContext.getResources().getDisplayMetrics().density);
                    currentItemView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, pixels));

                    TextView headerTitle = currentItemView.findViewById(R.id.header_title);
                    headerTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    headerTitle.setTextSize(pixels/6);
                    headerTitle.setTextColor(Color.parseColor("#aaaaaa"));

                }

            }

            currentItemView.setTag(isTask);
        }


        // set values
        if (isTask) {
            TextView taskName = currentItemView.findViewById(R.id.task_name);
            if (taskName != null) taskName.setText(currentTaskPosition.getTitle());

            CardView cardView = currentItemView.findViewById(R.id.color_card);
            if (cardView != null) cardView.setCardBackgroundColor(Color.parseColor(currentTaskPosition.getColorHex()));
        } else {
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