package ch.bbcag.dotooo;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;

import ch.bbcag.dotooo.adapter.TaskAdapter;
import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Task;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatingActionButton();
        addToDosToClickableList();
    }

    private void addToDosToClickableList() {
        ListView listView = findViewById(R.id.list);

        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        TaskRoomDao taskDao = database.getTaskDao();

        TaskAdapter arrayAdapter = new TaskAdapter((ArrayList<Task>) taskDao.getUnclompetedTasks(), getApplicationContext());
        listView.setAdapter(arrayAdapter);

        // setup click listener
        AdapterView.OnItemClickListener mListClickHandler = (parent, v, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            Task selected = (Task) parent.getItemAtPosition(position);

            intent.putExtra("taskId", selected.getId());
            startActivity(intent);
        };
        listView.setOnItemClickListener(mListClickHandler);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
            startActivity(intent);
        });
    }
}