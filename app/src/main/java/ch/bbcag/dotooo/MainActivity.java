package ch.bbcag.dotooo;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.bbcag.dotooo.adapter.TaskAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat weekDayFormatter = new SimpleDateFormat("EEEE");

    private TaskRoomDao taskDao;

    TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup dao
        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        taskDao = database.getTaskDao();

        initFloatingActionButton();
        initTaskList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        filterTasks(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterTasks(s);
        return false;
    }

    private void filterTasks(String filterString) {
        taskAdapter.getFilter().filter(filterString);
    }

    private void initTaskList() {

        ArrayList<Task> allTasks = (ArrayList<Task>) taskDao.getAll();

        allTasks.removeIf(Task::getDone);

        ArrayList<Task> TasksWithDay = getFormattedTaskListByDay(allTasks);

        ListView listView = findViewById(R.id.task_list);
        taskAdapter = new TaskAdapter(TasksWithDay, getApplicationContext());
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Task selected = (Task) parent.getItemAtPosition(position);

            // return if it's not a task
            if (selected.getTitle().charAt(0) == '?') return;

            // intent
            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            intent.putExtra("taskId", selected.getId());
            intent.putExtra("taskTitle", selected.getTitle());
            intent.putExtra("taskDescription", selected.getDescription());
            intent.putExtra("taskDate", dateFormatter.format(selected.getDate()));
            intent.putExtra("taskColorHex", selected.getColorHex());
            startActivity(intent);
        });
    }

    private ArrayList<Task> getFormattedTaskListByDay(ArrayList<Task> allTasks) {

        // get weekdays
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        String dayOfTheWeekToday = weekDayFormatter.format(date.getTime()).toUpperCase();
        date.add(Calendar.DAY_OF_MONTH, 1);
        String dayOfTheWeekTomorrow = weekDayFormatter.format(date.getTime()).toUpperCase();

        // sort late
        ArrayList<Task> lateTasks = new ArrayList<>(allTasks);
        lateTasks.removeIf(task -> {
            Calendar taskDate = Calendar.getInstance();
            taskDate.setTime(DateUtils.truncate(task.getDate(), Calendar.DAY_OF_MONTH));
            Calendar nowDate = Calendar.getInstance();
            nowDate.setTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));

            return !taskDate.before(nowDate);
        });

        // sort today
        ArrayList<Task> todayTasks = new ArrayList<>(allTasks);
        todayTasks.removeIf(task -> !DateUtils.isSameDay(new Date(), task.getDate()));

        // sort tomorrow
        ArrayList<Task> tomorrowTasks = new ArrayList<>(allTasks);
        tomorrowTasks.removeIf(task -> {
            Calendar taskDate = Calendar.getInstance();
            taskDate.setTime(DateUtils.truncate(task.getDate(), Calendar.DAY_OF_MONTH));
            Calendar nowDate = Calendar.getInstance();
            nowDate.setTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));

            nowDate.add(Calendar.DAY_OF_MONTH, 1);

            return !taskDate.equals(nowDate);
        });

        // sort other
        ArrayList<Task> otherTasks = new ArrayList<>(allTasks);
        otherTasks.removeIf(task -> {
            Calendar taskDate = Calendar.getInstance();
            taskDate.setTime(DateUtils.truncate(task.getDate(), Calendar.DAY_OF_MONTH));
            Calendar nowDate = Calendar.getInstance();
            nowDate.setTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));

            nowDate.add(Calendar.DAY_OF_MONTH, 1);

            return !taskDate.after(nowDate);
        });

        ArrayList<Task> tasks = new ArrayList<>();

        // config late
        if (lateTasks.size() > 0) {
            tasks.add(new Task("?notTask!Late!", "", new Date(), Color.BLACK.getDisplayName()));
            tasks.addAll(lateTasks);
        }
        // config today
        tasks.add(new Task("?notTask!Today!" + dayOfTheWeekToday, "", new Date(), Color.BLACK.getDisplayName()));
        if (todayTasks.size() > 0) {
            tasks.addAll(todayTasks);
        } else {
            tasks.add(new Task("?notTask!No tasks today", "", new Date(), Color.BLACK.getDisplayName()));
        }
        // config tomorrow
        tasks.add(new Task("?notTask!Tomorrow!" + dayOfTheWeekTomorrow, "", new Date(), Color.BLACK.getDisplayName()));
        if (tomorrowTasks.size() > 0) {
            tasks.addAll(tomorrowTasks);
        } else {
            tasks.add(new Task("?notTask!No tasks tomorrow", "", new Date(), Color.BLACK.getDisplayName()));
        }
        // config other
        if (otherTasks.size() > 0) {
            tasks.add(new Task("?notTask!Other!" + dayOfTheWeekTomorrow, "", new Date(), Color.BLACK.getDisplayName()));
            tasks.addAll(otherTasks);
        }


        return tasks;
    }


    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
            startActivity(intent);
        });
    }
}