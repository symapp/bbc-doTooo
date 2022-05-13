package ch.bbcag.dotooo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ch.bbcag.dotooo.dal.TaskRoomDao;
import ch.bbcag.dotooo.dal.TaskRoomDatabase;
import ch.bbcag.dotooo.entity.Task;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addExampleToDosToDatabase();
    }


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        addToDosToClickableList();

        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        TaskRoomDao taskDao= database.getTaskDao();

        

    }



    private void addExampleToDosToDatabase() {
        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        TaskRoomDao taskDao= database.getTaskDao();

        Task task1 = new Task();
        task1.setTitle("First Task");
        task1.setDescription("asdfasdfasdf");
        task1.setDate(new Date(2022 - 1900, 4, 12));
        taskDao.insert(task1);

        Task task2 = new Task();
        task2.setTitle("Second Task");
        task2.setDescription("Shessh what a task");
        task2.setDate(new Date(2022 - 1900, 4, 16));
        taskDao.insert(task2);
    }


    private void addToDosToClickableList() {
        ListView listView = findViewById(R.id.list);
        ArrayAdapter<Task> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

        TaskRoomDatabase database = TaskRoomDatabase.getInstance(getApplicationContext());
        TaskRoomDao taskDao= database.getTaskDao();

        arrayAdapter.addAll(taskDao.getAll());
        listView.setAdapter(arrayAdapter);


        AdapterView.OnItemClickListener mListClickHandler = (parent, v, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
            Task selected = (Task)parent.getItemAtPosition(position);

            intent.putExtra("taskId", selected.getId());
            intent.putExtra("taskTitle", selected.getTitle());
            intent.putExtra("taskDescription", selected.getDescription());
            startActivity(intent);
        };
        listView.setOnItemClickListener(mListClickHandler);
    }
}