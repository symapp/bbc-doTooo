package ch.bbcag.dotooo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.commons.lang3.time.DateUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ch.bbcag.dotooo.adapter.TaskAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Color;
import ch.bbcag.dotooo.entity.Task;

public class MainActivity extends AppCompatActivity {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    TaskRoomDao taskDao;

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

    private void initTaskList() {
        ArrayList<Task> allTasks = (ArrayList<Task>) taskDao.getAll();

        ArrayList<Task> lateTasks = new ArrayList<>(allTasks);
        lateTasks.removeIf(task -> {
            Calendar taskDate = Calendar.getInstance();
            taskDate.setTime(DateUtils.truncate(task.getDate(), Calendar.DAY_OF_MONTH));
            Calendar nowDate = Calendar.getInstance();
            nowDate.setTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));

            return !taskDate.before(nowDate);
        });

        ArrayList<Task> todayTasks = new ArrayList<>(allTasks);
        todayTasks.removeIf(task -> !DateUtils.isSameDay(new Date(), task.getDate()));

        ArrayList<Task> tomorrowTasks = new ArrayList<>(allTasks);
        tomorrowTasks.removeIf(task -> {
            Calendar taskDate = Calendar.getInstance();
            taskDate.setTime(DateUtils.truncate(task.getDate(), Calendar.DAY_OF_MONTH));
            Calendar nowDate = Calendar.getInstance();
            nowDate.setTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));

            nowDate.add(Calendar.DAY_OF_MONTH, 1);

            return !taskDate.equals(nowDate);
        });


        AdapterView.OnItemClickListener mListClickHandler = (parent, v, position, id) -> {
            Task selected = (Task) parent.getItemAtPosition(position);

            if (selected.getTitle().charAt(0) == '?') return;

            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            intent.putExtra("taskId", selected.getId());
            intent.putExtra("taskTitle", selected.getTitle());
            intent.putExtra("taskDescription", selected.getDescription());
            intent.putExtra("taskDate", dateFormat.format(selected.getDate()));
            intent.putExtra("taskColorHex", selected.getColorHex());
            startActivity(intent);
        };


        String dayOfTheWeekToday = new SimpleDateFormat("EEEE").format(new Date()).toUpperCase();
        String dayOfTheWeekTomorrow = new SimpleDateFormat("EEEE").format(new Date()).toUpperCase();

        ArrayList<Task> tasks = new ArrayList<>();

        if (lateTasks.size() > 0) {
            tasks.add(new Task("?notTask!Late!", "", new Date(), Color.BLACK.getDisplayName()));
            tasks.addAll(lateTasks);
        }

        tasks.add(new Task("?notTask!Today!" + dayOfTheWeekToday, "", new Date(), Color.BLACK.getDisplayName()));
        if (todayTasks.size() > 0) {
            tasks.addAll(todayTasks);
        } else {
            tasks.add(new Task("?notTask!noTasks", "", new Date(), Color.BLACK.getDisplayName()));
        }

        tasks.add(new Task("?notTask!Tomorrow!" + dayOfTheWeekTomorrow, "", new Date(), Color.BLACK.getDisplayName()));
        if (tomorrowTasks.size() > 0) {
            tasks.addAll(tomorrowTasks);
        } else {
            tasks.add(new Task("?notTask!No Tasks", "", new Date(), Color.BLACK.getDisplayName()));
        }

        ListView listView = (ListView) findViewById(R.id.task_list);
        TaskAdapter adapter = new TaskAdapter(tasks, getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mListClickHandler);




//        ListView todayTasksList = (ListView) findViewById(R.id.list_today);
//        TaskAdapter todayAdapter = new TaskAdapter(todayTasks, getApplicationContext());
//        todayTasksList.setAdapter(todayAdapter);
//        todayTasksList.setOnItemClickListener(mListClickHandler);
//
//        ListView lateTasksList = (ListView) findViewById(R.id.list_late);
//        TaskAdapter lateAdapter = new TaskAdapter(lateTasks, getApplicationContext());
//        lateTasksList.setAdapter(lateAdapter);
//        lateTasksList.setOnItemClickListener(mListClickHandler);
//
//        ListView tomorrowTaskList = (ListView) findViewById(R.id.list_tomorrow);
//        TaskAdapter tomorrowAdapter = new TaskAdapter(tomorrowTasks, getApplicationContext());
//        tomorrowTaskList.setAdapter(tomorrowAdapter);
//        tomorrowTaskList.setOnItemClickListener(mListClickHandler);
    }


    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                startActivity(intent);
            }
        });
    }
}