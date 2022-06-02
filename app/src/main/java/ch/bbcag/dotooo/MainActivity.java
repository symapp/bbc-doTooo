package ch.bbcag.dotooo;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.bbcag.dotooo.adapter.TaskAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;
import ch.bbcag.dotooo.helper.swipeCallback;
import ch.bbcag.dotooo.viewmodel.FilterViewModel;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat weekDayFormatter = new SimpleDateFormat("EEEE");

    private TaskRoomDao taskDao;
    private TaskAdapter taskAdapter;

    private boolean isFilterOpen = false;
    private FilterViewModel viewModel;

    private Color filter_color = null;
    private Date filter_date = null;
    private Boolean filter_onlyUncompleted = Boolean.TRUE;
    private String searchQuery = "";

    private AlertDialog.Builder errorDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup dao
        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        taskDao = database.getTaskDao();

        // setup errorDialogBuilder
        errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder.setTitle("Error").setPositiveButton(R.string.ok, null);

        initFloatingActionButton();
        initViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // searchView
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);

        // filter
        MenuItem filterButton = menu.findItem(R.id.filter);
        filterButton.setOnMenuItemClickListener(menuItem -> {
            toggleShowFilter();
            return false;
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAllTasks();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        viewModel.setSelectedSearchQuery(s);
        loadAllTasks();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        viewModel.setSelectedSearchQuery(s);
        loadAllTasks();
        return false;
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        viewModel.setSelectedColor(filter_color);
        viewModel.setSelectedDate(filter_date);
        viewModel.setSelectedOnlyUncompleted(filter_onlyUncompleted);

        viewModel.getSelectedColor().observe(this, color -> {
            this.filter_color = color;
            loadAllTasks();
        });

        viewModel.getSelectedDate().observe(this, date -> {
            this.filter_date = date;
            loadAllTasks();
        });

        viewModel.getSelectedOnlyUncompleted().observe(this, b -> {
            this.filter_onlyUncompleted = b;
            loadAllTasks();
        });

        viewModel.getSelectedSearchQuery().observe(this, query -> {
            this.searchQuery = query;
            loadAllTasks();
        });
    }

    private void toggleShowFilter() {
        FilterFragment filterFragment = new FilterFragment();
        FragmentManager fm = getSupportFragmentManager();

        isFilterOpen = !isFilterOpen;

        if (isFilterOpen) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container_view, filterFragment, null)
                    .commit();
        } else {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        }
    }

    private void loadAllTasks() {
        try {
            initTaskList((ArrayList<Task>) taskDao.getAll());
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't load tasks... Try again later.").create().show();
        }
    }

    private void initTaskList(ArrayList<Task> allTasks) {

        // filter completed
        if (filter_onlyUncompleted != null && filter_onlyUncompleted)
            allTasks.removeIf(Task::getDone);
        else if (filter_onlyUncompleted != null) allTasks.removeIf(task -> !task.getDone());

        // filter color
        if (filter_color != null) {
            allTasks.removeIf(task -> !task.getColorName().equals(filter_color.getDisplayName()));
        }

        // filter date
        if (filter_date != null) {
            allTasks.removeIf(task -> {
                Calendar taskDate = Calendar.getInstance();
                taskDate.setTime(task.getDate());
                Calendar filterDate = Calendar.getInstance();
                filterDate.setTime(filter_date);

                return !(taskDate.get(Calendar.DAY_OF_YEAR) == filterDate.get(Calendar.DAY_OF_YEAR) &&
                        taskDate.get(Calendar.YEAR) == filterDate.get(Calendar.YEAR));
            });
        }

        // filter by search query
        if (!searchQuery.trim().equals("")) {
            allTasks.removeIf(task -> {
                if (task.getTitle().charAt(0) != '?' && (task.getTitle().toLowerCase().contains(searchQuery) || task.getDescription().toLowerCase().contains(searchQuery))) {
                    return false;
                } else return task.getTitle().charAt(0) != '?';
            });
        }

        ArrayList<Task> tasksWithDay = getFormattedTaskListByDay(allTasks);

        RecyclerView recyclerView = findViewById(R.id.task_list);
        taskAdapter = new TaskAdapter(tasksWithDay);
        recyclerView.setAdapter(taskAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeCallback swipeToDeleteCallback = new swipeCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAbsoluteAdapterPosition();
                final Task task = taskAdapter.getTasks().get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    redirectToEdit(task);
                    taskAdapter.notifyItemChanged(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    task.setDone(!task.getDone());

                    try {
                        TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().update(task);
                    } catch (Exception e) {
                        errorDialogBuilder.setMessage("Couldn't update task... Try again later.").create().show();
                    }

                    loadAllTasks();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void redirectToEdit(Task task) {
        try {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            Task selected = TaskRoomDatabase.getInstance(getApplicationContext()).getTaskDao().getById(task.getId());
            intent.putExtra("taskId", selected.getId());
            startActivity(intent);
        } catch (Exception e) {
            errorDialogBuilder.setMessage("Couldn't load task... Try again later.").create().show();
        }

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