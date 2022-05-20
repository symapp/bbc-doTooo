package ch.bbcag.dotooo;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

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

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat weekDayFormatter = new SimpleDateFormat("EEEE");

    private TaskRoomDao taskDao;
    private TaskAdapter taskAdapter;

    private boolean isFiltering = false;
    private ItemViewModel viewModel;

    private Color filter_color;
    private Date filter_date;
    private Boolean filter_onlyCompleted = Boolean.FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup dao
        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        taskDao = database.getTaskDao();

        initFloatingActionButton();
        initTaskList((ArrayList<Task>) taskDao.getAll(), true);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedColor().observe(this, item -> {

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setAddDay(!b);
            }
        });


        MenuItem filterButton = menu.findItem(R.id.filter);
        filterButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                toggleShowFilter();
                return false;
            }
        });

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

    private void toggleShowFilter() {
        Filter filterFragment = new Filter();
        FragmentManager fm = getSupportFragmentManager();

        isFiltering = !isFiltering;

        if (isFiltering) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container_view, filterFragment, null)
                    .commit();
            setAddDay(false);
        } else {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            setAddDay(true);
        }
    }

    public void setFilter_color(Color filter_color) {
        this.filter_color = filter_color;
        initTaskListWithAllAndNotAddDay();
    }

    public void setFilter_date(Date filter_date) {
        this.filter_date = filter_date;
        initTaskListWithAllAndNotAddDay();
    }

    public void setFilter_onlyCompleted(Boolean filter_onlyCompleted) {
        this.filter_onlyCompleted = filter_onlyCompleted;
        initTaskListWithAllAndNotAddDay();
    }

    private void filterTasks(String filterString) {
        taskAdapter.getFilter().filter(filterString);
    }

    private void initTaskListWithAllAndNotAddDay() {
        initTaskList((ArrayList<Task>) taskDao.getAll(), false);
    }

    private void initTaskList(ArrayList<Task> allTasks, boolean addDay) {

        if (isFiltering){
            // filter completed
            if (filter_onlyCompleted != null && !filter_onlyCompleted)
                allTasks.removeIf(Task::getDone);
            else if (filter_onlyCompleted != null) allTasks.removeIf(task -> !task.getDone());

            // filter color
            if (filter_color != null) {
                allTasks.removeIf(task -> !task.getColorName().equals(filter_color.getDisplayName()));
            }
        } else {
            allTasks.removeIf(Task::getDone);
        }

        // filter date
        if (filter_date != null) {
            allTasks.removeIf(task -> {
                Calendar taskDate = Calendar.getInstance();
                taskDate.setTime(task.getDate());
                Calendar filterDate = Calendar.getInstance();
                filterDate.setTime(filter_date);
                System.out.println(taskDate.get(Calendar.DAY_OF_YEAR));

                return !(taskDate.get(Calendar.DAY_OF_YEAR) == filterDate.get(Calendar.DAY_OF_YEAR) &&
                        taskDate.get(Calendar.YEAR) == filterDate.get(Calendar.YEAR));
            });
        }


        ArrayList<Task> TasksWithDay = new ArrayList<>(allTasks);
        if (addDay) TasksWithDay = getFormattedTaskListByDay(allTasks);

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

    public void setAddDay(boolean addDay) {
        initTaskList((ArrayList<Task>) taskDao.getAll(), addDay);
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
            tasks.add(new Task("?notTask!Other!", "", new Date(), Color.BLACK.getDisplayName()));
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