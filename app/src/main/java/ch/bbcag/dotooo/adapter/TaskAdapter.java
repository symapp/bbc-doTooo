package ch.bbcag.dotooo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import ch.bbcag.dotooo.R;
import ch.bbcag.dotooo.entity.Task;

public class TaskAdapter extends BaseAdapter implements View.OnClickListener, Filterable {

    private ArrayList<Task> originalTasks;
    private ArrayList<Task> filteredTasks;
    private Context mContext;
    private TaskFilter mFilter = new TaskFilter();
    private LayoutInflater mInflater;

    public TaskAdapter(ArrayList<Task> tasks, Context context) {
        this.originalTasks = tasks;
        this.filteredTasks = tasks;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View v) {
        // onClick is set outside
    }

    @Override
    public int getCount() {
        return filteredTasks.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredTasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        Task currentTaskPosition = (Task) getItem(position);


        // set isTask
        // 1 -> task
        // 2 -> day
        // 3 -> empty group
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
                currentItemView = mInflater.inflate(R.layout.task_row_item, parent, false);
            } else if (isTask == 2) {
                currentItemView = mInflater.inflate(R.layout.day_row_item, parent, false);
                currentItemView.setEnabled(false);
            } else {
                currentItemView = mInflater.inflate(R.layout.no_task_row_item, parent, false);
                currentItemView.setEnabled(false);
            }

            currentItemView.setTag(isTask);
        }


        // set values
        if (isTask == 1) {
            TextView taskName = currentItemView.findViewById(R.id.task_name);
            if (taskName != null) {
                taskName.setText(currentTaskPosition.getTitle());
                if (currentTaskPosition.getDone()) {
                    taskName.setTextColor(Color.parseColor("#999999"));
                    currentItemView.setBackgroundColor(Color.parseColor("#fafafa"));
                }
            }


            CardView cardView = currentItemView.findViewById(R.id.color_card);
            if (cardView != null){
                cardView.setCardBackgroundColor(Color.parseColor(currentTaskPosition.getColorHex()));
                if (currentTaskPosition.getDone()) {
                    String color = currentTaskPosition.getColorHex();
                    color = color.charAt(0) + "50" + color.substring(1);
                    cardView.setCardBackgroundColor(Color.parseColor(color));
                    cardView.setElevation(0f);
                }
            }
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

    public Filter getFilter() {
        return mFilter;
    }

    private class TaskFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            String filterString = charSequence.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Task> list = originalTasks;

            int count = list.size();
            final ArrayList<Task> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                Task task = originalTasks.get(i);
                if (task.getTitle().charAt(0) != '?' && (task.getTitle().toLowerCase().contains(filterString) || task.getDescription().toLowerCase().contains(filterString))) {

                    System.out.println(task.getTitle() + " contains " + filterString + " : " + task.getTitle().toLowerCase().contains(filterString));
                    nlist.add(task);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredTasks = (ArrayList<Task>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}