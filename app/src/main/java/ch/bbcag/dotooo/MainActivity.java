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
        System.out.println("bimbim");
    }

    private void initTaskList() {
        ArrayList<Task> allTasks = (ArrayList<Task>) taskDao.getAll();

        ArrayList<Task> lateTasks = new ArrayList<>(allTasks);
        lateTasks.removeIf(task -> {
            Calendar taskDate = Calendar.getInstance();
            taskDate.setTime(DateUtils.truncate(task.getDate(), Calendar.DAY_OF_MONTH));
            Calendar nowDate = Calendar.getInstance();
            nowDate.setTime(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));

            System.out.println(task.getTitle());

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
            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            Task selected = (Task) parent.getItemAtPosition(position);

            intent.putExtra("taskId", selected.getId());
            intent.putExtra("taskTitle", selected.getTitle());
            intent.putExtra("taskDescription", selected.getDescription());
            intent.putExtra("taskDate", dateFormat.format(selected.getDate()));
            intent.putExtra("taskColorHex", selected.getColorHex());
            startActivity(intent);
        };

        ListView todayTasksList = (ListView) findViewById(R.id.list_today);
        TaskAdapter todayAdapter = new TaskAdapter(todayTasks, getApplicationContext());
        todayTasksList.setAdapter(todayAdapter);
        todayTasksList.setOnItemClickListener(mListClickHandler);

        ListView lateTasksList = (ListView) findViewById(R.id.list_late);
        TaskAdapter lateAdapter = new TaskAdapter(lateTasks, getApplicationContext());
        lateTasksList.setAdapter(lateAdapter);
        lateTasksList.setOnItemClickListener(mListClickHandler);

        ListView tomorrowTaskList = (ListView) findViewById(R.id.list_tomorrow);
        TaskAdapter tomorrowAdapter = new TaskAdapter(tomorrowTasks, getApplicationContext());
        tomorrowTaskList.setAdapter(tomorrowAdapter);
        tomorrowTaskList.setOnItemClickListener(mListClickHandler);
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